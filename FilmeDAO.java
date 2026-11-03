import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FilmeDAO {

    // Salva um novo filme no banco de dados
    public boolean salvar(Filme filme) {
        String sql = "INSERT INTO filme (titulo, genero, ano, capa, curtidas, nota) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, filme.getTitulo());
            stmt.setString(2, filme.getGenero());
            stmt.setInt(3, filme.getAno());
            stmt.setString(4, filme.getCapa());
            stmt.setInt(5, filme.getCurtidas());
            stmt.setInt(6, filme.getNota());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar filme: " + e.getMessage());
            return false;
        }
    }

    // Retorna todos os filmes cadastrados no banco H2
    public List<Filme> listarTodos() {
        List<Filme> filmes = new ArrayList<>();
        String sql = "SELECT * FROM filme ORDER BY titulo ASC";

        try (Connection conn = ConexaoBanco.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Filme f = new Filme(
                    rs.getString("titulo"),
                    rs.getString("genero"),
                    rs.getInt("ano"),
                    rs.getString("capa")
                );
                f.setCurtidas(rs.getInt("curtidas"));
                f.setNota(rs.getInt("nota"));
                filmes.add(f);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar filmes: " + e.getMessage());
        }

        return filmes;
    }

    // Busca filmes por gênero
    public List<Filme> buscarPorGenero(String genero) {
        List<Filme> filmes = new ArrayList<>();
        String sql = "SELECT * FROM filme WHERE LOWER(genero) LIKE LOWER(?) ORDER BY titulo ASC";

        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + genero + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Filme f = new Filme(
                        rs.getString("titulo"),
                        rs.getString("genero"),
                        rs.getInt("ano"),
                        rs.getString("capa")
                    );
                    f.setCurtidas(rs.getInt("curtidas"));
                    f.setNota(rs.getInt("nota"));
                    filmes.add(f);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar filmes por gênero: " + e.getMessage());
        }

        return filmes;
    }

    // Atualiza curtidas e notas de um filme existente
    public boolean atualizarAvaliacao(Filme filme) {
        String sql = "UPDATE filme SET curtidas = ?, nota = ? WHERE titulo = ?";

        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, filme.getCurtidas());
            stmt.setInt(2, filme.getNota());
            stmt.setString(3, filme.getTitulo());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar avaliação do filme: " + e.getMessage());
            return false;
        }
    }

    // Popula a tabela 'filme' caso esteja vazia na primeira execução
    public void popularBancoSeVazio() {
        String sqlContagem = "SELECT COUNT(*) FROM filme";

        try (Connection conn = ConexaoBanco.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlContagem)) {

            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("Populando o banco H2 com os filmes iniciais...");

                salvar(new Filme("O Poderoso Chefão", "Drama / Crime", 1972, "imagens/poderoso_chefao.jpg"));
                salvar(new Filme("Interestelar", "Ficção Científica", 2014, "imagens/interestelar.jpg"));
                salvar(new Filme("La La Land", "Romance / Musical", 2016, "imagens/lalaland.jpg"));
                salvar(new Filme("Batman: O Cavaleiro das Trevas", "Ação", 2008, "imagens/batman.jpg"));
                salvar(new Filme("Pulp Fiction", "Crime / Drama", 1994, "imagens/pulp_fiction.jpg"));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao checar/popular filmes iniciais: " + e.getMessage());
        }
    }
}