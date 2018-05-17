package space.dotcat.assistant.screen.roomList;

import java.util.List;

import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.screen.general.BaseDiffUtilCallback;

public class RoomsDIffUtilCallback extends BaseDiffUtilCallback<Room> {

    public RoomsDIffUtilCallback(List<Room> oldList, List<Room> newList) {
        super(oldList, newList);
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Room oldRoom = mOldList.get(oldItemPosition);

        Room newRoom = mNewList.get(newItemPosition);

        return oldRoom.getId().equals(newRoom.getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Room oldRoom = mOldList.get(oldItemPosition);

        Room newRoom = mNewList.get(newItemPosition);

        return oldRoom.getFriendlyName().equals(newRoom.getFriendlyName()) &&
                oldRoom.getImagePath().equals(newRoom.getImagePath());
    }
}
