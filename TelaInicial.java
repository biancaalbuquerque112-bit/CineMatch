
import java.awt.*;
import javax.swing.*;

public class TelaInicial extends JFrame {

    public TelaInicial() {

        setTitle("CineMatch");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painel = new JPanel();
        painel.setBackground(new Color(18, 18, 30));
        painel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(15, 10, 15, 10);

        JLabel titulo = new JLabel("CineMatch");
        titulo.setFont(new Font("Arial", Font.BOLD, 48));
        titulo.setForeground(new Color(255, 75, 100));

        JLabel subtitulo = new JLabel(
            "Descubra seu proximo filme favorito!"
        );
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 20));
        subtitulo.setForeground(Color.WHITE);

        JButton botao = new JButton("Explorar Filmes");
        botao.setFont(new Font("Arial", Font.BOLD, 18));
        botao.setBackground(new Color(255, 75, 100));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setPreferredSize(new Dimension(220, 50));

        
        botao.addActionListener(e -> {
        new TelaCatalogo();
        dispose();
        });

        gbc.gridy = 0;
        painel.add(titulo, gbc);

        gbc.gridy = 1;
        painel.add(subtitulo, gbc);

        gbc.gridy = 2;
        painel.add(botao, gbc);

        add(painel);
        setVisible(true);
    }
}
