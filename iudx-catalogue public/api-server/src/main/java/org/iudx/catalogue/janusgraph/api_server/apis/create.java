package org.iudx.catalogue.janusgraph.api_server.apis;
import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.IOUtils;
import org.iudx.catalogue.janusgraph.api_server.JanusGraphApp;
import org.iudx.catalogue.janusgraph.api_server.JG;
import org.json.JSONObject;

@WebServlet(
        name = "createServlet",
        urlPatterns = {"/create"}
    )


/**
 * Hello world!
 *
 */
public class create extends HttpServlet {
	
//	String janusGraphPropLocation = "src/main/conf/jgex-cql.properties";
//    
//    //Setup JanusGraph
//	File resourcesDirectory = new File(janusGraphPropLocation);
//    final String fileName = resourcesDirectory.getAbsolutePath();
//    JanusGraphApp app1 = new JanusGraphApp(fileName);
    
    
    static JanusGraphApp obj;
	
	static {
		obj = JG.getJg();
	}
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("REQ:");
        ServletOutputStream out = resp.getOutputStream();
//        JSONObject json = new JSONObject(jsonString);
        out.write("GOT req	".getBytes());
        out.flush();
        out.close();
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
//        System.out.println("REQ:" + IOUtils.toString(req.getInputStream()));
//        String jsonString = IOUtils.toString(req.getInputStream());
        ServletOutputStream out = resp.getOutputStream();
//        JSONObject json = new JSONObject(jsonString);
        System.out.println("OBJ:" + obj.toString());
        String response = obj.createElements(IOUtils.toString(req.getInputStream())	);
        out.write(response.getBytes());
        out.flush();
        out.close();
    }

}
