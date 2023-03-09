package cgm.model;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


public class GrouManUser extends User {


    private String firstName;

    private String lastName;

    private String branch;


    public GrouManUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public String getFirstName() {
        return firstName;
    }

    public GrouManUser setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public GrouManUser setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getBranch() {
        return branch;
    }

    public GrouManUser setBranch(String branch) {
        this.branch = branch;
        return this;
    }
}
