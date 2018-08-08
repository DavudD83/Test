package space.dotcat.assistant.api;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Request;
import okhttp3.Response;
import space.dotcat.assistant.AppDelegate;
import space.dotcat.assistant.BuildConfig;
import space.dotcat.assistant.repository.authRepository.AuthRepository;
import space.dotcat.assistant.utils.AddressUtils;

public class RequestMatcher {

    private final Map<String, String> mRequests = new HashMap<>();

    private Context mContext;

    private SharedPreferences mSharedPreferences;

    public final static String ERROR = "ERROR";

    public final static String ERROR_EMPTY_ROOMS = "ERROR_EMPTY_ROOMS";

    public final static String ERROR_EMPTY_THINGS = "ERROR_EMPTY_THINGS";

    private final static String rest_address = AddressUtils.API_PATH + AddressUtils.API_VERSION;

    @Inject
    public RequestMatcher(Context context, SharedPreferences sharedPreferences) {
        mContext = context;

        mSharedPreferences = sharedPreferences;

        mRequests.put(rest_address +"auth", "auth.json");
        mRequests.put(rest_address + "placements/", "placements.json");
        mRequests.put(rest_address + "things/", "things.json");
        mRequests.put(rest_address + "things/?placement=R1", "things_R1.json");
        mRequests.put(rest_address + "things/D1/execute", "message.json");
        mRequests.put(rest_address + "things/Li1/execute", "message.json");
    }

    public boolean shouldIntercept(String url) {
        return mRequests.containsKey(url);
    }

    public Response proceed(Request request, String path) {
        String token = mSharedPreferences.getString(BuildConfig.TOKEN_KEY, BuildConfig.TOKEN_DEFAULT_VALUE);

        if(ERROR.equals(token)) {
            return OkHttpResponse.error(request, 500, "error in path " + path);
        }

        if(ERROR_EMPTY_ROOMS.equals(token)) {
            return createResponseFromAsset(request, "empty_response_rooms.json");
        }

        if(ERROR_EMPTY_THINGS.equals(token)) {
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
            InputStream stream = mContext.getAssets().open(assertPath);

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
