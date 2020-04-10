package com.test.blog.mapper;

import com.test.blog.pojo.UsefulTool;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UsefulToolsMapper {
    @Select("select * from t_usefultool")
    List<UsefulTool> listAllLinks();
}
