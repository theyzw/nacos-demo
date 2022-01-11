package com.yk.common.mybatis.type.handler;

import com.yk.common.core.enums.BaseEnumInterface;
import com.yk.common.core.utils.EnumUtil;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * enum存入数据库时使用code值 不能放在handlers包内
 *
 * @param <E>
 * @author ye
 */
public class CodeEnumTypeHandler<E extends BaseEnumInterface> extends BaseTypeHandler<E> {

    private final Class<E> type;
    //private E[] enums;

    /**
     * 设置配置文件设置的转换类以及枚举类内容，供其他方法更便捷高效的实现
     *
     * @param type 配置文件中设置的转换类
     */
    public CodeEnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
        //this.enums = type.getEnumConstants();
        //if (this.enums == null) {
        //    throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
        //}
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        //BaseTypeHandler已经做了parameter的null判断
        ps.setInt(i, parameter.getCode());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // 根据数据库存储类型决定获取类型
        int code = rs.getInt(columnName);
        return rs.wasNull() ? null : codeOf(code);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int code = rs.getInt(columnIndex);
        return rs.wasNull() ? null : codeOf(code);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int code = cs.getInt(columnIndex);
        return cs.wasNull() ? null : codeOf(code);
    }

    /**
     * 枚举类型转换，由于构造函数获取了枚举的子类enums，让遍历更加高效快捷
     *
     * @param code 数据库中存储的自定义code属性
     * @return code对应的枚举类
     */
    private E codeOf(int code) {
        try {
            return EnumUtil.codeOf(type, code);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot convert " + code + " to " + type.getSimpleName() + " by code " +
                "value.", e);
            //throw new IllegalArgumentException("未知的枚举类型：" + code + ",请核对" + type.getSimpleName());
        }
    }
}
