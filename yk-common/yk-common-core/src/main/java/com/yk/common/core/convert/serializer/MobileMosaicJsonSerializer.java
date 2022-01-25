package com.yk.common.core.convert.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;

/**
 * 手机号脱敏
 */
public class MobileMosaicJsonSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (StringUtils.isNotEmpty(value)) {
            value = value.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
        gen.writeRawValue("\"" + value + "\"");
    }

}
