package core;

public class Note extends Item {
    private String content;
    public Note(String title, String content){
        super(title);
        this.content = content == null ? "" : content;
    }
    public String getContent(){ return content; }
    public void setContent(String content){ this.content = content; touchUpdated(); }
    @Override
    public String toString(){
        return "Title: "+title+"\nContent: "+content+"\nCreated: "+formatDate(createdAt)+"\nUpdated: "+formatDate(updatedAt);
    }
    @Override
    public void display(){ System.out.println(this.toString()); }
}
