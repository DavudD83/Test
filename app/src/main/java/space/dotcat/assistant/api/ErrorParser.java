package space.dotcat.assistant.api;


import java.io.IOException;
import java.lang.annotation.Annotation;


import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import space.dotcat.assistant.content.ApiError;

class ErrorParser {

    private ErrorParser(){
    }

    static ApiError parseError(Response response){
        Converter<ResponseBody, ApiError> converter = ApiFactory.buildRetrofit()
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
