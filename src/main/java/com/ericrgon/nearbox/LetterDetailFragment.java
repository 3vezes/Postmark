package com.ericrgon.nearbox;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ericrgon.nearbox.model.Letter;
import com.ericrgon.nearbox.model.Page;
import com.ericrgon.nearbox.model.Stack;
import com.ericrgon.nearbox.rest.OutboxMailService;
import com.squareup.picasso.Picasso;

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
        View rootView = inflater.inflate(R.layout.fragment_letter_detail, container, false);

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

        LinearLayout content = (LinearLayout) rootView.findViewById(R.id.letter_detail);

        for(Page page : letter.getPages()){
            ImageView pageView = new ImageView(getActivity());

            String url = page.getImages().getImages().get(100);

            Picasso picasso = Picasso.with(getActivity());
            picasso.setDebugging(true);
            picasso.load(url).into(pageView);

            content.addView(pageView);
        }

        return rootView;
    }
}
