package insper.collie.squad;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.NonNull;

import insper.collie.company.CompanyController;
import insper.collie.company.CompanyInfo;


@Service
public class SquadService {

    @Autowired
    private SquadRepository squadRepository;

    @Autowired
    private CompanyController companyController;

    public Squad create(Squad in) {

        ResponseEntity<Boolean> response = companyController.isCompany(in.company_id());

        // verifica se a empresa de fato existe
        if (response != null){
            if (!response.getBody()){
                return null;
            }
        }else return null;


        return squadRepository.save(new SquadModel(in)).to();
    }

    @Transactional(readOnly = true)
    public SquadAllInfo getSquad(String id) {
        Squad squad = squadRepository.findById(id).map(SquadModel::to).orElse(null);
        if (squad == null) return null;

        ResponseEntity<CompanyInfo> response = companyController.getCompany(squad.company_id());
        if (response == null) return null;
        
        CompanyInfo company = response.getBody();
        SquadAllInfo squadAll = SquadParser.toAll(squad, company);
        
        return squadAll;
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
        if(in.company_id() != null){
            ResponseEntity<Boolean> response = companyController.isCompany(in.company_id());

            // verifica se a empresa de fato existe
            if (response != null){
                if (!response.getBody()){
                    return null;
                }
            }else return null;
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
