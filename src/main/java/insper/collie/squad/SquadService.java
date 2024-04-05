package insper.collie.squad;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.NonNull;

@Service
public class SquadService {

    @Autowired
    private SquadRepository squadRepository;

    

    public Squad create(Squad in) {

        return squadRepository.save(new SquadModel(in)).to();
    }

    @Transactional(readOnly = true)
    public Squad getSquad(String id) {
        return squadRepository.findById(id).map(SquadModel::to).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Squad> getAllSquads() {
        List<Squad> squads = new ArrayList<Squad>();

        for (SquadModel c : squadRepository.findAll()){
            squads.add(c.to());
        };

        return squads;
    }

    @Transactional
    public Squad update(String id, Squad in) {
        SquadModel c = squadRepository.findById(id).orElse(null);
        if (c == null){
            return null;
        }

        SquadModel squad = c;

        if (in.name() != null) {
            squad.name(in.name());
        }
        if(in.description() != null){
            squad.description(in.description());
        }
        if(in.company_id != null){
            squad.company_id(in.company_id());
        }

        return squadRepository.save(squad).to();
    }

    @Transactional
    public String delete(String id) {
        SquadModel c = squadRepository.findById(id).orElse(null);
        if (c == null){
            return null;
        }
        squadRepository.deleteById(id);
        return "ok";
    }
    
}
