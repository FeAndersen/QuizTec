package Professor;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import util.Conexao;
import util.Sessao;

public class SeusJogosCriados extends JFrame {

    private Font robotoBold36, robotoBold24, robotoBold14;

    // ==========================================
    // MOCK DATABASE (Banco de Dados Simulado)
    // ==========================================
    public static class Jogo {
        public String nome;
        public String nivel;
        public String dataCriacao;
        public int quantidadePerguntas;

        public Jogo(String nome, String nivel, String dataCriacao, int qtd) {
            this.nome = nome;
            this.nivel = nivel;
            this.dataCriacao = dataCriacao;
            this.quantidadePerguntas = qtd;
        }
    }
    
    

    public SeusJogosCriados() {
        carregarFontes();
        List<Jogo> jogos = new ArrayList<>();
        try (Connection con = Conexao.conectar()) {
            PreparedStatement stmt = con.prepareStatement(
                "SELECT s.nome_sessao, d.nome_dificuldade, s.data_criacao, s.quantidade_perguntas " +
                "FROM sessao s " +
                "JOIN dificuldade d ON s.id_dificuldade = d.id_dificuldade " +
                "WHERE s.id_professor = ? " +
                "ORDER BY s.data_criacao DESC"
            );
            
            stmt.setInt(1, Sessao.idUsuario);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                jogos.add(new Jogo(
                    rs.getString("nome_sessao"),
                    rs.getString("nome_dificuldade"),
                    rs.getDate("data_criacao").toString(),
                    rs.getInt("quantidade_perguntas")
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
                ImageIcon imagemFundo = new ImageIcon("images\\fundo_etec.jpg");
                g.drawImage(imagemFundo.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        painelFundo.setLayout(null);
        setContentPane(painelFundo);

        // Header (Barra Vermelha Superior)
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

        // Card Central Vermelho
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

        // Títulos e Subtítulos
        JLabel lblTitulo = new JLabel("Seus Jogos Criados", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(robotoBold36);
        lblTitulo.setBounds(0, 40, larguraCard, 40);
        cardPrincipal.add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("Você tem " + jogos.size() + " jogos ativos no momento", SwingConstants.CENTER);
        lblSubtitulo.setForeground(Color.WHITE);
        lblSubtitulo.setFont(robotoBold24);
        lblSubtitulo.setBounds(0, 85, larguraCard, 30);
        cardPrincipal.add(lblSubtitulo);

        // Linha divisória branca
        JPanel linha = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        linha.setBounds((larguraCard - 800)/2, 130, 800, 3);
        linha.setOpaque(false);
        cardPrincipal.add(linha);

        // ==========================================
        // LISTA COM SCROLL (ROLAGEM INFINITA)
        // ==========================================
        int wLista = (int) (larguraCard * 0.85);
        int hLista = alturaCard - 180;
        int xLista = (larguraCard - wLista) / 2;

        JPanel painelJogos = new JPanel();
        painelJogos.setLayout(null); // Posicionamento absoluto dentro do painel
        painelJogos.setOpaque(false);
        painelJogos.setBackground(new Color(0,0,0,0));

        int yJogo = 10;
        int hJogo = 90;
        int espacoJogo = 15;

        // Loop que puxa os dados e cria os cards brancos
        for (Jogo jogo : jogos) {
            painelJogos.add(criarPainelJogo(jogo, 0, yJogo, wLista, hJogo));
            yJogo += hJogo + espacoJogo;
        }

        // Ajusta o tamanho do painel interno para o Scroll funcionar
        painelJogos.setPreferredSize(new Dimension(wLista, yJogo));

        JScrollPane scrollPane = new JScrollPane(painelJogos);
        scrollPane.setBounds(xLista, 150, wLista, hLista);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Rola mais rápido
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0,0)); // Esconde a barra feia
        
        cardPrincipal.add(scrollPane);

        painelFundo.add(header);
        painelFundo.add(cardPrincipal);
        
        setVisible(true);
    }

    private JPanel criarPainelJogo(Jogo jogo, int x, int y, int w, int h) {
        Color corAzulEscuro = new Color(30, 55, 90);

        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fundo Branco Arredondado do Jogo
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                // 1. ÍCONE DE DIFICULDADE (Sinalizado em AZUL)
                // Lógica dinâmica para ler a String e puxar o PNG certo
                try {
                    String imgPath = "images\\labs.png"; // Padrão Fácil
                    if (jogo.nivel.contains("Médio")) imgPath = "images\\biotech.png";
                    else if (jogo.nivel.contains("Difícil")) imgPath = "images\\fluid_med.png";
                    
                    ImageIcon icon = new ImageIcon(imgPath);
                    Image img = icon.getImage();
                    g2.drawImage(img, 20, 15, 60, 60, null);
                } catch (Exception e) {}
            }
        };
        p.setLayout(null);
        p.setBounds(x, y, w, h);
        p.setOpaque(false);

        // 2. NOME DO JOGO (Sinalizado em VERDE)
        JLabel lblNome = new JLabel(jogo.nome);
        lblNome.setFont(robotoBold24);
        lblNome.setForeground(corAzulEscuro);
        lblNome.setBounds(100, 20, w - 400, 30);
        p.add(lblNome);

        // 3. DATA DE CRIAÇÃO (Sinalizado em AMARELO)
        JLabel lblData = new JLabel("Criado em " + jogo.dataCriacao);
        lblData.setFont(robotoBold14);
        lblData.setForeground(corAzulEscuro);
        lblData.setBounds(100, 50, w - 400, 20);
        p.add(lblData);

        // 4. BOTÕES DE AÇÃO (Sinalizados em BRANCO)
        // Agora passando o caminho exato dos PNGs que você me enviou!
        int btnW = 80;
        int btnH = 70;
        int startXBtns = w - (btnW * 3) - 20;

        p.add(criarBotaoAcao("Renomear", "images\\rename.png", startXBtns, 10, btnW, btnH));
        p.add(criarBotaoAcao("Editar", "images\\edit.png", startXBtns + btnW, 10, btnW, btnH));
        p.add(criarBotaoAcao("Apagar", "images\\delete.png", startXBtns + (btnW * 2), 10, btnW, btnH));

        return p;
    }

    private JButton criarBotaoAcao(String texto, String caminhoIcone, int x, int y, int w, int h) {
        Color corAzulEscuro = new Color(30, 55, 90);
        Color corHover = new Color(240, 240, 240);

        JButton b = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fundo do botão (com efeito hover)
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // ==========================================
                // LÓGICA DE RENDERIZAÇÃO DO ÍCONE PNG
                // ==========================================
                try {
                    ImageIcon icon = new ImageIcon(caminhoIcone);
                    Image img = icon.getImage();
                    
                    // Centraliza e redimensiona o ícone para caber no botão (ex: 30x30)
                    int iconDim = 30;
                    int iconX = (getWidth() - iconDim) / 2;
                    int iconY = (getHeight() - iconDim) / 2 - 10; // Levanta um pouco para o texto
                    
                    g2.drawImage(img, iconX, iconY, iconDim, iconDim, null);
                } catch (Exception e) {
                    System.err.println("Erro ao carregar ícone: " + caminhoIcone);
                }

                // Desenha o texto da ação (ex: Renomear, Editar, Apagar)
                g2.setColor(corAzulEscuro);
                g2.setFont(robotoBold14);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(texto, (getWidth() - fm.stringWidth(texto)) / 2, getHeight() - 10);

                g2.dispose();
            }
        };

