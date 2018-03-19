package space.dotcat.assistant.content;


import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("command")
    private String mCommand;

    @SerializedName("command_args")
    private CommandArgs mCommandArgs;

    public Message(String command, CommandArgs commandArgs) {
        mCommand = command;
        mCommandArgs = commandArgs;
    }

    public String getCommand() {
        return mCommand;
    }

    public void setCommand(String command) {
        mCommand = command;
    }

    public CommandArgs getCommandArgs() {
        return mCommandArgs;
    }

    public void setCommandArgs(CommandArgs commandArgs) {
        mCommandArgs = commandArgs;
    }
}
