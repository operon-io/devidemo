////////////////////////////////////////////////////////////////////////
//
// PropertyRule.java
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


public final class PropertyRule {
	public enum NameMatchKind { Exact, Pattern, All }

	private String _nameMatch;
	private NameMatchKind _matchKind;
	private Reference _valueAcceptor;
	private Reference _presentAcceptor;
	private Reference _absentAcceptor;

	public String getNameMatch() { return _nameMatch; }
	public NameMatchKind getMatchKind() { return _matchKind; }
	public Reference getValueAcceptor() { return _valueAcceptor; }
	public Reference getPresentAcceptor() { return _presentAcceptor; }
	public Reference getAbsentAcceptor() { return _absentAcceptor; }

	public PropertyRule(String nameMatch, NameMatchKind matchKind, Reference valueAcceptor, Reference presentAcceptor, Reference absentAcceptor) {
		_nameMatch = nameMatch;
		_matchKind = matchKind;
		_valueAcceptor = valueAcceptor;
		_presentAcceptor = presentAcceptor;
		_absentAcceptor = absentAcceptor;
	}
}