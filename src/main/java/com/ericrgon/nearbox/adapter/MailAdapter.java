package com.ericrgon.nearbox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ericrgon.nearbox.model.Letter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MailAdapter extends BaseAdapter {

    private final List<Letter> letters;
    private final Context context;
    private final LayoutInflater inflater;


    public MailAdapter(Context context,List<Letter> letters) {
        this.letters = letters;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return letters.size();
    }

    @Override
    public Letter getItem(int position) {
        return letters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view = (ImageView) convertView;
        if (view == null) {
            float density = context.getResources().getDisplayMetrics().density;

            view = new ImageView(context);
            view.setLayoutParams(new GridView.LayoutParams((int) (100 * density),(int) (100 * density)));
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setPadding(8, 8, 8, 8);
        }
        String url = getItem(position).getPages().get(0).getImages().getImages().get(50);

        Picasso picasso = Picasso.with(context);
        picasso.setDebugging(true);
        picasso.load(url).into(view);

        return view;
    }
}
