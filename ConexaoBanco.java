import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoBanco {

    // Cria/conecta ao banco de dados "cinematch" em ficheiro local na pasta ./banco/
    private static final String URL = "jdbc:h2:./banco/cinematch;AUTO_SERVER=TRUE";
    private static final String USUARIO = "sa";
    private static final String SENHA = "";

    // Obtém a conexão ativa com o H2
    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    // Cria as tabelas do sistema se ainda não existirem
    public static void inicializarBanco() {
        String sqlUsuario = "CREATE TABLE IF NOT EXISTS usuario ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "nome VARCHAR(100) NOT NULL, "
                + "email VARCHAR(100) UNIQUE NOT NULL, "
                + "senha VARCHAR(255) NOT NULL, "
                + "idade INT, "
                + "bio TEXT, "
                + "intencao VARCHAR(50)"
                + ");";

        String sqlFilme = "CREATE TABLE IF NOT EXISTS filme ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "titulo VARCHAR(150) NOT NULL, "
                + "genero VARCHAR(50) NOT NULL, "
                + "ano INT, "
                + "capa VARCHAR(255), "
                + "curtidas INT DEFAULT 0, "
                + "nota INT DEFAULT 0"
                + ");";

        try (Connection conn = getConexao();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sqlUsuario);
            stmt.execute(sqlFilme);
            System.out.println("Banco de dados H2 inicializado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
        }
    }
}