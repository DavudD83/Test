package space.dotcat.assistant.content;


import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("command")
    private String mCommand;

    @SerializedName("command_args")
    private JsonElement mCommandArgs;

    public Message() {
    }

    public Message(String command, JsonElement commandArgs) {
        mCommand = command;

        mCommandArgs = commandArgs;
    }

    public String getCommand() {
        return mCommand;
    }

    public void setCommand(String command) {
        mCommand = command;
    }

    public JsonElement getCommandArgs() {
        return mCommandArgs;
    }

    public void setCommandArgs(JsonElement commandArgs) {
        mCommandArgs = commandArgs;
    }
}
