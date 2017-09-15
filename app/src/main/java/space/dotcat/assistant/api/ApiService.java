package space.dotcat.assistant.api;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import space.dotcat.assistant.content.Authorization;
import space.dotcat.assistant.content.AuthorizationAnswer;
import space.dotcat.assistant.content.Message;
import space.dotcat.assistant.content.RoomResponse;
import space.dotcat.assistant.content.ThingResponse;

public interface ApiService {

    @POST("auth")
    Observable<AuthorizationAnswer> auth(@Body Authorization authorizationInfo);

    @GET("placements/")
    Observable<RoomResponse> rooms();

    @GET("things/")
    Observable<ThingResponse> things(@Query("placement") String id);

    @POST("messages/")
    Observable<Message> action(@Body Message message);
}
