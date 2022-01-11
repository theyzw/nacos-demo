package com.yk.common.mybatis.type.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

/**
 * List类型转json
 * <p>
 * 解决泛型丢失的问题
 * 注意:这个反序列化会保存类的相关信息，不要修改反序列化类的类名和路径
 */
@Slf4j
@MappedJdbcTypes(value = {JdbcType.VARCHAR}, includeNullJdbcType = true)
public class ListJsonTypeHandler<E> extends BaseTypeHandler<E> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        // 未知字段忽略
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 不使用科学计数
        MAPPER.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        // null 值不输出(节省内存)
        MAPPER.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        // 解决泛型丢失的问题
        // 配置输出 反序列化类名的形式
        MAPPER.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.PROPERTY);
    }

    private final Class<E> type;

    public ListJsonTypeHandler(Class<E> type) {
        Objects.requireNonNull(type);
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        if (Objects.isNull(parameter)) {
            ps.setString(i, null);
            return;
        }

        ps.setString(i, toJson(parameter));
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toObject(rs.getString(columnName), type);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toObject(rs.getString(columnIndex), type);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toObject(cs.getString(columnIndex), type);
    }

    /**
     * object 转 json
     *
     * @param obj 对象
     * @return String json字符串
     */
    private String toJson(E obj) {
        if (Objects.isNull(obj)) {
            return null;
        }

        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("mybatis column to json error,obj:" + obj, e);
        }
    }

    /**
     * 转换对象
     *
     * @param json  json数据
     * @param clazz 类
     * @return E
     */
    private E toObject(String json, Class<E> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }

        try {
            return MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            log.error("mybatis column json to object error,json:{}", json, e);
            return null;
        }
    }
}