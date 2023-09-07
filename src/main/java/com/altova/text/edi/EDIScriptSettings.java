////////////////////////////////////////////////////////////////////////
//
// EDIScriptSettings.java
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

// Encapsulates NCPDP SCRIPT specific settings

public class EDIScriptSettings extends EDIFactSettings {

	public EDIScriptSettings() {
		super.mEDIStandard = EDIStandard.EDISCRIPT;
		setControllingAgency( "UNO" );
		setSyntaxVersionNumber( 0 );
	}	
	
}
