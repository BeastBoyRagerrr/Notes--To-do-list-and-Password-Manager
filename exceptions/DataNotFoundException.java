package exceptions;

public class DataNotFoundException extends Exception{
    public DataNotFoundException(String msg){
        super(msg);
    }
    @Override
    public String toString(){
        return "Data Not Found: " + getMessage();
    }
}
