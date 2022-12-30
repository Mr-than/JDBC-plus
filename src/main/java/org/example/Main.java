package org.example;

import jdbc.build.ConnectionBuilder;
import jdbc.proxy.SqlMsgFounder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class Main {
    static final String USER = "root";
    static final String PASS = "123456";
    static final String URL = "jdbc:mysql://localhost:3306/ssm_db?serverTimezone=UTC&useSSL=false";


    public static void main1(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        System.out.println("Connecting to database...");
        Connection connection = DriverManager.getConnection(URL, USER, PASS);

        String sql = "select * from tbl_book";

        PreparedStatement st = connection.prepareStatement(sql);

        ResultSet set = st.executeQuery();


        while(set.next()) {
            System.out.println("id " + set.getString("id") +
                    " type " + set.getString("type") +
                    " name " + set.getString("name") +
                    " desc " + set.getString("description"));
        }


/*
        Class<? extends Main> aClass = Main.class;
        Method method = aClass.getMethod("te");
        Class<?> returnType = method.getReturnType();
        Class<?> actualType=null;
        if (returnType==List.class){
            Type type = method.getGenericReturnType();
            if (type instanceof ParameterizedType){
                Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                //因为list泛型只有一个值 所以直接取0下标
                String typeName = actualTypeArguments[0].getTypeName();
                //真实返回值类型 Class对象
                actualType = Class.forName(typeName);
                //System.out.println(actualType.getName());
            }
        }

        Class<?> clz=Class.forName(actualType.getName());

        User o = (User) clz.newInstance();

        Class<? extends User> cla = o.getClass();

        Field[] fields = cla.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            System.out.println(field.getName());

            if(field.getName().equals("name")){
                field.set(o,"hello world");
            }
        }



        o.test();
*/


        //Main main=new Main();

        //main.a('a');



    }

    public List<User> te(){
        return null;
    }

    public void a(Object a){
        if(a instanceof Integer){
            System.out.println((int)a*(int)a);
        }else if(a instanceof String){
            System.out.println((String)a+"  "+(String)a);
        }
    }


    public static void main(String[] args) {

        SqlMsgFounder build = new ConnectionBuilder().addPwd(PASS).addURL(URL).addUser(USER).build();

        Update update=(Update)build.getProxy(Update.class);

        User user=new User(10,"好","好好好","好好好好好");

        boolean b = update.addA(user);

        System.out.println(b);



    }

}