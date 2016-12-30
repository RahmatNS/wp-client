package io.apptik.wpclient.generator.discover;


import io.apptik.json.JsonObject;
import io.apptik.json.wrapper.JsonObjectArrayWrapper;
import io.apptik.json.wrapper.JsonObjectWrapper;
import io.apptik.json.wrapper.JsonStringArrayWrapper;

public class Route extends JsonObjectWrapper {

    public Route(JsonObject jsonElement) {
        super(jsonElement);
    }

    public String getNamespace() {
        return getJson().optString("namespace");
    }
    public JsonStringArrayWrapper getMethods() {
        return new JsonStringArrayWrapper().wrap(getJson().optJsonArray("methods"));
    }
    public JsonObjectArrayWrapper<Endpoint> getEndpoints() {
        return new JsonObjectArrayWrapper()
                .wrap(getJson().optJsonArray("endpoints"), Endpoint.class);
    }
    public JsonObjectWrapper getLinks() {
        return new JsonObjectWrapper().wrap(getJson().optJsonObject("_links"));
    }

}
