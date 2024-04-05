package insper.collie.squad;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public ResponseEntity<SquadInfo> getSquad(String id){

        Squad squad = squadService.getSquad(id);
        if (squad == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(SquadParser.to(squad));
    }

    @Override
    public ResponseEntity<List<SquadInfo>> getAllSquads(){

        List<Squad> squads = squadService.getAllSquads();
        if (squads.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
            squads.stream()
                .map(SquadParser::to)
                .collect(Collectors.toList())
        );
    }

    @Override
    public ResponseEntity<SquadInfo> updateSquad(String id, SquadInfo in){

        Squad Squad = SquadParser.to(in);
        Squad = squadService.update(id, Squad);
        if (Squad == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(SquadParser.to(Squad));
    }

    @Override
    public ResponseEntity<SquadInfo> deleteSquad(String id){

        String r = squadService.delete(id);
        if (r == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    
}
