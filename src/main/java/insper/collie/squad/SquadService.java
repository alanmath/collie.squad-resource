package insper.collie.squad;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.NonNull;

@Service
public class SquadService {

    @Autowired
    private SquadRepository squadRepository;

    public Squad create(Squad in) {
        return squadRepository.save(new SquadModel(in)).to();
    }
    
}
