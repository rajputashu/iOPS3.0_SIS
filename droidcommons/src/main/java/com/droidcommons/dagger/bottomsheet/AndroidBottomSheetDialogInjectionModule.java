package com.droidcommons.dagger.bottomsheet;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Map;

import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.Multibinds;

@Module
public abstract class AndroidBottomSheetDialogInjectionModule {

    @Multibinds
    abstract Map<Class<? extends BottomSheetDialogFragment>, AndroidInjector.Factory<? extends BottomSheetDialogFragment>> bottomSheetDialogFragmentInjectorFactories();
}