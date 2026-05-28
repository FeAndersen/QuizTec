package Aluno;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import util.Conexao;
import util.Sessao;

public class EscolherJogoAluno extends JFrame {

    private Font robotoBold36, robotoBold24, robotoBold18, robotoBold14;
    static class Jogo {
        public int id;
        public String nome;
        public String dataCriacao;

        public Jogo(int id, String nome, String dataCriacao) {
            this.id = id;
            this.nome = nome;
            this.dataCriacao = dataCriacao;
        }
    }

    public EscolherJogoAluno(String nivel) {
       carregarFontes();

       String nomeDificuldadeDB;
       String nomeIcone;
       String nomeTitulo;

       switch (nivel) {
        case "Médio": nomeDificuldadeDB = "MEDIO"; nomeIcone = "images\\biotech.png"; nomeTitulo = "Jogos de funções"; break;
        case "Difícil": nomeDificuldadeDB = "DIFICIL"; nomeIcone = "images\\fluid_med.png"; nomeTitulo = "Jogos de sistemas"; break;
        default: nomeDificuldadeDB = "FACIL"; nomeIcone = "images\\labs.png"; nomeTitulo = "Jogos de identificação"; break;
       }

       java.util.List<Jogo> jogos = new java.util.ArrayList<>();

       try (Connection con = Conexao.conectar()) {
        PreparedStatement stmt = con.prepareStatement(
            "SELECT s.id_sessao, s.nome_sessao, s.data_criacao " +
            "FROM sessao s " +
            "JOIN dificuldade d USING(id_dificuldade) " +
            "JOIN turma t USING(id_professor) " +
            "JOIN aluno a USING(id_turma) " +
            "WHERE a.id_aluno = ? AND d.nome_dificuldade = ? " +
            "ORDER BY s.data_criacao DESC"
        );

        stmt.setInt(1, Sessao.idUsuario);
        stmt.setString(2, nomeDificuldadeDB);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            jogos.add(new Jogo(
                rs.getInt("id_sessao"),
                rs.getString("nome_sessao"),
                rs.getDate("data_criacao").toString()
            ));
        }
       } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Erro ao carregar jogos: " + ex.getMessage());
       }


       setUndecorated(true);
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    Toolkit tk = Toolkit.getDefaultToolkit();
    int larguraTela = (int) tk.getScreenSize().getWidth();
    int alturaTela = (int) tk.getScreenSize().getHeight();

    JPanel painelFundo = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(new ImageIcon("images\\fundo_etec.jpg").getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    };
    painelFundo.setLayout(null);
    setContentPane(painelFundo);

    JPanel header = new JPanel();
    header.setBackground(new Color(178, 0, 0));
    header.setBounds(0, 0, larguraTela, 80);
    header.setLayout(null);

    header.add(criarBotaoControle("X", larguraTela - 50, 0));
    header.add(criarBotaoControle("-", larguraTela - 100, 0));
    header.add(criarBotaoControle("↰", 0, 0));

    JLabel txtQuizTec = new JLabel("QuizTec");
    txtQuizTec.setForeground(Color.WHITE);
    txtQuizTec.setFont(robotoBold36);
    txtQuizTec.setBounds(110, 0, 200, 80);
    header.add(txtQuizTec);

    JLabel txtOla = new JLabel("Olá, " + Sessao.nomeUsuario, SwingConstants.RIGHT);
    txtOla.setForeground(Color.WHITE);
    txtOla.setFont(robotoBold24);
    txtOla.setBounds(larguraTela - 450, 0, 300, 80);
    header.add(txtOla);

    int larguraCard = (int) (larguraTela * 0.85);
    int alturaCard = (int) (alturaTela * 0.85);
    int xCard = (larguraTela - larguraCard) / 2;
    int yCard = 100;

    JPanel cardPrincipal = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(178, 0, 0));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 80, 80);
        }
    };
    cardPrincipal.setLayout(null);
    cardPrincipal.setOpaque(false);
    cardPrincipal.setBounds(xCard, yCard, larguraCard, alturaCard);

    JLabel lblTitulo = new JLabel(nomeTitulo, SwingConstants.CENTER);
    lblTitulo.setForeground(Color.WHITE);
    lblTitulo.setFont(robotoBold36);
    lblTitulo.setBounds(0, 40, larguraCard, 40);
    cardPrincipal.add(lblTitulo);

    JLabel lblSubtitulo = new JLabel("Você tem acesso a " + jogos.size() + " jogos de nível " + nivel.toLowerCase(), SwingConstants.CENTER);
    lblSubtitulo.setForeground(Color.WHITE);
    lblSubtitulo.setFont(robotoBold24);
    lblSubtitulo.setBounds(0, 85, larguraCard, 30);
    cardPrincipal.add(lblSubtitulo);

    JPanel linha = new JPanel() {
        @Override protected void paintComponent(Graphics g) {
            g.setColor(Color.WHITE); g.fillRect(0, 0, getWidth(), getHeight());
        }
    };
    linha.setBounds((larguraCard - 800)/2, 130, 800, 3);
    linha.setOpaque(false);
    cardPrincipal.add(linha);

    int wLista = (int) (larguraCard * 0.85);
    int hLista = alturaCard - 180;
    int xLista = (larguraCard - wLista) / 2;

    JPanel painelJogos = new JPanel();
    painelJogos.setLayout(null);
    painelJogos.setOpaque(false);
    painelJogos.setBackground(new Color(0,0,0,0));

    int yJogo = 10;
    int hJogo = 80;
    int espacoJogo = 15;

    for (Jogo jogo : jogos) {
        painelJogos.add(criarPainelJogoAluno(jogo, nomeIcone, 0, yJogo, wLista, hJogo));
        yJogo += hJogo + espacoJogo;
    }

    painelJogos.setPreferredSize(new Dimension(wLista, yJogo));

    JScrollPane scrollPane = new JScrollPane(painelJogos);
    scrollPane.setBounds(xLista, 150, wLista, hLista);
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);
    scrollPane.setBorder(null);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0,0));

    cardPrincipal.add(scrollPane);
    painelFundo.add(header);
    painelFundo.add(cardPrincipal);

    setVisible(true);

    }

    private JPanel criarPainelJogoAluno(Jogo jogo, String iconePath, int x, int y, int w, int h) {
        Color corAzulEscuro = new Color(30, 55, 90);

        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                try {
                    g2.drawImage(new ImageIcon(iconePath).getImage(), 20, 10, 60, 60, null);
                } catch (Exception e) {}
            }
        };
        p.setLayout(null);
        p.setBounds(x, y, w, h);
        p.setOpaque(false);

        JLabel lblNome = new JLabel(jogo.nome);
        lblNome.setFont(robotoBold24);
        lblNome.setForeground(corAzulEscuro);
        lblNome.setBounds(100, 15, w - 300, 30);
        p.add(lblNome);

        JLabel lblData = new JLabel("Criado em: " + jogo.dataCriacao);
        lblData.setFont(robotoBold14);
        lblData.setForeground(corAzulEscuro);
        lblData.setBounds(100, 45, w - 300, 20);
        p.add(lblData);

        JButton btnJogar = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                g2.setColor(Color.WHITE);
                g2.setFont(robotoBold18);
                g2.drawString("Jogar", 30, 32);

                int cx = getWidth() - 35;
                int cy = getHeight() / 2;
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(cx - 12, cy - 12, 24, 24);
                
                Path2D play = new Path2D.Double();
                play.moveTo(cx - 4, cy - 6);
                play.lineTo(cx + 6, cy);
                play.lineTo(cx - 4, cy + 6);
                play.closePath();
                g2.fill(play);
                g2.dispose();
            }
        };
        btnJogar.setBounds(w - 180, 15, 150, 50);
        btnJogar.setBackground(corAzulEscuro);
        btnJogar.setContentAreaFilled(false);
        btnJogar.setBorderPainted(false);
        btnJogar.setFocusPainted(false);
        btnJogar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnJogar.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btnJogar.setBackground(new Color(50, 75, 110)); btnJogar.repaint(); }
            @Override public void mouseExited(MouseEvent e) { btnJogar.setBackground(corAzulEscuro); btnJogar.repaint(); }
        });
        
        btnJogar.addActionListener(e -> {
            Sessao.idSessao = jogo.id;
            dispose();
            new JogarQuiz().setVisible(true);
        });

        p.add(btnJogar);
        return p;
    }

    private JButton criarBotaoControle(String texto, int x, int y) {
        Color corInvisivel = new Color(0, 0, 0, 0); 
        JButton b = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(getBackground()); g.fillRect(0, 0, getWidth(), getHeight()); super.paintComponent(g);
            }
        };
        if (texto.equals("↰")) {
            b.setFont(new Font("Segoe UI Symbol", Font.BOLD, 48));
            b.setBounds(x, 0, 60, 60);
            b.addActionListener(e -> { 
                this.dispose(); 
                new SelecaoNivel().setVisible(true); // Volta passando o nome
            });
        } else {
            b.setFont(new Font("Arial", Font.BOLD, 24));
            b.setBounds(x, y, 50, 40); 
            if (texto.equals("X")) b.addActionListener(e -> System.exit(0));
            else if (texto.equals("-")) b.addActionListener(e -> setState(Frame.ICONIFIED));
        }
        b.setMargin(new Insets(0,0,0,0)); b.setBackground(corInvisivel); b.setForeground(Color.WHITE);
        b.setFocusable(false); b.setBorder(null); b.setContentAreaFilled(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(texto.equals("X") ? new Color(232, 17, 35) : new Color(100,100,100)); }
            @Override public void mouseExited(MouseEvent e) { b.setBackground(corInvisivel); }
        });
        return b;
    }

    private void carregarFontes() {
        try {
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, new File("RobotoSerif-Bold.ttf"));
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(baseFont);
            robotoBold36 = baseFont.deriveFont(Font.BOLD, 36f);
            robotoBold24 = baseFont.deriveFont(Font.BOLD, 24f);
            robotoBold18 = baseFont.deriveFont(Font.BOLD, 18f);
            robotoBold14 = baseFont.deriveFont(Font.BOLD, 14f);
        } catch (Exception e) {
            robotoBold36 = new Font("Arial", Font.BOLD, 36);
            robotoBold24 = new Font("Arial", Font.BOLD, 24);
            robotoBold18 = new Font("Arial", Font.BOLD, 18);
            robotoBold14 = new Font("Arial", Font.BOLD, 14);
        }
    }

    public static void main(String[] args) { 
        SwingUtilities.invokeLater(() -> new EscolherJogoAluno("Aluno Teste")); 
    }
}