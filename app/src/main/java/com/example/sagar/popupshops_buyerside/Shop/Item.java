package com.example.sagar.popupshops_buyerside.Shop;

public class Item {

    private String itemCategory;
    private float itemPrice;
    private String itemDescription;
    private String itemImage;
    private int itemStock;
    private String shopID;
    private String itemID;

    public Item() {

    }

    public Item(String itemCategory, float itemPrice, String itemDescription, String itemImage, int itemStock, String itemID) {
        this.itemCategory = itemCategory;
        this.itemPrice = itemPrice;
        this.itemDescription = itemDescription;
        this.itemImage = itemImage;
        this.itemStock = itemStock;
        this.itemID=itemID;
    }

    public static int getAttributeCount() {
        return 7;
    }

    public float getItemPrice() {
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

    public String getShopID() {
        return shopID;
    }

    public String getItemID() {
        return itemID;
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
