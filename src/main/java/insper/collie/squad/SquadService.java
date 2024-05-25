package insper.collie.squad;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import insper.collie.account.AccountController;
import insper.collie.account.AccountOut;
import insper.collie.company.CompanyController;
import insper.collie.company.CompanyInfo;
import insper.collie.account.exceptions.AccountNotFoundException;
import insper.collie.company.exceptions.CompanyNotFoundException;
import insper.collie.squad.exceptions.RequestErrorException;
import insper.collie.squad.exceptions.SquadNotFoundException;
// import commons

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


@Service
public class SquadService {

    @Autowired
    private SquadRepository squadRepository;

    @Autowired
    private CompanyController companyController;

    @Autowired
    private AccountController accountController;

    @CachePut(value="squads", key = "#result.id")
    public Squad create(Squad in) {

        ResponseEntity<Boolean> responseCompany = companyController.isCompany(in.company_id());

        // verifica se a empresa de fato existe
        if (responseCompany != null){
            if (!responseCompany.getBody()){
                throw new CompanyNotFoundException(in.company_id());
            }
        }else throw new RequestErrorException("Company");

        ResponseEntity<Boolean> responseAccount = accountController.isAccount(in.manager_id());

        // verifica se a empresa de fato existe
        if (responseAccount != null){
            if (!responseAccount.getBody()){
                throw new AccountNotFoundException(in.manager_id());
            }
        }else throw new RequestErrorException("Account");


        return squadRepository.save(new SquadModel(in)).to();
    }

    @Cacheable(value="squads", key="#id")
    @Transactional(readOnly = true)
    public Squad getSquad(String id) {
        Squad squad = squadRepository.findById(id).map(SquadModel::to).orElse(null);
        if (squad == null) throw new SquadNotFoundException(id);
        
        return squad;
    }

    @Transactional(readOnly = true)
    public Boolean isSquad(String id){
        return squadRepository.existsById(id);
    }


    @Transactional(readOnly = true)
    public List<Squad> getAllSquads() {
        List<Squad> squads = new ArrayList<Squad>();

        for (SquadModel c : squadRepository.findAll()){
            squads.add(c.to());
        };

        return squads;
    }

    @CachePut(value="squads", key = "#id", unless = "#result == null")
    @Transactional
    public Squad update(String id, Squad in) {
        SquadModel c = squadRepository.findById(id).orElse(null);
        if (c == null) throw new SquadNotFoundException(id);

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
                    throw new CompanyNotFoundException(in.manager_id());
                }
            }else throw new RequestErrorException("Company");
            squad.company_id(in.company_id());
        }
        if(in.manager_id() != null){
            ResponseEntity<Boolean> responseA = accountController.isAccount(in.manager_id());

            // verifica se a empresa de fato existe
            if (responseA != null){
                if (!responseA.getBody()){
                    throw new AccountNotFoundException(in.manager_id());
                }
            }else throw new RequestErrorException("Account");
            squad.manager_id(in.manager_id());
        }

        return squadRepository.save(squad).to();
    }


    @CacheEvict(value="squads", key = "#id", condition = "#result != null")
    @Transactional
    public void delete(String id) {

        // SquadModel c = squadRepository.findById(id).orElse(null);
        if (squadRepository.existsById(id)) squadRepository.deleteById(id);
        throw new SquadNotFoundException(id);
    }

    @Transactional
    public List<SquadInfo> registerSquadsFromCSV(MultipartFile file) {
        List<SquadInfo> registeredSquadsInfo = new ArrayList<>();
        try (CSVParser csvParser = new CSVParser(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8), 
                CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord record : csvRecords) {
                String name = record.get("Name");
                String description = record.get("Description");

                SquadModel newSquad = new SquadModel(); 
                newSquad.name(name);
                newSquad.description(description);

                SquadModel savedSquad = squadRepository.save(newSquad);
                registeredSquadsInfo.add(SquadParser.to(savedSquad.to()));
            }
            return registeredSquadsInfo;
        } catch (Exception e) {
            throw new RuntimeException("Falha ao processar o arquivo CSV", e);
        }
    }

    public List<Squad> getSquadsByCompany(String id)  {
        List<Squad> squads = new ArrayList<Squad>();

        // get all squads from company
        for (SquadModel c : squadRepository.findAll()){
            if (c.company_id().equals(id)){
                squads.add(c.to());
            }
        };

        return squads;
    }
    
    
}
