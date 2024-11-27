package com.droidcommons.dagger.bottomsheet;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import dagger.android.AndroidInjector;

public interface HasBottomSheetDialogFragmentInjector {
    AndroidInjector<BottomSheetDialogFragment> bottomSheetDialogFragmentAndroidInjector();
}
