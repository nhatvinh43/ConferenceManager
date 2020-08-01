/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1712914;

import entities.Location;
import entities.LocationDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author VinhCute
 */
public class AddLocationController implements Initializable {

    private Location location;

    Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public AddLocationController() {
    }

    public AddLocationController(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    
    @FXML
    TextField name;
    @FXML
    TextField capacity;
    @FXML
    TextField address;
    @FXML
    Label error;
    @FXML
    Button save;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        error.setVisible(false);
        save.setOnAction(event ->{
            LocationDAO locationDAO = new LocationDAO();
            
            String locationName = name.getText();
            String locationCapacity = capacity.getText();
            String locationAddress = address.getText();
            
            if(locationName.isEmpty() || locationCapacity.isEmpty() || locationAddress.isEmpty())
            {
                error.setVisible(true);
                error.setText("Vui lòng điền tất cả các trường!");
                return;
            }
            
            if(!locationCapacity.matches("-?\\d+"))
            {
                error.setVisible(true);
                error.setText("Vui lòng điền số cho trường Sức chứa!");
                return;
            }
            Location newLocaion = new Location();
            
            newLocaion.setName(locationName);
            newLocaion.setCapacity(Integer.valueOf(locationCapacity));
            newLocaion.setAddress(locationAddress);
            
            int result = locationDAO.add(newLocaion);
            if(result>0)
            {
                location = newLocaion;
                stage.close();
            }
            else
            {
                error.setVisible(true);
                error.setText("Thông tin địa điểm đã tồn tại!");
                return;
            }
                    
        });
    }    
    
}
