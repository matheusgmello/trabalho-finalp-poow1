package dev.matheus.cadastroBolsistas.model;

/*
 * representa um usuario generico no sistema (pode ser ADMIN, BOLSISTA ou PROFESSOR).
 * contem os campos de autenticacao e informacoes basicas.
 */
public abstract class Usuario {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private boolean ativo;
    private String tipoUsuario; // 'ADMIN', 'BOLSISTA', 'PROFESSOR'
    private String fotoUrl;

    public Usuario() {}

    public Usuario(int id, String nome, String email, String senha, boolean ativo, String tipoUsuario, String fotoUrl) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.ativo = ativo;
        this.tipoUsuario = tipoUsuario;
        this.fotoUrl = fotoUrl;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public boolean isAdmin() {
        return "ADMIN".equals(this.tipoUsuario);
    }

    public boolean isBolsista() {
        return "BOLSISTA".equals(this.tipoUsuario);
    }

    public boolean isProfessor() {
        return "PROFESSOR".equals(this.tipoUsuario);
    }
}
