package com.example.sagar.popupshops_buyerside.Shop;

/**
 * Created by Fawad Masood Desmukh on 11/5/2017.
 */

public class Item {

    private String itemCategory;
    private int itemPrice;
    private String itemDescription;
    private String itemImage;
    private int itemStock;

    public Item() {

    }

    public Item(String itemCategory, int itemPrice, String itemDescription, String itemImage, int itemStock) {
        this.itemCategory = itemCategory;
        this.itemPrice = itemPrice;
        this.itemDescription = itemDescription;
        this.itemImage = itemImage;
        this.itemStock = itemStock;
    }

    public static int getAttributeCount() {
        return 6;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemImage() {
        return itemImage;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public int getItemStock() {
        return itemStock;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemCategory='" + itemCategory + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemDescription='" + itemDescription + '\'' +
                ", itemImage='" + itemImage + '\'' +
                ", itemStock=" + itemStock +
                '}';
    }
}
