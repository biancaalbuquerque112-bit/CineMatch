import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        // Cria as tabelas no H2 se ainda não existirem
        ConexaoBanco.inicializarBanco();

        SwingUtilities.invokeLater(() -> {
            new TelaLogin();
        });
    }
}