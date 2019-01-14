package com.example.oppai.top10.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oppai.top10.Adapters.RvAdapter;
import com.example.oppai.top10.Article;
import com.example.oppai.top10.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {
    private RvAdapter adapter;
    TextView textView;
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);
        SwipeRefreshLayout swipeRefreshLayout = RootView.findViewById(R.id.SwipeRefresh);
        swipeRefreshLayout.setEnabled(false);

        RecyclerView recyclerView = RootView.findViewById(R.id.RecyclerViewMain);

        imageView = RootView.findViewById(R.id.noResultsImageView);
        textView = RootView.findViewById(R.id.NoResultsTextView);
        //connect RecyclerView to adapter
        adapter = new RvAdapter(new ArrayList<>(), getActivity(), textView, imageView, FavoritesFragment.this);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //Fetch data from firebase asynch
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Favorites").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.getChildren() != null) {
                        adapter.getData().clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            adapter.getData().add(snapshot.getValue(Article.class));
                        }
                        adapter.getDataIntact().addAll(adapter.getData());
                    }

                    //notify datasetchanged
                    adapter.notifyDataSetChanged();
                    if (adapter.getItemCount() > 0){
                        //hide no results
                        RootView.findViewById(R.id.noResultsImageView).setVisibility(View.GONE);
                        RootView.findViewById(R.id.NoResultsTextView).setVisibility(View.GONE);
                    }
                    else {
                        RootView.findViewById(R.id.noResultsImageView).setVisibility(View.VISIBLE);
                        RootView.findViewById(R.id.NoResultsTextView).setVisibility(View.VISIBLE);
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

        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.searchArticlesView).getActionView();
        searchView.setOnCloseListener(() -> {
            //set the initial adapter data
            adapter.getFilter().filter("");
            return true;
        });
        //init the searchview
        searchView.setOnQueryTextListener((new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                //if it is not empty then dont do anything
                adapter.getFilter().filter(s);
                return true;


            }
        }));
    }
}
