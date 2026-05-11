package core;

public class PasswordItem extends Item {
    private String username;
    private String password;
    public PasswordItem(String title, String username, String password){
        super(title);
        this.username = username == null ? "" : username;
        this.password = password == null ? "" : password;
    }
    public String getUsername(){ return username; }
    public String getPassword(){ return password; }
    public void setUsername(String u){ this.username = u; touchUpdated(); }
    public void setPassword(String p){ this.password = p; touchUpdated(); }
    @Override
    public String toString(){
        return "Account: "+title+"\nUsername: "+username+"\nPassword: "+password+"\nCreated: "+formatDate(createdAt)+"\nUpdated: "+formatDate(updatedAt);
    }
    @Override
    public void display(){ System.out.println(this.toString()); }
}
