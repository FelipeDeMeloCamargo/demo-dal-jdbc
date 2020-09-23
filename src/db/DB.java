package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {
	//conexao com o banco de dados
	
	private static Connection conn = null;
	
	public static Connection getConnection() {
		if(conn ==null) {
			try {
			Properties props = loadProperties();
			String url = props.getProperty("dburl");
			conn = DriverManager.getConnection(url,props);
			}
			catch(SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
		
		
		return conn;
	}
	//para fechar a conexao
	
	public static void CloseConnection(){
		if(conn != null) {
			try {
			conn.close();
			}
			catch(SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	
	
	private static Properties loadProperties() {
		
		try (FileInputStream fs = new FileInputStream("db.properties")){ //abrir o arquivo de propriedades
			Properties props = new Properties();
			props.load(fs);//faz a leitura do arquivo e guarda dentro do props
			return props;
			
			
			
		}
		catch(IOException e) {
			throw new DbException(e.getMessage());
		}
	}

	public static void CloseStatement(Statement st) { //servira para fechar o statement
		if(st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
		
	}
	public static void CloseResultSet(ResultSet rs) { //servira para fechar o statement
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
		
	}
}
