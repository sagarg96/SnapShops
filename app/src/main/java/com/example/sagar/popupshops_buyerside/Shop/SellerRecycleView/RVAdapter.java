package com.example.sagar.popupshops_buyerside.Shop.SellerRecycleView;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sagar.popupshops_buyerside.R;
import com.example.sagar.popupshops_buyerside.Shop.Item;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseUtils;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> itemList;
    private boolean self;

    RVAdapter(List<Item> items, boolean self) {
        this.itemList = items;
        this.self = self;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case 0: {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.display, viewGroup, false);
                return new ItemViewHolder(v);
            }
            case 1: {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.display2, viewGroup, false);
                return new ItemViewHolderWithoutDelete(v);
            }
            default:{
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.display, viewGroup, false);
                return new ItemViewHolder(v);
            }
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        switch (viewHolder.getItemViewType()) {
            case 0: {
                ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
                itemViewHolder.itemPrice.setText(String.valueOf(itemList.get(position).getItemPrice()));
                itemViewHolder.itemDescr.setText(itemList.get(position).getItemDescription());
                itemViewHolder.itemCategory.setText(itemList.get(position).getItemCategory());
                itemViewHolder.itemStock.setText(String.valueOf(itemList.get(position).getItemStock()));
                Glide.with(itemViewHolder.itemImage.getContext()).load(itemList.get(position).getItemImage()).into(itemViewHolder.itemImage);

                itemViewHolder.deleteButton.setOnClickListener(new FloatingActionButton.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.w("here", "" + viewHolder.getAdapterPosition());
                        FirebaseUtils.getItemRef().child(itemList.get(viewHolder.getAdapterPosition()).getItemID()).setValue(null);

                    }
                });
                break;
            }
            case 1: {
                ItemViewHolderWithoutDelete itemViewHolder = (ItemViewHolderWithoutDelete) viewHolder;
                itemViewHolder.itemPrice.setText(String.valueOf(itemList.get(position).getItemPrice()));
                itemViewHolder.itemDescr.setText(itemList.get(position).getItemDescription());
                itemViewHolder.itemCategory.setText(itemList.get(position).getItemCategory());
                itemViewHolder.itemStock.setText(String.valueOf(itemList.get(position).getItemStock()));
                Glide.with(itemViewHolder.itemImage.getContext()).load(itemList.get(position).getItemImage()).into(itemViewHolder.itemImage);
                break;
            }

        }


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (self) {
            return 0;
        } else {
            return 1;
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView itemDescr;
        TextView itemCategory;
        TextView itemPrice;
        TextView itemStock;
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
            itemStock = itemView.findViewById(R.id.itemStock);
        }
    }

    public static class ItemViewHolderWithoutDelete extends RecyclerView.ViewHolder {

        CardView cv;
        TextView itemDescr;
        TextView itemCategory;
        TextView itemPrice;
        TextView itemStock;
        ImageView itemImage;

        ItemViewHolderWithoutDelete(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            itemDescr = (TextView) itemView.findViewById(R.id.itemDescr);
            itemCategory = (TextView) itemView.findViewById(R.id.itemCateg);
            itemPrice = (TextView) itemView.findViewById(R.id.itemPrice);
            itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
            itemStock = itemView.findViewById(R.id.itemStock);
        }
    }
}