package com.fall.infosysproject.activities.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.fall.infosysproject.R;
import com.fall.infosysproject.adapters.ItemsAdater;
import com.fall.infosysproject.models.Flickr;
import com.fall.infosysproject.viewmodels.FlickrViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

import static com.fall.infosysproject.utilities.Helper.toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String KEY_SEARCH = "itemSearch";
    private static final String SEARCH_TERM = "SEARCH_TERM";

    private EditText editTextSearch;
    private TextView textViewSearchTerm;
    private Button buttonSearch;
    private String searchTerm = "";
    private ListView listViewSearchTerms;

    //private ApiInterface apiInterface;
    private FlickrViewModel flickrViewModel;

    private GridView gridView;
    private ArrayList<String> listSearchTerm = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flickrViewModel = new FlickrViewModel(this);

        initViews();

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            searchTerm = savedInstanceState.getString(KEY_SEARCH);
            if (!searchTerm.equals(""))
                observeImages(searchTerm);
        }

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchTerm = editTextSearch.getText().toString().trim().toUpperCase();
                if (!hasValidationErrors(searchTerm)) {
                    searchTerm(searchTerm);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        observeSearchTerms();
        if (!searchTerm.equals(""))
            observeImages(searchTerm);
    }

    public void initViews() {
        editTextSearch = (EditText) findViewById(R.id.et_search);
        buttonSearch = (Button) findViewById(R.id.bt_search);
        gridView = (GridView) findViewById(R.id.gridview);
        textViewSearchTerm = (TextView) findViewById(R.id.tv_no_available_search_term);
        listViewSearchTerms = (ListView) findViewById(R.id.lv_search_term);
    }

    public void setListTerms(final ArrayList<String> listSearchTerm) {
        if (listSearchTerm.size() > 0) {
            textViewSearchTerm.setVisibility(View.GONE);

            ArrayAdapter adapter = new ArrayAdapter<String>(this,
                    R.layout.search_term, listSearchTerm);
            listViewSearchTerms.setAdapter(adapter);

            listViewSearchTerms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    searchTerm = listSearchTerm.get(position);
                    searchTerm(searchTerm);
                }
            });
        }
    }

    public void observeSearchTerms() {
        if (flickrViewModel == null) {
            flickrViewModel = ViewModelProviders.of(MainActivity.this).get(FlickrViewModel.class);
        }
        flickrViewModel.searchFlickr(searchTerm);
        flickrViewModel.getListTerms(MainActivity.this).observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> listTerms) {
                listSearchTerm = listTerms;
                setListTerms(listTerms);
            }
        });
    }

    public void observeImages(String item) {
        if (flickrViewModel == null) {
            flickrViewModel = ViewModelProviders.of(MainActivity.this).get(FlickrViewModel.class);
        }
        flickrViewModel.searchFlickr(item);
        listViewSearchTerms.setVisibility(View.GONE);
        flickrViewModel.getData().observe(this, new Observer<Flickr>() {
            @Override
            public void onChanged(Flickr flickr) {
                gridView.setAdapter(new ItemsAdater(MainActivity.this, flickr.getItems()));
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(KEY_SEARCH, searchTerm);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void saveSearchTerms(ArrayList<String> list, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    public boolean hasValidationErrors(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            editTextSearch.setError("Field required!");
            editTextSearch.requestFocus();
            return true;
        }
        return false;
    }

    public void searchTerm(String term) {
        observeImages(term);
        if (listSearchTerm.size() >= 5) {
            listSearchTerm.remove(4);
        }
        if (!listSearchTerm.contains(term))
            listSearchTerm.add(0, term);
        else {
            int index = listSearchTerm.indexOf(term);
            if (index >= 0)
                Collections.swap(listSearchTerm, 0, index);
        }
        saveSearchTerms(listSearchTerm, SEARCH_TERM);
    }
}
