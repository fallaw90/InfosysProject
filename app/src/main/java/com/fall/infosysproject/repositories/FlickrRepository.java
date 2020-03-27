package com.fall.infosysproject.repositories;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fall.infosysproject.models.Flickr;
import com.fall.infosysproject.services.ApiClient;
import com.fall.infosysproject.services.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlickrRepository {

    private static final String TAG = "FlickrRepository";
    private static final String SEARCH_TERM = "SEARCH_TERM";

    private MutableLiveData<Flickr> flickrLiveData = new MutableLiveData<>();
    private ApiInterface apiInterface;
    private final ProgressDialog progressDialog;
    private MutableLiveData<ArrayList<String>> listTerms = new MutableLiveData<>();;

    public FlickrRepository(Context context) {
        progressDialog = new ProgressDialog(context);
    }

    public LiveData<Flickr> getData(String searchItem) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Flickr> call = apiInterface.getData("https://api.flickr.com/services/feeds/photos_public.gne?nojsoncallback=1&tagmode=any&format=json&tags=" + searchItem);
        showProgressBar();
        call.enqueue(new Callback<Flickr>() {
            @Override
            public void onResponse(Call<Flickr> call, Response<Flickr> response) {
                if (response.body() != null) {
                    flickrLiveData.postValue(response.body());
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Flickr> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                progressDialog.dismiss();
            }
        });

        return flickrLiveData;
    }

    public LiveData<ArrayList<String>> getSearchTerms(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(SEARCH_TERM, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();

        ArrayList<String> arrayList = gson.fromJson(json, type);
        if (arrayList != null)
            listTerms.postValue(arrayList);

        return listTerms;
    }

    public LiveData<ArrayList<String>> execGetSearchTerms(Context context){
        BackgroundGetSharedPreference bgsp = new BackgroundGetSharedPreference(context);
        return bgsp.doInBackground();
    }


    public void showProgressBar(){
        // Set up progress before call
        progressDialog.setMax(100);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show progress bar
        progressDialog.show();
    }

    //*********************************** Get Image Size ********************************************
    @SuppressLint("StaticFieldLeak")
    public class BackgroundGetSharedPreference extends AsyncTask<Void, Void, LiveData<ArrayList<String>>> {

        Context context;
        public BackgroundGetSharedPreference(Context context){
            this.context = context;
        }

        @Override
        protected LiveData<ArrayList<String>> doInBackground(Void... voids) {
            return getSearchTerms(this.context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }
}
