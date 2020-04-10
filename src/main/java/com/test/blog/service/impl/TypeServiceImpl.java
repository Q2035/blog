package com.test.blog.service.impl;

import com.test.blog.dto.BlogQuery;
import com.test.blog.mapper.BlogMapper;
import com.test.blog.mapper.TypeMapper;
import com.test.blog.pojo.Blog;
import com.test.blog.pojo.Type;
import com.test.blog.service.TypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    private Logger logger = LoggerFactory.getLogger(TypeServiceImpl.class);

    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private BlogMapper blogMapper;

    @Override
    public Type findByName(String name) {
        return typeMapper.findByName(name);
    }

    @Override
    public void saveType(Type type) {
        typeMapper.saveType(type);
    }

    @Override
    public Type getType(Long id) {
        return typeMapper.getType(id);
    }

    @Override
    public Type getTypeByName(String name) {
        return typeMapper.getTypeByName(name);
    }

    @Override
    public Integer updateType(Long id, Type type) {
        return typeMapper.updateType(id,type);
    }

    @Override
    public void deleteType(Long id) {
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        List<Blog> blogs = blogMapper.listBlog(blogQuery);
        if (blogs !=null && !blogs.isEmpty()){
            logger.warn("尝试删除Type，但是存在关联的博客，删除失败");
            return;
        }
        typeMapper.deleteType(id);
    }

    @Override
    public List<Type> listType() {
        return typeMapper.listType();
    }

    /**
     * 只有Type信息，没有博客信息，为了避免再次查询数据库，使用之前已经查出的数据。
     * @param size
     * @return
     */
    @Override
    public List<Type> findTypeTop(Integer size) {
        return typeMapper.findTop(size);
    }
}
