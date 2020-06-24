package com.test.blog.mapper;

import com.test.blog.dto.BlogQuery;

public class BlogProvider {
    /**
     * 根据BlogQuery查询,需要多个if
     * 有几个条件就添加几个条件
     */
    public String listBlog(BlogQuery blogQuery){
        if (blogQuery==null ||
                (blogQuery.getTypeId()==null && (blogQuery.getTitle()==null || blogQuery.getTitle().equals("")))){
            return "select * from t_blog b,t_type t where b.type_id" +
                    "=t.id order by b.update_time desc";
        }
        StringBuffer sql =new StringBuffer("select * from t_blog blog,t_type type" +
                " where blog.type_id =type.id and ");
        if (blogQuery.getTitle()!=null && !blogQuery.getTitle().equals("")){
            sql.append(" title=#{title} and ");
        }
        if (blogQuery.isRecommend()){
            sql.append(" recommend = 1 and ");
        }
        if(blogQuery.getTypeId()!=null){
            sql.append(blogQuery.getTypeId()+"=type.id and ");
        }
        sql.append(" 1 =1 order by blog.update_time desc");
        return sql.toString();
    }
}
