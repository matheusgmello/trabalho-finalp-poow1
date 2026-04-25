package model;

import java.time.LocalDate;

public class Frequencia {
    private int id;
    private int bolsistaId;
    private String nomeBolsista;
    private LocalDate data;
    private double horasTrabalhadas;
    private String descricao;

    public Frequencia() {}

    public Frequencia(int id, int bolsistaId, LocalDate data, double horasTrabalhadas, String descricao) {
        this.id = id;
        this.bolsistaId = bolsistaId;
        this.data = data;
        this.horasTrabalhadas = horasTrabalhadas;
        this.descricao = descricao;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBolsistaId() { return bolsistaId; }
    public void setBolsistaId(int bolsistaId) { this.bolsistaId = bolsistaId; }

    public String getNomeBolsista() { return nomeBolsista; }
    public void setNomeBolsista(String nomeBolsista) { this.nomeBolsista = nomeBolsista; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public double getHorasTrabalhadas() { return horasTrabalhadas; }
    public void setHorasTrabalhadas(double horasTrabalhadas) { this.horasTrabalhadas = horasTrabalhadas; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
