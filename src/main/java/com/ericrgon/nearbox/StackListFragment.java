package com.ericrgon.nearbox;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ericrgon.nearbox.adapter.StackAdapter;
import com.ericrgon.nearbox.model.Stack;
import com.ericrgon.nearbox.rest.OutboxMailService;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class StackListFragment extends ListFragment {

    private OutboxMailService mailService;

    public StackListFragment() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mailService = ((BaseFragmentActivity) activity).getMailService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mailService.getStacks(new Callback<List<Stack>>() {
            @Override
            public void success(List<Stack> stacks, Response response) {
                setListAdapter(new StackAdapter(getActivity(),stacks));
            }

            @Override
            public void failure(RetrofitError retrofitError) {}
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
