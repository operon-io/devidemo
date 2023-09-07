////////////////////////////////////////////////////////////////////////
//
// TextDocument.java
//
// This file was generated by MapForce 2022r2.
//
// YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
// OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.
//
// Refer to the MapForce Documentation for further details.
// http://www.altova.com/mapforce
//
////////////////////////////////////////////////////////////////////////

package com.altova.text.edi;

import com.altova.text.*;

import java.io.OutputStreamWriter;
import java.io.IOException;

import com.altova.AltovaException;

public abstract class TextDocument extends Parser {

	private Generator mGenerator = new Generator();
	private String mStructureName = "";
	private String m_Encoding = "";
	private boolean m_bBigEndian = false;
	private boolean m_bBOM = false;
	private Particle mRootParticle;

	protected TextDocument (Particle rootParticle) {
		this.mRootParticle = rootParticle;
	}

	public void setEncoding( String encoding, boolean bBigEndian, boolean bBOM ) {
		m_Encoding = encoding;
		m_bBigEndian = bBigEndian;
		m_bBOM = bBOM;
	}

	private StringBuffer loadBuffer(FileIO io) {
		StringBuffer result;
		try {
			result = io.readToEnd();
		} catch (IOException x) {
			throw new AltovaException(x);
		}
		int i = 0;
		while (Character.isWhitespace(result.charAt(i)))
			++i;
		if (i > 0)
			result.delete(0, i - 1);
		return result;
	}

	public Generator getGenerator() {
		return mGenerator;
	}

	public String getStructureName() {
		return mStructureName;
	}

	public void setStructureName(String rhs) {
		mStructureName = rhs;
	}

	public ITextNode parseFile(java.io.InputStream stream) throws Exception {
		try (FileIO io = new FileIO(stream, m_Encoding, m_bBigEndian, m_bBOM)) {
			return parseFile(loadBuffer( io ));
		}
	}

	public ITextNode parseFile(java.io.Reader reader) throws Exception {
		try (FileIO io = new FileIO(reader)) {
			return parseFile(loadBuffer( io ));
		}
	}

	private ITextNode parseFile(StringBuffer buffer) throws Exception {
		if (validateSource(buffer)) {
			super.parse(mRootParticle, buffer.toString(), mGenerator, getSettings());
			mGenerator.resetToRoot();
			validateResult();
			return mGenerator.getRootNode();
		}
		return null;
	}

	public void parse(com.altova.io.Input input) throws Exception {
		switch (input.getType()) {
			case com.altova.io.Input.IO_DOM:
				throw new Exception ("This is text component, it cannot be read from DOM!");

			case com.altova.io.Input.IO_STREAM:
				parse(input.getStream());
				break;

			case com.altova.io.Input.IO_READER:
				parse(input.getReader());
				break;

			default:
				throw new Exception ("Unknown output type");
		}
	}

	public void parse(java.io.InputStream stream) throws Exception {
		parseFile(stream);
	}

	public void parse(java.io.Reader reader) throws Exception {
		parseFile(reader);
	}

	protected abstract boolean validateSource(StringBuffer source);

	protected boolean validateResult() {
		ITextNodeList rootnodes = mGenerator.getRootNodes();
		for (int i = 0; i < rootnodes.size(); ++i) {
			this.removeEmptyNodes(rootnodes.getAt(i));
		}
		
		return true;
	}

	public void save(com.altova.io.Output output) throws Exception {
		switch (output.getType()) {
			case com.altova.io.Output.IO_DOM:
				throw new Exception ("This is text component, it cannot be written into DOM!");

			case com.altova.io.Output.IO_STREAM:
				save(output.getStream());
				break;

			case com.altova.io.Output.IO_WRITER:
				save(output.getWriter());
				break;

			default:
				throw new Exception ("Unknown output type");
		}
	}

	public void save(java.io.OutputStream stream) throws Exception {
		//try (FileIO io = new FileIO(stream, m_Encoding, m_bBigEndian, m_bBOM)) { // CloseObjectsAfterRun property controls whether to close stream, do not auto close it with FileIO!
			FileIO io = new FileIO(stream, m_Encoding, m_bBigEndian, m_bBOM);
			OutputStreamWriter writer = io.openWriteStream();
			save(writer);
		//}
	}

	public void save(java.io.Writer writer) throws Exception {
		ITextNodeList rootnodes = mGenerator.getRootNodes();

		if (getSettings().getAutoCompleteData()) {
			DataCompletion datacompletion = null;

			switch (getEDIKind()) {
				case EDIFACT:
					datacompletion = new EDIFactDataCompletion(this, (EDIFactSettings) getSettings(), mStructureName);
					break;
				case EDIX12:
					datacompletion = new EDIX12DataCompletion(this, (EDIX12Settings) getSettings(), mStructureName);
					break;
				case EDIHL7:
					datacompletion = new EDIHL7DataCompletion(this, (EDIHL7Settings) getSettings(), mStructureName);
					break;
				case EDIFixed:
					datacompletion = new EDIFixedDataCompletion(this, (EDIFixedSettings) getSettings(), mStructureName);
					break;
				case EDITRADACOMS:
					datacompletion = new EDITradacomsDataCompletion(this, (EDITradacomsSettings) getSettings(), mStructureName);
					break;
				case EDISCRIPT:
					datacompletion = new EDIScriptDataCompletion(this, (EDIScriptSettings) getSettings(), mStructureName);
					break;
			}
			if (datacompletion != null) {
				for (int i = 0; i < rootnodes.size(); ++i) {
					datacompletion.completeData(rootnodes.getAt(i), mRootParticle);
				}
			} else
				System.err.println( "No data completion will be performed");
		}

		Writer ediWriter = new Writer(writer, mMessages, getSettings(), getErrorSettings(), getSettings().getLineEnd() );
		ediWriter.setNewlineAfterSegments(getSettings().getTerminateSegmentsWithLinefeed());

		for (int i = 0; i < rootnodes.size(); ++i) {
			mRootParticle.getNode().write(ediWriter, rootnodes.getAt(i), mRootParticle);
		}
		writer.flush();
	}

	private boolean isEmptyDataElement(ITextNode node) {
		return ((ITextNode.DataElement == node.getNodeClass()) && (0 == node
				.getValue().length()));
	}

	private boolean isNodeContainerWithoutChildren(ITextNode node) {
		byte nodeClass = node.getNodeClass();
		return (nodeClass == ITextNode.Composite ||
				nodeClass == ITextNode.Group ||
				nodeClass == ITextNode.SubComposite) &&
				(node.getChildren().size() == 0);
	}

	private void removeEmptyNodes(ITextNode node) {
		int i = 0;
		while (i < node.getChildren().size()) {
			ITextNode kid = node.getChildren().getAt(i);
			removeEmptyNodes(kid);
			if ((isEmptyDataElement(kid))
					|| (isNodeContainerWithoutChildren(kid)))
				node.getChildren().removeAt(i);
			else
				++i;
		}
	}
}
