package dev.matheus.cadastroBolsistas.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * responsavel por abrir a conexao com o banco de dados postgresql.
 * todos os daos chamam getConexao() para obter a conexao.
 * as credenciais estao fixas no codigo — se mudar o banco, atualizar aqui.
 */
public class ConectaDBPostgres {

    public static Connection getConexao() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5436/cadastroBolsista",
                    "postgres",
                    "1234");
            if (conn == null) {
                throw new RuntimeException("Falha ao abrir conexao: conexao obtida e nula.");
            }
            return conn;
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Classe do driver PostgreSQL nao encontrada ao conectar ao banco de dados.", ex);
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao abrir conexao com o banco de dados PostgreSQL na porta 5436.", ex);
        }
    }
}
