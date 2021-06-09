package org.rhine.unicorn.samples.springxml.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    int insert(@Param("name") String name);

}
