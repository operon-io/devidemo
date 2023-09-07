/**
 * MFNode.java
 *
 * This file was generated by MapForce 2022r2.
 *
 * YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
 * OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.
 *
 * Refer to the MapForce Documentation for further details.
 * http://www.altova.com/mapforce
 */

package com.altova.mapforce;

import java.util.ArrayList;

public class MFNode {
	static Object unboxTypedValue(Object o) throws Exception {
		if (o instanceof IMFNode)
			return ((IMFNode)o).typedValue();
		return o;
	}

	static void add(ArrayList<Object> a, Object o) {
		if (o instanceof Object[])
			for (Object i : (Object[])o)
				add(a, i);
		else
			a.add(o);
	}

	public static Object collectTypedValue(IEnumerable from) throws Exception {
		IEnumerator en = from.enumerator();
		if (!en.moveNext())
			return null;
		Object first = unboxTypedValue(en.current());
		if (!en.moveNext())
			return first;

		ArrayList<Object> a = new ArrayList<Object>();
		add(a, first);
		add(a, en.current());
		while (en.moveNext())
			add(a, en.current());
			
		return a.toArray();
	}
}
