package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class sqlLoginConnector{
	
	//MySQL Connection kısmı
	private String hostName= "root";
	private String hostPassword= "root";
	private String host = "127.0.0.1"; //XAMP için 3307 kullandım, 3306'da portlar çakışıyor 
	
	private String db= "java_potpot";
	private int port= 3306;
	public static Connection con= null;
	
	
	public void initialize() {
		
		System.out.println("✔️ sqlLoginConnector Başlatıldı");
		
		String url= "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.db;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch(ClassNotFoundException e) {
			System.out.println("⚠ Mysql Connector Yok");
			e.printStackTrace();
		}
		
		try {
			sqlLoginConnector.con = DriverManager.getConnection(url,hostName,hostPassword);
			System.out.println("✔️ SQL baglantisi basarili");
		}
		catch(SQLException e) {
			System.out.println("⚠ SQL baglantisi basarisiz");
			e.printStackTrace();
		}
		
		if (con!=null) {
			System.out.println("✔️ Tablo baglantisi basarili");
			createAdmin();
		}
		else {
			System.out.println("⚠ Tablo baglanti basarisiz");
		}
		
		
	}
	
	public void createAdmin() { // Admin yoksa admin yarat (tabloyu boşaltırken default kullanıcı yaratmak için)
	    try {
	        Statement statement = con.createStatement();
	        String query = "SELECT * FROM login_menu WHERE name = 'admin'";
	        ResultSet resultSet = statement.executeQuery(query);

	        if (resultSet.next()) {
	            System.out.println("✔️ Admin hesabi bulundu");
	        } else {
	            String insertQuery = "INSERT INTO login_menu (name, password,isAdmin) VALUES ('admin', 'root',1)";
	            int rowsAffected = statement.executeUpdate(insertQuery);
	            if (rowsAffected > 0) {
	                System.out.println("⚠ Admin bulunamadi, default admin yaratiliyor");
	            } else {
	                System.out.println("⚠ Admin yaratilamadi");
	            }
	        }
	        resultSet.close();
	        statement.close();
	    } catch (SQLException e) {
	        System.out.println("⚠ SQL Baglanti hatasi " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	
	
	
}

