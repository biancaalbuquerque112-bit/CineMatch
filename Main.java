import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
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

            javax.swing.Timer relogioSessao = new javax.swing.Timer(10_000, e -> {
                if (Sessao.consumirExpiracao()) {
                    for (java.awt.Window janela : java.awt.Window.getWindows()) {
                        if (janela.isDisplayable()) janela.dispose();
                    }
                    new TelaLogin();
                    JOptionPane.showMessageDialog(null,
                        "Sua sessão de 24 horas terminou.\nEntre novamente para continuar.",
                        "CineMatch - Sessão expirada", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            relogioSessao.start();
        });
    }
}