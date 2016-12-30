package io.apptik.wpclient.generator.discover;

import io.apptik.json.JsonArray;
import io.apptik.json.JsonElement;
import io.apptik.json.JsonObject;
import io.apptik.json.wrapper.JsonObjectWrapper;
import io.apptik.json.wrapper.TypedJsonObject;


public class OptionsResponse extends JsonObjectWrapper {

    public OptionsResponse(JsonObject jsonObject) {
        super(jsonObject);
    }

    public String getNamespace() {
        return getJson().optString("namespace");
    }

    public JsonArray getMethods() {
        return getJson().optJsonArray("methods");
    }

    public JsonArray getEndpoints() {
        return getJson().optJsonArray("endpoints");
    }

    public JsonObject getHalLinks() {
        return getJson().optJsonObject("_links");
    }

    public JsonObject getSchema() {
        return getJson().optJsonObject("schema");
    }

    public TypedJsonObject<Route> getRoutes() {

        return new TypedJsonObject<Route>() {
            @Override
            protected Route get(JsonElement jsonElement, String key) {
                return new Route(jsonElement.asJsonObject());
            }

            @Override
            protected JsonElement to(Route value) {
                return value.getJson();
            }
        }.wrap(getJson().optJsonObject("routes"));
    }

    public JsonArray getRoutesNames() {
        return getJson().optJsonObject("routes").names();
    }
}
