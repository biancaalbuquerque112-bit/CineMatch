import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComentarioDAO {

    public ComentarioDAO() {
        criarTabela();
    }

    private void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS comentario ("
                   + "id INT AUTO_INCREMENT PRIMARY KEY, "
                   + "filme_id INT, "
                   + "resposta_a INT DEFAULT 0, "
                   + "curtidas INT DEFAULT 0, "
                   + "titulo_filme VARCHAR(255), "
                   + "autor VARCHAR(255), "
                   + "texto TEXT"
                   + ");";
        try (Connection conn = ConexaoBanco.getConexao();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void inserir(Comentario c) {
        String sql = "INSERT INTO comentario (filme_id, resposta_a, curtidas, titulo_filme, autor, texto) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, c.getFilmeId());
            stmt.setInt(2, c.getRespostaA());
            stmt.setInt(3, c.getCurtidas());
            stmt.setString(4, c.getTituloFilme());
            stmt.setString(5, c.getAutor());
            stmt.setString(6, c.getTexto());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Comentario> listarPorFilme(int filmeId) {
        List<Comentario> lista = new ArrayList<>();
        String sql = "SELECT * FROM comentario WHERE filme_id = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, filmeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Comentario c = new Comentario();
                c.setId(rs.getInt("id"));
                c.setFilmeId(rs.getInt("filme_id"));
                c.setRespostaA(rs.getInt("resposta_a"));
                c.setCurtidas(rs.getInt("curtidas"));
                c.setTituloFilme(rs.getString("titulo_filme"));
                c.setAutor(rs.getString("autor"));
                c.setTexto(rs.getString("texto"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void curtir(int id) {
        String sql = "UPDATE comentario SET curtidas = curtidas + 1 WHERE id = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void limparPorFilme(int filmeId) {
        String sql = "DELETE FROM comentario WHERE filme_id = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, filmeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}