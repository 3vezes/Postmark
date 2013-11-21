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

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DrawerFragment extends Fragment {

    private OutboxMailService mailService;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mailService = ((BaseFragmentActivity) activity).getMailService();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_drawer,container,false);

        final LinearLayout folderLayout = (LinearLayout) rootView.findViewById(R.id.folders);

        mailService.getFolders(new Callback<List<Stack>>() {
            @Override
            public void success(List<Stack> stacks, Response response) {
                for(Stack stack : stacks){
                    TextView folderItem = (TextView) inflater.inflate(R.layout.folder_list_item,folderLayout,false);
                    folderItem.setText(stack.getLabel());
                    folderLayout.addView(folderItem);
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {}
        });


        return rootView;
    }
}
