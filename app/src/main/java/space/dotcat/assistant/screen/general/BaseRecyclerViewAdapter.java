package space.dotcat.assistant.screen.general;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T, VH extends BaseViewHolder<T>> extends RecyclerView.Adapter<VH> {

    protected List<T> mItems = new ArrayList<>();

    protected OnItemClickListener<T> mOnItemClickListener;

    public BaseRecyclerViewAdapter(List<T> items, OnItemClickListener<T> OnItemClickListener) {
        mItems.addAll(items);

        mOnItemClickListener = OnItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        T item = mItems.get(position);

        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateData(List<T> newItems) {
        DiffUtil.Callback callback = createDiffUtilCallback(mItems, newItems);

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);

        mItems.clear();
        mItems.addAll(newItems);

        diffResult.dispatchUpdatesTo(this);
    }

    protected abstract DiffUtil.Callback createDiffUtilCallback(List<T> oldList, List<T> newList);
    
    public interface OnItemClickListener<T> {

        void onItemClick(T item);
    }
}
