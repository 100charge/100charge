package com.xingchuan.common.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {

    @SneakyThrows
    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        if (null != bigDecimal) {
            jsonGenerator.writeString(bigDecimal.setScale(2, RoundingMode.HALF_UP).toPlainString());
        } else {
            jsonGenerator.writeString(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP).toPlainString());
        }
    }
}
