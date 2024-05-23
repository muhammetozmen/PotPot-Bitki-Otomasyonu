package application;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class pageIdealEnvController {
	
	private String name;
	sqlPlantConnector sqlPDB = new sqlPlantConnector();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text btn_search;

    @FXML
    private Button btn_searchPlant;

    @FXML
    private ImageView img_plantPreview;

    @FXML
    private Label lbl_isPoison;

    @FXML
    private Label lbl_maxTemp;

    @FXML
    private Label lbl_minTemp;

    @FXML
    private Label lbl_moist;

    @FXML
    private Label lbl_plantStatus;

    @FXML
    private StackPane stackPane;

    @FXML
    private TextField txtfield_plantName;

    @FXML
    void clicked_btn_searchPlant(ActionEvent event) {
    	
    	name= txtfield_plantName.getText();
    	sqlPDB.initialize();
    	
    	try {	// bitki adıyla sistemde ara
    		
            Statement statement = sqlPlantConnector.con.createStatement();
            String query = "SELECT * FROM plant_database WHERE LOWER(plantName) = LOWER('" + name + "')";
            ResultSet resultSet = statement.executeQuery(query);
            
            
            if (resultSet.next()) {
                Plant plantCursor = new Plant(
                    resultSet.getString("plantName"),
                    resultSet.getInt("minTemp"),
                    resultSet.getInt("maxTemp"),
                    resultSet.getDouble("moist"),
                    resultSet.getString("isPoison"),
                    resultSet.getString("image")
                );
                lbl_plantStatus.setText(plantCursor.name+" bitkisi bulundu!");
                lbl_isPoison.setText(plantCursor.isPoisonous);
                lbl_minTemp.setText(String.valueOf(plantCursor.minTemp));
                lbl_maxTemp.setText(String.valueOf(plantCursor.maxTemp));
                lbl_moist.setText(String.valueOf(plantCursor.moisture));
                Image image_new= new Image(plantCursor.image);
                img_plantPreview.setImage(image_new);
            }
            else {
            	lbl_plantStatus.setText("Bitki bulunamadı!");
            	lbl_isPoison.setText("...");
                lbl_minTemp.setText("...");
                lbl_maxTemp.setText("...");
                lbl_moist.setText("...");
                img_plantPreview.setImage(null);
            }
          
            resultSet.close();
            statement.close();
            
        } catch (SQLException e) {
            System.out.println("⚠ SQL Error: " + e.getMessage());
            e.printStackTrace();

    	}
    	
    }

    

    @FXML
    void initialize() {
    	System.out.println("✔️ pageIdealController Başlatıldı");

    }

}
