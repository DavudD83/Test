package space.dotcat.assistant.screen.roomDetail.thingsHolder;

import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.BindView;
import space.dotcat.assistant.R;
import space.dotcat.assistant.content.ContactSensor;
import space.dotcat.assistant.screen.general.BaseViewHolder;

public class ContactSensorsHolder extends BaseViewHolder<ContactSensor> {

    @BindView(R.id.tv_contact_sensor_label)
    public TextView mContactSensorName;

    @BindView(R.id.sw_contact_sensor_state)
    public Switch mSwitch;

    public ContactSensorsHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void bind(ContactSensor item) {
        mContactSensorName.setText(item.getFriendlyName());

        mSwitch.setChecked(item.getIsActive());

        mSwitch.setEnabled(false);
    }
}
