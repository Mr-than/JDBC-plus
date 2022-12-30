package jdbc.data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GetQuery implements Result{

    private final Connection connection;

    public GetQuery(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Object getResult(String sql, Object[] args, Method method,List<String> order) {
        PreparedStatement st=null;
        Object o=null;

        try {
            try{

                st = connection.prepareStatement(sql);
                if(args.length>0) {
                    Result.setArgs(st, args);
                }


                ResultSet resultSet = st.executeQuery();

                Class<?> actualType;
                Object bean;

                Class<?> returnType = method.getReturnType();
                if (returnType== List.class){
                    Type type = method.getGenericReturnType();
                    if (type instanceof ParameterizedType){
                        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                        //因为list泛型只有一个值 所以直接取0下标
                        String typeName = actualTypeArguments[0].getTypeName();
                        //真实返回值类型 Class对象
                        actualType = Class.forName(typeName);
                        Class<?> clz=Class.forName(actualType.getName());
                        //bean = clz.newInstance();
                        o = handleList(clz, resultSet);
                    }
                }else {
                    Class<?> clz=Class.forName(returnType.getName());
                    bean=clz.newInstance();
                    o= handleBean(bean, resultSet);
                }
            } finally {
                Objects.requireNonNull(st).close();
            }
        }catch (SQLException | ClassNotFoundException |InstantiationException | IllegalAccessException e){
            throw new RuntimeException(e);
        }

        return o;
    }


    private List<Object> handleList(Class<?> clz,ResultSet set) throws SQLException, IllegalAccessException, InstantiationException {

        List<Object> list=new ArrayList<>();

        while(set.next()) {
            Object bean = clz.newInstance();
            Field[] fields = Objects.requireNonNull(bean).getClass().getDeclaredFields();
            getSingleBean(bean, set, fields);
            list.add(bean);
        }

        return list;
    }


    private Object handleBean(Object bean,ResultSet set) throws SQLException, IllegalAccessException {
        Field[] fields = Objects.requireNonNull(bean).getClass().getDeclaredFields();

        if(set.next()) {
            getSingleBean(bean, set, fields);
        }
        return bean;
    }

    private void getSingleBean(Object bean, ResultSet set, Field[] fields) throws SQLException, IllegalAccessException {
        for (Field field : fields) {
            field.setAccessible(true);

            String name = field.getName();

            String result = set.getString(name);
            Class<?> type = field.getType();
            Object o = getTypeObject(type,result);

            field.set(bean, o);
        }
    }


    private Object getTypeObject(Class<?> type,String result){
        Object o = null;

        if (type == Integer.TYPE||type==Integer.class) {
            o = Integer.parseInt(result);
        } else if (type == Double.TYPE||type==Double.class) {
            o = Double.parseDouble(result);
        } else if (type == Float.TYPE||type== Float.class) {
            o = Float.parseFloat(result);
        } else if (type == Short.TYPE||type==Short.class) {
            o = Short.parseShort(result);
        } else if (type == Byte.TYPE||type== Byte.class) {
            o = Byte.parseByte(result);
        } else if (type == Character.TYPE||type== Character.class) {
            o = result.toCharArray()[0];
        } else if (type == Long.TYPE||type==Long.class) {
            o = Long.parseLong(result);
        } else if (type == String.class) {
            o = result;
        }
        return o;
    }



}
