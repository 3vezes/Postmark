package com.ericrgon.nearbox;


import android.os.Bundle;
import android.widget.ImageView;

import com.ericrgon.nearbox.model.Page;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

public class PageZoomActivity extends BaseFragmentActivity{

    public static final String PAGE = "page";

    private Page page;
    private ImageView pageImageView;
    private PhotoViewAttacher attacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_zoom);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        pageImageView = (ImageView) findViewById(R.id.page);
        attacher = new PhotoViewAttacher(pageImageView);

        page = (Page) getIntent().getSerializableExtra(PAGE);
        Picasso.with(this).load(page.getImages().getHighestRes()).into(pageImageView,new Callback() {
            @Override
            public void onSuccess() {
                attacher.update();
            }

            @Override
            public void onError() {}
        });
    }
}
