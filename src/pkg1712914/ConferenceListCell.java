/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1712914;

import entities.Account;
import entities.Conference;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 *
 * @author VinhCute
 */
public class ConferenceListCell extends ListCell<Conference> {
    
    @FXML
    private ImageView conference_list_image;
    @FXML
    private ImageView conference_list_icon;
    @FXML
    private Label conference_list_title;
    @FXML
    private Label conference_list_time;
    @FXML
    private Label conference_list_description;
    @FXML
    private Label conference_list_location;
    @FXML
    private Label conference_list_attendees;
    @FXML
    private AnchorPane conference_list_anchor_pane;
    
    private FXMLLoader mLLoader = null;
    private Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public ConferenceListCell(Account account) {
        this.account = account;
    }
    
    
    
    @Override
    protected void updateItem(Conference conference, boolean empty) {
        super.updateItem(conference, empty);

        if(empty || conference == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("ConferenceListView.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            conference_list_image.setImage(new Image(new File("./images/sample.jpg").toURI().toString()));
            conference_list_icon.setImage(new Image(new File("./images/user.png").toURI().toString()));
            
            byte[] img = conference.getAvatar();
            Image image = new Image(new ByteArrayInputStream(img));
            conference_list_image.setImage(image);
          
            conference_list_title.setText(conference.getName());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm - dd/MM/yyyy");
            conference_list_time.setText(simpleDateFormat.format(conference.getTime_start()));
            conference_list_description.setText(conference.getDescription_short());
            conference_list_location.setText(conference.getLocation().getName());
            conference_list_attendees.setText(String.valueOf(conference.getAttendees()));
            
            this.setOnMouseClicked(event ->{
                 try {
                    FXMLLoader conferenceloader = new FXMLLoader(getClass().getResource("ConferenceDetails.fxml"));
                    conferenceloader.setController(new ConferenceDetailsController(account, String.valueOf(this.getItem().getId())));
                    AnchorPane pane = conferenceloader.load();
                    
                    this.getScene().setRoot(pane);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            
            setText(null);
            setGraphic(conference_list_anchor_pane);
        }

    }
    
    
}
