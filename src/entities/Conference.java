/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name="conference")
public class Conference implements Serializable {
    
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id")
    private int id;
    
    @Column(name="name")
    private String name;
    
    @Column(name="description_short")
    private String description_short;
    
    @Column(name="description_long")
    private String description_long;
    
    @Column(name="avatar")
    private byte[] avatar;
    
    @Column(name="time_start")
    private Timestamp time_start;
    
    @Column(name="time_end")
    private Timestamp time_end;
    
    @Column(name="attendees")
    private int attendees;
    
    @ManyToOne
    @JoinColumn(name="location_id")
    private Location location;
    
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "conference", orphanRemoval = true)
    private List<Attend> attend;

    public Conference() {
    }

    public Conference(int id, String name, String description_short, String description_long, byte[] avatar, Timestamp time_start, Timestamp time_end, int attendees, Location location, List<Attend> attend) {
        this.id = id;
        this.name = name;
        this.description_short = description_short;
        this.description_long = description_long;
        this.avatar = avatar;
        this.time_start = time_start;
        this.time_end = time_end;
        this.attendees = attendees;
        this.location = location;
        this.attend = attend;
    }

    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription_short() {
        return description_short;
    }

    public void setDescription_short(String description_short) {
        this.description_short = description_short;
    }

    public String getDescription_long() {
        return description_long;
    }

    public void setDescription_long(String description_long) {
        this.description_long = description_long;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public Timestamp getTime_start() {
        return time_start;
    }

    public void setTime_start(Timestamp time_start) {
        this.time_start = time_start;
    }

    public Timestamp getTime_end() {
        return time_end;
    }

    public void setTime_end(Timestamp time_end) {
        this.time_end = time_end;
    }

    public int getAttendees() {
        return attendees;
    }

    public void setAttendees(int attendees) {
        this.attendees = attendees;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Attend> getAttend() {
        return attend;
    }

    public void setAttend(List<Attend> attend) {
        this.attend = attend;
    }
    
   
    
}
