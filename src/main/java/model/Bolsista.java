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
    private int laboratorioId;
    private String nomeLaboratorio;

    public Bolsista() {}

    public Bolsista(int id, String nome, String senha, LocalDate dataNascimento, String curso, String email, String matricula, String cpf, String telefone, boolean ativo, int laboratorioId, String nomeLaboratorio) {
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
        this.laboratorioId = laboratorioId;
        this.nomeLaboratorio = nomeLaboratorio;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getLaboratorioId() { return laboratorioId; }
    public void setLaboratorioId(int laboratorioId) { this.laboratorioId = laboratorioId; }

    public String getNomeLaboratorio() { return nomeLaboratorio; }
    public void setNomeLaboratorio(String nomeLaboratorio) { this.nomeLaboratorio = nomeLaboratorio; }

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
