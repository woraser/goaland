package com.anosi.asset.shiro;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class SessionRedisDao extends AbstractSessionDAO {

	@Resource
	@Lazy
	private RedisTemplate<String, Session> redisTemplate;

	private static int timeout = 60 * 60 * 24 * 7;// 一星期

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = null;
		if (session.getId() == null) {
			sessionId = generateSessionId(session);
			assignSessionId(session, sessionId);
			ValueOperations<String, Session> opv = redisTemplate.opsForValue();
			opv.set(sessionId.toString(), session, timeout, TimeUnit.SECONDS);
		} else {
			sessionId = session.getId();
		}
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		ValueOperations<String, Session> opv = redisTemplate.opsForValue();
		return opv.get(sessionId.toString());
	}

	@Override
	public void update(Session session) throws UnknownSessionException {
		ValueOperations<String, Session> opv = redisTemplate.opsForValue();
		opv.set(session.getId().toString(), session, timeout, TimeUnit.SECONDS);
	}

	@Override
	public void delete(Session session) {
		redisTemplate.delete(session.getId().toString());
	}

	@Override
	public Collection<Session> getActiveSessions() {
		return Collections.emptySet();
	}

}
