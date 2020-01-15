/*
 * Copyright 2002-2014 the original author or authors.
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

package org.springframework.test.context.env;

import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

import static org.junit.Assert.assertEquals;

/**
 * Integration tests that verify support for overriding properties from
 * properties files via inlined properties configured with
 * {@link TestPropertySource @TestPropertySource}.
 *
 * @author Sam Brannen
 * @since 4.1
 */
@TestPropertySource(properties = {"explicit = inlined", "extended = inlined1", "extended = inlined2"})
public class MergedPropertiesFilesOverriddenByInlinedPropertiesTestPropertySourceTests extends
		MergedPropertiesFilesTestPropertySourceTests {

	@Test
	@Override
	public void verifyPropertiesAreAvailableInEnvironment() {
		assertEquals("inlined", env.getProperty("explicit"));
	}

	@Test
	@Override
	public void verifyExtendedPropertiesAreAvailableInEnvironment() {
		assertEquals("inlined2", env.getProperty("extended"));
	}

}
