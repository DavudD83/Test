package space.dotcat.assistant.repository;

import android.support.annotation.NonNull;

import space.dotcat.assistant.repository.roomsRepository.RoomRepository;
import space.dotcat.assistant.repository.roomsRepository.RoomRepositoryImpl;

public final class RepositoryProvider {

    private static ApiRepository sApiRepository;

    private static AuthRepository sAuthRepository;

    private static RoomRepository sRoomRepository;

    private RepositoryProvider() {
    }

    public static void setApiRepository(ApiRepository apiRepository) {
        sApiRepository = apiRepository;
    }

    public static void setAuthRepository(AuthRepository authRepository) {
        sAuthRepository = authRepository;
    }

    @NonNull
    public static ApiRepository provideApiRepository() {
        if(sApiRepository == null) {
            sApiRepository = new DefaultApiRepository();
        }

        return sApiRepository;
    }

    @NonNull
    public static AuthRepository provideAuthRepository() {
        if(sAuthRepository == null) {
            sAuthRepository = new DefaultAuthRepository();
        }

        return sAuthRepository;
    }

   /* public static RoomRepository provideRoomRepository() {
        if(sRoomRepository == null) {
            sRoomRepository = new RoomRepositoryImpl();
        }

        return sRoomRepository;
    }*/
}
