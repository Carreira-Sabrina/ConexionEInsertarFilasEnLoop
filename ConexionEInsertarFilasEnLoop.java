import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class ConexionEInsertarFilasEnLoop {

	public static void main(String[] args) {
		//Creo un HashMap de paises, los cuales pienso guardar en la base de datos
		HashMap<String,String> paisesEuropa = new HashMap<String,String>();
		//Agrego algunos paises al HashMap
		paisesEuropa.put("Francia", "Paris");
		paisesEuropa.put("Italia", "Roma");
		paisesEuropa.put("España", "Madrid");
		
		//Se crea la conexion con la base de datos
		//Cuidado ! estos parametros pueden variar !
    	String url = "jdbc:mysql://localhost:3306/"; //una barra sola al final !
    	String db = "sampledb"; //nombre de la base de datos a la que se intenta conectar
    	String user = "root"; //default
    	String pass = "LtCdrData_4249"; //el password que se tenga en la base de datos
    	
    	try {
    		//Cuidado ! El "cj" antes de .jdbc es propio de versiones más actualizadas
    		//del driver, "com.mysql.jdbc.Driver" està deprecated en las versiones
    		//más modernas de Java... o eso fue lo que me dijo el error que me tiró :)
    		Class.forName("com.mysql.cj.jdbc.Driver");
        	Connection cnx = DriverManager.getConnection(url + db, user, pass);
        	//El codigo SQL para insertar una fila (los ? son parametros !)
        	String sqlAgregarPais = "INSERT INTO paises(nombre,capital) VALUES(?,?)";
        	//Usando PreparedStatemet en vez de Statement se pueden pasar parámetros !
        	PreparedStatement pst = cnx.prepareStatement(sqlAgregarPais);
        	
        	//Es hora de iterar por el HashMap y guardar su contenido en la BD
        	for(String p : paisesEuropa.keySet()) {
        		pst.setString(1,p); //el campo 1 es la key del HashMap
        		pst.setString(2, paisesEuropa.get(p));//el campo 2 es el value del HashMap
        		//Si no ejecuto el update, parece que no pasa nada !
        		pst.executeUpdate();
        	}
        	
        	//Ahora muestro como quedó la base de datos con lo que le agregué
        	String query1 = "select * from paises";	
        	Statement st = cnx.createStatement();
        	ResultSet rs = st.executeQuery(query1);
        	//Recorro el ResultSet como si fuera una array de resultados !
        	while(rs.next()) {
        		int id = rs.getInt(1);//un int en la columna 1
        		String pais = rs.getString(2);
        		String capital = rs.getString(3);
        		System.out.println(id + " " + pais + " " + capital);
        	}
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
		
		

	}

}
