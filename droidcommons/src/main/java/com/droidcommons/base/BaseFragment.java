package com.droidcommons.base;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import dagger.android.support.DaggerFragment;

public abstract class BaseFragment extends DaggerFragment {


    protected Message vMessage = new Message();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        initViewModel();

        extractBundle();

    }

    protected abstract void extractBundle();

    protected abstract void initViewModel();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = initViewBinding(inflater, container);
        initViewState();

        return rootView;
    }

    protected abstract View initViewBinding(LayoutInflater inflater, ViewGroup container);

    protected abstract void initViewState();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onCreated();
    }

    protected ViewDataBinding bindFragmentView(@LayoutRes int layoutResource, LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, layoutResource, container, false);
    }

    protected abstract void onCreated();

    protected abstract @LayoutRes
    int getLayoutResource();

}
