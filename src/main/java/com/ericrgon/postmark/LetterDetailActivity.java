package com.ericrgon.postmark;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.ericrgon.postmark.adapter.StackAdapter;
import com.ericrgon.postmark.dialog.StackDialog;
import com.ericrgon.postmark.model.Letter;
import com.ericrgon.postmark.rest.Callback;
import com.ericrgon.postmark.rest.OutboxMailService;
import com.google.common.eventbus.Subscribe;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * An activity representing a single Letter detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link LetterDetailFragment}.
 */
public class LetterDetailActivity extends BaseFragmentActivity {

    private OutboxMailService mailService;
    private Letter letter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_detail);

        mailService = getMailService();
        letter = (Letter) getIntent().getSerializableExtra(LetterDetailFragment.LETTER_ID);
        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putSerializable(LetterDetailFragment.LETTER_ID, letter);
            LetterDetailFragment fragment = new LetterDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.letter_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onMoveStackSelected(StackAdapter.StackSelectedEvent event){
        StackDialog stackDialog = (StackDialog) getSupportFragmentManager().findFragmentByTag("stack");
        stackDialog.dismiss();
        mailService.moveLetterToStack(event.getStack().getLabel(), letter.getIdentifier(), System.currentTimeMillis(), new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                super.success(aVoid,response);
                finish();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                super.failure(retrofitError);
                Toast.makeText(LetterDetailActivity.this,getString(R.string.generic_error),Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Fired when the user take an action on a message. Examples are adding to the todo stack, etc...
     * @param event
     */
    @Subscribe
    public void onLetterAction(LetterDetailFragment.LetterEvent event){
        finish();
    }
}
