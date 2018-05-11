package space.dotcat.assistant.service;

import android.content.Context;
import android.content.Intent;

import space.dotcat.assistant.repository.authRepository.AuthRepository;

/**
 * Class for managing {@link MessageReceiverService} lifecycle
 */
public class MessageServiceHandler implements ServiceHandler {

    private Context mContext;

    private AuthRepository mAuthRepository;

    public MessageServiceHandler(Context context, AuthRepository authRepository) {
        mContext = context;

        mAuthRepository = authRepository;
    }

    @Override
    public void startService() {
        if(mAuthRepository.isMessageServiceStarted()) {
            return;
        }

        Intent startIntent = MessageReceiverService.getIntent(mContext);

        mContext.startService(startIntent);
    }

    @Override
    public void stopService() {
        Intent stopIntent = MessageReceiverService.getIntent(mContext);

        mContext.stopService(stopIntent);
    }
}
