package io.apptik.wpclient.generator;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import io.apptik.comm.jus.Jus;
import io.apptik.comm.jus.Request;
import io.apptik.comm.jus.RequestQueue;
import io.apptik.comm.jus.converter.JJsonConverterFactory;
import io.apptik.comm.jus.http.HttpUrl;
import io.apptik.json.JsonArray;
import io.apptik.json.JsonElement;
import io.apptik.json.JsonObject;
import io.apptik.json.JsonString;
import io.apptik.json.modelgen.FtlGenerator;
import io.apptik.json.schema.SchemaV4;
import io.apptik.json.wrapper.JsonObjectArrayWrapper;
import io.apptik.json.wrapper.JsonObjectMapWrapper;
import io.apptik.json.wrapper.TypedJsonObject;
import io.apptik.wpclient.generator.discover.Argument;
import io.apptik.wpclient.generator.discover.Endpoint;
import io.apptik.wpclient.generator.discover.OptionsResponse;
import io.apptik.wpclient.generator.discover.Route;

import static io.apptik.comm.jus.Request.Method.GET;
import static io.apptik.comm.jus.Request.Method.OPTIONS;

public class WpClientMaker {

    private MakerConfig config;
    private RequestQueue queue;
    private boolean stopQueue = false;

    public WpClientMaker() {
        this.config = new MakerConfig();
    }

    public WpClientMaker(MakerConfig config) {
        this.config = config;
    }

    public WpClientMaker(MakerConfig config, RequestQueue requestQueue) {
        this.config = config;
        this.queue = requestQueue;
    }


    public void make(HttpUrl routes) throws ExecutionException, InterruptedException {
        if (queue == null) {
            queue = Jus.newRequestQueue().addConverterFactory(JJsonConverterFactory.create());
            stopQueue = true;
        }
        Request<JsonObject> request =
                new Request<>(GET, routes, JsonObject.class);
        System.out.println("Start discovering BASE: " + routes);
        JsonObject job = queue.add(request).getFuture().get();
        make(job);
    }

    public void make(InputStreamReader routes) throws IOException, ExecutionException,
            InterruptedException {
        make(JsonObject.readFrom(routes).asJsonObject());
    }

    public void make(String routes) throws IOException, ExecutionException, InterruptedException {
        make(JsonObject.readFrom(routes).asJsonObject());
    }

