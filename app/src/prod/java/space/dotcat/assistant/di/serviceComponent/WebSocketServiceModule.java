package space.dotcat.assistant.di.serviceComponent;

import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import space.dotcat.assistant.repository.authRepository.AuthRepository;
import space.dotcat.assistant.webSocket.WebSocketService;
import space.dotcat.assistant.webSocket.WebSocketServiceImpl;

@Module
public class WebSocketServiceModule {

    private WebSocketServiceImpl.ServerListener mServerListener;

    public WebSocketServiceModule(WebSocketServiceImpl.ServerListener serverListener) {
        mServerListener = serverListener;
    }

    @Provides
    @ServiceScope
    WebSocketService providedWebSocketService(AuthRepository authRepository, OkHttpClient okHttpClient,
                                              Gson gsonConverter) {
        return new WebSocketServiceImpl(authRepository, okHttpClient, mServerListener, gsonConverter);
    }
}
