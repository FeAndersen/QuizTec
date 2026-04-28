package Aluno;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class JogarQuiz extends JFrame {

    private Font robotoBold32, robotoBold24, robotoBold20, robotoBold28;
    private JLabel txtContador;
    private int questaoAtual = 1;
    private int totalQuestoes = 10;
    
    // Variáveis para a animação de Certo/Errado
    private boolean mostrarFeedback = false;
    private boolean acertou = false;

    public JogarQuiz() {
        carregarFontes();
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit tk = Toolkit.getDefaultToolkit();
        int larguraTela = (int) tk.getScreenSize().getWidth();
        int alturaTela = (int) tk.getScreenSize().getHeight();

        JPanel painelFundo = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(new ImageIcon("QuizTec\\images\\fundo_etec.jpg").getImage(), 0, 0, getWidth(), getHeight(), this);
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
        txtQuizTec.setFont(robotoBold32);
        txtQuizTec.setBounds(110, 0, 200, 80);
        header.add(txtQuizTec);

        JLabel txtOla = new JLabel("Olá, nome", SwingConstants.RIGHT);
        txtOla.setForeground(Color.WHITE);
        txtOla.setFont(robotoBold24);
        txtOla.setBounds(larguraTela - 450, 0, 300, 80);
        header.add(txtOla);

        int larguraCard = (int) (larguraTela * 0.85);
        int alturaCard = (int) (alturaTela * 0.85);
        int xCard = (larguraTela - larguraCard) / 2;
        int yCard = 100;

        JPanel cardPrincipal = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(178, 0, 0));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 80, 80);
            }
        };
        cardPrincipal.setLayout(null);
        cardPrincipal.setOpaque(false);
        cardPrincipal.setBounds(xCard, yCard, larguraCard, alturaCard);

        txtContador = new JLabel("1/10");
        txtContador.setForeground(Color.WHITE);
        txtContador.setFont(robotoBold28);
        txtContador.setBounds(40, 30, 100, 40);
        cardPrincipal.add(txtContador);

        // Ícone de Ajuda (Help)
        JButton btnAjuda = new JButton();
        try {
            Image img = new ImageIcon("QuizTec\\images\\help.png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            btnAjuda.setIcon(new ImageIcon(img));
        } catch (Exception e) {}
        btnAjuda.setBounds(larguraCard - 90, 30, 60, 60);
        btnAjuda.setContentAreaFilled(false);
        btnAjuda.setBorderPainted(false);
        btnAjuda.setFocusPainted(false);
        btnAjuda.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cardPrincipal.add(btnAjuda);

        int wImg = 550, hImg = 260, wOpcao = 450, hOpcao = 60;
        int espacoX = 50, espacoY = 25, hPergunta = 50;
        int margemAbaixoImg = 30, margemAbaixoPergunta = 30;

        int alturaTotalConteudo = hImg + margemAbaixoImg + hPergunta + margemAbaixoPergunta + (hOpcao * 2) + espacoY;
        int startYConteudo = (alturaCard - alturaTotalConteudo) / 2;
        int xImg = (larguraCard - wImg) / 2;
        int yImg = startYConteudo; 

        // Área da Foto Estática (No jogo real você carrega a imagem da pergunta aqui)
        JPanel painelFoto = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(200, 200, 200)); 
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.BLACK);
                g.setFont(robotoBold24);
                FontMetrics fm = g.getFontMetrics();
                g.drawString("Exemplo foto", (getWidth() - fm.stringWidth("Exemplo foto")) / 2, getHeight() / 2);
            }
        };
        painelFoto.setBounds(xImg, yImg, wImg, hImg);
        cardPrincipal.add(painelFoto);

        // Pergunta
        int yPergunta = yImg + hImg + margemAbaixoImg;
        JLabel lblPergunta = new JLabel("Exemplo pergunta??", SwingConstants.CENTER);
        lblPergunta.setBounds(xImg - 100, yPergunta, wImg + 200, hPergunta);
        lblPergunta.setFont(robotoBold28);
        lblPergunta.setForeground(Color.WHITE);
        cardPrincipal.add(lblPergunta);

        // Alternativas
        int startXOpcoes = (larguraCard - (wOpcao * 2 + espacoX)) / 2;
        int startYOpcoes = yPergunta + hPergunta + margemAbaixoPergunta;

        // Simulando que a resposta certa é a 'A' para testar
        cardPrincipal.add(criarBotaoAlternativa("A", "béquer", startXOpcoes, startYOpcoes, wOpcao, hOpcao, true));
        cardPrincipal.add(criarBotaoAlternativa("B", "béquer", startXOpcoes + wOpcao + espacoX, startYOpcoes, wOpcao, hOpcao, false));
        cardPrincipal.add(criarBotaoAlternativa("C", "béquer", startXOpcoes, startYOpcoes + hOpcao + espacoY, wOpcao, hOpcao, false));
        cardPrincipal.add(criarBotaoAlternativa("D", "béquer", startXOpcoes + wOpcao + espacoX, startYOpcoes + hOpcao + espacoY, wOpcao, hOpcao, false));

        painelFundo.add(header);
        painelFundo.add(cardPrincipal);

        configurarGlassPaneFeedback(); // Prepara o painel de animação

        setVisible(true);
    }

    // ==========================================
    // LÓGICA DO FEEDBACK (CERTO / ERRADO)
    // ==========================================
    private void configurarGlassPaneFeedback() {
        JPanel glassPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                if (!mostrarFeedback) return;
                
                // Escurece a tela 
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());

                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(35f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); // Traço bem grosso
                
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                int size = 100; // Tamanho do símbolo

                if (acertou) {
                    // Desenha o V de Certo (Verde)
                    g2.setColor(new Color(30, 180, 80)); 
                    g2.drawLine(cx - size, cy, cx - (size/3), cy + size);
                    g2.drawLine(cx - (size/3), cy + size, cx + size, cy - size);
                } else {
                    // Desenha o X de Errado (Vermelho Claro)
                    g2.setColor(new Color(255, 50, 50));
                    g2.drawLine(cx - size, cy - size, cx + size, cy + size);
                    g2.drawLine(cx + size, cy - size, cx - size, cy + size);
                }
            }
        };
        glassPane.setOpaque(false);
        setGlassPane(glassPane);
    }

    private void processarResposta(boolean isCorreta) {
        acertou = isCorreta;
        mostrarFeedback = true;
        getGlassPane().setVisible(true);
        getGlassPane().repaint();

        // Pausa a tela por 1.5 segundos para o aluno ver se acertou
        Timer timer = new Timer(1500, e -> {
            mostrarFeedback = false;
            getGlassPane().setVisible(false);
            
            // Avança questão ou finaliza
            if (questaoAtual < totalQuestoes) {
                questaoAtual++;
                txtContador.setText(questaoAtual + "/" + totalQuestoes);
                // Aqui você carregaria a próxima pergunta do BD
            } else {
                // Fim do Quiz - Vai para os Resultados
                System.out.println("Quiz Finalizado!");
                // this.dispose();
                // new ResultadoQuiz().setVisible(true); 
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    // ==========================================
    // CRIAÇÃO DA UI
    // ==========================================
    private JButton criarBotaoAlternativa(String letra, String texto, int x, int y, int w, int h, boolean isCorreta) {
        Color corAzulEscuro = new Color(30, 55, 90);
        Color corHover = new Color(50, 75, 110);

        JButton b = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Letra (A, B, C, D)
                g2.setColor(Color.WHITE);
                g2.setFont(robotoBold24);
                g2.drawString(letra, 30, 38);
                
                // Texto da Opção
                g2.setFont(robotoBold20);
                g2.drawString(texto, 100, 38);

                g2.dispose();
            }
        };
        b.setBounds(x, y, w, h);
        b.setBackground(corAzulEscuro);
        b.setFocusable(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(corHover); b.repaint(); }
            @Override public void mouseExited(MouseEvent e) { b.setBackground(corAzulEscuro); b.repaint(); }
        });

        // Ação do clique
        b.addActionListener(e -> processarResposta(isCorreta));

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
            b.setBounds(x, 0, 60, 60); 
            b.addActionListener(e -> { this.dispose(); new EscolherJogoAluno().setVisible(true); });
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
            robotoBold32 = baseFont.deriveFont(Font.BOLD, 32f);
            robotoBold28 = baseFont.deriveFont(Font.BOLD, 28f);
            robotoBold24 = baseFont.deriveFont(Font.BOLD, 24f);
            robotoBold20 = baseFont.deriveFont(Font.PLAIN, 20f); 
        } catch (Exception e) {
            robotoBold32 = new Font("Arial", Font.BOLD, 32);
            robotoBold28 = new Font("Arial", Font.BOLD, 28);
            robotoBold24 = new Font("Arial", Font.BOLD, 24);
            robotoBold20 = new Font("Arial", Font.PLAIN, 20);
        }
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new JogarQuiz()); }
}