package top.hellooooo.blog.mapper;

import top.hellooooo.blog.pojo.Type;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TypeMapper {
    @Select("select * from t_type where name=#{name}")
    Type findByName(String name);

    @Insert("insert into t_type values(#{id},#{name})")
    void saveType(Type type);

    @Select("select * from t_type where id =#{id}")
    Type getType(Long id);

    @Select("select * from t_type where name=#{name}")
    Type getTypeByName(String name);

    @Update("update t_type set name=#{type.name} where id =#{id}")
    Integer updateType(Long id,@Param("type") Type type);

    @Delete("delete from t_type where id =#{id}")
    void deleteType(Long id);

    @Select("select * from t_type")
    List<Type> listType();

    @Select({"SELECT DISTINCT t.id ,r.count ,t.name from t_type t," +
            "(select type_id tid,count(id) count from t_blog GROUP BY type_id" +
            " HAVING count>=1 ORDER BY count desc) r " +
            "where r.tid =t.id " +
            " limit ${size}"})
    List<Type> findTop(Integer size);
}
