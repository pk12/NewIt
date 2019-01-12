package com.example.oppai.top10.Activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.oppai.top10.Fragments.DatePickerFragment;
import com.example.oppai.top10.Fragments.QuerySearchFragment;
import com.example.oppai.top10.Fragments.RecyclerViewFragment;
import com.example.oppai.top10.R;

import java.util.Arrays;
import java.util.List;

public class QuerySearchActivity extends AppCompatActivity {
    private Spinner sortOptions;
    private CheckBox useSort;

    private Spinner languageOptions;
    private CheckBox useLanguage;

    private Button fromDate;
    private Button toDate;

    private EditText query;

    private EditText maxPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new QuerySearchFragment()).commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Advanced Search");


    }

    public void onSelectedDateClick(View view){
        DialogFragment newFragment = new DatePickerFragment();

        //pass view id to fragment
        Bundle bundle = new Bundle();
        bundle.putInt("Id", view.getId());
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.preference_menu, menu);
        return true;
    }


    private void getData(){
        //Get query
        if (query.getText() != null){
            String query = this.query.getText().toString();

            Bundle bundle = new Bundle();
            bundle.putString("Query", "q=" + query);

            //Get the data inside of so that we make sure the query has been entered

            //get sortOptions if checked
            if (useSort.isChecked()){
                int position = this.sortOptions.getSelectedItemPosition();
                List<String> sortBy = Arrays.asList(getResources().getStringArray(R.array.sort_options));
                bundle.putString("SortOption", "&sortBy" + sortBy.get(position));
            }
            else bundle.putString("SortOption", "");

            //get Language options if checked
            if (useLanguage.isChecked()){
                //get position to get from the language codes array
                int languagePosition = this.languageOptions.getSelectedItemPosition();
                List<String> codes = Arrays.asList(getResources().getStringArray(R.array.language_codes));
                String code = codes.get(languagePosition);
                bundle.putString("LanguageCode", "&language" + code);
            }
            else bundle.putString("LanguageCode", "");

            if (!fromDate.getText().equals("Select Date")){
                bundle.putString("FromDate", "&from" + fromDate.getText().toString());
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
            Toast.makeText(getApplicationContext(), "Please enter query", Toast.LENGTH_SHORT).show();
        }



    }

    private void search(Bundle args){
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();

    }
}
