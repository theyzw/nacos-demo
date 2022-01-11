package com.yk.common.mybatis.service.impl;

import com.github.pagehelper.PageHelper;
import com.yk.common.core.domain.Page;
import com.yk.common.mybatis.dao.BaseDao;
import com.yk.common.mybatis.entity.BaseEntity;
import com.yk.common.mybatis.query.Query;
import com.yk.common.mybatis.service.BaseService;
import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class BaseServiceImpl<T, D extends BaseDao<E>, E extends BaseEntity<T>> implements BaseService<T>,
    InitializingBean {

    private Class<T> dtoClz;

    private Class<E> entityClz;

    @Autowired
    protected D dao;

    @Override
    public T selectByPrimaryKey(Long id) {
        E entity = dao.selectByPrimaryKey(id);
        if (entity == null) {
            return null;
        }
        return entity.convert(dtoClz);
    }

    @Override
    public T insert(T dto) {
        E entity = createEntity(dto);
        dao.insert(entity);
        try {
            BeanUtils.copyProperties(entity, dto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return dto;
    }

    @Override
    public T insertSelective(T dto) {
        E entity = createEntity(dto);
        dao.insertSelective(entity);
        try {
            BeanUtils.copyProperties(entity, dto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSelective(T dto) {
        E entity = createEntity(dto);
        E e = dao.selectByPrimaryKey(entity.getId());
        int row = 0;
        if (!entity.equals(e)) {
            row += dao.updateSelective(entity);
        }
        return row;
    }

    @Override
    public T selectOne(Query query) {
        List<T> list = findList(query);
        if (list.isEmpty()) {
            return null;
        } else if (list.size() > 1) {
            String className = Arrays.stream(this.getClass().getInterfaces())
                .map(Class::getSimpleName)
                .filter(e -> e.toLowerCase().contains("service"))
                .findFirst()
                .orElse("未知");
            String msg = MessageFormat.format("{0}.selectOne 查询到大于1条的记录. 参数:{1}", className, query);
            throw new RuntimeException(msg);
        }

        return list.get(0);
    }

    @Override
    public T selectFirst(Query query) {
        return selectFirst(query, null);
    }

    @Override
    public T selectFirst(Query query, String order) {
        List<T> list = findList(query, order);
        if (list.isEmpty()) {
            return null;
        } else if (list.size() > 1) {
            String className = Arrays.stream(this.getClass().getInterfaces())
                .map(Class::getSimpleName)
                .filter(e -> e.toLowerCase().contains("service"))
                .findFirst()
                .orElse("未知");
            String msg = MessageFormat.format("{0}.selectFirst 查询到大于1条的记录. 参数:{1}", className, query);
            log.info(msg);
        }
        return list.get(0);
    }

    @Override
    public int count(Query query) {
        return dao.count(query);
    }

    @Override
    public List<T> findList(Query query) {
        List<E> list = dao.findList(query);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream().map(en -> en.convert(dtoClz)).collect(Collectors.toList());
    }

    @Override
    public List<T> findList(Query query, String order) {
        PageHelper.orderBy(order);
        return this.findList(query);
    }

    @Override
    public Page<T> findPage(Query query, int pageNo, int pageSize) {
        return findPage(query, pageNo, pageSize, null);
    }

    @Override
    public Page<T> findPage(Query query, int pageNo, int pageSize, String order) {
        pageNo = Math.max(pageNo, 1);
        pageSize = Math.max(pageSize, 1);

        PageHelper.startPage(pageNo, pageSize).setOrderBy(order);
        List<E> list = dao.findList(query);
        com.github.pagehelper.Page<E> result = (com.github.pagehelper.Page<E>) list;

        List<T> collect = result.stream().map(en -> en.convert(dtoClz)).collect(Collectors.toList());

        return new Page<>(result.getPageNum(), result.getPageSize(), result.size(),
            result.getTotal(), result.getPages(), collect);
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return dao.deleteByPrimaryKey(id);
    }

    @Override
    public Map<Long, T> findIdMap(Query query) {
        if (query == null) {
            return Collections.emptyMap();
        }
        List<E> list = dao.findList(query);
        if (list == null || list.isEmpty()) {
            return Collections.emptyMap();
        }

        return list.stream()
            .collect(Collectors.toMap(BaseEntity::getId, value -> value.convert(dtoClz), (t, t2) -> t2));
    }

    @Override
    public void afterPropertiesSet() {
        dtoClz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        entityClz = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[2];
    }

    protected E createEntity(T record) {
        E entity = null;
        try {
            entity = entityClz.getConstructor(dtoClz).newInstance(record);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return entity;
    }

}
