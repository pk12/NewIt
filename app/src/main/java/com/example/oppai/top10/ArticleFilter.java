package com.example.oppai.top10;


import android.widget.Filter;

import com.example.oppai.top10.Adapters.RvAdapter;

import java.util.ArrayList;
import java.util.Collection;

public class ArticleFilter extends Filter {
    private ArrayList<Article> allArticles, filteredArticles, intactArticles;
    RvAdapter adapter;

    public ArticleFilter(ArrayList<Article> allArticles, RvAdapter adapter, ArrayList<Article> intactArticles) {
        this.allArticles = allArticles;
        this.filteredArticles = new ArrayList<>();
        this.adapter = adapter;
        this.intactArticles = intactArticles;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {

        String query = charSequence.toString();

        if (query.isEmpty()){
            filteredArticles = intactArticles;
        }
        else {
            for (Article article: intactArticles) {
                if (article.getTitle().toLowerCase().contains(query.toLowerCase()) || article.getDescription().contains(query)){
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
    }
}
