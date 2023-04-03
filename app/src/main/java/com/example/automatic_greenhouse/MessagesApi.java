package com.example.automatic_greenhouse;

import java.sql.Time;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MessagesApi {
    @GET("/api")
    Call<MessageFromServer> getData();

//    @POST("/sendingData")
//    @FormUrlEncoded
//    Call<MessageFromServer> sendData(@Body MessageToServer messageToServer);

    @POST("/requestClient")
    @FormUrlEncoded
    Call<MessageFromServer> sendData(@Field("port21On") boolean port21On,
                                     @Field("timeOnLighting") Time timeOnLighting,
                                     @Field("timeOffLighting") Time timeOffLighting,
                                     @Field("minimumSoilMoisture") int minimumSoilMoisture);
}
