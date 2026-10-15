
import java.util.ArrayList;

public class GerenciadorUsuarios {

    private static final ArrayList<Usuario> usuarios =
        new ArrayList<>();

    public static boolean cadastrar(
            String nome, String email, String senha) {

        if (nome.isBlank() || email.isBlank()
                || senha.isBlank()) {
            return false;
        }

        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }

        usuarios.add(new Usuario(nome, email, senha));
        return true;
    }

    public static Usuario autenticar(
            String email, String senha) {

        for (Usuario usuario : usuarios) {

            if (usuario.getEmail().equalsIgnoreCase(email)
                    && usuario.verificarSenha(senha)) {
                return usuario;
            }
        }

        return null;
    }
}
