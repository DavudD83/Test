package space.dotcat.assistant.screen.roomDetail.thingsHolder;

import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.BindView;
import space.dotcat.assistant.R;
import space.dotcat.assistant.content.DoorLock;
import space.dotcat.assistant.screen.general.BaseViewHolder;
import space.dotcat.assistant.screen.roomDetail.RoomDetailsAdapter;

public class DoorLockHolder extends BaseViewHolder<DoorLock> {

    @BindView(R.id.tv_door_lock_label)
    public TextView mDoorLockFriendlyName;

    @BindView(R.id.sw_door_lock_state)
    public Switch mDoorLockState;

    public DoorLockHolder(View itemView, RoomDetailsAdapter.DoorSwitchClickListener doorSwitchClickListener) {
        super(itemView);

        mDoorLockState.setOnClickListener(doorSwitchClickListener);
    }

    @Override
    protected void bind(DoorLock item) {
        mDoorLockState.setTag(getAdapterPosition());

        mDoorLockFriendlyName.setText(item.getFriendlyName());

        mDoorLockState.setChecked(item.getIsActive());
    }
}
