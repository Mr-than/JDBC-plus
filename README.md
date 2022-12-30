# JDBC-plus

> 这是一个对于jdbc的封装，原版jdbc确实是及其难用了，但是mybatis对于平时的一写普通的项目又过于沉重（光是配置都要写半天），所以我就自己封装了一层，用法和mybatis相差不大，用动态代理实现。

## 代码演示

在我的项目仓库中的 `org.example` 包下有演示代码，这里再演示一遍：

* 首先要创建一个映射接口，下面给出演示代码：

  ```java
  public interface Update {
  
      @Insert("insert into tbl_book values(#{id},#{type},#{name},#{description})")
      boolean addA(@ObjectPara("user") User user);
  
      @Select("select * from tbl_book")
      List<User> getAll();
  
      @Select("select * from tbl_book where id=#{id} or type=#{type}")
      List<User> getById(@Para("id")int id,@Para("type")String type);
  
  }
  ```

  

* 再通过接口执行：

```java
public static void main(String[] args) {

        SqlMsgFounder build = new ConnectionBuilder().addPwd(PASS).addURL(URL).addUser(USER).build();

        Update update=(Update)build.getProxy(Update.class);

        User user=new User(10,"好","好好好","好好好好好");

        boolean b = update.addA(user);

        System.out.println(b);

    }
```

## 注意

由于这玩意儿主要是提供给自己用和学习的项目，所以直接没写注释，所以慎看！！！