/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1712914;

import entities.Account;
import entities.Attend;
import entities.Conference;
import entities.ConferenceDAO;
import entities.Location;
import entities.LocationDAO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.util.Callback;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author VinhCute
 */
public class EditConferenceController implements Initializable {

    private Conference conference;
    private Stage stage;
    
    private Location currentLocation;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Conference getConference() {
        return conference;
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
    Label conference_attendees;
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
    TableView <Attend> requests;
    @FXML
    TableColumn<Attend, String> fullname_col;
    @FXML
    TableColumn<Attend, String> email_col;
    @FXML
    TableColumn<Attend, String> options_col;
    @FXML
    Button save;
    
    private ObservableList<Attend> requestList;
    
    public EditConferenceController(Conference conference, Stage stage) {
        this.conference = conference;
        this.stage = stage;
    }
    

    public EditConferenceController(Conference conference) {
        this.conference = conference;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inflateData();
        initChange();
        
    }    
 
    public void initChange()
    {
        
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
                if (conference.getTime_start().before(new Date()))
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setContentText("Không thể thay đổi thông tin hội nghị đã diễn ra!");
                    alert.showAndWait();
                    return;
                }
                String name = conference_name.getText();
                String time_start = conference_time_start.getText();
                String time_end = conference_time_end.getText();
                String description_short = conference_description_short.getText();
                String description_long = conference_description_long.getText();
                
                if(name.isEmpty() || time_start.isEmpty() || time_end.isEmpty() || description_long.isEmpty() || description_short.isEmpty())
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setContentText("Không được bỏ trống trường nào!");
                    alert.showAndWait();
                    return;
                }
                
                Conference newConference = conference;
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
                conferenceDAO.update(newConference);
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thành công");
                alert.setContentText("Hội nghị đã được cập nhật thành công!");
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
    
    public void inflateData()
    {
        conference_avatar.setImage(new Image(getClass().getResourceAsStream("/images/sample.jpg")));
        currentLocation = conference.getLocation();
        
        byte[] img = conference.getAvatar();
        Image image = new Image(new ByteArrayInputStream(img));
        conference_avatar.setImage(image);
        
        conference_name.setText(conference.getName());
        conference_description_short.setText(conference.getDescription_short());
        conference_attendees.setText(String.valueOf(conference.getAttendees()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm - dd/MM/yyyy");
        conference_time_start.setText(simpleDateFormat.format(conference.getTime_start()));
        conference_time_end.setText(simpleDateFormat.format(conference.getTime_end()));
        conference_location_name.setText(conference.getLocation().getName());
        conference_location_address.setText(conference.getLocation().getAddress());
        conference_description_long.setText(conference.getDescription_long());
        conference_location_capacity.setText(String.valueOf(conference.getLocation().getCapacity()));
        
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
        
        initRequestTable();
        
    }
    
    public void initRequestTable()
    {
        if(conference.getTime_start().before(new Timestamp(new Date().getTime())))
        {
            requests.setDisable(true);
            return;
        }
        email_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getEmail()));
        fullname_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getFullname()));
        options_col.setCellValueFactory(new PropertyValueFactory<>("null"));
        
        requestList = new ConferenceDAO().getRequests(String.valueOf(conference.getId()));
        
        Callback<TableColumn<Attend, String>, TableCell<Attend, String>> cellFactory
                = //
                new Callback<TableColumn<Attend, String>, TableCell<Attend, String>>() {
            @Override
            public TableCell call(final TableColumn<Attend, String> param) {
                final TableCell<Attend, String> cell = new TableCell<Attend, String>() {

                    final Button btn = new Button();
                  

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            
                            Attend attend = getTableView().getItems().get(getIndex());
                            
                            btn.setText("Chấp nhận");
                            
                            btn.setOnAction(event -> {
                                
                                ConferenceDAO conferenceDAO = new ConferenceDAO();
                                if(conference.getAttendees()==conference.getLocation().getCapacity())
                                {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Không thể chấp nhận");
                                    alert.setContentText("Danh sách tham gia đã đầy!");
                                    alert.showAndWait();
                                    return;
                                }
                                conferenceDAO.acceptRequest(conference.getId(), attend.getUser().getId());
                                btn.setText("Đã chấp nhận");
                                btn.setDisable(true);
                                attend.setAccepted(true);
                                conference.setAttendees(conference.getAttendees()+1);
                                conference_attendees.setText(String.valueOf(conference.getAttendees()));
                                
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };
        
        options_col.setCellFactory(cellFactory);
        requests.setItems(requestList);
        
    }
}
