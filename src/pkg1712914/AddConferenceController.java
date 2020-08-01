/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1712914;

import entities.Attend;
import entities.Conference;
import entities.ConferenceDAO;
import entities.Location;
import entities.LocationDAO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author VinhCute
 */
public class AddConferenceController implements Initializable {

    private Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public Location currentLocation;
    private Conference conference;

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }
    
    
    
    @FXML
    ImageView conference_avatar;
    @FXML
    Button change_avatar;
    @FXML
    TextField conference_name;
    @FXML
    TextField conference_time_start;
    @FXML
    TextField conference_time_end;
    @FXML
    TextField conference_description_short;
    @FXML
    TextArea conference_description_long;
    @FXML
    SplitMenuButton conference_choose_location;
    @FXML 
    Label conference_location_name;
    @FXML 
    Label conference_location_capacity;
    @FXML 
    Label conference_location_address;
    @FXML
    Button save;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initAdd();
    }    
    
        public void initAdd()
    {
        
        
        ObservableList<Location> locations = new LocationDAO().getAll();
        
        MenuItem addNewLocation = new MenuItem("Thêm địa điểm mới");
        addNewLocation.setOnAction(event->{
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("AddLocation.fxml"));
                loader.setController(new AddLocationController());
                AnchorPane page = (AnchorPane) loader.load();
                
                // Create the dialog Stage.
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(conference_choose_location.getScene().getWindow());
                
                Scene scene = new Scene(page);
                dialogStage.setScene(scene);
                
                AddLocationController controller = loader.getController();
                controller.setStage(dialogStage);
                
                // Show the dialog and wait until the user closes it
                dialogStage.showAndWait();
                
                //Get result
                Location location = controller.getLocation();
                locations.add(location);
                conference_location_name.setText(location.getName());
                conference_location_address.setText(location.getAddress());
                conference_location_capacity.setText(String.valueOf(location.getCapacity()));
                currentLocation = location;
                 
                MenuItem newLocation = new MenuItem(location.getName());
                newLocation.setOnAction(value -> {
                    conference_location_name.setText(location.getName());
                    conference_location_address.setText(location.getAddress());
                    conference_location_capacity.setText(String.valueOf(location.getCapacity()));
                    currentLocation = location;
            });
            conference_choose_location.getItems().add(newLocation);
                 
                 
            } catch (IOException ex) {
                Logger.getLogger(AddConferenceController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        conference_choose_location.getItems().add(addNewLocation);
        
        for(Location item : locations)
        {
            MenuItem menuItem = new MenuItem(item.getName());
            menuItem.setOnAction(event -> {
                conference_location_name.setText(item.getName());
                conference_location_address.setText(item.getAddress());
                conference_location_capacity.setText(String.valueOf(item.getCapacity()));
                currentLocation = item;
            });
            conference_choose_location.getItems().add(menuItem);
        }
        
        change_avatar.setOnAction(event -> {
            FileChooser fc = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
     
            fc.getExtensionFilters().add(extFilter);
            
            File file = fc.showOpenDialog(stage);
            
            if(file!=null)
            {
                Image image = new Image(file.toURI().toString());
                conference_avatar.setImage(image);
            }
            
        });
        
        save.setOnAction(event -> {
            
            try {
              
                String name = conference_name.getText();
                String time_start = conference_time_start.getText();
                String time_end = conference_time_end.getText();
                String description_short = conference_description_short.getText();
                String description_long = conference_description_long.getText();
                
                if(name.isEmpty() || time_start.isEmpty() || time_end.isEmpty() || description_long.isEmpty() || description_short.isEmpty() || currentLocation==null )
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setContentText("Không được bỏ trống trường nào!");
                    alert.showAndWait();
                    return;
                }
                
                Conference newConference = new Conference();
                newConference.setName(name);
                
                SimpleDateFormat formatter = new SimpleDateFormat("hh:mm - dd/MM/yyyy");
                Date date = formatter.parse(time_start);
                Timestamp timeStampDate = new Timestamp(date.getTime());
                
                newConference.setTime_start(timeStampDate);
                date = formatter.parse(time_end);
                timeStampDate = new Timestamp(date.getTime());
                
                newConference.setTime_end(timeStampDate);
                newConference.setDescription_short(description_short);
                newConference.setDescription_long(description_long);
                
                newConference.setLocation(currentLocation);
                
                Image img = conference_avatar.getImage();

                BufferedImage bImage = SwingFXUtils.fromFXImage(img, null);
                ByteArrayOutputStream s = new ByteArrayOutputStream();
                ImageIO.write(bImage, "jpg", s);
                byte[] res  = s.toByteArray();
                s.close();
                
                newConference.setAvatar(res);
                
               ConferenceDAO conferenceDAO = new ConferenceDAO();
               conferenceDAO.add(newConference);
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thành công");
                alert.setContentText("Hội nghị đã được thêm thành công!");
                alert.showAndWait();
                
                conference = newConference;
                stage.close();
                
                
            } catch (ParseException ex ) {
                Logger.getLogger(EditConferenceController.class.getName()).log(Level.SEVERE, null, ex);
            }catch (IOException ex ) {
                Logger.getLogger(EditConferenceController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
    }
}
