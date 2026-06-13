package dev.matheus.cadastroBolsistas.model;

/*
 * representa um projeto vinculado a um laboratorio.
 * um laboratorio pode ter multiplos projetos.
 */
public class Projeto {
    private int id;
    private String nome;
    private String descricao;
    private int laboratorioId;
    private String nomeLaboratorio; // campo calculado preenchido por join
    private boolean ativo;

    public Projeto() {}

    public Projeto(int id, String nome, String descricao, int laboratorioId, String nomeLaboratorio, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.laboratorioId = laboratorioId;
        this.nomeLaboratorio = nomeLaboratorio;
        this.ativo = ativo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getLaboratorioId() { return laboratorioId; }
    public void setLaboratorioId(int laboratorioId) { this.laboratorioId = laboratorioId; }

    public String getNomeLaboratorio() { return nomeLaboratorio; }
    public void setNomeLaboratorio(String nomeLaboratorio) { this.nomeLaboratorio = nomeLaboratorio; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
