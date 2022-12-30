package jdbc.build;

import jdbc.factory.ConnectionFactory;
import jdbc.proxy.SqlMsgFounder;

import java.sql.Connection;

public class ConnectionBuilder extends Builder{

    private Connection connection=null;
    private String url;
    private String user;
    private String pwd;

    private SqlMsgFounder s=null;


    @Override
    public Builder addURL(String URL) {
        this.url=URL;
        return this;
    }

    @Override
    public Builder addUser(String USER) {
        this.user=USER;
        return this;
    }

    @Override
    public Builder addPwd(String PASS) {
        this.pwd=PASS;
        return this;
    }

    @Override
    public SqlMsgFounder build(){
        if(connection==null){
            connection= ConnectionFactory.createConnection(url,user,pwd);
        }
        if(s==null){
            s=new SqlMsgFounder(connection);
        }
        return this.s;
    }


}
