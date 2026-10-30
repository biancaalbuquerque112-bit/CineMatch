
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class TelaDescobrir extends JFrame {

    private final Color FUNDO = new Color(18, 18, 30);
    private final Color CARD = new Color(32, 32, 48);
    private final Color ROSA = new Color(255, 75, 100);
    private final Color CINZA = new Color(75, 75, 95);

    private final ArrayList<Filme> filmes;
    private final ArrayList<Filme> curtidos = new ArrayList<>();
    private final ArrayList<Filme> salvos = new ArrayList<>();

    private final Map<String, String> comentarios =
        new HashMap<>();

    private int indiceAtual = 0;
    private JPanel principal;
    private Filme filmeCurtido;

    public TelaDescobrir() {

        filmes = new Catalogo().getFilmes();

        setTitle("CineMatch - Descobrir");
        setSize(1050, 780);
        setMinimumSize(new Dimension(800, 620));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        principal = new JPanel(new BorderLayout());
        principal.setBackground(FUNDO);
        setContentPane(principal);

        mostrarDescoberta();
        setVisible(true);
    }

    private JLabel texto(String valor, int tamanho, Color cor) {
        JLabel label = new JLabel(valor);
        label.setFont(new Font("Arial", Font.BOLD, tamanho));
        label.setForeground(cor);
        return label;
    }

    private JButton botao(String nome, Color cor) {
        JButton b = new JButton(nome);
        b.setBackground(cor);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(
            12, 18, 12, 18
        ));
        return b;
    }

    private JLabel imagemFilme(
            Filme filme, int largura, int altura) {

        JLabel imagem = new JLabel("Sem capa");
        imagem.setForeground(Color.WHITE);
        imagem.setHorizontalAlignment(SwingConstants.CENTER);
        imagem.setPreferredSize(
            new Dimension(largura, altura)
        );

        File arquivo = new File("src/" + filme.getCapa());

        if (!arquivo.exists()) {
            arquivo = new File(filme.getCapa());
        }

        if (arquivo.exists()) {

            ImageIcon original = new ImageIcon(
                arquivo.getAbsolutePath()
            );

            if (original.getIconWidth() > 0) {

                Image redimensionada = original.getImage()
                    .getScaledInstance(
                        largura, altura, Image.SCALE_SMOOTH
                    );

                imagem.setText("");
                imagem.setIcon(
                    new ImageIcon(redimensionada)
                );
            }
        }

        return imagem;
    }

    private void atualizarTela(JPanel conteudo) {
        principal.removeAll();
        principal.add(conteudo, BorderLayout.CENTER);
        principal.revalidate();
        principal.repaint();
    }

    private JPanel cabecalho(String subtitulo) {

        JPanel topo = new JPanel(new BorderLayout(15, 0));
        topo.setBackground(FUNDO);
        topo.setBorder(BorderFactory.createEmptyBorder(
            10, 10, 20, 10
        ));

        JLabel logo = texto("CineMatch", 32, ROSA);
        topo.add(logo, BorderLayout.WEST);

        JPanel direita = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 12, 0)
        );
        direita.setBackground(FUNDO);

        JLabel descricao = texto(
            subtitulo, 13, Color.LIGHT_GRAY
        );

        JButton voltar = botao("← Início", CINZA);

        voltar.addActionListener(e -> {
            new TelaInicial();
            dispose();
        });

        direita.add(descricao);
        direita.add(voltar);

        topo.add(direita, BorderLayout.EAST);

        return topo;
    }

    private void mostrarDescoberta() {

        JPanel tela = new JPanel(new BorderLayout());
        tela.setBackground(FUNDO);
        tela.setBorder(BorderFactory.createEmptyBorder(
            20, 30, 20, 30
        ));

        tela.add(
            cabecalho("Descubra seu próximo filme favorito"),
            BorderLayout.NORTH
        );

        if (indiceAtual >= filmes.size()) {

            JPanel centro = new JPanel(new GridBagLayout());
            centro.setBackground(FUNDO);

            JLabel mensagem = texto(
                "Você descobriu todos os filmes!",
                24, ROSA
            );

            centro.add(mensagem);
            tela.add(centro, BorderLayout.CENTER);

            JButton reiniciar = botao("Recomeçar", ROSA);

            reiniciar.addActionListener(e -> {
                indiceAtual = 0;
                mostrarDescoberta();
            });

            tela.add(reiniciar, BorderLayout.SOUTH);
            atualizarTela(tela);
            return;
        }

        Filme filme = filmes.get(indiceAtual);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(FUNDO);

        JPanel card = new JPanel();
        card.setBackground(CARD);
        card.setLayout(
            new BoxLayout(card, BoxLayout.Y_AXIS)
        );
        card.setBorder(BorderFactory.createEmptyBorder(
            15, 20, 15, 20
        ));

        JLabel capa = imagemFilme(filme, 260, 350);
        capa.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = texto(
            filme.getTitulo(), 25, Color.WHITE
        );
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel genero = texto(
            filme.getGenero() + " • " + filme.getAno(),
            14, Color.LIGHT_GRAY
        );
        genero.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel contador = texto(
            (indiceAtual + 1) + " / " + filmes.size(),
            13, ROSA
        );
        contador.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(capa);
        card.add(Box.createVerticalStrut(15));
        card.add(titulo);
        card.add(Box.createVerticalStrut(8));
        card.add(genero);
        card.add(Box.createVerticalStrut(12));
        card.add(contador);

        centro.add(card);
        tela.add(centro, BorderLayout.CENTER);

        JPanel acoes = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 15, 10)
        );
        acoes.setBackground(FUNDO);

        JButton passar = botao("✖ Passar", CINZA);
        JButton salvar = botao(
            "★ Salvar", new Color(190, 140, 45)
        );
        JButton curtir = botao("♥ Curtir", ROSA);
        JButton comentar = botao(
            "Comentar", new Color(110, 65, 120)
        );

        passar.addActionListener(e -> {
            indiceAtual++;
            mostrarDescoberta();
        });

        salvar.addActionListener(e -> {
            if (!salvos.contains(filme)) {
                salvos.add(filme);
            }
            indiceAtual++;
            mostrarDescoberta();
        });

        curtir.addActionListener(e -> {
            if (!curtidos.contains(filme)) {
                curtidos.add(filme);
            }
            filmeCurtido = filme;
            indiceAtual++;
            mostrarPessoas();
        });

        comentar.addActionListener(e -> {
            comentarFilme(filme, false);
        });

        acoes.add(passar);
        acoes.add(salvar);
        acoes.add(curtir);
        acoes.add(comentar);

        tela.add(acoes, BorderLayout.SOUTH);
        atualizarTela(tela);
    }

    private void comentarFilme(
            Filme filme, boolean voltarPessoas) {

        JPanel tela = new JPanel(new BorderLayout(20, 20));
        tela.setBackground(FUNDO);
        tela.setBorder(BorderFactory.createEmptyBorder(
            25, 40, 25, 40
        ));

        tela.add(
            cabecalho("Seu comentário sobre o filme"),
            BorderLayout.NORTH
        );

        JPanel centro = new JPanel(new BorderLayout(25, 0));
        centro.setBackground(FUNDO);

        centro.add(
            imagemFilme(filme, 220, 310),
            BorderLayout.WEST
        );

        JPanel editor = new JPanel();
        editor.setBackground(CARD);
        editor.setLayout(
            new BoxLayout(editor, BoxLayout.Y_AXIS)
        );
        editor.setBorder(BorderFactory.createEmptyBorder(
            25, 25, 25, 25
        ));

        JLabel titulo = texto(
            "Por que você gosta de " + filme.getTitulo() + "?",
            20, Color.WHITE
        );
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea campo = new JTextArea(8, 30);
        campo.setText(
            comentarios.getOrDefault(filme.getTitulo(), "")
        );
        campo.setLineWrap(true);
        campo.setWrapStyleWord(true);
        campo.setFont(new Font("Arial", Font.PLAIN, 16));
        campo.setForeground(Color.WHITE);
        campo.setBackground(new Color(45, 45, 65));
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createEmptyBorder(
            15, 15, 15, 15
        ));

        JScrollPane rolagem = new JScrollPane(campo);
        rolagem.setAlignmentX(Component.LEFT_ALIGNMENT);

        editor.add(titulo);
        editor.add(Box.createVerticalStrut(20));
        editor.add(rolagem);

        centro.add(editor, BorderLayout.CENTER);
        tela.add(centro, BorderLayout.CENTER);

        JPanel botoes = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 12, 5)
        );
        botoes.setBackground(FUNDO);

        JButton cancelar = botao("Cancelar", CINZA);
        JButton salvar = botao("Salvar comentário", ROSA);

        Runnable voltar = () -> {
            if (voltarPessoas) {
                mostrarPessoas();
            } else {
                mostrarDescoberta();
            }
        };

        cancelar.addActionListener(e -> voltar.run());

        salvar.addActionListener(e -> {
            String comentario = campo.getText().trim();

            if (comentario.isEmpty()) {
                comentarios.remove(filme.getTitulo());
            } else {
                comentarios.put(
                    filme.getTitulo(), comentario
                );
            }

            voltar.run();
        });

        botoes.add(cancelar);
        botoes.add(salvar);

        tela.add(botoes, BorderLayout.SOUTH);
        atualizarTela(tela);
    }

    private void mostrarPessoas() {

        JPanel tela = new JPanel(new BorderLayout(20, 20));
        tela.setBackground(FUNDO);
        tela.setBorder(BorderFactory.createEmptyBorder(
            20, 30, 20, 30
        ));

        tela.add(
            cabecalho("Pessoas que também curtiram"),
            BorderLayout.NORTH
        );

        JPanel conteudo = new JPanel(
            new BorderLayout(25, 0)
        );
        conteudo.setBackground(FUNDO);

        JPanel painelFilme = new JPanel();
        painelFilme.setBackground(CARD);
        painelFilme.setLayout(
            new BoxLayout(painelFilme, BoxLayout.Y_AXIS)
        );
        painelFilme.setBorder(BorderFactory.createEmptyBorder(
            15, 15, 15, 15
        ));

        JLabel capa = imagemFilme(
            filmeCurtido, 220, 310
        );
        capa.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = texto(
            filmeCurtido.getTitulo(), 22, Color.WHITE
        );
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        painelFilme.add(capa);
        painelFilme.add(Box.createVerticalStrut(15));
        painelFilme.add(titulo);
        painelFilme.add(Box.createVerticalStrut(15));

        String comentario = comentarios.getOrDefault(
            filmeCurtido.getTitulo(), ""
        );

        if (!comentario.isEmpty()) {
            JTextArea meuComentario = new JTextArea(
                comentario
            );
            meuComentario.setLineWrap(true);
            meuComentario.setWrapStyleWord(true);
            meuComentario.setEditable(false);
            meuComentario.setBackground(CARD);
            meuComentario.setForeground(Color.LIGHT_GRAY);
            meuComentario.setFont(
                new Font("Arial", Font.ITALIC, 13)
            );
            meuComentario.setMaximumSize(
                new Dimension(220, 90)
            );
            painelFilme.add(meuComentario);
        }

        JButton editar = botao(
            "Editar meu comentário",
            new Color(110, 65, 120)
        );
        editar.setAlignmentX(Component.CENTER_ALIGNMENT);

        editar.addActionListener(e ->
            comentarFilme(filmeCurtido, true)
        );

        painelFilme.add(Box.createVerticalStrut(15));
        painelFilme.add(editar);

        conteudo.add(painelFilme, BorderLayout.WEST);

        JPanel lista = new JPanel();
        lista.setBackground(FUNDO);
        lista.setLayout(
            new BoxLayout(lista, BoxLayout.Y_AXIS)
        );

        lista.add(criarPerfil(
            "Marina, 25",
            "Amizades e relacionamento",
            "A história desse filme é incrível!"
        ));

        lista.add(criarPerfil(
            "Lucas, 27",
            "Amizades",
            "Adoro a trilha sonora!"
        ));

        lista.add(criarPerfil(
            "Camila, 24",
            "Relacionamento",
            "Um dos meus filmes favoritos!"
        ));

        lista.add(criarPerfil(
            "Rafael, 26",
            "Amizades e relacionamento",
            "Assisti várias vezes!"
        ));

        JScrollPane rolagem = new JScrollPane(lista);
        rolagem.setBorder(null);
        rolagem.getVerticalScrollBar()
            .setUnitIncrement(16);

        conteudo.add(rolagem, BorderLayout.CENTER);
        tela.add(conteudo, BorderLayout.CENTER);

        JButton continuar = botao(
            "Continuar descobrindo filmes →", ROSA
        );

        continuar.addActionListener(e ->
            mostrarDescoberta()
        );

        tela.add(continuar, BorderLayout.SOUTH);
        atualizarTela(tela);
    }

    private JPanel criarPerfil(
            String nome,
            String interesse,
            String comentario) {

        JPanel perfil = new JPanel(
            new BorderLayout(10, 10)
        );
        perfil.setBackground(CARD);
        perfil.setBorder(BorderFactory.createEmptyBorder(
            18, 18, 18, 18
        ));
        perfil.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 135)
        );

        JPanel informacoes = new JPanel();
        informacoes.setBackground(CARD);
        informacoes.setLayout(
            new BoxLayout(informacoes, BoxLayout.Y_AXIS)
        );

        informacoes.add(texto(
            nome, 18, Color.WHITE
        ));
        informacoes.add(Box.createVerticalStrut(5));
        informacoes.add(texto(
            interesse, 12, Color.LIGHT_GRAY
        ));
        informacoes.add(Box.createVerticalStrut(8));
        informacoes.add(texto(
            "\"" + comentario + "\"", 12, ROSA
        ));

        JButton verPerfil = botao(
            "Ver perfil", ROSA
        );

        verPerfil.addActionListener(e -> {
            new TelaPerfilPessoa(
                nome,
                interesse,
                comentario,
                filmeCurtido.getTitulo()
            );
        });

        perfil.add(informacoes, BorderLayout.CENTER);
        perfil.add(verPerfil, BorderLayout.EAST);

        JPanel espaco = new JPanel(
            new BorderLayout()
        );
        espaco.setBackground(FUNDO);
        espaco.setBorder(BorderFactory.createEmptyBorder(
            0, 0, 12, 0
        ));
        espaco.add(perfil);

        return espaco;
    }
}
