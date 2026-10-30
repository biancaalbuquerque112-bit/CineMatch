
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TelaPerfilPessoa extends JFrame {

    private final Color FUNDO = new Color(18, 18, 30);
    private final Color CARD = new Color(32, 32, 48);
    private final Color ROSA = new Color(255, 45, 105);
    private final Color CINZA = new Color(180, 180, 195);

    // Compatibilidade com a TelaDescobrir atual:
    // TelaPerfilPessoa(String, String, String, String)
    public TelaPerfilPessoa(
            String nome,
            String idade,
            String intencao,
            String comentario) {

        this(
            nome,
            converterIdade(idade),
            intencao,
            "Cinema",
            comentario,
            null
        );
    }

    // Versao completa com filme e generos
    public TelaPerfilPessoa(
            String nome,
            int idade,
            String intencao,
            String generos,
            String comentario,
            Filme filme) {

        setTitle("CineMatch - Perfil");
        setSize(720, 820);
        setMinimumSize(new Dimension(580, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(FUNDO);
        principal.setBorder(
            new EmptyBorder(25, 35, 25, 35)
        );

        JLabel logo = new JLabel("♥ CineMatch");
        logo.setFont(new Font("Arial", Font.BOLD, 30));
        logo.setForeground(ROSA);
        logo.setHorizontalAlignment(SwingConstants.CENTER);

        principal.add(logo, BorderLayout.NORTH);

        JPanel conteudo = new JPanel();
        conteudo.setBackground(FUNDO);
        conteudo.setLayout(
            new BoxLayout(conteudo, BoxLayout.Y_AXIS)
        );

        conteudo.add(Box.createVerticalStrut(25));

        JPanel foto = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(ROSA);
                g2.fillOval(5, 5, 140, 140);

                g2.setColor(Color.WHITE);
                g2.setFont(
                    new Font("Arial", Font.BOLD, 55)
                );

                String inicial =
                    nome == null || nome.isEmpty()
                    ? "?"
                    : nome.substring(0, 1).toUpperCase();

                FontMetrics fm = g2.getFontMetrics();

                g2.drawString(
                    inicial,
                    75 - fm.stringWidth(inicial) / 2,
                    93
                );

                g2.dispose();
            }
        };

        foto.setOpaque(false);
        foto.setPreferredSize(
            new Dimension(150, 150)
        );
        foto.setMaximumSize(
            new Dimension(150, 150)
        );
        foto.setAlignmentX(Component.CENTER_ALIGNMENT);

        conteudo.add(foto);
        conteudo.add(Box.createVerticalStrut(20));

        JLabel nomePessoa = criarTexto(
            nome + ", " + idade,
            29,
            Color.WHITE,
            Font.BOLD
        );

        JLabel objetivo = criarTexto(
            intencao,
            17,
            ROSA,
            Font.BOLD
        );

        JLabel interesses = criarTexto(
            generos,
            15,
            CINZA,
            Font.PLAIN
        );

        conteudo.add(nomePessoa);
        conteudo.add(Box.createVerticalStrut(10));
        conteudo.add(objetivo);
        conteudo.add(Box.createVerticalStrut(12));
        conteudo.add(interesses);
        conteudo.add(Box.createVerticalStrut(28));

        String nomeFilme =
            filme == null
            ? "Não informado"
            : filme.getTitulo();

        JLabel tituloFilme = criarTexto(
            "♥ Filme em comum: " + nomeFilme,
            19,
            Color.WHITE,
            Font.BOLD
        );

        conteudo.add(tituloFilme);
        conteudo.add(Box.createVerticalStrut(15));

        JPanel caixaComentario = new JPanel(
            new BorderLayout()
        );

        caixaComentario.setBackground(CARD);
        caixaComentario.setBorder(
            new EmptyBorder(20, 22, 20, 22)
        );
        caixaComentario.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 120)
        );
        caixaComentario.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        JTextArea textoComentario = new JTextArea(
            "\"" + (
                comentario == null
                ? ""
                : comentario
            ) + "\""
        );

        textoComentario.setEditable(false);
        textoComentario.setLineWrap(true);
        textoComentario.setWrapStyleWord(true);
        textoComentario.setBackground(CARD);
        textoComentario.setForeground(Color.WHITE);
        textoComentario.setFont(
            new Font("Arial", Font.ITALIC, 16)
        );

        caixaComentario.add(
            textoComentario,
            BorderLayout.CENTER
        );

        conteudo.add(caixaComentario);
        conteudo.add(Box.createVerticalStrut(25));

        JButton conectar = criarBotao(
            "♥ Solicitar conexão",
            ROSA
        );

        conectar.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );
        conectar.setMaximumSize(
            new Dimension(440, 52)
        );

        conectar.addActionListener(e -> {
            conectar.setText("♥ Solicitação registrada");
            conectar.setEnabled(false);
        });

        conteudo.add(conectar);
        conteudo.add(Box.createVerticalStrut(15));

        JLabel aviso = criarTexto(
            "A conexão depende da aceitação da outra pessoa.",
            13,
            CINZA,
            Font.PLAIN
        );

        conteudo.add(aviso);
        conteudo.add(Box.createVerticalStrut(25));

        JButton voltar = criarBotao(
            "← Voltar",
            new Color(65, 65, 85)
        );

        voltar.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );
        voltar.setMaximumSize(
            new Dimension(440, 45)
        );

        voltar.addActionListener(e -> dispose());

        conteudo.add(voltar);

        JScrollPane scroll = new JScrollPane(conteudo);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(FUNDO);
        scroll.getVerticalScrollBar()
            .setUnitIncrement(16);

        principal.add(
            scroll,
            BorderLayout.CENTER
        );

        setContentPane(principal);
        setVisible(true);
    }

    private static int converterIdade(String idade) {

        if (idade == null) {
            return 18;
        }

        try {
            return Integer.parseInt(
                idade.replaceAll("[^0-9]", "")
            );
        } catch (NumberFormatException e) {
            return 18;
        }
    }

    private JLabel criarTexto(
            String texto,
            int tamanho,
            Color cor,
            int estilo) {

        JLabel label = new JLabel(texto);
        label.setFont(
            new Font("Arial", estilo, tamanho)
        );
        label.setForeground(cor);
        label.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        return label;
    }

    private JButton criarBotao(
            String texto,
            Color cor) {

        JButton botao = new JButton(texto);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(
            new Font("Arial", Font.BOLD, 16)
        );
        botao.setFocusPainted(false);
        botao.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );
        botao.setBorder(
            new EmptyBorder(13, 18, 13, 18)
        );

        return botao;
    }
}
