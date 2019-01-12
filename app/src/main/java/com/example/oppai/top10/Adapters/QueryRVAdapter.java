package com.example.oppai.top10.Adapters;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.oppai.top10.Article;
import com.example.oppai.top10.DataDownloader;
import com.example.oppai.top10.Query;
import com.example.oppai.top10.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class QueryRVAdapter extends RecyclerView.Adapter<QueryRVAdapter.QueryViewHolder> {
    private ArrayList<Query> queries;
    private static final String URL = "https://newsapi.org/v2/everything?";
    private Activity activity;
    private Fragment fragment;
    private Button lastCheckedButton;

    //Below DataRecyclerView content
    View rootView;


    public QueryRVAdapter(ArrayList<Query> queries, Activity activity, View rootView, Fragment fragment) {
        this.queries = queries;
        this.activity = activity;
        this.rootView = rootView;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public QueryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.query_item, parent, false);
        return new QueryViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull QueryViewHolder holder, int position) {
        holder.queryButton.setText(queries.get(position).getQuery().substring(2));

        //add uncheck when user checks another button

        holder.queryButton.setOnClickListener((v -> {

            if (lastCheckedButton != null){
                lastCheckedButton.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.colorAccentLight)));

            }
            v.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.colorAccent)));
            RecyclerView recyclerView = rootView.findViewById(R.id.search_data_rv);

            RvAdapter adapter = new RvAdapter(new ArrayList<Article>(), activity, true, fragment);
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            DataDownloader downloader = new DataDownloader(activity, true, R.id.search_data_rv, adapter, queries.get(position).toString());
            downloader.execute(URL);
            lastCheckedButton = (Button) v;
        }));

        holder.queryButton.setOnLongClickListener((v -> {
            //TODO remove from db and from queries list
            FirebaseDatabase.getInstance().getReference("Queries")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(queries
                    .get(position).getQuery()).removeValue();
            queries.remove(position);

            notifyItemRemoved(position);


            return false;
        }));
    }

    @Override
    public int getItemCount() {
        return queries.size();
    }

    class QueryViewHolder extends RecyclerView.ViewHolder{
        private Button queryButton;


        public QueryViewHolder(View itemView) {
            super(itemView);
            queryButton = itemView.findViewById(R.id.queryItem);
        }
    }
}
