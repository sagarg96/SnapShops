package com.example.sagar.popupshops_buyerside.Shop;

import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sagar.popupshops_buyerside.R;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ItemViewHolder> {

    private List<Item> itemList;

    RVAdapter(List<Item> items) {
        this.itemList = items;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.display, viewGroup, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int position) {
        itemViewHolder.itemPrice.setText(String.valueOf(itemList.get(position).getItemPrice()));
        itemViewHolder.itemDescr.setText(itemList.get(position).getItemDescription());
        itemViewHolder.itemCategory.setText(itemList.get(position).getItemCategory());
       // itemViewHolder.itemImage.setImageURI(Uri.parse(itemList.get(position).getItemImage()));
        Glide.with(itemViewHolder.itemImage.getContext()).load(itemList.get(position).getItemImage()).into(itemViewHolder.itemImage);

        itemViewHolder.deleteButton.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO add deletion mechanism
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView itemDescr;
        TextView itemCategory;
        TextView itemPrice;
        ImageView itemImage;
        ImageButton deleteButton;

        ItemViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            itemDescr = (TextView) itemView.findViewById(R.id.itemDescr);
            itemCategory = (TextView) itemView.findViewById(R.id.itemCateg);
            itemPrice = (TextView) itemView.findViewById(R.id.itemPrice);
            itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}