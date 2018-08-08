package space.dotcat.assistant.screen.roomList;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import space.dotcat.assistant.R;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.screen.general.BaseRecyclerViewAdapter;

import java.util.List;

public class RoomsAdapter extends BaseRecyclerViewAdapter<Room, RoomHolder> {

    private final View.OnClickListener mRoomListener = view -> {
        int position = (int) view.getTag();

        Room room = mItems.get(position);

        mOnItemClickListener.onItemClick(room);
    };

    public RoomsAdapter(List<Room> items, OnItemClickListener<Room> OnItemClickListener) {
        super(items, OnItemClickListener);
    }

    @Override
    public RoomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View itemView = layoutInflater.inflate(R.layout.item_room_cards, parent, false);

        final RoomHolder roomHolder = new RoomHolder(itemView);

        roomHolder.itemView.setOnClickListener(mRoomListener);

        return roomHolder;
    }

    @Override
    protected DiffUtil.Callback createDiffUtilCallback(List<Room> oldList, List<Room> newList) {
        return new RoomsDIffUtilCallback(oldList, newList);
    }
}
