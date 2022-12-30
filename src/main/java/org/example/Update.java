package org.example;

import jdbc.sql.Insert;
import jdbc.sql.ObjectPara;
import jdbc.sql.Para;
import jdbc.sql.Select;

import java.util.List;

public interface Update {

    @Insert("insert into tbl_book values(#{id},#{type},#{name},#{description})")
    boolean addA(@ObjectPara("user") User user);

    @Select("select * from tbl_book")
    List<User> getAll();

    @Select("select * from tbl_book where id=#{id} or type=#{type}")
    List<User> getById(@Para("id")int id,@Para("type")String type);

}
