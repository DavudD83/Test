package space.dotcat.assistant.content;


import android.support.annotation.NonNull;

@Deprecated
public class Url {

    private String mUrl;

    public Url(){
    }

    public Url(@NonNull String url){
        mUrl = url;
    }

    public String getUrl(){ return mUrl;}

    public void setUrl(@NonNull String url){
        mUrl = url;
    }
}
