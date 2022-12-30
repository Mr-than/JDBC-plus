package jdbc.data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class GetUpdate implements Result{
    private final Connection connection;

    public GetUpdate(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Object getResult(String sql, Object[] args, Method method,List<String> order) {
        PreparedStatement st=null;
        boolean result=false;
        int lines;
        try {
            try {
                st = connection.prepareStatement(sql);

                if(args.length>0) {

                    if(args.length==1&&args[0].getClass().getClassLoader()!=null){

                        Field[] fields = args[0].getClass().getDeclaredFields();

                        List<Object> a=new ArrayList<>(fields.length);
                        for (String s : order) {
                            for (Field field : fields) {
                                field.setAccessible(true);
                                if(field.getName().equals(s)){
                                    a.add(field.get(args[0]));
                                }
                            }

                        }

                        args=a.toArray();
                    }
                        Result.setArgs(st, args);
                }
                int i = st.executeUpdate();

                lines=i;
                if(i>0){
                    result=true;
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } finally {
                Objects.requireNonNull(st).close();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        if(method.getReturnType()==Integer.class){
            return lines;
        }
        return result;
    }

}