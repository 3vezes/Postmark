package com.ericrgon.nearbox.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ericrgon.nearbox.LetterGridFragment;
import com.ericrgon.nearbox.model.Letter;
import com.ericrgon.nearbox.util.Placeholders;
import com.squareup.picasso.Picasso;

import java.util.List;

public class IndexAdapter extends BaseAdapter {

    private final List<Letter> letters;
    private final Context context;
    private final LayoutInflater inflater;
    private final LetterGridFragment.Callbacks callbacks;


    public IndexAdapter(Context context, List<Letter> letters) {
        this.letters = letters;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.callbacks = (LetterGridFragment.Callbacks) context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView view = (ImageView) convertView;

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        float density = displayMetrics.density;

        int width = (int) (310 * density);
        int height = (int) (240 * density);

        if (view == null) {
            view = new ImageView(context);
            view.setLayoutParams(new GridView.LayoutParams(width,height));
        }
        String url = getItem(position).getPages().get(0).getImages().getLowestRes(displayMetrics);

        Picasso picasso = Picasso.with(context);
        picasso.setDebugging(true);
        picasso.load(url).placeholder(Placeholders.getItemId()).resize(width, height).centerCrop().into(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onItemSelected(getItem(position));
            }
        });

        return view;
    }
}
