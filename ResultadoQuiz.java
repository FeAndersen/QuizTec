import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class ResultadoQuiz extends JFrame {

    private Font robotoBold36, robotoBold24, robotoBold20;

    public ResultadoQuiz(int acertos, int total) {
        carregarFontes();
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
                // Usando a regra do caminho da imagem
                Image imgFundo = new ImageIcon("QuizTec\\images\\fundo_etec.jpg").getImage();
                g.drawImage(imgFundo, 0, 0, getWidth(), getHeight(), this);
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

        JLabel txtOla = new JLabel("Olá, Aluno", SwingConstants.RIGHT);
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

        // ==========================================
        // CÁLCULO PARA CENTRALIZAR TROFÉU + TÍTULO
        // ==========================================
        int trophyW = 80;
        int gap = 20; // Espaço entre o troféu e o texto
        
        // A MÁGICA AQUI: O Java calcula a largura exata da frase com essa fonte!
        Font fonteDoTitulo = robotoBold36.deriveFont(48f);
        FontMetrics fmTitulo = cardPrincipal.getFontMetrics(fonteDoTitulo);
        int titleW = fmTitulo.stringWidth("Sessão Finalizada!") + 10; // +10 de margem de segurança
        
        // Largura total do "bloco" (Troféu + Espaço + Texto)
        int groupW = trophyW + gap + titleW;
        
        // Ponto X inicial para que o bloco todo fique perfeitamente no meio do card vermelho
        int startXGroup = (larguraCard - groupW) / 2;

        JLabel lblTrofeu = new JLabel();
        try {
            Image imgTrofeu = new ImageIcon("QuizTec\\images\\trophy.png").getImage().getScaledInstance(trophyW, trophyW, Image.SCALE_SMOOTH);
            lblTrofeu.setIcon(new ImageIcon(imgTrofeu));
        } catch (Exception e) {
            System.out.println("Erro ao carregar trophy.png");
        }
        lblTrofeu.setBounds(startXGroup, 60, trophyW, trophyW);
        cardPrincipal.add(lblTrofeu);

        JLabel lblTitulo = new JLabel("Sessão Finalizada!");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(fonteDoTitulo); // Usando a fonte que criamos ali em cima
        lblTitulo.setHorizontalAlignment(SwingConstants.LEFT); 
        lblTitulo.setBounds(startXGroup + trophyW + gap, 70, titleW, 60);
        cardPrincipal.add(lblTitulo);

        // Subtítulo centralizado normalmente
        JLabel lblSubtitulo = new JLabel("Você acertou " + acertos + " de " + total + " perguntas!", SwingConstants.CENTER);
        lblSubtitulo.setForeground(Color.WHITE);
        lblSubtitulo.setFont(robotoBold24);
        lblSubtitulo.setBounds(0, 160, larguraCard, 30);
        cardPrincipal.add(lblSubtitulo);

        // ==========================================
        // BOTÕES BRANCOS (MAIORES E CENTRALIZADOS)
        // ==========================================
        int wBtn = 280; // Aumentado (antes era 240)
        int hBtn = 280; // Aumentado
        int espacoBtn = 80; // Um pouco mais de respiro entre eles
        int startX = (larguraCard - (wBtn * 2 + espacoBtn)) / 2;
        int startY = 250; // Descidos um pouco para centralizar melhor no espaço vazio

        // Agora o botão 1 puxa a imagem replay.png perfeitamente!
        cardPrincipal.add(criarBotaoAcaoGrande("Jogar", "novamente", "QuizTec\\images\\replay.png", true, startX, startY, wBtn, hBtn));
        cardPrincipal.add(criarBotaoAcaoGrande("Voltar para o", "menu", "QuizTec\\images\\menu.png", false, startX + wBtn + espacoBtn, startY, wBtn, hBtn));

        painelFundo.add(header);
        painelFundo.add(cardPrincipal);

        setVisible(true);
    }

    private JButton criarBotaoAcaoGrande(String linha1, String linha2, String imgPath, boolean isRefresh, int x, int y, int w, int h) {
        Color corAzulEscuro = new Color(30, 55, 90);
        JButton b = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35); // Bordas um pouco mais arredondadas
                
                int cx = getWidth() / 2;
                int cy = getHeight() / 2 - 30; // Levanta o ícone um pouco para caber os textos
                
                // Desenha a imagem PNG (agora serve tanto pro replay quanto pro menu)
                if (imgPath != null) {
                    try {
                        Image img = new ImageIcon(imgPath).getImage();
                        // Ícones levemente maiores (90x90)
                        g2.drawImage(img, cx - 45, cy - 45, 90, 90, null);
                    } catch (Exception e) {
                        System.out.println("Erro ao carregar: " + imgPath);
                    }
                }

                g2.setColor(corAzulEscuro);
                g2.setFont(robotoBold24);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(linha1, (getWidth() - fm.stringWidth(linha1)) / 2, getHeight() - 75);
                g2.drawString(linha2, (getWidth() - fm.stringWidth(linha2)) / 2, getHeight() - 40);

                g2.dispose();
            }
        };

        b.setBounds(x, y, w, h);
        b.setBackground(Color.WHITE);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(new Color(240, 240, 240)); b.repaint(); }
            @Override public void mouseExited(MouseEvent e) { b.setBackground(Color.WHITE); b.repaint(); }
        });

        b.addActionListener(e -> {
            this.dispose();
            if (isRefresh) {
                new JogarQuiz().setVisible(true); // Reinicia o jogo
            } else {
                new MenuAluno().setVisible(true); // Volta pro menu
            }
        });

        return b;
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
            b.setBounds(x, 0, 60, 60); // REGRA APLICADA: 60x60 cravado
            b.addActionListener(e -> { this.dispose(); new MenuAluno().setVisible(true); });
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
        b.setFocusable(false); 
        b.setBorder(null);     
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(texto.equals("X") ? new Color(232, 17, 35) : new Color(100, 100, 100)); }
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
            robotoBold20 = baseFont.deriveFont(Font.BOLD, 20f);
        } catch (Exception e) {
            robotoBold36 = new Font("Arial", Font.BOLD, 36);
            robotoBold24 = new Font("Arial", Font.BOLD, 24);
            robotoBold20 = new Font("Arial", Font.BOLD, 20);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ResultadoQuiz(8, 10)); 
    }
}