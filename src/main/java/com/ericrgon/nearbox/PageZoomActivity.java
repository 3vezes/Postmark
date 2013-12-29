package com.ericrgon.nearbox;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ericrgon.nearbox.model.Page;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.InjectView;
import butterknife.Views;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PageZoomActivity extends BaseFragmentActivity{

    public static final String PAGE = "page";
    public static final String PAGE_NUMBER = "page_number";

    private Page page;

    private PhotoViewAttacher attacher;

    @InjectView(R.id.progress)
    ProgressBar progressBar;

    @InjectView(R.id.page)
    ImageView pageImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_zoom);
        Views.inject(this);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        int pageNumber = getIntent().getIntExtra(PAGE_NUMBER,0);
        setTitle(getString(R.string.page) + " " + pageNumber);

        attacher = new PhotoViewAttacher(pageImageView);

        page = (Page) getIntent().getSerializableExtra(PAGE);
        Picasso.with(this).load(page.getImages().getHighestRes()).into(pageImageView,new Callback() {
            @Override
            public void onSuccess() {
                pageImageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                attacher.update();
            }

            @Override
            public void onError() {}
        });
    }
}
