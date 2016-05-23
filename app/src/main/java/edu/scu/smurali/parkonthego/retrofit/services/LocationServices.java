package edu.scu.smurali.parkonthego.retrofit.services;

import edu.scu.smurali.parkonthego.retrofit.reponses.LocationResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by chshi on 5/23/2016.
 */
public interface LocationServices {

    @GET("search")
    Call<LocationResponse> getLocationDetails(
            @Query("id") String id);
}
