package space.dotcat.assistant.di.dataLayerComponent;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import space.dotcat.assistant.repository.authRepository.AuthRepository;
import space.dotcat.assistant.service.MessageServiceHandler;
import space.dotcat.assistant.service.ServiceHandler;

@Module
public class ServiceHandlerModule {

    @Provides
    @DataScope
    ServiceHandler provideMessageServiceHandler(Context context) {
        return new MessageServiceHandler(context);
    }

}
