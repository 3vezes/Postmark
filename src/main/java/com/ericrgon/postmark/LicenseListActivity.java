package com.ericrgon.postmark;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.ericrgon.postmark.adapter.LicenseAdapter;
import com.ericrgon.postmark.util.Licenses;

public class LicenseListActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new LicenseAdapter(Licenses.getLicenses(), this));
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
