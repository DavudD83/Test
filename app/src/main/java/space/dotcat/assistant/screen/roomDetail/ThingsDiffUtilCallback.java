package space.dotcat.assistant.screen.roomDetail;

import java.util.List;

import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.screen.general.BaseDiffUtilCallback;

public class ThingsDiffUtilCallback extends BaseDiffUtilCallback<Thing> {

    public ThingsDiffUtilCallback(List<Thing> oldList, List<Thing> newList) {
        super(oldList, newList);
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Thing oldThing = mOldList.get(oldItemPosition);

        Thing newThing = mNewList.get(newItemPosition);

        return oldThing.getId().equals(newThing.getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Thing oldThing = mOldList.get(oldItemPosition);

        Thing newThing = mNewList.get(newItemPosition);

        return oldThing.getIsActive().equals(newThing.getIsActive()) &&
                oldThing.getIsEnabled().equals(newThing.getIsEnabled()) &&
                oldThing.getFriendlyName().equals(newThing.getFriendlyName());
    }
}
