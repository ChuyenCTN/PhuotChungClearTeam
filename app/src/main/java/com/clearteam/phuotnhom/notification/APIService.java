package com.clearteam.phuotnhom.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization: key=AAAA9yWofjw:APA91bG7qYAgY2Jm_3vlcEVuV0t8wfr142zR5fSdXQxXueoZSHkpInLYL2hJpS5Yul70r_OgBja48R4seGrTUlUrHkWI0_yJ7VrKnpXqLyuU4eo-gERUSi56yPPdmxHVFv0udnMJtuGe"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotifycation(@Body Sender body);
}
