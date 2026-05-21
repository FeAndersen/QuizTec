package DAO;

import Professor.ProfessorModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import util.Conexao;
import util.Sessao;

public class ProfessorDAO {

    public boolean cadastrar(ProfessorModel professor) {
        String sql = "INSERT INTO professor (nome_professor, email_professor, senha_professor) VALUES (?, ?, ?)";
        
        try (Connection con = Conexao.conectar()) {
            if (con == null) return false;

            PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, professor.getNome());
            stmt.setString(2, professor.getEmail());
            stmt.setString(3, professor.getSenha());
            
            stmt.executeUpdate();

            // Correção aqui: método correto é getGeneratedKeys()
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                // Correção aqui: tiramos o texto columnIndex
                Sessao.idUsuario = rs.getInt(1);
                Sessao.tipoUsuario = "professor";
            }
            return true;
            
        } catch (Exception ex) {
            System.out.println("Erro no ProfessorDAO ao cadastrar: " + ex.getMessage());
            return false;
        }
    }
}