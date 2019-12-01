
package org.iudx.catalogue.janusgraph.api_server;


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
import org.codehaus.jettison.json.JSONException;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.Multiplicity;
import org.janusgraph.core.RelationType;
import org.janusgraph.core.attribute.Geoshape;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.noggit.JSONParser;

import com.tinkerpop.blueprints.util.io.graphson.GraphSONMode;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONUtility;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.TransactionalGraph;
import org.json.*; //http://theoryapp.com/parse-json-in-java/
import org.locationtech.jts.geom.Geometry;



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
//    	System.out.println("1111111111111111111111111111111111111111111111111");
        super.openGraph();
//        System.out.println("2222222222222222222222222222222222222222222222222222");
        useMixedIndex = useMixedIndex && conf.containsKey("index." + mixedIndexConfigName + ".backend");
//        System.out.println("3333333333333333333333333333333333333333333333333333333");
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
    	System.out.println("CE:" + item_json);
    	System.out.println("g" + g.toString());
    	System.out.println("g" + super.g.toString());
    	this.jsonObj = new JSONObject(item_json);
        
        System.err.println("\n"+this.jsonObj+"\n");
        if (useMixedIndex) {
           
        }
//        System.out.println("BEFORE IFF@@@@@@@@@@@@@@@@@@@@");
     	if(
    			g.V().has("___PROVIDER", this.jsonObj.getJSONObject("provider").getString("entityId"),
    			 this.jsonObj.getJSONObject("provider")).hasNext()
    			    			   )
     	{
    				System.out.println("PROVIDER attribute already exist, aborting");
    	    		g.tx().rollback();
    	    		
     	}
//     	System.out.println("AFTER IFFFFFFFFFFFFFFFFFFFF1111111111111111111111111");
     	
     	if(
    			g.V().has("___DATA_MODEL",this.jsonObj.getString("refBaseSchema")
    			,this.jsonObj.getString("refBaseSchemaRelease")).hasNext() 
    			
    			    			   )
     	{
    				System.out.println("DATA MODEL attribute already exist, aborting");
    	    		g.tx().rollback();
//    	    		
     	}
//     	System.out.println("AFTER IFFFFFFFFFFFFFFFFFFFF22222222222222222222222");
    	if(
    			g.V().has("___RESOURCE_CLASS",this.jsonObj.getJSONArray("accessInformation").getJSONObject(0).getString("accessSchemaType"),this.jsonObj.getJSONArray("accessInformation").getJSONObject(0).getJSONObject("accessVariables").getString("resourceClass")).hasNext() 
    		
    			
    			    			   )
     	{
    				System.out.println("DATA MODEL attribute already exist, aborting");
    	    		g.tx().rollback();
//    	    		
     	}
