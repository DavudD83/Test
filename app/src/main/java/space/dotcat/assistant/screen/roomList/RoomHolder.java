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
import space.dotcat.assistant.screen.general.BaseViewHolder;

public class RoomHolder extends BaseViewHolder<Room> {

    @BindView(R.id.tv_roomName)
    TextView mRoomName;

    @BindView(R.id.iv_roomImage)
    ImageView mRoomPicture;

    public RoomHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    protected void bind(Room item) {
        itemView.setTag(getAdapterPosition());

        mRoomName.setText(item.getFriendlyName());

        Picasso.with(mRoomPicture.getContext())
                .load(item.getImagePath())
                .noFade()
                .fit()
                .into(mRoomPicture);
    }
}
