////////////////////////////////////////////////////////////////////////
//
// DataTypeValidator.java
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

public abstract class DataTypeValidator {

	int mMinLength;
	int mMaxLength;
	DataValueValidator mValidator;

	public int getMinLength() {
		return mMinLength;
	}
	
	public int getMaxLength() {
		return mMaxLength;
	}
	
	public boolean isIncomplete() {
		if (mValidator != null)
			return mValidator.isIncomplete();
		return false;
	}

	public boolean hasValue(String value) {
		if (mValidator != null)
			return mValidator.hasValue(value);
		return true;
	}
	
	public String getCodeListValues() {
		if (mValidator != null)
			return mValidator.getCodeListValues();
		return "";
	}
	
	protected DataTypeValidator(int minLength, int maxLength, DataValueValidator validator) {
		mMinLength = minLength;
		mMaxLength = maxLength;
		mValidator = validator;
	}
	
	protected void validateLength( int effLen, String s, Parser.Context context, Scanner.State beforeRead) {
		//report error/warning
		if (effLen > getMaxLength()) {
			context.handleError(
				Parser.ErrorType.DataElementTooLong,
				ErrorMessages.GetDataElementTooLongMessage(
					context.getParticle().getNode().getName(),
					getMaxLength(),
					s
				),
				new ErrorPosition( beforeRead ),
				s
			);
		} else if( effLen < getMinLength()) {
			context.handleError(
				Parser.ErrorType.DataElementTooShort,
				ErrorMessages.GetDataElementTooShortMessage(
					context.getParticle().getNode().getName(),
					getMaxLength(),
					s
				),
				new ErrorPosition( beforeRead ),
				s
			);
		}
	}
	
	protected void validateLength( int effLen, String s, ITextNode node, Writer writer) {
		//report error/warning
		if (effLen > getMaxLength()) {
			writer.handleError(
				node,
				Parser.ErrorType.DataElementTooLong,
				ErrorMessages.GetDataElementTooLongMessage(
					node.getName(),
					getMaxLength(),
					s
				)
			);
		} else if( effLen < getMinLength()) {
			writer.handleError(
				node,
				Parser.ErrorType.DataElementTooShort,
				ErrorMessages.GetDataElementTooShortMessage(
					node.getName(),
					getMaxLength(),
					s
				)
			);
		}
	}
	
	// 2 Trim functions which are written for StringBuffer
	static public void trimRight( StringBuffer sBuffer, String sTrim) {
		int i;
		for (i = sBuffer.length() - 1; i >= 0 && sTrim.indexOf(sBuffer.charAt(i)) >= 0; --i);
		
		sBuffer.delete( i + 1, sBuffer.length());
	}
	
	static public void trimLeft( StringBuffer sBuffer, char cTrim) {
		int i;
		for (i = 0; i < sBuffer.length() && sBuffer.charAt(i) == cTrim ; ++i);
		
		sBuffer.delete( 0, i);
	}

	protected int effectiveLength(StringBuffer s, char escape) {
		int len = s.length();
		if (escape == '\0')
			return len;

		for (int i=0; i < s.length(); ++i)
			if (s.charAt(i) == escape) {
				len--;
				++i;
			}
		return len;
	}

	protected void escape(StringBuffer s, ServiceChars sc) {
		char escapeChar = sc.getReleaseCharacter();

		for (int i=0; i< s.length(); ++i)
			if (sc.mustEscape( s.charAt(i)) )
				if (escapeChar != 0)
					s.insert(i++, escapeChar);
				else
					s.setCharAt(i, ' ');
	}

	protected void escapeHL7(StringBuffer s, ServiceChars sc) {
		StringBuffer sbEscaped = new StringBuffer();
		boolean skipEscape = false;
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			if (sc.mustEscape( ch ) && !skipEscape) {
				if (sc.getReleaseCharacter() != 0) {
					char chNext = i + 1 < s.length() ? s.charAt(i+1) : 0;
					if (ch == sc.getReleaseCharacter()
						&& (chNext == EDIHL7Settings.cEscNormalText
						|| chNext == EDIHL7Settings.cEscStartHighlight
						|| chNext == EDIHL7Settings.cEscHexadecimalData
						|| chNext == EDIHL7Settings.cEscLocalEscapeSeq) ) {
						skipEscape = true;
						sbEscaped.append( ch );
					} else {
						sbEscaped.append( sc.getReleaseCharacter() );
						
						if (ch == sc.getDataElementSeparator()) sbEscaped.append( EDIHL7Settings.cEscFieldSeparator );
						else if(ch == sc.getSubComponentSeparator()) sbEscaped.append( EDIHL7Settings.cEscSubComponentSeparator );
						else if(ch == sc.getComponentSeparator()) sbEscaped.append( EDIHL7Settings.cEscComponentSeparator );
						else if(ch == sc.getReleaseCharacter()) sbEscaped.append( EDIHL7Settings.cEscEscapeSeparator );
						else if(ch == sc.getRepetitionSeparator()) sbEscaped.append( EDIHL7Settings.cEscRepetitionSeparator );
						
						sbEscaped.append( sc.getReleaseCharacter() );
					}
				} else
					sbEscaped.append( ' ');
			} else {
				skipEscape = skipEscape && ch != sc.getReleaseCharacter();
				sbEscaped.append( ch );
			}
		}
		
		//apply new stringbuffer
		s.delete(0, s.length());
		s.append( sbEscaped);
	}

	public abstract boolean makeValidOnRead (StringBuffer s, Parser.Context context, Scanner.State beforeRead);
	public abstract boolean makeValidOnWrite (StringBuffer s, ITextNode node, Writer writer, boolean esc);
	public abstract boolean makeValidOnWrite (StringBuffer s, ITextNode node, Writer writer);
}	
