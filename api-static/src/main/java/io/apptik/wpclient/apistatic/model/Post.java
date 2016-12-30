package io.apptik.wpclient.apistatic.model;


import io.apptik.json.wrapper.JsonObjectWrapper;

public class Post extends JsonObjectWrapper{


    public String getTest1() {
        return "me";
    }

    public String getTest2() {
        return getJson().optString("not me");
    }
}
