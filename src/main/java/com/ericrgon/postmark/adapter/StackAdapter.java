package com.ericrgon.postmark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ericrgon.postmark.BaseFragmentActivity;
import com.ericrgon.postmark.R;
import com.ericrgon.postmark.model.Stack;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import java.util.List;

public class StackAdapter extends BaseAdapter{

    public static class StackSelectedEvent{
        private final Stack stack;

        private StackSelectedEvent(Stack stack) {
            this.stack = stack;
        }

        public Stack getStack() {
            return stack;
        }
    }

    private final List<Stack> stacks;
    private final LayoutInflater inflater;
    private final EventBus eventBus;

    public StackAdapter(Context context,List<Stack> stacks) {
        this.stacks = Lists.newArrayList(stacks);
        this.inflater = LayoutInflater.from(context);
        this.eventBus = ((BaseFragmentActivity) context).getEventBus();
    }

    @Override
    public int getCount() {
        return stacks.size();
    }

    @Override
    public Stack getItem(int position) {
        return stacks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view =  inflater.inflate(R.layout.stack_list_item,parent,false);
        TextView name = (TextView) view.findViewById(R.id.stackName);
        name.setText(stacks.get(position).getLabel());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventBus.post(new StackSelectedEvent(stacks.get(position)));
            }
        });
        return view;
    }
}
