package io.apptik.wpclient.generator;

import io.apptik.comm.jus.http.HttpUrl;

public class Discover {


    public static String baseURL = "http://djodjo.org/linkedfarm/ff/wp-json/";

    public static void main(String[] args) throws Exception {

        WpClientMaker maker = new WpClientMaker();
//        maker.make(new InputStreamReader(Thread.currentThread().getContextClassLoader()
//                .getResourceAsStream("routes.json")));

        maker.make(HttpUrl.parse(baseURL));
    }

}
