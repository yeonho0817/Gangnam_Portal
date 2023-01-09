package com.gangnam.portal.repository.redis;

import com.gangnam.portal.util.jwt.redis.AccessToken;
import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<AccessToken, String> {
}