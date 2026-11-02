
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class TelaInicial extends JFrame {

    // Cores da identidade visual do CineMatch
    private final Color FUNDO = new Color(43, 22, 70);
    private final Color CORAL = new Color(255, 98, 92);
    private final Color ROXO_BOTAO = new Color(65, 37, 93);
    private final Color BRANCO = new Color(247, 243, 250);

    // Fontes utilizadas no aplicativo
    private final Font FONTE_LOGO =
            new Font("Nimbus Sans", Font.BOLD, 38);

    private final Font FONTE_TITULO =
            new Font("Modern No. 20", Font.BOLD, 25);

    private final Font FONTE_TEXTO =
            new Font("Modern No. 20", Font.PLAIN, 18);

    private final Font FONTE_BOTAO =
            new Font("Modern No. 20", Font.BOLD, 20);

    // Mantém compatibilidade com chamadas antigas
    public TelaInicial() {
        this("");
    }

    // Recebe o nome do usuário após o login
    public TelaInicial(String nomeUsuario) {

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
                BorderFactory.createEmptyBorder(35, 60, 35, 60)
        );

        // Ícone original do CineMatch
        JLabel icone = new JLabel();

        ImageIcon imagemIcone = new ImageIcon(
                "imagens/icone_cinematch.png"
        );

        if (imagemIcone.getIconWidth() > 0) {

            Image imagemAjustada = imagemIcone.getImage()
                    .getScaledInstance(
                            86, 61, Image.SCALE_SMOOTH
                    );

            icone.setIcon(new ImageIcon(imagemAjustada));
        }

        // Nome CineMatch com Nimbus Sans
        JLabel nomeAplicativo = new JLabel("CineMatch");
        nomeAplicativo.setFont(FONTE_LOGO);
        nomeAplicativo.setForeground(BRANCO);

        // Cabeçalho com ícone e nome
        JPanel cabecalho = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 0)
        );

        cabecalho.setOpaque(false);
        cabecalho.add(icone);
        cabecalho.add(nomeAplicativo);

        cabecalho.setAlignmentX(Component.CENTER_ALIGNMENT);
        cabecalho.setMaximumSize(new Dimension(480, 68));

        // Slogan original do projeto
        JLabel subtitulo = new JLabel(
                "Um filme em comum. Uma nova conexão."
        );

        subtitulo.setFont(FONTE_TEXTO);
        subtitulo.setForeground(BRANCO);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Mensagem de boas-vindas personalizada
        JLabel boasVindas = new JLabel();

        if (nomeUsuario != null && !nomeUsuario.trim().isEmpty()) {
            boasVindas.setText(
                    "Olá, " + nomeUsuario + "!"
            );
        } else {
            boasVindas.setText("Boas-vindas ao CineMatch!");
        }

        boasVindas.setFont(FONTE_TITULO);
        boasVindas.setForeground(BRANCO);
        boasVindas.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel mensagem = new JLabel(
                "Pronto para descobrir sua próxima conexão?"
        );

        mensagem.setFont(FONTE_TEXTO);
        mensagem.setForeground(new Color(220, 204, 232));
        mensagem.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Descrição original do aplicativo
        JLabel descricao = new JLabel(
                "<html><div style='text-align:center;'>"
                + "Descubra filmes, encontre pessoas com os "
                + "mesmos gostos e crie novas conexões!"
                + "</div></html>"
        );

        descricao.setFont(FONTE_TEXTO);
        descricao.setForeground(new Color(220, 204, 232));
        descricao.setAlignmentX(Component.CENTER_ALIGNMENT);
        descricao.setMaximumSize(new Dimension(420, 55));

        // Botões principais
        JButton descobrir = criarBotao(
                "Descobrir Filmes", CORAL
        );

        JButton catalogo = criarBotao(
                "Ver Catálogo", ROXO_BOTAO
        );

        JButton perfil = criarBotao(
                "Meu Perfil", ROXO_BOTAO
        );

        JButton sair = criarBotao(
                "Sair da Conta", ROXO_BOTAO
        );

        // Mantém as ações originais dos botões
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

        // Organização da tela original
        painel.add(cabecalho);
        painel.add(Box.createVerticalStrut(8));

        painel.add(subtitulo);
        painel.add(Box.createVerticalStrut(25));

        painel.add(boasVindas);
        painel.add(Box.createVerticalStrut(8));

        painel.add(mensagem);
        painel.add(Box.createVerticalStrut(15));

        painel.add(descricao);
        painel.add(Box.createVerticalStrut(25));

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

    // Cria botões arredondados com efeito hover
    private JButton criarBotao(String texto, Color cor) {

        JButton botao = new JButton(texto) {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D desenho = (Graphics2D) g.create();

                desenho.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                desenho.setColor(getBackground());

                desenho.fillRoundRect(
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        18,
                        18
                );

                desenho.dispose();

                super.paintComponent(g);
            }
        };

        botao.setFont(FONTE_BOTAO);
        botao.setBackground(cor);
        botao.setForeground(BRANCO);

        botao.setContentAreaFilled(false);
        botao.setBorderPainted(false);
        botao.setOpaque(false);
        botao.setFocusPainted(false);

        botao.setAlignmentX(Component.CENTER_ALIGNMENT);

        botao.setMaximumSize(
                new Dimension(350, 50)
        );

        botao.setPreferredSize(
                new Dimension(350, 50)
        );

        botao.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        // Destaque suave ao passar o mouse
        Color corHover = cor.equals(CORAL)
                ? new Color(255, 124, 116)
                : new Color(82, 52, 111);

        botao.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                botao.setBackground(corHover);
                botao.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botao.setBackground(cor);
                botao.repaint();
            }
        });

        return botao;
    }
}
