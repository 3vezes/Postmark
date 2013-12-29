package com.ericrgon.postmark;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ericrgon.postmark.adapter.StackAdapter;
import com.ericrgon.postmark.model.Stack;
import com.ericrgon.postmark.rest.Callback;
import com.ericrgon.postmark.rest.OutboxMailService;

import java.util.List;

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
                super.success(stacks,response);
                setListAdapter(new StackAdapter(getActivity(),stacks));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                super.failure(retrofitError);
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