        b.setBounds(x, y, w, h);
        b.setBackground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(corHover); b.repaint(); }
            @Override public void mouseExited(MouseEvent e) { b.setBackground(Color.WHITE); b.repaint(); }
        });

        // Exemplo de ação para os botões do Histórico
        b.addActionListener(e -> {
            if (texto.equals("Apagar")) {
                System.out.println("Lógica para apagar o jogo da lista...");
            }
        });

        return b;
    }

    private JButton criarBotaoControle(String texto, int x, int y) {
        Color corInvisivel = new Color(0, 0, 0, 0); 
        Color corHover = texto.equals("X") ? new Color(232, 17, 35) : new Color(100, 100, 100);

        JButton b = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };

        if (texto.equals("↰")) {
            b.setFont(new Font("Segoe UI Symbol", Font.BOLD, 48));
            b.setBounds(x, 0, 60, 60); 
            // Volta para o Menu Principal do Professor
            b.addActionListener(e -> { this.dispose(); new MenuProf().setVisible(true); });
        } else {
            b.setFont(new Font("Arial", Font.BOLD, 24));
            b.setBounds(x, y, 50, 40); 
            if (texto.equals("X")) b.addActionListener(e -> System.exit(0));
            else if (texto.equals("-")) b.addActionListener(e -> setState(Frame.ICONIFIED));
        }

        b.setMargin(new Insets(0, 0, 0, 0)); 
        b.setBackground(corInvisivel); 
        b.setForeground(Color.WHITE);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false); // Desliga o desenho do foco ao clicar
        b.setFocusable(false);    // Impede que o botão receba foco pelo teclado
        b.setBorder(null);        // Garante que não sobrou nenhuma borda do Windows
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(corHover); b.repaint(); }
            @Override public void mouseExited(MouseEvent e) { b.setBackground(corInvisivel); b.repaint(); }
        });

        return b;
    }

    private void carregarFontes() {
        try {
            File fontFile = new File("RobotoSerif-Bold.ttf");
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(baseFont);
            robotoBold36 = baseFont.deriveFont(Font.BOLD, 36f);
            robotoBold24 = baseFont.deriveFont(Font.BOLD, 24f);
            robotoBold14 = baseFont.deriveFont(Font.BOLD, 14f);
        } catch (Exception e) {
            robotoBold36 = new Font("Arial", Font.BOLD, 36);
            robotoBold24 = new Font("Arial", Font.BOLD, 24);
            robotoBold14 = new Font("Arial", Font.BOLD, 14);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SeusJogosCriados());
    }
}