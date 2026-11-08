import java.time.LocalDate;

public class GerenciadorUsuarios {
    private static final UsuarioDAO dao=new UsuarioDAO();

    public static boolean cadastrar(String nome,String email,String senha,LocalDate nascimento) {
        Usuario u=new Usuario(nome,email,senha);
        u.setNascimento(nascimento);
        return dao.cadastrar(u);
    }

    /** Sem nascimento não é permitido cadastrar, pois o CineMatch é 18+. */
    public static boolean cadastrar(String nome,String email,String senha) {
        return false;
    }

    public static Usuario autenticar(String email,String senha) {
        return dao.autenticar(email,senha);
    }
}
