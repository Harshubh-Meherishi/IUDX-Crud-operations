package io.vertx.blog.first;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.ivy.Main;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

/**
 * This is a verticle. A verticle is a _Vert.x component_. This verticle is implemented in Java, but you can
 * implement them in JavaScript, Groovy or even Ruby.
 */
public class MyFirstVerticle extends AbstractVerticle {
	JanusGraphApp obj;
//	File resourcesDirectory = new File("conf/jgex-cql.properties");
  /**
   * This method is called when the verticle is deployed. It creates a HTTP server and registers a simple request
   * handler.
   * <p/>
   * Notice the `listen` method. It passes a lambda checking the port binding result. When the HTTP server has been
   * bound on the port, it call the `complete` method to inform that the starting has completed. Else it reports the
   * error.
   *
   * @param fut the future
 * @throws IOException 
 * @throws ConfigurationException 
   */	 
	
  @Override
  public void start(Future<Void> fut) throws ConfigurationException, IOException {

//    createSomeData();
	  File resourcesDirectory = new File("src/main/resources/conf/jgex-cql.properties");
	    String fileName = resourcesDirectory.getAbsolutePath();
	    
		obj = new JanusGraphApp(fileName);

    // Create a router object.
    Router router = Router.router(vertx);

    // Bind "/" to our hello message.
    router.route("/").handler(routingContext -> {
      HttpServerResponse response = routingContext.response();
      
      response
          .putHeader("content-type", "text/html")
          .end("<h1>Hello from my first Vert.x 3 application</h1>");
    });

    router.route("/assets/*").handler(StaticHandler.create("assets"));

//    router.get("/read").handler(this::readElements);
    router.route("/*").handler(BodyHandler.create());
    router.post("/create").handler(this::createElements);
//    router.get("/read/:id").handler(this::readElement);
//    router.put("/update/:id").handler(this::updateElement);
//    router.delete("/delete/:id").handler(this::deletElemnt);
    	

    // Create the HTTP server and pass the "accept" method to the request handler.
    
    vertx
        .createHttpServer()
        .requestHandler(request -> {

            // Let's say we have to call a blocking API (e.g. JDBC) to execute a query for each
            // request. We can't do this directly or it will block the event loop
            // But you can do this using executeBlocking:
        	System.out.println("11111");
            vertx.<String>executeBlocking(future -> {

              // Do the blocking operation in here

              // Imagine this was a call to a blocking API to get the result
            	System.out.println("11111");
              try {
                obj.runApp();
              } catch (Exception ignore) {
              }
              String result = "armadillos!";

              future.complete(result);

            }, res -> {

              if (res.succeeded()) {

                request.response().putHeader("content-type", "text/plain").end(res.result());

              } else {
                res.cause().printStackTrace();
              }
            });

          })
        .listen(
            // Retrieve the port from the configuration,
            // default to 8080.
            config().getInteger("http.port", 8080),
            result -> {
              if (result.succeeded()) {
                fut.complete();
              } else {
                fut.fail(result.cause());
              }
            }
        );
   
  }
  
  

  public void createElements(RoutingContext routingContext) {
	    System.err.println(routingContext.toString());
		String res = this.obj.createElements(routingContext.getBodyAsString());
		if(res=="201") {
			routingContext.response().setStatusCode(201)
            .putHeader("content-type", "application/json; charset=utf-8")
            .end("{'status: 'ok''}");
		}else if(res=="000"){
			routingContext.response().setStatusCode(000)
            .putHeader("content-type", "application/json; charset=utf-8")
            .end("{'status: 'rollback''}");
		}
  }
  
  
//  private void readAll(RoutingContext routingContext) {
//	    // Write the HTTP response
//	    // The response is in JSON using the utf-8 encoding
//	    // We returns the list of bottles
//	    routingContext.response()
//	        .putHeader("content-type", "application/json; charset=utf-8")
//	        .end(Json.encodePrettily(products.values()));
//	  }


//  private void updateOne(RoutingContext routingContext) {
//    final String id = routingContext.request().getParam("id");
//    JsonObject json = routingContext.getBodyAsJson();
//    if (id == null || json == null) {
//      routingContext.response().setStatusCode(400).end();
//    } else {
//      final Integer idAsInteger = Integer.valueOf(id);
//      GraphApp whisky = products.get(idAsInteger);
//      if (whisky == null) {
//        routingContext.response().setStatusCode(404).end();
//      } else {
//        whisky.setName(json.getString("name"));
//        whisky.setOrigin(json.getString("origin"));
//        routingContext.response()
//            .putHeader("content-type", "application/json; charset=utf-8")
//            .end(Json.encodePrettily(whisky));
//      }
//    }
//  }
//
//  private void deleteOne(RoutingContext routingContext) {
//    String id = routingContext.request().getParam("id");
//    if (id == null) {
//      routingContext.response().setStatusCode(400).end();
//    } else {
//      Integer idAsInteger = Integer.valueOf(id);
//      products.remove(idAsInteger);
//    }
//    routingContext.response().setStatusCode(204).end();
//  }

  
//
//  private void createSomeData() {
//    GraphApp bowmore = new GraphApp("Bowmore 15 Years Laimrig", "Scotland, Islay");
//    products.put(bowmore.getId(), bowmore);
//    GraphApp talisker = new GraphApp("Talisker 57° North", "Scotland, Island");
//    products.put(talisker.getId(), talisker);
//  }



}