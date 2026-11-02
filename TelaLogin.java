import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.AbstractBorder;

public class TelaLogin extends JFrame {

    // Cores baseadas na identidade visual do CineMatch.
    private final Color FUNDO = new Color(43, 22, 70);
    private final Color CORAL = new Color(255, 98, 92);
    private final Color ROXO_BOTAO = new Color(65, 37, 93);
    private final Color BRANCO = new Color(247, 243, 250);

    private JTextField campoEmail;
    private JPasswordField campoSenha;

    public TelaLogin() {
        setTitle("CineMatch - Login");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painel = new JPanel();
        painel.setBackground(FUNDO);
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createEmptyBorder(35, 65, 35, 65));

        // Ícone original: ingresso coral com coração branco.
        JLabel icone = new JLabel();
        ImageIcon imagemIcone = new ImageIcon("imagens/icone_cinematch.png");
        if (imagemIcone.getIconWidth() > 0) {
            Image imagemAjustada = imagemIcone.getImage()
                    .getScaledInstance(86, 61, Image.SCALE_SMOOTH);
            icone.setIcon(new ImageIcon(imagemAjustada));
        }

        // Nome em texto claro para contrastar com o fundo roxo.
        JLabel nomeAplicativo = new JLabel("CineMatch");
        nomeAplicativo.setFont(new Font("Arial", Font.BOLD, 38));
        nomeAplicativo.setForeground(BRANCO);

        JPanel cabecalho = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        cabecalho.setOpaque(false);
        cabecalho.add(icone);
        cabecalho.add(nomeAplicativo);
        cabecalho.setAlignmentX(Component.CENTER_ALIGNMENT);
        cabecalho.setMaximumSize(new Dimension(370, 68));

        JLabel subtitulo = new JLabel("Entre no universo do cinema");
        subtitulo.setForeground(BRANCO);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        campoEmail = new JTextField();
        campoSenha = new JPasswordField();
        configurarCampo(campoEmail);
        configurarCampo(campoSenha);

        JCheckBox mostrarSenha = new JCheckBox("Mostrar senha");
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

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(BRANCO);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private void configurarCampo(JTextField campo) {
        campo.setMaximumSize(new Dimension(350, 40));
        campo.setFont(new Font("Arial", Font.PLAIN, 16));
        campo.setAlignmentX(Component.CENTER_ALIGNMENT);
        campo.setBorder(new BordaArredondada());
        campo.setBackground(Color.WHITE);
        campo.setForeground(new Color(35, 18, 60));
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D desenho = (Graphics2D) g.create();
                desenho.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                desenho.setColor(getBackground());
                desenho.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                desenho.dispose();
                super.paintComponent(g);
            }
        };
        botao.setContentAreaFilled(false);
        botao.setBorderPainted(false);
        botao.setOpaque(false);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 16));
        botao.setFocusPainted(false);
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setMaximumSize(new Dimension(350, 45));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efeito hover simples (sem animação ou bibliotecas externas).
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

    // Classe gráfica pequena, apenas para desenhar o contorno arredondado.
    private static class BordaArredondada extends AbstractBorder {
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(8, 12, 8, 12);
        }

        @Override
        public void paintBorder(Component c, Graphics g,
                                int x, int y, int width, int height) {
            Graphics2D desenho = (Graphics2D) g.create();
            desenho.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            desenho.setColor(new Color(190, 168, 210));
            desenho.drawRoundRect(x, y, width - 1, height - 1, 14, 14);
            desenho.dispose();
        }
    }

    private void fazerLogin() {
        String email = campoEmail.getText().trim();
        char[] caracteres = campoSenha.getPassword();
        String senha = new String(caracteres);
        java.util.Arrays.fill(caracteres, '\0');

        Usuario usuario = GerenciadorUsuarios.autenticar(email, senha);

        if (usuario != null) {
            // Mantido por enquanto. Vamos mudar a saudação na próxima etapa.
            JOptionPane.showMessageDialog(
                    this, "Bem-vindo(a), " + usuario.getNome() + "!");
            new TelaInicial();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "E-mail ou senha incorretos!");
        }
    }

    private void criarConta() {
        // A lógica original do cadastro foi preservada nesta primeira etapa.
        JTextField nome = new JTextField();
        JTextField email = new JTextField();
        JPasswordField senha = new JPasswordField();

        Object[] campos = {
                "Nome:", nome,
                "E-mail:", email,
                "Senha:", senha
        };

        int opcao = JOptionPane.showConfirmDialog(
                this, campos, "Criar Conta - CineMatch",
                JOptionPane.OK_CANCEL_OPTION);

        if (opcao == JOptionPane.OK_OPTION) {
            char[] caracteres = senha.getPassword();
            String senhaDigitada = new String(caracteres);
            java.util.Arrays.fill(caracteres, '\0');

            boolean sucesso = GerenciadorUsuarios.cadastrar(
                    nome.getText().trim(),
                    email.getText().trim(),
                    senhaDigitada);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Conta criada com sucesso!");
                campoEmail.setText(email.getText().trim());
                campoSenha.setText("");
            } else {
                JOptionPane.showMessageDialog(
                        this, "Preencha os campos ou utilize outro e-mail.");
            }
        }
    }
}
