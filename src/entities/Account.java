
package entities;
import java.io.Serializable;
import static java.util.Collections.list;
import java.util.List;
import javax.persistence.*;


@Entity
@Table(name="account")
public class Account implements Serializable {
    
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    @Column
    private int id;
    
    @Column (name="fullname")
    private String fullname;
    
    @Column(name="username")
    private String username;
    
    @Column(name="password")
    private String password;
    
    @Column(name="email")
    private String email;
    
    @Column(name="admin")
    private boolean admin = false;
    
    @Column(name="blocked")
    private boolean blocked = false;
    
    @OneToMany(mappedBy = "user")
    private List<Attend> attend;
    
    

    public Account(int id, String fullname, String username, String password, String email, boolean admin, boolean blocked, List<Attend> attend) {
        this.id = id;
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.admin = admin;
        this.blocked = blocked;
        this.attend = attend;
    }

    public Account(String fullname, String username, String password, String email) {
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.email = email;
    }
    
    

    public Account() {
    }

    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public List<Attend> getAttend() {
        return attend;
    }

    public void setAttend(List<Attend> attend) {
        this.attend = attend;
    }

    

     
}
