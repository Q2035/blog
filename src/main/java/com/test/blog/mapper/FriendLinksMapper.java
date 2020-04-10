package com.test.blog.mapper;

import com.test.blog.pojo.FriendLinks;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FriendLinksMapper {

    @Select("select * from t_friendlink")
    List<FriendLinks> listAllLinks();
}
