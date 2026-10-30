
import java.awt.*;
import javax.swing.*;

public class TelaInicial extends JFrame {

    private final Color FUNDO = new Color(18, 18, 30);
    private final Color ROSA = new Color(255, 75, 100);
    private final Color BRANCO = Color.WHITE;

    public TelaInicial() {

        setTitle("CineMatch - Início");
        setSize(600, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painel = new JPanel();
        painel.setBackground(FUNDO);
        painel.setLayout(
            new BoxLayout(painel, BoxLayout.Y_AXIS)
        );

        painel.setBorder(
            BorderFactory.createEmptyBorder(60, 60, 60, 60)
        );

        JLabel logo = new JLabel("CineMatch");
        logo.setFont(new Font("Arial", Font.BOLD, 44));
        logo.setForeground(ROSA);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel(
            "Um filme em comum. Uma nova conexão."
        );
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitulo.setForeground(BRANCO);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descricao = new JLabel(
            "<html><div style='text-align:center;'>"
            + "Descubra filmes, encontre pessoas com os "
            + "mesmos gostos e crie novas conexões!"
            + "</div></html>"
        );
        descricao.setFont(new Font("Arial", Font.PLAIN, 15));
        descricao.setForeground(Color.LIGHT_GRAY);
        descricao.setAlignmentX(Component.CENTER_ALIGNMENT);
        descricao.setMaximumSize(new Dimension(420, 80));

        JButton descobrir = criarBotao(
            "♥ Descobrir Filmes", ROSA
        );

        JButton catalogo = criarBotao(
            "🎬 Ver Catálogo", new Color(55, 55, 75)
        );

        JButton perfil = criarBotao(
            "👤 Meu Perfil", new Color(55, 55, 75)
        );

        JButton sair = criarBotao(
            "Sair da Conta", new Color(85, 45, 55)
        );

        descobrir.addActionListener(e -> {
            new TelaDescobrir();
            dispose();
        });

        catalogo.addActionListener(e -> {
            new TelaCatalogo();
            dispose();
        });

        perfil.addActionListener(e -> {
        new TelaMeuPerfil();
        
        });

        sair.addActionListener(e -> {
            int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja sair da sua conta?",
                "CineMatch",
                JOptionPane.YES_NO_OPTION
            );

            if (resposta == JOptionPane.YES_OPTION) {
                new TelaLogin();
                dispose();
            }
        });

        painel.add(logo);
        painel.add(Box.createVerticalStrut(15));
        painel.add(subtitulo);
        painel.add(Box.createVerticalStrut(35));
        painel.add(descricao);
        painel.add(Box.createVerticalStrut(30));
        painel.add(descobrir);
        painel.add(Box.createVerticalStrut(15));
        painel.add(catalogo);
        painel.add(Box.createVerticalStrut(15));
        painel.add(perfil);
        painel.add(Box.createVerticalStrut(25));
        painel.add(sair);

        add(painel);
        setVisible(true);
    }

    private JButton criarBotao(String texto, Color cor) {

        JButton botao = new JButton(texto);

        botao.setFont(new Font("Arial", Font.BOLD, 16));
        botao.setBackground(cor);
        botao.setForeground(BRANCO);
        botao.setFocusPainted(false);
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);

        botao.setMaximumSize(new Dimension(350, 50));
        botao.setPreferredSize(new Dimension(350, 50));

        botao.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        return botao;
    }
}
