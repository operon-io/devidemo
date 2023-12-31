////////////////////////////////////////////////////////////////////////
//
// Document.java
//
// This file was generated by MapForce MapForce 2022r2.
//
// YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
// OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.
//
// Refer to the MapForce Documentation for further details.
// http://www.altova.com/mapforce
//
////////////////////////////////////////////////////////////////////////

package com.altova.json;

import com.altova.mapforce.IEnumerable;


public class Document implements com.altova.mapforce.IMFNode, com.altova.mapforce.IMFDocumentNode {
	Value value;
	String uri;
		
	public Value getRootValue() { return value; }
	public String getUri() { return uri; }

	public Document(Value value, String uri) {
		this.value = value;
		this.uri = uri;
	}

	public String getDocumentUri() {
		return uri;
	}
	
	public String getLocalName() {
		assert false : "Method is not implemented.";
		return "";
	}

	public String getNamespaceURI() {
		assert false : "Method is not implemented.";
		return "";
	}

	public int /*Altova.Mapforce.MFNodeKind*/ getNodeKind() {
		assert false : "Method is not implemented.";
		return -1;
	}

	public String getNodeName() {
		assert false : "Method is not implemented.";
		return "";
	}

	public javax.xml.namespace.QName getQName() {
		assert false : "Method is not implemented.";
		return null;
	}

	public String getPrefix() {
		assert false : "Method is not implemented.";
		return "";
	}

	public String value() throws Exception {
		throw new UnsupportedOperationException("Method is not implemented.");
	}

	public java.lang.Object typedValue() throws Exception {
		throw new UnsupportedOperationException("Method is not implemented.");
	}

	public javax.xml.namespace.QName qnameValue() {
		assert false : "Method is not implemented.";
		return null;
	}

	public IEnumerable select(int mfQueryKind, java.lang.Object query) {
		assert false : "Method is not implemented.";
		return null;
	}
}
