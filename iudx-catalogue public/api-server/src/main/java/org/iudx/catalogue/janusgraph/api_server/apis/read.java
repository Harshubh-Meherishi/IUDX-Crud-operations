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
import org.codehaus.jettison.json.JSONException;
import org.iudx.catalogue.janusgraph.api_server.JG;
import org.iudx.catalogue.janusgraph.api_server.JanusGraphApp;
import org.json.JSONObject;

@WebServlet(
        name = "readServlet",
        urlPatterns = {"/read"}
    )


/**
 * Hello world!
 *
 */
public class read extends HttpServlet {
	
	static JanusGraphApp obj;
	
	static {
		obj = JG.getJg();
	}

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ServletOutputStream out = resp.getOutputStream();
        ServletInputStream in=req.getInputStream();
        String jsonString = IOUtils.toString(req.getInputStream());
//        JSONObject json = new JSONObject(jsonString);
//        System.out.println("Reached here");
        String code="";
        String response = null;
//        System.out.println("Reached here 2");
//		try {
//			System.out.println("Before try");
			response = obj.readElements(jsonString);
//			System.out.println("After try");
//		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        if(response == "Successful")
        code = "200 OK";
        else if (response == "Failed")
        code = "401";
           
      
        out.write(code.getBytes());
        out.flush();
        out.close();
    }

}
