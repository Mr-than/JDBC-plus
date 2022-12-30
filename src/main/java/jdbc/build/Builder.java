package jdbc.build;

import jdbc.proxy.SqlMsgFounder;

public abstract class Builder {
    public abstract Builder addURL(String URL);
    public abstract Builder addUser(String USER);
    public abstract Builder addPwd(String PASS);
    public abstract SqlMsgFounder build();
}
