package insper.collie.squad;



import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SquadRepository extends CrudRepository<SquadModel, String> {

    // SquadModel[] findByCompanyId(String id);


}
