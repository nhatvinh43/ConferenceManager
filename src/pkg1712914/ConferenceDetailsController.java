/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1712914;

import entities.Account;
import entities.AccountDAO;
import entities.Attend;
import entities.Conference;
import entities.ConferenceDAO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author VinhCute
 */
public class ConferenceDetailsController implements Initializable {

    private Account account;
    private String conf_id;

    @FXML
    private ImageView back;
    @FXML
    private ImageView conference_avatar;
    @FXML 
    private Label conference_name;
    @FXML
    private Label conference_description_short;
    @FXML
    private Button conference_join;
    @FXML
    private Label conference_time_start;
    @FXML
    private Label conference_time_end;
    @FXML
    private Label conference_location_name;
    @FXML
    private Label conference_location_address;
    @FXML
    private Label conference_description_long;
    @FXML
    private Label conference_location_capacity;
    @FXML
    private Label conference_attendees;
    @FXML
    private TableView conference_attendees_table;
    @FXML
    private TableColumn<Account,String> attendees_fullname;
    @FXML
    private TableColumn<Account,String> attendees_email;
    
    public ConferenceDetailsController( Account account, String conf_id) {
        this.account = account;
        this.conf_id = conf_id;
    }
    
    Conference conference;
    ConferenceDAO conferenceDAO = new ConferenceDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        inflateData();
        initBackButton();
        initJoinButton();
        
    }    
    
    public void inflateData()
    {
        
                
        //Kiểm tra trạng thái duyệt
        if(account!=null)
        {
            Attend attend = conferenceDAO.getAttendeeByID(conf_id, String.valueOf(account.getId()));
            if(attend!=null)
            {
                conference_join.setDisable(true);
                if(attend.isAccepted())
                {
                   conference_join.setText("Yêu cầu tham gia đã được chấp nhận!");
                }
                else
                {
                   conference_join.setText("Yêu cầu tham gia đang chờ xét duyệt!");
                }
            }
        }
  
        
        conference = conferenceDAO.getConferenceByID(conf_id);
        
        if(conference.getTime_start().before(new Timestamp(new Date().getTime())))
        {
            conference_join.setVisible(false);
        }
        
        conference_avatar.setImage(new Image(getClass().getResourceAsStream("/images/sample.jpg")));
        
        byte[] img = conference.getAvatar();
        Image image = new Image(new ByteArrayInputStream(img));
        conference_avatar.setImage(image);

        conference_name.setText(conference.getName());
        conference_description_short.setText(conference.getDescription_short());
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm - dd/MM/yyyy");
        conference_time_start.setText(simpleDateFormat.format(conference.getTime_start()));
        conference_time_end.setText(simpleDateFormat.format(conference.getTime_end()));
        conference_location_name.setText(conference.getLocation().getName());
        conference_location_address.setText(conference.getLocation().getAddress());
        conference_description_long.setText(conference.getDescription_long());
        conference_location_capacity.setText(String.valueOf(conference.getLocation().getCapacity()));
        
        ObservableList<Attend> attend = conferenceDAO.getAttendees(conf_id);
        
        ObservableList<Account> account = FXCollections.observableArrayList();
        for(int i=0 ; i<attend.size();i++)
        {
            account.add(attend.get(i).getUser());
        }
        
        conference_attendees.setText(String.valueOf(attend.size()));
        
        attendees_fullname.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        attendees_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        conference_attendees_table.setItems(account);
    }
        
    public void initBackButton()
    {
        back.setOnMouseClicked(event -> {
            try {
                FXMLLoader mainloader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
                mainloader.setController(new MainScreenController(account));
                AnchorPane pane = mainloader.load();
                back.getScene().setRoot(pane);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    
    public void initJoinButton()
    {
        conference_join.setOnMouseClicked(event ->{
            try {
                
                if(account!=null)
                {
                    
                    if(conference.getAttendees()>=conference.getLocation().getCapacity())
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Lỗi khi tham gia!");
                        alert.setContentText("Danh sách người tham gia đã đầy, vui lòng chọn hội nghị khác!");
                        alert.showAndWait();
                        return;
                    }
                    
                    long addAttendeeResult = conferenceDAO.addAttendee(this.account, conference);
                    if(addAttendeeResult==-1)
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Tham gia thất bại");
                        alert.setContentText("Đã có lỗi xảy ra, vui lòng thử lại!");
                        alert.showAndWait();
                        return;
                    }
                    else
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Yêu cầu thành công");
                        alert.setContentText("Yêu cầu của bạn sẽ sớm được quản trị viên duyệt!");
                        alert.showAndWait();
                        
                        conference_join.setText("Yêu cầu tham gia đang chờ xét duyệt!");
                        conference_join.setDisable(true);
                        return;
                    }
                }
                else
                {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(Main.class.getResource("AccountOptions.fxml"));
                    loader.setController(new AccountOptionsController());
                    AnchorPane page = (AnchorPane) loader.load();

                    // Create the dialog Stage.
                    Stage dialogStage = new Stage();
                    dialogStage.initModality(Modality.WINDOW_MODAL);
                    dialogStage.initOwner(conference_join.getScene().getWindow());

                    Scene scene = new Scene(page);
                    dialogStage.setScene(scene);

                    AccountOptionsController controller = loader.getController();
                    controller.setDialogStage(dialogStage);

                    // Show the dialog and wait until the user closes it
                    dialogStage.showAndWait();

                    //Get result
                    account = controller.getAccount();

                    if(account !=null)
                    {
                        long addAttendeeResult = conferenceDAO.addAttendee(this.account, conference);
                        
                        if(conference.getAttendees()>=conference.getLocation().getCapacity())
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Lỗi khi tham gia!");
                            alert.setContentText("Danh sách người tham gia đã đầy, vui lòng chọn hội nghị khác!");
                            alert.showAndWait();
                            return;
                        }
                            
                        
                        if(addAttendeeResult==-1)
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Tham gia thất bại");
                            alert.setContentText("Đã có lỗi xảy ra, vui lòng thử lại!");
                            alert.showAndWait();
                        }
                        else
                        {
                            
                            
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Yêu cầu thành công");
                            alert.setContentText("Yêu cầu của bạn sẽ sớm được quản trị viên duyệt!");
                            alert.showAndWait();
                            
                            conference_join.setText("Yêu cầu tham gia đang chờ xét duyệt!");
                            conference_join.setDisable(true);
                        }
                    }
                    else
                    {
                        return;
                    }
                }

                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            
        });
    }
    
}
