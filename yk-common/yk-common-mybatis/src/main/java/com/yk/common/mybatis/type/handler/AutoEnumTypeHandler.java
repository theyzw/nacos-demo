package com.yk.common.mybatis.type.handler;

import com.yk.base.common.enums.BaseEnumInterface;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 方案1：配置mybatis.configuration.default-enum-type-handler=com.yk.demo.app.enums.handlers
 * .AutoEnumTypeHandler或CodeEnumTypeHandler；
 * <p>
 * 方案2：不要AutoEnumTypeHandler，配置xml文件，一条enum对应一个typeHandler；
 * <p>
 * 方案3：配置mybatis.type-handlers-package=com.yk.demo.app.enums.handlers，每个enum对应的typeHandler都继承CodeEnumTypeHandler
 * <p>
 * 自动匹配枚举转换器
 *
 * @author ye
 * @date 2018/08/30 22:20
 */
public class AutoEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    private BaseTypeHandler typeHandler;

    public AutoEnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }

        if (BaseEnumInterface.class.isAssignableFrom(type)) {
            //如果实现了BaseEnumInterface，则使用自定义转换器
            typeHandler = new CodeEnumTypeHandler(type);
        } else {
            //否则使用默认转换器
            typeHandler = new EnumTypeHandler(type);
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        typeHandler.setNonNullParameter(ps, i, parameter, jdbcType);
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return (E) typeHandler.getNullableResult(rs, columnName);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return (E) typeHandler.getNullableResult(rs, columnIndex);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return (E) typeHandler.getNullableResult(cs, columnIndex);
    }
}
