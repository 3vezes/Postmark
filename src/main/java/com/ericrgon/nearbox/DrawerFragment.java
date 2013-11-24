package com.ericrgon.nearbox;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ericrgon.nearbox.model.Stack;
import com.ericrgon.nearbox.rest.OutboxMailService;
import com.google.common.eventbus.EventBus;

import java.util.List;

import retrofit.Callback;
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

        final LinearLayout folderLayout = (LinearLayout) rootView.findViewById(R.id.folders);
        mailService.getStacks(new Callback<List<Stack>>() {
            @Override
            public void success(List<Stack> stacks, Response response) {
                for (final Stack stack : stacks) {
                    TextView folderItem = (TextView) inflater.inflate(R.layout.folder_list_item, folderLayout, false);
                    folderItem.setText(stack.getLabel());
                    folderItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            eventBus.post(new StackSelectedEvent(stack));
                        }
                    });
                    folderLayout.addView(folderItem);
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
            }
        });

        return rootView;
    }
}
