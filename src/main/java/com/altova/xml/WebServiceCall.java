/**
 * WebServiceCall.java
 *
 * This file was generated by MapForce 2022r2.
 *
 * YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
 * OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.
 *
 * Refer to the MapForce Documentation for further details.
 * http://www.altova.com/mapforce
 */

package com.altova.xml;

import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class WebServiceCall {
	private String endpointURL;
	private String soapAction;
	private String encoding;
	private String operationName;
	private String WSDLTargetNamespace;
	private String operationLocation;
	private byte style;
	private byte httpContentType;

	private String username = "";
	private String password = "";
	
	private String soapenvUri = "http://schemas.xmlsoap.org/soap/envelope/";
	private String soapencUri = "http://schemas.xmlsoap.org/soap/encoding/";

	private byte soapVer;
	
	public static final byte UNKNOWN = 0;

	public static final byte SOAP_RPC_ENCODED = 1;
	public static final byte SOAP_DOCUMENT_LITERAL = 2;
	public static final byte HTTP_GET = 3;
	public static final byte HTTP_POST= 4;

	public static final byte HTTP_URL_ENCODED = 1;
	public static final byte HTTP_URL_REPLACEMENT = 2;
	public static final byte HTTP_XML = 3;
		
	public static final byte SOAP_11 = 11;
	public static final byte SOAP_12 = 12; 

	public WebServiceCall(String endpointURL, String WSDLTargetNamespace, String operationName, String soapAction, String encoding, byte style, byte soapVer) {
		this.endpointURL = endpointURL;
		this.soapAction = soapAction;
		this.encoding = encoding;
		this.WSDLTargetNamespace = WSDLTargetNamespace;
		this.operationName = operationName;
		this.style = style;
		this.soapVer = soapVer;
		if (soapVer == SOAP_12) {
			soapenvUri = "http://www.w3.org/2003/05/soap-envelope";
			soapencUri = "http://www.w3.org/2003/05/soap-encoding";
		}
	}

	public WebServiceCall(String endpointURL, String opLocation, byte contentType, String encoding, byte style, byte soapVer) {
		this.endpointURL = endpointURL;
		this.operationLocation= opLocation;
		this.httpContentType = contentType;
		this.encoding = encoding;
		this.style = style;
		this.soapVer = soapVer;
		if (soapVer == SOAP_12) {
			soapenvUri = "http://www.w3.org/2003/05/soap-envelope";
			soapencUri = "http://www.w3.org/2003/05/soap-encoding";
		}
	}

	public void setCredentials( String u, String p) {
		username = u;
		password = p;
	}

	private void fixPrefix(org.w3c.dom.Node n) {
		if (n.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE)
			return;

		String prefix = XmlTreeOperations.findPrefixForNamespace(n, n.getNamespaceURI());
		if (prefix != null)
			n.setPrefix(prefix);
		
		for (org.w3c.dom.Node c = n.getFirstChild(); c != null; c = c.getNextSibling())
			fixPrefix(c);
	}

	public org.w3c.dom.Node call (org.w3c.dom.Node in) throws Exception {
		org.w3c.dom.Node node = in;

		// non soap case
		if (style == HTTP_GET || style == HTTP_POST) {
			node=in.getFirstChild();
			HttpURLConnection conn = null;

			String urlOperation = endpointURL + operationLocation;
			String parameters = "";

			// construct parameters from parts
			if (node != null) {
				if (httpContentType == HTTP_XML && style == HTTP_POST) {
					org.w3c.dom.NodeList parts = node.getChildNodes();
					if (parts.getLength() > 1)
						throw new Exception ("HTTP POST with text/xml encoding can handle only one part");
					if (parts.getLength() == 1) {
						javax.xml.transform.Transformer xformer = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
						//xformer.setOutputProperty("indent", "yes");
						//xformer.setOutputProperty("omit-xml-declaration", "yes");
						javax.xml.transform.Source source = new javax.xml.transform.dom.DOMSource(parts.item(0));
						java.io.StringWriter swr = new java.io.StringWriter();
						xformer.transform(source, new javax.xml.transform.stream.StreamResult(swr));
						parameters = swr.toString();
					}
				} else if (httpContentType == HTTP_URL_ENCODED ) {
					while (node.hasChildNodes() && node.getFirstChild().hasChildNodes() && node.getFirstChild().getFirstChild().getNodeType() == org.w3c.dom.Node.ELEMENT_NODE)
						node = node.getFirstChild();
					
					org.w3c.dom.NodeList parts = node.getChildNodes();
					for (int i = 0; i<parts.getLength(); i++) {
						if (i>0)
							parameters += "&";
						parameters += java.net.URLEncoder.encode(parts.item(i).getLocalName(), "UTF-8");
						parameters += "=";
						parameters += java.net.URLEncoder.encode(getDomNodeValue(parts.item(i)), "UTF-8");
					}
				} else
					throw new Exception ("Unsupported mime type for HTTP binding");
			}

			if (style == HTTP_GET) {
				if (parameters.length() > 0)
					urlOperation += ("?" + parameters);
				URL url = new URL(urlOperation);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");

				if (username.length() > 0 && password.length() > 0)
					conn.setRequestProperty("Authorization", "Basic " + base64Encode(username + ":" + password));

				conn.connect();
			} else { // POST
				URL url = new URL(urlOperation);
				conn = (HttpURLConnection) url.openConnection();

				byte[] buff = parameters.getBytes(encoding);
				createHTTPPostHeader( conn, buff.length);

				conn.setDoInput(true);
				conn.setDoOutput(true);

				OutputStream connectionStream = conn.getOutputStream();
				connectionStream.write(buff);
				connectionStream.close();
			}

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				org.w3c.dom.Document doc = XmlTreeOperations.getDomBuilder().parse(conn.getInputStream());
				return doc;
			} else
				throw new Exception ("Failed: " + conn.getResponseCode() + " " + conn.getResponseMessage());
		} else if (style == SOAP_DOCUMENT_LITERAL || style == SOAP_RPC_ENCODED) {
			// turn it into text and send it away

			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			Source source = new DOMSource(in);
			StringWriter swr = new StringWriter();
			Result result = new StreamResult(swr);
			xformer.transform(source, result);
			String req = swr.toString();
			byte[] buff = req.getBytes(encoding);
			int len = buff.length;

			// calculate length set headers and send it away
			URL url = new URL(endpointURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			createHTTPPostHeader( conn, len);

			conn.setDoInput(true);
			conn.setDoOutput(true);

			OutputStream connectionStream = conn.getOutputStream();
			connectionStream.write(buff);
			connectionStream.close();

			org.w3c.dom.Document soapDoc;
			if (conn.getResponseCode() == 200)
				soapDoc = XmlTreeOperations.getDomBuilder().parse(conn.getInputStream());
			else
				soapDoc = XmlTreeOperations.getDomBuilder().parse(conn.getErrorStream());

			// check mustUnderstand
			org.w3c.dom.NodeList headers = soapDoc.getElementsByTagNameNS(soapenvUri, "Header");
			if (headers.getLength() > 1)
				throw new Exception("More than one header found");
			if (headers.getLength() == 1) {
				for (org.w3c.dom.Node headerNode = headers.item(0).getFirstChild(); headerNode != null; headerNode = headerNode.getNextSibling())
					if (headerNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
						org.w3c.dom.Attr muAtt = ((org.w3c.dom.Element)headerNode).getAttributeNodeNS(soapenvUri, "mustUnderstand");
						if (muAtt != null && (muAtt.getNodeValue().equals("1") || muAtt.getNodeValue().equals("true")))
							throw new Exception("Cannot process messages with mustUnderstand headers");
					}
			}

			return soapDoc;
		} else
			throw new Exception ("Unsupported style");
	}


	public boolean call (org.w3c.dom.Node in, org.w3c.dom.Node out) throws Exception {
		org.w3c.dom.Node node = in;

		// non soap case
		if (style == HTTP_GET || style == HTTP_POST) {
			node=in.getFirstChild();
			HttpURLConnection conn = null;

			String urlOperation = endpointURL + operationLocation;
			String parameters = "";

			// construct parameters from parts
			if (node != null) {
				org.w3c.dom.NodeList parts = node.getChildNodes();
				if (httpContentType == HTTP_XML && style == HTTP_POST) {
					if (parts.getLength() > 1)
						throw new Exception ("HTTP POST with text/xml encoding can handle only one part");
					if (parts.getLength() == 1) {
						javax.xml.transform.Transformer xformer = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
						//xformer.setOutputProperty("indent", "yes");
						//xformer.setOutputProperty("omit-xml-declaration", "yes");
						javax.xml.transform.Source source = new javax.xml.transform.dom.DOMSource(parts.item(0));
						java.io.StringWriter swr = new java.io.StringWriter();
						xformer.transform(source, new javax.xml.transform.stream.StreamResult(swr));
						parameters = swr.toString();
					}
				} else if (httpContentType == HTTP_URL_ENCODED ) {
					for (int i = 0; i<parts.getLength(); i++) {
						if (i>0)
							parameters += "&";
						parameters += java.net.URLEncoder.encode(parts.item(i).getLocalName(), "UTF-8");
						parameters += "=";
						parameters += java.net.URLEncoder.encode(getDomNodeValue(parts.item(i)), "UTF-8");
					}
				} else
					throw new Exception ("Unsupported mime type for HTTP binding");
			}

			if (style == HTTP_GET) {
				if (parameters.length() > 0)
					urlOperation += ("?" + parameters);
				URL url = new URL(urlOperation);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");

				if (username.length() > 0 && password.length() > 0)
					conn.setRequestProperty("Authorization", "Basic " + base64Encode(username + ":" + password));

				conn.connect();
			} else { // POST
				URL url = new URL(urlOperation);
				conn = (HttpURLConnection) url.openConnection();
				byte[] buff = parameters.getBytes(encoding);
				createHTTPPostHeader( conn, buff.length);

				conn.setDoInput(true);
				conn.setDoOutput(true);

				OutputStream connectionStream = conn.getOutputStream();
				connectionStream.write(buff);
				connectionStream.close();
			}

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				org.w3c.dom.Document doc = XmlTreeOperations.getDomBuilder().parse(conn.getInputStream());
				out.appendChild(out.getOwnerDocument().importNode(doc.getDocumentElement(),true));
				return true;
			} else
				throw new Exception ("Failed: " + conn.getResponseCode() + " " + conn.getResponseMessage());
		} else if (style == SOAP_DOCUMENT_LITERAL || style == SOAP_RPC_ENCODED) {
			org.w3c.dom.Document soapDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			org.w3c.dom.Element envelope = soapDoc.createElementNS(soapenvUri, "SOAP-ENV:Envelope");
			envelope.setAttribute("xmlns:SOAP-ENV", soapenvUri);
			envelope.setAttribute("xmlns:SOAP-ENC", soapencUri);
			envelope.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			envelope.setAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");

			org.w3c.dom.Element body = soapDoc.createElementNS(soapenvUri, "SOAP-ENV:Body");
			org.w3c.dom.Element header = soapDoc.createElementNS(soapenvUri, "SOAP-ENV:Header");

			if (style == SOAP_RPC_ENCODED) {
				// create rpcEnvelope
				org.w3c.dom.Element rpcEnvelope = soapDoc.createElementNS(WSDLTargetNamespace, "m:"+ operationName); // don't ask
				rpcEnvelope.setAttribute("xmlns:m", WSDLTargetNamespace);
				rpcEnvelope.setAttribute("SOAP-ENV:encodingStyle", soapencUri);
				
				// take all attributes from root element, insert them into rpcEnvelope (?)
				org.w3c.dom.NamedNodeMap attrs = node.getFirstChild().getAttributes();
				for (int i = 0; i<attrs.getLength(); i++) {
					String attrName = ((org.w3c.dom.Attr)attrs.item(i)).getName();
					rpcEnvelope.setAttribute(attrName, ((org.w3c.dom.Attr)attrs.item(i)).getValue());
				}

				// and add children of root node to rpcEnvelope
				if (node != null) {
					org.w3c.dom.NodeList parts = node.getChildNodes();
					int count = parts.getLength();
					
					// headers 0..n-1
					for (int i = 0; i < count-1; i++)
						header.appendChild(soapDoc.importNode(parts.item(i), true));
					// message is last
					org.w3c.dom.Node dummyMsgRoot = parts.item(count-1);
					
					for (int i = 0; i< dummyMsgRoot.getChildNodes().getLength(); i++)
						rpcEnvelope.appendChild(soapDoc.importNode(dummyMsgRoot.getChildNodes().item(i), true));
				}
				body.appendChild(rpcEnvelope);
			} else {
				if (node != null) {
					org.w3c.dom.NodeList parts = node.getChildNodes();
					int count = parts.getLength();
					
					// headers 0..n-1
					for (int i = 0; i < count-1; i++)
						header.appendChild(soapDoc.importNode(parts.item(i), true));
					// message is last
					org.w3c.dom.Node dummyMsgRoot = parts.item(count-1);
					
					for (org.w3c.dom.Node l = dummyMsgRoot; l != null; l = l.getParentNode()) {
						org.w3c.dom.NamedNodeMap nnm = l.getAttributes();
						if (nnm != null)
							for (int i=0; i < nnm.getLength(); i++)
								body.setAttributeNodeNS((org.w3c.dom.Attr) soapDoc.importNode(nnm.item(i), true));
					}
					
					for (int i = 0; i< dummyMsgRoot.getChildNodes().getLength(); i++)
						fixPrefix(body.appendChild(soapDoc.importNode(dummyMsgRoot.getChildNodes().item(i), true)));
				}
			}

			if (header.getChildNodes().getLength() > 0)
				envelope.appendChild(header);
			envelope.appendChild(body);
			soapDoc.appendChild(envelope);

			// turn it into text and send it away

			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			Source source = new DOMSource(soapDoc);
			StringWriter swr = new StringWriter();
			Result result = new StreamResult(swr);
			xformer.transform(source, result);
			String req = swr.toString();
			byte[] buff = req.getBytes(encoding);
			int len = buff.length;

			// calculate length set headers and send it away
			URL url = new URL(endpointURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			createHTTPPostHeader( conn, len);

			conn.setDoInput(true);
			conn.setDoOutput(true);

			OutputStream connectionStream = conn.getOutputStream();
			connectionStream.write(buff);
			connectionStream.close();

			if (conn.getResponseCode() == 200)
				soapDoc = XmlTreeOperations.getDomBuilder().parse(conn.getInputStream());
			else
				soapDoc = XmlTreeOperations.getDomBuilder().parse(conn.getErrorStream());

			// check mustUnderstand
			org.w3c.dom.NodeList headers = soapDoc.getElementsByTagNameNS(soapenvUri, "Header");
			if (headers.getLength() > 1)
				throw new Exception("More than one header found");
			if (headers.getLength() == 1) {
				for (org.w3c.dom.Node headerNode = headers.item(0).getFirstChild(); headerNode != null; headerNode = headerNode.getNextSibling())
					if (headerNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
						org.w3c.dom.Attr muAtt = ((org.w3c.dom.Element)headerNode).getAttributeNodeNS(soapenvUri, "mustUnderstand");
						if (muAtt != null && (muAtt.getNodeValue().equals("1") || muAtt.getNodeValue().equals("true")))
							throw new Exception("Cannot process messages with mustUnderstand headers");
					}
			}
			
			out.appendChild(out.getOwnerDocument().importNode(soapDoc.getDocumentElement(), true));
			return true;
		} else
			throw new Exception ("Unsupported style");
	}

	private void createHTTPPostHeader( 
			HttpURLConnection conn,
			int len) throws Exception {
		conn.setRequestMethod("POST");
		
		switch (style) {
			case HTTP_POST: {
				if (httpContentType == HTTP_XML)
					conn.setRequestProperty("Content-Type", "application/xml; charset=\"" + encoding + "\"");
				else if (httpContentType == HTTP_URL_ENCODED )
					conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				else
					throw new Exception ("Unsupported mime type for HTTP binding");
			}
			break;
			case SOAP_DOCUMENT_LITERAL:
			case SOAP_RPC_ENCODED:
			default: {
				conn.setRequestProperty("Content-Length", String.valueOf(len));
				if (soapVer == SOAP_12)
					conn.setRequestProperty("Content-Type", "application/soap+xml; action=" + soapAction);
				else {
					conn.setRequestProperty("Content-Type", "text/xml; charset=\"" + encoding + "\"");
					conn.setRequestProperty("SOAPAction", "\"" + soapAction + "\"");
				}

				if (username.length() > 0 && password.length() > 0)
					conn.setRequestProperty("Authorization", "Basic " + base64Encode(username + ":" + password));
			}
		}
	}

	private String base64Encode(String srcString) {
		String encodeArray = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

		byte[] src = srcString.getBytes();

		if (src.length == 0)
			return "";

		int tmpSize, i;
		int buff;

		tmpSize =src.length / 3 * 3;

		StringWriter dstWriter = new StringWriter((src.length * 4 / 3) + 8);

		for (i =0; i < tmpSize; i += 3) {
			buff = src[i] << 16 | src[i+1] << 8 | src[i+2];
			dstWriter.write(encodeArray.charAt((buff >> 18) & 0x3F));
			dstWriter.write(encodeArray.charAt((buff >> 12) & 0x3F));
			dstWriter.write(encodeArray.charAt((buff >> 6) & 0x3F));
			dstWriter.write(encodeArray.charAt((buff) & 0x3F));
		}

		int rest = src.length - tmpSize;
		if (rest == 2) {
			buff = src[i] << 8 | src[i+1];
			dstWriter.write(encodeArray.charAt((buff >> 10) & 0x3F));
			dstWriter.write(encodeArray.charAt((buff >> 4) & 0x3F));
			dstWriter.write(encodeArray.charAt((buff << 2) & 0x3F));
			dstWriter.write("=");
		} else if (rest == 1) {
			buff = src[i];
			dstWriter.write(encodeArray.charAt((buff >> 2) & 0x3F));
			dstWriter.write(encodeArray.charAt((buff << 4) & 0x3F));
			dstWriter.write("==");
		}

		return dstWriter.toString();
	}
	
	private String getDomNodeValue(org.w3c.dom.Node node) {
		if (node == null)
			return null;
		String value = node.getNodeValue();
		if (value != null)
			return value;
		return getInnerText(node);
	}

	private String getInnerText(org.w3c.dom.Node node) {
		org.w3c.dom.NodeList elements = node.getChildNodes();
		int length = elements.getLength();
		
		StringBuffer text = new StringBuffer();

		for (int i = 0; i < length; i++) {
			org.w3c.dom.Node child = elements.item(i);
			if (child.getNodeType() == org.w3c.dom.Node.TEXT_NODE)
				text.append(child.getNodeValue());
			else if (child.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE)
				text.append(getInnerText(child));
			else if (child.getNodeType() == org.w3c.dom.Node.CDATA_SECTION_NODE)
				text.append(child.getNodeValue());
		}
		return text.toString();
	}
}
