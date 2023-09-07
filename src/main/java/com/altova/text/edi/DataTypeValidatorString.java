////////////////////////////////////////////////////////////////////////
//
// DataTypeValidatorString.java
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

import com.altova.text.ITextNode;

public class DataTypeValidatorString extends DataTypeValidator {

	public DataTypeValidatorString (int minLength, int maxLength, DataValueValidator validator) {
		super (minLength, maxLength, validator);
	}

	public boolean makeValidOnRead (StringBuffer s, Parser.Context context, Scanner.State beforeRead) {
		if (context.getParser().getEDIKind() == EDISettings.EDIStandard.EDIFixed)
			trimRight(s, " \r");
		
		int effLen = effectiveLength(s, context.getScanner().getServiceChars().getReleaseCharacter());
		validateLength(effLen, s.toString(), context, beforeRead);

		return true;
	}

	public boolean makeValidOnWrite (StringBuffer s, ITextNode node, Writer writer) {
		return makeValidOnWrite(s, node, writer, true);
	}

	public boolean makeValidOnWrite (StringBuffer s, ITextNode node, Writer writer, boolean esc) {
		if (esc) {
			if (writer.getEDIKind() == EDISettings.EDIStandard.EDIHL7)
				escapeHL7(s, writer.getServiceChars() );
			else
				escape(s, writer.getServiceChars());
		}

		int toPad = getMinLength() - s.length();

		//use correct padding for fixed configs
		if (writer.getEDIKind() == EDISettings.EDIStandard.EDIFixed) {
			toPad = mMaxLength - s.length();
			if (s.length() > getMaxLength())
				s.delete( getMaxLength(), s.length() );	
		}

		if (writer.getEDIKind() == EDISettings.EDIStandard.EDITRADACOMS) {
			s.replace(0, s.length(), s.toString().toUpperCase());
		}
		
		if (toPad >0) {
			StringBuffer spad = new StringBuffer();
			for (int i=0; i< toPad; ++i)
				spad.append( ' ');
			s.append(spad);
		}

		int effLen = effectiveLength(s, writer.getServiceChars().getReleaseCharacter());
		validateLength(effLen, s.toString(), node, writer);
		return true;
	}
}
