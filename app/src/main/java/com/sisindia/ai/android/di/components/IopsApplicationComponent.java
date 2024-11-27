package com.sisindia.ai.android.di.components;

import com.sisindia.ai.android.IopsApplication;
import com.sisindia.ai.android.di.modules.ActivityBindingModule;
import com.sisindia.ai.android.di.modules.ApplicationModule;
import com.sisindia.ai.android.di.modules.BaseModule;
import com.sisindia.ai.android.di.modules.BottomSheetDialogFragmentModule;
import com.sisindia.ai.android.di.modules.BroadcastReceiverBindingModule;
import com.sisindia.ai.android.di.modules.DialogFragmentModule;
import com.sisindia.ai.android.di.modules.FragmentBindingModule;
import com.sisindia.ai.android.di.modules.IopsDatabaseModule;
import com.sisindia.ai.android.di.modules.IopsUiModule;
import com.sisindia.ai.android.di.modules.IopsViewPagerModule;
import com.sisindia.ai.android.di.modules.NotificationHandlerModule;
import com.sisindia.ai.android.di.modules.RetrofitModule;
import com.sisindia.ai.android.di.modules.ServiceBindingModule;
import com.sisindia.ai.android.di.modules.WorkerModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ActivityBindingModule.class,
        FragmentBindingModule.class,
        ServiceBindingModule.class,
        ApplicationModule.class,
        BaseModule.class,
        IopsDatabaseModule.class,
        RetrofitModule.class,
        BottomSheetDialogFragmentModule.class,
        BroadcastReceiverBindingModule.class,
        WorkerModule.class,
        IopsUiModule.class,
        DialogFragmentModule.class,
        IopsViewPagerModule.class,
        NotificationHandlerModule.class,
})
public interface IopsApplicationComponent extends AndroidInjector<IopsApplication> {

    void inject(IopsApplication iopsApplication);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(IopsApplication application);

        IopsApplicationComponent build();
    }
}
