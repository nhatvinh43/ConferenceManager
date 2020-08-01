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
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author VinhCute
 */
public class SignUpDialogController implements Initializable {

    private Account account = null;
    private Stage stage = null;
    
    @FXML
    private TextField signup_username;
    @FXML
    private TextField signup_fullname;
    @FXML
    private TextField signup_email;
    @FXML
    private TextField signup_password;
    @FXML
    private TextField signup_password_confirm;
    @FXML
    private Button signup_button;
    @FXML
    private Label signup_error_text;
    
    public void setDialogStage(Stage stage)
    {
        this.stage = stage;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       signup_error_text.setVisible(false);
       setSignUpButtonAction();
    }    
    
    public Account getAccount()
    {
        return account;
    }
    
    public void setSignUpButtonAction()
    {
        signup_button.setOnAction(event -> {
           
            signup_error_text.setVisible(false);
            
            String username = signup_username.getText();
            String fullname = signup_fullname.getText();
            String email = signup_email.getText();
            String password = signup_password.getText();
            String password_confirm = signup_password_confirm.getText();
            
            if(username.isEmpty() || fullname.isEmpty() || email.isEmpty() || password.isEmpty() || password_confirm.isEmpty())
            {
                signup_error_text.setText("Vui lòng không để trống trường nào!");
                signup_error_text.setVisible(true);
                return;
            }
            
            if(!password.equals(password_confirm))
            {
                signup_error_text.setText("Xác nhận mật khẩu không khớp, vui lòng thử lại!");
                signup_error_text.setVisible(true);
                return;
            }
            
            try {
                password = EncryptionHelper.toHexString(EncryptionHelper.getSHA(password));
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
            
            AccountDAO accountDAO = new AccountDAO();
            long addResult = accountDAO.addAccount(fullname, username, password, email);
            
            if(addResult==-1)
            {
                signup_error_text.setText("Thông tin đã tồn tại trong hệ thống, vui lòng nhập thông tin khác!");
                signup_error_text.setVisible(true);
                return;
            }
            else
            {
                account = new Account((int)addResult,fullname, username, password, email, false, false, null);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Đăng ký thành công");

                alert.setContentText("Bạn đã đăng ký tài khoản thành công!");
                
                alert.showAndWait();
                stage.close();
            }
        });
    }
    
}
