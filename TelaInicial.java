
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class TelaInicial extends JFrame {

    // CORES DO CINEMATCH
    private final Color FUNDO = new Color(43, 22, 70);
    private final Color CORAL = new Color(255, 98, 92);
    private final Color ROXO_BOTAO = new Color(65, 37, 93);
    private final Color BRANCO = new Color(247, 243, 250);

    // FONTES DO PROJETO
    private final Font FONTE_LOGO =
            new Font("Nimbus Sans", Font.BOLD, 38);

    private final Font FONTE_TITULO =
            new Font("Modern No. 20", Font.BOLD, 25);

    private final Font FONTE_TEXTO =
            new Font("Modern No. 20", Font.PLAIN, 18);

    private final Font FONTE_BOTAO =
            new Font("Modern No. 20", Font.BOLD, 20);

    // ==========================================
    // VERIFICAÇÃO DE ACESSO ÀS ÁREAS PROTEGIDAS
    // ==========================================

    private void abrirAreaProtegida(Runnable abrirDestino) {

        // Verifica se a pessoa continua conectada.
        if (!Sessao.ativa()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Sua sessão terminou.\n"
                    + "Entre novamente para continuar."
            );

            Sessao.sair();

            new TelaLogin();
            dispose();

            return;
        }

        int idUsuario = Sessao.getUsuario().getId();

        // Consulta os dados mais recentes da conta no H2.
        Usuario atualizado = new UsuarioDAO()
                .buscarPorId(idUsuario);

        if (atualizado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível localizar seu perfil.\n"
                    + "Tente entrar novamente."
            );

            return;
        }

        // Atualiza a sessão sem reiniciar as 24 horas.
        Sessao.atualizarUsuario(atualizado);

        // Se a conta já tem 18 anos ou mais,
        // abre a tela normalmente.
        if (atualizado.maiorDeIdade()) {

            abrirDestino.run();
            return;
        }

        // Se existe uma data de nascimento que indica
        // menos de 18 anos, o acesso é bloqueado.
        if (atualizado.getNascimento() != null) {

            JOptionPane.showMessageDialog(
                    this,
                    "O CineMatch é exclusivo para "
                    + "pessoas com 18 anos ou mais."
            );

            return;
        }

        // ==========================================
        // CASO ESPECIAL: CONTA ANTIGA
        // ==========================================

        // Algumas contas foram criadas antes da
        // implementação da data de nascimento.
        // Elas podem ter o campo vazio no banco.

        JOptionPane.showMessageDialog(
                this,
                "Precisamos completar sua data de nascimento.\n\n"
                + "Preencha a informação em Meu Perfil e salve.\n"
                + "Depois clique em 'Fechar perfil' para continuar."
        );

        // Abre o perfil sem perder a sessão atual.
        TelaMeuPerfil telaPerfil =
                new TelaMeuPerfil(atualizado);

        // Quando a pessoa fechar o perfil,
        // verificamos se a data foi realmente salva.
        telaPerfil.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {

                // A sessão pode ter expirado enquanto
                // a pessoa preenchia seu perfil.
                if (!Sessao.ativa()) {
                    return;
                }

                // Busca novamente os dados no banco.
                Usuario salvo = new UsuarioDAO()
                        .buscarPorId(idUsuario);

                if (salvo == null) {
                    return;
                }

                // Só libera o acesso após salvar
                // uma data válida de pessoa adulta.
                if (salvo.maiorDeIdade()) {

                    Sessao.atualizarUsuario(salvo);

                    SwingUtilities.invokeLater(
                            abrirDestino
                    );
                }
            }
        });
    }

    // ==========================================
    // CONSTRUTORES
    // ==========================================

    // Mantém compatibilidade com outras classes.
    public TelaInicial() {

        this(
                Sessao.ativa()
                        ? Sessao.getUsuario().getNome()
                        : ""
        );
    }

    // Recebe o nome do usuário após o login.
    public TelaInicial(String nomeUsuario) {

        setTitle("CineMatch - Início");
        setSize(600, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painel = new JPanel();

        painel.setBackground(FUNDO);

        painel.setLayout(
                new BoxLayout(painel, BoxLayout.Y_AXIS)
        );

        painel.setBorder(
                BorderFactory.createEmptyBorder(
                        35, 60, 35, 60
                )
        );

        // ==========================================
        // LOGO DO CINEMATCH
        // ==========================================

        JLabel icone = new JLabel();

        ImageIcon imagemIcone = new ImageIcon(
                "imagens/icone_cinematch.png"
        );

        if (imagemIcone.getIconWidth() <= 0) {

            imagemIcone = new ImageIcon(
                    "src/imagens/icone_cinematch.png"
            );
        }

        if (imagemIcone.getIconWidth() > 0) {

            Image imagemAjustada = imagemIcone
                    .getImage()
                    .getScaledInstance(
                            86,
                            61,
                            Image.SCALE_SMOOTH
                    );

            icone.setIcon(
                    new ImageIcon(imagemAjustada)
            );
        }

        JLabel nomeAplicativo =
                new JLabel("CineMatch");

        nomeAplicativo.setFont(FONTE_LOGO);
        nomeAplicativo.setForeground(BRANCO);

        JPanel cabecalho = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER, 10, 0
                )
        );

        cabecalho.setOpaque(false);

        cabecalho.add(icone);
        cabecalho.add(nomeAplicativo);

        cabecalho.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        cabecalho.setMaximumSize(
                new Dimension(480, 68)
        );

        // ==========================================
        // SLOGAN
        // ==========================================

        JLabel subtitulo = new JLabel(
                "Um filme em comum. Uma nova conexão."
        );

        subtitulo.setFont(FONTE_TEXTO);
        subtitulo.setForeground(BRANCO);

        subtitulo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        // ==========================================
        // BOAS-VINDAS
        // ==========================================

        JLabel boasVindas = new JLabel();

        if (nomeUsuario != null
                && !nomeUsuario.trim().isEmpty()) {

            boasVindas.setText(
                    "Olá, " + nomeUsuario + "!"
            );

        } else {

            boasVindas.setText(
                    "Boas-vindas ao CineMatch!"
            );
        }

        boasVindas.setFont(FONTE_TITULO);
        boasVindas.setForeground(BRANCO);

        boasVindas.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel mensagem = new JLabel(
                "Pronto para descobrir sua próxima conexão?"
        );

        mensagem.setFont(FONTE_TEXTO);
        mensagem.setForeground(
                new Color(220, 204, 232)
        );

        mensagem.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel descricao = new JLabel(
                "<html><div style='text-align:center;'>"
                + "Descubra filmes, encontre pessoas com os "
                + "mesmos gostos e crie novas conexões!"
                + "</div></html>"
        );

        descricao.setFont(FONTE_TEXTO);

        descricao.setForeground(
                new Color(220, 204, 232)
        );

        descricao.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        descricao.setMaximumSize(
                new Dimension(420, 55)
        );

        // ==========================================
        // BOTÕES PRINCIPAIS
        // ==========================================

        JButton descobrir = criarBotao(
                "Descobrir Filmes", CORAL
        );

        JButton catalogo = criarBotao(
                "Ver Catálogo", ROXO_BOTAO
        );

        JButton perfil = criarBotao(
                "Meu Perfil", ROXO_BOTAO
        );

        JButton conexoes = criarBotao(
                "Minhas Conexões", ROXO_BOTAO
        );

        JButton sair = criarBotao(
                "Sair da Conta", ROXO_BOTAO
        );

        // ==========================================
        // AÇÕES DOS BOTÕES
        // ==========================================

        // DESCOBRIR FILMES
        descobrir.addActionListener(e -> {

            abrirAreaProtegida(() -> {

                new TelaDescobrir();
                dispose();
            });
        });

        // CATÁLOGO
        catalogo.addActionListener(e -> {

            new TelaCatalogo();
            dispose();
        });

        // MEU PERFIL
        perfil.addActionListener(e -> {

            if (!Sessao.ativa()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Entre novamente para acessar seu perfil."
                );

                Sessao.sair();

                new TelaLogin();
                dispose();

                return;
            }

            new TelaMeuPerfil(
                    Sessao.getUsuario()
            );
        });

        // ==========================================
        // MINHAS CONEXÕES - CORRIGIDO
        // ==========================================

        conexoes.addActionListener(e -> {

            // Agora a tela realmente é chamada
            // depois da verificação de acesso.
            abrirAreaProtegida(() -> {

                new TelaConexoes();
            });
        });

        // SAIR DA CONTA
        sair.addActionListener(e -> {

            int resposta = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja sair da sua conta?",
                    "CineMatch",
                    JOptionPane.YES_NO_OPTION
            );

            if (resposta == JOptionPane.YES_OPTION) {

                Sessao.sair();

                new TelaLogin();
                dispose();
            }
        });

        // ==========================================
        // ORGANIZAÇÃO VISUAL
        // ==========================================

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
        painel.add(Box.createVerticalStrut(15));

        painel.add(conexoes);
        painel.add(Box.createVerticalStrut(15));

        painel.add(sair);

        add(painel);

        setVisible(true);
    }

    // ==========================================
    // BOTÕES ARREDONDADOS COM HOVER
    // ==========================================

    private JButton criarBotao(
            String texto,
            Color cor) {

        JButton botao = new JButton(texto) {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D desenho =
                        (Graphics2D) g.create();

                desenho.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                desenho.setColor(
                        getBackground()
                );

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

        botao.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        botao.setMaximumSize(
                new Dimension(350, 50)
        );

        botao.setPreferredSize(
                new Dimension(350, 50)
        );

        botao.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

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
