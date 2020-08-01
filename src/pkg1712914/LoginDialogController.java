/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1712914;

import entities.Account;
import entities.AccountDAO;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class LoginDialogController implements Initializable {

    
    private Account account = null;
    
    @FXML
    private TextField login_username;
    @FXML
    private PasswordField login_password;
    @FXML 
    private Button login_button;
    @FXML
    private Label login_error_text;
    
    private Stage dialogStage;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        login_error_text.setVisible(false);
        
        login_button.setOnAction(event -> {
            String username = login_username.getText();
            String password = login_password.getText();
            
            try {
                password = EncryptionHelper.toHexString(EncryptionHelper.getSHA(password));
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
            
            AccountDAO accountDAO = new AccountDAO();
            account = accountDAO.queryAccount(username, password);
            
            if(account==null)
            {
                login_error_text.setVisible(true);
            }
            
            else
            {
                dialogStage.close();
            }
            
        });
    }    
    
    public Account getAccountResult()
    {
        return account;
    }
    
    public void setDialogStage(Stage stage)
    {
        this.dialogStage = stage;
    }
    
    
}
