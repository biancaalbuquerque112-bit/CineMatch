import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/** Perfil público de outra pessoa, sem mostrar dados privados ou e-mail. */
public class TelaPerfilPessoa extends JFrame {
    private final Color FUNDO=new Color(43,22,70), CARD=new Color(56,32,82);
    private final Color CORAL=new Color(255,98,92), ROXO=new Color(65,37,93);
    private final Color TEXTO=new Color(247,243,250), SUAVE=new Color(220,204,232);

    public TelaPerfilPessoa(Usuario pessoa, Filme comum, AlgoritmoMatch.Resultado resultado) {
        setTitle("CineMatch - Perfil de " + pessoa.getNome());
        setSize(720,740);setMinimumSize(new Dimension(580,550));
        setLocationRelativeTo(null);setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel fundo=new JPanel(new BorderLayout(12,12));
        fundo.setBackground(FUNDO);fundo.setBorder(new EmptyBorder(20,30,20,30));

        JPanel topo=new JPanel(new BorderLayout());topo.setOpaque(false);
        JLabel marca=new JLabel("CineMatch");marca.setForeground(TEXTO);
        marca.setFont(new Font("Nimbus Sans",Font.BOLD,30));topo.add(marca,BorderLayout.WEST);
        JButton voltar=botao("Fechar",ROXO);voltar.addActionListener(e->dispose());
        topo.add(voltar,BorderLayout.EAST);fundo.add(topo,BorderLayout.NORTH);

        JPanel coluna=new JPanel();coluna.setOpaque(false);
        coluna.setLayout(new BoxLayout(coluna,BoxLayout.Y_AXIS));
        JLabel foto=new JLabel("●",SwingConstants.CENTER);
        foto.setForeground(CORAL);foto.setFont(new Font("SansSerif",Font.PLAIN,85));
        foto.setPreferredSize(new Dimension(140,140));foto.setMaximumSize(new Dimension(140,140));
        foto.setAlignmentX(Component.CENTER_ALIGNMENT);
        if(pessoa.getFoto()!=null&&!pessoa.getFoto().isBlank()){
            File arquivo=new File(pessoa.getFoto());
            if(arquivo.isFile()){
                ImageIcon icone=new ImageIcon(arquivo.getAbsolutePath());
                if(icone.getIconWidth()>0){
                    double k=Math.min(140.0/icone.getIconWidth(),140.0/icone.getIconHeight());
                    Image imagem=icone.getImage().getScaledInstance(
                        (int)(icone.getIconWidth()*k),(int)(icone.getIconHeight()*k),Image.SCALE_SMOOTH);
                    foto.setText("");foto.setIcon(new ImageIcon(imagem));
                }
            }
        }
        coluna.add(foto);coluna.add(Box.createVerticalStrut(12));
        JLabel nome=texto(pessoa.getNome()+(pessoa.getIdade()>0?", "+pessoa.getIdade():""),26,TEXTO,true);
        nome.setAlignmentX(Component.CENTER_ALIGNMENT);
        coluna.add(nome);coluna.add(Box.createVerticalStrut(12));
        if(resultado!=null){
            JLabel compat=texto(resultado.percentual+"% de afinidade • "+
                resultado.filmesEmComum+" filmes em comum",20,CORAL,true);
            compat.setAlignmentX(Component.CENTER_ALIGNMENT);
            coluna.add(compat);coluna.add(Box.createVerticalStrut(12));
        }
        secao(coluna,"O que procura",pessoa.getIntencao());
        secao(coluna,"Sobre mim",pessoa.getBio()==null||pessoa.getBio().isBlank()?
            "Esta pessoa ainda não escreveu uma apresentação.":pessoa.getBio());
        secao(coluna,"Gêneros favoritos",pessoa.getGenerosFavoritos()==null||
            pessoa.getGenerosFavoritos().isBlank()?"Nenhum gênero marcado.":pessoa.getGenerosFavoritos().replace("|",", "));
        if(comum!=null)secao(coluna,"Filme em comum",comum.getTitulo());
        if(pessoa.isMostrarInformacoes()){
            secao(coluna,"Identidade de gênero",pessoa.getIdentidadeGenero());
            secao(coluna,"Orientação (informação pública autorizada)",pessoa.getOrientacao());
            secao(coluna,"Interesse em conhecer",pessoa.getInteresse());
        }
        JScrollPane rolagem=new JScrollPane(coluna,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rolagem.getViewport().setBackground(FUNDO);
        rolagem.setBorder(null);rolagem.getVerticalScrollBar().setUnitIncrement(18);
        fundo.add(rolagem,BorderLayout.CENTER);

        JButton conectar=botao("Solicitar conexão",CORAL);
        MatchDAO match=new MatchDAO();
        Usuario eu=Sessao.getUsuario();
        if(eu==null||pessoa.getId()<=0||pessoa.getId()==eu.getId()){
            conectar.setEnabled(false);
        }else{
            String status=match.statusEntre(eu.getId(),pessoa.getId());
            if(status!=null){conectar.setText("Conexão: "+status);conectar.setEnabled(false);}
        }
        conectar.addActionListener(e->{
            if(!Sessao.ativa())return;
            if(match.solicitar(Sessao.getUsuario().getId(),pessoa.getId())){
                JOptionPane.showMessageDialog(this,"Solicitação enviada para "+pessoa.getNome()+"!");
                conectar.setText("Solicitação enviada");conectar.setEnabled(false);
            }else JOptionPane.showMessageDialog(this,
                "Não foi possível enviar: confira se o filme está em comum ou se já existe uma solicitação.");
        });
        fundo.add(conectar,BorderLayout.SOUTH);
        setContentPane(fundo);setVisible(true);
    }
    /** Compatibilidade com as assinaturas antigas da interface. */
    public TelaPerfilPessoa(String nome,String intencao,String comentario,String filme){
        this(perfilDemonstrativo(nome,intencao,comentario),null,null);
    }
    public TelaPerfilPessoa(String nome,int idade,String intencao,String generos,String comentario,Filme filme){
        this(perfilDemonstrativo(nome,intencao,comentario),filme,null);
    }
    private static Usuario perfilDemonstrativo(String nome,String intencao,String bio){
        Usuario u=new Usuario();u.setNome(nome);u.setIntencao(intencao);u.setBio(bio);return u;
    }
    private void secao(JPanel coluna,String titulo,String valor){
        JLabel label=texto(titulo,19,CORAL,true);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        coluna.add(label);coluna.add(Box.createVerticalStrut(6));
        JTextArea campo=new JTextArea(valor==null?"Não informado":valor);
        campo.setFont(new Font("Times New Roman",Font.PLAIN,18));
        campo.setForeground(SUAVE);campo.setBackground(CARD);
        campo.setBorder(new EmptyBorder(13,13,13,13));
        campo.setEditable(false);campo.setLineWrap(true);campo.setWrapStyleWord(true);
        campo.setFocusable(false);campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setRows(Math.max(2,Math.min(5,1+campo.getText().length()/55)));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE,campo.getPreferredSize().height));
        coluna.add(campo);coluna.add(Box.createVerticalStrut(15));
    }
    private JLabel texto(String s,int tam,Color c,boolean bold){
        JLabel l=new JLabel(s);l.setForeground(c);
        l.setFont(new Font("Times New Roman",bold?Font.BOLD:Font.PLAIN,tam));return l;
    }
    private JButton botao(String texto,Color cor){
        JButton b=new JButton(texto);b.setFont(new Font("Times New Roman",Font.BOLD,17));
        b.setBackground(cor);b.setForeground(Color.WHITE);b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(12,18,12,18));return b;
    }
}
