package insper.collie.squad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "squad")
@EqualsAndHashCode(of = "id")
@Builder @Getter @Setter @Accessors(chain = true, fluent = true)
@NoArgsConstructor @AllArgsConstructor
public class SquadModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private String id;

    @Column
    String name;

    @Column
    String description;
    
    @Column
    String company_id;
    
    @Column
    String manager_id;   

    public SquadModel(Squad o) {
        this.id = o.id();
        this.name = o.name();
        this.description = o.description();
        this.company_id = o.company_id();
        this.manager_id = o.manager_id();
    }
    
    public Squad to() {
        return Squad.builder()
            .id(id)
            .name(name)
            .description(description)
            .company_id(company_id)
            .manager_id(manager_id)
            .build();
    }
}
