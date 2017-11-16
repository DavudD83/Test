package space.dotcat.assistant.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;
import space.dotcat.assistant.AppDelegate;
import space.dotcat.assistant.repository.RepositoryProvider;

class RequestMatcher {

    private final Map<String, String> mRequests = new HashMap<>();

    public RequestMatcher() {
        mRequests.put("/auth", "auth.json");
        mRequests.put("/placements/", "placements.json");
        mRequests.put("/things/", "things.json");
        mRequests.put("/messages/", "message.json");
    }

    public boolean shouldIntercept(String url) {
        return mRequests.containsKey(url);
    }

    public Response proceed(Request request, String path) {
        String token = RepositoryProvider.provideAuthRepository().token();

        if("ERROR".equals(token)) {
            return OkHttpResponse.error(request, 500, "error in path " + path);
        }

        if("ERROR_EMPTY_ROOMS".equals(token)) {
            return createResponseFromAsset(request, "empty_response_rooms.json");
        }

        if("ERROR_EMPTY_THINGS".equals(token)) {
            return createResponseFromAsset(request, "empty_response_things.json");
        }


        if(shouldIntercept(path)) {
            String mockedPath = mRequests.get(path);

            return createResponseFromAsset(request, mockedPath);
        }

        return OkHttpResponse.error(request, 500, "Incorrectly intercepted request");
    }

    private Response createResponseFromAsset(Request request, String assertPath) {
        try {
            InputStream stream = AppDelegate.getContext().getAssets().open(assertPath);

            try{
                return OkHttpResponse.success(request, stream);
            } finally {
                stream.close();
            }
        }
        catch (IOException e) {
            return OkHttpResponse.error(request, 500, e.getMessage());
        }
    }
}
