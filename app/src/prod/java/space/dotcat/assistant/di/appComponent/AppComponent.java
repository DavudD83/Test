package space.dotcat.assistant.di.appComponent;

import javax.inject.Singleton;

import dagger.Component;
import space.dotcat.assistant.di.dataLayerComponent.DataLayerComponent;
import space.dotcat.assistant.di.dataLayerComponent.NetworkModule;
import space.dotcat.assistant.di.dataLayerComponent.RepoModule;

@Component(modules = {AppModule.class, DatabaseModule.class, SharedPreferencesModule.class})
@Singleton
public interface AppComponent {

    DataLayerComponent plusDataLayerComponent(NetworkModule networkModule, RepoModule repoModule);
}
