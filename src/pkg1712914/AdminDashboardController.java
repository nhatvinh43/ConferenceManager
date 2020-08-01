/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1712914;

import entities.Account;
import entities.AccountDAO;
import entities.Conference;
import entities.ConferenceDAO;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author VinhCute
 */
public class AdminDashboardController implements Initializable {

    private Account account;

    public AdminDashboardController(Account account) {
        this.account = account;
    }
    
    @FXML
    ImageView back;
    @FXML
    private TableView<Account> table_account;
    @FXML
    private TableColumn<Account, String> account_fullname;
    @FXML
    private TableColumn<Account,String> account_id;
    @FXML
    private TableColumn<Account,String> account_username;
    @FXML
    private TableColumn<Account,String> account_password;
    @FXML
    private TableColumn<Account,String> account_email;
    @FXML
    private TableColumn<Account,String> account_admin;
    @FXML
    private TableColumn<Account,String> account_blocked;
    @FXML
    private TableColumn<Account, String> account_block_action;
    @FXML
    private StackPane stackpane;
    @FXML
    private ImageView account_switch;
    @FXML
    private ImageView conference_switch;
    @FXML
    private GridPane account_pane;
    @FXML
    private GridPane conference_pane;
    @FXML
    TableView<Conference> table_conference;
    @FXML
    TableColumn<Conference, String> conference_id;
    @FXML
    TableColumn<Conference, String> conference_name;
    @FXML
    TableColumn<Conference, String> conference_time_start;
    @FXML
    TableColumn<Conference, String> conference_time_end;
    @FXML
    TableColumn<Conference, String> conference_description_short;
    @FXML
    TableColumn<Conference, String> conference_location;
    @FXML
    TableColumn<Conference, String> conference_attendees;
    @FXML
    TableColumn<Conference, String> conference_action;
    @FXML
    Button conference_add;
    @FXML
    MenuItem user_filter_admin;
    @FXML
    MenuItem user_filter_user;
    @FXML
    MenuItem user_filter_blocked;
    @FXML
    MenuItem user_filter_all;
    @FXML
    SplitMenuButton account_filter;
    
