package top.hellooooo.blog.service;

import top.hellooooo.blog.pojo.Type;

import java.util.List;

public interface TypeService {

    Type findByName(String name);

    void saveType(Type type);

    Type getType(Long id);

    Type getTypeByName(String name);

    Integer updateType(Long id,Type type);

    void deleteType(Long id);

    List<Type> listType();

    List<Type> findTypeTop(Integer size);
}
