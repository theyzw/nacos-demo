package com.yk.common.redis.lock;

import com.yk.common.core.domain.ApiResult;

/**
 * @author yzw
 * @date 2022/01/19 10:21
 */
@FunctionalInterface
public interface LockBuilder {

    public abstract ApiResult biz();

}
