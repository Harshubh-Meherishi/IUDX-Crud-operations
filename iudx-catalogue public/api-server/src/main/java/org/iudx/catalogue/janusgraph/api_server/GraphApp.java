package org.iudx.catalogue.janusgraph.api_server;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.util.GraphFactory;
import org.janusgraph.core.attribute.Geoshape;

import javassist.Loader;


public class GraphApp {
    // private static final Logger LOGGER = LoggerFactory.getLogger(GraphApp.class);

    protected String propFileName;
    protected Configuration conf;
    protected Graph graph;
    protected GraphTraversalSource g;
    protected boolean supportsTransactions;
    protected boolean supportsSchema;
    protected boolean supportsGeoshape;
   
    /**
     * Constructs a graph app using the given properties.
     * @param fileName location of the properties file
     */
    public GraphApp(final String fileName) {
        this.propFileName = fileName;
    }

    /**
     * Opens the graph instance. If the graph instance does not exist, a new
     * graph instance is initialized.
     * @throws ConfigurationException 
     * @throws IOException 
     */
    public GraphTraversalSource openGraph() throws ConfigurationException {
        //LOGGER.info("opening graph");
    	System.err.println("1");
        try {
//        	System.out.println("TRYYYYYYYYYYYYYYYYYYYYYYYY SUPER-OPENGRPAH");
			conf = new PropertiesConfiguration(this.propFileName);
			System.err.println(conf.getString("index.jgex.hostname"));
			System.err.println(conf.getString("index.jgex.index-name"));
			System.err.println(conf.getString("index.jgex.backend"));
			System.err.println(conf.getString("storage.backend"));
			System.err.println(conf.getString("storage.cql.keyspace"));
			System.err.println(conf.getString("storage.hostname"));
		} catch (ConfigurationException e1) {
			// TODO Auto-generated catch block
//			System.out.println("CATCHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHh SUPER-OPENGRAPH");
			e1.printStackTrace();
		}
        System.err.println("2");
        System.err.println(this.propFileName);
//        System.out.println("CONFFFFFFFFFFFFFFFFFFFFFFFff:" + conf.toString());
        try {
//        	System.out.println("TRY@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@22");
        	graph = GraphFactory.open(conf);
//        	System.out.println("TRY@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@23");
		} catch (Exception e) {
			// TODO: handle exception
//			System.out.println("CATCH@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@22");
			System.out.println("ERROR:" + e.toString());
//			System.out.println("CATCH@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@23");
		}
        System.err.println("3");
        g = graph.traversal();
        return g;
    }

    /**
     * Closes the graph instance.
     */
    public void closeGraph() throws Exception {
        //LOGGER.info("closing graph");
        try {
            if (g != null) {
                g.close();
            }
            if (graph != null) {
                graph.close();
            }
        } finally {
            g = null;
            graph = null;
        }
    }

    /**
     * Drops the graph instance. The default implementation does nothing.
     */
    public void dropGraph() throws Exception {
    }

    /**
     * Creates the graph schema. The default implementation does nothing.
     */
    public void createSchema() {
    }

    /**
     * Adds the vertices, edges, and properties to the graph.
     */
    public String createElements(String item) {
		return "";
    }

    /**
     * Runs some traversal queries to get data from the graph.
     */
    public void readElements() {
        
    }

    /**
     * Makes an update to the existing graph structure. Does not create any
     * new vertices or edges.
     */
    public void updateElements() {
        
    }

    /**
     * Deletes elements from the graph structure. When a vertex is deleted, its incident edges are also deleted.
     */
    public void deleteElements() {
        
    }

    /**
     * Run the entire application:
     * 1. Open and initialize the graph
     * 2. Define the schema
     * 3. Build the graph
     * 4. Run traversal queries to get data from the graph
     * 5. Make updates to the graph
     * 6. Close the graph
     */
    public void runApp() {
        
    }
    

}
