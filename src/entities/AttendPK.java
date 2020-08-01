/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.*;

@Embeddable
public class AttendPK implements Serializable {
    
    @Column(name = "user_id")
    private int user_id;

    @Column(name = "conf_id")
    private int conf_id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getConf_id() {
        return conf_id;
    }

    public void setConf_id(int conf_id) {
        this.conf_id = conf_id;
    }

    public AttendPK(int user_id, int conf_id) {
        this.user_id = user_id;
        this.conf_id = conf_id;
    }

    public AttendPK() {
    }
    
    
    
}
