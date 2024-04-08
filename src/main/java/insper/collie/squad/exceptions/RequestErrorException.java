package insper.collie.squad.exceptions;

public class RequestErrorException extends RuntimeException{

    public RequestErrorException(String api){
        super("Request error in" + api + " API.");
    }

}
