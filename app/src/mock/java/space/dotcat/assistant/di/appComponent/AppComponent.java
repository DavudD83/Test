package space.dotcat.assistant.di.appComponent;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Component;
import space.dotcat.assistant.di.dataLayerComponent.DataLayerComponent;
import space.dotcat.assistant.di.dataLayerComponent.NetworkModule;
import space.dotcat.assistant.di.dataLayerComponent.RepoModule;
import space.dotcat.assistant.repository.roomsRepository.localRoomsDataSource.RoomsDao;

@Component(modules = {AppModule.class, DatabaseModule.class, SharedPreferencesModule.class})
@Singleton
public interface AppComponent {

    SharedPreferences getFakeSharedPref();

    RoomsDao getFakeRoomsDao();

    DataLayerComponent plusDataLayerComponent(NetworkModule networkModule, RepoModule repoModule);
}
