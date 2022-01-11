package com.yk.common.mybatis.dao;

import com.yk.common.mybatis.query.Query;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BaseDao<T> {

    T selectByPrimaryKey(Long id);

    int insert(T record);

    int insertSelective(T record);

    int updateSelective(T record);

    int count(Query query);

    List<T> findList(Query query);

    int deleteByPrimaryKey(Long id);

}
