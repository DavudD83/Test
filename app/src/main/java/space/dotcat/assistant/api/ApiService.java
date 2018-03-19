package space.dotcat.assistant.api;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import space.dotcat.assistant.content.Authorization;
import space.dotcat.assistant.content.AuthorizationAnswer;
import space.dotcat.assistant.content.Message;
import space.dotcat.assistant.content.ResponseActionMessage;
import space.dotcat.assistant.content.RoomResponse;
import space.dotcat.assistant.content.ThingResponse;

public interface ApiService {

    @POST("auth")
    Single<AuthorizationAnswer> auth(@Body Authorization authorizationInfo);

    @GET("placements/")
    Single<RoomResponse> rooms();

    @GET("things/")
    Single<ThingResponse> things(@Query("placement") String id);

    @POST("things/{id}/execute")
    Single<ResponseActionMessage> action(@Path("id") String id, @Body Message message);
}
