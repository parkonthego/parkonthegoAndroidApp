package edu.scu.smurali.parkonthego.retrofit.services;

import edu.scu.smurali.parkonthego.retrofit.reponses.ReservationCfnResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by chshi on 5/23/2016.
 */
public interface ReservationServices {

    @FormUrlEncoded
    @POST("reservation")
    Call<ReservationCfnResponse> createReservation(
            @Field("parkingid") Integer parkingId,
            @Field("userid") Integer userId,
            @Field("startingtime") String startingTime,
            @Field("endtime") String endTime,
            @Field("cost") Double cost);

   
}
