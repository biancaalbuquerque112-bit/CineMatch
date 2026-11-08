
import java.time.LocalDateTime;

/**
 * Representa uma mensagem enviada em uma
 * conexão aceita do CineMatch.
 *
 * Esta classe guarda os dados da mensagem.
 */
public class Mensagem {

    private final long id;
    private final int conexaoId;
    private final int remetenteId;
    private final String texto;
    private final LocalDateTime enviadaEm;

    public Mensagem(long id, int conexaoId, int remetenteId,
                    String texto, LocalDateTime enviadaEm) {

        this.id = id;
        this.conexaoId = conexaoId;
        this.remetenteId = remetenteId;
        this.texto = texto;
        this.enviadaEm = enviadaEm;
    }

    public long getId() {
        return id;
    }

    public int getConexaoId() {
        return conexaoId;
    }

    public int getRemetenteId() {
        return remetenteId;
    }

    public String getTexto() {
        return texto;
    }

    public LocalDateTime getEnviadaEm() {
        return enviadaEm;
    }
}
