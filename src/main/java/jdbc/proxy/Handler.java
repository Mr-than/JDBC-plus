package jdbc.proxy;

import jdbc.data.GetQuery;
import jdbc.data.GetUpdate;
import jdbc.data.Result;
import jdbc.sql.SqlHandler;
import jdbc.sql.Update;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Handler implements InvocationHandler {

    private final Connection connection;
    private final List<String> order=new ArrayList<>();

    public Handler(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        order.clear();
        String sql = SqlHandler.getSql(method,args);
        Class<?> type = SqlHandler.getType();
        order.addAll(SqlHandler.getOrder());

        Result result;
        if(type== Update.class){
            result =new GetUpdate(connection);
        }else {
            result =new GetQuery(connection);
        }

        Object[] objects = SqlHandler.getSqlOrder().toArray();

        return result.getResult(sql, objects, method,order);
    }
}
