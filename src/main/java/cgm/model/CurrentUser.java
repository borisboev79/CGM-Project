package cgm.model;

import cgm.model.entity.BranchEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


public class CurrentUser extends User {

    private String firstName;
    private String lastName;
    private BranchEntity branch;


    public CurrentUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public String getFirstName() {
        return firstName;
    }

    public CurrentUser setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public CurrentUser setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public BranchEntity getBranch() {
        return branch;
    }

    public CurrentUser setBranch(BranchEntity branch) {
        this.branch = branch;
        return this;
    }
}
