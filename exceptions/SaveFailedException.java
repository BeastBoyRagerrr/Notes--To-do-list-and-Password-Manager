package exceptions;

public class SaveFailedException extends Exception{
    public SaveFailedException(String msg){
        super(msg);
    }
    @Override
    public String toString(){
        return "Save Failed: " + getMessage();
    }
}
