package com.example.oppai.top10.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.oppai.top10.R;

import java.util.Arrays;
import java.util.List;

public class QuerySearchFragment extends Fragment {
    private Spinner sortOptions;
    private CheckBox useSort;

    private Spinner languageOptions;
    private CheckBox useLanguage;

    private Button fromDate;
    private Button toDate;

    private EditText query;

    private EditText maxPages;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        View RootView = inflater.inflate(R.layout.fragment_query_search,container,false);

        RecyclerView recyclerView = RootView.findViewById(R.id.queriesRV);

        //Fetch saved queries


        //Initialize views
        sortOptions = RootView.findViewById(R.id.sort_options_spinner);
        useSort = RootView.findViewById(R.id.use_sort_checkbox);

        languageOptions = RootView.findViewById(R.id.language_spinner);
        useLanguage = RootView.findViewById(R.id.use_language_checkbox);

        fromDate = RootView.findViewById(R.id.fromText);
        toDate = RootView.findViewById(R.id.toText);

        query = RootView.findViewById(R.id.QueryText);

        maxPages = RootView.findViewById(R.id.num_of_results);

        fromDate.setOnClickListener((v -> {
            onSelectedDateClick(v);
        }));

        toDate.setOnClickListener((v -> {
            onSelectedDateClick(v);
        }));

        setHasOptionsMenu(true);
        return RootView;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
            case R.id.preference_done:
                getData();
                return true;

        }

        return false;
    }

    public void onSelectedDateClick(View view){
        DialogFragment newFragment = new DatePickerFragment();

        //pass view id to fragment
        Bundle bundle = new Bundle();
        bundle.putInt("Id", view.getId());
        newFragment.setArguments(bundle);
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private void getData(){
        //Get query
        if (!query.getText().toString().equals("")){
            String query = this.query.getText().toString();

            Bundle bundle = new Bundle();
            bundle.putString("Query", "q=" + query);

            //Get the data inside of so that we make sure the query has been entered

            //get sortOptions if checked
            if (useSort.isChecked()){
                int position = this.sortOptions.getSelectedItemPosition();
                List<String> sortBy = Arrays.asList(getResources().getStringArray(R.array.sort_options));
                bundle.putString("SortOption", "&sortBy=" + sortBy.get(position));
            }
            else bundle.putString("SortOption", "");

            //get Language options if checked
            if (useLanguage.isChecked()){
                //get position to get from the language codes array
                int languagePosition = this.languageOptions.getSelectedItemPosition();
                List<String> codes = Arrays.asList(getResources().getStringArray(R.array.language_codes));
                String code = codes.get(languagePosition);
                bundle.putString("LanguageCode", "&language=" + code);
            }
            else bundle.putString("LanguageCode", "");

            if (!fromDate.getText().equals("Select Date")){
                bundle.putString("FromDate", "&from=" + fromDate.getText().toString());
            }
            else bundle.putString("FromDate", "");

            if (!toDate.getText().equals("Select Date")){
                bundle.putString("ToDate", "&to=" + toDate.getText().toString());
            }
            else bundle.putString("ToDate", "");

            if (!maxPages.getText().toString().equals("")){
                String pageSize = maxPages.getText().toString();
                bundle.putString("PageSize", "&pageSize=" + pageSize);
            }
            else bundle.putString("PageSize", "");

            //StartSearch
            search(bundle);


        }
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Please enter query", Toast.LENGTH_SHORT).show();
        }



    }

    private void search(Bundle args){
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        fragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();


    }


}
