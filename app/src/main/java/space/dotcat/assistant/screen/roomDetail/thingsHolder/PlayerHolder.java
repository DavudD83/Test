package space.dotcat.assistant.screen.roomDetail.thingsHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import space.dotcat.assistant.R;
import space.dotcat.assistant.content.Player;
import space.dotcat.assistant.screen.general.BaseViewHolder;
import space.dotcat.assistant.screen.roomDetail.RoomDetailsAdapter;


public class PlayerHolder extends BaseViewHolder<Player> {

    @BindView(R.id.tv_player_friendly_name)
    TextView mPlayerName;

    @BindView(R.id.bt_play)
    Button mPlay;

    @BindView(R.id.bt_stop)
    Button mStop;

    private final String PLAYING_STATE = "playing";

    private final String STOPPED_STATE = "stopped";

    public static final String PLAYING_COMMAND = "play";

    public static final String STOPPED_COMMAND = "stop";

    public PlayerHolder(View itemView, RoomDetailsAdapter.PlayerStateListener playerStateListener) {
        super(itemView);

        mPlay.setOnClickListener(v-> {
            int position = (int) v.getTag();

            playerStateListener.onPlayerStateChange(position, PLAYING_STATE, PLAYING_COMMAND);
        });

        mStop.setOnClickListener(v-> {
            int position = (int) v.getTag();

            playerStateListener.onPlayerStateChange(position, STOPPED_STATE, STOPPED_COMMAND);
        });
    }

    @Override
    protected void bind(Player item) {
        mPlayerName.setText(item.getFriendlyName());

        mPlay.setTag(getAdapterPosition());
        mStop.setTag(getAdapterPosition());
    }
}
