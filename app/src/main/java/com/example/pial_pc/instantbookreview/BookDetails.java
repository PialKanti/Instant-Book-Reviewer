package com.example.pial_pc.instantbookreview;


import java.io.Serializable;
import java.io.SerializablePermission;

/**
 * Created by Pial-PC on 1/4/2016.
 */
public class BookDetails {
    String itemTitle, itemAuthor, itemDescription, itemRating, itemLanguage, itemReview;

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemReview() {
        return itemReview;
    }

    public void setItemReview(String itemReview) {
        this.itemReview = itemReview;
    }

    public String getItemLanguage() {
        return itemLanguage;
    }

    public void setItemLanguage(String itemLanguage) {
        this.itemLanguage = itemLanguage;
    }

    public String getItemRating() {
        return itemRating;
    }

    public void setItemRating(String itemRating) {
        this.itemRating = itemRating;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemAuthor() {
        return itemAuthor;
    }

    public void setItemAuthor(String itemAuthor) {
        this.itemAuthor = itemAuthor;
    }
}
