package swmaestro.lightsoo.game.RestAPI;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import swmaestro.lightsoo.game.Data.Message;

/**
 * Created by LG on 2016-07-17.
 */
public interface HyodolAPI {

    @FormUrlEncoded
    @POST("/event")
    Call<Message> addEvent(@Field("event_title")String event_title,
                           @Field("event_date")String event_date);



}
