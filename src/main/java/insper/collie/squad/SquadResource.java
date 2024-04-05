package insper.collie.squad;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class SquadResource implements SquadController {

    @Autowired
    private SquadService squadService;

    @Override
    public ResponseEntity<SquadInfo> create(SquadInfo in) {
        // parser
        Squad squad = SquadParser.to(in);
        // insert using service
        squad = squadService.create(squad);
        // return
        return ResponseEntity.created(
            ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(squad.id())
                .toUri())
            .body(SquadParser.to(squad));
    }


    
}
