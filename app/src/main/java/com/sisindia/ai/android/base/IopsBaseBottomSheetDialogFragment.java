package com.sisindia.ai.android.base;

import android.content.Context;
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
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.droidcommons.base.SingleLiveEvent;
import com.droidcommons.dagger.bottomsheet.AndroidBottomSheetDialogFragmentInjection;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import javax.inject.Inject;

public abstract class IopsBaseBottomSheetDialogFragment extends BottomSheetDialogFragment {


    @Inject
    protected ViewModelProvider.Factory viewModelFactory;

    @Inject
    protected SingleLiveEvent<Message> liveData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applyStyle();

        initViewModel();

        extractBundle();

    }


    @Override
    public void onAttach(@NonNull Context context) {
        AndroidBottomSheetDialogFragmentInjection.inject(this);
        super.onAttach(context);
    }

    protected abstract void extractBundle();

    protected abstract void applyStyle();

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

    protected abstract void onCreated();

    protected abstract @LayoutRes
    int getLayoutResource();

    protected AndroidViewModel getAndroidViewModel(Class type) {
        return (AndroidViewModel) new ViewModelProvider(this, viewModelFactory).get(type);
    }

    protected ViewDataBinding bindFragmentView(@LayoutRes int layoutResource, LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, layoutResource, container, false);
    }
}
