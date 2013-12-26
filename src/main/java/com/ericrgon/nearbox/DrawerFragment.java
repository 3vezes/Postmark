package com.ericrgon.nearbox;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ericrgon.nearbox.model.Stack;
import com.ericrgon.nearbox.rest.Callback;
import com.ericrgon.nearbox.rest.OutboxMailService;
import com.ericrgon.nearbox.util.Placeholders;
import com.google.common.eventbus.EventBus;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class DrawerFragment extends Fragment {

    private OutboxMailService mailService;
    private EventBus eventBus;

    public static class StackSelectedEvent {
        private final Stack stack;

        public StackSelectedEvent(Stack stack) {
            this.stack = stack;
        }

        public Stack getStack() {
            return stack;
        }
    }

    public static class StatusSelectedEvent {
        private final OutboxMailService.Status status;

        public StatusSelectedEvent(OutboxMailService.Status status) {
            this.status = status;
        }

        public OutboxMailService.Status getStatus() {
            return status;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mailService = ((BaseFragmentActivity) activity).getMailService();
        eventBus = ((BaseFragmentActivity) activity).getEventBus();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_drawer,container,false);

        View inbox = rootView.findViewById(R.id.inbox);
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventBus.post(new StatusSelectedEvent(OutboxMailService.Status.UNSORTED));
            }
        });

        View todo = rootView.findViewById(R.id.todo);
        todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventBus.post(new StatusSelectedEvent(OutboxMailService.Status.TODO));
            }
        });

        View requested = rootView.findViewById(R.id.requested);
        requested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventBus.post(new StatusSelectedEvent(OutboxMailService.Status.REQUESTED));
            }
        });

        final View progressBar = rootView.findViewById(R.id.progress);

        final LinearLayout folderLayout = (LinearLayout) rootView.findViewById(R.id.folders);
        mailService.getStacks(new Callback<List<Stack>>() {
            @Override
            public void success(List<Stack> stacks, Response response) {
                super.success(stacks,response);

                //Hide progress bar.
                progressBar.setVisibility(View.GONE);

                for (final Stack stack : stacks) {
                    View folderItemLayout = inflater.inflate(R.layout.drawer_child_item, folderLayout, false);
                    folderItemLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            eventBus.post(new StackSelectedEvent(stack));
                        }
                    });

                    TextView labelName = (TextView) folderItemLayout.findViewById(R.id.text1);
                    labelName.setText(stack.getLabel());

                    //Load the image icon.
                    ImageView labelIcon = (ImageView) folderItemLayout.findViewById(R.id.icon);

                    Picasso picasso = Picasso.with(getActivity());
                    picasso.load(stack.getIconURL())
                            .placeholder(Placeholders.getItemId())
                            .resize(labelName.getMinHeight(), labelName.getMinHeight())
                            .centerCrop()
                            .into(labelIcon);

                    folderLayout.addView(folderItemLayout);
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                super.failure(retrofitError);
            }
        });

        return rootView;
    }
}
