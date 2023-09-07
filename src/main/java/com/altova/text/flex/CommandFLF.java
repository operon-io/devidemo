////////////////////////////////////////////////////////////////////////
//
// CommandFLF.java
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

package com.altova.text.flex;

import com.altova.text.*;

public class CommandFLF extends Command {
	private Splitter splitter;
	private ColumnFixed[] columns;
	private boolean columnHeaders;
	private boolean removeEmpty;

	public CommandFLF(String name, ColumnFixed[] columns, boolean headers, Splitter splitter, boolean removeEmpty) {
		super(name);
		this.columns = columns;
		this.columnHeaders = headers;
		this.splitter = splitter;
		this.removeEmpty = removeEmpty;
	}
	
	public boolean readText(DocumentReader doc) {
		Range range = new Range(doc.getRange());

		if (columnHeaders) {
			/*Range firstLine = */splitter.split(range);
		}
				
		while (range.isValid()) {
			Range lineRange = splitter.split(range);

			doc.getOutputTree().enterElement(getName(), ITextNode.Group);
			{
				for (int col = 0; col < columns.length; ++col) {
					SplitAtPosition splitField = new SplitAtPosition(columns[col].width);
					Range cellRange = splitField.split(lineRange);
					if (columns[col].next != null) {
						DocumentReader cell = new DocumentReader(doc, cellRange);
						if (!removeEmpty || cellRange.toString().trim().length() > 0)
							columns[col].next.readText(cell);
					}
				}
			}
			doc.getOutputTree().leaveElement(getName());
		}
		return true;
	}

	public boolean writeText(DocumentWriter doc) {
		if (columnHeaders) {
			for (int col = 0; col < columns.length; ++col) {
				StringBuffer cellHeader = new StringBuffer(columns[col].name);
				doc.appendText(formatCell(cellHeader, columns[col]));
			}
			splitter.appendDelimiter(doc);
		}

		TextNodeList children = doc.getCurrentNode().getChildren().filterByName(getName());
		for (int row = 0; row < children.size(); ++row)	{
			ITextNode rowNode = children.getAt(row);

			for (int col = 0; col < columns.length; ++col) {
				if (columns[col].next != null) {
					StringBuffer cellString = new StringBuffer();
					DocumentWriter cellDoc = new DocumentWriter(rowNode, cellString, doc.getLineEnd());
					columns[col].next.writeText(cellDoc);
					doc.appendText(formatCell(cellString, columns[col]));
				}
			}
			splitter.appendDelimiter(doc);
		}
		return children.size() != 0;
	}

	private String formatCell(StringBuffer str, ColumnFixed col) {
		if (col.alignment == 1) {
			// align right
			if (str.length() > col.width)
				str.delete(0, str.length() - col.width);
			StringBuffer result = new StringBuffer();
			for (int i = col.width - str.length(); i > 0; --i)
				result.append(col.fillChar);
			result.append(str);
			return str.toString();
		} else {
			// align left
			if (str.length() > col.width)
				str.delete(col.width, str.length());
			for (int i = col.width - str.length(); i > 0; --i)
				str.append(col.fillChar);
			return str.toString();
		}
	}
	
}
