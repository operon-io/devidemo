////////////////////////////////////////////////////////////////////////
//
// ColumnFixed.java
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

public class ColumnFixed {
	public Command next;
	public int width;
	public char fillChar;
	public int alignment;
	public String name;

	public ColumnFixed(Command next, int width, char fillChar, int alignment, String name) {
		this.next = next;
		this.width = width;
		this.fillChar = fillChar;
		this.alignment = alignment;
		this.name = name;
	}
}
