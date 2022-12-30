package jdbc.data;

import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface Result {

    Object getResult(String sql, Object[] args, Method method, List<String> order);

    static void setArgs(PreparedStatement st,@NotNull Object[] args) throws SQLException {
        for (int i = 0; i < args.length; i++) {
            Class<?> type = args[i].getClass();
            if(type == Integer.TYPE||type==Integer.class){
                st.setInt(i+1,(int)args[i]);
            }else if (type == Double.TYPE||type==Double.class){
                st.setDouble(i+1,(double) args[i]);
            }else if(type == Float.TYPE||type== Float.class){
                st.setFloat(i+1,(float)args[i]);
            }else if(type == String.class){
                st.setString(i+1,(String)args[i]);
            }else if(type == Short.TYPE||type==Short.class){
                st.setShort(i+1,(short)args[i]);
            }else if(type == Byte.TYPE||type== Byte.class){
                st.setByte(i+1,(byte)args[i]);
            } else if (type == Character.TYPE||type== Character.class) {
                st.setObject(i+1,args[i]);
            } else if (type == Long.TYPE||type==Long.class) {
                st.setLong(i+1,(long)args[i]);
            }
        }
    }


}
