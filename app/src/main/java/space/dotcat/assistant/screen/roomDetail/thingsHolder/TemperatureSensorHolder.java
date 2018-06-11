package space.dotcat.assistant.screen.roomDetail.thingsHolder;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import space.dotcat.assistant.R;
import space.dotcat.assistant.content.TemperatureSensor;
import space.dotcat.assistant.screen.general.BaseViewHolder;

public class TemperatureSensorHolder extends BaseViewHolder<TemperatureSensor> {

    @BindView(R.id.tv_temperature_name)
    public TextView mTemperatureSensorName;

    @BindView(R.id.tv_temperature_value)
    public TextView mTemperatureValue;

    public TemperatureSensorHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void bind(TemperatureSensor item) {
        mTemperatureSensorName.setText(item.getFriendlyName());

        String temperature = String.valueOf(item.getTemperatureC()) + " C";

        mTemperatureValue.setText(temperature);
    }
}
