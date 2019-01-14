package com.example.oppai.top10;


import android.app.Activity;
import android.view.View;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oppai.top10.Adapters.RvAdapter;

import java.util.ArrayList;
import java.util.Collection;

public class ArticleFilter extends Filter {
    private ArrayList<Article> allArticles, filteredArticles, intactArticles;
    private RvAdapter adapter;
    private Activity activity;
    private TextView textView;
    private ImageView imageView;

    public ArticleFilter(ArrayList<Article> allArticles, ArrayList<Article> intactArticles, RvAdapter adapter, TextView textView, ImageView imageView) {
        this.allArticles = allArticles;
        this.filteredArticles = new ArrayList<>();
        this.intactArticles = intactArticles;
        this.adapter = adapter;
        this.textView = textView;
        this.imageView = imageView;
    }

    public ArticleFilter(ArrayList<Article> allArticles, RvAdapter adapter, ArrayList<Article> intactArticles, Activity activity) {
        this.allArticles = allArticles;
        this.filteredArticles = new ArrayList<>();
        this.adapter = adapter;
        this.intactArticles = intactArticles;
        this.activity = activity;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {

        String query = charSequence.toString();

        if (query.isEmpty()){
            filteredArticles = intactArticles;
        }
        else {
            for (Article article: intactArticles) {
                if (article.getTitle().toLowerCase().contains(query.toLowerCase())){
                    filteredArticles.add(article);
                }
            }
        }

        FilterResults results = new FilterResults();
        results.values = filteredArticles;
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.getData().clear();

        //Update only the dataset not the intact one
        adapter.getData().addAll((Collection<? extends Article>) filterResults.values);
        adapter.notifyDataSetChanged();

        if (adapter.getItemCount() == 0){
            if (activity == null){
                textView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
            }
            else {
                activity.findViewById(R.id.noResultsImageView).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.NoResultsTextView).setVisibility(View.VISIBLE);
            }

        }
        else {
            if (activity == null){
                textView.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
            }
            else {
                activity.findViewById(R.id.noResultsImageView).setVisibility(View.GONE);
                activity.findViewById(R.id.NoResultsTextView).setVisibility(View.GONE);
            }
        }
    }
}
