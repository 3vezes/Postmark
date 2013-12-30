package com.ericrgon.postmark.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ericrgon.postmark.LetterGridFragment;
import com.ericrgon.postmark.R;
import com.ericrgon.postmark.image.GridImageView;
import com.ericrgon.postmark.model.Letter;
import com.google.common.collect.Lists;
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

    public IndexAdapter(Context context) {
        this(context, Lists.<Letter>newArrayList());
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
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Letter letter = getItem(position);
        String url = letter.getPages().get(0).getImages().getRes(displayMetrics);

        View root = inflater.inflate(R.layout.letter_grid_item, parent, false);

        TextView date = (TextView) root.findViewById(R.id.date);
        date.setText(letter.getDeliveredDate());

        GridImageView indexImage = (GridImageView) root.findViewById(R.id.indexImage);
        Picasso.with(context)
                .load(url)
                .into(indexImage);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onItemSelected(getItem(position));
            }
        });


        return root;
    }

    public void update(List<Letter> letters){
        this.letters.clear();
        this.letters.addAll(letters);
        notifyDataSetChanged();
    }
}
