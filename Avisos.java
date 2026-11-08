import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public final class Avisos {
    private static final Color FUNDO = new Color(43, 22, 70);
    private static final Color CORAL = new Color(255, 98, 92);
    private static final Color TEXTO = new Color(247, 243, 250);
    private Avisos() {}

    public static void sucesso(Component parent, String mensagem) {
        exibir(parent, "Tudo certo!", mensagem);
    }
    public static void erro(Component parent, String mensagem) {
        exibir(parent, "Atenção", mensagem);
    }
    public static void informacao(Component parent, String mensagem) {
        exibir(parent, "CineMatch", mensagem);
    }
    private static void exibir(Component parent, String titulo, String mensagem) {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println(titulo + ": " + mensagem);
            return;
        }
        Window dono = parent == null ? null : SwingUtilities.getWindowAncestor(parent);
        JDialog dialogo = new JDialog(dono, "CineMatch", Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JPanel conteudo = new JPanel(new BorderLayout(0, 16));
        conteudo.setBackground(FUNDO);
        conteudo.setBorder(new EmptyBorder(22, 28, 22, 28));
        JLabel cabecalho = new JLabel(titulo);
        cabecalho.setForeground(CORAL);
        cabecalho.setFont(new Font("Times New Roman", Font.BOLD, 23));
        conteudo.add(cabecalho, BorderLayout.NORTH);
        JTextArea corpo = new JTextArea(mensagem);
        corpo.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        corpo.setForeground(TEXTO);
        corpo.setBackground(FUNDO);
        corpo.setEditable(false);
        corpo.setFocusable(false);
        corpo.setLineWrap(true);
        corpo.setWrapStyleWord(true);
        corpo.setRows(3);
        corpo.setColumns(34);
        conteudo.add(corpo, BorderLayout.CENTER);
        JButton fechar = new JButton("Entendi");
        fechar.setForeground(Color.WHITE);
        fechar.setBackground(CORAL);
        fechar.setFont(new Font("Times New Roman", Font.BOLD, 17));
        fechar.setFocusPainted(false);
        fechar.setBorder(new EmptyBorder(10, 25, 10, 25));
        fechar.addActionListener(e -> dialogo.dispose());
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.setOpaque(false);
        rodape.add(fechar);
        conteudo.add(rodape, BorderLayout.SOUTH);
        dialogo.setContentPane(conteudo);
        dialogo.pack();
        dialogo.setMinimumSize(new Dimension(390, 220));
        dialogo.setLocationRelativeTo(parent);
        dialogo.getRootPane().setDefaultButton(fechar);
        dialogo.setVisible(true);
    }
}

