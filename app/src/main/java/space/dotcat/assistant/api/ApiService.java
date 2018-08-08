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

    /**
     *  Try to auth and get valid token for further operations.
     *
     * @param authorizationInfo info that you need to try authorize(e.g login, password)
     * @return single with authorization answer, which contains answer from server
     */

    @POST("auth")
    Single<AuthorizationAnswer> auth(@Body Authorization authorizationInfo);

    /**
     *  Load rooms from the server.
     *
     * @return single with room response object, which contains list of rooms inside
     */

    @GET("placements/")
    Single<RoomResponse> rooms();

    /**
     * Load things inside the placement.
     *
     * @param id placement id, where you want to load things
     * @return single with thing response object, which is simple wrapper of thing list
     */

    @GET("things/")
    Single<ThingResponse> things(@Query("placement") String id);

    /**
     *  Do action with thing. Basically, you can do toggle with every thing, but pay attention that
     *  you have diversity of operations for things.
     *
     * @param id thing id, that you want to execute
     * @param message info where you specify parameters how to execute thing
     * @return single with response action message, which contains info how execution was done
     */

    @POST("things/{id}/execute")
    Single<ResponseActionMessage> action(@Path("id") String id, @Body Message message);
}
