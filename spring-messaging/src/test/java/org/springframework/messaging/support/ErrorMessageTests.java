/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.messaging.support;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * @author Gary Russell
 * @since 5.0
 */
public class ErrorMessageTests {

	@Test
	public void testToString() {
		ErrorMessage em = new ErrorMessage(new RuntimeException("foo"));
		String emString = em.toString();
		assertThat(emString, not(containsString("original")));

		em = new ErrorMessage(new RuntimeException("foo"), new GenericMessage<>("bar"));
		emString = em.toString();
		assertThat(emString, containsString("original"));
		assertThat(emString, containsString(em.getOriginalMessage().toString()));
	}

}
