package com.yk.common.core.convert.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.DeserializerCache;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.util.LRUMap;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * 主要源码
 * <p>
 * package: org.springframework.web.servlet.mvc.method.annotation
 * <p>
 * class.method：AbstractMessageConverterMethodArgumentResolver.readWithMessageConverters()
 * <p>
 * com.fasterxml.jackson.databind.deser.DeserializerCache
 * <p>
 * org.springframework.http.converter.json.AbstractJackson2HttpMessageConverte
 * <p>
 * 使用方式： 在枚举类反序列化注解上加入即可。如：
 *
 * @JsonDeserialize(using = BaseEnumInterfaceJsonDeserializer.class)
 */
@Slf4j
public class BaseEnumInterfaceJsonDeserializer extends JsonDeserializer<Object> {

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        Object invoke = null;
        try {
            // 获取属性名称
            String currentName = p.getCurrentName();
            // 获取枚举code值
            Integer code = p.readValueAs(Integer.class);

            // 反射拿取_cache
            Field declaredFields = DeserializationContext.class.getDeclaredField("_cache");
            declaredFields.setAccessible(true);
            // 每次调用此反序列化方法，会将字段名和使用bean的映射关系缓存，然后遍历Bean，找到字段名对应的枚举类型
            // 如果同一个字段名，例如a，在不同的bean里对应了不同的枚举（a->aEnum, a->bEnum)，此时缓存里可能存了多个对应关系
            // 下次反序列化时可能不能取到正确的枚举类型，所以：
            // ** 不允许同一个字段名对应多个枚举类型 **
            DeserializerCache deserializerCache = (DeserializerCache) declaredFields.get(ctx);

            // 反射拿取_cachedDeserializers
            Field cachedDeserializers = DeserializerCache.class.getDeclaredField("_cachedDeserializers");
            cachedDeserializers.setAccessible(true);
            // 新版jackson DeserializerCache._cachedDeserializers类型为LRUMap
            LRUMap lruMap = (LRUMap) cachedDeserializers.get(deserializerCache);

            Field mapField = LRUMap.class.getDeclaredField("_map");
            mapField.setAccessible(true);
            Map<Object, Object> map = (Map) mapField.get(lruMap);

            // 遍历SimpleType值
            for (Object o : map.keySet()) {
                if (!(o instanceof SimpleType)) {
                    continue;
                }

                Object obj = map.get(o);
                //找到能转成bean类的反序列化工具
                if (obj instanceof BeanDeserializer) {
                    //反射拿取_propertyBasedCreator
                    //com.fasterxml.jackson.databind.deser.BeanDeserializer._deserializeUsingPropertyBased()方法中409行
                    //Field propertyBasedCreator = BeanDeserializer.class.getSuperclass().getDeclaredField
                    // ("_propertyBasedCreator");
                    //propertyBasedCreator.setAccessible(true);
                    //PropertyBasedCreator propertyBasedCreator1 = (PropertyBasedCreator)propertyBasedCreator.get(o2);
                    //SettableBeanProperty creatorProp = propertyBasedCreator1.findCreatorProperty(currentName);
                    Field beanProperties = BeanDeserializer.class.getSuperclass().getDeclaredField("_beanProperties");
                    beanProperties.setAccessible(true);
                    BeanPropertyMap beanPropertyMap = (BeanPropertyMap) beanProperties.get(obj);
                    SettableBeanProperty creatorProp = beanPropertyMap.find(currentName);
                    //防止对象中有 多个对象 子对象却没有这个字段 直接跳过 执行下一个
                    if (creatorProp == null) {
                        continue;
                    }
                    SimpleType type = (SimpleType) creatorProp.getType();
                    Class<?> rawClass = type.getRawClass();
                    //判断此类是否是枚举
                    if (rawClass.isEnum()) {
                        //调用 枚举的 get 方法
                        Method get = rawClass.getMethod("get", Integer.class);
                        invoke = get.invoke(rawClass, code);
                        return invoke;
                    }
                }
            }
        } catch (Exception e) {
            log.error("枚举反序列化错误.", e);
            throw new RuntimeException("枚举反序列化错误");
        }

        return invoke;
    }
}
