package com.ericrgon.nearbox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ericrgon.nearbox.R;
import com.ericrgon.nearbox.model.License;
import com.ericrgon.nearbox.util.ViewHolder;

import java.util.List;

public class LicenseAdapter extends BaseAdapter {

    private final List<License> licenses;
    private final LayoutInflater inflater;

    public LicenseAdapter(List<License> licenses, Context context) {
        this.licenses = licenses;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return licenses.size();
    }

    @Override
    public License getItem(int position) {
        return licenses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.license_list_item,parent,false);
        }

        License license = licenses.get(position);

        TextView header = ViewHolder.get(convertView,R.id.header);
        header.setText(license.getHeader());

        TextView copyright = ViewHolder.get(convertView,R.id.copyright);
        copyright.setText(license.getCopyright());

        TextView licenseText = ViewHolder.get(convertView,R.id.license);
        licenseText.setText(license.getLicense());

        return convertView;
    }
}
