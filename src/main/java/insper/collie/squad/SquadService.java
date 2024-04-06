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
import insper.collie.account.AccountController;
import insper.collie.account.AccountOut;
import insper.collie.company.CompanyController;
import insper.collie.company.CompanyInfo;


@Service
public class SquadService {

    @Autowired
    private SquadRepository squadRepository;

    @Autowired
    private CompanyController companyController;

    @Autowired
    private AccountController accountController;

    public Squad create(Squad in) {

        ResponseEntity<Boolean> responseCompany = companyController.isCompany(in.company_id());

        // verifica se a empresa de fato existe
        if (responseCompany != null){
            if (!responseCompany.getBody()){
                return null;
            }
        }else return null;

        ResponseEntity<Boolean> responseAccount = accountController.isAccount(in.manager_id());

        // verifica se a empresa de fato existe
        if (responseAccount != null){
            if (!responseAccount.getBody()){
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

        ResponseEntity<AccountOut> responseA = accountController.getAccount(squad.manager_id());
        if (responseA == null) return null;
        
        AccountOut manager = responseA.getBody();

        SquadAllInfo squadAll = SquadParser.toAll(squad, company, manager);
        
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
        if(in.manager_id() != null){
            ResponseEntity<Boolean> responseA = accountController.isAccount(in.manager_id());

            // verifica se a empresa de fato existe
            if (responseA != null){
                if (!responseA.getBody()){
                    return null;
                }
            }else return null;
            squad.manager_id(in.manager_id());
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
