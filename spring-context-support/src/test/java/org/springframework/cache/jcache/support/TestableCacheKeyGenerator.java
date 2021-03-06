package org.springframework.cache.jcache.support;

import org.springframework.cache.interceptor.SimpleKey;

import javax.cache.annotation.CacheKeyGenerator;
import javax.cache.annotation.CacheKeyInvocationContext;
import javax.cache.annotation.GeneratedCacheKey;
import java.lang.annotation.Annotation;

/**
 * A simple test key generator that only takes the first key arguments into
 * account. To be used with a multi parameters key to validate it has been
 * used properly.
 *
 * @author Stephane Nicoll
 */
public class TestableCacheKeyGenerator implements CacheKeyGenerator {

	@Override
	public GeneratedCacheKey generateCacheKey(CacheKeyInvocationContext<? extends Annotation> context) {
		return new SimpleGeneratedCacheKey(context.getKeyParameters()[0]);
	}


	@SuppressWarnings("serial")
	private static class SimpleGeneratedCacheKey extends SimpleKey implements GeneratedCacheKey {

		public SimpleGeneratedCacheKey(Object... elements) {
			super(elements);
		}

	}

}
