package core;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TodoItem extends Item {
    private String description;
    private Date deadline;
    private int userPriority; // 1..5
    private boolean forceHighPriority;

    public TodoItem(String title, String description, Date deadline, int userPriority, boolean forceHighPriority){
        super(title);
        this.description = description == null ? "" : description;
        this.deadline = deadline == null ? new Date() : deadline;
        this.userPriority = Math.max(1, Math.min(5, userPriority));
        this.forceHighPriority = forceHighPriority;
    }

    public String getDescription(){ return description; }
    public Date getDeadline(){ return deadline; }
    public int getUserPriority(){ return userPriority; }
    public boolean isForceHighPriority(){ return forceHighPriority; }
    public void setDescription(String d){ this.description = d; touchUpdated(); }
    public void setDeadline(Date d){ this.deadline = d; touchUpdated(); }
    public void setUserPriority(int p){ this.userPriority = Math.max(1, Math.min(5, p)); touchUpdated(); }
    public void setForceHighPriority(boolean f){ this.forceHighPriority = f; touchUpdated(); }

    // dynamic urgency score
    public double getPriorityScore(){
        if (forceHighPriority) return 1_000_000;
        long now = System.currentTimeMillis();
        long diff = deadline.getTime() - now;
        double daysLeft = diff / (1000.0*60*60*24);
        double base = userPriority * 10.0;
        double urgency;
        if (daysLeft <= 0) urgency = 100.0;
        else urgency = 1.0 + (7.0/(daysLeft + 1.0));
        return base * urgency;
    }

    public long getDaysRemaining(){
        long diff = deadline.getTime() - System.currentTimeMillis();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public String toString(){
        return "Task: "+title+
               "\nDescription: "+description+
               "\nDeadline: "+formatDate(deadline)+
               "\nUserPriority: "+userPriority+(forceHighPriority?" (FORCED HIGH)":"")+
               "\nDays remaining: "+getDaysRemaining()+
               "\nCreated: "+formatDate(createdAt)+
               "\nUpdated: "+formatDate(updatedAt);
    }

    @Override
    public void display(){ System.out.println(this.toString()); }
}
