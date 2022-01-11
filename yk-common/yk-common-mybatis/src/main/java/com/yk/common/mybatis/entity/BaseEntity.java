package com.yk.common.mybatis.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

@Slf4j
@Data
public class BaseEntity<T> implements Serializable {

    protected Long id;

    public BaseEntity() {

    }

    public BaseEntity(T dto) {
        try {
            BeanUtils.copyProperties(dto, this);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public T convert(Class<T> clazz) {
        try {
            T dto = clazz.newInstance();
            BeanUtils.copyProperties(this, dto);
            return dto;
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return null;
    }

}
