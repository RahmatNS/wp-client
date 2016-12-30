package io.apptik.wpclient.apistatic;


import java.util.Map;

import io.apptik.comm.jus.NetworkRequest;
import io.apptik.comm.jus.Request;
import io.apptik.comm.jus.retro.http.Body;
import io.apptik.comm.jus.retro.http.DELETE;
import io.apptik.comm.jus.retro.http.GET;
import io.apptik.comm.jus.retro.http.Header;
import io.apptik.comm.jus.retro.http.Multipart;
import io.apptik.comm.jus.retro.http.POST;
import io.apptik.comm.jus.retro.http.PartMap;
import io.apptik.comm.jus.retro.http.Path;
import io.apptik.comm.jus.retro.http.Query;
import io.apptik.comm.jus.retro.http.QueryMap;
import io.apptik.json.wrapper.CachedTypedJsonArray;
import io.apptik.json.wrapper.JsonObjectArrayWrapper;
import io.apptik.json.wrapper.TypedJsonArray;
import io.apptik.wpclient.apistatic.model.Comment;
import io.apptik.wpclient.apistatic.model.Media;
import io.apptik.wpclient.apistatic.model.Meta;
import io.apptik.wpclient.apistatic.model.Page;
import io.apptik.wpclient.apistatic.model.Post;
import io.apptik.wpclient.apistatic.model.Taxonomy;
import io.apptik.wpclient.apistatic.model.User;

public interface APIv2 {

/* POSTS */

    /**
     * Creates a new Post.
     *
     * @param postFields Map of Post fields
     * @return The created Post object
     */
    @POST("posts")
    Request<Post> createPost(@Body Map<String, Object> postFields);

    /**
     * Gets all Posts.
     *
     * @return List of Post objects
     */
    @GET("posts")
    Request<JsonObjectArrayWrapper<Post>> getPosts();

    /**
     * Gets all Posts using provided query params
     *
     * @param map Optional query parameters
     * @return List of Post objects
     */
    @GET("posts")
    Request<JsonObjectArrayWrapper<Post>> getPosts(@QueryMap Map<String, String> map);

    /**
     * Gets a single Post.
     *
     * @param postId Id of the Post
     * @param map    Optional query params
     * @return Post object
     */
    @GET("posts/{id}")
    Request<Post> getPost(@Path("id") long postId, @QueryMap Map<String, String> map);

    /**
     * Gets all Posts created by a User.
     *
     * @param authorId Id of the User
     * @param status   The status of the post, eg. draft or publish
     * @return List of Post objects for the User
     */
    @GET("posts")
    Request<JsonObjectArrayWrapper<Post>> getPostsForAuthor(@Query("author") long authorId, @Query("status") String
            status, @Query("context") String context);

    /**
     * Updates an existing Post.
     *
     * @param postId     Id of the Post
     * @param postFields Map of the fields to update
     * @return The updated Post object
     */
    @POST("posts/{id}")
    Request<Post> updatePost(@Path("id") long postId, @Body Map<String, Object> postFields);

    /**
     * Deletes a Post.
     *
     * @param postId Id of the Post
     * @param force  Whether to bypass trash and force deletion.
     * @return Post object that was deleted
     */
    @DELETE("posts/{id}")
    Request<Post> deletePost(@Path("id") long postId, @Query("force") boolean force, @Query
            ("context") String context);

    /**
     * Creates new Meta objects for a Post
     *
     * @param postId Id of the Post
     * @param fields Map containing key/value pairs
     * @return The created PostMeta object
     */
    @POST("posts/{id}/meta")
    Request<Meta> createPostMeta(@Path("id") long postId, @Body Map<String, Object> fields);

    @GET("posts/{id}/meta")
    Request<JsonObjectArrayWrapper<Meta>> getPostMeta(@Path("id") long postId);

    @GET("posts/{postId}/meta/{metaId}")
    Request<Meta> getPostMeta(@Path("postId") long postId, @Path("metaId") long metaId);

    @POST("posts/{postId}/meta/{metaId}")
    Request<Meta> updatePostMeta(@Path("postId") long postId, @Path("metaId") long metaId, @Body
            Map<String, Object> fields);

    @DELETE("posts/{postId}/meta/{metaId}?force=true")
    Request<Meta> deletePostMeta(@Path("postId") long postId, @Path("metaId") long metaId);


    @GET("posts/{postId}/revisions")
    Request<JsonObjectArrayWrapper<Post>> getPostRevisions(@Path("postId") long postId);

