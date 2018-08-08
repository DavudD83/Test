package space.dotcat.assistant.api;



import retrofit2.Response;
import retrofit2.Retrofit;
import space.dotcat.assistant.content.ApiError;

/**
 * Base class for error parser which will be provided to Handling Error RxJava2 Factory
 */

public abstract class ErrorParser {

    /**
     *  Try to parse error from Retrofit response into ApiError. If response can not be parsed, it will
     *  return null
     *
     * @param retrofit retrofit instance that you used for request
     * @param response response that contains possible error body
     * @return parsed error from response error body
     */

    public abstract ApiError parseError(Retrofit retrofit, Response response);
}
