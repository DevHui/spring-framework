/**
 * Copyright 2002-2016 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.aop.aspectj.autoproxy.spr3064;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@Retention(RetentionPolicy.RUNTIME)
@interface Transaction {
}


interface Service {

	void serveMe();
}

/**
 * @author Adrian Colyer
 * @author Chris Beams
 */
public class SPR3064Tests {

	private Service service;


	@Test
	public void testServiceIsAdvised() {
		ClassPathXmlApplicationContext ctx =
				new ClassPathXmlApplicationContext(getClass().getSimpleName() + ".xml", getClass());

		service = (Service) ctx.getBean("service");

		try {
			this.service.serveMe();
			fail("service operation has not been advised by transaction interceptor");
		} catch (RuntimeException ex) {
			assertEquals("advice invoked", ex.getMessage());
		}
	}

}

@Aspect
class TransactionInterceptor {

	@Around(value = "execution(* *..Service.*(..)) && @annotation(transaction)")
	public Object around(ProceedingJoinPoint pjp, Transaction transaction) throws Throwable {
		throw new RuntimeException("advice invoked");
		//return pjp.proceed();
	}
}

class ServiceImpl implements Service {

	@Override
	@Transaction
	public void serveMe() {
	}
}
