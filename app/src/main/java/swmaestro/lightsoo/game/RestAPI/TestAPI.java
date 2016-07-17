package swmaestro.lightsoo.game.RestAPI;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import swmaestro.lightsoo.game.Data.Message;

/**
 * Created by LG on 2016-04-18.
 */
public interface TestAPI {

    @FormUrlEncoded
    @POST("/auth/facebook/login")
    Call<Message> authFacebookLogin(@Field("access_token")String accessToken);

    @FormUrlEncoded
    @POST("/auth/local/login")
    Call<Message> authLocalLogin(@Field("email")String email,
                            @Field("pwd")String pwd);

    @FormUrlEncoded
    @POST("/join")
    Call<Message> join(@Field("email")String email,
                       @Field("pwd")String pwd,
                       @Field("user_name")String name);

    @GET("/myinfo")
    Call<Message> getMyInfo();

}
