package Cadastro.modelo;

public class Professor{
    private String nome;
    private String email;
    private String senha;
    private String disciplina;

    public Professor(String nome, String email, String senha, String disciplina){
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.disciplina = disciplina;
    }

    public String getNome(){return nome;}
    public String getEmail(){return email;}
    public String getSenha(){return senha;}
    public String getDisciplina(){return disciplina;}

}
