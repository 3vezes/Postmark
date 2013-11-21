package com.ericrgon.nearbox;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.ericrgon.nearbox.adapter.MailAdapter;
import com.ericrgon.nearbox.model.Letter;
import com.ericrgon.nearbox.rest.OutboxMailService;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LetterGridFragment extends Fragment{

    private OutboxMailService mailService;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mailService = ((BaseFragmentActivity) activity).getMailService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_letter_grid,container,false);

        final GridView gridView = (GridView) rootView.findViewById(R.id.letterGrid);

        mailService.getMail(OutboxMailService.Status.UNSORTED,new Callback<List<Letter>>() {
            @Override
            public void success(List<Letter> letters, Response response) {
                gridView.setAdapter(new MailAdapter(getActivity(),letters));
            }

            @Override
            public void failure(RetrofitError retrofitError) {}
        });

        return rootView;
    }
}
