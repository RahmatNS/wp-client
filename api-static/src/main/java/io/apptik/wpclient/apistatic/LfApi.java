package io.apptik.wpclient.apistatic;


import java.util.List;

import io.apptik.comm.jus.Request;
import io.apptik.comm.jus.retro.http.GET;
import io.apptik.comm.jus.retro.http.Path;
import io.apptik.wpclient.apistatic.model.Meta;

public interface LfApi {

    @GET("posts/{id}/meta")
    Request<List<Meta>> getProducerMeta(@Path("id") long postId);

}
