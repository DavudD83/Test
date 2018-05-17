package space.dotcat.assistant.screen.roomDetail;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import space.dotcat.assistant.R;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.screen.general.BaseRecyclerViewAdapter;

import java.util.List;

public class RoomDetailsAdapter extends BaseRecyclerViewAdapter<Thing, RoomDetailsHolder> {

    private final View.OnClickListener mOnClickListener = view -> {
        int position = (int) view.getTag();

        Thing thing = mItems.get(position);

        mOnItemClickListener.onItemClick(thing);
    };

    public RoomDetailsAdapter(List<Thing> items, OnItemClickListener<Thing> OnItemClickListener) {
        super(items, OnItemClickListener);
    }

    @NonNull
    @Override
    public RoomDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View item = layoutInflater.inflate(R.layout.item_room_detail, parent, false);

        final RoomDetailsHolder roomDetailsHolder = new RoomDetailsHolder(item);

        roomDetailsHolder.mSwitch.setOnClickListener(mOnClickListener);

        return roomDetailsHolder;
    }

    @Override
    public void updateData(List<Thing> newItems) {
        mItems.clear();
        mItems.addAll(newItems);

        notifyDataSetChanged();
    }

    @Override
    protected DiffUtil.Callback createDiffUtilCallback(List<Thing> oldList, List<Thing> newList) {
        return new ThingsDiffUtilCallback(oldList, newList);
    }
}
