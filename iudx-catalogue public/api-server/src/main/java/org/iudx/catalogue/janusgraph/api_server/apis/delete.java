package org.iudx.catalogue.janusgraph.api_server.apis;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
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
        name = "deleteServlet",
        urlPatterns = {"/delete"}
    )


/**
 * Hello world!
 *
 */
public class delete extends HttpServlet {
	
	static JanusGraphApp obj;
	
	static {
		obj = JG.getJg();
	}

    @Override
    protected void doDelete(HttpServletRequest req,
            HttpServletResponse resp)
     throws ServletException,
            java.io.IOException {
    	 ServletOutputStream out = resp.getOutputStream();
//         ServletInputStream in=req.getInputStream();
         String jsonString = IOUtils.toString(req.getInputStream());
//         JSONObject json = new JSONObject(jsonString);
         String code="";
         String response = null;
		response = obj.deleteVertex(jsonString);
         if(response == "Successful")
         code = "200 OK";
         else if (response == "Failed")
         code = "401";
         
        out.write(code.getBytes());
        out.flush();
        out.close();
    }
}
