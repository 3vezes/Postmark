package com.ericrgon.nearbox;

import android.app.ListActivity;
import android.os.Bundle;

import com.ericrgon.nearbox.adapter.LicenseAdapter;
import com.ericrgon.nearbox.util.Licenses;

public class LicenseListActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new LicenseAdapter(Licenses.getLicenses(), this));
    }
}
