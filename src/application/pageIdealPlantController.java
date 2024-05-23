package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class pageIdealPlantController {
	
	sqlPlantConnector sqlPDB = new sqlPlantConnector();
	Integer temp;
	Double moist;
	Double moistGap;
	String isPoison;

    @FXML
    private Button btn_searchPlant;

    @FXML
    private CheckBox checkbx_isPoison;

    @FXML
    private ImageView img_plantPreview;

    @FXML
    private Slider slider_moist;

    @FXML
    private Spinner<Integer> spnr_temp;

    @FXML
    private StackPane stackPane;

    @FXML
    private TableColumn<Plant, String> tablecolumn_isPoison;

    @FXML
    private TableColumn<Plant, String> tablecolumn_plantName;

    @FXML
    private TableView<Plant> tableview_plants;
    
    @FXML
    void clicked_btn_searchPlant(ActionEvent event) {	// bitki ara
    	tableview_plants.getItems().clear();
    	System.out.println("✔️ Bitkiler aranıyor...");
    	temp= spnr_temp.getValue();
    	moist= slider_moist.getValue();
    	isPoison= checkbx_isPoison.isSelected() ? "Zehirli" : "Zehirsiz";
    	
		sqlPDB.initialize();
		try {	// Bitkiyi koşula göre ara
			ResultSet resultSet = null;
			Statement statement = null;
			if (checkbx_isPoison.isSelected()){
				statement = sqlPlantConnector.con.createStatement();
	            String query = "SELECT * FROM plant_database WHERE ('"+temp+"' BETWEEN minTemp AND maxTemp) AND (isPoison = '"+isPoison+"' OR isPoison = 'Zehirsiz') AND ('"+moist+"' BETWEEN (moist-10) AND (moist+10) )";

	            resultSet = statement.executeQuery(query);
	            if (resultSet.next()) {	
	            	System.out.println("✔️ Uygun bitki bulundu!");
	            	while (resultSet!=null) {
	                    Plant plantCursor = new Plant(
	                        resultSet.getString("plantName"),
	                        resultSet.getInt("minTemp"),
	                        resultSet.getInt("maxTemp"),
	                        resultSet.getDouble("moist"),
	                        resultSet.getString("isPoison"),
	                        resultSet.getString("image")
	                    );
	                    tableview_plants.getItems().add(plantCursor);
	                    if(resultSet.next()==false) {
	                    	
	                    	break;
	                    }
	                    else {
	                    	resultSet.next();
	                    }              
	            	}
	            } else {
	                System.out.println("⚠ Uygun bitki bulunamadı");
	            }
			}
			else {
				statement = sqlPlantConnector.con.createStatement();
	            String query = "SELECT * FROM plant_database WHERE ( '"+temp+"' BETWEEN minTemp AND maxTemp) AND (isPoison = '"+isPoison+"') AND ('"+moist+"' BETWEEN (moist-10) AND (moist+10))";
	            resultSet = statement.executeQuery(query);
	            if (resultSet.next()) {	
	            	System.out.println("✔️ Uygun bitki bulundu!");
	            	while (resultSet!=null) {
	                    Plant plantCursor = new Plant(
	                        resultSet.getString("plantName"),
	                        resultSet.getInt("minTemp"),
	                        resultSet.getInt("maxTemp"),
	                        resultSet.getDouble("moist"),
	                        resultSet.getString("isPoison"),
	                        resultSet.getString("image")
	                    );
	                    tableview_plants.getItems().add(plantCursor);
	                    if(resultSet.next()==false) {
	                    	
	                    	break;
	                    }
	                    else {
	                    	resultSet.next();
	                    }              
	            	}
	            } else {
	                System.out.println("⚠ Uygun bitki bulunamadı");
	            }
			}
			
            
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("⚠ SQL Error: " + e.getMessage());
            e.printStackTrace();
	        	}
    }
    
    @FXML
    private void clicked_tableview(MouseEvent event) {	// tabloda takılı rowu seç
        if (tableview_plants != null) {
            Plant selectedPlant = tableview_plants.getSelectionModel().getSelectedItem();
            if (selectedPlant != null) {
                String plantName = selectedPlant.getName();
                try {
                    Statement statement = sqlPlantConnector.con.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT image FROM plant_database WHERE plantName = '" + plantName + "'");
                    if (resultSet.next()) {
                        String imagePath = resultSet.getString("image");
                        Image newImage = new Image(imagePath.toString());
                        img_plantPreview.setImage(newImage);
                    } else {
                        System.out.println("⚠ No image found for plant: " + plantName);
                    }
                    resultSet.close();
                    statement.close();
                } catch (SQLException e) {
                    System.out.println("⚠ SQL Error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    
    @FXML
    void initialize() {
    	
    	System.out.println("✔️ pageHerbarium başlatıldı");
    	sqlPDB.initialize();
		
    	//	Tablo
    	tableview_plants.setPlaceholder(new Label("Tablo Boş"));
    	TableViewSelectionModel<Plant> selectionModel = 
    			tableview_plants.getSelectionModel();
    	selectionModel.setSelectionMode(SelectionMode.SINGLE);
    	tableview_plants.setSelectionModel(selectionModel);
    	
    	
    	
    	tablecolumn_plantName.setCellValueFactory(new PropertyValueFactory<Plant, String>("name"));
    	tablecolumn_isPoison.setCellValueFactory(new PropertyValueFactory<Plant, String>("isPoisonous"));
    	
    	//	Spinnerlar
    	SpinnerValueFactory<Integer> valueFactoryMin= 
    			new SpinnerValueFactory.IntegerSpinnerValueFactory(-100, 100);
    	valueFactoryMin.setValue(0);
    	spnr_temp.setValueFactory(valueFactoryMin);

    	

    }

}
