package com.yk.common.redis.lock;

import com.yk.common.core.consts.Consts;
import com.yk.common.core.domain.ApiResult;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * @author yzw
 * @date 2022/01/19 10:33
 */
@Slf4j
public class LockUtil {

    public static ApiResult lock(RedissonClient redissonClient, String label, LockBuilder lockBuilder)
        throws Exception {
        RLock lock = redissonClient.getFairLock(Consts.REDIS_LOCK_PRE + label);
        boolean locked = false;

        ApiResult apiResult = ApiResult.ok();

        try {
            //尝试获取锁。最多等待2s，若获取成功，最多持有5s
            locked = lock.tryLock(2L, 5L, TimeUnit.SECONDS);
            log.info("{} locked={}", label, locked);

            if (locked) {
                apiResult = lockBuilder.biz();
            }
        } catch (Exception e) {
            log.error("{} 数据处理异常.", label, e);
            apiResult = ApiResult.error(e.getMessage());
        } finally {
            //解锁
            log.info("{} unlock", label);
            if (locked) {
                lock.unlock();
            }
        }

        return apiResult;
    }

}
