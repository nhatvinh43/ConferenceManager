/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1712914;

import entities.Account;
import entities.AccountDAO;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
        loader.setController(new MainScreenController());
        
        Parent root = loader.load();
        
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setWidth(1200);
        stage.setHeight(800);
        stage.setTitle("Quản lý hội nghị");
        stage.setResizable(false);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       launch(args);
       
    }
    
}
