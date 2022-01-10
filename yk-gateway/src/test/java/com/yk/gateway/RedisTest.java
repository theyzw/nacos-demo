package com.yk.gateway;

import com.yk.common.redis.utils.RedisUtil;
import org.junit.jupiter.api.Test;

/**
 * @author yzw
 * @date 2022/01/10 18:18
 */
public class RedisTest extends BaseTest {

    @Test
    public void test() {
        RedisUtil.set("sdfdsf", "sdjlf");


        print(RedisUtil.get("sdfdsf"));
    }
}
