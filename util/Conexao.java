package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String URL = "jdbc:mysql://quiztec-db-hodastiago22-0c1b.a.aivencloud.com:13192/quiztec?ssl-mode=REQUIRED&useSSL=true";
    private static final String USUARIO = "avnadmin";
    private static final String SENHA = "AVNS_mUP33E_70TmMfiP41Ro";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar com o banco: " + e.getMessage());
            return null;
        }
    }
}


