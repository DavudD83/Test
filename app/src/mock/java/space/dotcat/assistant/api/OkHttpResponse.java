package space.dotcat.assistant.api;


import java.io.IOException;
import java.io.InputStream;


import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class OkHttpResponse {

    private static final byte[] mBytes = new byte[0];

    private static final MediaType mMediaType = MediaType.parse("Application/json");

    private OkHttpResponse() {
    }

    public static Response success(Request request, InputStream stream) throws IOException {
        Buffer buffer = new Buffer().readFrom(stream);

        return new Response.Builder()
                .request(request)
                .code(200)
                .protocol(Protocol.HTTP_1_1)
                .message("SUCCESS")
                .body(ResponseBody.create(mMediaType, buffer.size(), buffer))
                .build();
    }

    public static Response error(Request request, int code, String message) {
        return new Response.Builder()
                .request(request)
                .code(code)
                .message(message)
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(mMediaType, mBytes))
                .build();
    }

}
