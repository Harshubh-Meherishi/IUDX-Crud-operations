package org.iudx.catalogue.janusgraph.api_server;

import java.io.File;

import javax.servlet.http.HttpServlet;

import org.apache.catalina.startup.Tomcat;

public class Main {

	public static void main(String[] args) throws Exception {

        String webappDirLocation = "src/main/webapp/";
        String janusGraphPropLocation = "src/main/conf/jgex-cql.properties";
        //Setup JanusGraph
    	File resourcesDirectory = new File(janusGraphPropLocation);
        final String fileName = resourcesDirectory.getAbsolutePath();
        final boolean drop = (args != null && args.length > 1) ? "drop".equalsIgnoreCase(args[1]) : false;
        final JanusGraphApp app = new JanusGraphApp(fileName);
//        if (drop) {
//            app.openGraph();
////            app.dropGraph();
//        } else {
//            app.runApp();
//        }
        
        
        
        app.runApp();
        
//        JG.setJg(app);
        
        Tomcat tomcat = new Tomcat();

        //The port that we should run on can be set into an environment variable
        //Look for that variable and default to 8080 if it isn't there.
        String webPort = System.getenv("PORT");
        if(webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }

        tomcat.setPort(Integer.valueOf(webPort));

        tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
        System.out.println("configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());
                
        tomcat.start();
        tomcat.getServer().await();
    }
}