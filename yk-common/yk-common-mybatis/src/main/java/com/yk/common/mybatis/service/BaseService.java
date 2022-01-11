package com.yk.common.mybatis.service;

import com.yk.common.core.domain.Page;
import com.yk.common.mybatis.query.Query;
import java.util.List;
import java.util.Map;

public interface BaseService<T> {

    T selectByPrimaryKey(Long id);

    T insert(T dto);

    T insertSelective(T dto);

    int updateSelective(T dto);

    T selectOne(Query query);

    T selectFirst(Query query);

    T selectFirst(Query query, String order);

    int count(Query query);

    List<T> findList(Query query);

    List<T> findList(Query query, String order);

    Page<T> findPage(Query query, int pageNo, int pageSize);

    Page<T> findPage(Query query, int pageNo, int pageSize, String order);

    int deleteByPrimaryKey(Long id);

    Map<Long, T> findIdMap(Query query);
}