/**
 * exampleDocument.java
 *
 * This file was generated by MapForce 2022r2.
 *
 * YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
 * OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.
 *
 * Refer to the MapForce Documentation for further details.
 * http://www.altova.com/mapforce
 */

package com.mapforce.example;



import com.altova.text.tablelike.ColumnSpecification;
import com.altova.text.tablelike.Header;
import com.altova.text.tablelike.ISerializer;
import com.altova.text.tablelike.csv.Table;
import com.altova.text.tablelike.csv.Serializer;

public class exampleDocument extends Table {
	protected ISerializer createSerializer() {
		Serializer result= new Serializer(this,0);
		result.getFormat().setAssumeFirstRowAsHeaders(true);
		result.getFormat().setFieldDelimiter(';');
		
		result.getFormat().setQuoteCharacter('\"');
		
		result.getFormat().setRemoveEmpty(true);
		result.getFormat().setAlwaysQuote(0 == 1);
		return result;
	}
	protected void initHeader(Header header) {
		
		header.add(new ColumnSpecification("id"));
		header.add(new ColumnSpecification("date"));
		header.add(new ColumnSpecification("arrival"));
		header.add(new ColumnSpecification("departure"));
		header.add(new ColumnSpecification("extra_info"));
		header.add(new ColumnSpecification("firstname"));
		header.add(new ColumnSpecification("lastname"));
		header.add(new ColumnSpecification("address"));
		header.add(new ColumnSpecification("city"));
		header.add(new ColumnSpecification("zipCode"));
		header.add(new ColumnSpecification("mobilePhone"));
	}
	public exampleDocument(com.altova.typeinfo.TypeInfo tableType,int lineend) {
		super( tableType,lineend);
	}
}


