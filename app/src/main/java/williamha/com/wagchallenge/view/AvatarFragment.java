package williamha.com.wagchallenge.view;


import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

import williamha.com.wagchallenge.R;
import williamha.com.wagchallenge.model.StackProfile;
import williamha.com.wagchallenge.view.adapter.AvatarAdapter;
import williamha.com.wagchallenge.viewmodel.StackProfileViewModel;

/**
 * A simple {@link Fragment} subclass.
 */

public class AvatarFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private AvatarAdapter adapter;
    private StackProfileViewModel stackProfileViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;


    public AvatarFragment() {
        // Required empty public constructor
    }

    public static AvatarFragment newInstance() {
        return new AvatarFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_avatar, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.avatar_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        stackProfileViewModel = ViewModelProviders.of(this).get(StackProfileViewModel.class);
        kickoffFetch();


        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        List<StackProfile> profiles = new ArrayList<>();
        adapter = new AvatarAdapter(profiles);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!isNetworkAvailable()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle(R.string.network_unavailable);
            dialog.setMessage(R.string.offline_message);
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            dialog.show();
        }
    }

    // Helpers

    private void kickoffFetch() {
        stackProfileViewModel.getProfiles().observe(this, profiles -> {
            adapter.setProfiles(profiles, getContext());
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Swipe Refresh Listener
    @Override
    public void onRefresh() {
        kickoffFetch();
    }
}