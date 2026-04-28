import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    // Se o seu banco no MySQL Workbench tiver outro nome, mude o final da URL
    private static final String URL = "jdbc:mysql://localhost:3306/quiz_etec";
    private static final String USER = "root"; 
    private static final String PASS = ""; // Coloque sua senha do MySQL aqui (se tiver)

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Erro de conexão: " + e.getMessage());
            return null;
        }
    }
}
