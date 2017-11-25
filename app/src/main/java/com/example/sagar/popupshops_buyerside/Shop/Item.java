package com.example.sagar.popupshops_buyerside.Shop;

public class Item {

    private String itemCategory;
    private float itemPrice;
    private String itemDescription;
    private String itemImage;
    private int itemStock;
    private String shopID;

    public Item() {

    }

    public Item(String itemCategory, float itemPrice, String itemDescription, String itemImage, int itemStock) {
        this.itemCategory = itemCategory;
        this.itemPrice = itemPrice;
        this.itemDescription = itemDescription;
        this.itemImage = itemImage;
        this.itemStock = itemStock;
    }

    public static int getAttributeCount() {
        return 6;
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
