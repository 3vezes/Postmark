package com.ericrgon.postmark.dialog;

import android.support.v4.app.DialogFragment;

public class RetainedDialogFragment extends DialogFragment {

    /**
     * Required for bug in the support lib.
     */
    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

}
