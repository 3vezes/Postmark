package com.ericrgon.nearbox;

import android.app.Activity;
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
import com.ericrgon.nearbox.model.Letter;
import com.ericrgon.nearbox.model.Stack;
import com.ericrgon.nearbox.model.Todo;
import com.ericrgon.nearbox.rest.OutboxMailService;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A fragment representing a single Letter detail screen.
 * This fragment is either contained in a {@link LetterListActivity}
 * in two-pane mode (on tablets) or a {@link LetterDetailActivity}
 * on handsets.
 */
public class LetterDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String LETTER_ID = "letter";

    private Letter letter;

    private OutboxMailService mailService;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LetterDetailFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mailService = ((BaseFragmentActivity) activity).getMailService();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(LETTER_ID)) {
            letter = (Letter) getArguments().getSerializable(LETTER_ID);
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
                        Log.d("LETTER", "Shred Success");
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Log.d("LETTER", "Shred Failure");
                    }
                });
            }
        });


        View move = rootView.findViewById(R.id.move);
        move.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Get available stacks
                mailService.getStacks(new Callback<List<Stack>>() {
                    @Override
                    public void success(List<Stack> stacks, Response response) {
                        Log.d("LETTER","Letter Get Stack: " + stacks.toString());
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Log.d("LETTER","Letter Stacks failed");
                    }
                });

                mailService.moveLetterToStack("Coupons",letter.getIdentifier(), System.currentTimeMillis(),new Callback<Void>() {
                    @Override
                    public void success(Void aVoid, Response response) {
                        Log.d("LETTER","Move Success");
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Log.d("LETTER","Move Failed");
                    }
                });
            }
        });

        View todo = rootView.findViewById(R.id.todo);
        todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailService.todo(letter.getIdentifier(), System.currentTimeMillis(), Todo.EMPTY, new Callback<Letter>() {
                    @Override
                    public void success(Letter letter, Response response) {

                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {

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

                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {

                    }
                });
            }
        });

        final View actions = rootView.findViewById(R.id.actions);

        final ListView pagesList = (ListView) rootView.findViewById(R.id.pages_list);
        pagesList.setAdapter(new PagesAdapter(getActivity(), letter.getPages()));
        pagesList.setOnScrollListener(new AbsListView.OnScrollListener() {

            private final int maxY = actions.getHeight();

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int scrollY = getScrollY(pagesList);
                actions.setTranslationY(Math.max(scrollY,maxY));
            }
        });

        return rootView;
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

        int returnValue = -top + firstVisiblePosition * c.getHeight() + headerHeight;

        Log.d("SCROLL", String.valueOf(returnValue));

        return returnValue;
    }
}
