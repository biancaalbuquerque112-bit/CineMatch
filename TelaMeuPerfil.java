
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TelaMeuPerfil extends JFrame {

    private final Color FUNDO = new Color(18, 18, 30);
    private final Color CARD = new Color(32, 32, 48);
    private final Color CAMPO = new Color(45, 45, 65);
    private final Color ROSA = new Color(255, 75, 100);
    private final Color TEXTO = new Color(235, 235, 245);

    private JTextField campoNome;
    private JSpinner campoIdade;
    private JTextArea campoBio;
    private JComboBox<String> campoIntencao;
    private JCheckBox permitirSugestoes;

    private JLabel fotoPerfil;
    private File arquivoFoto;

    private final ArrayList<JCheckBox> generos =
        new ArrayList<>();

    public TelaMeuPerfil() {

        setTitle("CineMatch - Meu Perfil");
        setSize(780, 850);
        setMinimumSize(new Dimension(650, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout(15, 15));
        principal.setBackground(FUNDO);
        principal.setBorder(new EmptyBorder(25, 35, 25, 35));

        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(FUNDO);

        JLabel logo = criarLabel("CineMatch", 32, ROSA);
        JLabel titulo = criarLabel("Meu Perfil", 23, Color.WHITE);

        JPanel titulos = new JPanel();
        titulos.setBackground(FUNDO);
        titulos.setLayout(new BoxLayout(titulos, BoxLayout.Y_AXIS));

        titulos.add(logo);
        titulos.add(Box.createVerticalStrut(8));
        titulos.add(titulo);

        topo.add(titulos, BorderLayout.WEST);
        principal.add(topo, BorderLayout.NORTH);

        JPanel formulario = new JPanel();
        formulario.setBackground(CARD);
        formulario.setLayout(
            new BoxLayout(formulario, BoxLayout.Y_AXIS)
        );
        formulario.setBorder(
            new EmptyBorder(25, 35, 25, 35)
        );

        fotoPerfil = new JLabel("Adicionar foto");
        fotoPerfil.setHorizontalAlignment(SwingConstants.CENTER);
        fotoPerfil.setVerticalAlignment(SwingConstants.CENTER);
        fotoPerfil.setForeground(ROSA);
        fotoPerfil.setFont(new Font("Arial", Font.BOLD, 15));
        fotoPerfil.setOpaque(true);
        fotoPerfil.setBackground(CAMPO);
        fotoPerfil.setPreferredSize(new Dimension(140, 140));
        fotoPerfil.setMinimumSize(new Dimension(140, 140));
        fotoPerfil.setMaximumSize(new Dimension(140, 140));
        fotoPerfil.setAlignmentX(Component.CENTER_ALIGNMENT);
        fotoPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));

        fotoPerfil.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                escolherFoto();
            }
        });

        JButton escolherFoto = criarBotao(
            "Escolher foto", new Color(70, 70, 95)
        );
        escolherFoto.setAlignmentX(Component.CENTER_ALIGNMENT);
        escolherFoto.setMaximumSize(
            new Dimension(190, 42)
        );
        escolherFoto.addActionListener(e -> escolherFoto());

        formulario.add(fotoPerfil);
        formulario.add(Box.createVerticalStrut(12));
        formulario.add(escolherFoto);
        formulario.add(Box.createVerticalStrut(25));

        campoNome = new JTextField();
        configurarCampo(campoNome);

        adicionarCampo(formulario, "Nome", campoNome);

        campoIdade = new JSpinner(
            new SpinnerNumberModel(18, 18, 100, 1)
        );
        configurarSpinner(campoIdade);

        adicionarCampo(formulario, "Idade", campoIdade);

        campoBio = new JTextArea(4, 25);
        campoBio.setLineWrap(true);
        campoBio.setWrapStyleWord(true);
        campoBio.setFont(new Font("Arial", Font.PLAIN, 15));
        campoBio.setBackground(CAMPO);
        campoBio.setForeground(TEXTO);
        campoBio.setCaretColor(Color.WHITE);
        campoBio.setBorder(new EmptyBorder(12, 12, 12, 12));

        JScrollPane rolagemBio = new JScrollPane(campoBio);
        rolagemBio.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 115)
        );
        rolagemBio.setBorder(null);

        adicionarCampo(formulario, "Sobre mim", rolagemBio);

        campoIntencao = new JComboBox<>(new String[]{
            "Amizades",
            "Relacionamento",
            "Amizades e relacionamento"
        });

        campoIntencao.setBackground(CAMPO);
        campoIntencao.setForeground(TEXTO);
        campoIntencao.setFont(
            new Font("Arial", Font.PLAIN, 15)
        );
        campoIntencao.setFocusable(false);
        campoIntencao.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 42)
        );

        adicionarCampo(
            formulario, "O que você procura?", campoIntencao
        );

        JLabel tituloGeneros = criarLabel(
            "Meus gêneros favoritos", 16, Color.WHITE
        );
        tituloGeneros.setAlignmentX(Component.LEFT_ALIGNMENT);

        formulario.add(tituloGeneros);
        formulario.add(Box.createVerticalStrut(12));

        JPanel painelGeneros = new JPanel(
            new GridLayout(0, 2, 12, 12)
        );
        painelGeneros.setBackground(CARD);
        painelGeneros.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] opcoes = {
            "Romance",
            "Comédia",
            "Terror",
            "Ação",
            "Ficção científica",
            "Suspense",
            "Drama",
            "Animação",
            "Aventura",
            "Fantasia"
        };

        for (String genero : opcoes) {

            JCheckBox check = new JCheckBox(genero);
            check.setBackground(CARD);
            check.setForeground(TEXTO);
            check.setFont(
                new Font("Arial", Font.PLAIN, 14)
            );
            check.setFocusPainted(false);

            generos.add(check);
            painelGeneros.add(check);
        }

        formulario.add(painelGeneros);
        formulario.add(Box.createVerticalStrut(25));

        permitirSugestoes = new JCheckBox(
            "Permitir que meu perfil apareça nas sugestões"
        );
        permitirSugestoes.setSelected(true);
        permitirSugestoes.setBackground(CARD);
        permitirSugestoes.setForeground(TEXTO);
        permitirSugestoes.setFocusPainted(false);
        permitirSugestoes.setAlignmentX(Component.LEFT_ALIGNMENT);

        formulario.add(permitirSugestoes);

        JScrollPane rolagem = new JScrollPane(formulario);
        rolagem.setBorder(null);
        rolagem.getVerticalScrollBar().setUnitIncrement(16);

        principal.add(rolagem, BorderLayout.CENTER);

        JPanel botoes = new JPanel(
            new GridLayout(1, 2, 15, 0)
        );
        botoes.setBackground(FUNDO);

        JButton voltar = criarBotao(
            "← Voltar", new Color(65, 65, 85)
        );
        JButton salvar = criarBotao(
            "♥ Salvar meu perfil", ROSA
        );

        voltar.addActionListener(e -> dispose());
        salvar.addActionListener(e -> salvarPerfil());

        botoes.add(voltar);
        botoes.add(salvar);

        principal.add(botoes, BorderLayout.SOUTH);

        setContentPane(principal);
        setVisible(true);
    }

    private JLabel criarLabel(
            String valor, int tamanho, Color cor) {

        JLabel label = new JLabel(valor);
        label.setFont(
            new Font("Arial", Font.BOLD, tamanho)
        );
        label.setForeground(cor);
        return label;
    }

    private JButton criarBotao(String texto, Color cor) {

        JButton botao = new JButton(texto);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(
            new Font("Arial", Font.BOLD, 15)
        );
        botao.setFocusPainted(false);
        botao.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );
        botao.setBorder(
            new EmptyBorder(12, 15, 12, 15)
        );
        return botao;
    }

    private void configurarCampo(JTextField campo) {

        campo.setFont(
            new Font("Arial", Font.PLAIN, 15)
        );
        campo.setBackground(CAMPO);
        campo.setForeground(TEXTO);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(
            new EmptyBorder(10, 12, 10, 12)
        );
        campo.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 42)
        );
    }

    private void configurarSpinner(JSpinner spinner) {

        spinner.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 42)
        );

        JComponent editor = spinner.getEditor();

        if (editor instanceof JSpinner.DefaultEditor) {

            JTextField campo =
                ((JSpinner.DefaultEditor) editor).getTextField();

            configurarCampo(campo);
            campo.setHorizontalAlignment(
                JTextField.LEFT
            );
        }

        spinner.setBackground(CAMPO);
        spinner.setBorder(null);
    }

    private void adicionarCampo(
            JPanel painel, String nome, JComponent componente) {

        JLabel label = criarLabel(
            nome, 15, Color.WHITE
        );
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        componente.setAlignmentX(Component.LEFT_ALIGNMENT);

        painel.add(label);
        painel.add(Box.createVerticalStrut(8));
        painel.add(componente);
        painel.add(Box.createVerticalStrut(20));
    }

    private void escolherFoto() {

        JFileChooser seletor = new JFileChooser();
        seletor.setDialogTitle("Escolha sua foto de perfil");

        seletor.setFileFilter(
            new javax.swing.filechooser.FileNameExtensionFilter(
                "Imagens JPG e PNG", "jpg", "jpeg", "png"
            )
        );

        int resultado = seletor.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {

            arquivoFoto = seletor.getSelectedFile();

            ImageIcon imagem = new ImageIcon(
                arquivoFoto.getAbsolutePath()
            );

            if (imagem.getIconWidth() <= 0) {
                mostrarMensagem(
                    "Não foi possível abrir essa imagem.",
                    false
                );
                return;
            }

            Image redimensionada = imagem.getImage()
                .getScaledInstance(
                    140, 140, Image.SCALE_SMOOTH
                );

            fotoPerfil.setText("");
            fotoPerfil.setIcon(
                new ImageIcon(redimensionada)
            );
        }
    }

    private void salvarPerfil() {

        String nome = campoNome.getText().trim();
        int idade = (int) campoIdade.getValue();
        String bio = campoBio.getText().trim();
        String intencao =
            (String) campoIntencao.getSelectedItem();

        if (nome.isEmpty()) {
            mostrarMensagem(
                "Por favor, informe seu nome.", false
            );
            return;
        }

        ArrayList<String> favoritos = new ArrayList<>();

        for (JCheckBox check : generos) {
            if (check.isSelected()) {
                favoritos.add(check.getText());
            }
        }

        System.out.println("=== MEU PERFIL CINEMATCH ===");
        System.out.println("Nome: " + nome);
        System.out.println("Idade: " + idade);
        System.out.println("Biografia: " + bio);
        System.out.println("Intenção: " + intencao);
        System.out.println("Gêneros: " + favoritos);
        System.out.println(
            "Sugestões: " + permitirSugestoes.isSelected()
        );
        System.out.println(
            "Foto: " + (
                arquivoFoto != null
                ? arquivoFoto.getAbsolutePath()
                : "Não selecionada"
            )
        );

        mostrarMensagem(
            "Perfil preenchido com sucesso!\n"
            + "Na próxima etapa vamos salvar os dados "
            + "no banco de dados.",
            true
        );
    }

    private void mostrarMensagem(
            String mensagem, boolean sucesso) {

        JDialog dialogo = new JDialog(
            this, "CineMatch", true
        );
        dialogo.setSize(430, 210);
        dialogo.setLocationRelativeTo(this);
        dialogo.setUndecorated(true);

        JPanel painel = new JPanel(new BorderLayout(15, 15));
        painel.setBackground(CARD);
        painel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel titulo = criarLabel(
            sucesso ? "Tudo certo! ♥" : "Atenção",
            22, ROSA
        );

        JTextArea texto = new JTextArea(mensagem);
        texto.setEditable(false);
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);
        texto.setBackground(CARD);
        texto.setForeground(Color.WHITE);
        texto.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton fechar = criarBotao("Entendi", ROSA);
        fechar.addActionListener(e -> dialogo.dispose());

        painel.add(titulo, BorderLayout.NORTH);
        painel.add(texto, BorderLayout.CENTER);
        painel.add(fechar, BorderLayout.SOUTH);

        dialogo.setContentPane(painel);
        dialogo.setVisible(true);
    }
}
