package com.sisindia.ai.android.commons.audiorecord;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.databinding.BottomSheetRecordAudioBinding;

public class AudioRecordingBottomSheetFragment extends IopsBaseBottomSheetDialogFragment {

    private BottomSheetRecordAudioBinding binding;
    private AudioRecordingViewModel viewModel;


    public static AudioRecordingBottomSheetFragment newInstance() {
        return new AudioRecordingBottomSheetFragment();
    }

    @Override
    protected void extractBundle() {

    }

    @Override
    protected void applyStyle() {
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    @Override
    protected void initViewModel() {
        viewModel = (AudioRecordingViewModel) getAndroidViewModel(AudioRecordingViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        binding = (BottomSheetRecordAudioBinding) bindFragmentView(getLayoutResource(), inflater, container);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            switch (message.what) {
                case NavigationConstants.ON_AUDIO_RECORDED_FOR_GRIEVANCE:
                    dismissAllowingStateLoss();
                    break;
            }
        });
    }

    @Override
    protected void onCreated() {
        binding.ivClose.setOnClickListener(v -> this.dismissAllowingStateLoss());
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.bottom_sheet_record_audio;
    }
}
