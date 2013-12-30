package com.ericrgon.postmark;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.ericrgon.postmark.adapter.IndexAdapter;
import com.ericrgon.postmark.model.Letter;
import com.ericrgon.postmark.model.Stack;
import com.ericrgon.postmark.rest.Callback;
import com.ericrgon.postmark.rest.OutboxMailService;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class LetterGridFragment extends Fragment{

    public static final String FOLDER_ITEM = "folder";

    public static final String STATUS_ITEM = "status";

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

    private OutboxMailService mailService;
    private Context context;

    private ProgressBar progressBar;
    private GridView gridView;
    private View emptyState;
    private View connectionIssueState;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mailService = ((BaseFragmentActivity) activity).getMailService();
        context = activity;

        //Receive events for refresh.
        setHasOptionsMenu(true);


        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_refresh:
                refresh(true);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_letter_grid, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
        gridView = (GridView) rootView.findViewById(R.id.letterGrid);
        emptyState = rootView.findViewById(android.R.id.empty);
        connectionIssueState = rootView.findViewById(R.id.connectionIssue);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh(false);
    }

    /**
     * @param isForced True will clear the content and display the progress dialog.
     */
    private void refresh(boolean isForced){
        //Show progress bar if this is the first time loading.
        if(gridView.getCount() == 0 || isForced){
            progressBar.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
            connectionIssueState.setVisibility(View.GONE);
            emptyState.setVisibility(View.GONE);
        }

        Bundle arguments = getArguments();

        if(arguments.containsKey(STATUS_ITEM)){
            OutboxMailService.Status status = (OutboxMailService.Status) arguments.getSerializable(STATUS_ITEM);
            mailService.getMail(status,new Callback<List<Letter>>() {
                @Override
                public void success(List<Letter> letters, Response response) {
                    super.success(letters,response);
                    gridView.setAdapter(new IndexAdapter(context,letters));
                    setContentVisible(gridView,progressBar,emptyState,letters.isEmpty());
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    super.failure(retrofitError);
                    setContentVisible(gridView,progressBar,connectionIssueState,true);
                }
            });

        } else if(arguments.containsKey(FOLDER_ITEM)){
            final Stack stack = (Stack) arguments.getSerializable(FOLDER_ITEM);
            mailService.getStack(stack.getLabel(),new Callback<List<Letter>>() {
                @Override
                public void success(List<Letter> letters, Response response) {
                    super.success(letters,response);
                    gridView.setAdapter(new IndexAdapter(context,letters));
                    setContentVisible(gridView,progressBar,emptyState,letters.isEmpty());
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    super.failure(retrofitError);
                    setContentVisible(gridView,progressBar,connectionIssueState,true);
                }
            });
        }
    }

    private void setContentVisible(View content, View progress,View emptyView ,boolean isEmpty){
        if(isEmpty){
            emptyView.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            content.setVisibility(View.GONE);
        } else {
            progress.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
        }
    }
}
