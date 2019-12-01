package org.iudx.catalogue.janusgraph.api_server;

public class JG {
	
	private static JanusGraphApp jg;

	public static JanusGraphApp getJg() {
		return jg;
	}

	public static void setJg(JanusGraphApp jg1) {
		jg = jg1;
	}
	
}
