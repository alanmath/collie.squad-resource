package insper.collie.squad.exceptions;


public class AccountNotFoundException extends RuntimeException{
    
    public AccountNotFoundException(String id){
        super("Account not found with id: " + id);
    }
}
