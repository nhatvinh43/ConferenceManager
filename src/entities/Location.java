package entities;

import com.mysql.cj.conf.StringProperty;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table (name="location")
public class Location implements Serializable {
    
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    @Column 
    private int id;
    
    @Column(name="name")
    private String name;
    
    @Column(name="address")
    private String address;
    
    @Column(name="capacity")
    private int capacity;
    
    @OneToMany (fetch = FetchType.LAZY, mappedBy = "location")
    private Set<Conference> conference;

    public Location() {
    }

    public Location(int id, String name, String address, int capacity, Set<Conference> conference) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.conference = conference;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
    
}
