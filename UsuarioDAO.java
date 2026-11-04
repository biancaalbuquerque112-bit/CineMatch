import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // Salva o usuário no banco de dados H2
    public boolean cadastrar(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, email, senha, idade, bio, intencao) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setInt(4, usuario.getIdade());
            stmt.setString(5, usuario.getBio());
            stmt.setString(6, usuario.getIntencao());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
            return false;
        }
    }

    // Busca usuário pelo e-mail e senha no banco
    public Usuario autenticar(String email, String senha) {
        String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";

        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setNome(rs.getString("nome"));
                    u.setEmail(rs.getString("email"));
                    u.setSenha(rs.getString("senha"));
                    
                    try {
                        u.setIdade(rs.getInt("idade"));
                    } catch (SQLException ignored) {}
                    
                    try {
                        u.setBio(rs.getString("bio"));
                    } catch (SQLException ignored) {}
                    
                    try {
                        u.setIntencao(rs.getString("intencao"));
                    } catch (SQLException ignored) {}
                    
                    return u;
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao autenticar: " + e.getMessage());
        }

        return null;
    }

    // Atualiza os dados de biografia e intenção do usuário no banco H2
    public boolean atualizarPerfil(String email, String bio, String intencao) {
        String sql = "UPDATE usuario SET bio = ?, intencao = ? WHERE email = ?";

        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bio);
            stmt.setString(2, intencao);
            stmt.setString(3, email);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar perfil do usuário: " + e.getMessage());
            return false;
        }
    }

    // Lista todos os usuários cadastrados no banco H2 (usado pela TelaPessoas)
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario";

        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                u.setSenha(rs.getString("senha"));

                try {
                    u.setIdade(rs.getInt("idade"));
                } catch (SQLException ignored) {}

                try {
                    u.setBio(rs.getString("bio"));
                } catch (SQLException ignored) {}

                try {
                    u.setIntencao(rs.getString("intencao"));
                } catch (SQLException ignored) {}

                lista.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar usuários: " + e.getMessage());
        }

        return lista;
    }
}