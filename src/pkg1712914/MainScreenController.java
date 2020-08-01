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
import entities.Location;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.tuple.PropertyFactory;

/**
 * FXML Controller class
 *
 * @author VinhCute
 */
public class MainScreenController implements Initializable {

    private ObservableList<Conference> conferenceListData = null;
    private Account account = null;

    public MainScreenController(Account account) {
        this.account = account;
    }

    public MainScreenController() {
    }
    
    @FXML
    private ListView conference_list;
    @FXML
    private FlowPane conference_flow_pane;
    @FXML
    private SplitMenuButton main_view_options;
    @FXML
    private ImageView main_login;
    @FXML
    private Label main_login_label;
    @FXML
    private ImageView main_account;
    @FXML
    private Label main_account_label;
    @FXML
    private ImageView main_logout;
    @FXML
    private Label main_logout_label;
    @FXML
    private ScrollPane conference_scroll_pane;
    @FXML
    private ImageView main_history;
    @FXML
    private Label main_history_label;
    @FXML
    private ImageView main_administrate;
    @FXML
    private Label main_administrate_label;
    @FXML
    private ImageView main_signup;
    @FXML
    private Label main_signup_label;
    @FXML
    private ImageView main_home;
    @FXML
    private Label main_home_label;
    @FXML
    private GridPane  main_conferences_pane;
    @FXML
    private GridPane  main_account_pane;
    @FXML
    private TextField main_account_fullname;
    @FXML
    private TextField main_account_email;
    @FXML
    private TextField main_account_password_old;
    @FXML
    private TextField main_account_password_new;
    @FXML
    private TextField main_account_password_new_confirm;
    @FXML
    private Button update_info;
    @FXML
    private Button update_password;
    @FXML
    private Label update_info_err;
    @FXML
    private Label update_password_err;
    @FXML
    TextField search_bar;
    @FXML
    Button search_button;
    @FXML
    SplitMenuButton filter_button;
    @FXML
    TableView table;
    @FXML
    TableColumn<Conference, String> name_col;
    @FXML
    TableColumn<Conference, String> time_start_col;
    @FXML
    TableColumn<Conference, String> time_end_col;
    @FXML
    TableColumn<Conference, String> description_col;
    @FXML
    TableColumn<Conference, String> location_col;
    @FXML
    TableColumn<Conference, String> attendees_col;
    @FXML
    TableColumn<Conference, String> options_col;
    @FXML
    GridPane history_pane;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ConferenceDAO conferenceDAO = new ConferenceDAO();
        conferenceListData = conferenceDAO.getAllActive();
        
        
        main_conferences_pane.setVisible(true);
        main_conferences_pane.toFront();
        
        initCardView();
        initListView();
        
        conference_list.setVisible(false);
        conference_flow_pane.setVisible(true);
  
