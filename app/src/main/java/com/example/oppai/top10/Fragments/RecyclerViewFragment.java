package com.example.oppai.top10.Fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.os.ConfigurationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.oppai.top10.Activities.MainActivity;
import com.example.oppai.top10.Activities.QuerySearchActivity;
import com.example.oppai.top10.Adapters.RvAdapter;
import com.example.oppai.top10.Article;
import com.example.oppai.top10.DataDownloader;
import com.example.oppai.top10.Query;
import com.example.oppai.top10.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class RecyclerViewFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "RecyclerViewFragment";
    private String URL = "https://newsapi.org/v2/top-headlines";
    private ArrayList<String> enabledCategories;
    private RvAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler myHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_main,container,false);

        adapter = new RvAdapter(new ArrayList<Article>(), getActivity(), true);
        adapter.setHasStableIds(true);
        //for the custom menus
        setHasOptionsMenu(true);


        //Get Category data and make them selected or not
        enabledCategories = new ArrayList<>();
        List<String> categories = Arrays.asList(getResources().getStringArray(R.array.categories));

        //Get the preferred categories
        for (String category : categories){
            boolean isEnabled = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(category, false);
            if (isEnabled)
                enabledCategories.add(category);
        }

        RecyclerView recyclerView = RootView.findViewById(R.id.RecyclerViewMain);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        swipeRefreshLayout = RootView.findViewById(R.id.SwipeRefresh);


        //Check what activity is the current one and get the correct params
        ChangeParameters(recyclerView, swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorAccentDark));
        //Swipe To refresh Listener
        swipeRefreshLayout.setOnRefreshListener(() -> {
            myHandler = new Handler();
            myHandler.post(() -> {
                swipeRefreshLayout.setRefreshing(true);
                ChangeParameters(recyclerView, swipeRefreshLayout);
                swipeRefreshLayout.setRefreshing(false);
            });

        });




        return RootView;
    }


    private void ChangeParameters(RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout){
        if (getActivity() instanceof MainActivity){
            Locale locale = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).get(0);
            DownloadData(URL + "?country=" + locale.getCountry(), recyclerView);
        }
        else if (getActivity() instanceof QuerySearchActivity){
            //get Bundle data
            assert this.getArguments() != null;
            Bundle bundle = this.getArguments();

            //edit URL
            this.URL = "https://newsapi.org/v2/everything?";
            DownloadQueryData(URL, recyclerView, bundle);
        }
        else {
            String country = getActivity().getIntent().getStringExtra("Country");

            if (country != null)
                DownloadData(URL + "?country=" + country , recyclerView);
            else
                Log.e(TAG, "onCreateView: No country found");
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (getActivity() instanceof QuerySearchActivity){
            menu.findItem(R.id.preference_done).setVisible(false);
            menu.findItem(R.id.save_query).setVisible(true);
            menu.findItem(R.id.searchArticlesView).setVisible(true);
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.save_query:
                SaveQuery();
                return true;
        }
        return false;
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
                if (!s.isEmpty()){
                    swipeRefreshLayout.setEnabled(false);
                }

                swipeRefreshLayout.setEnabled(true);
                adapter.getFilter().filter(s);
                return true;


            }
        }));
    }

    private void DownloadData(String URL, RecyclerView mainRecyclerView){
        //Initialize layout data(Empty)


        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1 , StaggeredGridLayoutManager.VERTICAL);

        mainRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mainRecyclerView.setAdapter(adapter);

        //Fetch Data for each of the categories, append the datalist(synchronized block) and notifydatasetChanged
        for (String category : enabledCategories){
            DataDownloader dataDownloader = new DataDownloader(getActivity(), true, R.id.RecyclerViewMain, adapter, "&category=" + category);
            dataDownloader.execute(URL);

        }



    }

    private void DownloadQueryData(String URL, RecyclerView mainRecyclerView, Bundle paremeters){

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        mainRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mainRecyclerView.setAdapter(adapter);

        //Build params
        StringBuilder sb = new StringBuilder();
        sb.append(paremeters.get("Query"));
        sb.append(paremeters.get("FromDate"));
        sb.append(paremeters.get("ToDate"));
        sb.append(paremeters.get("LanguageCode"));
        sb.append(paremeters.get("SortOption"));
        sb.append(paremeters.get("PageSize"));

        DataDownloader dataDownloader = new DataDownloader(getActivity(), true, mainRecyclerView.getId(), adapter, sb.toString());
        dataDownloader.execute(URL);
    }

    private void SaveQuery(){
        Bundle paremeters = getArguments();
        Query query = new Query(paremeters.get("Query").toString(), paremeters.get("SortOption").toString(), paremeters.get("LanguageCode").toString()
                , paremeters.get("FromDate").toString(), paremeters.get("ToDate").toString(), paremeters.get("PageSize").toString());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Queries").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(query.getQuery());
        reference.setValue(query);
        Toast.makeText(getActivity().getApplicationContext(), "Query saved", Toast.LENGTH_SHORT).show();
    }

}
