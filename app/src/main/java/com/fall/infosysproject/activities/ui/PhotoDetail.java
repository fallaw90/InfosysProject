package com.fall.infosysproject.activities.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.fall.infosysproject.R;
import com.fall.infosysproject.adapters.ItemsAdater;
import com.fall.infosysproject.models.Flickr;
import com.fall.infosysproject.models.Item;
import com.fall.infosysproject.viewmodels.FlickrViewModel;
import com.master.glideimageview.GlideImageView;

import java.io.IOException;
import java.net.URL;

public class PhotoDetail extends AppCompatActivity {

    private Item item;
    private GlideImageView imageViewItem;
    private TextView textViewTitle;
    private TextView textViewDescription;
    private TextView textViewWidth;
    private TextView textViewHeight;
    private TextView textViewAuthor;
    private String heightImage = "";
    private String widthImage = "";
    private ItemsAdater.DetailsViewModel viewModel = new ItemsAdater.DetailsViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        initViews();

        Intent intentItem = getIntent();
        item = (Item) intentItem.getSerializableExtra("item");
        if (item != null) {
            setDetails(item);
        }

        viewModel = ViewModelProviders.of(PhotoDetail.this).get(ItemsAdater.DetailsViewModel.class);
        viewModel.getItemLiveData().observe(this, new Observer<Item>() {
            @Override
            public void onChanged(Item item) {
                setDetails(item);
            }
        });
    }

    private void initViews() {
        imageViewItem = (GlideImageView) findViewById(R.id.iv_item);
        textViewTitle = (TextView) findViewById(R.id.tv_title);
        textViewDescription = (TextView) findViewById(R.id.tv_description);
        textViewWidth = (TextView) findViewById(R.id.tv_image_width);
        textViewHeight = (TextView) findViewById(R.id.tv_image_height);
        textViewAuthor = (TextView) findViewById(R.id.tv_author);
    }

    private void setDetails(Item item) {
        imageViewItem.loadImageUrl(item.getMedia().getM());
        textViewTitle.setText("Title: " + item.getTitle());
        textViewDescription.setText("Description: " + Html.fromHtml(item.getDescription()));
        textViewAuthor.setText("Author: " + item.getAuthor());
        new BackgroundImageSize().execute();
    }

    //*********************************** Get Image Size ********************************************
    @SuppressLint("StaticFieldLeak")
    public class BackgroundImageSize extends AsyncTask<Void, Void, Void> {
        private Bitmap mBitmap;
        private URL url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (mBitmap == null) {
                try {
                    url = new URL(item.getMedia().getM());
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    heightImage = String.valueOf(bmp.getHeight());
                    widthImage = String.valueOf(bmp.getWidth());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            textViewWidth.setText("Width: " + widthImage);
            textViewHeight.setText("Height: " + heightImage);
            super.onPostExecute(aVoid);
        }
    }
}
