package com.example.oppai.top10.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private ArrayList<Article> articles;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_main, container, false);

        RecyclerView recyclerView = RootView.findViewById(R.id.RecyclerViewMain);
        articles = new ArrayList<>();


        //connect RecyclerView to adapter
        RvAdapter adapter = new RvAdapter(articles, getActivity(), true, FavoritesFragment.this);
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
                        articles.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            articles.add(snapshot.getValue(Article.class));
                        }
                    }

                    //notify datasetchanged
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return RootView;
    }
}
