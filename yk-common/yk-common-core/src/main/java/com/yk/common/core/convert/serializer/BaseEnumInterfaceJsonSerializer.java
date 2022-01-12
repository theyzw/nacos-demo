package com.yk.common.core.convert.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.yk.common.core.enums.BaseEnumInterface;
import java.io.IOException;

public class BaseEnumInterfaceJsonSerializer extends JsonSerializer<BaseEnumInterface> {

    @Override
    public void serialize(BaseEnumInterface baseEnumInterface, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {
        gen.writeRawValue(String.valueOf(baseEnumInterface.getCode()));
        gen.writeFieldName(gen.getOutputContext().getCurrentName() + "_name");
        gen.writeRawValue("\"" + baseEnumInterface.getName() + "\"");
    }

}
