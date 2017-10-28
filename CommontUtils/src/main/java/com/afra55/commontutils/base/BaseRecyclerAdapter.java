package com.afra55.commontutils.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Afra55 on 2017/10/14.
 * Smile is the best name card.
 */

public abstract class BaseRecyclerAdapter<T extends BaseBean> extends RecyclerView.Adapter<BaseViewHolder> {

    protected Context context;

    protected List<T> dataList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public BaseRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void init(@NonNull List<T> data) {
        this.dataList.clear();
        this.dataList.addAll(data);
    }

    public void clear() {
        this.dataList.clear();
    }

    public void addAll(@NonNull List<T> data) {
        this.dataList.addAll(data);
    }

    public void add(@NonNull T data) {
        this.dataList.add(data);
    }

    public void remove(int i) {
        this.dataList.remove(i);
    }

    public void remove(@NonNull T t) {
        if (this.dataList.contains(t)) {
            dataList.remove(t);
        }
    }

    public boolean contains(@NonNull T data) {
        if (dataList.isEmpty()) {
            return false;
        } else if (dataList.contains(data)) {
            return true;
        }
        return false;
    }

    public List<T> getData() {
        return dataList;
    }

    public T getItem(int index) {
        if (!dataList.isEmpty() && index >= 0 && index < dataList.size()) {
            return dataList.get(index);
        } else {
            return null;
        }
    }

    public void replaceItemByPos(int pos, T item) {
        if (pos < 0 || pos >= dataList.size() || item == null) {
            return;
        }
        dataList.remove(pos);
        dataList.add(pos, item);
    }

    public void replaceItemList(int start, List<T> list) {
        if (start >= dataList.size() || list == null || list.isEmpty()) {
            return;
        }
        for (int i = start; i < start + list.size(); i++) {

            if (start < dataList.size()) {
                dataList.remove(start);
            } else {
                break;
            }
        }

        if (start >= dataList.size()) {
            dataList.addAll(list);
        } else {
            dataList.addAll(start, list);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getViewType();
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateViewHolder(context, parent, viewType);
    }

    protected abstract BaseViewHolder onCreateViewHolder(Context context, ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        if (holder.getTag() != null) {
            BaseViewSugar viewSugar = holder.getTag();
            viewSugar.bind(dataList.get(position));
            View itemView = holder.getItemView();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClicked(
                                holder.getAdapterPosition()
                                , dataList.get(holder.getAdapterPosition()));
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onItemClickListener != null
                            && onItemClickListener.onItemLongClicked(holder.getAdapterPosition()
                            , dataList.get(holder.getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
