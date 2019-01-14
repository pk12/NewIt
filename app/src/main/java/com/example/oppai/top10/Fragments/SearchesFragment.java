package com.example.oppai.top10.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
    private QueryRVAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_searches, container, false);
        setHasOptionsMenu(true);
        RootView.findViewById(R.id.noResultsImageView).setVisibility(View.GONE);
        RootView.findViewById(R.id.NoResultsTextView).setVisibility(View.GONE);

        RecyclerView recyclerView = RootView.findViewById(R.id.queriesRV);
        queries = new ArrayList<>();

        //Init adapter
        adapter = new QueryRVAdapter(queries, getActivity(), RootView, this);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        SearchView searchView = (SearchView) menu.findItem(R.id.searchArticlesView).getActionView();
        searchView.setOnCloseListener(() -> {
            //set the initial adapter data
            adapter.getRvAdapter().getFilter().filter("");
            return true;
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter.getRvAdapter() != null){
                    adapter.getRvAdapter().getFilter().filter(newText);
                    return true;
                }
                return false;

            }
        });
    }
}
