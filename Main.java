import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        // Cria as tabelas no H2 se ainda não existirem
        try {
            ConexaoBanco.inicializarBanco();
        } catch (Exception erro) {
            javax.swing.JOptionPane.showMessageDialog(null,
                "Não foi possível iniciar o banco H2.\n" +
                "Verifique se a biblioteca lib/h2-2.4.240.jar está no classpath.\n\n" +
                erro.getMessage(), "CineMatch", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            new TelaLogin();
        });
    }
}