    @GET("posts/{postId}/revisions/{revId}")
    Request<Post> getPostRevision(@Path("postId") long postId, @Path("revId") long revId);

    @DELETE("posts/{postId}/revisions/{revId}")
    Request<Post> deltePostRevision(@Path("postId") long postId, @Path("revId") long revId);


    @POST("posts/{postId}/categories/{catId}")
    Request<Taxonomy> setPostCategory(@Path("postId") long postId, @Path("catId") long catId);

    @GET("posts/{postId}/categories")
    Request<JsonObjectArrayWrapper<Taxonomy>> getPostCategories(@Path("postId") long postId);

    @GET("posts/{postId}/categories/{catId}")
    Request<Taxonomy> getPostCategory(@Path("postId") long postId, @Path("catId") long catId);

    @DELETE("posts/{postId}/categories/{catId}")
    Request<Taxonomy> deletePostCategory(@Path("postId") long postId, @Path("catId") long catId);


    @POST("posts/{postId}/tags/{tagId}")
    Request<Taxonomy> setPostTag(@Path("postId") long postId, @Path("tagId") long tagId);

    //@GET("posts/{postId}/tags")
    //Request<List<Taxonomy>> getPostTags(@Path("postId") long postId);

    @GET("tags")
    Request<JsonObjectArrayWrapper<Taxonomy>> getPostTags(@Query("post") long postId);

    @GET("posts/{postId}/tags/{tagId}")
    Request<Taxonomy> getPostTag(@Path("postId") long postId, @Path("tagId") long catId);

    @DELETE("posts/{postId}/tags/{tagId}")
    Request<Taxonomy> deletePostTag(@Path("postId") long postId, @Path("tagId") long catId);


    /* PAGES */

    @POST("pages")
    Request<Page> createPage(@Body Map<String, Object> fieldMap);

    @GET("pages")
    Request<JsonObjectArrayWrapper<Page>> getPages();


    @GET("pages/{pageId}")
    Request<Page> getPage(@Path("pageId") long pageId);

    @POST("pages/{pageId}")
    Request<Page> updatePage(@Path("pageId") long pageId, @Body Map<String, Object> fieldMap);

    @DELETE("pages/{pageId}")
    Request<Page> deletePage(@Path("pageId") long pageId);


    @POST("pages/{pageId}/meta")
    Request<Meta> createPageMeta(@Path("pageId") long pageId, @Body Map<String, Object> fields);

    @GET("pages/{pageId}/meta")
    Request<JsonObjectArrayWrapper<Media>> getPageMeta(@Path("pageId") long pageId);

    @GET("pages/{pageId}/meta/{metaId}")
    Request<Meta> getPageMeta(@Path("pageId") long postId, @Path("metaId") long metaId);

    @POST("pages/{pageId}/meta/{metaId}")
    Request<Meta> updatePageMeta(@Path("pageId") long postId, @Path("metaId") long metaId, @Body
            Map<String, Object> fields);

    @DELETE("pages/{pageId}/meta/{metaId}")
    Request<Meta> deletePageMeta(@Path("pageId") long postId, @Path("metaId") long metaId);


    @GET("pages/{pageId}/revisions")
    Request<JsonObjectArrayWrapper<Page>> getPageRevisions(@Path("pageId") long postId);

    @GET("pages/{pageId}/revisions/{revId}")
    Request<Page> getPageRevision(@Path("pageId") long postId, @Path("revId") long revId);

    @DELETE("pages/{pageId}/revisions/{revId}")
    Request<Page> deltePageRevision(@Path("pageId") long postId, @Path("revId") long revId);


    /* MEDIA */

    /**
     * Upload a new Media item into WordPress.
     *
     * @param header Content-Disposition header containing filename, eg "filename=file.jpg"
     * @param params Map containing all fields to upload
     * @return Media item created
     * @see ContentUtil#makeMediaItemUploadMap(Media, File)
     */
    @Multipart
    @POST("media")
    Request<Media> createMedia(@Header("Content-Disposition") String header,
                               @PartMap Map<String, NetworkRequest> params);

    /**
     * Gets all Media objects.
     *
     * @return List of Media objects
     */
    @GET("media")
    Request<JsonObjectArrayWrapper<Media>> getMedia();

    /**
     * Returns a single Media item.
     *
     * @param mediaId Id of the Media item
     * @return The Media object
     */
    @GET("media/{id}")
    Request<Media> getMedia(@Path("id") long mediaId);

