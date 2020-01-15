/*
 * Copyright 2002-2018 the original author or authors.
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

package org.springframework.util.xml;

import org.junit.Test;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

/**
 * @author Arjen Poutsma
 * @author Andrzej Hołowko
 */
public class ListBasedXMLEventReaderTests {

	private final XMLInputFactory inputFactory = XMLInputFactory.newInstance();

	private final XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();


	@Test
	public void standard() throws Exception {
		String xml = "<foo><bar>baz</bar></foo>";
		List<XMLEvent> events = readEvents(xml);

		ListBasedXMLEventReader reader = new ListBasedXMLEventReader(events);

		StringWriter resultWriter = new StringWriter();
		XMLEventWriter writer = this.outputFactory.createXMLEventWriter(resultWriter);
		writer.add(reader);

		assertThat(resultWriter.toString(), isSimilarTo(xml));
	}

	@Test
	public void testGetElementText() throws Exception {
		String xml = "<foo><bar>baz</bar></foo>";
		List<XMLEvent> events = readEvents(xml);

		ListBasedXMLEventReader reader = new ListBasedXMLEventReader(events);

		assertEquals(START_DOCUMENT, reader.nextEvent().getEventType());
		assertEquals(START_ELEMENT, reader.nextEvent().getEventType());
		assertEquals(START_ELEMENT, reader.nextEvent().getEventType());
		assertEquals("baz", reader.getElementText());
		assertEquals(END_ELEMENT, reader.nextEvent().getEventType());
		assertEquals(END_DOCUMENT, reader.nextEvent().getEventType());
	}

	@Test
	public void testGetElementTextThrowsExceptionAtWrongPosition() throws Exception {
		String xml = "<foo><bar>baz</bar></foo>";
		List<XMLEvent> events = readEvents(xml);

		ListBasedXMLEventReader reader = new ListBasedXMLEventReader(events);

		assertEquals(START_DOCUMENT, reader.nextEvent().getEventType());

		try {
			reader.getElementText();
			fail("Should have thrown XMLStreamException");
		} catch (XMLStreamException ex) {
			// expected
			assertTrue(ex.getMessage().startsWith("Not at START_ELEMENT"));
		}
	}

	private List<XMLEvent> readEvents(String xml) throws XMLStreamException {
		XMLEventReader reader = this.inputFactory.createXMLEventReader(new StringReader(xml));
		List<XMLEvent> events = new ArrayList<>();
		while (reader.hasNext()) {
			events.add(reader.nextEvent());
		}
		return events;
	}

}
