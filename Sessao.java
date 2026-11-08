import java.time.Duration;
import java.time.Instant;

public final class Sessao {
    private static Usuario usuario;
    private static Instant inicio;
    private static boolean expirada;

    private Sessao() {}

    public static synchronized Usuario getUsuario() {
        verificarPrazo();
        return usuario;
    }


    public static synchronized void entrar(Usuario novo) {
        // Chamado SOMENTE depois de autenticar e-mail e senha.
        if (novo == null) { sair(); return; }
        usuario = novo;
        inicio = Instant.now();
        expirada = false;
    }

    public static synchronized void atualizarUsuario(Usuario atualizado) {
        verificarPrazo();
        if (usuario != null && atualizado != null
                && usuario.getId() == atualizado.getId()) {
            usuario = atualizado;
        }
    }

    public static synchronized void sair() {
        usuario = null;
        inicio = null;
        expirada = false;
    }

    public static synchronized boolean verificarPrazo() {
        if (usuario != null && inicio != null
                && !Instant.now().isBefore(inicio.plus(Duration.ofHours(24)))) {
            usuario = null;
            inicio = null;
            expirada = true;
        }
        return expirada;
    }

    public static synchronized boolean ativa() {
        verificarPrazo();
        return usuario != null && usuario.getId() > 0;
    }

    public static synchronized boolean adulta() {
        return ativa() && usuario.maiorDeIdade();
    }

    public static synchronized boolean consumirExpiracao() {
        verificarPrazo();
        if (!expirada) return false;
        expirada = false;
        return true;
    }

    static synchronized void ajustarInicioParaTeste(Instant instante) {
        inicio = instante;
    }
}
