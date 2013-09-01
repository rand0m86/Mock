package com.example.mock;

import java.util.HashMap;
import java.util.Map;

public final class HTMLsymbols {
	private static Map<String, String> symbols;
	static {
		symbols = new HashMap<String, String>();
		symbols.put("&mdash;", " —");
		symbols.put("&hellip;", "...");
		symbols.put("&nbsp;", "");
	}
	
	public static Map<String, String> getSymbols() {
		return symbols;
	}
}
