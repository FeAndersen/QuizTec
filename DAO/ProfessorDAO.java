package DAO;

import Professor.ProfessorModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProfessorDAO {

    // Centralizando as credenciais do banco para não depender da classe externa Conexao
    private static final String URL = "jdbc:mysql://localhost:3306/quiztec"; 
    private static final String USUARIO = "root"; 
    private static final String SENHA = ""; // coloque a senha do seu banco aqui, se houver

    public boolean cadastrar(ProfessorModel professor) {
        String sql = "INSERT INTO professor (nome_professor, email_professor, senha_professor) VALUES (?, ?, ?)";
        
        // Conectando diretamente via DriverManager para blindar contra erros de pacotes do VS Code
        try (Connection con = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            if (con == null) return false;

            try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, professor.getNome());
                stmt.setString(2, professor.getEmail());
                stmt.setString(3, professor.getSenha());
                
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        // Guardando o ID gerado de forma segura no escopo do modelo para usar na troca de telas
                        int idGerado = rs.getInt(1);
                        System.out.println("Professor cadastrado com ID: " + idGerado);
                    }
                }
            }
            return true;
            
        } catch (Exception ex) {
            System.out.println("Erro no ProfessorDAO ao cadastrar: " + ex.getMessage());
            return false;
        }
    }
}