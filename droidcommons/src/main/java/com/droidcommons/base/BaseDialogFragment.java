package com.droidcommons.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import dagger.android.support.DaggerAppCompatDialogFragment;

public abstract class BaseDialogFragment extends DaggerAppCompatDialogFragment {

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
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }


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

    @Override
    public void onStart() {
        super.onStart();
        /*if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT);
        }*/
    }

    protected ViewDataBinding bindFragmentView(@LayoutRes int layoutResource, LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, layoutResource, container, false);
    }

    protected abstract void onCreated();

    protected abstract @LayoutRes
    int getLayoutResource();

}