//    	System.out.println("AFTER IFFFFFFFFFFFFFFFFFFFF33333333333333333333333333333333");
            try {
            // naive check if the graph was previously created
            if (this.g.V().has("___NAME", "Hello").hasNext()) {
                if (supportsTransactions) {
                    this.g.tx().rollback();
                }
                return "";
            }
            
            
//////////////////////////////////////////////////////////////////////
            /////////////DUMMY DATA////////////////////
/////////////////////////////////////////////////////////////////////
            
            
//            
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
//           
//            Vertex item = this.g.addV("___ITEM")
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
//            Vertex provider = this.g.addV("___PROVIDER")
//            		.property("___ENTITY_ID", "smart-cities-rbccps")
//            		.property("___ENTITY_IDENTITY_TYPE", "X509")
//            		.property("___ENTITY_IDENTITY_PROVIDER_URL", "https://ca.iudx.org.in")
//            		.property("___ENTITY_URL", "https://example.iudx-provider.org").next();
//                      
//            Vertex dataModel= this.g.addV("___DATA_MODEL")
//            		.property("___REF_BASE_SCHEMA","https://raw.githubusercontent.com/rbccps-iisc/iudx-schemas/master/base_schemas/v0.0.0/iudx_data_item_schema.json")
//            		.property("___REF_BASE_SCHEMA_RELEASE","v0.0.0")
//            		.property("___REF_DATA_MODEL","https://raw.githubusercontent.com/rbccps-iisc/iudx-schemas/master/data_models/environment/floodSensor/examples/exdataModel_env_flood_climoPune_0.json#/dataModelSchema").next();
//                                   
//            Vertex resourceClass=this.g.addV("___RESOURCE_CLASS")
//            		.property("___RESOURCE_CLASS","flood-sensor")
//            		.property("___ACCESS_SCHEMA_TYPE","openAPI").next();
//            Vertex tag=this.g.addV("___TAG")
//            		.property("___TAG_NAME","flood").next();
//            
//                     tag=this.g.addV("___TAG")
//            		.property("___TAG_NAME","flooding").next();
//
//                     tag=this.g.addV("___TAG")
//            		.property("___TAG_NAME","flood alert").next();
//
//                     tag=this.g.addV("___TAG")
//            		.property("___TAG_NAME","flood danger").next();
//            
//            this.g.V(provider).as("a").V(item).addE("___PROVIDES").from("a").next();
//            this.g.V(item).as("a").V(dataModel).addE("___INSTANCE_OF").from("a").next();
//            this.g.V(item).as("a").V(resourceClass).addE("___HAS_CLASS").from("a").next();
//            this.g.V(item).as("a").V(tag).addE("___ASSOCIATED_WITH").from("a").next();
//            this.g.V(item).as("a").V(tag).addE("___ASSOCIATED_WITH").from("a").next();
//            this.g.V(item).as("a").V(tag).addE("___ASSOCIATED_WITH").from("a").next();
//            this.g.V(item).as("a").V(tag).addE("___ASSOCIATED_WITH").from("a").next();
//              
//                System.err.println("api1");                
////                this.json=new ObjectMapper().readValue(this.jsonObj.getString("geoJsonLocation"), HashMap.class);
//                System.err.println(this.jsonObj.getJSONArray("accessInformation").getJSONObject(0).getJSONObject("accessVariables").getString("resourceClass"));
//                System.err.println(this.jsonObj.getJSONArray("accessInformation").getJSONObject(0).getJSONObject("accessVariables"));
//                System.err.println(this.jsonObj.getJSONArray("accessInformation").getJSONObject(0));
//                System.err.println(this.jsonObj.getJSONArray("accessInformation"));
//                System.err.println("1:"+this.jsonObj.getString("NAME"));
//                System.err.println("2:"+this.jsonObj.getJSONObject("geoJsonLocation"));
//                System.err.println("3:"+this.jsonObj.getInt("__version"));
//                System.err.println("4:"+this.jsonObj.getJSONObject("provider").getString("entityId"));
//                System.err.println("5:"+this.jsonObj.getJSONObject("provider").getString("entityURL"));
//                System.err.println("6:"+this.jsonObj.getJSONObject("provider").getString("entityIdentityType"));
//                System.err.println("7:"+this.jsonObj.getJSONObject("provider").getString("entityIdentityProviderURL"));
//                System.err.println("8:"+this.jsonObj.getString("refBaseSchemaRelease"));
//                System.err.println("9:"+this.jsonObj.getString("refDataModel"));
//                System.err.println("10:"+this.jsonObj.getJSONArray("accessInformation").getJSONObject(0).getString("accessSchemaType"));
            
///////////////////////////////DUMMY DATA ENDS/////////////////////////////////////////////////////////////
                
                
                       Vertex item = g.addV("___ITEM")
                		.property("	___NAME", this.jsonObj.getString("NAME"))
    					.property("___LATEST_RESOURCE_DATA", this.jsonObj.getString("latestResourceData"))
    					.property("___ITEM_STATUS", this.jsonObj.getString("__itemStatus"))
    					.property("___STATION_ID", this.jsonObj.getString("STATION_ID"))
    					.property("___ID", this.jsonObj.getString("id"))
    					.property("___ITEM_TYPE",this.jsonObj.getString("__itemType"))
//    					.property("___GEO_JSON_LOCATION",this.n.convert(this.jsonObj.getJSONObject("geoJsonLocation")).toGeoJson())
    					.property("___ITEM_DESCRIPTION",this.jsonObj.getString("itemDescription"))
    					.property("___RESOURCE_ID", this.jsonObj.getJSONArray("accessInformation").getJSONObject(0).getJSONObject("accessVariables").getString("resourceId"))
    					.property("___RESOURCE_SERVER_ID", this.jsonObj.getJSONArray("accessInformation").getJSONObject(0).getJSONObject("accessVariables").getString("resourceServerId"))
    					.property("___CREATED_AT", this.jsonObj.getString("__createdAt"))
    					.property("___UPDATED_AT", this.jsonObj.getString("__updatedAt"))
    					.property("___VERSION", this.jsonObj.getInt("__version"))
    					.property("___ACCESS_SCHEMA",this.jsonObj.getJSONArray("accessInformation").getJSONObject(0).getString("accessSchema"))
    					.property("___ON_BOARDED_BY", this.jsonObj.getString("__onboardedBy")).next();
                       System.err.println("api2");
             
                        Vertex provider = g.addV("___PROVIDER")
                         .property("___ENTITY_ID", this.jsonObj.getJSONObject("provider").getString("entityId"))
                         .property("___ENTITY_IDENTITY_TYPE", this.jsonObj.getJSONObject("provider").getString("entityIdentityType"))
                         .property("___ENTITY_IDENTITY_PROVIDER_URL", this.jsonObj.getJSONObject("provider").getString("entityIdentityProviderURL"))
                         .property("___ENTITY_URL",this.jsonObj.getJSONObject("provider").getString("entityURL")).next();
            
                        Vertex dataModel=g.addV("___DATA_MODEL")
                         .property("___REF_BASE_SCHEMA",this.jsonObj.getString("refBaseSchema"))
                         .property("___REF_BASE_SCHEMA_RELEASE",this.jsonObj.getString("refBaseSchemaRelease"))
                         .property("___REF_DATA_MODEL",this.jsonObj.getString("refDataModel")).next();       
                         
                        Vertex resourceClass=g.addV("___RESOURCE_CLASS")
                        		.property("___ACCESS_SCHEMA_TYPE",this.jsonObj.getJSONArray("accessInformation").getJSONObject(0).getString("accessSchemaType"))
                        		.property("___RESOURCE_CLASS",this.jsonObj.getJSONArray("accessInformation").getJSONObject(0).getJSONObject("accessVariables").getString("resourceClass"))
                        		.next();
                                 
                        Vertex tag;
                        System.err.println("api3");
                        
                this.g.V(provider).as("a").V(item).addE("___PROVIDES").from("a").next();
                this.g.V(item).as("a").V(dataModel).addE("___INSTANCE_OF").from("a").next();
                this.g.V(item).as("a").V(resourceClass).addE("___HAS_CLASS").from("a").next();
                         
                         for (int i = 0; i < this.jsonObj.getJSONArray("tags").length(); i++) {
                        	 System.err.println("i:"+i+" "+this.jsonObj.getJSONArray("tags").getString(i));
                        	 tag=this.g.addV("___TAG")
                            		 .property("___TAG_NAME",this.jsonObj.getJSONArray("tags").getString(i)).next();
                        	 this.g.V(item).as("a").V(tag).addE("___ASSOCIATED_WITH").from("a").next();
						}
              
            if (this.supportsTransactions) {
            	this.g.tx().commit();
                res = "200OK";
            }

        } catch (Exception e) {
        	System.err.println(e);
            if (this.supportsTransactions) {
            	this.g.tx().rollback();
                res = "401";
            }
        }
           
        return res;
    }
