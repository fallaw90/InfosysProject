package com.fall.infosysproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fall.infosysproject.R;
import com.fall.infosysproject.activities.ui.PhotoDetail;
import com.fall.infosysproject.models.Item;
import com.master.glideimageview.GlideImageView;

import java.util.List;

public class ItemsAdater extends BaseAdapter {

    private List<Item> listItems;
    private Context context;
    static MutableLiveData<Item> itemLiveData;

    public ItemsAdater(Context context, List<Item> listItems) {
        this.context = context;
        this.listItems = listItems;
        itemLiveData = new MutableLiveData<>();
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolderItem holder = null;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_gridview_item, parent, false);
            holder = new ViewHolderItem(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolderItem) row.getTag();
        }

        final Item item = listItems.get(position);

        holder.imageViewItem.loadImageUrl(item.getMedia().getM());
        holder.TextViewTitleImage.setText(item.getTitle());
        this.itemLiveData.setValue(item);

        holder.imageViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PhotoDetail.class);
                i.putExtra("item", item);
                context.startActivity(i);
            }
        });

        return row;
    }

    public class ViewHolderItem {
        public GlideImageView imageViewItem;
        public TextView TextViewTitleImage;

        ViewHolderItem(View v) {
            imageViewItem = v.findViewById(R.id.iv_item);
            TextViewTitleImage = v.findViewById(R.id.tv_title);
        }
    }

    public static class DetailsViewModel extends ViewModel {

        public DetailsViewModel() {
        }

        public LiveData<Item> getItemLiveData() {
            return itemLiveData;
        }

    }
}