        initMenu();
        initSideMenu();
        
        
    }    
    
    public void initTable()
    {
       ConferenceDAO conferenceDAO = new ConferenceDAO();
       ObservableList<Conference> conferences = conferenceDAO.getAllByUser(String.valueOf(account.getId()));
       FilteredList<Conference> filteredData = new FilteredList<>(conferences, b -> true);
       
       name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
       time_start_col.setCellValueFactory(new PropertyValueFactory<>("time_start"));
       time_end_col.setCellValueFactory(new PropertyValueFactory<>("time_end"));
       description_col.setCellValueFactory(new PropertyValueFactory<>("description_short"));
       location_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation().getName()));
       attendees_col.setCellValueFactory(new PropertyValueFactory<>("attendees"));
       options_col.setCellValueFactory(new PropertyValueFactory<>("null"));
       
       Callback<TableColumn<Conference, String>, TableCell<Conference, String>> cellFactory
                = //
                new Callback<TableColumn<Conference, String>, TableCell<Conference, String>>() {
            @Override
            public TableCell call(final TableColumn<Conference, String> param) {
                final TableCell<Conference, String> cell = new TableCell<Conference, String>() {

                    final Button detailsButton = new Button("Xem");
                    final Button unEnrolButton = new Button("Hủy");
                    final HBox container = new HBox(detailsButton, unEnrolButton);
                    Conference conference = new Conference();
                    
                    {
                        detailsButton.setOnAction(event -> {
                            try {
                                FXMLLoader conferenceloader = new FXMLLoader(getClass().getResource("ConferenceDetails.fxml"));
                                conferenceloader.setController(new ConferenceDetailsController(account, String.valueOf(conference.getId())));
                                AnchorPane pane = conferenceloader.load();
                                detailsButton.getScene().setRoot(pane);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        });

                        unEnrolButton.setOnAction(event -> {
                           conferenceDAO.deleteAttend(conference.getId(), account.getId());
                           unEnrolButton.setDisable(true);
                           unEnrolButton.setText("Đã hủy");
                        });
                    }
                    
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            
                            conference = getTableView().getItems().get(getIndex());
                            
                            if(conference.getTime_start().before((new Timestamp(new Date().getTime()))))
                            {
                                unEnrolButton.setVisible(false);
                            }
                            
                            table.refresh();
                            
                            setGraphic(container);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };
       
       
      
       options_col.setCellFactory(cellFactory);
       
       SortedList<Conference>   sortableData = new SortedList<>(filteredData);
       
       table.setItems(sortableData);
       sortableData.comparatorProperty().bind(table.comparatorProperty());
       
       //Search
       search_bar.textProperty().addListener((observable,oldValue, newValue) -> {
           filteredData.setPredicate(new Predicate<Conference>()
           {    
               
               @Override
               public boolean test(Conference conference) {
                   if(newValue==null || newValue.isEmpty())
                    {
                        return true;
                    }
               
                    String lowerCase = newValue.toLowerCase();

                    //Name
                    if(conference.getName().toLowerCase().indexOf(lowerCase)!=-1)
                    {
                        return true;
                    }
                    //Location
                    if(conference.getLocation().getName().toLowerCase().indexOf(lowerCase)!=-1)
                    {
                        return true;
                    }
                    //Short description
                     if(conference.getDescription_short().toLowerCase().indexOf(lowerCase)!=-1)
                    {
                        return true;
                    }
                     
                    return false;

               }
               
           });
       });
       
       //Filter
       MenuItem item = new MenuItem("Tất cả");
       MenuItem item1 = new MenuItem("Đã đầy chỗ");
       MenuItem item2 = new MenuItem("Chưa đầy chỗ");
       MenuItem item3 = new MenuItem("Đã diễn ra");
       MenuItem item4 = new MenuItem("Chưa diễn ra");
       filter_button.getItems().addAll(item1, item2, item3, item4);
       
       
       item.setOnAction(event ->{
           filter_button.setText("Tất cả");
           filteredData.setPredicate(new Predicate<Conference>(){
               @Override
               public boolean test(Conference t) {
                   return (true);
               }
           });
       });
       
       item1.setOnAction(event ->{
           filter_button.setText("Đã đầy chỗ");
           filteredData.setPredicate(new Predicate<Conference>(){
               @Override
               public boolean test(Conference t) {
                   return (t.getAttendees() == t.getLocation().getCapacity());
               }
               
           });
       });
       
       item2.setOnAction(event ->{
           filter_button.setText("Chưa đầy chỗ");
           filteredData.setPredicate(new Predicate<Conference>(){
               @Override
               public boolean test(Conference t) {
                   return (t.getAttendees() < t.getLocation().getCapacity());
               }
               
           });
       });
       
       item3.setOnAction(event ->{
           filter_button.setText("Đã diễn ra");
           filteredData.setPredicate(new Predicate<Conference>(){
               @Override
               public boolean test(Conference t) {
                   return (t.getTime_start().before(new Timestamp(new Date().getTime())));
               }
           });
       });
       
       item4.setOnAction(event ->{
           filter_button.setText("Chưa diễn ra");
           filteredData.setPredicate(new Predicate<Conference>(){
               @Override
               public boolean test(Conference t) {
                   return (t.getTime_start().after(new Timestamp(new Date().getTime())));
               }
           });
       });
       
    }
    
   
    
    public void initAccountPane()
    {
        if(account!=null)
        {
            main_account_fullname.setText(account.getFullname());
            main_account_email.setText(account.getEmail());
        
        update_info.setOnAction(event ->{
           AccountDAO accountDAO = new AccountDAO();
           String fullname = main_account_fullname.getText();
           String email = main_account_email.getText();
           
           if(fullname.isEmpty() || email.isEmpty())
           {
               update_info_err.setVisible(true);
               update_info_err.setText("Vui lòng không để trống trường nào!");
           }
           
           if(fullname == account.getFullname() && email==account.getEmail())
           {
               update_info_err.setVisible(true);
               update_info_err.setText("Thông tin cập nhật phải khác với thông tin ban đầu!");
           }
           
           accountDAO.updateAccountInfo(account, fullname, email);

            account.setFullname(fullname);
            account.setEmail(email);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cập nhật thành công");
            alert.setContentText("Thông tin đã được cập nhật thành công!");
            alert.showAndWait();
           
           
        });
        
        update_password.setOnAction(event ->{
            try {
                AccountDAO accountDAO = new AccountDAO();
                String oldPassword;
                oldPassword = EncryptionHelper.toHexString(EncryptionHelper.getSHA(main_account_password_old.getText()));
                
                String newPassword = main_account_password_new.getText();
                String newPasswordConfirm = main_account_password_new_confirm.getText();
                
                if(oldPassword.isEmpty() || newPassword.isEmpty() || newPasswordConfirm.isEmpty() )
                {
                    update_password_err.setVisible(true);
                    update_password_err.setText("Vui lòng không để trống trường nào!");
                }
                
                if(!oldPassword.equals(account.getPassword()))
                {
                    update_password_err.setVisible(true);
                    update_password_err.setText("Mật khẩu cũ không đúng!");
                }
                
                if(!newPassword.equals(newPasswordConfirm))
                {
                    update_password_err.setVisible(true);
                    update_password_err.setText("Mật khẩu mới không khớp!");
                }
                
                newPassword = EncryptionHelper.toHexString(EncryptionHelper.getSHA(newPassword));
                
                accountDAO.updateAccountPassword(account, newPassword);
                account.setPassword(newPassword);
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Cập nhật thành công");
                alert.setContentText("Thông tin đã được cập nhật thành công!");
                alert.showAndWait();
            
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        }
    }
    
    public void initLoginButton()
    {
         main_login.setOnMouseClicked(event ->{
            try {
                
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("LoginDialog.fxml"));
                loader.setController(new LoginDialogController());
                AnchorPane page = (AnchorPane) loader.load();
                
                // Create the dialog Stage.
                Stage dialogStage = new Stage();
                dialogStage.setMaxHeight(350);
                dialogStage.setMaxWidth(400);
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(main_login.getScene().getWindow());
                
                Scene scene = new Scene(page);
                dialogStage.setScene(scene);
                
                LoginDialogController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                
                // Show the dialog and wait until the user closes it
                dialogStage.showAndWait();
                
                //Get result
                account = controller.getAccountResult();
                
                if(account!=null)
                {
                    //Tài khoản bị chặn
                    if(account.isBlocked())
                    {
                        account = null;
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Tài khoản bị chặn");
 
        
                        alert.setContentText("Tài khoản của bạn đã bị chặn, vui lòng liên hệ với quản trị viên để được giúp đỡ!");
 
                        alert.showAndWait();
                        return;
                    }
                    
                    main_login.setVisible(false);
                    main_login_label.setVisible(false);
                    
                    main_signup.setVisible(false);
                    main_signup_label.setVisible(false);
                    
                    main_logout.setVisible(true);
                    main_logout_label.setVisible(true);
                    
                    main_account.setVisible(true);
                    main_account_label.setVisible(true);
                    
                    main_history.setVisible(true);
                    main_history_label.setVisible(true);
                    
                    //Tài khoản Admin
                    if(account.isAdmin())
                    {
                        main_administrate.setVisible(true);
                        main_administrate_label.setVisible(true);
                    } 
                    else
                    {
                        main_administrate.setVisible(false);
                        main_administrate_label.setVisible(false);
                    }
                    
                }
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            
        });
    }
    
    public void initLogoutButton()
    {
        main_logout.setOnMouseClicked(event -> {
        account = null;
        main_login.setVisible(true);
        main_login_label.setVisible(true);
        
        main_signup.setVisible(true);
        main_signup_label.setVisible(true);

        main_logout.setVisible(false);
        main_logout_label.setVisible(false);

        main_account.setVisible(false);
        main_account_label.setVisible(false);

        main_history.setVisible(false);
        main_history_label.setVisible(false);

        main_administrate.setVisible(false);
        main_administrate_label.setVisible(false);
        });
        
       
    }
    public void initSignUpButton()
    {
        main_signup.setOnMouseClicked(event ->{
            try {
                
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("SignUpDialog.fxml"));
                loader.setController(new SignUpDialogController());
                AnchorPane page = (AnchorPane) loader.load();
                
                // Create the dialog Stage.
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(main_signup.getScene().getWindow());
                
                Scene scene = new Scene(page);
                dialogStage.setScene(scene);
                
                SignUpDialogController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                
                // Show the dialog and wait until the user closes it
                dialogStage.showAndWait();
                
                //Get result
                account = controller.getAccount();
                
                if(account!=null)
                {
                    //Tài khoản bị chặn
                    if(account.isBlocked())
                    {
                        account = null;
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Tài khoản bị chặn");
                        alert.setContentText("Tài khoản của bạn đã bị chặn, vui lòng liên hệ với quản trị viên để được giúp đỡ!");
                        alert.showAndWait();
                        return;
                    }
                    
                    main_login.setVisible(false);
                    main_login_label.setVisible(false);
                    
                    main_signup.setVisible(false);
                    main_signup_label.setVisible(false);
                    
                    main_logout.setVisible(true);
                    main_logout_label.setVisible(true);
                    
                    main_account.setVisible(true);
                    main_account_label.setVisible(true);
                    
                    main_history.setVisible(true);
                    main_history_label.setVisible(true);
                    
                    //Tài khoản Admin
                    if(account.isAdmin())
                    {
                        main_administrate.setVisible(true);
                        main_administrate_label.setVisible(true);
                    } 
                    else
                    {
                         main_administrate.setVisible(false);
                        main_administrate_label.setVisible(false);
                    }
                    
                }
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            
        });
    }
    
    public void initSideMenu()
    {
        
        //Image
        main_login.setImage(new Image(getClass().getResourceAsStream("/images/login.png")));
        main_account.setImage(new Image(getClass().getResourceAsStream("/images/person.png")));
        main_logout.setImage(new Image(getClass().getResourceAsStream("/images/logout.png")));
        main_history.setImage(new Image(getClass().getResourceAsStream("/images/history.png")));
        main_administrate.setImage(new Image(getClass().getResourceAsStream("/images/settings.png")));
        main_signup.setImage(new Image(getClass().getResourceAsStream("/images/signup.png")));
        main_home.setImage(new Image(getClass().getResourceAsStream("/images/home.png")));
        
        if(account!=null)
        {
            
            main_login.setVisible(false);
            main_login_label.setVisible(false);

            main_signup.setVisible(false);
            main_signup_label.setVisible(false);
            
            main_logout.setVisible(true);
            main_logout_label.setVisible(true);

            main_account.setVisible(true);
            main_account_label.setVisible(true);

            main_history.setVisible(true);
            main_history_label.setVisible(true);
            
            if(account.isAdmin())
            {
                main_administrate.setVisible(true);
                main_administrate_label.setVisible(true);
            }
            else
            {
                main_administrate.setVisible(false);
                main_administrate_label.setVisible(false);
            }
        }
        else
        {
            main_login.setVisible(true);
            main_login_label.setVisible(true);
            
            main_signup.setVisible(true);
            main_signup_label.setVisible(true);
            
            main_logout.setVisible(false);
            main_logout_label.setVisible(false);
            
            main_account.setVisible(false);
            main_account_label.setVisible(false);
            
            main_history.setVisible(false);
            main_history_label.setVisible(false);
            
            main_administrate.setVisible(false);
            main_administrate_label.setVisible(false);
        }
        
        
        
        //On click action
        
        initLoginButton();
        initSignUpButton();
        initLogoutButton();
        initOtherButtons();
        
    }
    
    public void initOtherButtons()
    {
        main_home.setOnMouseClicked(event -> {
            main_conferences_pane.toFront();
        });
        
        main_account.setOnMouseClicked(event -> {
            initAccountPane();
            main_account_pane.setVisible(true);
            main_account_pane.toFront();
        });
        
        main_history.setOnMouseClicked(event -> {
            initTable();
            history_pane.setVisible(true);
            history_pane.toFront();
        });
        
        main_administrate.setOnMouseClicked(event -> {
            try {
                FXMLLoader conferenceloader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
                conferenceloader.setController(new AdminDashboardController(account));
                AnchorPane pane = conferenceloader.load();
                main_administrate.getScene().setRoot(pane);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    
    public void initCardView()
    {
        for(Conference item : conferenceListData )
        {
            Node node;
            try {
                //node = (Node)FXMLLoader.load(getClass().getResoursce("ConferenceCardView.fxml"));
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ConferenceCardView.fxml"));
                loader.setController(new ConferenceCardViewController(item));
                node = loader.load();
                node.setOnMouseClicked(event -> {
                    try {
                        FXMLLoader conferenceloader = new FXMLLoader(getClass().getResource("ConferenceDetails.fxml"));
                        conferenceloader.setController(new ConferenceDetailsController(account, String.valueOf(item.getId())));
                        AnchorPane pane = conferenceloader.load();
                        node.getScene().setRoot(pane);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                conference_flow_pane.getChildren().add(node);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void initListView()
    {
        conference_list.setItems(conferenceListData);
        conference_list.setCellFactory(conferenceListCell -> new ConferenceListCell(account));
    }
    
    public void initMenu()
    {
        MenuItem cardOption = new MenuItem("Thẻ");
        MenuItem listOption = new MenuItem("Danh sách");
        main_view_options.getItems().addAll(cardOption, listOption);
        
        cardOption.setOnAction(e -> {
            conference_list.setVisible(false);
            conference_scroll_pane.setVisible(true);
            conference_flow_pane.setVisible(true);
        });
        
        listOption.setOnAction(e -> {
            conference_scroll_pane.setVisible(false);
            conference_flow_pane.setVisible(false);
            conference_list.setVisible(true);
        });
    }
    
}
