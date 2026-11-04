import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TelaDescobrir extends JFrame {

    // CORES DO CINEMATCH
    private final Color FUNDO = new Color(43, 22, 70);
    private final Color CARD = new Color(56, 32, 82);
    private final Color CORAL = new Color(255, 98, 92);
    private final Color ROXO_BOTAO = new Color(65, 37, 93);
    private final Color TEXTO = new Color(247, 243, 250);
    private final Color SUAVE = new Color(220, 204, 232);
    private final Color BORDA = new Color(94, 65, 119);
    private final Color DOURADO = new Color(255, 185, 76);

    // PERSISTÊNCIA & LISTAS DE FILMES
    private final FilmeDAO filmeDAO;
    private final List<Filme> filmes;
    private final PreferenciaFilmeDAO preferencias = new PreferenciaFilmeDAO();


    private int indiceAtual = 0;
    private Filme filmeCurtido;
    private JPanel principal;

    // Só mostra possíveis matches na primeira curtida
    private boolean primeiraCurtida = true;

    // CONSTRUTOR
    public TelaDescobrir() {
        // Conexão com o banco de dados H2
        this.filmeDAO = new FilmeDAO();
        this.filmes = filmeDAO.listarTodos();

        setTitle("CineMatch - Descobrir");
        setSize(1150, 820);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        principal = new JPanel(new BorderLayout());
        principal.setBackground(FUNDO);

        setContentPane(principal);

        mostrarDescoberta();

        setVisible(true);
    }

    // CRIA TEXTOS COM TIMES NEW ROMAN
    private JLabel label(String valor, int tamanho, Color cor, boolean negrito) {
        JLabel l = new JLabel(valor);
        l.setFont(new Font("Times New Roman", negrito ? Font.BOLD : Font.PLAIN, tamanho));
        l.setForeground(cor);
        return l;
    }

    // CRIA PAINÉIS TRANSPARENTES
    private JPanel transparente(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setOpaque(false);
        return p;
    }

    // CARTÃO COM BORDAS ARREDONDADAS
    private class Cartao extends JPanel {
        private final Color cor;
        private final int raio;

        Cartao(Color cor, int raio) {
            this.cor = cor;
            this.raio = raio;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(cor);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, raio, raio);
            g2.setColor(BORDA);
            g2.setStroke(new BasicStroke(1.1f));
            g2.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 2, raio, raio);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // BOTÃO ARREDONDADO COM HOVER
    private JButton botao(String texto, Color cor) {
        JButton b = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(cor.brighter());
                } else {
                    g2.setColor(cor);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        b.setFont(new Font("Times New Roman", Font.BOLD, 18));
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 17, 10, 17));

        return b;
    }

    // BOTÕES REDONDOS: PASSAR, SALVAR E CURTIR
    private JButton botaoCircular(String simbolo, Color fundo, Color contorno) {
        JButton b = new JButton(simbolo) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int d = Math.min(getWidth(), getHeight()) - 7;
                int x = (getWidth() - d) / 2;
                int y = (getHeight() - d) / 2;

                if (getModel().isRollover()) {
                    g2.setColor(fundo.brighter());
                } else {
                    g2.setColor(fundo);
                }
                g2.fillOval(x, y, d, d);

                g2.setColor(contorno);
                g2.setStroke(new BasicStroke(2.3f));
                g2.drawOval(x, y, d, d);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        b.setFont(new Font("SansSerif", Font.BOLD, 40));
        b.setForeground(contorno);
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setOpaque(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(88, 88));

        return b;
    }

    // LOCALIZA A CAPA DO FILME
    private File arquivoCapa(Filme filme) {
        File direto = new File(filme.getCapa());
        if (direto.isFile()) {
            return direto;
        }

        File src = new File("src", filme.getCapa());
        if (src.isFile()) {
            return src;
        }

        return direto;
    }

    // EXIBE A CAPA SEM ACHATAR A IMAGEM
    private JLabel capa(Filme filme, int largura, int altura) {
        JLabel imagem = new JLabel("Capa indisponível", SwingConstants.CENTER);
        imagem.setForeground(SUAVE);
        imagem.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        imagem.setOpaque(true);
        imagem.setBackground(new Color(38, 34, 56));
        imagem.setHorizontalAlignment(SwingConstants.CENTER);
        imagem.setVerticalAlignment(SwingConstants.CENTER);

        Dimension tamanho = new Dimension(largura, altura);
        imagem.setPreferredSize(tamanho);
        imagem.setMinimumSize(tamanho);
        imagem.setMaximumSize(tamanho);

        File arquivo = arquivoCapa(filme);
        if (arquivo.isFile()) {
            ImageIcon original = new ImageIcon(arquivo.getAbsolutePath());
            int larguraOriginal = original.getIconWidth();
            int alturaOriginal = original.getIconHeight();

            if (larguraOriginal > 0 && alturaOriginal > 0) {
                double escalaLargura = (double) largura / larguraOriginal;
                double escalaAltura = (double) altura / alturaOriginal;
                double escala = Math.min(escalaLargura, escalaAltura);

                int novaLargura = Math.max(1, (int) Math.round(larguraOriginal * escala));
                int novaAltura = Math.max(1, (int) Math.round(alturaOriginal * escala));

                Image redimensionada = original.getImage().getScaledInstance(novaLargura, novaAltura, Image.SCALE_SMOOTH);
                imagem.setIcon(new ImageIcon(redimensionada));
                imagem.setText("");
            }
        }

        return imagem;
    }

    // TROCA O CONTEÚDO SEM ABRIR OUTRA JANELA
    private void atualizar(JPanel tela) {
        principal.removeAll();
        principal.add(tela, BorderLayout.CENTER);
        principal.revalidate();
        principal.repaint();
    }

    // CABEÇALHO COM LOGO E SLOGAN
    private JPanel topo(boolean pessoas) {
        JPanel barra = transparente(new BorderLayout(14, 8));
        barra.setBorder(new EmptyBorder(13, 20, 14, 20));

        JButton inicio = botao("Início", ROXO_BOTAO);
        inicio.addActionListener(e -> {
            new TelaInicial();
            dispose();
        });

        barra.add(inicio, BorderLayout.WEST);

        JPanel centro = transparente(new BorderLayout(0, 5));
        JPanel identidade = transparente(new FlowLayout(FlowLayout.CENTER, 10, 0));

        if (!pessoas) {
            JLabel icone = new JLabel();
            File arquivoLogo = new File("imagens/icone_cinematch.png");
            if (!arquivoLogo.isFile()) {
                arquivoLogo = new File("src/imagens/icone_cinematch.png");
            }

            if (arquivoLogo.isFile()) {
                ImageIcon logo = new ImageIcon(arquivoLogo.getAbsolutePath());
                if (logo.getIconWidth() > 0) {
                    Image imagem = logo.getImage().getScaledInstance(72, 51, Image.SCALE_SMOOTH);
                    icone.setIcon(new ImageIcon(imagem));
                }
            }

            JLabel nome = new JLabel("CineMatch");
            nome.setFont(new Font("Nimbus Sans", Font.BOLD, 38));
            nome.setForeground(TEXTO);

            identidade.add(icone);
            identidade.add(nome);
        } else {
            JLabel titulo = label("Pessoas que também curtiram", 27, TEXTO, true);
            identidade.add(titulo);
        }

        JLabel slogan = label(
                pessoas ? "Veja quem compartilha esse gosto com você" : "Um filme em comum. Uma nova conexão.",
                17, SUAVE, false
        );

        slogan.setHorizontalAlignment(SwingConstants.CENTER);

        centro.add(identidade, BorderLayout.CENTER);
        centro.add(slogan, BorderLayout.SOUTH);

        barra.add(centro, BorderLayout.CENTER);

        JButton acao = botao(pessoas ? "Continuar filmes" : "Meu perfil", CORAL);
        acao.addActionListener(e -> {
            if (pessoas) {
                mostrarDescoberta();
            } else {
                new TelaMeuPerfil(Sessao.getUsuario());
            }
        });

        barra.add(acao, BorderLayout.EAST);

        return barra;
    }

    // CARTÃO COM CAPA, TÍTULO, GÊNERO, DIRETOR E ANO
    private JPanel cardFilme(Filme filme, int largura, int alturaCapa, boolean compacto) {
        Cartao card = new Cartao(CARD, 30);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(10, 10, 14, 10));

        JLabel imagem = capa(filme, largura, alturaCapa);
        imagem.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(imagem);
        card.add(Box.createVerticalStrut(9));

        JLabel nome = label(filme.getTitulo(), compacto ? 19 : 22, TEXTO, true);
        nome.setAlignmentX(Component.CENTER_ALIGNMENT);
        nome.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(nome);
        card.add(Box.createVerticalStrut(4));

        JLabel genero = label(filme.getGenero(), 16, SUAVE, false);
        genero.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(genero);
        card.add(Box.createVerticalStrut(4));

        String nomeDiretor = (filme.getDiretor() != null && !filme.getDiretor().isEmpty())
                ? filme.getDiretor() : "Não informado";
        JLabel diretor = label("Direção: " + nomeDiretor, 15, SUAVE, false);
        diretor.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(diretor);
        card.add(Box.createVerticalStrut(7));

        JLabel ano = label("Ano: " + filme.getAno(), 15, CORAL, true);
        ano.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(ano);

        if (!compacto) {
            card.add(Box.createVerticalStrut(4));
            JLabel contador = label(
                    "Filme " + (indiceAtual + 1) + " de " + filmes.size(),
                    14, SUAVE, false
            );
            contador.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(contador);
        }

        return card;
    }

    // ABRE OS DETALHES DENTRO DA MESMA JANELA
    private void mostrarDetalhes(Filme filme) {
        TelaDetalhesFilme detalhes = new TelaDetalhesFilme(filme, () -> mostrarDescoberta());
        atualizar(detalhes);
    }

    // TELA PRINCIPAL DE DESCOBERTA
    void mostrarDescoberta() {
        JPanel tela = transparente(new BorderLayout(15, 15));
        tela.setBackground(FUNDO);
        tela.setOpaque(true);
        tela.setBorder(new EmptyBorder(10, 24, 20, 24));

        tela.add(topo(false), BorderLayout.NORTH);

        // FIM DO CATÁLOGO
        if (filmes.isEmpty() || indiceAtual >= filmes.size()) {
            JPanel fim = transparente(new GridBagLayout());
            JPanel conteudo = transparente(new GridLayout(3, 1, 0, 18));

            JLabel msg = label("Você descobriu todos os filmes!", 24, TEXTO, true);
            msg.setHorizontalAlignment(SwingConstants.CENTER);
            conteudo.add(msg);

            JButton reiniciar = botao("Recomeçar descoberta", CORAL);
            reiniciar.addActionListener(e -> {
                indiceAtual = 0;
                mostrarDescoberta();
            });
            conteudo.add(reiniciar);

            JButton voltar = botao("Voltar ao início", ROXO_BOTAO);
            voltar.addActionListener(e -> {
                new TelaInicial();
                dispose();
            });
            conteudo.add(voltar);

            fim.add(conteudo);
            tela.add(fim, BorderLayout.CENTER);
            atualizar(tela);
            return;
        }

        Filme atual = filmes.get(indiceAtual);

        // CARROSSEL DOS FILMES
        JPanel palco = transparente(new GridBagLayout());
        JPanel cartoes = transparente(new FlowLayout(FlowLayout.CENTER, 12, 8));

        // CAPA ANTERIOR
        if (filmes.size() > 1) {
            int anterior = (indiceAtual - 1 + filmes.size()) % filmes.size();
            JLabel esquerda = capa(filmes.get(anterior), 145, 225);
            esquerda.setBorder(BorderFactory.createLineBorder(BORDA, 2));
            cartoes.add(esquerda);
        }

        // CARTÃO CENTRAL
        JPanel central = cardFilme(atual, 280, 350, false);
        cartoes.add(central);

        // PRÓXIMA CAPA
        if (filmes.size() > 2) {
            int proximo = (indiceAtual + 1) % filmes.size();
            JLabel direita = capa(filmes.get(proximo), 145, 225);
            direita.setBorder(BorderFactory.createLineBorder(BORDA, 2));
            cartoes.add(direita);
        }

        palco.add(cartoes);

        JScrollPane rolagemPalco = new JScrollPane(
                palco,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        rolagemPalco.setBorder(null);
        rolagemPalco.setOpaque(false);
        rolagemPalco.getViewport().setOpaque(false);
        rolagemPalco.getVerticalScrollBar().setUnitIncrement(16);

        tela.add(rolagemPalco, BorderLayout.CENTER);

        // BOTÕES PASSAR, SALVAR E CURTIR
        JPanel acoes = transparente(new FlowLayout(FlowLayout.CENTER, 32, 0));

        // PASSAR
        acoes.add(acaoCircular("✕", "Passar", ROXO_BOTAO, CORAL, () -> {
            indiceAtual++;
            mostrarDescoberta();
        }));

        // SALVAR
        acoes.add(acaoCircular("★", "Salvar", ROXO_BOTAO, DOURADO, () -> {
            if (!Sessao.adulta() || !preferencias.marcarSalvo(Sessao.getUsuario().getId(), atual.getId())) {
                JOptionPane.showMessageDialog(this, "Não foi possível salvar. Entre na sua conta.");
                return;
            }
            indiceAtual++;
            mostrarDescoberta();
        }));

        // CURTIR
        acoes.add(acaoCircular("♥", "Curtir", CORAL, Color.WHITE, () -> {
            if (!Sessao.adulta() || !preferencias.marcarCurtido(Sessao.getUsuario().getId(), atual.getId())) {
                JOptionPane.showMessageDialog(this, "Não foi possível curtir. Entre na sua conta.");
                return;
            }
            filmeCurtido = atual;
            indiceAtual++;

            // Preserva a primeira exibição de matches, e também mostra
            // novas compatibilidades reais caso outro filme gere conexões.
            boolean temPessoas = !new MatchDAO().sugerir(
                    Sessao.getUsuario().getId(), atual.getId()).isEmpty();
            if (primeiraCurtida || temPessoas) {
                primeiraCurtida = false;
                mostrarPessoas();
            } else {
                mostrarDescoberta();
            }
        }));

        // RODAPÉ
        JPanel rodape = transparente(new BorderLayout());
        rodape.add(acoes, BorderLayout.CENTER);

        JButton detalhes = botao("Ver detalhes e comentários", ROXO_BOTAO);
        detalhes.addActionListener(e -> mostrarDetalhes(atual));

        JPanel linha = transparente(new FlowLayout(FlowLayout.CENTER));
        linha.add(detalhes);

        rodape.add(linha, BorderLayout.SOUTH);
        tela.add(rodape, BorderLayout.SOUTH);

        atualizar(tela);
    }

    // MONTA UM BOTÃO CIRCULAR E SUA LEGENDA
    private JPanel acaoCircular(String simbolo, String nome, Color fundo, Color contorno, Runnable acao) {
        JPanel p = transparente(new BorderLayout(0, 5));
        JButton b = botaoCircular(simbolo, fundo, contorno);
        b.addActionListener(e -> acao.run());

        p.add(b, BorderLayout.CENTER);

        JLabel legenda = label(nome, 16, TEXTO, false);
        legenda.setHorizontalAlignment(SwingConstants.CENTER);

        p.add(legenda, BorderLayout.SOUTH);
        return p;
    }


    // =========================================
    // MATCHES REAIS: substitui os seis perfis fictícios.
    // =========================================
    private void mostrarPessoas() {
        if (filmeCurtido == null || !Sessao.ativa()) {
            mostrarDescoberta();
            return;
        }

        JPanel tela = transparente(new BorderLayout(18, 18));
        tela.setOpaque(true);
        tela.setBackground(FUNDO);
        tela.setBorder(new EmptyBorder(10, 22, 22, 22));
        tela.add(topo(true), BorderLayout.NORTH);

        JPanel conteudo = transparente(new BorderLayout(20, 0));
        JPanel esquerda = transparente(new BorderLayout(0, 14));
        esquerda.setPreferredSize(new Dimension(290, 0));
        esquerda.add(cardFilme(filmeCurtido, 250, 330, true), BorderLayout.NORTH);

        JPanel contexto = new Cartao(CARD, 24);
        contexto.setLayout(new BorderLayout(0, 12));
        contexto.setBorder(new EmptyBorder(18, 18, 18, 18));
        contexto.add(label("Um filme em comum!", 20, CORAL, true), BorderLayout.NORTH);
        JTextArea explicacao = new JTextArea(
            "Estes perfis também curtiram este filme. " +
            "A afinidade considera filmes e gêneros favoritos.");
        explicacao.setEditable(false);
        explicacao.setLineWrap(true);
        explicacao.setWrapStyleWord(true);
        explicacao.setOpaque(false);
        explicacao.setForeground(SUAVE);
        explicacao.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        contexto.add(explicacao, BorderLayout.CENTER);
        JButton detalhes = botao("Ver detalhes e comentar", ROXO_BOTAO);
        detalhes.addActionListener(e -> mostrarDetalhes(filmeCurtido));
        contexto.add(detalhes, BorderLayout.SOUTH);
        esquerda.add(contexto, BorderLayout.CENTER);
        conteudo.add(esquerda, BorderLayout.WEST);

        java.util.List<AlgoritmoMatch.Resultado> sugestoes =
            new MatchDAO().sugerir(Sessao.getUsuario().getId(), filmeCurtido.getId());

        JPanel grade = transparente(new GridBagLayout());
        grade.setBorder(new EmptyBorder(4, 4, 4, 8));
        GridBagConstraints pos = new GridBagConstraints();
        pos.insets = new Insets(4, 4, 8, 8);
        pos.anchor = GridBagConstraints.NORTHWEST;
        pos.fill = GridBagConstraints.NONE;

        if (sugestoes.isEmpty()) {
            JPanel vazio = new Cartao(CARD, 24);
            vazio.setLayout(new BorderLayout());
            vazio.setBorder(new EmptyBorder(24, 24, 24, 24));
            JTextArea mensagem = new JTextArea(
                "Ainda não há outra pessoa cadastrada que tenha curtido este filme e tenha " +
                "preferências compatíveis com as suas.\n\n" +
                "Nenhum perfil fictício será mostrado. Quando outra conta curtir " +
                "este filme, ela poderá aparecer aqui.");
            mensagem.setLineWrap(true);
            mensagem.setWrapStyleWord(true);
            mensagem.setEditable(false);
            mensagem.setOpaque(false);
            mensagem.setForeground(TEXTO);
            mensagem.setFont(new Font("Times New Roman", Font.PLAIN, 18));
            vazio.setPreferredSize(new Dimension(540, 170));
            vazio.add(mensagem);
            pos.gridx = 0; pos.gridy = 0; pos.gridwidth = 2;
            grade.add(vazio, pos);
        } else {
            for (int i = 0; i < sugestoes.size(); i++) {
                pos.gridx = i % 2;
                pos.gridy = i / 2;
                pos.gridwidth = 1;
                grade.add(cardPessoa(sugestoes.get(i)), pos);
            }
        }
        // Ocupa somente o espaço necessário, sem esticar os cartões.
        pos.gridx = 0;
        pos.gridy = Math.max(1, (sugestoes.size() + 1) / 2);
        pos.gridwidth = 2;
        pos.weighty = 1;
        pos.fill = GridBagConstraints.VERTICAL;
        grade.add(Box.createVerticalGlue(), pos);

        JScrollPane rolagem = new JScrollPane(grade,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rolagem.setBorder(null);
        rolagem.getViewport().setBackground(FUNDO);
        rolagem.getVerticalScrollBar().setUnitIncrement(18);
        conteudo.add(rolagem, BorderLayout.CENTER);

        tela.add(conteudo, BorderLayout.CENTER);
        atualizar(tela);
    }

    private JPanel cardPessoa(AlgoritmoMatch.Resultado resultado) {
        Usuario pessoa = resultado.usuario;
        Cartao card = new Cartao(CARD, 26);
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel cab = transparente(new BorderLayout(10, 0));
        JLabel avatar = label("●", 40, CORAL, true);
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(46, 55));
        cab.add(avatar, BorderLayout.WEST);

        JPanel dados = transparente(new GridLayout(2, 1, 0, 3));
        dados.add(label(pessoa.getNome() + ", " + pessoa.getIdade(), 18, TEXTO, true));
        dados.add(label(resultado.percentual + "% de afinidade", 17, CORAL, true));
        cab.add(dados, BorderLayout.CENTER);
        card.add(cab, BorderLayout.NORTH);

        JPanel centro = transparente(new GridLayout(3, 1, 0, 3));
        centro.add(label(resultado.motivo, 15, TEXTO, false));
        centro.add(label(resultado.filmesEmComum + " filmes em comum", 15, SUAVE, false));
        centro.add(label(resultado.generosEmComum + " gêneros favoritos em comum", 14, SUAVE, false));
        card.add(centro, BorderLayout.CENTER);

        JButton ver = botao("Ver perfil e conectar", CORAL);
        ver.addActionListener(e ->
            new TelaPerfilPessoa(pessoa, filmeCurtido, resultado)
        );
        card.add(ver, BorderLayout.SOUTH);
        card.setPreferredSize(new Dimension(270, 208));
        return card;
    }
}
