/*
 * Copyright 2002-2019 the original author or authors.
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

package org.springframework.core.codec;

import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.core.ResolvableType.forClass;

/**
 * @author Arjen Poutsma
 */
public class ResourceDecoderTests extends AbstractDecoderTestCase<ResourceDecoder> {

	private final byte[] fooBytes = "foo".getBytes(StandardCharsets.UTF_8);

	private final byte[] barBytes = "bar".getBytes(StandardCharsets.UTF_8);


	public ResourceDecoderTests() {
		super(new ResourceDecoder());
	}

	@Override
	@Test
	public void canDecode() {
		assertTrue(this.decoder.canDecode(forClass(InputStreamResource.class), MimeTypeUtils.TEXT_PLAIN));
		assertTrue(this.decoder.canDecode(forClass(ByteArrayResource.class), MimeTypeUtils.TEXT_PLAIN));
		assertTrue(this.decoder.canDecode(forClass(Resource.class), MimeTypeUtils.TEXT_PLAIN));
		assertTrue(this.decoder.canDecode(forClass(InputStreamResource.class), MimeTypeUtils.APPLICATION_JSON));
		assertFalse(this.decoder.canDecode(forClass(Object.class), MimeTypeUtils.APPLICATION_JSON));
	}


	@Override
	@Test
	public void decode() {
		Flux<DataBuffer> input = Flux.concat(dataBuffer(this.fooBytes), dataBuffer(this.barBytes));

		testDecodeAll(input, Resource.class, step -> step
				.consumeNextWith(resource -> {
					try {
						byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
						assertEquals("foobar", new String(bytes));
					} catch (IOException e) {
						fail(e.getMessage());
					}
				})
				.expectComplete()
				.verify());
	}

	@Override
	public void decodeToMono() {
		Flux<DataBuffer> input = Flux.concat(
				dataBuffer(this.fooBytes),
				dataBuffer(this.barBytes));

		testDecodeToMonoAll(input, Resource.class, step -> step
				.consumeNextWith(resource -> {
					try {
						byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
						assertEquals("foobar", new String(bytes));
					} catch (IOException e) {
						fail(e.getMessage());
					}
				})
				.expectComplete()
				.verify());
	}

}