    private ObservableList<Account> accountList = FXCollections.observableArrayList();
    private ObservableList<Conference> conferenceList = FXCollections.observableArrayList();
    ObservableList<Conference> list;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initNavigations();
        initAccountPane();
        initConferencePane();
        initBackButton();
        initAddConference();
        
    }    
    
    
    public void initAddConference()
    {
        conference_add.setOnAction(event ->{
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("AddConference.fxml"));
                loader.setController(new AddConferenceController());
                AnchorPane page = (AnchorPane) loader.load();
                
                // Create the dialog Stage.
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(conference_add.getScene().getWindow());
                
                Scene scene = new Scene(page);
                dialogStage.setScene(scene);
                
                AddConferenceController controller = loader.getController();
                controller.setStage(dialogStage);
                
                // Show the dialog and wait until the user closes it
                dialogStage.showAndWait();
                Conference newConference = controller.getConference();
                if(newConference!=null)
                {
                    list.add(newConference);
                    table_conference.refresh();
                }
                
                
            } catch (IOException ex) {
                Logger.getLogger(AdminDashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    public void initConferencePane()
    {
        ConferenceDAO conferenceDAO = new ConferenceDAO();
        list = conferenceDAO.getAll();
        
        conference_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        conference_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        conference_time_start.setCellValueFactory(new PropertyValueFactory<>("time_start"));
        conference_time_end.setCellValueFactory(new PropertyValueFactory<>("time_end"));
        conference_description_short.setCellValueFactory(new PropertyValueFactory<>("description_short"));
        conference_location.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation().getName()));
        conference_attendees.setCellValueFactory(new PropertyValueFactory<>("attendees"));
        conference_action.setCellValueFactory(new PropertyValueFactory<>("null"));
        
        Callback<TableColumn<Conference, String>, TableCell<Conference, String>> cellFactory
                = //
                new Callback<TableColumn<Conference, String>, TableCell<Conference, String>>() {
            @Override
            public TableCell call(final TableColumn<Conference, String> param) {
                final TableCell<Conference, String> cell = new TableCell<Conference, String>() {

                    final Button btn = new Button();
                    
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            Conference conference = getTableView().getItems().get(getIndex());
                            int index = getIndex();
                            btn.setText("Chi tiết");
                            btn.setOnAction(event -> {
                                try {
                                    /*FXMLLoader conferenceloader = new FXMLLoader(getClass().getResource("EditConference.fxml"));
                                    conferenceloader.setController(new EditConferenceController(getTableView().getItems().get(getIndex())));
                                    AnchorPane pane = conferenceloader.load();
                                    btn.getScene().setRoot(pane);*/
                                    
                                    FXMLLoader loader = new FXMLLoader();
                                    loader.setLocation(Main.class.getResource("EditConference.fxml"));
                                    loader.setController(new EditConferenceController(getTableView().getItems().get(getIndex())));
                                    AnchorPane page = (AnchorPane) loader.load();

                                    // Create the dialog Stage.
                                    Stage dialogStage = new Stage();
                                    dialogStage.initModality(Modality.WINDOW_MODAL);
                                    dialogStage.initOwner(btn.getScene().getWindow());

                                    Scene scene = new Scene(page);
                                    dialogStage.setScene(scene);

                                    EditConferenceController controller = loader.getController();
                                    controller.setStage(dialogStage);

                                    // Show the dialog and wait until the user closes it
                                    dialogStage.showAndWait();
                                    
                                    list.set(index, controller.getConference());
                                    table_conference.refresh();
                                    
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };
        
        conference_action.setCellFactory(cellFactory);
        table_conference.setItems(list);
        
    }
    
    public void initBackButton()
    {
        back.setOnMouseClicked(event ->{
            try {
                FXMLLoader conferenceloader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
                conferenceloader.setController(new MainScreenController(account));
                AnchorPane pane = conferenceloader.load();
                back.getScene().setRoot(pane);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    
    public void initAccountPane()
    {
        
        AccountDAO accountDAO = new AccountDAO();
        accountList = accountDAO.getAll();
        
        FilteredList<Account> filteredAccounts = new FilteredList<>(accountList, b -> true);
        
        //User management
        account_fullname.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        account_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        account_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        account_password.setCellValueFactory(new PropertyValueFactory<>("password"));
        account_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        account_admin.setCellValueFactory(new PropertyValueFactory<>("admin"));
        account_blocked.setCellValueFactory(new PropertyValueFactory<>("blocked"));
        account_block_action.setCellValueFactory(new PropertyValueFactory<>("null"));
        
        
        
        Callback<TableColumn<Account, String>, TableCell<Account, String>> cellFactory
                = //
                new Callback<TableColumn<Account, String>, TableCell<Account, String>>() {
            @Override
            public TableCell call(final TableColumn<Account, String> param) {
                final TableCell<Account, String> cell = new TableCell<Account, String>() {

                    final Button btn = new Button();
                  

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            Account account = getTableView().getItems().get(getIndex());
                            if(account.isBlocked())
                            {
                                btn.setText("Bỏ chặn");
                            } else {
                                btn.setText("Chặn");
                            }
                            btn.setOnAction(event -> {
                                if(account.isBlocked())
                                {
                                    account.setBlocked(false);
                                    accountDAO.setBlocked(account.getId(), false);
                                    btn.setText("Chặn");
                                }
                                else
                                {
                                    account.setBlocked(true);
                                    accountDAO.setBlocked(account.getId(), true);
                                    btn.setText("Bỏ chặn");
                                }
                                
                                table_account.refresh();
                                
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };
        
        account_block_action.setCellFactory(cellFactory);
        
        SortedList<Account>   sortableData = new SortedList<>(filteredAccounts);
       
        table_account.setItems(sortableData);
        sortableData.comparatorProperty().bind(table_account.comparatorProperty());
        
        
        
        user_filter_admin.setOnAction(event->{
           account_filter.setText("Tài khoản Admin");
           filteredAccounts.setPredicate(new Predicate<Account>(){
               @Override
               public boolean test(Account t) {
                   return (t.isAdmin());
               }
               
           });
        });
        
        user_filter_user.setOnAction(event->{
           account_filter.setText("Tài khoản User");
           filteredAccounts.setPredicate(new Predicate<Account>(){
               @Override
               public boolean test(Account t) {
                   return (!t.isAdmin());
               }
               
           });
        });
        
        user_filter_blocked.setOnAction(event->{
           account_filter.setText("Tài khoản bị chặn");
           filteredAccounts.setPredicate(new Predicate<Account>(){
               @Override
               public boolean test(Account t) {
                   return (t.isBlocked());
               }
               
           });
        });
        
         user_filter_all.setOnAction(event->{
           account_filter.setText("Tất cả");
           filteredAccounts.setPredicate(new Predicate<Account>(){
               @Override
               public boolean test(Account t) {
                   return (true);
               }
           });
        });
        
    }
    
    public void initNavigations()
    {
        //Navigation
        //Account button
        account_switch.setOnMouseClicked(event ->{
            account_pane.toFront();
            account_pane.setVisible(true);
            conference_pane.setVisible(false);
        });
        
        //Conference button
         conference_switch.setOnMouseClicked(event ->{
            conference_pane.toFront();
            conference_pane.setVisible(true);
            account_pane.setVisible(false);
        });
    }
    
}
