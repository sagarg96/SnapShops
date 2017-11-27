package com.example.sagar.popupshops_buyerside;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sagar.popupshops_buyerside.Shop.Item;
import com.example.sagar.popupshops_buyerside.Shop.recycle;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseEndpoint;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

@Layout(R.layout.tinder_card_view)
public class ItemCard {

    @View(R.id.profileImageView)
    private ImageView profileImageView;

    @View(R.id.nameAgeTxt)
    private TextView itemDetail;

    @View(R.id.shopName)
    private Button shopName;

    private Item mItem;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;

    public ItemCard(Context context, Item item, SwipePlaceHolderView swipeView) {
        mContext = context;
        mItem = item;
        mSwipeView = swipeView;
    }

    @Resolve
    private void onResolved() {
        Glide.with(mContext).load(mItem.getItemImage()).into(profileImageView);
        itemDetail.setText(mItem.getItemDescription() + ", $" + mItem.getItemPrice());
        DatabaseReference shopReference = FirebaseUtils.getShopsRef().child(mItem.getShopID()).child(FirebaseEndpoint.SHOPS.SHOPNAME);
        Log.w("here", mItem.getShopID());
        shopReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w("here", dataSnapshot.exists() + "");
                if (dataSnapshot.exists()) {
                    shopName.setText(dataSnapshot.getValue().toString());
                    shopName.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(android.view.View view) {
                            Intent intent = new Intent(mContext, recycle.class);
                            intent.putExtra("shopID", mItem.getShopID());
                            mContext.startActivity(intent);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @SwipeOut
    private void onSwipedOut() {
        Log.d("EVENT", "onSwipedOut");
        //mSwipeView.addView(this);
        DatabaseReference wishListReference = FirebaseUtils.getWishListRef();
        wishListReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(mItem.getItemID()).setValue(null);
    }

    @SwipeCancelState
    private void onSwipeCancelState() {
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn() {
        Log.d("EVENT", "onSwipedIn");
        DatabaseReference wishListReference = FirebaseUtils.getWishListRef();
        wishListReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(mItem.getItemID()).setValue(mItem);
    }

    @SwipeInState
    private void onSwipeInState() {
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState() {
        Log.d("EVENT", "onSwipeOutState");
    }
}
