package insper.collie.squad.exceptions;


public class CompanyNotFoundException extends RuntimeException{
    
    public CompanyNotFoundException(String id){
        super("Company not found with id: " + id);
    }
}