    /**
     * basically we:
     * 1) get the index of the wp
     * 2) iterate routes
     * 3) when we find _links inside one we
     * 1) call discoverOPTIONS
     */
    public void make(JsonObject routes) throws ExecutionException, InterruptedException {
        if (queue == null) {
            queue = Jus.newRequestQueue().addConverterFactory(JJsonConverterFactory.create());
            stopQueue = true;
        }
        List<RequestDesc> requests = new ArrayList<>();
        OptionsResponse op = new OptionsResponse(routes);
        //System.out.println(op);
        TypedJsonObject<Route> routesObj = op.getRoutes();
        for (Map.Entry<String, Route> routeEntry : routesObj) {
            System.out.println("found Route: " + routeEntry.getKey());
            List<RequestDesc> tmpReq = getEndpoints(routeEntry);
            // -->> used to gen Object Models
            JsonObject links = routeEntry.getValue().getLinks().getJson();
            if (links != null) {
                String self = links.optString("self");
                if (self != null) {
                    //System.out.println("Got link: " + self);
                    //discoverOPTIONS(self);
                }
            }
            // <<--
            requests.addAll(tmpReq);
        }

        if (stopQueue) {
            queue.stop();
            stopQueue = false;
        }

        try {
            FileWriter fw = new FileWriter(new File("./build/gen/WpApi.java"));
            fw.append("package test;\n");
            fw.append("public class WpApi {\n");

            for (RequestDesc rd : requests) {
                try {
                    System.out.println("\tREQUEST ************\n" + rd.toString());
                    fw.append(rd.toString() + "\n");
                } catch (Exception ex) {
                    System.err.println("Error parsing request desc for Uri: " + rd.uri);
                }
            }
            fw.append("}");

            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<RequestDesc> getEndpoints(Map.Entry<String, Route> routeEntry) {
        Route route = routeEntry.getValue();
        JsonObjectArrayWrapper<Endpoint> endpoints = route.getEndpoints();
        List<RequestDesc> requests = new ArrayList<>();
        for (Endpoint endpoint : endpoints) {
//            System.out.println("\tfound Endpoint: " + endpoint);
            ArrayList<String> methods = endpoint.getMethods();
            JsonObjectMapWrapper<Argument> arguments = endpoint.getArguments();
            for (String method : methods) {
                RequestDesc r = new RequestDesc();
                r.method = method;
                r.uri = routeEntry.getKey();
//                System.out.println("\tfound Method: " + method);
                for (Map.Entry<String, Argument> argumentEntry : arguments) {
//                    System.out.println("\tfound Argument: " + argumentEntry.getKey());
//                    System.out.println("\t\tdesc: " + argumentEntry.getValue().getDescription());
                    r.desc = argumentEntry.getValue().getDescription();
                    r.queryArgs.add(new RequestDesc.Param(argumentEntry.getKey(), "String"));
                }
                requests.add(r);
            }
        }
        return requests;
    }

    /**
     * 1) call OPTIONS
     * 2) get schema
     * 3) generate Model class form schema
     *
     * @param url the URL to get options from
     * @throws Exception
     */
    private void discoverOPTIONS(String url) throws ExecutionException, InterruptedException {
        Request<JsonObject> request =
                new Request<>(OPTIONS, url, JsonObject.class);
        JsonObject job = queue.add(request).getFuture().get();
        OptionsResponse op = new OptionsResponse(job);
        System.out.println(op);
        System.out.println("Got OPTIONS: " + url);
        JsonObject schema = op.getSchema();
        if (schema != null) {
            //GENERATE MODEL - POJOs or JsonWrappers
            genModelFromSchema((SchemaV4) new SchemaV4().wrap(schema));
        }
    }


    private void genModelFromSchema(SchemaV4 schema) {
        System.out.println("Got Schema: " + schema.getTitle() + " : " + schema);
        if ("user".equals(schema.getTitle())) {
            //fix incorrect type
            //https://github.com/WP-API/WP-API/issues/2587
            if (schema.getProperties()
                    .getValue("registered_date").getJson().get("type").equals("date-time")) {

                schema.getProperties()
                        .getValue("registered_date").getJson().put("type", "string");
            }
        }
        FtlGenerator generator = new FtlGenerator();
        try {
            generator.fromSchema(schema, null, new File("./build/gen"));
        } catch (Exception e) {
            e.printStackTrace();
            // throw new RuntimeException("Error with schema: " + schema, e);
        }

    }

    private static String getLinkHref(JsonArray linkArr) {
        return linkArr.get(0).asJsonObject().getString("href");
    }


    //  <<<<Xexperimental>>>>
    private void discoverHAL(String url) throws Exception {
        System.out.println("Discovering: " + url);
        Request<JsonObject> request
                = new Request<>(OPTIONS, url, JsonObject.class);

        JsonObject job = queue.add(request).getFuture().get();
        OptionsResponse op = new OptionsResponse(job);
        //System.out.println(op);
        System.out.println("Got: " + url);
        JsonObject schema = op.getSchema();
        if (schema != null) {
            System.out.println("Got Schema: " + schema.get("title"));
        }

        JsonArray methods = op.getMethods();
        if (methods != null && methods.contains(new JsonString("GET"))) {
            Request<JsonObject> request2
                    = new Request<>(GET, url, JsonObject.class);
            JsonObject job2 = queue.add(request2).getFuture().get();
            OptionsResponse op2 = new OptionsResponse(job2);
            JsonObject links = op2.getHalLinks();
            if (links != null) {
                System.out.println("Got Links: " + links);
                if (links.has("self") && links.length() == 1) {
                    //todo?
                } else {
                    for (Map.Entry<String, JsonElement> entry : links) {
                        String nextLink = getLinkHref(entry.getValue().asJsonArray());
                        System.out.println("Got Link: " + entry.getKey()
                                + ":" + nextLink);
                        if (!"http://v2.wp-api.org/".equals(nextLink)
                                && !"self".equals(entry.getKey())) {
                            discoverHAL(nextLink);
                        }
                    }
                }
            }
        }
    }


}
