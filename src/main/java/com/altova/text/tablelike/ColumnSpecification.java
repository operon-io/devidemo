////////////////////////////////////////////////////////////////////////
//
// ColumnSpecification.java
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

package com.altova.text.tablelike;

public class ColumnSpecification {
	private String m_Name = "";

	private int m_Length = 0;

	public ColumnSpecification() {
	}

	public ColumnSpecification(String name) {
		m_Name = name;
	}

	public ColumnSpecification(String name, int length) {
		m_Name = name;
		m_Length = length;
	}

	public String getName() {
		return m_Name;
	}

	public void setName(String rhs) {
		m_Name = rhs;
	}

	public int getLength() {
		return m_Length;
	}

	public void setLength(int rhs) {
		m_Length = rhs;
	}
}
