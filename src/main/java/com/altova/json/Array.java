////////////////////////////////////////////////////////////////////////
//
// Array.java
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

import java.util.HashMap;
import java.util.HashSet;

public class Array extends Value {
	Value[] items;

	public Type getType() { return Type.Array; }

	public Array(Value[] items) {
		this.items = items;
	}
		
	public Value[] getItems() { return items; }

	protected void ApplyDescendants(HashMap<Value, ValidationInfo> itemInfos) {
		for (Value v: items) { v.Apply(itemInfos); }
	}


	protected ValidationInfo DoValidateCore(Validator validator, ValueAcceptor schema, HashMap<Value, ValidationInfo> itemInfos) {
		if (schema.getArray() != null) {
			if (schema.getArray().getMinItems() != null && schema.getArray().getMinItems() > getItems().length)
				return new ValidationInfo(Validity.Invalid);
			if (schema.getArray().getMaxItems() != null && schema.getArray().getMaxItems() < getItems().length)
				return new ValidationInfo(Validity.Invalid);

			if (schema.getArray().getItems() != null) {
				for (int i = 0; i != getItems().length; ++i) {
					Value item = getItems()[i];
					Reference itemSchema = schema.getArray().getItems().length > i ? schema.getArray().getItems()[i] : schema.getArray().getItems()[schema.getArray().getItems().length - 1];
					ValidationInfo res = item.DoValidate(validator, itemSchema, itemInfos);
					if (!res.IsValid())
						return new ValidationInfo(Validity.Invalid);
					Merge(itemInfos, item, res);
				}
			}
			if (schema.getArray().getUniqueItems()) {
				HashSet<Value.EqualityComparer> hash = new HashSet<Value.EqualityComparer>();
				for (Value item: getItems()) {
					hash.add(new Value.EqualityComparer(item));
				}
				if (hash.size() != getItems().length)
					return new ValidationInfo(Validity.Invalid);
			}
			return new ValidationInfo(schema.getId());
		}
		return new ValidationInfo(Validity.Invalid);
	}

	protected int GetHashCode() {
		int n = 979750913;
		for (Value v: getItems()) {
			n ^= v.GetHashCode();
		}
		return n;
	}

	protected boolean Equals(java.lang.Object obj) {
		Array a = obj instanceof Array ? (Array)obj : null;
		if (a == null)
			return false;
		if (getItems().length != a.getItems().length)
			return false;
		for (int i = 0; i != getItems().length; ++i)
			if (!getItems()[i].Equals(a.getItems()[i]))
				return false;
		return true;
	}
}
