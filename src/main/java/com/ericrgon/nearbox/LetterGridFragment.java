package com.ericrgon.nearbox;

import android.app.Activity;
import android.content.Context;
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

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;


    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(Letter id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(Letter id) {
        }
    };

    private OutboxMailService mailService;
    private Context context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mailService = ((BaseFragmentActivity) activity).getMailService();
        context = activity;

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_letter_grid,container,false);

        final GridView gridView = (GridView) rootView.findViewById(R.id.letterGrid);

        mailService.getMail(OutboxMailService.Status.UNSORTED,new Callback<List<Letter>>() {
            @Override
            public void success(List<Letter> letters, Response response) {
                gridView.setAdapter(new MailAdapter(context,letters));
            }

            @Override
            public void failure(RetrofitError retrofitError) {}
        });

        return rootView;
    }
}
