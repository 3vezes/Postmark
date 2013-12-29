package com.ericrgon.nearbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ericrgon.nearbox.adapter.PagesAdapter;
import com.ericrgon.nearbox.dialog.StackDialog;
import com.ericrgon.nearbox.model.Letter;
import com.ericrgon.nearbox.model.Todo;
import com.ericrgon.nearbox.rest.Callback;
import com.ericrgon.nearbox.rest.OutboxMailService;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class LetterDetailFragment extends Fragment {

    public static final String LETTER_ID = "letter";

    private Letter letter;

    private OutboxMailService mailService;

    public LetterDetailFragment() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mailService = ((BaseFragmentActivity) activity).getMailService();
        EventBus eventBus = ((BaseFragmentActivity) activity).getEventBus();
        eventBus.register(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(LETTER_ID)) {
            letter = (Letter) getArguments().getSerializable(LETTER_ID);
            getActivity().getActionBar().setTitle(letter.getDeliveredDate());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout rootView = (RelativeLayout) inflater.inflate(R.layout.fragment_letter_detail, container, false);

        View shred = rootView.findViewById(R.id.shred);
        shred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailService.shred(letter.getIdentifier(),new Callback<Letter>() {
                    @Override
                    public void success(Letter letter, Response response) {
                        super.success(letter,response);
                        Log.d("LETTER", "Shred Success");
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        super.failure(retrofitError);
                        Log.d("LETTER", "Shred Failure");
                    }
                });
            }
        });


        View move = rootView.findViewById(R.id.move);
        move.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StackDialog stackDialog = new StackDialog();
                stackDialog.setRetainInstance(true);
                stackDialog.show(getFragmentManager(),"stack");
            }
        });

        View todo = rootView.findViewById(R.id.todo);
        todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailService.todo(letter.getIdentifier(), System.currentTimeMillis(), Todo.EMPTY, new Callback<Letter>() {
                    @Override
                    public void success(Letter letter, Response response) {
                        super.success(letter,response);
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        super.failure(retrofitError);
                    }
                });
            }
        });

        View request = rootView.findViewById(R.id.request);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailService.request(letter.getIdentifier(),System.currentTimeMillis(),new Callback<Letter>() {
                    @Override
                    public void success(Letter letter, Response response) {
                        super.success(letter,response);
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        super.failure(retrofitError);
                    }
                });
            }
        });

        //Hide the request button if the letter has be archived
        if(letter.isArchived()){
            request.setVisibility(View.GONE);
        }

        final View actions = rootView.findViewById(R.id.actions);

        final ListView pagesList = (ListView) rootView.findViewById(R.id.pages_list);
        pagesList.setAdapter(new PagesAdapter(getActivity(), letter.getPages()));
        pagesList.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int previous = 0;
            private int current = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int scrollY = getScrollY(pagesList);
                int height = actions.getHeight();
                int diff = scrollY - previous;
                current = clamp(current + diff,0,height);
                actions.setTranslationY(current);
                previous = scrollY;
            }
        });

        return rootView;
    }

    @Subscribe
    public void onPageSelected(PagesAdapter.PageSelectedEvent pageSelectedEvent){
        Intent pageZoomIntent = new Intent(getActivity(),PageZoomActivity.class);
        pageZoomIntent.putExtra(PageZoomActivity.PAGE,pageSelectedEvent.getPage());
        startActivity(pageZoomIntent);
    }

    public int getScrollY(ListView listView) {
        View c = listView.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = listView.getHeight();
        }

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(Math.min(value, max), min);
    }

}
