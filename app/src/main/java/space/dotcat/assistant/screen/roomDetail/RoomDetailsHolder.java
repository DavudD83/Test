package space.dotcat.assistant.screen.roomDetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Switch;

import space.dotcat.assistant.R;
import space.dotcat.assistant.content.Thing;

import butterknife.BindView;
import butterknife.ButterKnife;
import space.dotcat.assistant.screen.general.BaseViewHolder;

public class RoomDetailsHolder extends BaseViewHolder<Thing> {

    @BindView(R.id.switchThing)
    Switch mSwitch;

    public RoomDetailsHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void bind(Thing item) {
        mSwitch.setTag(getAdapterPosition());

        mSwitch.setText(item.getId());
        mSwitch.setEnabled(item.getIsAvailable());
        mSwitch.setChecked(item.getIsActive());
    }
}
