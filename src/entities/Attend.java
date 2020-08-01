package entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="attend")
public class Attend implements Serializable {
    
    @EmbeddedId
    private AttendPK id;
    
    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private Account user;
    
    @ManyToOne
    @MapsId("conf_id")
    @JoinColumn(name = "conf_id")
    private Conference conference;
    
    @Column(name = "accepted")
    private boolean accepted;

    public Attend() {
    }

    public Attend(AttendPK id, Account user, Conference conference) {
        this.id = id;
        this.user = user;
        this.conference = conference;
    }

    public Attend(Account user, Conference conference, boolean accepted) {
        this.user = user;
        this.conference = conference;
        this.accepted = accepted;
    }
    
    public AttendPK getId() {
        return id;
    }

    public void setId(AttendPK id) {
        this.id = id;
    }

    public Account getUser() {
        return user;
    }

    public void setUser(Account user) {
        this.user = user;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
  
}
