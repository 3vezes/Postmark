package com.ericrgon.nearbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;

import com.ericrgon.nearbox.adapter.MailAdapter;
import com.ericrgon.nearbox.model.Letter;
import com.ericrgon.nearbox.rest.OutboxMailService;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * An activity representing a list of Letters. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link LetterDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link LetterListFragment} and the item details
 * (if present) is a {@link LetterDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link LetterListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class LetterListActivity extends FragmentActivity
        implements LetterListFragment.Callbacks {

    public static final String SESSION = "session";
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private ListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_list);

        if (findViewById(R.id.letter_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((LetterListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.letter_list))
                    .setActivateOnItemClick(true);
        }

        listFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.letter_list);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer(OutboxMailService.URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();


        OutboxMailService mailService = restAdapter.create(OutboxMailService.class);
        mailService.getMail(OutboxMailService.Status.UNSORTED,getIntent().getExtras().getString(SESSION),new Callback<List<Letter>>() {
            @Override
            public void success(List<Letter> letters, Response response) {
                listFragment.setListAdapter(new MailAdapter(LetterListActivity.this,letters));
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });



    }

    /**
     * Callback method from {@link LetterListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(Letter id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putSerializable(LetterDetailFragment.LETTER_ID, id);
            LetterDetailFragment fragment = new LetterDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.letter_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, LetterDetailActivity.class);
            detailIntent.putExtra(LetterDetailFragment.LETTER_ID, id);
            startActivity(detailIntent);
        }
    }
}
