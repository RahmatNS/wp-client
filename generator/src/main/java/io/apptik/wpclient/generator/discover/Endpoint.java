package io.apptik.wpclient.generator.discover;


import java.util.ArrayList;

import io.apptik.json.JsonObject;
import io.apptik.json.wrapper.JsonObjectMapWrapper;
import io.apptik.json.wrapper.JsonObjectWrapper;

public class Endpoint extends JsonObjectWrapper {


    public Endpoint() {
    }

    public Endpoint(JsonObject jsonElement) {
        super(jsonElement);
    }

    public ArrayList<String> getMethods() {
        return getJson().optJsonArray("methods").toArrayList();
    }

    public JsonObjectMapWrapper<Argument> getArguments() {
        return new JsonObjectMapWrapper<>(Argument.class)
                .wrap(getJson().optJsonObject("args"));
    }
}
