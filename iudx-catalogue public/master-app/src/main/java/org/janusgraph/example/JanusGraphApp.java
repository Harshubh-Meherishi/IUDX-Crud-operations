// Copyright 2017 JanusGraph Authors
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.janusgraph.example;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

    public class JanusGraphApp extends GraphApp {
    
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
    
    /**
     * Constructs a graph app using the given properties.
     * @param fileName location of the properties file
     */
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
         
            System.err.println("creating schema");
            createProperties(management);
            System.out.println("creating v labeeeels0");
            System.err.println("creating v labels");
            createVertexLabels(management);
            System.err.println("creating e labels");
            createEdgeLabels(management);
            System.err.println("creating c___I");
            createCompositeIndexes(management);
            System.err.println("creating m___I");
            createMixedIndexes(management);
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
    

    /**
     * Creates the composite indexes. A composite index is best used for
     * exact match lookups.
     */
    protected void createCompositeIndexes(final JanusGraphManagement management) {
    	System.err.println("Creating ci");
    }

    /**
     * Creates the mixed indexes. A mixed index requires that an external
     * indexing backend is configured on the graph instance. A mixed index
     * is best for full text search, numerical range, and geospatial queries.
     */
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
    @Override
    public void createElements() {
        super.createElements();
        if (useMixedIndex) {
            try {
                // mixed indexes typically have a delayed refresh interval
                Thread.sleep(10000);
            } catch (InterruptedException e) {
              // LOGGER.error(e.getMessage(), e);
            }
        }
            try {
            // naive check if the graph was previously created
            if (g.V().has("___NAME", "Hello").hasNext()) {
                if (supportsTransactions) {
                    g.tx().rollback();
                }
                return;
            }

            this.json=new ObjectMapper().readValue("{\n" +
                    "  \"type\": \"Feature\",\n" +
                    "  \"geometry\": {\n" +
                    "    \"type\": \"Circle\",\n" +
                    "    \"radius\": 30.5, " +
                    "    \"coordinates\": [20.5, 10.5]\n" +
                    "  },\n" +
                    "  \"properties\": {\n" +
                    "    \"name\": \"Dinagat Islands\"\n" +
                    "  }\n" +
                    "}", HashMap.class);
            
            Vertex item1 = g.addV("___ITEM")
            		.property("___NAME", "Vishrantwadi Nala")
					.property("___LATEST_RESOURCE_DATA", "https://pune.iudx.org.in/api/1.0.0/resource/latest/flood-sensor/Vishrantwadi%20Nala")
					.property("___ITEM_STATUS", "active")
					.property("___STATION_ID", "FWR015")
					.property("___ID", "e75c6460-89ef-4fcf-a937-c99766ffa38a")
					.property("___ITEM_TYPE", "resource-item")
					.property("___ITEM_DESCRIPTION", "Flood sensing station")
					.property("___GEO_JSON_LOCATION", this.n.convert(this.json))
					.property("___RESOURCE_ID", "Vishrantwadi Nala")
					.property("___RESOURCE_SERVER_ID", "https://pune.iudx.org.in:443")
					.property("___CREATED_AT", "Wed Mar 27 13:39:57 UTC 2019")
					.property("___UPDATED_AT", "Wed Mar 27 13:39:57 UTC 2019")
					.property("___VERSION", 1)
					.property("___ACCESS_SCHEMA", "https://raw.githubusercontent.com/rbccps-iisc/iudx-schemas/master/data_models/environment/floodSensor/examples/exApiObject_env_flood_climoPune_0.json")
					.property("___ON_BOARDED_BY", "iudx-provider").next();
                        
            Vertex provider1 = g.addV("___PROVIDER")
            		.property("___ENTITY_ID", "smart-cities-rbccps")
            		.property("___ENTITY_IDENTITY_TYPE", "X509")
            		.property("___ENTITY_IDENTITY_PROVIDER_URL", "https://ca.iudx.org.in")
            		.property("___ENTITY_URL", "https://example.iudx-provider.org").next();
                      
            Vertex dataModel1=g.addV("___DATA_MODEL")
            		.property("___REF_BASE_SCHEMA","https://raw.githubusercontent.com/rbccps-iisc/iudx-schemas/master/base_schemas/v0.0.0/iudx_data_item_schema.json")
            		.property("___REF_BASE_SCHEMA_RELEASE","v0.0.0")
            		.property("___REF_DATA_MODEL","https://raw.githubusercontent.com/rbccps-iisc/iudx-schemas/master/data_models/environment/floodSensor/examples/exdataModel_env_flood_climoPune_0.json#/dataModelSchema").next();
                                   
            Vertex resourceClass1=g.addV("___RESOURCE_CLASS")
            		.property("___RESOURCE_CLASS","flood-sensor")
            		.property("___ACCESS_SCHEMA_TYPE","openAPI").next();
                        
            Vertex item2 = g.addV("___ITEM")
            		.property("___NAME", "Hadapsar Mundhwa")
					.property("___LATEST_RESOURCE_DATA", "https://pune.iudx.org.in/api/1.0.0/resource/latest/flood-sensor/Hadapsar%20Mundhwa")
					.property("___ITEM_STATUS", "active")
					.property("___STATION_ID", "FWR015")
					.property("___ID", "b2df804d-3341-4ee9-abdb-d28a677282a8")
					.property("___ITEM_TYPE", "resource-item")
					.property("___GEO_JSON_LOCATION", this.n.convert(this.json))
					.property("___ITEM_DESCRIPTION", "Flood sensing station")
					.property("___RESOURCE_ID", "Hadapsar Mundhwa")
					.property("___RESOURCE_SERVER_ID", "https://pune.iudx.org.in:443")
					.property("___CREATED_AT", "Wed Mar 27 13:39:57 UTC 2019")
					.property("___UPDATED_AT", "Wed Mar 27 13:39:57 UTC 2019")
					.property("___VERSION", 1)
					.property("___ACCESS_SCHEMA", "https://raw.githubusercontent.com/rbccps-iisc/iudx-schemas/master/data_models/environment/floodSensor/examples/exApiObject_env_flood_climoPune_0.json")
					.property("___ON_BOARDED_BY", "iudx-provider").next();
                       
            Vertex provider2 = g.addV("___PROVIDER")
            		.property("___ENTITY_ID", "smart-cities-rbccps")
            		.property("___ENTITY_IDENTITY_TYPE", "X509")
            		.property("___ENTITY_IDENTITY_PROVIDER_URL", "https://ca.iudx.org.in")
            		.property("___ENTITY_URL", "https://example.iudx-provider.org").next();
               
            Vertex dataModel2=g.addV("___DATA_MODEL")
            		.property("___REF_BASE_SCHEMA","https://raw.githubusercontent.com/rbccps-iisc/iudx-schemas/master/base_schemas/v0.0.0/iudx_data_item_schema.json")
            		.property("___REF_BASE_SCHEMA_RELEASE","v0.0.0")
            		.property("___REF_DATA_MODEL","https://raw.githubusercontent.com/rbccps-iisc/iudx-schemas/master/data_models/environment/floodSensor/examples/exdataModel_env_flood_climoPune_0.json#/dataModelSchema").next();       
            
            Vertex resourceClass2=g.addV("___RESOURCE_CLASS")
            		.property("___RESOURCE_CLASS","flood-sensor")
            		.property("___ACCESS_SCHEMA_TYPE","openAPI").next();
            
            Vertex tag1=g.addV("___TAG")
            		.property("___TAG_NAME","flood").next();
            
            Vertex tag2=g.addV("___TAG")
            		.property("___TAG_NAME","flooding").next();

            Vertex tag3=g.addV("___TAG")
            		.property("___TAG_NAME","flood alert").next();

            Vertex tag4=g.addV("___TAG")
            		.property("___TAG_NAME","flood danger").next();

            
            
           
            //define relations
            g.V(provider1).as("a").V(item1).addE("___PROVIDES").from("a").next();
            g.V(item1).as("a").V(dataModel1).addE("___INSTANCE_OF").from("a").next();
            g.V(item1).as("a").V(resourceClass1).addE("___HAS_CLASS").from("a").next();
            g.V(item1).as("a").V(tag1).addE("___ASSOCIATED_WITH").from("a").next();
            g.V(item1).as("a").V(tag2).addE("___ASSOCIATED_WITH").from("a").next();
            g.V(item1).as("a").V(tag3).addE("___ASSOCIATED_WITH").from("a").next();
            g.V(item1).as("a").V(tag4).addE("___ASSOCIATED_WITH").from("a").next();
            
            
            g.V(provider2).as("a").V(item2).addE("___PROVIDES").from("a").next();
            g.V(item2).as("a").V(dataModel2).addE("___INSTANCE_OF").from("a").next();
            g.V(item2).as("a").V(resourceClass2).addE("___HAS_CLASS").from("a").next();
            g.V(item2).as("a").V(tag1).addE("___ASSOCIATED_WITH").from("a").next();
            g.V(item2).as("a").V(tag2).addE("___ASSOCIATED_WITH").from("a").next();
            g.V(item2).as("a").V(tag3).addE("___ASSOCIATED_WITH").from("a").next();
            g.V(item2).as("a").V(tag4).addE("___ASSOCIATED_WITH").from("a").next();
            
  
            if (supportsTransactions) {
                g.tx().commit();
            }

        } catch (Exception e) {
            if (supportsTransactions) {
                g.tx().rollback();
            }
        }
    }

    
    
    // queries
    public void readElements() {
        try {
            if (g == null) {
                return;
            }
            final List<Object> geojson = g.V().has("___STATION_ID","FWR015").values("___GEO_JSON_LOCATION").toList();
            System.err.println("answer: " + geojson.toString());
            
//            final List<Object> lat_lng = g.V().has("___STATION_ID","FWR015").values("___COORDINATES___").toList();
//            System.err.println("answer: " + lat_lng.toString());

            final Optional<Map<Object, Object>> edge = g.V().has("___ENTITY_ID", "smart-cities-rbccps").outE("___PROVIDES").as("e").inV()
                    .has("___NAME", "Vishrantwadi Nala").select("e").valueMap(true).tryNext();
            if (edge.isPresent()) {
                System.err.println(edge.get().toString());
            } else {
            	System.err.println("EDGE not found");
            }
            
        } finally {
            // the default behavior automatically starts a transaction for
            // any graph interaction, so it is best to finish the transaction
            // even for read-only graph query operations
            if (supportsTransactions) {
                g.tx().rollback();
            }
        }
    }

    
    
    
    //run the program ie call all the functions
    public void runApp() {
        try {
            // open and initialize the graph
            openGraph();

            // define the schema before loading data
            if (supportsSchema) {
                createSchema();
            }
            // build the graph structure
            createElements();
            // read to see they were made
            readElements();
           // close the graph
            closeGraph();
            } catch (Exception e) {
        }
    }

    public static void main(String[] args) throws Exception {
    	System.err.println("1");
    	File resourcesDirectory = new File("src/main/resources/conf/jgex-cql.properties");
    	System.err.println("2");
        final String fileName = resourcesDirectory.getAbsolutePath();
        final boolean drop = (args != null && args.length > 1) ? "drop".equalsIgnoreCase(args[1]) : false;
        final JanusGraphApp app = new JanusGraphApp(fileName);
        if (drop) {
        	System.err.println("3");
            app.openGraph();
            System.err.println("4");
//            app.dropGraph();
        } else {
            app.runApp();
        }
    }
}