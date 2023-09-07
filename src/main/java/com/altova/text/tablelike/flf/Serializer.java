////////////////////////////////////////////////////////////////////////
//
// Serializer.java
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

package com.altova.text.tablelike.flf;

import java.io.IOException;

import com.altova.text.tablelike.ColumnSpecification;
import com.altova.text.tablelike.MappingException;
import com.altova.text.tablelike.Record;
import com.altova.text.tablelike.RecordBasedParser;
import com.altova.text.tablelike.Table;

public class Serializer extends com.altova.text.tablelike.Serializer {
	private Format m_Format = new Format(0);

	private String assureCorrectFieldFormat(String rhs, int len)
			throws MappingException {
		if (null==rhs)
			rhs= "";
		if (rhs.length() > len)
			throw new MappingException("Field is too long for specification.");
		char c = m_Format.getFillCharacter();
		StringBuffer buffer = new StringBuffer(len);
		buffer.append(rhs);
		for (int i = rhs.length(); i < len; ++i)
			buffer.append(c);
		return buffer.toString();
	}

	private void writeRecord(Record record) throws IOException,
			MappingException {
		int fieldcount = super.getTable().getHeader().size();
		for (int i = 0; i < fieldcount; ++i) {
			ColumnSpecification cs = super.getTable().getHeader().getAt(i);
			int desiredlength = cs.getLength();
			String value = record.getAt(i);
			value = this.assureCorrectFieldFormat(value, desiredlength);
			super.getStream().write(value);
		}
		if (m_Format.getAssumeRecordDelimitersPresent())
			super.getStream().write(m_Format.getLineEnd());
	}

	public Serializer(Table table, int lineEnd) {
		super(table);
		this.m_Format = new Format(lineEnd);
	}

	public Format getFormat() {
		return m_Format;
	}

	protected RecordBasedParser createParser() {
		return new Parser(m_Format, super.getTable().getHeader());
	}

	protected void doSerialize() throws IOException, MappingException {
		for (int i = 0; i < super.getTable().size(); ++i)
			this.writeRecord(super.getTable().getAt(i));
	}

	protected boolean doStoreRecord(Record record) {
		return true;
	}

}
