package org.rhine.unicorn.samples.springboot.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.rhine.unicorn.samples.springboot.entity.User;

@Mapper
public interface UserMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert  into user (name) values (#{name})")
    int insert(User user);

}
