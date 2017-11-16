package space.dotcat.assistant.content;

import com.google.gson.annotations.SerializedName;

public class Body {

    @SerializedName("action")
    private String mAction;

    @SerializedName("obj_id")
    private String mId;

    @SerializedName("action_params")
    private ActionParams mActionParams;

    public Body(String action, String id, ActionParams actionParams) {
        mAction = action;
        mId = id;
        mActionParams = actionParams;
    }

    public String getAction() {
        return mAction;
    }

    public void setAction(String action) {
        mAction = action;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public ActionParams getActionParams() {
        return mActionParams;
    }

    public void setActionParams(ActionParams actionParams) {
        mActionParams = actionParams;
    }
}
