import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TelaCatalogo extends JFrame {

    // Cores oficiais do CineMatch
    private final Color FUNDO = new Color(43, 22, 70);
    private final Color CARD = new Color(56, 32, 82);
    private final Color CORAL = new Color(255, 98, 92);
    private final Color ROXO_BOTAO = new Color(65, 37, 93);
    private final Color BRANCO = new Color(247, 243, 250);
    private final Color TEXTO_SUAVE = new Color(220, 204, 232);
    private final Color DOURADO = new Color(255, 205, 60);

    // Fontes do aplicativo
    private final Font FONTE_LOGO = new Font("Nimbus Sans", Font.BOLD, 28);
    private final Font FONTE_TITULO = new Font("Times New Roman", Font.BOLD, 22);
    private final Font FONTE_TEXTO = new Font("Times New Roman", Font.PLAIN, 19);
    private final Font FONTE_BOTAO = new Font("Times New Roman", Font.BOLD, 18);
    private final Font FONTE_DIRETOR = new Font("Times New Roman", Font.PLAIN, 17);

    private FilmeDAO filmeDAO;
    private JPanel painelFilmes;
    private JTextField campoPesquisa;

    public TelaCatalogo() {
        // Conexão com o banco de dados H2
        this.filmeDAO = new FilmeDAO();

        setTitle("CineMatch - Catálogo");
        setSize(1100, 750);
        setMinimumSize(new Dimension(850, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel principal = new JPanel(new BorderLayout(15, 15));
        principal.setBackground(FUNDO);
        principal.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Cabeçalho do catálogo
        JPanel cabecalho = new JPanel(new BorderLayout(15, 10));
        cabecalho.setBackground(FUNDO);

        JButton voltar = criarBotao("Voltar", ROXO_BOTAO);
        voltar.addActionListener(e -> {
            new TelaInicial();
            dispose();
        });

        JLabel titulo = new JLabel("CineMatch - Filmes");
        titulo.setFont(FONTE_LOGO);
        titulo.setForeground(BRANCO);

        JPanel esquerda = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        esquerda.setBackground(FUNDO);
        esquerda.add(voltar);
        esquerda.add(titulo);

        // Barra de pesquisa com lupa
        JPanel barraPesquisa = criarBarraPesquisa();

        cabecalho.add(esquerda, BorderLayout.WEST);
        cabecalho.add(barraPesquisa, BorderLayout.EAST);

        principal.add(cabecalho, BorderLayout.NORTH);

        // Grade com três colunas
        painelFilmes = new JPanel(new GridLayout(0, 3, 18, 18));
        painelFilmes.setBackground(FUNDO);

        JScrollPane rolagem = new JScrollPane(painelFilmes);
        rolagem.setBorder(null);
        rolagem.setBackground(FUNDO);
        rolagem.getViewport().setBackground(FUNDO);
        rolagem.getVerticalScrollBar().setUnitIncrement(18);

        principal.add(rolagem, BorderLayout.CENTER);

        add(principal);

        // Carrega filmes do banco de dados na inicialização
        mostrarFilmes("");

        // Pesquisa em tempo real via DocumentListener
        campoPesquisa.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { pesquisar(); }
            public void removeUpdate(DocumentEvent e) { pesquisar(); }
            public void changedUpdate(DocumentEvent e) { pesquisar(); }
        });

        setVisible(true);
    }

    // Barra de pesquisa arredondada
    private JPanel criarBarraPesquisa() {
        JPanel barra = new JPanel(new BorderLayout(10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D desenho = (Graphics2D) g.create();
                desenho.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                desenho.setColor(BRANCO);
                desenho.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                desenho.dispose();
            }
        };

        barra.setOpaque(false);
        barra.setPreferredSize(new Dimension(300, 46));
        barra.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));

        // Ícone de lupa desenhado
        JLabel lupa = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D desenho = (Graphics2D) g.create();
                desenho.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                desenho.setColor(new Color(90, 72, 110));
                desenho.setStroke(new BasicStroke(2.5f));
                desenho.drawOval(3, 3, 13, 13);
                desenho.drawLine(15, 15, 22, 22);
                desenho.dispose();
            }
        };

        lupa.setPreferredSize(new Dimension(25, 25));

        campoPesquisa = new JTextField();
        campoPesquisa.setFont(FONTE_TEXTO);
        campoPesquisa.setBorder(null);
        campoPesquisa.setOpaque(false);
        campoPesquisa.setForeground(FUNDO);
        campoPesquisa.setCaretColor(FUNDO);
        campoPesquisa.setToolTipText("Pesquisar filmes ou gêneros");

        barra.add(lupa, BorderLayout.WEST);
        barra.add(campoPesquisa, BorderLayout.CENTER);

        // Texto de orientação na pesquisa (Placeholder)
        campoPesquisa.setText("Pesquisar filmes ou gêneros...");
        campoPesquisa.setForeground(new Color(130, 120, 140));

        campoPesquisa.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (campoPesquisa.getText().equals("Pesquisar filmes ou gêneros...")) {
                    campoPesquisa.setText("");
                    campoPesquisa.setForeground(FUNDO);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (campoPesquisa.getText().trim().isEmpty()) {
                    campoPesquisa.setText("Pesquisar filmes ou gêneros...");
                    campoPesquisa.setForeground(new Color(130, 120, 140));
                }
            }
        });

        return barra;
    }

    // Botões arredondados com hover
    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D desenho = (Graphics2D) g.create();
                desenho.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                desenho.setColor(getBackground());
                desenho.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                desenho.dispose();
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
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setMargin(new Insets(8, 14, 8, 14));

        Color corHover = cor.equals(CORAL) ? new Color(255, 124, 116) : new Color(88, 55, 118);

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

    // Pesquisa acionada pelo campo de texto
    private void pesquisar() {
        String pesquisa = campoPesquisa.getText();
        if (pesquisa.equals("Pesquisar filmes ou gêneros...")) {
            pesquisa = "";
        }
        mostrarFilmes(pesquisa);
    }

    // Busca filmes diretamente do banco de dados H2 via FilmeDAO
    private void mostrarFilmes(String pesquisa) {
        painelFilmes.removeAll();

        List<Filme> lista;
        String termo = pesquisa.trim();

        if (termo.isEmpty()) {
            lista = filmeDAO.listarTodos();
        } else {
            lista = filmeDAO.buscarPorGenero(termo);
        }

        for (Filme filme : lista) {
            painelFilmes.add(criarCard(filme));
        }

        painelFilmes.revalidate();
        painelFilmes.repaint();
    }

    // Carregamento e renderização da capa
    private JLabel criarImagem(Filme filme) {
        JLabel imagem = new JLabel();
        imagem.setHorizontalAlignment(SwingConstants.CENTER);
        imagem.setAlignmentX(Component.CENTER_ALIGNMENT);

        File arquivo = new File(filme.getCapa());
        if (arquivo.exists()) {
            ImageIcon original = new ImageIcon(arquivo.getAbsolutePath());
            Image redimensionada = original.getImage().getScaledInstance(170, 235, Image.SCALE_SMOOTH);
            imagem.setIcon(new ImageIcon(redimensionada));
        } else {
            imagem.setText("SEM CAPA");
            imagem.setForeground(TEXTO_SUAVE);
            imagem.setFont(FONTE_TEXTO);
        }

        imagem.setPreferredSize(new Dimension(180, 240));
        return imagem;
    }

    // Sistema de avaliação por 5 estrelas sincronizado com o banco H2
    private JPanel criarAvaliacao(Filme filme) {
        JPanel painelEstrelas = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 0));
        painelEstrelas.setOpaque(false);
        painelEstrelas.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton[] estrelas = new JButton[5];

        for (int i = 0; i < 5; i++) {
            final int quantidade = i + 1;
            JButton estrela = new JButton();
            estrela.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 27));
            estrela.setForeground(DOURADO);
            estrela.setContentAreaFilled(false);
            estrela.setBorderPainted(false);
            estrela.setFocusPainted(false);
            estrela.setOpaque(false);
            estrela.setMargin(new Insets(0, 0, 0, 0));
            estrela.setPreferredSize(new Dimension(35, 40));
            estrela.setCursor(new Cursor(Cursor.HAND_CURSOR));
            estrela.setToolTipText("Avaliar com " + quantidade + (quantidade == 1 ? " estrela" : " estrelas"));

            estrelas[i] = estrela;

            // Salva a avaliação no objeto e no banco de dados
            estrela.addActionListener(e -> {
                filme.avaliar(quantidade);
                filmeDAO.atualizarAvaliacao(filme); // Atualiza no H2
                atualizarEstrelas(estrelas, filme.getNota());
            });

            // Prévia visual de estrelas ao passar o mouse
            estrela.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    atualizarEstrelas(estrelas, quantidade);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    atualizarEstrelas(estrelas, filme.getNota());
                }
            });

            painelEstrelas.add(estrela);
        }

        atualizarEstrelas(estrelas, filme.getNota());
        return painelEstrelas;
    }

    private void atualizarEstrelas(JButton[] estrelas, int quantidade) {
        for (int i = 0; i < estrelas.length; i++) {
            if (estrelas[i] == null) continue;
            if (i < quantidade) {
                estrelas[i].setText("\u2605"); // Estrela preenchida
            } else {
                estrelas[i].setText("\u2606"); // Estrela vazia
            }
        }
    }

    // Monta o card visual do filme
    private JPanel criarCard(Filme filme) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D desenho = (Graphics2D) g.create();
                desenho.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                desenho.setColor(getBackground());
                desenho.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 24, 24));
                desenho.dispose();
            }
        };

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createEmptyBorder(15, 12, 15, 12));

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(72, 43, 100));
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(CARD);
                card.repaint();
            }
        });

        JLabel imagem = criarImagem(filme);

        JLabel titulo = new JLabel(filme.getTitulo());
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(BRANCO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel genero = new JLabel(filme.getGenero() + " | " + filme.getAno());
        genero.setFont(FONTE_TEXTO);
        genero.setForeground(TEXTO_SUAVE);
        genero.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Exibe o diretor caso esteja definido no modelo
        String nomeDiretor = (filme.getDiretor() != null && !filme.getDiretor().isEmpty())
                ? filme.getDiretor() : "Não informado";
        JLabel diretor = new JLabel("Direção: " + nomeDiretor);
        diretor.setFont(FONTE_DIRETOR);
        diretor.setForeground(TEXTO_SUAVE);
        diretor.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel curtidas = new JLabel("Curtidas: " + filme.getCurtidas());
        curtidas.setFont(FONTE_TEXTO);
        curtidas.setForeground(BRANCO);
        curtidas.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel avaliacao = criarAvaliacao(filme);

        JButton gostar = criarBotao("Gostei", CORAL);
        JButton rejeitar = criarBotao("Não gostei", ROXO_BOTAO);

        gostar.setAlignmentX(Component.CENTER_ALIGNMENT);
        rejeitar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ação de curtir (Persiste no banco H2)
        gostar.addActionListener(e -> {
            filme.curtir();
            filmeDAO.atualizarAvaliacao(filme);
            curtidas.setText("Curtidas: " + filme.getCurtidas());
        });

        // Ação de rejeitar
        rejeitar.addActionListener(e -> {
            filme.rejeitar();
            JOptionPane.showMessageDialog(this, "Preferência registrada!");
        });

        card.add(imagem);
        card.add(Box.createVerticalStrut(12));
        card.add(titulo);
        card.add(Box.createVerticalStrut(6));
        card.add(genero);
        card.add(Box.createVerticalStrut(5));
        card.add(diretor);
        card.add(Box.createVerticalStrut(12));
        card.add(curtidas);
        card.add(Box.createVerticalStrut(8));
        card.add(avaliacao);
        card.add(Box.createVerticalStrut(12));
        card.add(gostar);
        card.add(Box.createVerticalStrut(8));
        card.add(rejeitar);

        return card;
    }
}