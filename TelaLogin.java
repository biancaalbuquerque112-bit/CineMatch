import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.AbstractBorder;

public class TelaLogin extends JFrame {

    // Cores da identidade visual do CineMatch
    private final Color FUNDO = new Color(43, 22, 70);
    private final Color CORAL = new Color(255, 98, 92);
    private final Color ROXO_BOTAO = new Color(65, 37, 93);
    private final Color BRANCO = new Color(247, 243, 250);

    // Fontes do CineMatch
    private final Font FONTE_LOGO =
            new Font("Nimbus Sans", Font.BOLD, 38);

    private final Font FONTE_TEXTO =
            new Font("Modern No. 20", Font.PLAIN, 18);

    private final Font FONTE_LABEL =
            new Font("Modern No. 20", Font.BOLD, 18);

    private final Font FONTE_BOTAO =
            new Font("Modern No. 20", Font.BOLD, 20);

    private final Font FONTE_CAMPO =
            new Font("Modern No. 20", Font.PLAIN, 20);

    private final Font FONTE_TITULO =
            new Font("Modern No. 20", Font.BOLD, 30);

    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private UsuarioDAO usuarioDAO;

    public TelaLogin() {

        // Instancia a camada de persistência com o Banco H2
        this.usuarioDAO = new UsuarioDAO();

        setTitle("CineMatch - Login");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painel = new JPanel();
        painel.setBackground(FUNDO);
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(
                BorderFactory.createEmptyBorder(35, 65, 35, 65)
        );

        // Logo original do CineMatch
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

        // Nome CineMatch em Nimbus Sans
        JLabel nomeAplicativo = new JLabel("CineMatch");
        nomeAplicativo.setFont(FONTE_LOGO);
        nomeAplicativo.setForeground(BRANCO);

        JPanel cabecalho = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 0)
        );

        cabecalho.setOpaque(false);
        cabecalho.add(icone);
        cabecalho.add(nomeAplicativo);
        cabecalho.setAlignmentX(Component.CENTER_ALIGNMENT);
        cabecalho.setMaximumSize(new Dimension(370, 68));

        JLabel subtitulo = new JLabel(
                "Entre no universo do cinema"
        );

        subtitulo.setFont(FONTE_TEXTO);
        subtitulo.setForeground(BRANCO);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campos do login
        campoEmail = new JTextField();
        campoSenha = new JPasswordField();

        configurarCampo(campoEmail);
        configurarCampo(campoSenha);

        // Mostrar e ocultar senha
        JCheckBox mostrarSenha = new JCheckBox("Mostrar senha");
        mostrarSenha.setFont(FONTE_TEXTO);
        mostrarSenha.setForeground(BRANCO);
        mostrarSenha.setBackground(FUNDO);
        mostrarSenha.setFocusPainted(false);
        mostrarSenha.setAlignmentX(Component.CENTER_ALIGNMENT);

        char caractereOriginal = campoSenha.getEchoChar();

        mostrarSenha.addActionListener(e -> {
            if (mostrarSenha.isSelected()) {
                campoSenha.setEchoChar((char) 0);
                mostrarSenha.setText("Ocultar senha");
            } else {
                campoSenha.setEchoChar(caractereOriginal);
                mostrarSenha.setText("Mostrar senha");
            }
        });

        JButton entrar = criarBotao("Entrar", CORAL);
        JButton cadastrar = criarBotao("Criar Conta", ROXO_BOTAO);

        // Organização visual
        painel.add(cabecalho);
        painel.add(Box.createVerticalStrut(10));
        painel.add(subtitulo);
        painel.add(Box.createVerticalStrut(35));

        painel.add(criarLabel("E-mail"));
        painel.add(Box.createVerticalStrut(8));
        painel.add(campoEmail);
        painel.add(Box.createVerticalStrut(15));

        painel.add(criarLabel("Senha"));
        painel.add(Box.createVerticalStrut(8));
        painel.add(campoSenha);
        painel.add(Box.createVerticalStrut(8));

        painel.add(mostrarSenha);
        painel.add(Box.createVerticalStrut(20));

        painel.add(entrar);
        painel.add(Box.createVerticalStrut(10));
        painel.add(cadastrar);

        entrar.addActionListener(e -> fazerLogin());
        cadastrar.addActionListener(e -> criarConta());

        add(painel);
        setVisible(true);
    }

    // Cria as legendas dos campos
    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FONTE_LABEL);
        label.setForeground(BRANCO);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    // Personaliza os campos de texto
    private void configurarCampo(JTextField campo) {
        campo.setMaximumSize(new Dimension(350, 40));
        campo.setFont(FONTE_CAMPO);
        campo.setAlignmentX(Component.CENTER_ALIGNMENT);
        campo.setBorder(new BordaArredondada());
        campo.setBackground(Color.WHITE);
        campo.setForeground(new Color(35, 18, 60));
    }

    // Cria os botões com cantos arredondados
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
                        0, 0, getWidth(), getHeight(), 18, 18
                );

                desenho.dispose();
                super.paintComponent(g);
            }
        };

        botao.setContentAreaFilled(false);
        botao.setBorderPainted(false);
        botao.setOpaque(false);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(FONTE_BOTAO);
        botao.setFocusPainted(false);
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setMaximumSize(new Dimension(350, 45));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efeito hover
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

    // Borda arredondada dos campos
    private static class BordaArredondada extends AbstractBorder {

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(8, 12, 8, 12);
        }

        @Override
        public void paintBorder(
                Component c,
                Graphics g,
                int x,
                int y,
                int width,
                int height
        ) {
            Graphics2D desenho = (Graphics2D) g.create();

            desenho.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            desenho.setColor(new Color(190, 168, 210));
            desenho.drawRoundRect(
                    x, y, width - 1, height - 1, 14, 14
            );

            desenho.dispose();
        }
    }

    // Autenticação do usuário consultando o banco H2 via UsuarioDAO
    private void fazerLogin() {

        String email = campoEmail.getText().trim();

        char[] caracteres = campoSenha.getPassword();
        String senha = new String(caracteres);

        java.util.Arrays.fill(caracteres, '\0');

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, preencha o e-mail e a senha."
            );
            return;
        }

        // Consulta o banco H2
        Usuario usuario = usuarioDAO.autenticar(email, senha);

        if (usuario != null) {

            // Abre a tela inicial com o nome do usuário autenticado no banco
            new TelaInicial(usuario.getNome());

            dispose();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "E-mail ou senha incorretos!"
            );
        }
    }

    // Tela personalizada para criar conta e salvar no banco H2
    private void criarConta() {

        JDialog janela = new JDialog(
                this, "Criar Conta - CineMatch", true
        );

        janela.setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE
        );

        janela.setSize(440, 485);
        janela.setResizable(false);
        janela.setLocationRelativeTo(this);

        JPanel painel = new JPanel();
        painel.setBackground(FUNDO);
        painel.setLayout(
                new BoxLayout(painel, BoxLayout.Y_AXIS)
        );

        painel.setBorder(
                BorderFactory.createEmptyBorder(25, 40, 25, 40)
        );

        JLabel titulo = new JLabel("Criar conta");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(BRANCO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descricao = new JLabel(
                "Seu próximo filme começa aqui."
        );

        descricao.setFont(FONTE_TEXTO);
        descricao.setForeground(BRANCO);
        descricao.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField nome = new JTextField();
        JTextField email = new JTextField();
        JPasswordField senha = new JPasswordField();

        configurarCampo(nome);
        configurarCampo(email);
        configurarCampo(senha);

        JButton confirmar = criarBotao("Criar conta", CORAL);
        JButton cancelar = criarBotao("Cancelar", ROXO_BOTAO);

        painel.add(titulo);
        painel.add(Box.createVerticalStrut(8));
        painel.add(descricao);
        painel.add(Box.createVerticalStrut(23));

        painel.add(criarLabel("Nome"));
        painel.add(Box.createVerticalStrut(5));
        painel.add(nome);
        painel.add(Box.createVerticalStrut(13));

        painel.add(criarLabel("E-mail"));
        painel.add(Box.createVerticalStrut(5));
        painel.add(email);
        painel.add(Box.createVerticalStrut(13));

        painel.add(criarLabel("Senha"));
        painel.add(Box.createVerticalStrut(5));
        painel.add(senha);
        painel.add(Box.createVerticalStrut(22));

        painel.add(confirmar);
        painel.add(Box.createVerticalStrut(10));
        painel.add(cancelar);

        confirmar.addActionListener(e -> {

            String nomeStr = nome.getText().trim();
            String emailStr = email.getText().trim();

            char[] caracteres = senha.getPassword();
            String senhaDigitada = new String(caracteres);
            java.util.Arrays.fill(caracteres, '\0');

            if (nomeStr.isEmpty() || emailStr.isEmpty() || senhaDigitada.isEmpty()) {
                JOptionPane.showMessageDialog(
                        janela,
                        "Preencha todos os campos!"
                );
                return;
            }

            // Cria o objeto modelo Usuario
            Usuario novoUsuario = new Usuario(nomeStr, emailStr, senhaDigitada);

            // Persiste no banco de dados H2 via UsuarioDAO
            boolean sucesso = usuarioDAO.cadastrar(novoUsuario);

            if (sucesso) {
                JOptionPane.showMessageDialog(
                        janela,
                        "Conta criada com sucesso no banco de dados!"
                );

                campoEmail.setText(emailStr);
                campoSenha.setText("");
                janela.dispose();

            } else {
                JOptionPane.showMessageDialog(
                        janela,
                        "Erro ao cadastrar. Verifique se o e-mail já está em uso."
                );
            }
        });

        cancelar.addActionListener(e -> janela.dispose());

        janela.add(painel);
        janela.setVisible(true);
    }
}