package space.dotcat.assistant.api;


import java.io.IOException;
import java.lang.annotation.Annotation;


import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import space.dotcat.assistant.content.ApiError;

public class BasicErrorParser extends ErrorParser {

    public ApiError parseError(Retrofit retrofit, Response response) {
        Converter<ResponseBody, ApiError> converter = retrofit
                .responseBodyConverter(ApiError.class, new Annotation[0]);

        ApiError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return null;
        }

        return error;
    }
}
