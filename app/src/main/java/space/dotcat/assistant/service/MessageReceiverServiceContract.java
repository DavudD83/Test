package space.dotcat.assistant.service;

import android.content.Intent;

import space.dotcat.assistant.content.ApiError;

public interface MessageReceiverServiceContract {

    void sendErrorBroadcast(ApiError error);
}
