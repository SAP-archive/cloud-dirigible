/*******************************************************************************
 * Copyright (c) 2014 SAP AG or an SAP affiliate company. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *******************************************************************************/

package com.sap.dirigible.runtime.agent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceProvider;

import org.apache.camel.ProducerTemplate;

import com.sap.dirigible.runtime.logger.Logger;

@WebServiceProvider
@ServiceMode(value = Service.Mode.MESSAGE)
public class WsEndpointProviderImplementor implements Provider<SOAPMessage> {

	private static final String INVOKE = "invoke"; //$NON-NLS-1$

	private static final Logger logger = Logger.getLogger(WsEndpointProviderImplementor.class.getCanonicalName());

	public static final String PROCESSOR_TYPE_CAMEL_PARAMETER = "CAMEL"; //$NON-NLS-1$
	public static final int PROCESSOR_TYPE_CAMEL = 1;

	private ConfigurationAgent configurationAgent;
	private String processor;
	private int processorType = PROCESSOR_TYPE_CAMEL;

	public WsEndpointProviderImplementor(ConfigurationAgent configurationAgent,
			String processorType, String processor) {
		this.configurationAgent = configurationAgent;
		this.processor = processor;
		if (PROCESSOR_TYPE_CAMEL_PARAMETER.equalsIgnoreCase(processorType)) {
			this.processorType = PROCESSOR_TYPE_CAMEL;
		}
	}

	@Override
	public SOAPMessage invoke(SOAPMessage message) {
		SOAPMessage responseMessage = null;
		try {
			// serialize to XML
			ByteArrayOutputStream baos = serializeMessage(message);

			String responseXML = ""; //$NON-NLS-1$
			if (processorType == PROCESSOR_TYPE_CAMEL) {
				// processor is camel route starting with direct:...
				ProducerTemplate template = configurationAgent
						.getCamelContext().createProducerTemplate();
				// TODO check whether processor endpoint exists
				responseXML = template.requestBody(processor, new String(
					baos.toByteArray()), String.class);
			}

			responseMessage = MessageFactory.newInstance().createMessage(null,
					new ByteArrayInputStream(responseXML.getBytes()));

		} catch (IOException e) {
			logger.error(INVOKE, e);
			e.printStackTrace();
		} catch (SOAPException e) {
			logger.error(INVOKE, e);
			e.printStackTrace();
		}

		return responseMessage;
	}

	private ByteArrayOutputStream serializeMessage(SOAPMessage message)
			throws SOAPException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		message.writeTo(baos);
		return baos;
	}

}
