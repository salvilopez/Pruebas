
import java.io.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONObject;

@WebServlet("/RecuperaC")
public class RecuperaClientes extends HttpServlet {

private String userName="usuario";
private String password="usuario";
private String url="jdbc:mysql://localhost/clientespueblos";
private String driver="com.mysql.jdbc.Driver";

//DataSource pool;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException {
        
      doPost(request,  response);
    
}
   
    //----------------------------------------

@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

       
    response.setContentType("application/json");
    PrintWriter out=response.getWriter();

        Connection     conn = null;
        Statement      stmt = null;
        String         sqlStr=null;

        try {
       
           //Paso 1: Cargar el driver JDBC.
           Class.forName(driver).newInstance();

        // Paso 2: Conectarse a la Base de Datos utilizando la clase Connection
           conn = DriverManager.getConnection(url, userName, password);
                      
        // Paso 3: Crear sentencias SQL, utilizando objetos de tipo Statement
            //conn=pool.getConnection();
            stmt = conn.createStatement();
          
            JSONObject salida =new JSONObject();
            ResultSet rs=null;
                       
            rs=stmt.executeQuery("select * from clientes");
           
           int i=0;
            while(rs.next()){
                            
             String n = rs.getString("nombre");
             String a = rs.getString("apellidos");
             String nf = rs.getString("nif");
             int    e = rs.getInt("edad");
             String t = rs.getString("sexo");
             String em = rs.getString("email");
             String p = rs.getString("provincia");
             String l = rs.getString("localidad");
             
             Cliente linea = new Cliente(n,a,nf,e,t,em,p,l);
             
             String jsonStr = new com.google.gson.Gson().toJson(linea);  


             JSONObject arrayObj = new JSONObject(jsonStr);
            
             salida.put( Integer.toString(i), jsonStr);
             i++;
            }//while..........
          
            out.print(salida.toString());
            rs.close ();
            stmt.close ();
      
        }catch (Exception exc){ 
            out.println("<html><head><title>Resultado de la consulta</title></head><body>");
            out.println("<p> se ha producido un error = " + exc+"</p>");
            out.println("<p> StrSql = " + sqlStr+"</p>");
            out.println("<p> Error  completo= " + exc+"</p>");
            out.println("</body></html>");
        }finally{
            out.close();
            try{
                if(conn!=null) conn.close(); // Devuelve la conexion al pool
                
            } catch (SQLException ex) {
                Logger.getLogger(RecuperaClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
     
}// Servlet.....


