package com.gangnam.portal.repository.redis;

import com.gangnam.portal.util.jwt.redis.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<RefreshToken, String> {
}