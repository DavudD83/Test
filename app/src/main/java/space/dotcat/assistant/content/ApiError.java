package space.dotcat.assistant.content;

import com.google.gson.annotations.SerializedName;

public class ApiError extends Throwable {

    @SerializedName("error_id")
    private int mErrorId;

    @SerializedName("devel_message")
    private String mDevelMessage;

    @SerializedName("user_message")
    private String mUserMessage;

    @SerializedName("docs_url")
    private String mDocsUrl;

    public ApiError() {
        mErrorId = 0;
        mDevelMessage = "Base error";
        mUserMessage = "Error";
        mDocsUrl = "Base docs url";
    }

    public int getErrorId() {
        return mErrorId;
    }

    public void setErrorId(int errorId) {
        mErrorId = errorId;
    }

    public String getDevelMessage() {
        return mDevelMessage;
    }

    public void setDevelMessage(String develMessage) {
        mDevelMessage = develMessage;
    }

    public String getUserMessage() {
        return mUserMessage;
    }

    public void setUserMessage(String userMessage) {
        mUserMessage = userMessage;
    }

    public String getDocsUrl() {
        return mDocsUrl;
    }

    public void setDocsUrl(String docsUrl) {
        mDocsUrl = docsUrl;
    }
}

