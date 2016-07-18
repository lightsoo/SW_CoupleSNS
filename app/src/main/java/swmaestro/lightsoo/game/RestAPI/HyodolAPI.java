package swmaestro.lightsoo.game.RestAPI;

import com.squareup.okhttp.RequestBody;

import retrofit.Call;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import swmaestro.lightsoo.game.Data.Message;

/**
 * Created by LG on 2016-07-17.
 */
public interface HyodolAPI {

    @Multipart
    @POST("/event")
    Call<Message> addEvent(@Part("event_title")String event_title,
                           @Part("event_date")String event_date,
                           @Part("event_img\"; filename=\"image.jpg\" ")RequestBody file1,
                           @Part("event_img\"; filename=\"image.jpg\" ")RequestBody file2);

}
