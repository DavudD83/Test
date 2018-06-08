package space.dotcat.assistant.screen.roomDetail.thingsHolder;

import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.BindView;
import space.dotcat.assistant.R;
import space.dotcat.assistant.content.Lamp;
import space.dotcat.assistant.screen.general.BaseViewHolder;
import space.dotcat.assistant.screen.roomDetail.RoomDetailsAdapter;

public class LightHolder extends BaseViewHolder<Lamp> {

    @BindView(R.id.tv_light_label)
    public TextView mLightFriendlyName;

    @BindView(R.id.sw_light_state_switch)
    public Switch mLightStateSwitch;

    public LightHolder(View itemView, RoomDetailsAdapter.LampSwitchClickListener switchStateListener) {
        super(itemView);

        mLightStateSwitch.setOnClickListener(switchStateListener);
    }

    @Override
    protected void bind(Lamp item) {
        mLightStateSwitch.setTag(getAdapterPosition());

        mLightFriendlyName.setText(item.getFriendlyName());

        mLightStateSwitch.setChecked(item.getIsPoweredOn());
    }
}
