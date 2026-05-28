package dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectaDBPostgres {

    public static void main(String[] args) {
        getConexao();
    }

    public static Connection getConexao() {

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn =
                    DriverManager.getConnection(
                            "jdbc:postgresql://localhost:5432/cadastroBolsista",
                            "postgres",
                            "1234");
            return conn;
        }catch (ClassNotFoundException ex){
            System.out.println("Classe do drive não encontrada");
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }catch (SQLException ex){
            System.out.println("Alguma configuração para " +
                    "abertura de conexão está errada");
            //ex.printStackTrace();
        }
        return null;
    }

}