package model;

public class Laboratorio {
    private int id;
    private String nome;
    private String areaPesquisa;
    private String tituloProjeto;
    private String status;

    public Laboratorio() {}

    public Laboratorio(int id, String nome, String areaPesquisa, String tituloProjeto, String status) {
        this.id = id;
        this.nome = nome;
        this.areaPesquisa = areaPesquisa;
        this.tituloProjeto = tituloProjeto;
        this.status = status;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getAreaPesquisa() { return areaPesquisa; }
    public void setAreaPesquisa(String areaPesquisa) { this.areaPesquisa = areaPesquisa; }

    public String getTituloProjeto() { return tituloProjeto; }
    public void setTituloProjeto(String tituloProjeto) { this.tituloProjeto = tituloProjeto; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
