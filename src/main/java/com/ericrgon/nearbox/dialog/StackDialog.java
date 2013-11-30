package com.ericrgon.nearbox.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ericrgon.nearbox.R;
import com.ericrgon.nearbox.StackListFragment;

public class StackDialog extends DialogFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(getString(R.string.select_stack));
        View rootView = inflater.inflate(R.layout.fragment_stack_list_dialog,container,false);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.stackList,new StackListFragment())
                .commit();
        return rootView;
    }
}
