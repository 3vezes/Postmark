package com.ericrgon.nearbox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ericrgon.nearbox.R;
import com.ericrgon.nearbox.model.Page;
import com.google.common.collect.Lists;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PagesAdapter extends BaseAdapter{

    private final List<Page> pagesList;
    private final LayoutInflater layoutInflater;
    private final Context context;

    public PagesAdapter(Context context, List<Page> pages) {
        this.layoutInflater = LayoutInflater.from(context);
        this.pagesList = Lists.newArrayList(pages);
        this.context = context;
    }

    @Override
    public int getCount() {
        return pagesList.size();
    }

    @Override
    public Object getItem(int position) {
        return pagesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView pageImage = new ImageView(context);
        pageImage.setAdjustViewBounds(true);
        
        String url = pagesList.get(position).getImages().getHighestRes();

        Picasso picasso = Picasso.with(context);
        picasso.setDebugging(true);
        picasso.load(url).placeholder(R.drawable.ic_action_mail).into(pageImage);

        return pageImage;
    }
}
