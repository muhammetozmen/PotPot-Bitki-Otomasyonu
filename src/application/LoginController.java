package application;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginController {
	
	sqlLoginConnector sqlDB = new sqlLoginConnector();
	
	public String user_name;	//	Navigatöre yollanacak kullanıcı adı
	public boolean isAdmin;	// Navigatöre yollanacak kullanıcı tipi
	private String user_password;	// şifre
	private boolean isSignIn;	// giriş tipi nedir?
	
	//	drag-drop için mouse konumları
	double xOffset;
	double yOffset;
	

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_enter;
    @FXML
    private void clicked_btn_enter(ActionEvent event) {	// Enter butonuna tıklandı mı
    	//kullanıcı girdilerini al
    	this.user_name= txtfield_userName.getText();
    	this.user_password= txtfield_userPassword.getText();
    	this.isSignIn= radioButton_signIn.isSelected();
    	sqlDB.initialize();	//	sql başlat
    	TryLogin();	// giriş yap fonksiyonu
    	
    }
    
    void TryLogin() {
		if (isSignIn) {	//giriş-login mi?
	    	try {	// Başarılı giriş
	            Statement statement = sqlLoginConnector.con.createStatement();
	            String query = "SELECT * FROM login_menu WHERE name = '" + user_name + "' AND password = '" + user_password + "'";
	            ResultSet resultSet = statement.executeQuery(query);
	            
	            if (resultSet.next()) {
	            		if (resultSet.getInt("isAdmin")==1) {	//tinybit 0 ise false 1 ise true
	            			isAdmin=true;
	            		}
	            		else {
	            			isAdmin=false;
	            		}
		                System.out.println("✔️ Giris yapildi");
		                loadNavigator(user_name, isAdmin);
	                
	            } else {
	                System.out.println("⚠ "+ user_name+" kullanıcı adı ve "+user_password+ " şifresiyle ile eslesen hesap bulunamadi");
	                label_status.setTextFill(Color.RED);
	                label_status.setText("⚠ Kullanıcı adı veya şifre yanlış");
	            }
	            resultSet.close();	
	            statement.close();
	        } catch (SQLException e) {
	            System.out.println("⚠ SQL Hatasi: " + e.getMessage());
	            e.printStackTrace();
	        }
    	}
    	else {	// kayıt-signup mı?
    		try {
                Statement statement = sqlLoginConnector.con.createStatement();
                String query = "SELECT * FROM login_menu WHERE name = '" + user_name + "'";
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {	// Kayıt başarısız
                	System.out.println("⚠ "+user_name+" kullanıcı adı ve "+user_password+ " şifresiyle ile eslesen hesap zaten var");
                	label_status.setTextFill(Color.ORANGERED);
                	label_status.setText("Böyle bir hesap var, giriş yapmayı deneyin");
                } else {
                	isAdmin=false;
                    System.out.println(user_name+" kullanıcı adı ve "+user_password+ " şifresiyle ile hesap yaratiliyor");
                    String insertQuery = "INSERT INTO login_menu (name, password,isAdmin) VALUES ('"+user_name+"','"+user_password+"',0)";
                    statement.executeUpdate(insertQuery);
                    loadNavigator(user_name, isAdmin);
                }
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                System.out.println("⚠ SQL Error: " + e.getMessage());
                e.printStackTrace();
            }
    	}
	    }
    
    void loadNavigator(String user_name, boolean isAdmin) {	//	navigatörü yükle
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Navigator.fxml"));
            BorderPane root = loader.load();

            NavigatorController controller = loader.getController();
            controller.setUserInfo(user_name, isAdmin);

            Scene scene = new Scene(root);
            Stage stage = (Stage) btn_enter.getScene().getWindow();
            
            stage.setScene(scene);
            stage.centerOnScreen();
            
            //	drag-drop 
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

    @FXML
    private Button btn_exit;
    @FXML
    private void clicked_btn_exit(ActionEvent event) {	// kapama tuşu
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
    private ImageView img_guardWorm;

    @FXML
    private Label label_status;

    @FXML
    private ToggleGroup loginGroup;

    @FXML
    private RadioButton radioButton_signIn;

    @FXML
    private RadioButton radioButton_signUp;

    @FXML
    private TextField txtfield_userName;

    @FXML
    private PasswordField txtfield_userPassword;

    @FXML
    void initialize() {
    	System.out.println("✔️ LoginController Başlatıldı");
    	wormCloseEyes();	// solucan göz kapama

    }
    
    void wormCloseEyes(){	// şifre girilirken solucanın gözü kapatılıyor
    	//Solucan göz kapatma
    	txtfield_userPassword.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { // İmleç girdi kısmına girdi
            	Image newImage = new Image(getClass().getResource("img/worm_login_writing.png").toExternalForm());
            	img_guardWorm.setImage(newImage);
            } else { // İmleç girdi kısmından çıktı
            	Image newImage = new Image(getClass().getResource("img/worm_login.png").toExternalForm());
            	img_guardWorm.setImage(newImage);
            }
        });
    	//Solucan göz kapatma (tekrar kısmı için)
    	txtfield_userPassword.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { // İmleç girdi kısmına girdi
            	Image newImage = new Image(getClass().getResource("img/worm_login_writing.png").toExternalForm());
            	img_guardWorm.setImage(newImage);
            } else { // İmleç girdi kısmından çıktı
            	Image newImage = new Image(getClass().getResource("img/worm_login.png").toExternalForm());
            	img_guardWorm.setImage(newImage);
            }
        });
    }

}
