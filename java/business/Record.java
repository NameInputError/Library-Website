/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import java.text.NumberFormat;

/**
 *
 * @author Owner
 */
public class Record {
    private int storeId, quantity;
    private String Title, BookId, Author;
    private double price;
    private NumberFormat c = NumberFormat.getCurrencyInstance();
    
    public Record() {
    storeId = 0;
    quantity = 0;
    Title = "";
    BookId = "";
    price = 0;
    Author = "";
    }
    
    public Record(int StoreID, String BookID, String Title, double price, int quantity, String author) {
        this.storeId = StoreID;
        this.BookId = BookID;
        this.Title = Title;
        this.price = price;
        this.quantity = quantity;
        this.Author = author;
    }

    public int getStoreId() {
        return storeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getTitle() {
        return Title;
    }

    public String getBookId() {
        return BookId;
    }

    public String getPrice() {
        return c.format(price);
    }
   
    public String getAuthor() {
        return Author;
    }
}
