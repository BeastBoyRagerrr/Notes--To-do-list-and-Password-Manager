package core;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Item implements Serializable {
    protected String title;
    protected Date createdAt;
    protected Date updatedAt;

    public Item(String title){
        this.title = title;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public String getTitle(){ return title; }
    public Date getCreatedAt(){ return createdAt; }
    public Date getUpdatedAt(){ return updatedAt; }
    protected void touchUpdated(){ this.updatedAt = new Date(); }

    protected String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        return sdf.format(date);
    }
    public abstract void display();
}
