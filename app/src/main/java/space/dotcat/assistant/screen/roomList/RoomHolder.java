package space.dotcat.assistant.screen.roomList;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import space.dotcat.assistant.R;
import space.dotcat.assistant.content.Room;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoomHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_roomName)
    TextView mRoomName;

    @BindView(R.id.iv_roomImage)
    ImageView mRoomPicture;

    public RoomHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void bind(@NonNull Room room) {
        mRoomName.setText(room.getFriendlyName());

        Picasso.with(mRoomPicture.getContext())
                .load(room.getImagePath())
                .noFade()
                .fit()
                .into(mRoomPicture);
    }
}
