package space.dotcat.assistant.di.serviceComponent;

import dagger.Subcomponent;
import space.dotcat.assistant.service.MessageReceiverService;

@Subcomponent(modules = {WiFiListenerModule.class, WebSocketServiceModule.class, MessageReceiverPresenterModule.class})
@ServiceScope
public interface MessageReceiverComponent {

    void inject(MessageReceiverService service);
}
