/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1712914;

import entities.Account;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author VinhCute
 */
public class AccountOptionsController implements Initializable {

    private Account account = null;
    private Stage stage = null;
    
    @FXML
    private Button signup_button;
    @FXML
    private Button login_button;
    
    public void setDialogStage(Stage stage)
    {
        this.stage = stage;
    }
    
    public Account getAccount()
    {
       return account;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setLoginButtOnAction();
        setSignUpButtonOnAction();
    }    
    
    public void setLoginButtOnAction()
    {
        login_button.setOnMouseClicked(event ->{
            try {
                
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("LoginDialog.fxml"));
                loader.setController(new LoginDialogController());
                AnchorPane page = (AnchorPane) loader.load();
                
                // Create the dialog Stage.
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(login_button.getScene().getWindow());
                
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
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Tài khoản bị chặn");
 
        
                        alert.setContentText("Tài khoản của bạn đã bị chặn, vui lòng liên hệ với quản trị viên để được giúp đỡ!");
                        alert.showAndWait();
                        return;
                    }
                    else stage.close();
                }
                
               
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            
        });
    }
    
    public void setSignUpButtonOnAction()
    {
        signup_button.setOnMouseClicked(event ->{
            try {
                
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("SignUpDialog.fxml"));
                loader.setController(new SignUpDialogController());
                AnchorPane page = (AnchorPane) loader.load();
                
                // Create the dialog Stage.
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(signup_button.getScene().getWindow());
                
                Scene scene = new Scene(page);
                dialogStage.setScene(scene);
                
                SignUpDialogController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                
                // Show the dialog and wait until the user closes it
                dialogStage.showAndWait();
                
                //Get result
                account = controller.getAccount();
                
                if(account !=null)
                {
                    stage.close();
                }
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
        });
    }
}
