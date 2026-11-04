/** Usuário autenticado na execução atual do programa. */
public final class Sessao {
    private static Usuario usuario;
    private Sessao() {}
    public static Usuario getUsuario() { return usuario; }
    public static void entrar(Usuario novo) { usuario = novo; }
    public static void sair() { usuario = null; }
    public static boolean adulta() { return ativa() && usuario.maiorDeIdade(); }
    public static boolean ativa() { return usuario != null && usuario.getId() > 0; }
}

