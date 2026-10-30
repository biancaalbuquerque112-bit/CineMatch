import java.awt.*;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TelaDescobrir extends JFrame {
    private final Color FUNDO = new Color(17, 16, 30);
    private final Color CARD = new Color(30, 27, 45);
    private final Color ROSA = new Color(247, 48, 107);
    private final Color TEXTO = new Color(244, 241, 249);
    private final Color SUAVE = new Color(181, 174, 195);
    private final Color BORDA = new Color(73, 47, 78);
    private final ArrayList<Filme> filmes;
    private final ArrayList<Filme> curtidos = new ArrayList<>();
    private final ArrayList<Filme> salvos = new ArrayList<>();
    private final Map<String, String> comentarios = new HashMap<>();
    private int indiceAtual = 0;
    private Filme filmeCurtido;
    private JPanel principal;

    public TelaDescobrir() {
        filmes = new Catalogo().getFilmes();
        setTitle("CineMatch - Descobrir");
        setSize(1150, 820);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        principal = new JPanel(new BorderLayout());
        principal.setBackground(FUNDO);
        setContentPane(principal);
        mostrarDescoberta();
        setVisible(true);
    }

    private JLabel label(String valor, int tamanho, Color cor, boolean negrito) {
        JLabel l = new JLabel(valor);
        l.setFont(new Font("SansSerif", negrito ? Font.BOLD : Font.PLAIN, tamanho));
        l.setForeground(cor);
        return l;
    }

    private JPanel transparente(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setOpaque(false);
        return p;
    }

    private class Cartao extends JPanel {
        private final Color cor;
        private final int raio;
        Cartao(Color cor, int raio) {
            this.cor = cor;
            this.raio = raio;
            setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
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

    private JButton botao(String texto, Color cor) {
        JButton b = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? cor.brighter() : cor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 17, 10, 17));
        return b;
    }

    private JButton botaoCircular(String simbolo, Color fundo, Color contorno) {
        JButton b = new JButton(simbolo) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int d = Math.min(getWidth(), getHeight()) - 7;
                int x = (getWidth() - d) / 2;
                int y = (getHeight() - d) / 2;
                g2.setColor(getModel().isRollover() ? fundo.brighter() : fundo);
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

    private File arquivoCapa(Filme filme) {
        File direto = new File(filme.getCapa());
        if (direto.isFile()) return direto;
        File src = new File("src", filme.getCapa());
        if (src.isFile()) return src;
        return direto;
    }

    private JLabel capa(Filme filme, int largura, int altura) {
        JLabel imagem = new JLabel("Capa indisponível", SwingConstants.CENTER);
        imagem.setForeground(SUAVE);
        imagem.setFont(new Font("SansSerif", Font.PLAIN, 13));
        imagem.setOpaque(true);
        imagem.setBackground(new Color(38, 34, 56));
        imagem.setPreferredSize(new Dimension(largura, altura));
        imagem.setMinimumSize(new Dimension(largura, altura));
        imagem.setMaximumSize(new Dimension(largura, altura));
        File arquivo = arquivoCapa(filme);
        if (arquivo.isFile()) {
            ImageIcon original = new ImageIcon(arquivo.getAbsolutePath());
            if (original.getIconWidth() > 0) {
                Image img = original.getImage().getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
                imagem.setIcon(new ImageIcon(img));
                imagem.setText("");
            }
        }
        return imagem;
    }

    private void atualizar(JPanel tela) {
        principal.removeAll();
        principal.add(tela, BorderLayout.CENTER);
        principal.revalidate();
        principal.repaint();
    }

    private JPanel topo(boolean pessoas) {
        JPanel barra = transparente(new BorderLayout(14, 8));
        barra.setBorder(new EmptyBorder(13, 20, 14, 20));
        JButton inicio = botao("⌂  Início", new Color(46, 41, 64));
        inicio.addActionListener(e -> { new TelaInicial(); dispose(); });
        barra.add(inicio, BorderLayout.WEST);
        JPanel centro = transparente(new GridLayout(2, 1, 0, 3));
        JLabel titulo = label(pessoas ? "♥  Pessoas que também curtiram" : "🎬  CineMatch", pessoas ? 25 : 34, pessoas ? TEXTO : ROSA, true);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel sub = label(pessoas ? "Veja quem compartilha esse gosto com você" : "Descubra seu próximo filme favorito", 14, SUAVE, false);
        sub.setHorizontalAlignment(SwingConstants.CENTER);
        centro.add(titulo);
        centro.add(sub);
        barra.add(centro, BorderLayout.CENTER);
        JButton acao = botao(pessoas ? "Continuar filmes  ›" : "Meu perfil  ♡", pessoas ? ROSA : new Color(46, 41, 64));
        acao.addActionListener(e -> {
            if (pessoas) mostrarDescoberta();
            else new TelaMeuPerfil();
        });
        barra.add(acao, BorderLayout.EAST);
        return barra;
    }

    private JPanel cardFilme(Filme filme, int largura, int alturaCapa, boolean compacto) {
        Cartao card = new Cartao(CARD, 30);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(10, 10, 12, 10));
        JLabel imagem = capa(filme, largura, alturaCapa);
        imagem.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(imagem);
        card.add(Box.createVerticalStrut(9));
        JLabel nome = label(filme.getTitulo(), compacto ? 19 : 22, TEXTO, true);
        nome.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(nome);
        card.add(Box.createVerticalStrut(4));
        JLabel genero = label(filme.getGenero(), 13, SUAVE, false);
        genero.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(genero);
        if (!compacto) {
            card.add(Box.createVerticalStrut(6));
            JLabel contador = label(filme.getAno() + "    •    " + (indiceAtual + 1) + " / " + filmes.size(), 13, ROSA, true);
            contador.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(contador);
        }
        return card;
    }

    private void mostrarDescoberta() {
        JPanel tela = transparente(new BorderLayout(15, 15));
        tela.setBackground(FUNDO);
        tela.setOpaque(true);
        tela.setBorder(new EmptyBorder(10, 24, 20, 24));
        tela.add(topo(false), BorderLayout.NORTH);
        if (filmes.isEmpty() || indiceAtual >= filmes.size()) {
            JPanel fim = transparente(new GridBagLayout());
            JPanel conteudo = transparente(new GridLayout(3, 1, 0, 18));
            JLabel msg = label("Você descobriu todos os filmes! ♥", 24, TEXTO, true);
            msg.setHorizontalAlignment(SwingConstants.CENTER);
            conteudo.add(msg);
            JButton reiniciar = botao("Recomeçar descoberta", ROSA);
            reiniciar.addActionListener(e -> { indiceAtual = 0; mostrarDescoberta(); });
            conteudo.add(reiniciar);
            JButton voltar = botao("Voltar ao início", new Color(50, 45, 65));
            voltar.addActionListener(e -> { new TelaInicial(); dispose(); });
            conteudo.add(voltar);
            fim.add(conteudo);
            tela.add(fim, BorderLayout.CENTER);
            atualizar(tela);
            return;
        }
        Filme atual = filmes.get(indiceAtual);
        JPanel palco = transparente(new GridBagLayout());
        JPanel cartoes = transparente(new FlowLayout(FlowLayout.CENTER, 0, 8));
        if (filmes.size() > 1) {
            int anterior = (indiceAtual - 1 + filmes.size()) % filmes.size();
            JLabel esquerda = capa(filmes.get(anterior), 145, 225);
            esquerda.setBorder(BorderFactory.createLineBorder(BORDA, 2));
            cartoes.add(esquerda);
        }
        JPanel central = cardFilme(atual, 280, 300, false);
        cartoes.add(central);
        if (filmes.size() > 2) {
            int proximo = (indiceAtual + 1) % filmes.size();
            JLabel direita = capa(filmes.get(proximo), 145, 225);
            direita.setBorder(BorderFactory.createLineBorder(BORDA, 2));
            cartoes.add(direita);
        }
        palco.add(cartoes);
        JScrollPane rolagemPalco = new JScrollPane(palco);
        rolagemPalco.setBorder(null);
        rolagemPalco.setOpaque(false);
        rolagemPalco.getViewport().setOpaque(false);
        rolagemPalco.getVerticalScrollBar().setUnitIncrement(16);
        tela.add(rolagemPalco, BorderLayout.CENTER);

        JPanel acoes = transparente(new FlowLayout(FlowLayout.CENTER, 32, 0));
        acoes.add(acaoCircular("✕", "Passar", new Color(37, 31, 50), ROSA, () -> {
            indiceAtual++; mostrarDescoberta();
        }));
        acoes.add(acaoCircular("★", "Salvar", new Color(37, 31, 50), new Color(255, 185, 76), () -> {
            if (!salvos.contains(atual)) salvos.add(atual);
            indiceAtual++; mostrarDescoberta();
        }));
        acoes.add(acaoCircular("♥", "Curtir", ROSA, Color.WHITE, () -> {
            if (!curtidos.contains(atual)) curtidos.add(atual);
            filmeCurtido = atual;
            indiceAtual++;
            mostrarPessoas();
        }));
        JPanel rodape = transparente(new BorderLayout());
        rodape.add(acoes, BorderLayout.CENTER);
        JButton comentar = botao("✎  Comentar sobre este filme", new Color(48, 37, 63));
        comentar.addActionListener(e -> comentarFilme(atual, false));
        JPanel linha = transparente(new FlowLayout(FlowLayout.CENTER));
        linha.add(comentar);
        rodape.add(linha, BorderLayout.SOUTH);
        tela.add(rodape, BorderLayout.SOUTH);
        atualizar(tela);
    }

    private JPanel acaoCircular(String simbolo, String nome, Color fundo, Color contorno, Runnable acao) {
        JPanel p = transparente(new BorderLayout(0, 5));
        JButton b = botaoCircular(simbolo, fundo, contorno);
        b.addActionListener(e -> acao.run());
        p.add(b, BorderLayout.CENTER);
        JLabel legenda = label(nome, 15, TEXTO, false);
        legenda.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(legenda, BorderLayout.SOUTH);
        return p;
    }

    private void comentarFilme(Filme filme, boolean voltarPessoas) {
        JPanel tela = transparente(new BorderLayout(20, 20));
        tela.setOpaque(true);
        tela.setBackground(FUNDO);
        tela.setBorder(new EmptyBorder(20, 40, 30, 40));
        tela.add(topo(voltarPessoas), BorderLayout.NORTH);
        JPanel conteudo = transparente(new BorderLayout(25, 0));
        conteudo.add(capa(filme, 230, 330), BorderLayout.WEST);
        Cartao editor = new Cartao(CARD, 26);
        editor.setLayout(new BorderLayout(0, 15));
        editor.setBorder(new EmptyBorder(25, 25, 25, 25));
        editor.add(label("Por que você gosta de " + filme.getTitulo() + "?", 20, TEXTO, true), BorderLayout.NORTH);
        JTextArea campo = new JTextArea(comentarios.getOrDefault(filme.getTitulo(), ""));
        campo.setLineWrap(true);
        campo.setWrapStyleWord(true);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        campo.setForeground(TEXTO);
        campo.setCaretColor(TEXTO);
        campo.setBackground(new Color(42, 37, 57));
        campo.setBorder(new EmptyBorder(15, 15, 15, 15));
        editor.add(new JScrollPane(campo), BorderLayout.CENTER);
        conteudo.add(editor, BorderLayout.CENTER);
        tela.add(conteudo, BorderLayout.CENTER);
        JPanel botoes = transparente(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        Runnable voltar = () -> { if (voltarPessoas) mostrarPessoas(); else mostrarDescoberta(); };
        JButton cancelar = botao("Cancelar", new Color(55, 48, 72));
        cancelar.addActionListener(e -> voltar.run());
        JButton salvar = botao("Salvar comentário  ♥", ROSA);
        salvar.addActionListener(e -> {
            String comentario = campo.getText().trim();
            if (comentario.isEmpty()) comentarios.remove(filme.getTitulo());
            else comentarios.put(filme.getTitulo(), comentario);
            voltar.run();
        });
        botoes.add(cancelar);
        botoes.add(salvar);
        tela.add(botoes, BorderLayout.SOUTH);
        atualizar(tela);
    }

    private void mostrarPessoas() {
        if (filmeCurtido == null) { mostrarDescoberta(); return; }
        JPanel tela = transparente(new BorderLayout(18, 18));
        tela.setOpaque(true);
        tela.setBackground(FUNDO);
        tela.setBorder(new EmptyBorder(10, 22, 22, 22));
        tela.add(topo(true), BorderLayout.NORTH);
        JPanel conteudo = transparente(new BorderLayout(20, 0));
        JPanel esquerda = transparente(new BorderLayout(0, 14));
        esquerda.setPreferredSize(new Dimension(300, 0));
        JPanel filme = cardFilme(filmeCurtido, 250, 330, true);
        esquerda.add(filme, BorderLayout.NORTH);
        Cartao comentario = new Cartao(CARD, 24);
        comentario.setLayout(new BorderLayout(0, 12));
        comentario.setBorder(new EmptyBorder(18, 18, 18, 18));
        comentario.add(label("❝  Seu comentário", 15, ROSA, true), BorderLayout.NORTH);
        JTextArea resumo = new JTextArea(comentarios.getOrDefault(filmeCurtido.getTitulo(), "Escreva o que você achou deste filme..."));
        resumo.setLineWrap(true);
        resumo.setWrapStyleWord(true);
        resumo.setEditable(false);
        resumo.setOpaque(false);
        resumo.setForeground(SUAVE);
        resumo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        resumo.setRows(3);
        comentario.add(resumo, BorderLayout.CENTER);
        JButton editar = botao("✎  Escrever / editar", new Color(55, 39, 70));
        editar.addActionListener(e -> comentarFilme(filmeCurtido, true));
        comentario.add(editar, BorderLayout.SOUTH);
        esquerda.add(comentario, BorderLayout.CENTER);
        conteudo.add(esquerda, BorderLayout.WEST);

        JPanel grade = transparente(new GridLayout(0, 2, 12, 12));
        grade.setBorder(new EmptyBorder(2, 2, 2, 8));
        grade.add(cardPessoa("Marina, 25", "Amizades e relacionamento", "Fantasia • Animação", "Coraline me marcou muito pela atmosfera única!"));
        grade.add(cardPessoa("Lucas, 27", "Amizades", "Animação • Terror", "A estética e a história são surreais, amo!"));
        grade.add(cardPessoa("Camila, 24", "Relacionamento", "Fantasia • Drama", "Um dos meus filmes favoritos da vida!"));
        grade.add(cardPessoa("Rafael, 26", "Amizades e relacionamento", "Animação • Aventura", "A trilha sonora é perfeita!"));
        grade.add(cardPessoa("Beatriz, 23", "Amizades", "Fantasia • Mistério", "A Coraline é muito inspiradora!"));
        grade.add(cardPessoa("Thiago, 28", "Relacionamento", "Animação • Suspense", "Esse filme tem uma atmosfera incrível!"));
        JScrollPane rolagem = new JScrollPane(grade);
        rolagem.setBorder(null);
        rolagem.getViewport().setOpaque(false);
        rolagem.setOpaque(false);
        rolagem.getVerticalScrollBar().setUnitIncrement(20);
        rolagem.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        conteudo.add(rolagem, BorderLayout.CENTER);
        tela.add(conteudo, BorderLayout.CENTER);
        atualizar(tela);
    }

    private JPanel cardPessoa(String nome, String interesse, String generos, String frase) {
        Cartao card = new Cartao(CARD, 26);
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));
        JPanel cab = transparente(new BorderLayout(10, 0));
        JLabel avatar = new JLabel("●", SwingConstants.CENTER);
        avatar.setForeground(ROSA);
        avatar.setFont(new Font("SansSerif", Font.BOLD, 48));
        avatar.setPreferredSize(new Dimension(62, 65));
        cab.add(avatar, BorderLayout.WEST);
        JPanel dados = transparente(new GridLayout(2, 1, 0, 3));
        dados.add(label(nome, 17, TEXTO, true));
        dados.add(label(interesse, 11, SUAVE, false));
        cab.add(dados, BorderLayout.CENTER);
        card.add(cab, BorderLayout.NORTH);
        JPanel centro = transparente(new BorderLayout(0, 10));
        centro.add(label(generos, 12, ROSA, true), BorderLayout.NORTH);
        JTextArea citacao = new JTextArea("❝  " + frase);
        citacao.setEditable(false);
        citacao.setLineWrap(true);
        citacao.setWrapStyleWord(true);
        citacao.setOpaque(false);
        citacao.setForeground(SUAVE);
        citacao.setFont(new Font("SansSerif", Font.PLAIN, 13));
        centro.add(citacao, BorderLayout.CENTER);
        card.add(centro, BorderLayout.CENTER);
        JButton ver = botao("♙  Ver perfil", ROSA);
        ver.addActionListener(e -> new TelaPerfilPessoa(nome, interesse, frase, filmeCurtido.getTitulo()));
        card.add(ver, BorderLayout.SOUTH);
        card.setPreferredSize(new Dimension(235, 220));
        return card;
    }
}
