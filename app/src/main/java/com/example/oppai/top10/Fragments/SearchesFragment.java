package com.example.oppai.top10.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oppai.top10.Adapters.QueryRVAdapter;
import com.example.oppai.top10.Query;
import com.example.oppai.top10.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.util.ArrayList;

public class SearchesFragment extends Fragment {
    private ArrayList<Query> queries;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_searches, container, false);

        RecyclerView recyclerView = RootView.findViewById(R.id.queriesRV);
        queries = new ArrayList<>();

        //Init adapter
        QueryRVAdapter adapter = new QueryRVAdapter(queries, getActivity(), RootView, this);

        //Init LayoutManager
        FlowLayoutManager layoutManager = new FlowLayoutManager();
        layoutManager.setAutoMeasureEnabled(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //initialize data fetcher
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Queries").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.getChildren() != null){
                        queries.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            queries.add(snapshot.getValue(Query.class));
                        }
                        adapter.notifyDataSetChanged();

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return RootView;
    }
}
