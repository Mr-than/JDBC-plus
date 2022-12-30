package jdbc.proxy;

import java.lang.reflect.Proxy;
import java.sql.Connection;

public class SqlMsgFounder{

    private final Connection connection;

    public SqlMsgFounder(Connection connection) {
        this.connection = connection;
    }

    public Object getProxy(Class<?> target){
        Handler handler=new Handler(connection);
        return Proxy.newProxyInstance(target.getClassLoader(), new Class[]{target},handler);
    }

}
