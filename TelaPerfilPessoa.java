
import java.awt.*;
import javax.swing.*;

public class TelaPerfilPessoa extends JFrame {

    private final Color FUNDO = new Color(18, 18, 30);
    private final Color CARD = new Color(32, 32, 48);
    private final Color ROSA = new Color(255, 75, 100);

    public TelaPerfilPessoa(
            String nome,
            String interesse,
            String comentario,
            String filme) {

        setTitle("CineMatch - Perfil");
        setSize(550, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel();
        painel.setBackground(FUNDO);
        painel.setLayout(
            new BoxLayout(painel, BoxLayout.Y_AXIS)
        );
        painel.setBorder(BorderFactory.createEmptyBorder(
            35, 45, 35, 45
        ));

        JLabel logo = criarTexto(
            "CineMatch", 28, ROSA
        );

        JLabel avatar = criarTexto(
            "●", 90, ROSA
        );

        JLabel nomeLabel = criarTexto(
            nome, 27, Color.WHITE
        );

        JLabel interesseLabel = criarTexto(
            interesse, 15, ROSA
        );

        JLabel filmeLabel = criarTexto(
            "Filme em comum: " + filme,
            17, Color.WHITE
        );

        JTextArea comentarioArea = new JTextArea(
            "\"" + comentario + "\""
        );
        comentarioArea.setFont(
            new Font("Arial", Font.ITALIC, 15)
        );
        comentarioArea.setForeground(Color.LIGHT_GRAY);
        comentarioArea.setBackground(CARD);
        comentarioArea.setLineWrap(true);
        comentarioArea.setWrapStyleWord(true);
        comentarioArea.setEditable(false);
        comentarioArea.setBorder(
            BorderFactory.createEmptyBorder(
                15, 15, 15, 15
            )
        );
        comentarioArea.setMaximumSize(
            new Dimension(430, 110)
        );

        JButton conectar = new JButton(
            "♥ Solicitar conexão"
        );
        conectar.setBackground(ROSA);
        conectar.setForeground(Color.WHITE);
        conectar.setFont(
            new Font("Arial", Font.BOLD, 16)
        );
        conectar.setFocusPainted(false);
        conectar.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );
        conectar.setMaximumSize(
            new Dimension(350, 50)
        );

        JLabel status = criarTexto(
            "A conexão depende da aceitação da outra pessoa.",
            12, Color.LIGHT_GRAY
        );

        conectar.addActionListener(e -> {

            conectar.setText("Solicitação simulada");
            conectar.setEnabled(false);

            status.setText(
                "Demonstração: nenhuma solicitação real foi enviada."
            );
        });

        JButton voltar = new JButton("← Voltar");
        voltar.setBackground(new Color(65, 65, 85));
        voltar.setForeground(Color.WHITE);
        voltar.setFocusPainted(false);
        voltar.setAlignmentX(Component.CENTER_ALIGNMENT);
        voltar.setMaximumSize(
            new Dimension(350, 45)
        );

        voltar.addActionListener(e -> dispose());

        painel.add(logo);
        painel.add(Box.createVerticalStrut(25));
        painel.add(avatar);
        painel.add(Box.createVerticalStrut(12));
        painel.add(nomeLabel);
        painel.add(Box.createVerticalStrut(10));
        painel.add(interesseLabel);
        painel.add(Box.createVerticalStrut(35));
        painel.add(filmeLabel);
        painel.add(Box.createVerticalStrut(20));
        painel.add(comentarioArea);
        painel.add(Box.createVerticalStrut(25));
        painel.add(conectar);
        painel.add(Box.createVerticalStrut(12));
        painel.add(status);
        painel.add(Box.createVerticalStrut(20));
        painel.add(voltar);

        add(painel);
        setVisible(true);
    }

    private JLabel criarTexto(
            String valor, int tamanho, Color cor) {

        JLabel label = new JLabel(valor);
        label.setFont(
            new Font("Arial", Font.BOLD, tamanho)
        );
        label.setForeground(cor);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
}
