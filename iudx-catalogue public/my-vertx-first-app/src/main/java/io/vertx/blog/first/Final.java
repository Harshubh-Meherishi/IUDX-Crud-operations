//package io.vertx.blog.first;
//
//import java.io.File;
//import java.util.concurrent.atomic.AtomicInteger;
//
//public class Final {
//
//
//
//	public static void main(String[] args) throws Exception {
//	  	System.err.println("1");
//	  	File resourcesDirectory = new File("src/main/resources/conf/jgex-cql.properties");
//	  	System.err.println("2");
//	      final String fileName = resourcesDirectory.getAbsolutePath();
//	      final boolean drop = (args != null && args.length > 1) ? "drop".equalsIgnoreCase(args[1]) : false;
//	      final MyFirstVerticle app = new MyFirstVerticle(fileName);
//	      if (drop) {
//	      	System.err.println("3");
//	          app.openGraph();
//	          System.err.println("4");
////	          app.dropGraph();
//	      } else {
//	          app.runApp();
//	      }
//	  }
//}











