package com.sisindia.ai.android.features.livecamera;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.databinding.FragmentImageRenderBinding;

public class LiveImageRenderFragment extends IopsBaseFragment {

    private final static String URI_PATH = "uri_path_data";

    private FragmentImageRenderBinding binding;
    private LiveImageCameraViewModel activityViewModel;


    public static LiveImageRenderFragment newInstance(Uri uri) {
        LiveImageRenderFragment fragment = new LiveImageRenderFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(URI_PATH, uri);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void extractBundle() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(URI_PATH)) {
            activityViewModel.setImageUri(bundle.getParcelable(URI_PATH));
        }
    }

    @Override
    protected void initViewModel() {


        if (getActivity() != null) {
            activityViewModel = new ViewModelProvider(getActivity(), viewModelFactory).get(LiveImageCameraViewModel.class);
        }

    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        binding.setVm(activityViewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {

    }

    @Override
    protected void onCreated() {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_image_render;
    }
}
