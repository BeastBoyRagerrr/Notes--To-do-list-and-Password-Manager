package exceptions;

public class InvalidChoiceException extends Exception{
    public InvalidChoiceException(String msg){
        super(msg);
    }
    @Override
    public String toString(){
        return "Invalid Choice: " + getMessage();
    }
}
