
import java.awt.*;
import javax.swing.*;

public class TelaLogin extends JFrame {

    private final Color FUNDO = new Color(18, 18, 30);
    private final Color ROSA = new Color(255, 75, 100);

    private JTextField campoEmail;
    private JPasswordField campoSenha;

    public TelaLogin() {

        setTitle("CineMatch - Login");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painel = new JPanel();
        painel.setBackground(FUNDO);
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(
            BorderFactory.createEmptyBorder(45, 65, 45, 65)
        );

        JLabel titulo = new JLabel("CineMatch");
        titulo.setFont(new Font("Arial", Font.BOLD, 38));
        titulo.setForeground(ROSA);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Entre no universo do cinema");
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        campoEmail = new JTextField();
        campoSenha = new JPasswordField();

        configurarCampo(campoEmail);
        configurarCampo(campoSenha);

        JButton entrar = criarBotao("Entrar", ROSA);
        JButton cadastrar = criarBotao(
            "Criar Conta", new Color(65, 65, 80)
        );

        painel.add(titulo);
        painel.add(Box.createVerticalStrut(12));
        painel.add(subtitulo);
        painel.add(Box.createVerticalStrut(45));

        painel.add(criarLabel("E-mail"));
        painel.add(Box.createVerticalStrut(8));
        painel.add(campoEmail);
        painel.add(Box.createVerticalStrut(20));

        painel.add(criarLabel("Senha"));
        painel.add(Box.createVerticalStrut(8));
        
        painel.add(campoSenha);
        painel.add(Box.createVerticalStrut(8));
        painel.add(Box.createVerticalStrut(20));


        painel.add(entrar);
        painel.add(Box.createVerticalStrut(12));
        painel.add(cadastrar);

        entrar.addActionListener(e -> fazerLogin());

        cadastrar.addActionListener(e -> criarConta());

        add(painel);
        setVisible(true);
    }

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private void configurarCampo(JTextField campo) {
        campo.setMaximumSize(new Dimension(350, 40));
        campo.setFont(new Font("Arial", Font.PLAIN, 16));
        campo.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 16));
        botao.setFocusPainted(false);
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setMaximumSize(new Dimension(350, 45));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return botao;
    }

    private void fazerLogin() {

        String email = campoEmail.getText().trim();
        char[] caracteres = campoSenha.getPassword();
        String senha = new String(caracteres);
        java.util.Arrays.fill(caracteres, '\0');

        Usuario usuario = GerenciadorUsuarios.autenticar(
            email, senha
        );

        if (usuario != null) {

            JOptionPane.showMessageDialog(
                this,
                "Bem-vindo(a), " + usuario.getNome() + "!"
            );

            new TelaInicial();
            dispose();

        } else {

            JOptionPane.showMessageDialog(
                this,
                "E-mail ou senha incorretos!"
            );
        }
    }

    private void criarConta() {

        JTextField nome = new JTextField();
        JTextField email = new JTextField();
        JPasswordField senha = new JPasswordField();

        Object[] campos = {
            "Nome:", nome,
            "E-mail:", email,
            "Senha:", senha
        };

        int opcao = JOptionPane.showConfirmDialog(
            this,
            campos,
            "Criar Conta - CineMatch",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (opcao == JOptionPane.OK_OPTION) {

            char[] caracteres = senha.getPassword();
            String senhaDigitada = new String(caracteres);
            java.util.Arrays.fill(caracteres, '\0');

            boolean sucesso = GerenciadorUsuarios.cadastrar(
                nome.getText().trim(),
                email.getText().trim(),
                senhaDigitada
            );

            if (sucesso) {

                JOptionPane.showMessageDialog(
                    this,
                    "Conta criada com sucesso!"
                );

                campoEmail.setText(email.getText().trim());
                campoSenha.setText("");
                
JCheckBox mostrarSenha = new JCheckBox("👁 Mostrar senha");

mostrarSenha.setForeground(Color.WHITE);
mostrarSenha.setBackground(FUNDO);
mostrarSenha.setFocusPainted(false);
mostrarSenha.setAlignmentX(Component.CENTER_ALIGNMENT);

char caractereOriginal = campoSenha.getEchoChar();

mostrarSenha.addActionListener(e -> {
if (mostrarSenha.isSelected()) {
    campoSenha.setEchoChar((char) 0);
    mostrarSenha.setText("🙈 Ocultar senha");
    } else {
    campoSenha.setEchoChar(caractereOriginal);
    mostrarSenha.setText("👁 Mostrar senha");
    }
});


            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Preencha os campos ou utilize outro e-mail."
                );
            }
        }
    }
}
