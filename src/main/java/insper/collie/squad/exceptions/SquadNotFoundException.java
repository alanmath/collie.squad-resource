package insper.collie.squad.exceptions;

public class SquadNotFoundException extends RuntimeException{

    public SquadNotFoundException(String id){
        super("Squad not found with id: " + id);
    }


}
