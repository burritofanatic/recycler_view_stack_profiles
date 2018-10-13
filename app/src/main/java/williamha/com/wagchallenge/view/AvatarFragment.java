package williamha.com.wagchallenge.view;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class AvatarFragment extends Fragment {

    RecyclerView recyclerView;
    AvatarAdapter adapter;

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

        recyclerView = view.findViewById(R.id.avatar_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        StackProfileViewModel stackProfileViewModel = ViewModelProviders.of(this).get(StackProfileViewModel.class);
        stackProfileViewModel.getProfiles().observe(this, profiles -> {
            adapter.setProfiles(profiles, getContext());
        });

        List<StackProfile> profiles = new ArrayList<>();
        adapter = new AvatarAdapter(profiles);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}