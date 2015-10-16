package com.zanshang.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by Lookis on 6/16/15.
 */
public class Xml {

    private static ObjectMapper mapper;

    static {
        mapper = new XmlMapper();
        mapper.addMixIn(Map.class, MapMixin.class);
    }

    public static String toXml(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(String xml, Class<T> clz) {
        try {
            return mapper.readValue(xml, clz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(InputStream inputStream, Class<T> clz) {
        try {
            return mapper.readValue(inputStream, clz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @JacksonXmlRootElement(localName = "xml")
    class MapMixin {

    }
}
