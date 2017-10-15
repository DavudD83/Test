package space.dotcat.assistant.content;


import android.support.annotation.NonNull;

import io.realm.RealmObject;

public class Url extends RealmObject {

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
