
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TelaMeuPerfil extends JFrame {

    // CORES DO CINEMATCH
    private final Color FUNDO = new Color(43, 22, 70);
    private final Color CARD = new Color(56, 32, 82);
    private final Color ROXO = new Color(65, 37, 93);
    private final Color CORAL = new Color(255, 98, 92);
    private final Color TEXTO = new Color(247, 243, 250);
    private final Color SUAVE = new Color(220, 204, 232);

    private final File ARQUIVO =
            new File("perfil_cinematch.properties");

    private JTextField campoNome;
    private JComboBox<String> campoDia;
    private JComboBox<String> campoMes;
    private JComboBox<String> campoAno;
    private JTextArea campoBio;
    private JComboBox<String> campoIntencao;

    private JComboBox<String> campoGenero;
    private JComboBox<String> campoOrientacao;
    private JComboBox<String> campoInteresse;

    private JCheckBox mostrarInformacoes;
    private JCheckBox permitirSugestoes;

    private JLabel fotoPerfil;
    private File arquivoFoto;

    private final ArrayList<String> nomesGeneros =
            new ArrayList<>();

    private final ArrayList<JToggleButton> estrelasGeneros =
            new ArrayList<>();

    // TODOS OS 18 GÊNEROS
    private final String[] GENEROS = {
            "Ação",
            "Aventura",
            "Animação",
            "Biografia",
            "Cinema Nacional",
            "Comédia",
            "Comédia Romântica",
            "Crime",
            "Documentário",
            "Drama",
            "Família",
            "Fantasia",
            "Ficção Científica",
            "Mistério",
            "Musical",
            "Romance",
            "Suspense",
            "Terror"
    };

    public TelaMeuPerfil() {

        setTitle("CineMatch - Meu Perfil");
        setSize(1000, 820);
        setMinimumSize(new Dimension(700, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(
                new BorderLayout(0, 14)
        );

        principal.setBackground(FUNDO);
        principal.setBorder(
                new EmptyBorder(16, 24, 18, 24)
        );

        // CABEÇALHO SEM O SLOGAN
        principal.add(
                criarCabecalho(),
                BorderLayout.NORTH
        );

        // ÁREA DO FORMULÁRIO
        JPanel coluna = new JPanel();

        coluna.setLayout(
                new BoxLayout(coluna, BoxLayout.Y_AXIS)
        );

        coluna.setBackground(CARD);
        coluna.setBorder(
                new EmptyBorder(26, 28, 30, 28)
        );

        coluna.add(criarAreaFoto());
        coluna.add(Box.createVerticalStrut(26));
        coluna.add(criarFormulario());

        // Centraliza o formulário horizontalmente,
        // mas respeita sua altura real.
        JPanel conteudo = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 0, 0)
        );

        conteudo.setBackground(FUNDO);

        coluna.setPreferredSize(
                new Dimension(
                        610,
                        coluna.getPreferredSize().height
                )
        );

        conteudo.add(coluna);

        // A ROLAGEM VAI ATÉ O FINAL DOS 18 GÊNEROS.
        JScrollPane rolagem = new JScrollPane(
                conteudo,
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

        // RODAPÉ FIXO
        JPanel rodape = new JPanel(
                new GridLayout(1, 2, 15, 0)
        );

        rodape.setOpaque(false);

        JButton fechar = criarBotao(
                "Fechar perfil", ROXO
        );

        fechar.addActionListener(e -> dispose());

        JButton salvar = criarBotao(
                "Salvar meu perfil", CORAL
        );

        salvar.addActionListener(e -> salvarPerfil());

        rodape.add(fechar);
        rodape.add(salvar);

        principal.add(
                rodape,
                BorderLayout.SOUTH
        );

        setContentPane(principal);

        carregarPerfil();

        setVisible(true);

        SwingUtilities.invokeLater(() ->
                rolagem.getVerticalScrollBar().setValue(0)
        );
    }

    // ======================================
    // CABEÇALHO
    // ======================================

    private JPanel criarCabecalho() {

        JPanel cabecalho = new JPanel(
                new BorderLayout(12, 0)
        );

        cabecalho.setOpaque(false);

        JPanel marca = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 0)
        );

        marca.setOpaque(false);

        JLabel icone = new JLabel();

        File logo = new File(
                "imagens/icone_cinematch.png"
        );

        if (!logo.isFile()) {
            logo = new File(
                    "src/imagens/icone_cinematch.png"
            );
        }

        if (logo.isFile()) {

            ImageIcon original = new ImageIcon(
                    logo.getAbsolutePath()
            );

            if (original.getIconWidth() > 0) {

                Image imagem = original.getImage()
                        .getScaledInstance(
                                65,
                                48,
                                Image.SCALE_SMOOTH
                        );

                icone.setIcon(
                        new ImageIcon(imagem)
                );
            }
        }

        JLabel nomeMarca = new JLabel("CineMatch");

        nomeMarca.setFont(new Font(
                "Nimbus Sans", Font.BOLD, 32
        ));

        nomeMarca.setForeground(TEXTO);

        marca.add(icone);
        marca.add(nomeMarca);

        cabecalho.add(
                marca,
                BorderLayout.WEST
        );

        JLabel titulo = criarLabel(
                "Meu Perfil",
                26,
                TEXTO,
                true
        );

        titulo.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        cabecalho.add(
                titulo,
                BorderLayout.CENTER
        );

        JButton voltar = criarBotao(
                "Voltar", ROXO
        );

        voltar.addActionListener(e -> dispose());

        cabecalho.add(
                voltar,
                BorderLayout.EAST
        );

        return cabecalho;
    }

    // ======================================
    // FOTO CENTRALIZADA
    // ======================================

    private JPanel criarAreaFoto() {

        JPanel area = new JPanel();

        area.setLayout(
                new BoxLayout(area, BoxLayout.Y_AXIS)
        );

        area.setOpaque(false);
        area.setAlignmentX(Component.CENTER_ALIGNMENT);

        fotoPerfil = new JLabel(
                "Adicionar foto",
                SwingConstants.CENTER
        );

        fotoPerfil.setFont(new Font(
                "Times New Roman",
                Font.BOLD,
                17
        ));

        fotoPerfil.setForeground(SUAVE);
        fotoPerfil.setBackground(ROXO);
        fotoPerfil.setOpaque(true);

        Dimension tamanho = new Dimension(150, 150);

        fotoPerfil.setPreferredSize(tamanho);
        fotoPerfil.setMinimumSize(tamanho);
        fotoPerfil.setMaximumSize(tamanho);

        fotoPerfil.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        fotoPerfil.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        fotoPerfil.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                escolherFoto();
            }
        });

        JLabel legenda = criarLabel(
                "Sua foto de perfil",
                18,
                TEXTO,
                true
        );

        legenda.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JButton escolher = criarBotao(
                "Escolher foto", ROXO
        );

        escolher.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        escolher.setMaximumSize(
                new Dimension(180, 44)
        );

        escolher.addActionListener(e -> escolherFoto());

        area.add(fotoPerfil);
        area.add(Box.createVerticalStrut(10));
        area.add(legenda);
        area.add(Box.createVerticalStrut(12));
        area.add(escolher);

        return area;
    }

    // ======================================
    // FORMULÁRIO
    // ======================================

    private JPanel criarFormulario() {

        JPanel formulario = new JPanel();

        formulario.setLayout(
                new BoxLayout(
                        formulario,
                        BoxLayout.Y_AXIS
                )
        );

        formulario.setOpaque(false);
        formulario.setAlignmentX(Component.LEFT_ALIGNMENT);

        // NOME
        campoNome = new JTextField();
        configurarCampo(campoNome);

        adicionarCampo(
                formulario,
                "Nome",
                campoNome
        );

        // ======================================
        // DATA DE NASCIMENTO
        // ======================================

        JLabel tituloNascimento = criarLabel(
                "Data de nascimento",
                17,
                TEXTO,
                true
        );

        tituloNascimento.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        formulario.add(tituloNascimento);
        formulario.add(Box.createVerticalStrut(8));

        // DIAS
        String[] dias = new String[32];
        dias[0] = "Dia";

        for (int i = 1; i <= 31; i++) {
            dias[i] = String.valueOf(i);
        }

        campoDia = criarCombo(dias);

        // MESES
        campoMes = criarCombo(new String[]{
                "Mês",
                "Janeiro",
                "Fevereiro",
                "Março",
                "Abril",
                "Maio",
                "Junho",
                "Julho",
                "Agosto",
                "Setembro",
                "Outubro",
                "Novembro",
                "Dezembro"
        });

        // ANOS
        int anoAtual = LocalDate.now().getYear();

        String[] anos = new String[
                anoAtual - 1900 + 2
        ];

        anos[0] = "Ano";

        for (int i = 1; i < anos.length; i++) {
            anos[i] = String.valueOf(
                    anoAtual - i + 1
            );
        }

        campoAno = criarCombo(anos);

        JPanel linhaNascimento = new JPanel(
                new GridLayout(1, 3, 10, 0)
        );

        linhaNascimento.setOpaque(false);

        linhaNascimento.add(campoDia);
        linhaNascimento.add(campoMes);
        linhaNascimento.add(campoAno);

        linhaNascimento.setPreferredSize(
                new Dimension(500, 43)
        );

        linhaNascimento.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 43)
        );

        linhaNascimento.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        formulario.add(linhaNascimento);
        formulario.add(Box.createVerticalStrut(7));

        JLabel avisoIdade = criarLabel(
                "O CineMatch é destinado a maiores de 18 anos.",
                15,
                SUAVE,
                false
        );

        avisoIdade.setAlignmentX(Component.LEFT_ALIGNMENT);

        formulario.add(avisoIdade);
        formulario.add(Box.createVerticalStrut(20));

        // ======================================
        // BIOGRAFIA
        // ======================================

        campoBio = new JTextArea(4, 25);

        campoBio.setFont(new Font(
                "Times New Roman",
                Font.PLAIN,
                17
        ));

        campoBio.setBackground(ROXO);
        campoBio.setForeground(TEXTO);
        campoBio.setCaretColor(TEXTO);

        campoBio.setLineWrap(true);
        campoBio.setWrapStyleWord(true);

        campoBio.setBorder(
                new EmptyBorder(12, 12, 12, 12)
        );

        JScrollPane rolagemBio = new JScrollPane(
                campoBio
        );

        rolagemBio.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 110)
        );

        adicionarCampo(
                formulario,
                "Sobre mim",
                rolagemBio
        );

        // O QUE PROCURA
        campoIntencao = criarCombo(new String[]{
                "Amizades",
                "Relacionamento",
                "Amizades e relacionamento"
        });

        adicionarCampo(
                formulario,
                "O que você procura?",
                campoIntencao
        );

        // ======================================
        // PREFERÊNCIAS E CONEXÕES
        // ======================================

        JLabel preferencias = criarLabel(
                "Preferências e conexões",
                21,
                CORAL,
                true
        );

        preferencias.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        formulario.add(preferencias);
        formulario.add(Box.createVerticalStrut(14));

        campoGenero = criarCombo(new String[]{
                "Prefiro não informar",
                "Mulher",
                "Homem",
                "Pessoa não binária",
                "Outra identidade"
        });

        adicionarCampo(
                formulario,
                "Identidade de gênero (opcional)",
                campoGenero
        );

        campoOrientacao = criarCombo(new String[]{
                "Prefiro não informar",
                "Heterossexual",
                "Homossexual",
                "Bissexual",
                "Pansexual",
                "Assexual",
                "Outra"
        });

        adicionarCampo(
                formulario,
                "Orientação sexual (opcional)",
                campoOrientacao
        );

        campoInteresse = criarCombo(new String[]{
                "Sem preferência",
                "Homens",
                "Mulheres",
                "Homens e mulheres",
                "Todos os gêneros",
                "Prefiro não informar"
        });

        adicionarCampo(
                formulario,
                "Tenho interesse em conhecer",
                campoInteresse
        );

        mostrarInformacoes = new JCheckBox(
                "Mostrar essas informações no perfil público"
        );

        mostrarInformacoes.setSelected(false);
        configurarCheck(mostrarInformacoes);

        formulario.add(mostrarInformacoes);
        formulario.add(Box.createVerticalStrut(6));

        JLabel avisoPrivacidade = criarLabel(
                "Essas informações são opcionais e privadas por padrão.",
                15,
                SUAVE,
                false
        );

        avisoPrivacidade.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        formulario.add(avisoPrivacidade);
        formulario.add(Box.createVerticalStrut(24));

        // ======================================
        // TODOS OS 18 GÊNEROS FAVORITOS
        // ======================================

        JLabel tituloGeneros = criarLabel(
                "Meus gêneros favoritos",
                21,
                CORAL,
                true
        );

        tituloGeneros.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        formulario.add(tituloGeneros);
        formulario.add(Box.createVerticalStrut(6));

        JLabel instrucao = criarLabel(
                "Clique na estrela para marcar um gênero favorito.",
                16,
                SUAVE,
                false
        );

        instrucao.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        formulario.add(instrucao);
        formulario.add(Box.createVerticalStrut(12));

        JPanel painelGeneros = new JPanel(
                new GridLayout(0, 2, 10, 8)
        );

        painelGeneros.setOpaque(false);
        painelGeneros.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        for (String genero : GENEROS) {

            JPanel item = new JPanel(
                    new FlowLayout(
                            FlowLayout.LEFT, 3, 0
                    )
            );

            item.setOpaque(false);

            // ESTRELA DESENHADA COM JAVA
            JToggleButton estrela = new JToggleButton() {

                @Override
                protected void paintComponent(Graphics g) {

                    Graphics2D g2 =
                            (Graphics2D) g.create();

                    g2.setRenderingHint(
                            RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON
                    );

                    int centroX = getWidth() / 2;
                    int centroY = getHeight() / 2;

                    int raioExterno = 11;
                    int raioInterno = 5;

                    Polygon formato = new Polygon();

                    for (int i = 0; i < 10; i++) {

                        double angulo =
                                Math.PI * i / 5
                                - Math.PI / 2;

                        int raio = (i % 2 == 0)
                                ? raioExterno
                                : raioInterno;

                        int x = centroX
                                + (int) Math.round(
                                        Math.cos(angulo) * raio
                                );

                        int y = centroY
                                + (int) Math.round(
                                        Math.sin(angulo) * raio
                                );

                        formato.addPoint(x, y);
                    }

                    g2.setStroke(new BasicStroke(2f));

                    if (isSelected()) {

                        g2.setColor(CORAL);
                        g2.fillPolygon(formato);

                    } else {

                        g2.setColor(SUAVE);
                        g2.drawPolygon(formato);
                    }

                    g2.dispose();
                }
            };

            estrela.setPreferredSize(
                    new Dimension(30, 32)
            );

            estrela.setContentAreaFilled(false);
            estrela.setBorderPainted(false);
            estrela.setFocusPainted(false);
            estrela.setOpaque(false);

            estrela.setCursor(
                    Cursor.getPredefinedCursor(
                            Cursor.HAND_CURSOR
                    )
            );

            estrela.setToolTipText(
                    "Favoritar " + genero
            );

            JLabel nomeGenero = criarLabel(
                    genero,
                    17,
                    TEXTO,
                    false
            );

            nomeGenero.setCursor(
                    Cursor.getPredefinedCursor(
                            Cursor.HAND_CURSOR
                    )
            );

            nomeGenero.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {

                    estrela.setSelected(
                            !estrela.isSelected()
                    );

                    estrela.repaint();
                }
            });

            item.add(estrela);
            item.add(nomeGenero);

            nomesGeneros.add(genero);
            estrelasGeneros.add(estrela);

            painelGeneros.add(item);
        }

        formulario.add(painelGeneros);
        formulario.add(Box.createVerticalStrut(22));

        permitirSugestoes = new JCheckBox(
                "Permitir que meu perfil apareça nas sugestões"
        );

        permitirSugestoes.setSelected(true);
        configurarCheck(permitirSugestoes);

        formulario.add(permitirSugestoes);
        formulario.add(Box.createVerticalStrut(8));

        return formulario;
    }

    // ======================================
    // VALIDAÇÃO DA DATA E DOS 18 ANOS
    // ======================================

    private LocalDate obterNascimento() {

        // O índice zero representa o campo não escolhido.
        if (campoDia.getSelectedIndex() == 0
                || campoMes.getSelectedIndex() == 0
                || campoAno.getSelectedIndex() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe sua data de nascimento completa."
            );

            return null;
        }

        try {

            int dia = Integer.parseInt(
                    (String) campoDia.getSelectedItem()
            );

            int mes = campoMes.getSelectedIndex();

            int ano = Integer.parseInt(
                    (String) campoAno.getSelectedItem()
            );

            return LocalDate.of(ano, mes, dia);

        } catch (DateTimeException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Data de nascimento inválida."
            );

            return null;
        }
    }

    private boolean validarIdade(LocalDate nascimento) {

        LocalDate hoje = LocalDate.now();

        if (nascimento.isAfter(hoje)) {

            JOptionPane.showMessageDialog(
                    this,
                    "A data de nascimento não pode ser futura."
            );

            return false;
        }

        int idade = Period.between(
                nascimento,
                hoje
        ).getYears();

        if (idade < 18) {

            JOptionPane.showMessageDialog(
                    this,
                    "O CineMatch é destinado a maiores de 18 anos."
            );

            return false;
        }

        return true;
    }

    // ======================================
    // COMPONENTES DO FORMULÁRIO
    // ======================================

    private JComboBox<String> criarCombo(String[] opcoes) {

        JComboBox<String> combo =
                new JComboBox<>(opcoes);

        combo.setFont(new Font(
                "Times New Roman",
                Font.PLAIN,
                17
        ));

        combo.setBackground(ROXO);
        combo.setForeground(TEXTO);
        combo.setFocusable(false);

        combo.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 44)
        );

        return combo;
    }

    private void configurarCheck(JCheckBox check) {

        check.setOpaque(false);
        check.setForeground(TEXTO);

        check.setFont(new Font(
                "Times New Roman",
                Font.PLAIN,
                16
        ));

        check.setFocusPainted(false);
        check.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );
    }

    private void adicionarCampo(
            JPanel painel,
            String titulo,
            JComponent componente) {

        JLabel label = criarLabel(
                titulo, 17, TEXTO, true
        );

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        componente.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        painel.add(label);
        painel.add(Box.createVerticalStrut(7));
        painel.add(componente);
        painel.add(Box.createVerticalStrut(18));
    }

    private JLabel criarLabel(
            String texto,
            int tamanho,
            Color cor,
            boolean negrito) {

        JLabel label = new JLabel(texto);

        label.setFont(new Font(
                "Times New Roman",
                negrito ? Font.BOLD : Font.PLAIN,
                tamanho
        ));

        label.setForeground(cor);

        return label;
    }

    private JButton criarBotao(
            String texto,
            Color cor) {

        JButton botao = new JButton(texto) {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(
                        getModel().isRollover()
                                ? cor.brighter()
                                : cor
                );

                g2.fillRoundRect(
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        20,
                        20
                );

                g2.dispose();
                super.paintComponent(g);
            }
        };

        botao.setFont(new Font(
                "Times New Roman",
                Font.BOLD,
                17
        ));

        botao.setForeground(Color.WHITE);
        botao.setContentAreaFilled(false);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setOpaque(false);

        botao.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        botao.setBorder(
                new EmptyBorder(12, 18, 12, 18)
        );

        return botao;
    }

    private void configurarCampo(JTextField campo) {

        campo.setFont(new Font(
                "Times New Roman",
                Font.PLAIN,
                17
        ));

        campo.setBackground(ROXO);
        campo.setForeground(TEXTO);
        campo.setCaretColor(TEXTO);

        campo.setBorder(
                new EmptyBorder(10, 12, 10, 12)
        );

        campo.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 44)
        );
    }

    // ======================================
    // FOTO
    // ======================================

    private void escolherFoto() {

        JFileChooser seletor = new JFileChooser();

        seletor.setDialogTitle(
                "Escolha sua foto de perfil"
        );

        seletor.setFileFilter(
                new FileNameExtensionFilter(
                        "Imagens JPG e PNG",
                        "jpg",
                        "jpeg",
                        "png"
                )
        );

        if (seletor.showOpenDialog(this)
                == JFileChooser.APPROVE_OPTION) {

            File escolhida =
                    seletor.getSelectedFile();

            if (exibirFoto(escolhida)) {

                arquivoFoto = escolhida;

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível abrir essa imagem."
                );
            }
        }
    }

    private boolean exibirFoto(File arquivo) {

        if (arquivo == null || !arquivo.isFile()) {
            return false;
        }

        ImageIcon original = new ImageIcon(
                arquivo.getAbsolutePath()
        );

        int larguraOriginal = original.getIconWidth();
        int alturaOriginal = original.getIconHeight();

        if (larguraOriginal <= 0
                || alturaOriginal <= 0) {

            return false;
        }

        double escala = Math.min(
                150.0 / larguraOriginal,
                150.0 / alturaOriginal
        );

        int largura = Math.max(
                1,
                (int) Math.round(
                        larguraOriginal * escala
                )
        );

        int altura = Math.max(
                1,
                (int) Math.round(
                        alturaOriginal * escala
                )
        );

        Image imagem = original.getImage()
                .getScaledInstance(
                        largura,
                        altura,
                        Image.SCALE_SMOOTH
                );

        fotoPerfil.setText("");

        fotoPerfil.setIcon(
                new ImageIcon(imagem)
        );

        return true;
    }

    // ======================================
    // SALVAR PERFIL
    // ======================================

    private void salvarPerfil() {

        String nome = campoNome.getText().trim();

        if (nome.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, informe seu nome."
            );

            return;
        }

        // OBRIGATÓRIO: NASCIMENTO VÁLIDO E 18+
        LocalDate nascimento = obterNascimento();

        if (nascimento == null) {
            return;
        }

        if (!validarIdade(nascimento)) {
            return;
        }

        Properties dados = new Properties();

        dados.setProperty("nome", nome);

        // Agora salva a data de nascimento,
        // e não uma idade fixa.
        dados.setProperty(
                "dataNascimento",
                nascimento.toString()
        );

        dados.setProperty(
                "bio",
                campoBio.getText().trim()
        );

        dados.setProperty(
                "intencao",
                (String) campoIntencao.getSelectedItem()
        );

        dados.setProperty(
                "identidadeGenero",
                (String) campoGenero.getSelectedItem()
        );

        dados.setProperty(
                "orientacaoSexual",
                (String) campoOrientacao.getSelectedItem()
        );

        dados.setProperty(
                "interesseConhecer",
                (String) campoInteresse.getSelectedItem()
        );

        dados.setProperty(
                "mostrarInformacoes",
                String.valueOf(
                        mostrarInformacoes.isSelected()
                )
        );

        dados.setProperty(
                "permitirSugestoes",
                String.valueOf(
                        permitirSugestoes.isSelected()
                )
        );

        // MANTÉM TODOS OS FAVORITOS
        StringBuilder favoritos = new StringBuilder();

        for (int i = 0; i < estrelasGeneros.size(); i++) {

            if (estrelasGeneros.get(i).isSelected()) {

                if (favoritos.length() > 0) {
                    favoritos.append("|");
                }

                favoritos.append(
                        nomesGeneros.get(i)
                );
            }
        }

        dados.setProperty(
                "generosFavoritos",
                favoritos.toString()
        );

        dados.setProperty(
                "foto",
                arquivoFoto == null
                        ? ""
                        : arquivoFoto.getAbsolutePath()
        );

        try (Writer escritor = new OutputStreamWriter(
                new FileOutputStream(ARQUIVO),
                StandardCharsets.UTF_8
        )) {

            dados.store(
                    escritor,
                    "Perfil do CineMatch"
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Perfil salvo com sucesso!"
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível salvar o perfil."
            );
        }
    }

    // ======================================
    // CARREGAR PERFIL
    // ======================================

    private void carregarPerfil() {

        if (!ARQUIVO.isFile()) {
            return;
        }

        Properties dados = new Properties();

        try (Reader leitor = new InputStreamReader(
                new FileInputStream(ARQUIVO),
                StandardCharsets.UTF_8
        )) {

            dados.load(leitor);

            campoNome.setText(
                    dados.getProperty("nome", "")
            );

            campoBio.setText(
                    dados.getProperty("bio", "")
            );

            // DATA DE NASCIMENTO
            String dataSalva = dados.getProperty(
                    "dataNascimento", ""
            );

            if (!dataSalva.isEmpty()) {

                try {

                    LocalDate nascimento =
                            LocalDate.parse(dataSalva);

                    campoDia.setSelectedItem(
                            String.valueOf(
                                    nascimento.getDayOfMonth()
                            )
                    );

                    campoMes.setSelectedIndex(
                            nascimento.getMonthValue()
                    );

                    campoAno.setSelectedItem(
                            String.valueOf(
                                    nascimento.getYear()
                            )
                    );

                } catch (DateTimeException e) {

                    // Mantém os campos vazios
                    // se a data salva for inválida.
                }
            }

            campoIntencao.setSelectedItem(
                    dados.getProperty(
                            "intencao",
                            "Amizades"
                    )
            );

            campoGenero.setSelectedItem(
                    dados.getProperty(
                            "identidadeGenero",
                            "Prefiro não informar"
                    )
            );

            campoOrientacao.setSelectedItem(
                    dados.getProperty(
                            "orientacaoSexual",
                            "Prefiro não informar"
                    )
            );

            campoInteresse.setSelectedItem(
                    dados.getProperty(
                            "interesseConhecer",
                            "Sem preferência"
                    )
            );

            mostrarInformacoes.setSelected(
                    Boolean.parseBoolean(
                            dados.getProperty(
                                    "mostrarInformacoes",
                                    "false"
                            )
                    )
            );

            permitirSugestoes.setSelected(
                    Boolean.parseBoolean(
                            dados.getProperty(
                                    "permitirSugestoes",
                                    "true"
                            )
                    )
            );

            // RECUPERA OS 18 GÊNEROS E SUAS ESTRELAS
            String[] favoritos = dados.getProperty(
                    "generosFavoritos",
                    ""
            ).split("\\|");

            for (int i = 0; i < nomesGeneros.size(); i++) {

                String nomeGenero =
                        nomesGeneros.get(i);

                JToggleButton estrela =
                        estrelasGeneros.get(i);

                boolean favorito = false;

                for (String generoSalvo : favoritos) {

                    if (nomeGenero.equals(generoSalvo)) {
                        favorito = true;
                        break;
                    }
                }

                estrela.setSelected(favorito);
                estrela.repaint();
            }

            String caminhoFoto = dados.getProperty(
                    "foto",
                    ""
            );

            if (!caminhoFoto.isEmpty()) {

                File foto = new File(caminhoFoto);

                if (exibirFoto(foto)) {
                    arquivoFoto = foto;
                }
            }

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível carregar o perfil salvo."
            );
        }
    }
}
