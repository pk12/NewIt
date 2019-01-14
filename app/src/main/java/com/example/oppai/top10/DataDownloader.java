package com.example.oppai.top10;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.example.oppai.top10.Adapters.RvAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DataDownloader extends AsyncTask<String, Void, String> {
    private static final String TAG = "DownloadDataTask";
    private static final String APIKey = "&apiKey=e30cd747bab14a26a485fa5cc9d9689b";

    private Activity activity;
    private boolean isVertical;
    private int rvID;
    private RvAdapter adapter;
    private String searchParams;
    private volatile int counter;

    public DataDownloader(Activity activity, boolean isVertical, int rvID, RvAdapter adapter, String params) {
        this.activity = activity;
        this.isVertical = isVertical;
        this.rvID = rvID;
        this.searchParams = params;
        this.adapter = adapter;
        counter = 0;
    }

    @Override
    protected void onPostExecute(String jsonData) {
        super.onPostExecute(jsonData);
        Log.d(TAG, "onPostExecute parameter is " + jsonData );
        JsonParser parser = new JsonParser();
        parser.parseJson(jsonData);

        ArrayList<Article> articles = parser.getArticles();
        synchronized (articles){
            adapter.getData().addAll(articles);
            adapter.getDataIntact().addAll(articles);
            adapter.notifyDataSetChanged();
            if (adapter.getItemCount() > 0){
                //hide no results
                activity.findViewById(R.id.noResultsImageView).setVisibility(View.GONE);
                activity.findViewById(R.id.NoResultsTextView).setVisibility(View.GONE);
            }
            else {
                activity.findViewById(R.id.noResultsImageView).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.NoResultsTextView).setVisibility(View.VISIBLE);
            }
        }





//        if (isVertical){
//            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL);
//            recyclerView.setLayoutManager(staggeredGridLayoutManager);
//        }
//        else {
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity.getApplicationContext());
//            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//            recyclerView.setLayoutManager(linearLayoutManager);
//        }



//        recyclerView.setAdapter(rvAdapter); Done in the recyclerFragmentMain
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: starts with: " + strings[0]);
        String postData;

        if (isVertical)
           postData  = DownloadJson(strings[0] + searchParams + APIKey);
        else
            postData = DownloadJson(strings[0]+ "&pagesize=7" + APIKey);

        
        if (postData == null){
            Log.e(TAG,  "doInBackground: Error downloading from url " + strings[0]);
        }
        
        return postData;
    }

    private String DownloadJson(String urlPath){
        StringBuilder stringBuilder = new StringBuilder();

        try {
            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            Log.d(TAG, "DownloadJson: Response code was" + responseCode);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = reader.readLine();
            while(line != null){
                stringBuilder.append(line).append("\n");
                line = reader.readLine();
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "downloadJSON: not correct URL: "+urlPath , e);
        } catch (IOException e) {
            Log.e(TAG, "downloadJSON: io error ",e);
        }

        return stringBuilder.toString();
    }
}