//    public String readElements(String item_json) throws JSONException
//    {
////    	this.jsonObj = new JSONObject(item_json);
////    	 String id=this.jsonObj.getString("id");
////        if (g == null) {
////                return "";
////            }
////    	
////        Vertex v = graph.getVertex(id);
////        JSONObject json = GraphSONUtility.jsonFromElement(v);
////        System.out.println(json.toString());
//
//    	GraphSONUtility gsonUtility = new GraphSONUtility(GraphSONMode.NORMAL, null);
//    	this.jsonObj = new JSONObject(item_json);
//    	 String id=this.jsonObj.getString("id");
//    	
//    	 String res="";
//        if (g == null) {
//                return "";
//            }
////        g.V().has("___ID", this.jsonObj.getString("id")).out();
//    	if(g.V().has("___ID", this.jsonObj.getString("id")).hasNext())
//    	{
//       System.out.println(g.V().has("___ID", this.jsonObj.getString("id")).out()+ "in sysout");
//       res="Succesful";
//    	}
//    	else
//    	res="Failed";
//    	
//    	return res;
//    }
//    
  public String readElements(String item_json) {
  String res = "";
	try {
      if (g == null) {
          return "";
      }
      
      
      final List<Object> li=g.V().values().toList();
      
      
//      final List<Object> geojson = g.V().has("___STATION_ID","FWR015").values("___GEO_JSON_LOCATION").toList();
//      System.err.println("answer: " + geojson.toString());
//      
////      final List<Object> lat_lng = g.V().has("___STATION_ID","FWR015").values("___COORDINATES___").toList();
////      System.err.println("answer: " + lat_lng.toString());
//
//      final Optional<Map<Object, Object>> edge = g.V().has("___ENTITY_ID", "smart-cities-rbccps").outE("___PROVIDES").as("e").inV()
//              .has("___NAME", "Vishrantwadi Nala").select("e").valueMap(true).tryNext();
//      if (edge.isPresent()) {
//          System.err.println(edge.get().toString());
//      } else {
//      	System.err.println("EDGE not found");
//      }           
  } finally {
      if (supportsTransactions) {
          g.tx().rollback();
          res = "Successful";
      }
      else
    	  res="Failed";
  }
  return res;
}


    public String updateElements(String item_json)
    {
    String res = "";
  	this.jsonObj = new JSONObject(item_json);
  	try {
        if (g == null) {
            return "";
        }
        String id=this.jsonObj.getString("id");
        String s1=this.jsonObj.getString("NAME");
        if(s1!="")
        {
        	g.V().has("____ID",id).property("___NAME",s1).iterate();
        }
        String s2=this.jsonObj.getString("latestResourceData");
        if(s1!="")
        {
        	g.V().has("____ID",id).property("___LATEST_RESOURCE_DATA",s2).iterate();
        }
        String s3=this.jsonObj.getString("__itemStatus");
        
        if(s3!="")
        {
        	g.V().has("____ID",id).property("___ITEM_STATUS",s3).iterate();
        }
        String s5=this.jsonObj.getString("STATION_ID");
        if(s5!="")
        {
        	g.V().has("____ID",id).property("___STATION_ID",s5).iterate();
        }
        String s7=this.jsonObj.getString("__itemType");
		if(s7!="")
        {
        	g.V().has("____ID",id).property("___ITEM_TYPE",s7).iterate();
        }
//      String s8=this.n.convert(String s=this.jsonObj.getJSONObject("geoJsonLocation")).toGeoJson();
//		if(s8!="")
//        {
//        	g.V().has("____ID",id).property("___GEO_JSON_LOCATION",s8).iterate();
//        }
		String s9=this.jsonObj.getString("itemDescription");
		if(s9!="")
        {
        	g.V().has("____ID",id).property("___ITEM_DESCRIPTION",s9).iterate();
        }
		String s10=this.jsonObj.getJSONArray("accessInformation").getJSONObject(0).getJSONObject("accessVariables").getString("resourceId");
		if(s10!="")
        {
        	g.V().has("____ID",id).property("___RESOURCE_ID",s10).iterate();
        }
		String s11=this.jsonObj.getJSONArray("accessInformation").getJSONObject(0).getJSONObject("accessVariables").getString("resourceServerId");
		if(s11!="")
        {
        	g.V().has("____ID",id).property("___RESOURCE_SERVER_ID",s11).iterate();
        }
		String s12=this.jsonObj.getString("__createdAt");
		if(s12!="")
        {
        	g.V().has("____ID",id).property("___CREATED_AT",s12).iterate();
        }
		String s13=this.jsonObj.getString("__updatedAt");
		if(s13!="")
        {
        	g.V().has("____ID",id).property("___UPDATED_AT",s13).iterate();
        }
		int s14=this.jsonObj.getInt("__version");
		if(s14==0)
        {
        	g.V().has("____ID",id).property("___VERSION",s14).iterate();
        }
		String s15=this.jsonObj.getJSONArray("accessInformation").getJSONObject(0).getString("accessSchema");
		if(s15!="")
        {
        	g.V().has("____ID",id).property("___ACCESS_SCHEMA",s15).iterate();
        }
		String s16=this.jsonObj.getString("__onboardedBy");
		if(s16!="")
        {
        	g.V().has("____ID",id).property("___ON_BOARDED_BY",s16).iterate();
        }
		String s17=this.jsonObj.getJSONObject("provider").getString("entityId");
		if(s17!="")
        {
        	g.V().has("____ID",id).property("___ENTITY_ID",s17).iterate();
        }
		String s18=this.jsonObj.getJSONObject("provider").getString("entityIdentityType");
		if(s18!="")
        {
        	g.V().has("____ID",id).property("___ENTITY_IDENTITY_TYPE",s18).iterate();
        }
		String s19=this.jsonObj.getJSONObject("provider").getString("entityIdentityProviderURL");
		if(s19!="")
        {
        	g.V().has("____ID",id).property("___ENTITY_IDENTITY_PROVIDER_URL",s19).iterate();
        }
		String s20=this.jsonObj.getJSONObject("provider").getString("entityURL");
		if(s20!="")
        {
        	g.V().has("____ID",id).property("___ENTITY_URL",s20).iterate();
        }
		String s21=this.jsonObj.getString("refBaseSchema");
		if(s21!="")
        {
        	g.V().has("____ID",id).property("___REF_BASE_SCHEMA",s21).iterate();
        }
		String s22=this.jsonObj.getString("refBaseSchemaRelease");
		if(s22!="")
        {
        	g.V().has("____ID",id).property("___REF_BASE_SCHEMA_RELEASE",s22).iterate();
        }
		String s23=this.jsonObj.getString("refDataModel");       
		if(s23!="")
        {
        	g.V().has("____ID",id).property("___REF_DATA_MODEL",s23).iterate();
        }
		String s24=this.jsonObj.getJSONArray("accessInformation").getJSONObject(0).getString("accessSchemaType");
		if(s24!="")
        {
        	g.V().has("____ID",id).property("___ACCESS_SCHEMA_TYPE",s24).iterate();
        }
		String s25=this.jsonObj.getJSONArray("accessInformation").getJSONObject(0).getJSONObject("accessVariables").getString("resourceClass");
		if(s25!="")
        {
        	g.V().has("____ID",id).property("___RESOURCE_CLASS",s25).iterate();
        }
//		String s26=this.jsonObj.getString("tags");
//		if(s26!="")
//        {
//        	g.V().has("____ID",id).property("___TAG_NAME",s26).iterate();
//        }  	              
    } finally {
        if (supportsTransactions) {
            g.tx().commit();
            res = "200OK";
        }
    }
    return res;
  	}
    

    ////////////////////////////////////////////////////
    ///// Function to Delete Elements as per property///
    ////////////////////////////////////////////////////
    
