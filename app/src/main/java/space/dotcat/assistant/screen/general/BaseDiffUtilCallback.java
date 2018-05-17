package space.dotcat.assistant.screen.general;

import android.support.v7.util.DiffUtil;

import java.util.List;

public abstract class BaseDiffUtilCallback<T> extends DiffUtil.Callback {

    protected List<T> mOldList;

    protected List<T> mNewList;

    public BaseDiffUtilCallback(List<T> oldList, List<T> newList) {
        mOldList = oldList;

        mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }
}
