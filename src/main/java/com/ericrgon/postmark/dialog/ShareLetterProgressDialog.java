package com.ericrgon.postmark.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.ericrgon.postmark.BaseFragmentActivity;
import com.ericrgon.postmark.R;
import com.google.common.eventbus.EventBus;

public class ShareLetterProgressDialog extends RetainedDialogFragment {

    public static class DismissedEvent {}

    private EventBus bus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus = ((BaseFragmentActivity) getActivity()).getEventBus();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(R.string.share);
        progressDialog.setMessage(getString(R.string.downloading));
        progressDialog.setIndeterminate(true);
        return progressDialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        bus.post(new DismissedEvent());
    }
}
