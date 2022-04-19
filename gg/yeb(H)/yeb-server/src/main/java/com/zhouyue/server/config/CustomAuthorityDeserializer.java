package com.zhouyue.server.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 自定义反序列化,使其可以解析 authority
 */
public class CustomAuthorityDeserializer extends JsonDeserializer {
    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectMapper codec = (ObjectMapper) jsonParser.getCodec();
        JsonNode jsonNode = codec.readTree(jsonParser);
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        Iterator<JsonNode> elements = jsonNode.elements();
        while (elements.hasNext()){
            JsonNode next = elements.next();
            JsonNode authority = next.get("authority");
            list.add(new SimpleGrantedAuthority(authority.asText()));
        }
        return list;
    }
}
