package com.ericrgon.nearbox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ericrgon.nearbox.model.Letter;
import com.ericrgon.nearbox.model.Page;
import com.squareup.picasso.Picasso;

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

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LetterDetailFragment() {
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
