package space.dotcat.assistant.testMock;

import android.support.annotation.NonNull;

import space.dotcat.assistant.content.AuthorizationAnswer;
import space.dotcat.assistant.content.Url;
import space.dotcat.assistant.repository.AuthRepository;

public class MockAuthRepository implements AuthRepository {
    @Override
    public void saveAuthorizationAnswer(@NonNull AuthorizationAnswer answer) {
        //do nothing
    }

    @Override
    public String token() {
        return null;
    }

    @Override
    public void deleteToken() {
        // do nothing
    }

    @Override
    public void saveUrl(Url url) {
        //do nothing
    }

    @NonNull
    @Override
    public String url() {
        return "";
    }
}
