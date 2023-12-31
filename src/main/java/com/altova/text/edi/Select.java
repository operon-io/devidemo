////////////////////////////////////////////////////////////////////////
//
// Select.java
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

import java.io.IOException;
import java.util.ArrayList;

import com.altova.text.Generator;
import com.altova.text.ITextNode;
import com.altova.text.ITextNodeList;
import com.altova.text.edi.Parser.Context;

public class Select extends StructureItem {

	private String msField;
	private String msPrefix;
	//private String msType;

	public Select (String name, String sPrefix, String sField, String sType, Particle[] children) {
		super(name, ITextNode.Select, children);
		msPrefix = sPrefix;
		msField = sField;
		//msType = sType;
	}
	
	@Override
	public boolean read(Context context) {
		Scanner tmpScanner = new Scanner( context.getScanner().mText, context.getScanner().getServiceChars(), context.getParser().getEDIKind());
		tmpScanner.setCurrentState( context.getScanner().getCurrentState() );
		Generator tmpGenerator = new Generator();
		EDISettings tmpSettings = new EDISettings();
		EDISemanticValidator tmpValidator = new EDISemanticValidator( tmpSettings ); // no validation
		Context preScanCtx = (new Parser()).new Context( context.getParser(), tmpScanner, mChildren[0], tmpGenerator, tmpValidator);

		//backup parseinfo data
		Parser.ParseInfo parseInfoBackup = context.getParser().cloneParseInfo();
		if (mChildren[0].getNode().read(preScanCtx)) {
			String sMessage;

			if (msField.equals("@HL7_old")) {
				sMessage = preScanCtx.getGenerator().getNodeValueByPath("MSH/MSH-9/CM_MSG-1") + "_" +
					preScanCtx.getGenerator().getNodeValueByPath("MSH/MSH-9/CM_MSG-2");
			} else if (msField.equals("@HL7")) {
				sMessage = preScanCtx.getGenerator().getNodeValueByPath("MSH/MSH-9/MSG-1") + "_" +
					preScanCtx.getGenerator().getNodeValueByPath("MSH/MSH-9/MSG-2");
			} else if (msField.contains("+")) {
				String[] fields = msField.split("\\+");
				sMessage = "";
				for (String field : fields) {
					if (sMessage.length() > 0)
						sMessage += "_";
					sMessage += preScanCtx.getGenerator().getNodeValueByPath(field);
				}
			} else {
				sMessage = preScanCtx.getGenerator().getNodeValueByPath(msField);
			}

			//restore parseinfo
			context.getParser().setParseInfo(parseInfoBackup);
			if (sMessage != null && sMessage.length() > 0) {
				ArrayList<Message> filtered = context.getParser().filterMessages(sMessage);
				for (int i = 0 ; i < filtered.size() ; ++i) {
					Message m = filtered.get( i );
					if (m.getRootParticle().getNode().read( context.newContext( context, m.getRootParticle() ) ))
						return true;
				}
			}
			if (sMessage != null && context.getParser().getSettings().getStandard() == EDISettings.EDIStandard.EDITRADACOMS && sMessage.equals( "RSGRSG" )) {
				context.getParser().setParseInfo(parseInfoBackup);
				return false; // this message is handled at interchange level
			}

			throw new com.altova.AltovaException ("Message type '" + sMessage + "' unknown.");
		}

		//restore parseinfo
		context.getParser().setParseInfo(parseInfoBackup);

		return false;
	}

	@Override
	public void write(Writer writer, ITextNode node, Particle particle) throws IOException {
		boolean anyMessageWritten = false;
		if (node != null) {
			for (String key : writer.mMessages.keySet()) {
				ITextNodeList children = node.getChildren().filterByName(msPrefix + writer.mMessages.get(key).getMessageType());
				for (int i = 0; i < children.size(); i++) {
					writer.mValidator.setCurrentMessageType( key );
					Particle p = writer.mMessages.get(key).getRootParticle();
					p.getNode().write(writer, children.getAt(i), p);
					anyMessageWritten = true;
				}
			}
		}

		if (!anyMessageWritten) {
			//report error/warning
			writer.handleError(
					 node,
					 Parser.ErrorType.MissingGroup,
					 ErrorMessages.GetMissingGroupMessage("Message")
			);
		}
	}

}
