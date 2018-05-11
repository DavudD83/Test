package space.dotcat.assistant.di.serviceComponent;

import dagger.Subcomponent;
import space.dotcat.assistant.service.MessageReceiverService;

@Subcomponent(modules = {WiFiListenerModule.class, WebSocketServiceModule.class})
@ServiceScope
public interface MessageReceiverComponent {

    void inject(MessageReceiverService service);
}
