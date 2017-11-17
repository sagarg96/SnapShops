package com.example.sagar.popupshops_buyerside.Shop;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sagar.popupshops_buyerside.R;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseEndpoint;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class ItemCatalogue extends AppCompatActivity {

    private ItemAdapter itemAdapter;
    private DatabaseReference itemReference;
    private RecyclerView itemRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_catalogue);

        itemRecycler = (RecyclerView) findViewById(R.id.recycler_items);

        itemRecycler.setLayoutManager(new LinearLayoutManager(this));
        itemRecycler.setHasFixedSize(true);


//        itemReference= FirebaseUtils.getItemRef()
    }

    @Override
    public void onStart() {
        super.onStart();

        itemAdapter = new ItemAdapter(this);
        itemRecycler.setAdapter(itemAdapter);

    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView itemDescr;
        TextView itemCategory;
        TextView itemPrice;
//        ImageView itemImage;

        public ItemViewHolder(View itemView) {
            super(itemView);

            itemDescr = (TextView) itemView.findViewById(R.id.item_Descr);
            itemCategory = (TextView) itemView.findViewById(R.id.item_Cat);
            itemPrice = (TextView) itemView.findViewById(R.id.item_Price);
//            itemImage = (ImageView) itemView.findViewById(R.id.item_Image);

        }

    }

    private static class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        private static final String TAG = "ItemAdapter";
        private Context mContext;
        private DatabaseReference databaseReference;
        private List<Item> items = new ArrayList<>();

        public ItemAdapter(final Context context/*, DatabaseReference ref*/) {
            mContext = context;
//            databaseReference = ref;

            FirebaseUtils.getCurrentShopID(new FirebaseUtils.Callback() {
                @Override
                public void OnComplete(String value) {
                    final Query itemQuery = FirebaseUtils.getItemRef().orderByChild(FirebaseEndpoint.ITEMS.SHOPID).equalTo(value);
                    itemQuery.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot itemSnapshot, String s) {
                            Log.w("children count", "" + itemSnapshot.getChildrenCount());
                            if (itemSnapshot.getChildrenCount() == Item.getAttributeCount()) {
                                boolean added = items.add(
                                        new Item
                                                (
                                                        itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMCATEGORY).getValue().toString(),
                                                        Integer.parseInt(itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMPRICE).getValue().toString()),
                                                        itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMDESCRIPTION).getValue().toString(),
                                                        itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMIMAGE).getValue().toString(),
                                                        Integer.parseInt(itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMSTOCK).getValue().toString())
                                                )
                                );

                                Log.w(TAG, String.valueOf(added));
                            }

                            notifyItemInserted(items.size() - 1);

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });

                }
            });


        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_card, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            holder.itemPrice.setText(String.valueOf(items.get(position).getItemPrice()));
            holder.itemDescr.setText(items.get(position).getItemDescription());
            holder.itemCategory.setText(items.get(position).getItemCategory());
//            holder.itemImage.setImageURI(Uri.parse(items.get(position).getItemImage()));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }


    }
}