    /**
     * Returns all Media items attached to a Post.
     *
     * @param postId Id of the Post
     * @param type   MIME type of Media
     * @return List of Media objects
     */
    @GET("posts/{id}/media/{type}")
    Request<JsonObjectArrayWrapper<Media>> getMediaForPost(@Path("id") long postId, @Path("type") String type);

    /**
     * Updates a Media item.
     *
     * @param mediaId Id the Media item
     * @param fields  Fields to update
     * @return The updated Media object
     */
    @POST("media/{id}")
    Request<Media> updateMedia(@Path("id") long mediaId, @Body Map<String, Object> fields);

    /**
     * Deletes a Media item.
     *
     * @param mediaId Id of the Media item
     * @return The deleted Media object
     */
    @DELETE("media/{id}?force=true")
    Request<Media> deleteMedia(@Path("id") long mediaId);


    /* TYPES */

    //@GET("types")

    //@GET("types/{typeId}")


    /* STATUSES */

    // @GET("statuses")

    // @GET("statuses/{statusId}")


    /* TAXONOMIES */

    // @GET("taxonomies")

    // @GET("taxonomies/{id}")


    /* CATEGORIES */

    @POST("categories")
    Request<Taxonomy> createCategory(@Body Map<String, Object> fields);

    @GET("categories")
    Request<JsonObjectArrayWrapper<Taxonomy>> getCategories();

    @GET("categories/{id}")
    Request<Taxonomy> getCategory(@Path("id") long id);

    @GET("categories")
    Request<JsonObjectArrayWrapper<Taxonomy>> getCategories(@QueryMap Map<String, Object> map);

    @POST("categories/{id}")
    Request<Taxonomy> updateCategory(@Path("id") long id, Map<String, Object> fields);

    @DELETE("categories/{id}")
    Request<Taxonomy> deleteCategory(@Path("id") long id);


    /* TAGS */

    @POST("tags")
    Request<Taxonomy> createTag(@Body Map<String, Object> fields);

    @GET("tags")
    Request<JsonObjectArrayWrapper<Taxonomy>> getTags();

    @GET("tags")
    Request<JsonObjectArrayWrapper<Taxonomy>> getTagsOrdered(@QueryMap Map<String, String> map);

    @GET("tags/{id}")
    Request<Taxonomy> getTag(@Path("id") long id);

    @POST("tags/{id}")
    Request<Taxonomy> updateTag(@Path("id") long id, Map<String, Object> fields);

    @DELETE("tags/{id}")
    Request<Taxonomy> deleteTag(@Path("id") long id);


    /* USERS */

    /**
     * Creates a new WordPress user.
     *
     * @param fields Map of fields
     * @return The created User object
     */
    @POST("users")
    Request<User> createUser(@Body Map<String, Object> fields);

    @GET("users")
    Request<JsonObjectArrayWrapper<User>> getUsers();

    @GET("users/{id}")
    Request<User> getUser(@Path("id") long id);

    @POST("users/{id}")
    Request<User> updateUser(@Path("id") long id, @Body Map<String, Object> fields);

    @DELETE("users/{id}")
    Request<User> deleteUser(@Path("id") long id);

    /**
     * Gets existing User using username.
     *
     * @param username Login username of the User
     * @return The User object
     */
    @GET("users/login/{username}")
    Request<User> getUserFromLogin(@Path("username") String username);

    @GET("users/email/{email}")
    Request<User> getUserFromEmail(@Path("email") String email);

    @GET("users/me")
    Request<User> getUserMe();


    /* COMMENTS */

    @POST("comments")
    Request<Comment> createComment(Map<String, Object> fields);

    @GET("comments")
    Request<JsonObjectArrayWrapper<Comment>> getComments();

    @GET("comments/{id}")
    Request<Comment> getComment(@Path("id") long id);

    @POST("comments/{id}")
    Request<Comment> updateComment(@Path("id") long id, Map<String, Object> fields);

    @DELETE("comments/{id}")
    Request<Comment> deleteComment(@Path("id") long id);

    
    /* OTHER */

    @GET("posts")
    Request<JsonObjectArrayWrapper<Post>> getPostsForTags(@Query("filter[tag]") String tag);

    /**
     * Returns the number of pages for each of the following post states:
     * publish, draft, private
     *
     * @return Number of pages for post states
     */
//    @GET("posts/counts")
//    Request<PostCount> getPostCounts();

}
