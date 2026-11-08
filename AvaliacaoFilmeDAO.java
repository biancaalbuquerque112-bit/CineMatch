
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Acesso ao banco de dados das avaliações por estrelas.
 *
 * Cada usuário pode ter apenas UMA avaliação por filme.
 * Se ele avaliar novamente, sua nota anterior será atualizada.
 */
public class AvaliacaoFilmeDAO {

    public static class Estatisticas {
        private final int quantidade;
        private final double media;

        public Estatisticas(int quantidade, double media) {
            this.quantidade = quantidade;
            this.media = media;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public double getMedia() {
            return media;
        }

        public int getPercentual() {
            return (int) Math.round((media / 5.0) * 100.0);
        }
    }

    public AvaliacaoFilmeDAO() {
        // Cria a tabela somente se ainda não existir.
        // Não apaga nem altera as tabelas antigas do CineMatch.
        String sql = "CREATE TABLE IF NOT EXISTS avaliacao_filme ("
                + "usuario_id INT NOT NULL, "
                + "filme_id INT NOT NULL, "
                + "estrelas INT NOT NULL CHECK (estrelas BETWEEN 1 AND 5), "
                + "PRIMARY KEY (usuario_id, filme_id), "
                + "FOREIGN KEY (usuario_id) REFERENCES usuario(id), "
                + "FOREIGN KEY (filme_id) REFERENCES filme(id)"
                + ")";

        try (Connection conexao = ConexaoBanco.getConexao();
             Statement comando = conexao.createStatement()) {
            comando.execute(sql);
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Não foi possível preparar as avaliações: "
                    + e.getMessage(), e);
        }
    }

    public boolean avaliar(int usuarioId, int filmeId, int estrelas) {
        if (usuarioId <= 0 || filmeId <= 0
                || estrelas < 1 || estrelas > 5) {
            return false;
        }

        // MERGE atualiza a avaliação anterior ou cria uma nova.
        String sql = "MERGE INTO avaliacao_filme "
                + "(usuario_id, filme_id, estrelas) "
                + "KEY(usuario_id, filme_id) VALUES (?, ?, ?)";

        try (Connection conexao = ConexaoBanco.getConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, usuarioId);
            comando.setInt(2, filmeId);
            comando.setInt(3, estrelas);
            return comando.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Erro ao registrar avaliação: " + e.getMessage());
            return false;
        }
    }

    public Map<Integer, Integer> listarAvaliacoesDoUsuario(int usuarioId) {
        Map<Integer, Integer> resultado = new HashMap<>();

        if (usuarioId <= 0) {
            return resultado;
        }

        String sql = "SELECT filme_id, estrelas FROM avaliacao_filme "
                + "WHERE usuario_id = ?";

        try (Connection conexao = ConexaoBanco.getConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, usuarioId);

            try (ResultSet registros = comando.executeQuery()) {
                while (registros.next()) {
                    resultado.put(
                            registros.getInt("filme_id"),
                            registros.getInt("estrelas")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar notas do usuário: "
                    + e.getMessage());
        }

        return resultado;
    }

    public Map<Integer, Estatisticas> listarEstatisticas() {
        Map<Integer, Estatisticas> resultado = new HashMap<>();

        String sql = "SELECT filme_id, COUNT(*) AS total, "
                + "AVG(estrelas) AS media "
                + "FROM avaliacao_filme GROUP BY filme_id";

        try (Connection conexao = ConexaoBanco.getConexao();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet registros = comando.executeQuery()) {

            while (registros.next()) {
                resultado.put(
                        registros.getInt("filme_id"),
                        new Estatisticas(
                                registros.getInt("total"),
                                registros.getDouble("media")
                        )
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao calcular média dos filmes: "
                    + e.getMessage());
        }

        return resultado;
    }
}
