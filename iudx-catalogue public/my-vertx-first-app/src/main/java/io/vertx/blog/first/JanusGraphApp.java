
package io.vertx.blog.first;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.shaded.jackson.databind.ObjectMapper;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.Multiplicity;
import org.janusgraph.core.RelationType;
import org.janusgraph.core.attribute.Geoshape;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.noggit.JSONParser;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.json.*; //http://theoryapp.com/parse-json-in-java/



    public class JanusGraphApp extends GraphApp {
    	
//    private Map<Integer, GraphApp> products = new LinkedHashMap<>();	
    
    protected static final String APP_NAME = "jgex";
    protected static final String MIXED_INDEX_CONFIG_NAME = "jgex";

    // Storage backends

    protected static final String CASSANDRA = "cassandra";
    protected static final String CQL = "cql";
    protected static final String ELASTICSEARCH = "elasticsearch";
    
    //declaring variables
    protected boolean useMixedIndex;
    protected String mixedIndexConfigName;
    protected Geoshape.GeoshapeSerializer n;
    protected Map json;
    protected JSONObject jsonObj;
    
    
    /**
     * Constructs a graphmp app using the given properties.
     * @param fileName location of the properties file
     */
    public void JanusGraphApp() {}

    public JanusGraphApp(final String fileName) {
        super(fileName);
        this.supportsSchema = true;
        this.supportsTransactions = true;
        this.supportsGeoshape = true;
        this.useMixedIndex = true;
        this.mixedIndexConfigName = MIXED_INDEX_CONFIG_NAME;
        this.n = new Geoshape.GeoshapeSerializer();
    }

    //graph traversal
    @Override
    public GraphTraversalSource openGraph() throws ConfigurationException {
        super.openGraph();
        useMixedIndex = useMixedIndex && conf.containsKey("index." + mixedIndexConfigName + ".backend");
        return g;
    }
 
    //drop graph
    @Override
    public void dropGraph() throws Exception {
        if (graph != null) {
            JanusGraphFactory.drop(getJanusGraph());
        }
    }
    
    /**
     * Returns the JanusGraph instance.
     */
    protected JanusGraph getJanusGraph() {
        return (JanusGraph) graph;
    }
    
    
    //create schema and define vertex, edge and properties classes
    @Override
    public void createSchema() {
        final JanusGraphManagement management = getJanusGraph().openManagement();
        try {
            // naive check if the schema was previously created
            if (management.getRelationTypes(RelationType.class).iterator().hasNext()) {
                management.rollback();
                return;
            }
            System.out.println((char)27 + "[33mcreating schema1");
            System.err.println("creating schema");
            createProperties(management);
            System.out.println((char)27 + "[33mcreating schema2");
            System.out.println("creating v labeeeels0");
            
            System.err.println("creating v labels");
            createVertexLabels(management);
            System.out.println((char)27 + "[33mcreating schema3");
            System.err.println("creating e labels");
            createEdgeLabels(management);
            System.out.println((char)27 + "[33mcreating schema4");
//            System.err.println("creating c___I");
//            createCompositeIndexes(management);
            System.err.println("creating m___I");
            System.out.println((char)27 + "[33mcreating schema5");
            createMixedIndexes(management);
            System.out.println((char)27 + "[33mcreating schema6");
            System.err.println("commit");
            management.commit();
        } catch (Exception e) {
            management.rollback();
        }
    }
    
    /**
     * Creates the vertex labels.
     */
    protected void createVertexLabels(final JanusGraphManagement management) {
    	System.err.println("VL001");
        management.makeVertexLabel("___ITEM").make();
        System.err.println("VL002");
        management.makeVertexLabel("___PROVIDER").make();
        System.err.println("VL003");
        management.makeVertexLabel("___RESOURCE_CLASS").make();
        System.err.println("VL004");
        management.makeVertexLabel("___DATA_MODEL").make();
        management.makeVertexLabel("___TAG").make();
       System.err.println("VL005");
    }
    
    /**
     * Creates the edge labels.
     */
    protected void createEdgeLabels(final JanusGraphManagement management) {
    	System.err.println("EL001");
        management.makeEdgeLabel("___PROVIDES").multiplicity(Multiplicity.ONE2MANY).make();
        System.err.println("EL002");
        management.makeEdgeLabel("___INSTANCE_OF").multiplicity(Multiplicity.ONE2MANY).make();
        System.err.println("EL003");
        management.makeEdgeLabel("___HAS_CLASS").multiplicity(Multiplicity.ONE2MANY).make();
        management.makeEdgeLabel("___ASSOCIATED_WITH").multiplicity(Multiplicity.MULTI).make();
        System.err.println("EL004");
    }

    /**
     * Creates the properties for vertices, edges, and meta-properties.
     */
    protected void createProperties(final JanusGraphManagement management) {
    	System.err.println("CP001");
        
        management.makePropertyKey("___NAME").dataType(String.class).make();        
        management.makePropertyKey("___TAG_NAME").dataType(String.class).make();
        System.err.println("CP002");
        management.makePropertyKey("___ID").dataType(UUID.class).make();
        management.makePropertyKey("___ITEM_STATUS").dataType(String.class).make();
        management.makePropertyKey("___STATION_ID").dataType(String.class).make();
        System.err.println("CP003");
        management.makePropertyKey("___REF_BASE_SCHEMA_RELEASE").dataType(String.class).make();
        System.err.println("CP004");
        management.makePropertyKey("___ITEM_TYPE").dataType(String.class).make();
        management.makePropertyKey("___ITEM_DESCRIPTION").dataType(String.class).make();
        System.err.println("CP005");
        management.makePropertyKey("___GEO_JSON_LOCATION").dataType(Geoshape.class).make();
        System.err.println("CP006");
        System.err.println("CP007");
        management.makePropertyKey("___CREATED_AT").dataType(String.class).make();
        System.err.println("CP008");
        management.makePropertyKey("___UPDATED_AT").dataType(String.class).make();
        System.err.println("CP009");
        management.makePropertyKey("___VERSION").dataType(Integer.class).make();
        System.err.println("CP0010");
        management.makePropertyKey("___ON_BOARDED_BY").dataType(String.class).make();
        
        System.err.println("CP0011");
        
        management.makePropertyKey("___REF_BASE_SCHEMA").dataType(String.class).make();
        System.err.println("CP007");
        management.makePropertyKey("___LATEST_RESOURCE_DATA").dataType(String.class).make();
        System.err.println("CP008");
        management.makePropertyKey("___REF_DATA_MODEL").dataType(String.class).make();
        System.err.println("CP009");
        management.makePropertyKey("___ENTITY_ID").dataType(String.class).make();
        System.err.println("CP0010");
        management.makePropertyKey("___ENTITY_IDENTITY_TYPE").dataType(String.class).make();
        System.err.println("CP0011");
        management.makePropertyKey("___ENTITY_IDENTITY_PROVIDER_URL").dataType(String.class).make();
        System.err.println("CP0012");
        management.makePropertyKey("___ENTITY_URL").dataType(String.class).make();
        System.err.println("CP0013");
        management.makePropertyKey("___RESOURCE_CLASS").dataType(String.class).make();
        System.err.println("CP0014");
        management.makePropertyKey("___RESOURCE_SERVER_ID").dataType(String.class).make();
        System.err.println("CP0015");
        management.makePropertyKey("___RESOURCE_ID").dataType(String.class).make();
        System.err.println("CP0016");
        management.makePropertyKey("___ACCESS_SCHEMA").dataType(String.class).make();
        System.err.println("CP0017");
        management.makePropertyKey("___ACCESS_SCHEMA_TYPE").dataType(String.class).make();
        System.err.println("CP0018");
    }
    
//      Creates the composite indexes. 
//    protected void createCompositeIndexes(final JanusGraphManagement management) {
//    	System.err.println("Creating ci");
//    }
    

//   Creates the mixed indexes. 
        protected void createMixedIndexes(final JanusGraphManagement management) {
    	System.err.println("Creating mi");
        if (useMixedIndex) {
            management.buildIndex("station_id", Vertex.class).addKey(management.getPropertyKey("___STATION_ID"))
                    .buildMixedIndex(mixedIndexConfigName);
            System.err.println("CMI001");
            management.buildIndex("resource_class", Vertex.class).addKey(management.getPropertyKey("___RESOURCE_CLASS"))
                    .buildMixedIndex(mixedIndexConfigName);
            System.err.println("CMI002");
        }
        System.err.println("CMI003");
    }
    

    //create the vertexes and edges
    public String createElements(String item_json) {
//        super.createElements(routingContext);
    	String res = "";
    	this.jsonObj = new JSONObject(item_json);
        
        System.err.println("\n"+this.jsonObj+"\n");
        if (useMixedIndex) {
           
        }
            try {
            // naive check if the graph was previously created
//            if (g.V().has("___NAME", "Hello").hasNext()) {
//                if (supportsTransactions) {
//                    g.tx().rollback();
//                }
//                return;
//            }

//            this.json=new ObjectMapper().readValue("{\n" +
//                    "  \"type\": \"Feature\",\n" +
//                    "  \"geometry\": {\n" +
//                    "    \"type\": \"Circle\",\n" +
//                    "    \"radius\": 30.5, " +
//                    "    \"coordinates\": [20.5, 10.5]\n" +
//                    "  },\n" +
//                    "  \"properties\": {\n" +
//                    "    \"name\": \"Dinagat Islands\"\n" +
//                    "  }\n" +
//                    "}", HashMap.class);
            
//            Vertex item = g.addV("___ITEM")
//            		.property("___NAME", "Vishrantwadi Nala")
//					.property("___LATEST_RESOURCE_DATA", "https://pune.iudx.org.in/api/1.0.0/resource/latest/flood-sensor/Vishrantwadi%20Nala")
//					.property("___ITEM_STATUS", "active")
//					.property("___STATION_ID", "FWR015")
//					.property("___ID", "e75c6460-89ef-4fcf-a937-c99766ffa38a")
//					.property("___ITEM_TYPE", "resource-item")
//					.property("___ITEM_DESCRIPTION", "Flood sensing station")
//					.property("___GEO_JSON_LOCATION", this.n.convert(this.json))
//					.property("___RESOURCE_ID", "Vishrantwadi Nala")
//					.property("___RESOURCE_SERVER_ID", "https://pune.iudx.org.in:443")
//					.property("___CREATED_AT", "Wed Mar 27 13:39:57 UTC 2019")
//					.property("___UPDATED_AT", "Wed Mar 27 13:39:57 UTC 2019")
//					.property("___VERSION", 1)
//					.property("___ACCESS_SCHEMA", "https://raw.githubusercontent.com/rbccps-iisc/iudx-schemas/master/data_models/environment/floodSensor/examples/exApiObject_env_flood_climoPune_0.json")
//					.property("___ON_BOARDED_BY", "iudx-provider").next();
//                        
//            Vertex provider = g.addV("___PROVIDER")
//            		.property("___ENTITY_ID", "smart-cities-rbccps")
//            		.property("___ENTITY_IDENTITY_TYPE", "X509")
//            		.property("___ENTITY_IDENTITY_PROVIDER_URL", "https://ca.iudx.org.in")
//            		.property("___ENTITY_URL", "https://example.iudx-provider.org").next();
//                      
//            Vertex dataModel=g.addV("___DATA_MODEL")
//            		.property("___REF_BASE_SCHEMA","https://raw.githubusercontent.com/rbccps-iisc/iudx-schemas/master/base_schemas/v0.0.0/iudx_data_item_schema.json")
//            		.property("___REF_BASE_SCHEMA_RELEASE","v0.0.0")
//            		.property("___REF_DATA_MODEL","https://raw.githubusercontent.com/rbccps-iisc/iudx-schemas/master/data_models/environment/floodSensor/examples/exdataModel_env_flood_climoPune_0.json#/dataModelSchema").next();
//                                   
//            Vertex resourceClass=g.addV("___RESOURCE_CLASS")
//            		.property("___RESOURCE_CLASS","flood-sensor")
//            		.property("___ACCESS_SCHEMA_TYPE","openAPI").next();
//            Vertex tag=g.addV("___TAG")
//            		.property("___TAG_NAME","flood").next();
//            
//                     tag=g.addV("___TAG")
//            		.property("___TAG_NAME","flooding").next();
//
//                     tag=g.addV("___TAG")
//            		.property("___TAG_NAME","flood alert").next();
//
//                     tag=g.addV("___TAG")
//            		.property("___TAG_NAME","flood danger").next();
//            
//            g.V(provider).as("a").V(item).addE("___PROVIDES").from("a").next();
//            g.V(item).as("a").V(dataModel).addE("___INSTANCE_OF").from("a").next();
//            g.V(item).as("a").V(resourceClass).addE("___HAS_CLASS").from("a").next();
//            g.V(item).as("a").V(tag).addE("___ASSOCIATED_WITH").from("a").next();
//            g.V(item).as("a").V(tag).addE("___ASSOCIATED_WITH").from("a").next();
//            g.V(item).as("a").V(tag).addE("___ASSOCIATED_WITH").from("a").next();
//            g.V(item).as("a").V(tag).addE("___ASSOCIATED_WITH").from("a").next();
//                        
//                     item = g.addV("___ITEM")
//            		.property("___NAME", "Hadapsar Mundhwa")
//					.property("___LATEST_RESOURCE_DATA", "https://pune.iudx.org.in/api/1.0.0/resource/latest/flood-sensor/Hadapsar%20Mundhwa")
//					.property("___ITEM_STATUS", "active")
//					.property("___STATION_ID", "FWR015")
//					.property("___ID", "b2df804d-3341-4ee9-abdb-d28a677282a8")
//					.property("___ITEM_TYPE", "resource-item")
//					.property("___GEO_JSON_LOCATION", this.n.convert(this.json))
//					.property("___ITEM_DESCRIPTION", "Flood sensing station")
//					.property("___RESOURCE_ID", "Hadapsar Mundhwa")
//					.property("___RESOURCE_SERVER_ID", "https://pune.iudx.org.in:443")
//					.property("___CREATED_AT", "Wed Mar 27 13:39:57 UTC 2019")
//					.property("___UPDATED_AT", "Wed Mar 27 13:39:57 UTC 2019")
//					.property("___VERSION", 1)
//					.property("___ACCESS_SCHEMA", "https://raw.githubusercontent.com/rbccps-iisc/iudx-schemas/master/data_models/environment/floodSensor/examples/exApiObject_env_flood_climoPune_0.json")
//					.property("___ON_BOARDED_BY", "iudx-provider").next();
//                       
//                     provider = g.addV("___PROVIDER")
//            		.property("___ENTITY_ID", "smart-cities-rbccps")
//            		.property("___ENTITY_IDENTITY_TYPE", "X509")
//            		.property("___ENTITY_IDENTITY_PROVIDER_URL", "https://ca.iudx.org.in")
//            		.property("___ENTITY_URL", "https://example.iudx-provider.org").next();
//               
//                    dataModel=g.addV("___DATA_MODEL")
//            		.property("___REF_BASE_SCHEMA","https://raw.githubusercontent.com/rbccps-iisc/iudx-schemas/master/base_schemas/v0.0.0/iudx_data_item_schema.json")
//            		.property("___REF_BASE_SCHEMA_RELEASE","v0.0.0")
//            		.property("___REF_DATA_MODEL","https://raw.githubusercontent.com/rbccps-iisc/iudx-schemas/master/data_models/environment/floodSensor/examples/exdataModel_env_flood_climoPune_0.json#/dataModelSchema").next();       
//            
//                    resourceClass=g.addV("___RESOURCE_CLASS")
//            		.property("___RESOURCE_CLASS","flood-sensor")
//            		.property("___ACCESS_SCHEMA_TYPE","openAPI").next();
//                    
//                    tag=g.addV("___TAG")
//                    	.property("___TAG_NAME","flood").next();
//                    
//                    tag=g.addV("___TAG")
//                    	.property("___TAG_NAME","flooding").next();
//
//                    tag=g.addV("___TAG")
//                   		.property("___TAG_NAME","flood alert").next();
//
//                    tag=g.addV("___TAG")
//                   		.property("___TAG_NAME","flood danger").next();
//          
//            g.V(provider).as("a").V(item).addE("___PROVIDES").from("a").next();
//            g.V(item).as("a").V(dataModel).addE("___INSTANCE_OF").from("a").next();
//            g.V(item).as("a").V(resourceClass).addE("___HAS_CLASS").from("a").next();
//            g.V(item).as("a").V(tag).addE("___ASSOCIATED_WITH").from("a").next();
//            g.V(item).as("a").V(tag).addE("___ASSOCIATED_WITH").from("a").next();
//            g.V(item).as("a").V(tag).addE("___ASSOCIATED_WITH").from("a").next();
//            g.V(item).as("a").V(tag).addE("___ASSOCIATED_WITH").from("a").next();
            
              // Read the request's content and create an instance of GraphApp.
                
                
                


                // Add it to the backend map
//                products.put(, whisky);
                
                System.err.println("api1");
                System.err.println(this.jsonObj);
                System.err.println("N:"+this.jsonObj.getString("NAME"));
//                       Vertex item = g.addV("___ITEM")
//                		.property("	___NAME", "FFF")
//    					.property("___LATEST_RESOURCE_DATA", whisky.getlatestResourceData())
//    					.property("___ITEM_STATUS", whisky.getitemStatus())
//    					.property("___STATION_ID", whisky.getstationId())
//    					.property("___ID", whisky.getid())
//    					.property("___ITEM_TYPE",whisky.getitemType())
//    					.property("___GEO_JSON_LOCATION", whisky.getgeoJsonLocation())
//    					.property("___ITEM_DESCRIPTION",whisky.getitemDescription())
//    					.property("___RESOURCE_ID", whisky.getresourceId())
//    					.property("___RESOURCE_SERVER_ID", whisky.getresourceServerId())
//    					.property("___CREATED_AT", whisky.getcreatedAt())
//    					.property("___UPDATED_AT", whisky.getupdatedAt())
//    					.property("___VERSION", whisky.getversion())
//    					.property("___ACCESS_SCHEMA",whisky.getaccessSchema())
//    					.property("___ON_BOARDED_BY", "LILY").next();
//                       System.err.println("api2");
             
//                        Vertex provider = g.addV("___PROVIDER")
//                         .property("___ENTITY_ID", whisky.getentityId())
//                         .property("___ENTITY_IDENTITY_TYPE", whisky.getentityIdentityType())
//                         .property("___ENTITY_IDENTITY_PROVIDER_URL", whisky.getentityIdentityProviderUrl())
//                         .property("___ENTITY_URL",whisky.getentityUrl()).next();
//            
//                        Vertex dataModel=g.addV("___DATA_MODEL")
//                         .property("___REF_BASE_SCHEMA",whisky.getrefBaseSchema())
//                         .property("___REF_BASE_SCHEMA_RELEASE",whisky.getrefBaseSchemaRelease())
//                         .property("___REF_DATA_MODEL",whisky.getrefDataModel()).next();       
//                         
//                        Vertex resourceClass=g.addV("___RESOURCE_CLASS")
//                         .property("___RESOURCE_CLASS",whisky.getresourceClass())
//                         .property("___ACCESS_SCHEMA_TYPE",whisky.getaccessSchemaType()).next();
//                                 
//                        Vertex tag=g.addV("___TAG")
//                        		 .property("___TAG_NAME",whisky.gettagName()).next();
//                                 
//                         tag=g.addV("___TAG")
//                        		 .property("___TAG_NAME",whisky.gettagName()).next();
//                         tag=g.addV("___TAG")
//                        		 .property("___TAG_NAME",whisky.gettagName()).next();
//
//                         tag=g.addV("___TAG")
//                        		 .property("___TAG_NAME",whisky.gettagName()).next();
//                       
//                         g.V(provider).as("a").V(item).addE("___PROVIDES").from("a").next();
//                         g.V(item).as("a").V(dataModel).addE("___INSTANCE_OF").from("a").next();
//                         g.V(item).as("a").V(resourceClass).addE("___HAS_CLASS").from("a").next();
//                         g.V(item).as("a").V(tag).addE("___ASSOCIATED_WITH").from("a").next();
//                         g.V(item).as("a").V(tag).addE("___ASSOCIATED_WITH").from("a").next();
//                         g.V(item).as("a").V(tag).addE("___ASSOCIATED_WITH").from("a").next();
//                         g.V(item).as("a").V(tag).addE("___ASSOCIATED_WITH").from("a").next();
//                         
//                      
       
            if (this.supportsTransactions) {
                g.tx().commit();
             // Return the created whisky as JSON
                res = "201";
            }

        } catch (Exception e) {
        	System.err.println(e);
            if (this.supportsTransactions) {
                g.tx().rollback();
                res = "000";
             // Return the created whisky as JSON
            }
        }
           
        return res;
    }

    
    
//    // queries
//    public void readElement(RoutingContext routingContext) {
//        try {
//            if (g == null) {
//                return;
//            }
////            
////            final String id = routingContext.request().getParam("id");
////            if (id == null) {
////              routingContext.response().setStatusCode(400).end();
////            } else {
////              final Integer idAsInteger = Integer.valueOf(id);
////              GraphApp whisky = products.get(idAsInteger);
////              if (whisky == null) {
////                routingContext.response().setStatusCode(404).end();
////              } else {
////                routingContext.response()
////                    .putHeader("content-type", "application/json; charset=utf-8")
////                    .end(Json.encodePrettily(whisky));
////              }
//            final List<Object> geojson = g.V().has("___STATION_ID","FWR015").values("___GEO_JSON_LOCATION").toList();
//            System.err.println("answer: " + geojson.toString());
//            
////            final List<Object> lat_lng = g.V().has("___STATION_ID","FWR015").values("___COORDINATES___").toList();
////            System.err.println("answer: " + lat_lng.toString());
//
//            final Optional<Map<Object, Object>> edge = g.V().has("___ENTITY_ID", "smart-cities-rbccps").outE("___PROVIDES").as("e").inV()
//                    .has("___NAME", "Vishrantwadi Nala").select("e").valueMap(true).tryNext();
//            if (edge.isPresent()) {
//                System.err.println(edge.get().toString());
//            } else {
//            	System.err.println("EDGE not found");
//            }
//   
//                     
//        } finally {
//            // the default behavior automatically starts a transaction for
//            // any graph interaction, so it is best to finish the transaction
//            // even for read-only graph query operations
//            if (supportsTransactions) {
//                g.tx().rollback();
//            }
//        }
//    }

    
    
    
    //run the program ie call all the functions
    public void runApp() {
        try {
            // open and initialize the graph
        	System.err.println("RUN APP");
            this.openGraph();
            System.err.println("RUN APP2");
            // define the schema before loading data
            if (this.supportsSchema) {
            	System.err.println("CREATING SCHEMA");
                this.createSchema();
            }
           
//            // read to see they were made
//            readElements();
           // close the graph
//            closeGraph();
            } catch (Exception e) {
        }
    }

   

    }