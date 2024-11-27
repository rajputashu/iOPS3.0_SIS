package com.droidcommons.base.recycler;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseRecyclerAdapter<I> extends RecyclerView.Adapter {

    private List<I> items;

    public BaseRecyclerAdapter() {
        items = new ArrayList<>();
    }


    protected ViewDataBinding getViewDataBinding(@LayoutRes int layoutRes, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return DataBindingUtil.inflate(inflater, layoutRes, parent, false);
    }


    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void clearAndSetItems(List<I> items) {
        if (items == null) {
//            throw new IllegalArgumentException("Cannot set `null` item to the Recycler adapter");
//            Timber.e("Cannot set `null` item to the Recycler adapter");
            items = new ArrayList<>();
        }
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public List<I> getItems() {
        return items;
    }

    public I getItem(int position) {
        return items.get(position);
    }

    public void add(I item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null item to the Recycler adapter");
        }
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void addWithRange(I item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null item to the Recycler adapter");
        }
        items.add(item);
        notifyItemRangeChanged(items.size() - 2, getItemCount());
    }

    public void setItemsForPagination(List<I> items) {
        if (items == null) {
            throw new IllegalArgumentException("Cannot add `null` items to the Recycler adapter");
        }
        this.items.addAll(items);
        notifyItemRangeInserted(this.items.size() - items.size(), items.size());
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void remove(I item) {
        int position = items.indexOf(item);
        if (position > -1) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }


    public boolean isEmpty() {
        return getItemCount() == 0;
    }

}