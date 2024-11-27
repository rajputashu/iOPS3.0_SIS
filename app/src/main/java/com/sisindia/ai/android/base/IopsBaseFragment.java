package com.sisindia.ai.android.base;

import android.os.Message;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.droidcommons.base.BaseActivity;
import com.droidcommons.base.BaseFragment;
import com.droidcommons.base.SingleLiveEvent;

import javax.inject.Inject;

public abstract class IopsBaseFragment extends BaseFragment {


    @Inject
    protected ViewModelProvider.Factory viewModelFactory;

    @Inject
    protected SingleLiveEvent<Message> liveData;


    protected AndroidViewModel getAndroidViewModel(Class type) {
        return (AndroidViewModel) new ViewModelProvider(this, viewModelFactory).get(type);
    }

    protected AndroidViewModel getParentFragmentViewModel(BaseFragment parent, Class type) {
        return (AndroidViewModel) new ViewModelProvider(parent, viewModelFactory).get(type);
    }

    protected AndroidViewModel getActivityViewModel(BaseActivity activity, Class type) {
        return (AndroidViewModel) new ViewModelProvider(activity, viewModelFactory).get(type);
    }

}
