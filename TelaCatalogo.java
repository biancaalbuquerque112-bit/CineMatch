
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TelaCatalogo extends JFrame {

    // Identidade visual do CineMatch
    private final Color FUNDO = new Color(43, 22, 70);
    private final Color CARD = new Color(56, 32, 82);
    private final Color CORAL = new Color(255, 98, 92);
    private final Color ROXO_BOTAO = new Color(65, 37, 93);
    private final Color BRANCO = new Color(247, 243, 250);
    private final Color TEXTO_SUAVE = new Color(220, 204, 232);
    private final Color DOURADO = new Color(255, 205, 60);

    private final Font FONTE_LOGO =
            new Font("Nimbus Sans", Font.BOLD, 28);
    private final Font FONTE_TITULO =
            new Font("Times New Roman", Font.BOLD, 22);
    private final Font FONTE_TEXTO =
            new Font("Times New Roman", Font.PLAIN, 19);
    private final Font FONTE_BOTAO =
            new Font("Times New Roman", Font.BOLD, 18);
    private final Font FONTE_DIRETOR =
            new Font("Times New Roman", Font.PLAIN, 17);

    // A grade acompanha a largura da janela.
    // Assim, não esconde a terceira coluna.
    private static class GradeFilmes
            extends JPanel implements Scrollable {

        GradeFilmes() {
            super(new GridLayout(0, 3, 18, 18));
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(
                Rectangle visible, int orientacao, int direcao) {
            return 20;
        }

        @Override
        public int getScrollableBlockIncrement(
                Rectangle visible, int orientacao, int direcao) {
            return Math.max(100, visible.height - 60);
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    // DAOs
    private final FilmeDAO filmeDAO = new FilmeDAO();
    private final AvaliacaoFilmeDAO avaliacaoDAO =
            new AvaliacaoFilmeDAO();

    // Navegação dentro da mesma janela
    private final CardLayout navegacao = new CardLayout();
    private final JPanel telas = new JPanel(navegacao);

    private JPanel detalhesAtual;
    private JPanel painelFilmes;
    private JTextField campoPesquisa;

    // Notas do usuário conectado
    private Map<Integer, Integer> minhasAvaliacoes;

    // =========================================
    // CONSTRUTOR
    // =========================================

    public TelaCatalogo() {

        setTitle("CineMatch - Catálogo");
        setSize(1100, 750);
        setMinimumSize(new Dimension(850, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel catalogo = montarCatalogo();

        telas.add(catalogo, "CATALOGO");
        setContentPane(telas);

        mostrarFilmes("");

        campoPesquisa.getDocument().addDocumentListener(
                new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                pesquisar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                pesquisar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                pesquisar();
            }
        });

        setVisible(true);
    }

    // =========================================
    // TELA PRINCIPAL DO CATÁLOGO
    // =========================================

    private JPanel montarCatalogo() {

        JPanel principal = new JPanel(
                new BorderLayout(15, 15)
        );

        principal.setBackground(FUNDO);

        principal.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 25, 20, 25
                )
        );

        // CABEÇALHO
        JPanel cabecalho = new JPanel(
                new BorderLayout(15, 10)
        );

        cabecalho.setBackground(FUNDO);

        JButton voltar = criarBotao(
                "Voltar", ROXO_BOTAO
        );

        voltar.addActionListener(e -> {
            new TelaInicial();
            dispose();
        });

        // LOGO ORIGINAL DO CINEMATCH
        JLabel logo = new JLabel();

        logo.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        logo.setPreferredSize(
                new Dimension(62, 48)
        );

        File imagemLogo = new File(
                "imagens/icone_cinematch.png"
        );

        if (!imagemLogo.isFile()) {
            imagemLogo = new File(
                    "src/imagens/icone_cinematch.png"
            );
        }

        if (imagemLogo.isFile()) {

            ImageIcon original = new ImageIcon(
                    imagemLogo.getAbsolutePath()
            );

            if (original.getIconWidth() > 0) {

                Image ajustada = original.getImage()
                        .getScaledInstance(
                                62,
                                46,
                                Image.SCALE_SMOOTH
                        );

                logo.setIcon(
                        new ImageIcon(ajustada)
                );
            }
        }

        JLabel nomeApp = new JLabel("CineMatch");

        nomeApp.setFont(FONTE_LOGO);
        nomeApp.setForeground(BRANCO);

        JLabel nomePagina = new JLabel(
                "Catálogo de filmes"
        );

        nomePagina.setFont(
                new Font(
                        "Times New Roman",
                        Font.PLAIN,
                        16
                )
        );

        nomePagina.setForeground(TEXTO_SUAVE);

        JPanel nomeEpagina = new JPanel();

        nomeEpagina.setOpaque(false);

        nomeEpagina.setLayout(
                new BoxLayout(
                        nomeEpagina,
                        BoxLayout.Y_AXIS
                )
        );

        nomeEpagina.add(nomeApp);
        nomeEpagina.add(nomePagina);

        JPanel esquerda = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT, 10, 0
                )
        );

        esquerda.setOpaque(false);

        esquerda.add(voltar);
        esquerda.add(logo);
        esquerda.add(nomeEpagina);

        cabecalho.add(
                esquerda,
                BorderLayout.WEST
        );

        cabecalho.add(
                criarBarraPesquisa(),
                BorderLayout.EAST
        );

        principal.add(
                cabecalho,
                BorderLayout.NORTH
        );

        // GRADE DE FILMES
        painelFilmes = new GradeFilmes();
        painelFilmes.setBackground(FUNDO);

        JScrollPane rolagem = new JScrollPane(
                painelFilmes,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        rolagem.setBorder(null);
        rolagem.getViewport().setBackground(FUNDO);

        rolagem.getVerticalScrollBar()
                .setUnitIncrement(20);

        principal.add(
                rolagem,
                BorderLayout.CENTER
        );

        return principal;
    }

    // =========================================
    // ABRIR DETALHES NA MESMA JANELA
    // =========================================

    private void abrirDetalhes(Filme filme) {

        if (detalhesAtual != null) {
            telas.remove(detalhesAtual);
        }

        detalhesAtual = new TelaDetalhesFilme(
                filme,
                () -> navegacao.show(
                        telas,
                        "CATALOGO"
                )
        );

        telas.add(
                detalhesAtual,
                "DETALHES"
        );

        navegacao.show(
                telas,
                "DETALHES"
        );

        telas.revalidate();
        telas.repaint();
    }

    // =========================================
    // BARRA DE PESQUISA
    // =========================================

    private JPanel criarBarraPesquisa() {

        JPanel barra = new JPanel(
                new BorderLayout(10, 0)
        ) {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(BRANCO);

                g2.fillRoundRect(
                        0, 0,
                        getWidth() - 1,
                        getHeight() - 1,
                        24, 24
                );

                g2.dispose();
            }
        };

        barra.setOpaque(false);

        barra.setPreferredSize(
                new Dimension(300, 46)
        );

        barra.setBorder(
                BorderFactory.createEmptyBorder(
                        5, 12, 5, 12
                )
        );

        JLabel lupa = new JLabel() {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(
                        new Color(90, 72, 110)
                );

                g2.setStroke(new BasicStroke(2.5f));

                g2.drawOval(3, 3, 13, 13);
                g2.drawLine(15, 15, 22, 22);

                g2.dispose();
            }
        };

        lupa.setPreferredSize(
                new Dimension(25, 25)
        );

        campoPesquisa = new JTextField();

        campoPesquisa.setFont(FONTE_TEXTO);
        campoPesquisa.setBorder(null);
        campoPesquisa.setOpaque(false);
        campoPesquisa.setForeground(FUNDO);
        campoPesquisa.setCaretColor(FUNDO);

        campoPesquisa.setToolTipText(
                "Pesquisar filmes ou gêneros"
        );

        barra.add(lupa, BorderLayout.WEST);
        barra.add(campoPesquisa, BorderLayout.CENTER);

        campoPesquisa.setText(
                "Pesquisar filmes ou gêneros..."
        );

        campoPesquisa.setForeground(
                new Color(130, 120, 140)
        );

        campoPesquisa.addFocusListener(
                new java.awt.event.FocusAdapter() {

            @Override
            public void focusGained(
                    java.awt.event.FocusEvent e) {

                if (campoPesquisa.getText().equals(
                        "Pesquisar filmes ou gêneros...")) {

                    campoPesquisa.setText("");
                    campoPesquisa.setForeground(FUNDO);
                }
            }

            @Override
            public void focusLost(
                    java.awt.event.FocusEvent e) {

                if (campoPesquisa.getText()
                        .trim().isEmpty()) {

                    campoPesquisa.setText(
                            "Pesquisar filmes ou gêneros..."
                    );

                    campoPesquisa.setForeground(
                            new Color(130, 120, 140)
                    );
                }
            }
        });

        return barra;
    }

    // =========================================
    // BOTÕES DO CINEMATCH
    // =========================================

    private JButton criarBotao(
            String texto, Color cor) {

        JButton botao = new JButton(texto) {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(getBackground());

                g2.fillRoundRect(
                        0, 0,
                        getWidth(),
                        getHeight(),
                        18, 18
                );

                g2.dispose();

                super.paintComponent(g);
            }
        };

        botao.setFont(FONTE_BOTAO);
        botao.setBackground(cor);
        botao.setForeground(BRANCO);

        botao.setContentAreaFilled(false);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setOpaque(false);

        botao.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        botao.setMargin(
                new Insets(8, 14, 8, 14)
        );

        Color hover = cor.equals(CORAL)
                ? new Color(255, 124, 116)
                : new Color(88, 55, 118);

        botao.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                botao.setBackground(hover);
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

    // =========================================
    // PESQUISAR FILMES
    // =========================================

    private void pesquisar() {

        String texto = campoPesquisa.getText();

        if (texto.equals(
                "Pesquisar filmes ou gêneros...")) {

            texto = "";
        }

        mostrarFilmes(texto);
    }

    // =========================================
    // EXIBIR FILMES
    // =========================================

    private void mostrarFilmes(String pesquisa) {

        painelFilmes.removeAll();

        // Calcula as avaliações de todos os filmes.
        Map<Integer, AvaliacaoFilmeDAO.Estatisticas>
                estatisticas =
                avaliacaoDAO.listarEstatisticas();

        // Busca notas da conta conectada.
        Usuario atual = Sessao.getUsuario();

        int usuarioId = atual == null
                ? 0
                : atual.getId();

        minhasAvaliacoes =
                avaliacaoDAO.listarAvaliacoesDoUsuario(
                        usuarioId
                );

        List<Filme> filmes =
                pesquisa.trim().isEmpty()
                        ? filmeDAO.listarTodos()
                        : filmeDAO.buscarPorGenero(
                                pesquisa.trim()
                        );

        for (Filme filme : filmes) {

            painelFilmes.add(
                    criarCard(
                            filme,
                            estatisticas.get(filme.getId())
                    )
            );
        }

        painelFilmes.revalidate();
        painelFilmes.repaint();
    }

    // =========================================
    // CAPA DO FILME
    // =========================================

    private JLabel criarImagem(Filme filme) {

        JLabel imagem = new JLabel(
                "SEM CAPA",
                SwingConstants.CENTER
        );

        imagem.setForeground(TEXTO_SUAVE);
        imagem.setFont(FONTE_TEXTO);

        File arquivo = new File(
                filme.getCapa()
        );

        if (!arquivo.isFile()) {
            arquivo = new File(
                    "src", filme.getCapa()
            );
        }

        if (arquivo.isFile()) {

            ImageIcon original = new ImageIcon(
                    arquivo.getAbsolutePath()
            );

            if (original.getIconWidth() > 0
                    && original.getIconHeight() > 0) {

                double escala = Math.min(
                        170.0 / original.getIconWidth(),
                        235.0 / original.getIconHeight()
                );

                int largura = Math.max(
                        1,
                        (int) Math.round(
                                original.getIconWidth()
                                * escala
                        )
                );

                int altura = Math.max(
                        1,
                        (int) Math.round(
                                original.getIconHeight()
                                * escala
                        )
                );

                Image ajustada = original.getImage()
                        .getScaledInstance(
                                largura,
                                altura,
                                Image.SCALE_SMOOTH
                        );

                imagem.setIcon(
                        new ImageIcon(ajustada)
                );

                imagem.setText("");
            }
        }

        imagem.setPreferredSize(
                new Dimension(180, 240)
        );

        imagem.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        return imagem;
    }

    // =========================================
    // TEXTO CENTRALIZADO E RESPONSIVO
    // =========================================

    private String textoCentralizado(String texto) {

        String seguro = texto.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");

        return "<html><div style='text-align:center; width:200px;'>"
                + seguro
                + "</div></html>";
    }

    // =========================================
    // PORCENTAGEM DAS AVALIAÇÕES
    // =========================================

    private String resumoAvaliacao(
            AvaliacaoFilmeDAO.Estatisticas resultado) {

        if (resultado == null
                || resultado.getQuantidade() == 0) {

            return "Avaliação do público: sem avaliações";
        }

        int total = resultado.getQuantidade();

        return String.format(
                Locale.forLanguageTag("pt-BR"),
                "Avaliação: %d%% (%.1f/5 - %d %s)",
                resultado.getPercentual(),
                resultado.getMedia(),
                total,
                total == 1
                        ? "avaliação"
                        : "avaliações"
        );
    }

    // =========================================
    // COLORIR ESTRELAS
    // =========================================

    private void atualizarEstrelas(
            JButton[] estrelas, int nota) {

        for (int i = 0; i < estrelas.length; i++) {

            if (estrelas[i] != null) {

                estrelas[i].putClientProperty(
                        "preenchida",
                        i < nota
                );

                estrelas[i].repaint();
            }
        }
    }

    // =========================================
    // AVALIAÇÃO POR ESTRELAS
    // =========================================

    private JPanel criarAvaliacao(
            Filme filme, JLabel resumo) {

        JPanel painel = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER, 3, 0
                )
        );

        painel.setOpaque(false);

        painel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        painel.setPreferredSize(
                new Dimension(240, 44)
        );

        painel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE, 44
                )
        );

        JButton[] estrelas = new JButton[5];

        for (int i = 0; i < 5; i++) {

            final int notaEscolhida = i + 1;

            // Estrela desenhada em Java.
            // Não depende de símbolos de fonte.
            JButton estrela = new JButton() {

                @Override
                protected void paintComponent(Graphics g) {

                    Graphics2D g2 =
                            (Graphics2D) g.create();

                    g2.setRenderingHint(
                            RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON
                    );

                    Polygon formato = new Polygon();

                    for (int j = 0; j < 10; j++) {

                        double angulo =
                                Math.PI * j / 5
                                - Math.PI / 2;

                        int raio = j % 2 == 0
                                ? 13
                                : 6;

                        formato.addPoint(
                                getWidth() / 2
                                        + (int) Math.round(
                                                raio
                                                * Math.cos(angulo)
                                        ),
                                getHeight() / 2
                                        + (int) Math.round(
                                                raio
                                                * Math.sin(angulo)
                                        )
                        );
                    }

                    g2.setColor(DOURADO);

                    if (Boolean.TRUE.equals(
                            getClientProperty("preenchida"))) {

                        g2.fillPolygon(formato);

                    } else {

                        g2.setStroke(
                                new BasicStroke(2f)
                        );

                        g2.drawPolygon(formato);
                    }

                    g2.dispose();
                }
            };

            estrela.setContentAreaFilled(false);
            estrela.setBorderPainted(false);
            estrela.setFocusPainted(false);
            estrela.setOpaque(false);

            estrela.setPreferredSize(
                    new Dimension(35, 40)
            );

            estrela.setCursor(
                    Cursor.getPredefinedCursor(
                            Cursor.HAND_CURSOR
                    )
            );

            estrela.setToolTipText(
                    "Sua avaliação: "
                    + notaEscolhida
                    + " de 5 estrelas"
            );

            estrelas[i] = estrela;

            // SALVAR A AVALIAÇÃO NO H2
            estrela.addActionListener(e -> {

                if (!Sessao.adulta()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Entre com uma conta maior "
                            + "de 18 anos para avaliar."
                    );

                    return;
                }

                Usuario usuario = Sessao.getUsuario();

                if (usuario == null) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Sua sessão terminou. "
                            + "Entre novamente."
                    );

                    return;
                }

                boolean salvo = avaliacaoDAO.avaliar(
                        usuario.getId(),
                        filme.getId(),
                        notaEscolhida
                );

                if (!salvo) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Não foi possível salvar "
                            + "a avaliação."
                    );

                    return;
                }

                // A mesma conta não duplica votos.
                // Ela pode atualizar sua nota.
                minhasAvaliacoes.put(
                        filme.getId(),
                        notaEscolhida
                );

                atualizarEstrelas(
                        estrelas,
                        notaEscolhida
                );

                // Atualiza a porcentagem imediatamente.
                AvaliacaoFilmeDAO.Estatisticas novaMedia =
                        avaliacaoDAO
                                .listarEstatisticas()
                                .get(filme.getId());

                resumo.setText(
                        textoCentralizado(
                                resumoAvaliacao(novaMedia)
                        )
                );
            });

            // Prévia ao passar o mouse.
            estrela.addMouseListener(
                    new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {

                    atualizarEstrelas(
                            estrelas,
                            notaEscolhida
                    );
                }

                @Override
                public void mouseExited(MouseEvent e) {

                    atualizarEstrelas(
                            estrelas,
                            minhasAvaliacoes.getOrDefault(
                                    filme.getId(), 0
                            )
                    );
                }
            });

            painel.add(estrela);
        }

        // Recupera a nota da conta ao abrir o catálogo.
        atualizarEstrelas(
                estrelas,
                minhasAvaliacoes.getOrDefault(
                        filme.getId(), 0
                )
        );

        return painel;
    }

    // =========================================
    // CLIQUE NA CAPA OU NO TEXTO
    // =========================================

    private void ativarCliqueDetalhes(
            JComponent componente, Filme filme) {

        componente.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        componente.setToolTipText(
                "Clique para ver os detalhes do filme"
        );

        componente.addMouseListener(
                new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                abrirDetalhes(filme);
            }
        });
    }

    // =========================================
    // CARD COMPLETO DO FILME
    // =========================================

    private JPanel criarCard(
            Filme filme,
            AvaliacaoFilmeDAO.Estatisticas estatisticas) {

        JPanel card = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(getBackground());

                g2.fill(
                        new RoundRectangle2D.Double(
                                0, 0,
                                getWidth() - 1,
                                getHeight() - 1,
                                24, 24
                        )
                );

                g2.dispose();
            }
        };

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setOpaque(false);
        card.setBackground(CARD);

        card.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 12, 15, 12
                )
        );

        // Clique na área livre do card.
        card.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                card.setBackground(
                        new Color(72, 43, 100)
                );

                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {

                card.setBackground(CARD);
                card.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                abrirDetalhes(filme);
            }
        });

        card.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        JLabel imagem = criarImagem(filme);

        JLabel titulo = new JLabel(
                textoCentralizado(
                        filme.getTitulo()
                ),
                SwingConstants.CENTER
        );

        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(BRANCO);

        titulo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel genero = new JLabel(
                textoCentralizado(
                        filme.getGenero()
                        + " | "
                        + filme.getAno()
                )
        );

        genero.setFont(FONTE_TEXTO);
        genero.setForeground(TEXTO_SUAVE);

        genero.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        String nomeDiretor =
                filme.getDiretor() != null
                && !filme.getDiretor().isEmpty()
                        ? filme.getDiretor()
                        : "Não informado";

        JLabel diretor = new JLabel(
                textoCentralizado(
                        "Direção: " + nomeDiretor
                )
        );

        diretor.setFont(FONTE_DIRETOR);
        diretor.setForeground(TEXTO_SUAVE);

        diretor.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel curtidas = new JLabel(
                "Curtidas: " + filme.getCurtidas()
        );

        curtidas.setFont(FONTE_TEXTO);
        curtidas.setForeground(BRANCO);

        curtidas.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        // MÉDIA DE AVALIAÇÕES
        JLabel resumo = new JLabel(
                textoCentralizado(
                        resumoAvaliacao(estatisticas)
                )
        );

        resumo.setFont(
                new Font(
                        "Times New Roman",
                        Font.PLAIN,
                        15
                )
        );

        resumo.setForeground(TEXTO_SUAVE);

        resumo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JPanel avaliacao = criarAvaliacao(
                filme, resumo
        );

        JButton detalhes = criarBotao(
                "Ver detalhes", ROXO_BOTAO
        );

        JButton gostar = criarBotao(
                "Gostei", CORAL
        );

        JButton rejeitar = criarBotao(
                "Não gostei", ROXO_BOTAO
        );

        detalhes.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        gostar.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        rejeitar.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        // ABRIR DETALHES
        detalhes.addActionListener(
                e -> abrirDetalhes(filme)
        );

        // CURTIR FILME
        gostar.addActionListener(e -> {

            if (!Sessao.adulta()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Complete seu perfil com data "
                        + "de nascimento (18+) antes de curtir."
                );

                return;
            }

            PreferenciaFilmeDAO preferencias =
                    new PreferenciaFilmeDAO();

            int usuarioId =
                    Sessao.getUsuario().getId();

            if (preferencias.marcarCurtido(
                    usuarioId, filme.getId())) {

                int total = preferencias.contarCurtidas(
                        filme.getId()
                );

                filme.setCurtidas(total);

                curtidas.setText(
                        "Curtidas: " + total
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível registrar sua curtida."
                );
            }
        });

        // NÃO GOSTEI - COMPORTAMENTO ANTERIOR
        rejeitar.addActionListener(e -> {

            filme.rejeitar();

            JOptionPane.showMessageDialog(
                    this,
                    "Preferência registrada!"
            );
        });

        // CLIQUE NO FILME ABRE SUA FICHA
        ativarCliqueDetalhes(imagem, filme);
        ativarCliqueDetalhes(titulo, filme);
        ativarCliqueDetalhes(genero, filme);
        ativarCliqueDetalhes(diretor, filme);
        ativarCliqueDetalhes(curtidas, filme);
        ativarCliqueDetalhes(resumo, filme);

        // ORGANIZAÇÃO VISUAL
        card.add(imagem);

        card.add(
                Box.createRigidArea(
                        new Dimension(0, 12)
                )
        );

        card.add(titulo);

        card.add(
                Box.createRigidArea(
                        new Dimension(0, 6)
                )
        );

        card.add(genero);

        card.add(
                Box.createRigidArea(
                        new Dimension(0, 5)
                )
        );

        card.add(diretor);

        card.add(
                Box.createRigidArea(
                        new Dimension(0, 12)
                )
        );

        card.add(curtidas);

        card.add(
                Box.createRigidArea(
                        new Dimension(0, 8)
                )
        );

        card.add(avaliacao);

        card.add(
                Box.createRigidArea(
                        new Dimension(0, 3)
                )
        );

        card.add(resumo);

        card.add(
                Box.createRigidArea(
                        new Dimension(0, 12)
                )
        );

        card.add(detalhes);

        card.add(
                Box.createRigidArea(
                        new Dimension(0, 8)
                )
        );

        card.add(gostar);

        card.add(
                Box.createRigidArea(
                        new Dimension(0, 8)
                )
        );

        card.add(rejeitar);

        return card;
    }
}
