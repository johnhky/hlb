package com.hlb.haolaoban.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by heky on 2017/11/8.
 */

public class JacksonUtils {
    static ObjectMapper mapper = new ObjectMapper();

    public static ObjectMapper getInstance() {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

}
