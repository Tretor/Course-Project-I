package sec.project.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.AbstractPersistable;


@Entity
@Table(name = "pcpart")
public class Signup extends AbstractPersistable<Long> {

    @Column(name = "PCPART_NAME")
    private String name;
    @Column(name = "COMPANY")
    private String company;
    @Column(name = "MASTER_CODE")
    private String masterCode;

    public Signup() {
        super();
    }

public Signup(String name, String company, String secret) {
        this();
        this.name = name;
        this.company = company;
        this.masterCode = secret;
}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setSecretCode(String secret) {
        this.masterCode = secret;
        
    }
    
     public String getSecretCode() {
        
        return this.masterCode;
    }
    
     @Override
    public String toString(){
        
        String text = this.name + ", " + this.company;
        return text;
    
    }
}

