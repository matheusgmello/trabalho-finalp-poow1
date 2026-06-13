package dev.matheus.cadastroBolsistas.model;

/*
 * representa um professor/coordenador no sistema.
 * herda atributos basicos de Usuario.
 */
public class Professor extends Usuario {

    public Professor() {
        super();
        setTipoUsuario("PROFESSOR");
    }

    public Professor(int id, String nome, String email, String senha, boolean ativo, String fotoUrl) {
        super(id, nome, email, senha, ativo, "PROFESSOR", fotoUrl, null);
    }
}
