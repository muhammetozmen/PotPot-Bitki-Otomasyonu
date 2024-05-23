package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class sqlPlantConnector{
	
	//MySQL Connection kısmı
	private String hostName= "root";
	private String hostPassword= "root";
	private String host = "127.0.0.1"; //XAMP için 3307 kullandım, 3306'da portlar çakışıyor 
	
	private String db= "java_potpot";
	private int port= 3306;
	public static Connection con= null;
	
	
	public void initialize() {
		System.out.println("✔️ sqlPlantConnector Başlatıldı");
		
		String url= "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.db;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch(ClassNotFoundException e) {
			System.out.println("⚠ Mysql Connector Yok");
			e.printStackTrace();
		}
		
		try {
			sqlPlantConnector.con = DriverManager.getConnection(url,hostName,hostPassword);
			System.out.println("✔️ SQL baglantisi basarili");
		}
		catch(SQLException e) {
			System.out.println("⚠ SQL baglantisi basarisiz");
			e.printStackTrace();
		}
		
		if (con!=null) {
			System.out.println("✔️ Tablo baglantisi basarili");
		}
		else {
			System.out.println("⚠ Tablo baglanti basarisiz");
		}
		
		
	}
	
	
}