//    public String deleteElements(String item_json)
//    {
//    	 String res = "";
//    	  	this.jsonObj = new JSONObject(item_json);
//    	  	try {
//    	        if (g == null) {
//    	            return "";
//    	        }
//    	        String id=this.jsonObj.getString("id");
//    	        String s1=this.jsonObj.getString("NAME");
//    	        if(g.V().has("____ID",id).property("___NAME",s1).hasNext())
//    	        {
//    	        	g.V().has("____ID",id).property("___NAME",s1).drop();
//    	        }
//    	        String s2=this.jsonObj.getString("latestResourceData");
//    	        if(g.V().has("____ID",id).property("___LATEST_RESOURCE_DATA",s2).hasNext())
//    	        {
//    	        	g.V().has("____ID",id).property("___LATEST_RESOURCE_DATA",s2).drop();
//    	        }
//    	        String s3=this.jsonObj.getString("__itemStatus");
//    	        
//    	        if(g.V().has("____ID",id).property("___ITEM_STATUS",s3).hasNext())
//    	        {
//    	        	g.V().has("____ID",id).property("___ITEM_STATUS",s3).drop();
//    	        }
//    	        String s5=this.jsonObj.getString("STATION_ID");
//    	        if(g.V().has("____ID",id).property("___STATION_ID",s5).hasNext())
//    	        {
//    	        	g.V().has("____ID",id).property("___STATION_ID",s5).drop();
//    	        }
//    	        String s7=this.jsonObj.getString("__itemType");
//    			if(g.V().has("____ID",id).property("___ITEM_TYPE",s7).hasNext())
//    	        {
//    	        	g.V().has("____ID",id).property("___ITEM_TYPE",s7).drop();
//    	        }
////    	      String s8=this.n.convert(String s=this.jsonObj.getJSONObject("geoJsonLocation")).toGeoJson();
////    			if(s8!="")
////    	        {
////    	        	g.V().has("____ID",id).property("___GEO_JSON_LOCATION",s8).drop();
////    	        }
//    			String s9=this.jsonObj.getString("itemDescription");
//    			if(g.V().has("____ID",id).property("___ITEM_DESCRIPTION",s9).hasNext())
//    	        {
//    	        	g.V().has("____ID",id).property("___ITEM_DESCRIPTION",s9).drop();
//    	        }
//    			String s10=this.jsonObj.getJSONArray("accessInformation").getJSONObject(0).getJSONObject("accessVariables").getString("resourceId");
//    			if(g.V().has("____ID",id).property("___RESOURCE_ID",s10).hasNext())
//    	        {
//    	        	g.V().has("____ID",id).property("___RESOURCE_ID",s10).drop();
//    	        }
//    			String s11=this.jsonObj.getJSONArray("accessInformation").getJSONObject(0).getJSONObject("accessVariables").getString("resourceServerId");
//    			if(g.V().has("____ID",id).property("___RESOURCE_SERVER_ID",s11).hasNext())
//    	        {
//    	        	g.V().has("____ID",id).property("___RESOURCE_SERVER_ID",s11).drop();
//    	        }
//    			String s12=this.jsonObj.getString("__createdAt");
//    			if(g.V().has("____ID",id).property("___CREATED_AT",s12).hasNext())
//    	        {
//    	        	g.V().has("____ID",id).property("___CREATED_AT",s12).drop();
//    	        }
//    			String s13=this.jsonObj.getString("__updatedAt");
//    			if(g.V().has("____ID",id).property("___UPDATED_AT",s13).hasNext())
//    	        {
//    	        	g.V().has("____ID",id).property("___UPDATED_AT",s13).drop();
//    	        }
//    			int s14=this.jsonObj.getInt("__version");
//    			if(g.V().has("____ID",id).property("___VERSION",s14).hasNext())
//    	        {
//    	        	g.V().has("____ID",id).property("___VERSION",s14).drop();
//    	        }
//    			String s15=this.jsonObj.getJSONArray("accessInformation").getJSONObject(0).getString("accessSchema");
//    			if(g.V().has("____ID",id).property("___ACCESS_SCHEMA",s15).hasNext())
//    	        {
//    	        	g.V().has("____ID",id).property("___ACCESS_SCHEMA",s15).drop();
//    	        }
//    			String s16=this.jsonObj.getString("__onboardedBy");
//    			if(g.V().has("____ID",id).property("___ON_BOARDED_BY",s16).hasNext())
//    	        {
//    	        	g.V().has("____ID",id).property("___ON_BOARDED_BY",s16).drop();
//    	        }
//    			String s17=this.jsonObj.getJSONObject("provider").getString("entityId");
//    			if(g.V().has("____ID",id).property("___ENTITY_ID",s17).hasNext())
//    	        {
//    	        	g.V().has("____ID",id).property("___ENTITY_ID",s17).drop();
//    	        }
//    			String s18=this.jsonObj.getJSONObject("provider").getString("entityIdentityType");
//    			if(g.V().has("____ID",id).property("___ENTITY_IDENTITY_TYPE",s18).hasNext())
//    	        {
//    	        	g.V().has("____ID",id).property("___ENTITY_IDENTITY_TYPE",s18).drop();
//    	        }
//    			String s19=this.jsonObj.getJSONObject("provider").getString("entityIdentityProviderURL");
//    			if(g.V().has("____ID",id).property("___ENTITY_IDENTITY_PROVIDER_URL",s19).hasNext())
//    	        {
//    	        	g.V().has("____ID",id).property("___ENTITY_IDENTITY_PROVIDER_URL",s19).drop();
//    	        }
//    			String s20=this.jsonObj.getJSONObject("provider").getString("entityURL");
//    			if(g.V().has("____ID",id).property("___ENTITY_URL",s20).hasNext())
//    	        {
//    	        	g.V().has("____ID",id).property("___ENTITY_URL",s20).drop();
//    	        }
//    			String s21=this.jsonObj.getString("refBaseSchema");
//    			if(g.V().has("____ID",id).property("___REF_BASE_SCHEMA",s21).hasNext())
//    	        {
//    	        	g.V().has("____ID",id).property("___REF_BASE_SCHEMA",s21).drop();
//    	        }
//    			String s22=this.jsonObj.getString("refBaseSchemaRelease");
//    			if(g.V().has("____ID",id).property("___REF_BASE_SCHEMA_RELEASE",s22).hasNext())
//    	        {
//    	        	g.V().has("____ID",id).property("___REF_BASE_SCHEMA_RELEASE",s22).drop();
//    	        }
//    			String s23=this.jsonObj.getString("refDataModel");       
//    			if(g.V().has("____ID",id).property("___REF_DATA_MODEL",s23).hasNext())
//    	        {
//    	        	g.V().has("____ID",id).property("___REF_DATA_MODEL",s23).drop();
//    	        }
//    			String s24=this.jsonObj.getJSONArray("accessInformation").getJSONObject(0).getString("accessSchemaType");
//    			if(g.V().has("____ID",id).property("___ACCESS_SCHEMA_TYPE",s24).hasNext())
//    	        {
//    	        	g.V().has("____ID",id).property("___ACCESS_SCHEMA_TYPE",s24).drop();
//    	        }
//    			String s25=this.jsonObj.getJSONArray("accessInformation").getJSONObject(0).getJSONObject("accessVariables").getString("resourceClass");
//    			if(g.V().has("____ID",id).property("___RESOURCE_CLASS",s25).hasNext())
//    	        {
//    	        	g.V().has("____ID",id).property("___RESOURCE_CLASS",s25).drop();
//    	        }
////    			String s26=this.jsonObj.getString("tags");
////    			if(s26!="")
////    	        {
////    	        	g.V().has("____ID",id).property("___TAG_NAME",s26).iterate();
////    	        }  	              
//    	    } finally {
//    	        if (supportsTransactions) {
//    	            g.tx().rollback();
//    	            res = "201";
//    	        }
//    	    }
//    	    return res;
//            
//		
//            
//    }
    
    public String deleteVertex(String item_json)
    {
    	
    	 String res = "";
   	  	this.jsonObj = new JSONObject(item_json.substring(item_json.indexOf('{')));
   	  	String id=this.jsonObj.getString("id");
   	  	System.out.println("Deleting " + id);
   	  	System.out.println(this.jsonObj.getString("NAME"));
   	  	try {
   	  		if(g == null)
   	  			return "";
   	  		if(g.V().hasId(id).hasNext())
   	  		{
   	  		g.V().hasId(id).drop();
   	  		res = "Sucessful";
   	  		g.tx().commit();
   	  		}
   	  		else
   	  		{
   	  			res="Failed";
   	  			g.tx().rollback();
   	  		}
   	  	
   	  		
   	  	}finally {
   	        if (supportsTransactions) {
   	            g.tx().rollback();
   	        }
   	  	}
    	
		return res;
    	
    }
    
    //run the program ie call all the functions
    public void runApp() {
        try {
            // open and initialize the graph
        	System.err.println("RUN APP");
            this.g=this.openGraph();
            System.err.println("RUN APP2");
            // define the schema before loading data
            if (this.supportsSchema) {
            	System.err.println("CREATING SCHEMA");
                this.createSchema();
            }
            
//            System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE: RUN APP");
            
            JG.setJg(this);
            
//            System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE: RUN APP2 ");
           // close the graph
//            closeGraph();
            } catch (Exception e) {
        }
    }

    }