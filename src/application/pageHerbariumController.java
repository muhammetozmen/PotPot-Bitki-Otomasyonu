package application;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class pageHerbariumController {
	
	sqlPlantConnector sqlPDB = new sqlPlantConnector();
	URI img_path;
	
	@FXML
    private Label lbl_imageStatus;
	
	@FXML
	private ImageView img_plantPreview;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_addPlant;

    @FXML
    private Button btn_removePlant;

    @FXML
    private Button btn_resetInputs;
    
    @FXML
    private Button btn_browseImage;

    @FXML
    private CheckBox checkbx_isPoison;

    @FXML
    private Slider slider_moist;

    @FXML
    private Spinner<Integer> spnr_maxTemp;

    @FXML
    private Spinner<Integer> spnr_minTemp;

    @FXML
    private StackPane stackPane;

    @FXML
    private TableView<Plant> tableview_plants;
    
    @FXML
    private TableColumn<Plant, String> tablecolumn_isPoison;

    @FXML
    private TableColumn<Plant, Integer> tablecolumn_maxTemp;

    @FXML
    private TableColumn<Plant, Integer> tablecolumn_minTemp;

    @FXML
    private TableColumn<Plant, Double> tablecolumn_moist;

    @FXML
    private TableColumn<Plant, String> tablecolumn_plantName;
    
    @FXML
    void clicked_btn_browseImage(ActionEvent event) {	//	bitkiyi ara
    	img_path= openImageFile();
    	if (img_path==null) {
    		lbl_imageStatus.setText("Bitki Resmi Girilmedi");
    		lbl_imageStatus.setTextFill(Color.RED);
    		img_plantPreview.setImage(null);
    		
    	}
    	else {
    		lbl_imageStatus.setTextFill(Color.GREEN);
    		lbl_imageStatus.setText(img_path.toString());
    		Image newImage = new Image(img_path.toString());
            img_plantPreview.setImage(newImage);
    	}
    }
    
    @FXML
    private TextField txtfield_plantName;

    @FXML
    void clicked_btn_addPlant(ActionEvent event) {
    		if (txtfield_plantName.getText()=="") {	// İSİM GİRDİSİ GİRİLMEMİŞ
    		showErrorDialog("Girdi Hatası","Bitki adı girilmedi!");
    		}
    		else if (img_path==null ){	// RESİM GİRİLMEDİ
    		showErrorDialog("Girdi Hatası","Bitki resmi girilmedi!");	
    		}
	    	else {	// İSİM GİRDİSİ GİRİLMİŞ -> PLANT ADLI OBJE YARAT
	    		sqlPDB.initialize();
	    		Plant plant= new Plant(
	        					txtfield_plantName.getText(),
	        					spnr_minTemp.getValue(),
	        					spnr_maxTemp.getValue(),
	        					round(slider_moist.getValue(),2),
	        					(checkbx_isPoison.isSelected() ? "Zehirli" : "Zehirsiz"), 
	        					img_path.toString()
	        			);
	    		try {	// AYNI ADA SAHİP BİTKİ VAR MI KONTROL ET
	                Statement statement = sqlPlantConnector.con.createStatement();
	                String query = "SELECT * FROM plant_database WHERE plantName = '" + plant.name + "'";
	                ResultSet resultSet = statement.executeQuery(query);
	                if (resultSet.next()) {	// Kayıt başarısız
	                	System.out.println("⚠ "+plant.name+" adı ile eslesen bitki zaten var");
	                	showErrorDialog("Girdi Hatası","Var olan bitki girildi, silip yeniden girmeyi deneyebilirsiniz");
	                	
	                } else {
	                    System.out.println(plant.name+"  adı ile bitki yaratiliyor");
	                    tableview_plants.getItems().add(plant);
	                    String insertQuery = "INSERT INTO plant_database (plantName, minTemp, maxTemp, moist, isPoison, image) VALUES ('"+plant.name+"','"+plant.minTemp+"','"+plant.maxTemp+"','"+plant.moisture+"','"+plant.isPoisonous+"','"+plant.image+"')";
	                    statement.executeUpdate(insertQuery);
	                }
	                resultSet.close();
	                statement.close();
	            } catch (SQLException e) {
	                System.out.println("⚠ SQL Error: " + e.getMessage());
	                e.printStackTrace();
	            
	        	
	        	}
	    	}
    }
    
    void loadSQLPlant() {	// SQL içinde önceden yaratılmış bitkileri tabloya çağırır
		
		try {
            Statement statement = sqlPlantConnector.con.createStatement();
            String query = "SELECT * FROM plant_database";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Plant plantCursor = new Plant(
                    resultSet.getString("plantName"),
                    resultSet.getInt("minTemp"),
                    resultSet.getInt("maxTemp"),
                    resultSet.getDouble("moist"),
                    resultSet.getString("isPoison"),
                    resultSet.getString("image")
                );
                tableview_plants.getItems().add(plantCursor);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("⚠ SQL Error: " + e.getMessage());
            e.printStackTrace();

    	}
    	
    }
    	
    
    @FXML
    private Button btn_resetList;
    
    @FXML
    void clicked_btn_resetList(ActionEvent event) {	// BİTKİ LİSTESİNİ SIFIRLA
    	boolean resetListPermission;
    	resetListPermission= showConfirmationDialog("Listeyi Sıfırla","Tüm listeyi sıfırlamaya çalışıyorsunuz","Tüm girdiler silinecektir, onaylıyor musunuz?");
    	if (resetListPermission==true) {
    		try {
                Statement statement = sqlPlantConnector.con.createStatement();
                statement.executeUpdate("TRUNCATE TABLE plant_database");
                tableview_plants.getItems().clear();
            } catch (SQLException e) {
                System.out.println("⚠ SQL Error: " + e.getMessage());
                e.printStackTrace();

        	}
    	}
    }

    @FXML
    void clicked_btn_removePlant(ActionEvent event) {	//SEÇİLİ BİTKİYİ SİL
    	String plantCursor= tableview_plants.getSelectionModel().getSelectedItems().get(0).name;
    	
    	try {
            Statement statement = sqlPlantConnector.con.createStatement();
            statement.executeUpdate("DELETE FROM plant_database WHERE plantName = '" +plantCursor+"'");
        } catch (SQLException e) {
            System.out.println("⚠ SQL Error: " + e.getMessage());
            e.printStackTrace();

    	}
    	int SelectedRow= tableview_plants.getSelectionModel().getSelectedIndex();
        tableview_plants.getItems().remove(SelectedRow);

    }

    @FXML
    void clicked_btn_resetInputs(ActionEvent event) {	// kullanıcı girdilerini sıfırla (input kısmındaki)
    	txtfield_plantName.setText("");
    	spnr_minTemp.getValueFactory().setValue(0);
    	spnr_maxTemp.getValueFactory().setValue(0);
    	slider_moist.setValue(0);
    	checkbx_isPoison.setSelected(false);
    	img_plantPreview.setImage(null);

    	
    }

    @FXML
    void initialize() {
    	
    	System.out.println("✔️ pageHerbarium başlatıldı");
    	sqlPDB.initialize();
		loadSQLPlant();
		
    	//	Tablo
    	tableview_plants.setPlaceholder(new Label("Tablo Boş"));
    	TableViewSelectionModel<Plant> selectionModel = 
    			tableview_plants.getSelectionModel();
    	selectionModel.setSelectionMode(SelectionMode.SINGLE);
    	tableview_plants.setSelectionModel(selectionModel);
    	
    	
    	
    	tablecolumn_plantName.setCellValueFactory(new PropertyValueFactory<Plant, String>("name"));
    	tablecolumn_minTemp.setCellValueFactory(new PropertyValueFactory<Plant, Integer>("minTemp"));
    	tablecolumn_maxTemp.setCellValueFactory(new PropertyValueFactory<Plant, Integer>("maxTemp"));
    	tablecolumn_moist.setCellValueFactory(new PropertyValueFactory<Plant, Double>("moisture"));
    	tablecolumn_isPoison.setCellValueFactory(new PropertyValueFactory<Plant, String>("isPoisonous"));
    	
    	//	Spinnerlar
    	SpinnerValueFactory<Integer> valueFactoryMin= 
    			new SpinnerValueFactory.IntegerSpinnerValueFactory(-100, 100);
    	valueFactoryMin.setValue(0);
    	spnr_minTemp.setValueFactory(valueFactoryMin);
    	SpinnerValueFactory<Integer> valueFactoryMax= 
    			new SpinnerValueFactory.IntegerSpinnerValueFactory(-100, 100);
    	valueFactoryMax.setValue(0);
    	spnr_maxTemp.setValueFactory(valueFactoryMax);
    	

    }
    
    public static double round(double value, int places) {	// nem değerini yuvarla
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    
    private void showErrorDialog(String title, String message) {	// hata
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private boolean showConfirmationDialog(String title, String message, String detail) {	// onaylama
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(detail);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        } 
    }
    
    public static URI openImageFile() {	// file browseri aç, resim seçmek için
        // Create a temporary stage
        Stage stage = new Stage();
        
        FileChooser fileChooser = new FileChooser();
        
        // Set extension filters
        fileChooser.getExtensionFilters().addAll(
            new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        // Show open file dialog
        File selectedFile = fileChooser.showOpenDialog(stage);

        // Return the path of the selected file
        if (selectedFile != null) {
            return selectedFile.toURI();
        } else {
            return null; // No file selected
        }
    }
    
}
