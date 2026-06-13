package dev.matheus.cadastroBolsistas.model;

import java.util.ArrayList;

/*
 * representa um laboratorio no sistema.
 * possui coordenador associado da tabela professor e lista de projetos vinculados.
 */
public class Laboratorio {
    private int id;
    private String nome;
    private String areaPesquisa;
    private String status;
    private int capacidade;
    private int coordenadorId;
    private String coordenador; // mantido para compatibilidade com as JSP (armazena o nome do coordenador)
    private boolean ativo;
    private ArrayList<Projeto> projetos = new ArrayList<>();

    public Laboratorio() {}

    public Laboratorio(int id, String nome, String areaPesquisa, String status, int capacidade,
                       int coordenadorId, String coordenador, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.areaPesquisa = areaPesquisa;
        this.status = status;
        this.capacidade = capacidade;
        this.coordenadorId = coordenadorId;
        this.coordenador = coordenador;
        this.ativo = ativo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getAreaPesquisa() { return areaPesquisa; }
    public void setAreaPesquisa(String areaPesquisa) { this.areaPesquisa = areaPesquisa; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getCapacidade() { return capacidade; }
    public void setCapacidade(int capacidade) { this.capacidade = capacidade; }

    public int getCoordenadorId() { return coordenadorId; }
    public void setCoordenadorId(int coordenadorId) { this.coordenadorId = coordenadorId; }

    public String getCoordenador() { return coordenador; }
    public void setCoordenador(String coordenador) { this.coordenador = coordenador; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public ArrayList<Projeto> getProjetos() { return projetos; }
    public void setProjetos(ArrayList<Projeto> projetos) { this.projetos = projetos; }
}
