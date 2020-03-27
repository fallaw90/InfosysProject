package com.fall.infosysproject.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fall.infosysproject.models.Flickr;
import com.fall.infosysproject.repositories.FlickrRepository;

import java.util.ArrayList;

public class FlickrViewModel extends ViewModel {

    private MutableLiveData<Flickr> data;
    private FlickrRepository flickrRepository;

    public FlickrViewModel(Context context) {
        flickrRepository = new FlickrRepository(context);
    }

    public void searchFlickr(String searchItem) {
        data = (MutableLiveData<Flickr>) flickrRepository.getData(searchItem);
    }

    public LiveData<Flickr> getData() {
        return this.data;
    }

    public LiveData<ArrayList<String>> getListTerms(Context context) {
        return flickrRepository.execGetSearchTerms(context);
    }

}
