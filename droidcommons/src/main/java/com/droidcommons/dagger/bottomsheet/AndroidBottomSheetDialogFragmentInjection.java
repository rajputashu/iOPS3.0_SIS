package com.droidcommons.dagger.bottomsheet;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import dagger.android.AndroidInjector;

import static dagger.internal.Preconditions.checkNotNull;

public class AndroidBottomSheetDialogFragmentInjection {
    public static void inject(BottomSheetDialogFragment fragment) {
        checkNotNull(fragment, "BottomSheetDialogFragment");
        Object application = fragment.getActivity();
        if (!(application instanceof HasBottomSheetDialogFragmentInjector)) {
            throw new RuntimeException(
                    String.format(
                            "%s does not implement %s",
                            application.getClass().getCanonicalName(),
                            HasBottomSheetDialogFragmentInjector.class.getCanonicalName()));
        }

        AndroidInjector<BottomSheetDialogFragment> bottomSheetDialogFragmentAndroidInjector =
                ((HasBottomSheetDialogFragmentInjector) application).bottomSheetDialogFragmentAndroidInjector();
        checkNotNull(bottomSheetDialogFragmentAndroidInjector, "%s.bottomSheetDialogFragmentAndroidInjector() returned null", application.getClass());
        bottomSheetDialogFragmentAndroidInjector.inject(fragment);
    }
}
