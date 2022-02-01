package com.p.chat;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.boot.jackson.JsonComponent;

class UserName {
    private String value;

    public UserName() {
    }

    public UserName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

@JsonComponent
class UserNameSerializer extends StdSerializer<UserName> {

    UserNameSerializer() {
        super(UserName.class);
    }

    @Override
    public void serialize(UserName userName, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(userName.toString());
    }
}

@JsonComponent
class UserNameDeserializer extends StdDeserializer<UserName> {

    UserNameDeserializer() {
        super(UserName.class);
    }

    @Override
    public UserName deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return new UserName(jsonParser.getValueAsString());
    }
}
