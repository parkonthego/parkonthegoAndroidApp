package edu.scu.smurali.parkonthego.retrofit.services;

import edu.scu.smurali.parkonthego.retrofit.reponses.SignUpResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by chshi on 5/21/2016.
 */
public interface UserServices {
    @FormUrlEncoded
    @POST("register")
    Call<SignUpResponse> createNewUser(
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("email") String email,
            @Field("password") String password);

//    @FormUrlEncoded
//    @POST("login/login")
//    Call<LoginResponse> login(
//            @Field("username") String username,
//            @Field("password") String password,
//            @Field("device_id") String deviceId,
//            @Field("device_token") String deviceToken,
//            @Field("device_type") String deviceType,
//            @Field("language") String language);

}
