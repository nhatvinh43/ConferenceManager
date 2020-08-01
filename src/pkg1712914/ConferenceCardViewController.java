/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1712914;

import entities.Conference;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javax.imageio.ImageIO;
import static sun.security.krb5.Confounder.bytes;

/**
 *
 * @author VinhCute
 */
public class ConferenceCardViewController implements Initializable {
    
    @FXML
    private ImageView conference_card_image;
    @FXML
    private ImageView conference_card_icon;
    @FXML
    private Label conference_card_title;
    @FXML
    private Label conference_card_time;
    @FXML
    private Label conference_card_description;
    @FXML
    private Label conference_card_location;
    @FXML
    private Label conference_card_attendees;
   
    private Conference conference;

    public ConferenceCardViewController( Conference conference) {
        this.conference = conference;
    }
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(this.conference!=null)
        {
            conference_card_image.setImage(new Image(new File("./images/sample.jpg").toURI().toString()));
            conference_card_icon.setImage(new Image(new File("./images/user.png").toURI().toString()));
            
           
            byte[] img = conference.getAvatar();
            Image image = new Image(new ByteArrayInputStream(img));
            conference_card_image.setImage(image);
            
            
            conference_card_title.setText(conference.getName());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm - dd/MM/yyyy");
            conference_card_time.setText(simpleDateFormat.format(conference.getTime_start()));
            conference_card_description.setText(conference.getDescription_short());
            conference_card_location.setText(conference.getLocation().getName());
            conference_card_attendees.setText(String.valueOf(conference.getAttendees()));
        }
    }
    
    
}
