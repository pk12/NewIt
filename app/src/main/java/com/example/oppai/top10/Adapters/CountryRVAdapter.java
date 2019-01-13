package com.example.oppai.top10.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oppai.top10.Activities.CountryActivity;
import com.example.oppai.top10.Country;
import com.example.oppai.top10.CountryFilter;
import com.example.oppai.top10.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CountryRVAdapter extends RecyclerView.Adapter<CountryRVAdapter.SelectionRVViewHolder> implements Filterable {
    private ArrayList<Country> countries, intactData;
    private Activity activity;


    public CountryRVAdapter(ArrayList<Country> countries, Activity activity) {
        this.activity = activity;
        this.countries = countries;
        this.intactData = new ArrayList<>();
        this.intactData.addAll(countries);

    }

    @NonNull
    @Override
    public SelectionRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_item, parent, false);
        return new SelectionRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectionRVViewHolder holder, int position) {

        Picasso.get().load(countries.get(position).getUrl()).into(holder.countryImg);
        holder.countryTextView.setText(countries.get(position).getName());

    }


    @Override
    public int getItemCount() {
        return countries.size();
    }

    @Override
    public Filter getFilter() {
        return new CountryFilter(countries, intactData,this);
    }

    public ArrayList<Country> getCountries() {
        return countries;
    }

    public class SelectionRVViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        private TextView countryTextView;
        private ImageView countryImg;

        public SelectionRVViewHolder(View itemView) {
            super(itemView);
            countryTextView = itemView.findViewById(R.id.country_textView);
            countryImg = itemView.findViewById(R.id.country_image);
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, CountryActivity.class);
            intent.putExtra("Country", countries.get(getLayoutPosition()).getCode());
            intent.putExtra("CountryName", countryTextView.getText().toString());
            activity.startActivity(intent);
            //dont finish in case we want to go back
        }
    }
}
