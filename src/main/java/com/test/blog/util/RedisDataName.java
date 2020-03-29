package com.test.blog.util;

import org.springframework.stereotype.Component;

@Component
public class RedisDataName {
    public static final String ALL_BLOGVOS = "listAllBlogVOs";
    public static final String TOP_TYPES = "findTypeTop";
    public static final String TOP_TAGS = "listTagTop";
}
