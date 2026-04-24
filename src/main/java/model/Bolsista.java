package model;

import java.time.LocalDate;

public class Bolsista {
    private int id;
    private String nome;
    private String senha;
    private LocalDate dataNascimento;
    private String curso;
    private String email;
    private String matricula;
    private String cpf;
    private String telefone;
    private boolean ativo;

    public Bolsista() {}

    public Bolsista(int id, String nome, String senha, LocalDate dataNascimento, String curso, String email, String matricula, String cpf, String telefone, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.curso = curso;
        this.email = email;
        this.matricula = matricula;
        this.cpf = cpf;
        this.telefone = telefone;
        this.ativo = ativo;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
