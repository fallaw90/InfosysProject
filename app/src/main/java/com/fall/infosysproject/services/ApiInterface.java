package com.fall.infosysproject.services;

import com.fall.infosysproject.models.Flickr;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiInterface {

    /*@GET("posts")
    Call<List<Post>> getPosts(
            @Query("userId") Integer[] userId,
            @Query("_sort") String sort,
            @Query("_order") String order
    );

    @GET("posts")
    Call<List<Post>> getPosts(@QueryMap Map<String, String> parameters);

    @GET("photos_public.gne?nojsoncallback=1&tagmode=any&format=json&tags={item}")
    Call<List<FlickerItem>> getItems(@Path("item") String item);
    */

    @GET
    Call<Flickr> getData(@Url String url);

}
