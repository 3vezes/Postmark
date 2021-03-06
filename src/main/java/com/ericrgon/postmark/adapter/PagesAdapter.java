package com.ericrgon.postmark.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ericrgon.postmark.BaseFragmentActivity;
import com.ericrgon.postmark.ZoomDetectedImageView;
import com.ericrgon.postmark.model.Page;
import com.ericrgon.postmark.util.Placeholders;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PagesAdapter extends BaseAdapter{

    private final List<Page> pagesList;
    private final LayoutInflater layoutInflater;
    private final Context context;
    private final EventBus bus;

    public static class PageSelectedEvent{
        private final Page page;
        private final int pageNumber;

        public PageSelectedEvent(Page page, int pageNumber) {
            this.page = page;
            this.pageNumber = pageNumber;
        }

        public Page getPage() {
            return page;
        }

        public int getPageNumber() {
            return pageNumber;
        }
    }

    public PagesAdapter(Context context, List<Page> pages) {
        this.layoutInflater = LayoutInflater.from(context);
        this.pagesList = Lists.newArrayList(pages);
        this.context = context;
        this.bus = ((BaseFragmentActivity) context).getEventBus();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ZoomDetectedImageView pageImage = new ZoomDetectedImageView(context);
        pageImage.setAdjustViewBounds(true);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        String url = pagesList.get(position).getImages().getRes(displayMetrics);

        Picasso picasso = Picasso.with(context);
        picasso.load(url).placeholder(Placeholders.getItemId()).into(pageImage);

        pageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bus.post(new PageSelectedEvent(pagesList.get(position), position));
            }
        });

        return pageImage;
    }
}
