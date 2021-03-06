package com.example.sagar.popupshops_buyerside.BuyerRecycleView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sagar.popupshops_buyerside.MapsActivity;
import com.example.sagar.popupshops_buyerside.R;
import com.example.sagar.popupshops_buyerside.Shop.Item;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseEndpoint;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class WishlistRecycleAdapter extends RecyclerView.Adapter<WishlistRecycleAdapter.ItemViewHolder> {

    private static final String TAG = "WishlistRecycle";
    private List<Item> itemList;
    private Context context;

    WishlistRecycleAdapter(List<Item> items, Context context) {
        this.itemList = items;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.display_buyer_recycle, viewGroup, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, int position) {
        itemViewHolder.itemPrice.setText(String.valueOf(itemList.get(position).getItemPrice()));
        itemViewHolder.itemDescr.setText(itemList.get(position).getItemDescription());
        itemViewHolder.itemCategory.setText(itemList.get(position).getItemCategory());
        // itemViewHolder.itemImage.setImageURI(Uri.parse(itemList.get(position).getItemImage()));
        Glide.with(itemViewHolder.itemImage.getContext()).load(itemList.get(position).getItemImage()).into(itemViewHolder.itemImage);

        itemViewHolder.findShop.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.w("here", "" + itemViewHolder.getAdapterPosition());
                Log.w(TAG, itemList.get(itemViewHolder.getAdapterPosition()).toString());
                String shopID = itemList.get(itemViewHolder.getAdapterPosition()).getShopID();
                Log.w(TAG, shopID);
                Query shopLocationQuery = FirebaseUtils.getShopsRef().orderByKey().equalTo(shopID);
                shopLocationQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        double latitude = 0;
                        double longitude = 0;
                        String shopName="";
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Log.w(TAG, "found shop" + snapshot.child(FirebaseEndpoint.SHOPS.LOCATION).toString());
                            latitude = Double.parseDouble(snapshot.child(FirebaseEndpoint.SHOPS.LOCATION).child("latitude").getValue().toString());
                            longitude = Double.parseDouble(snapshot.child(FirebaseEndpoint.SHOPS.LOCATION).child("longitude").getValue().toString());
                            shopName = snapshot.child(FirebaseEndpoint.SHOPS.SHOPNAME).getValue().toString();
                        }

                        // TODO open MAP API and open shop here using location coordinates
                        Intent myIntent = new Intent(view.getContext(), MapsActivity.class);
                        myIntent.putExtra("latitude", latitude);
                        myIntent.putExtra("longitude", longitude);
                        myIntent.putExtra("shopName", shopName);
                        view.getContext().startActivity(myIntent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


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
        TextView itemStock;
        Button findShop;

        ItemViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            itemDescr = (TextView) itemView.findViewById(R.id.itemDescr);
            itemCategory = (TextView) itemView.findViewById(R.id.itemCateg);
            itemPrice = (TextView) itemView.findViewById(R.id.itemPrice);
            itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
            findShop = itemView.findViewById(R.id.findShop);
            itemStock = itemView.findViewById(R.id.itemStock);
        }
    }

}
