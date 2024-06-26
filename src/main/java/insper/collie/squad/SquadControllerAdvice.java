package insper.collie.squad;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import insper.collie.account.exceptions.AccountNotFoundException;
import insper.collie.company.exceptions.CompanyNotFoundException;
import insper.collie.squad.exceptions.RequestErrorException;
import insper.collie.squad.exceptions.SquadNotFoundException;

@ControllerAdvice
public class SquadControllerAdvice extends ResponseEntityExceptionHandler{

    @ExceptionHandler({CompanyNotFoundException.class, AccountNotFoundException.class})
    private ResponseEntity<String> badRequestHandler(RuntimeException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler({SquadNotFoundException.class})
    private ResponseEntity<String> notFoundHandler(RuntimeException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({RequestErrorException.class})
    private ResponseEntity<String> requestErrorHandler(RuntimeException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
