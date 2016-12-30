package io.apptik.wpclient.generator;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import freemarker.template.utility.StringUtil;

public class RequestDesc {

    String desc;
    String method;
    String uri;
    String responseType = "NetworkResponse";
    List<Param> queryArgs = new ArrayList<>();
    List<Param> pathArgs = new ArrayList<>();
    String bodyType;  //can be null if no body

    private final static Pattern resourcePattern = Pattern.compile(".*/([^/(?]+).*");
    private final static Pattern pathArgPattern = Pattern.compile(".*/([^/?]+).*");

    public static String getLastBit(String uri) {
        if(uri.equals("/")) {
            return uri;
        }
        Matcher m = resourcePattern.matcher(uri);
        if (m.matches()) {
            return m.group(1);
        }
        //should not happen as uri must be a valid URI
        throw new RuntimeException("Cannot get last bit from uri: " + uri);
    }

    @Override
    public String toString() {
        String resName;
        resName = getLastBit(uri);
        resName = StringUtil.capitalize(resName);
        StringBuilder sb = new StringBuilder();
        Prefix prefix = new Prefix(", ");
        if (desc != null) {
            sb.append("/**\n").append(desc).append("\n*/\n");
        }
        sb.append("@").append(method)
                .append("(\"").append(uri).append("\")\n")
                .append("Request<").append(responseType).append("> ")
                .append(method.toLowerCase()).append(resName)
                .append("(");

        if (pathArgs != null) {
            for (Param p : pathArgs) {
                sb.append(prefix.get()).append("@Path(\"").append(p.pName).append("\") ")
                        .append(p.pType).append(" ").append(p.pName);
            }
        }

        if (method.equals("POST") && bodyType != null) {
            sb.append(prefix.get()).append("@Body ").append(bodyType).append(" body");
        }

        if (queryArgs != null) {
            for (Param p : queryArgs) {
                sb.append(prefix.get()).append("@Query(\"").append(p.pName).append("\") ")
                        .append(p.pType).append(" ").append(p.pName);
            }
        }
        sb.append(");");
        return sb.toString();
    }


    public static class Param {

        public Param(String pName, String pType) {
            this.pName = pName;
            this.pType = pType;
        }

        String pType;
        String pName;
    }

    private static class Prefix {
        private boolean first = true;
        private String pref = "";

        public Prefix(String pref) {
            this.pref = pref;
        }

        public String get() {
            if (first) {
                first = false;
                return "";
            } else {
                return pref;
            }
        }
    }
}
