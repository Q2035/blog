package com.test.blog.mapper;

import com.test.blog.pojo.ContactMe;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface ContactMapper {

    @Insert("insert into t_contact values(default,#{email},#{description},#{createTime})")
    void insertContactIntoDB(ContactMe contactMe);

//    查找到最近插入的数据
    @Select("select * from t_contact where email = #{email} order by create_time desc limit 1")
    ContactMe searchContactInfoByEmail(String email);
}
