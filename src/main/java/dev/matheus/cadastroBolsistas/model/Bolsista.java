package dev.matheus.cadastroBolsistas.model;

import java.time.LocalDate;

/*
 * representa um bolsista do sistema.
 * herda de Usuario para autenticacao e perfil basico.
 */
public class Bolsista extends Usuario {
    private LocalDate dataNascimento;
    private String curso;
    private String matricula;
    private String cpf;
    private String telefone;
    private int laboratorioId;
    private Cargo cargo; // papel/cargo do bolsista no laboratorio

    public Bolsista() {
        super();
        setTipoUsuario("BOLSISTA");
    }

    public Bolsista(int id, String nome, String senha, LocalDate dataNascimento, String curso, String email,
                    String matricula, String cpf, String telefone, boolean ativo, int laboratorioId,
                    String nomeLaboratorio, String tipoUsuario, String fotoUrl, Cargo cargo) {
        super(id, nome, email, senha, ativo, tipoUsuario, fotoUrl, nomeLaboratorio);
        this.dataNascimento = dataNascimento;
        this.curso = curso;
        this.matricula = matricula;
        this.cpf = cpf;
        this.telefone = telefone;
        this.laboratorioId = laboratorioId;
        this.cargo = cargo;
    }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public int getLaboratorioId() { return laboratorioId; }
    public void setLaboratorioId(int laboratorioId) { this.laboratorioId = laboratorioId; }

    public Cargo getCargo() { return cargo; }
    public void setCargo(Cargo cargo) { this.cargo = cargo; }
}
