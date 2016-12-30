package io.apptik.wpclient.generator.discover;


import io.apptik.json.JsonObject;
import io.apptik.json.wrapper.JsonObjectWrapper;
import io.apptik.json.wrapper.JsonStringArrayWrapper;

public class Argument extends JsonObjectWrapper {

    public Argument() {
    }

    public Argument(JsonObject jsonElement) {
        super(jsonElement);
    }

    public Boolean isRequired() {
        return getJson().optBoolean("required");
    }

    public String getDefault() {
        return getJson().optString("default");
    }

    public JsonStringArrayWrapper getEnum() {
        return new JsonStringArrayWrapper().wrap(getJson().optJsonArray("enum"));
    }

    public String getDescription() {
        return getJson().optString("description");
    }
}
