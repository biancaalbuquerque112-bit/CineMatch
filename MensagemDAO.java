
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsável por salvar e consultar
 * mensagens no banco de dados H2.
 *
 * Apenas participantes de uma conexão
 * aceita podem conversar.
 */
public class MensagemDAO {

    // =========================================
    // VERIFICAR PERMISSÃO PARA CONVERSAR
    // =========================================

    public boolean podeConversar(int conexaoId, int usuarioId) {

        if (conexaoId <= 0 || usuarioId <= 0) {
            return false;
        }

        String sql =
                "SELECT 1 FROM conexao "
                + "WHERE id = ? "
                + "AND status = 'ACEITA' "
                + "AND (solicitante_id = ? "
                + "OR destinatario_id = ?)";

        try (
                Connection conexao =
                        ConexaoBanco.getConexao();

                PreparedStatement comando =
                        conexao.prepareStatement(sql)
        ) {

            comando.setInt(1, conexaoId);
            comando.setInt(2, usuarioId);
            comando.setInt(3, usuarioId);

            try (
                    ResultSet resultado =
                            comando.executeQuery()
            ) {

                return resultado.next();
            }

        } catch (SQLException e) {

            System.err.println(
                    "Erro ao validar acesso ao chat: "
                    + e.getMessage()
            );

            return false;
        }
    }

    // =========================================
    // ENVIAR E SALVAR MENSAGEM
    // =========================================

    public boolean enviar(
            int conexaoId,
            int remetenteId,
            String texto) {

        if (conexaoId <= 0
                || remetenteId <= 0
                || texto == null) {

            return false;
        }

        String mensagem = texto.trim();

        // Impede mensagens vazias ou muito longas.
        if (mensagem.isEmpty()
                || mensagem.length() > 2000) {

            return false;
        }

        /*
         * O próprio INSERT verifica se o usuário
         * participa de uma conexão aceita.
         *
         * Assim, ninguém pode enviar mensagens
         * para uma conexão que não lhe pertence.
         */

        String sql =
                "INSERT INTO mensagem "
                + "(conexao_id, remetente_id, texto) "
                + "SELECT c.id, ?, ? "
                + "FROM conexao c "
                + "WHERE c.id = ? "
                + "AND c.status = 'ACEITA' "
                + "AND (c.solicitante_id = ? "
                + "OR c.destinatario_id = ?)";

        try (
                Connection conexao =
                        ConexaoBanco.getConexao();

                PreparedStatement comando =
                        conexao.prepareStatement(sql)
        ) {

            comando.setInt(1, remetenteId);
            comando.setString(2, mensagem);
            comando.setInt(3, conexaoId);
            comando.setInt(4, remetenteId);
            comando.setInt(5, remetenteId);

            // Uma linha inserida significa sucesso.
            return comando.executeUpdate() == 1;

        } catch (SQLException e) {

            System.err.println(
                    "Erro ao salvar mensagem: "
                    + e.getMessage()
            );

            return false;
        }
    }

    // =========================================
    // CARREGAR MENSAGENS DO CHAT
    // =========================================

    public List<Mensagem> listar(
            int conexaoId,
            int usuarioId) {

        List<Mensagem> mensagens =
                new ArrayList<>();

        if (conexaoId <= 0 || usuarioId <= 0) {
            return mensagens;
        }

        /*
         * Consulta as mensagens em ordem de envio.
         *
         * A consulta também verifica a permissão
         * do usuário para ler a conversa.
         */

        String sql =
                "SELECT m.id, "
                + "m.conexao_id, "
                + "m.remetente_id, "
                + "m.texto, "
                + "m.enviada_em "
                + "FROM mensagem m "
                + "INNER JOIN conexao c "
                + "ON c.id = m.conexao_id "
                + "WHERE m.conexao_id = ? "
                + "AND c.status = 'ACEITA' "
                + "AND (c.solicitante_id = ? "
                + "OR c.destinatario_id = ?) "
                + "ORDER BY m.id ASC";

        try (
                Connection conexao =
                        ConexaoBanco.getConexao();

                PreparedStatement comando =
                        conexao.prepareStatement(sql)
        ) {

            comando.setInt(1, conexaoId);
            comando.setInt(2, usuarioId);
            comando.setInt(3, usuarioId);

            try (
                    ResultSet resultado =
                            comando.executeQuery()
            ) {

                while (resultado.next()) {

                    Timestamp horario =
                            resultado.getTimestamp(
                                    "enviada_em"
                            );

                    Mensagem mensagem = new Mensagem(
                            resultado.getLong("id"),
                            resultado.getInt("conexao_id"),
                            resultado.getInt("remetente_id"),
                            resultado.getString("texto"),
                            horario.toLocalDateTime()
                    );

                    mensagens.add(mensagem);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Erro ao carregar mensagens: "
                    + e.getMessage()
            );
        }

        return mensagens;
    }
}
