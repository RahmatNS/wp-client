package io.apptik.wpclient;

import java.lang.reflect.InvocationTargetException;

import io.apptik.comm.jus.Jus;
import io.apptik.comm.jus.RequestQueue;
import io.apptik.comm.jus.converter.JJsonConverterFactory;
import io.apptik.comm.jus.http.Headers;
import io.apptik.comm.jus.retro.RetroProxy;
import io.apptik.json.wrapper.JsonObjectWrapper;
import io.apptik.wpclient.apistatic.APIv2;

public class MyClass {


    public static String baseURL = "http://djodjo.org/linkedfarm/ff/wp-json/wp/v2/";
    public static RequestQueue queue;
    public static APIv2 apIv2;

    public static void main(String[] args) throws Exception {

        queue = Jus.newRequestQueue();
        RetroProxy retroProxy = new RetroProxy.Builder()
                .baseUrl(baseURL)
                .requestQueue(queue)
                //.addConverterFactory(JJsonConverterFactory.create())
                .addConverterFactory(JJsonConverterFactory.create())
                .build();
        apIv2 = retroProxy.create(APIv2.class);
        //JusLog.MarkerLog.on();

        test();
        queue.stopWhenDone();
    }

    private static void test() throws Exception {
        //apIv2.getPostMeta(2782,52);
        checkVars(apIv2.getPost(2327, null).getFuture().get());

        //apIv2.getPosts();
    }


    public static void checkVars(JsonObjectWrapper job) throws InvocationTargetException,
            IllegalAccessException {
        System.out.println(job.getJson());
    }

}
