package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class NavigatorController {
	
	String user_name;	// sol üstteki kullanıcı adı
	boolean isAdmin;	// sol üstteki kullanıcı admin tipi
	
	double xOffset;
	double yOffset;
	
	boolean isWiggling=false;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane borderpane_navigator;
    
    @FXML
    private StackPane stackPane;
    
    @FXML
    private Button btn_herbarium;
    @FXML
    private void clicked_btn_herbarium(ActionEvent event) {	// ortadaki anchoru herbarium tabına dönüştür
    	setStage("herbarium");
    }

    @FXML
    private Button btn_idealEnv;
    @FXML
    private void clicked_btn_idealEnv(ActionEvent event) {	// ortadaki anchoru ideal bitki tabına dönüştür
    	setStage("idealPlant");
    }

    @FXML
    private Button btn_idealPlant;
    @FXML
    private void clicked_btn_idealPlant(ActionEvent event) {	// ortadaki anchoru ideal ortam tabına dönüştür
    	setStage("idealEnv");
    }

    @FXML
    private Button btn_logout;
    
    @FXML
    private void clicked_btn_logout(ActionEvent event) {	// tekrar login menüsüne dön
    	setStage("logout");
    }

    @FXML
    private Button btn_main;
    @FXML
    private void clicked_btn_main(ActionEvent event) {	// ortadaki anchoru menü tabına dönüştür
    	setStage("main");
    }
    
    @FXML
    private Button btn_exit;
    @FXML
    private void clicked_btn_exit(ActionEvent event) {	// çıkış butonu
    	Platform.exit();
    }

    @FXML
    private Button btn_minimize;
    @FXML
    private void clicked_btn_minimize(ActionEvent event) {	// minimize etme tuşu
    	Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();	//	Kaynaktan sahneyi al
        stage.setIconified(true);	// Minimize et
    }
    
    @FXML
    private Label label_username;

    @FXML
    private Label label_usertype;
    
    @FXML
    private ImageView img_logo;
    
    @FXML
    private Button btn_logo;
    @FXML
    private void clicked_btn_logo(ActionEvent event) {	// logoya tıklandı mı
    	createWiggleEffect(img_logo);	// logo titretme
    }
    
    @FXML
    private AnchorPane anchor_herbarium;
    
	public void setUserInfo(String user_name, boolean isAdmin) {	//login controller üzerinden sol üstte gözüken kullanıcı adı ve tipi belirtme
		this.user_name= user_name;
		this.isAdmin= isAdmin;
		
		System.out.print(isAdmin);
		System.out.print(user_name);
		setStage("main");
		setLabelsTop();
	}
	
	public void setStage(String currStage) {	// sahne değiştirme
		if (currStage=="main") {
			try {
		        FXMLLoader loader = new FXMLLoader(getClass().getResource("pageMain.fxml"));
		        Node mainView = loader.load();
		        borderpane_navigator.setCenter(mainView);
		        
		    } catch (IOException e) {
		        e.printStackTrace();
		        System.out.println("Error loading pageMain.fxml");
		    }
		}
		else if (currStage=="herbarium") {
			try {
		        FXMLLoader loader = new FXMLLoader(getClass().getResource("pageHerbarium.fxml"));
		        Node mainView = loader.load();
		        borderpane_navigator.setCenter(mainView);
		        

		    } catch (IOException e) {
		        e.printStackTrace();
		        System.out.println("Error loading pageMain.fxml");
		    }
			
		}
		else if (currStage=="idealPlant") {
			try {
		        FXMLLoader loader = new FXMLLoader(getClass().getResource("pageIdealPlant.fxml"));
		        Node mainView = loader.load();
		        borderpane_navigator.setCenter(mainView);
		        

		    } catch (IOException e) {
		        e.printStackTrace();
		        System.out.println("Error loading pageMain.fxml");
		    }
			
		}
		else if (currStage=="idealEnv") {
			try {
		        FXMLLoader loader = new FXMLLoader(getClass().getResource("pageIdealEnv.fxml"));
		        Node mainView = loader.load();
		        borderpane_navigator.setCenter(mainView);
		        

		    } catch (IOException e) {
		        e.printStackTrace();
		        System.out.println("Error loading pageMain.fxml");
		    }
			
		}
		else if (currStage=="logout") {
			
			try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
	            AnchorPane root = loader.load();
	            Scene scene = new Scene(root);
	            Stage stage = (Stage) btn_logout.getScene().getWindow();
	            
	            stage.setScene(scene);
	            stage.centerOnScreen();
	            
	            //drag-drop
				root.setOnMousePressed(event -> {
	                xOffset = event.getSceneX();
	                yOffset = event.getSceneY();
	            });

	            root.setOnMouseDragged(event -> {
	                stage.setX(event.getScreenX() - xOffset);
	                stage.setY(event.getScreenY() - yOffset);
	            });
	            
	            stage.show();
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
	}
	
	void createWiggleEffect(ImageView imageView) {	// titretme fonksiyonu
    	
		
		if (isWiggling==false) { // Wiggle loopunu engelleme
    	  double wiggleAngle = 5.0; // Dönüş açısı
    	  int totalDuration = 300; // Saniye (milisaniye)
    	  int wiggleDuration = totalDuration / 2; // Saniyede kaç dönüş olacak?
    	  RotateTransition rotateTransition = new RotateTransition(Duration.millis(wiggleDuration), imageView);
    	  rotateTransition.setByAngle(wiggleAngle); // Dönüş açısını ayarlama
    	  rotateTransition.setAutoReverse(true); // oto dönüş
    	  rotateTransition.setCycleCount(4); // sağ sol yap (wiggle animasyonu için)
    	  rotateTransition.setOnFinished(event -> {
    		  imageView.setRotate(0);
    		  isWiggling=false;
    	  	});
    	  rotateTransition.play();
    	  isWiggling=true;
  
    	}
    }
	    
	
	
	void setLabelsTop() {	// üsteki kullanıcı tipini ve adını belrileme
		label_username.setText(user_name);
		Platform.runLater(() -> {
			if(isAdmin) {
				label_usertype.setText("Yönetici");
			}
			else {
				label_usertype.setText("Normal");
			}
        });
		
		anchor_herbarium.setVisible(isAdmin);
	}

	
	@FXML
    void initialize() {	
		System.out.println("✔️ NavigatorController Başlatıldı");
		
    }
}




