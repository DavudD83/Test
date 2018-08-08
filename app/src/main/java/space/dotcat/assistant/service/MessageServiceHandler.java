package space.dotcat.assistant.service;

import android.content.Context;
import android.content.Intent;

import space.dotcat.assistant.repository.authRepository.AuthRepository;

/**
 * Class for managing {@link MessageReceiverService} lifecycle
 */

public class MessageServiceHandler implements ServiceHandler {

    private Context mContext;

    public MessageServiceHandler(Context context) {
        mContext = context;
    }

    @Override
    public void startService() {
        if(MessageReceiverService.sIsWorking != null) {
            if(MessageReceiverService.sIsWorking) {
                return;
            }
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
