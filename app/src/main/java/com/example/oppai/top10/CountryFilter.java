package com.example.oppai.top10;

import android.widget.Filter;

import com.example.oppai.top10.Adapters.CountryRVAdapter;

import java.util.ArrayList;
import java.util.Collection;

public class CountryFilter extends Filter {
    private CountryRVAdapter adapter;
    private ArrayList<Country> dataset, intactData, filteredData;

    public CountryFilter(ArrayList<Country> dataset,ArrayList<Country> intactData, CountryRVAdapter adapter) {
        this.dataset = dataset;
        this.intactData = intactData;
        this.filteredData = new ArrayList<>();
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        String query = constraint.toString();

        if (query.isEmpty()){
            filteredData = intactData;
        }
        else {
            for (Country country: intactData){
                if (country.getName().toLowerCase().contains(query)){
                    filteredData.add(country);
                }
            }
        }
        results.values = filteredData;
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.getCountries().clear();

        adapter.getCountries().addAll((Collection<? extends Country>) results.values);
        adapter.notifyDataSetChanged();
    }
}
