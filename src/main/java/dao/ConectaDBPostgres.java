package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * responsavel por abrir a conexao com o banco de dados postgresql.
 * todos os daos chamam getConexao() para obter a conexao.
 * as credenciais estao fixas no codigo — se mudar o banco, atualizar aqui.
 */
public class ConectaDBPostgres {

    public static void main(String[] args) {
        getConexao();
    }

    public static Connection getConexao() {
        try {
            // carrega o driver do postgresql
            Class.forName("org.postgresql.Driver");

            // abre a conexao com as credenciais do banco local
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/cadastroBolsista",
                    "postgres",
                    "1234");
            return conn;
        } catch (ClassNotFoundException ex) {
            System.out.println("classe do driver nao encontrada");
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.out.println("erro ao abrir conexao com o banco");
        }
        return null;
    }
}
