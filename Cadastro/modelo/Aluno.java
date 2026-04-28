package Cadastro.modelo; // Use o caminho completo se estiver dentro de Cadastro/modelo

public class Aluno {
    private String nome;
    private String email;
    private String senha;
    private String turma;

    // ESTE É O CONSTRUTOR QUE ESTÁ FALTANDO:
    public Aluno(String nome, String email, String senha, String turma) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.turma = turma;
    }

    // Getters (para o seu amigo do banco de dados conseguir ler os dados)
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public String getTurma() { return turma; }
}