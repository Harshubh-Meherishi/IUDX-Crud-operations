package org.iudx.catalogue.janusgraph.api_server.apis;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.iudx.catalogue.janusgraph.api_server.JG;
import org.iudx.catalogue.janusgraph.api_server.JanusGraphApp;
import org.json.JSONObject;

@WebServlet(
        name = "updateServlet",
        urlPatterns = {"/update"}
    )


/**
 * Hello world!
 *
 */
public class update extends HttpServlet {
	
	static JanusGraphApp obj;
	
	static {
		obj = JG.getJg();
	}

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	 ServletOutputStream out = resp.getOutputStream();
         String jsonString = IOUtils.toString(req.getInputStream());
         JSONObject json = new JSONObject(jsonString);
         String response = obj.updateElements(jsonString);
         out.write(response.getBytes());
         out.flush();
         out.close();
     }

}
