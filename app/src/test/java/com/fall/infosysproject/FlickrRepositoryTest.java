package com.fall.infosysproject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fall.infosysproject.models.Flickr;
import com.fall.infosysproject.repositories.FlickrRepository;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class FlickrRepositoryTest {

    MutableLiveData<Flickr> flickrMutableLiveData = new MutableLiveData<>() ;

    @Test
    public void test_requestingOject_returnFlickerType(){
        FlickrRepository flickrRepository = Mockito.mock(FlickrRepository.class);
        Mockito.when(flickrRepository.getData("dog")).thenReturn(flickrMutableLiveData);
        Flickr flickr = flickrRepository.getData("dog").getValue();
        //Assert.assertArrayEquals("Request success", flickr.getItems());
    }
}
