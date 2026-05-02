package Aluno;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class SelecaoNivel extends JFrame {

    private Font robotoBold32, robotoBold24, robotoBold36, robotoBold18;
    private String nomeAluno; 

    public SelecaoNivel(String nome) { 
        this.nomeAluno = nome;
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
                ImageIcon imagemFundo = new ImageIcon("QuizTec\\images\\fundo_etec.jpg");
                g.drawImage(imagemFundo.getImage(), 0, 0, getWidth(), getHeight(), this);
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
        header.add(criarBotaoControle("↰", 40, 0));

        JLabel txtQuizTec = new JLabel("QuizTec");
        txtQuizTec.setForeground(Color.WHITE);
        txtQuizTec.setFont(robotoBold32);
        txtQuizTec.setBounds(110, 0, 200, 80);
        header.add(txtQuizTec);

        // NOME DINÂMICO: Agora usa a variável nomeAluno
        JLabel txtOla = new JLabel("Olá, " + nomeAluno + "!", SwingConstants.RIGHT);
        txtOla.setForeground(Color.WHITE);
        txtOla.setFont(robotoBold24);
        txtOla.setBounds(larguraTela - 450, 0, 300, 80);
        header.add(txtOla);

        int larguraCard = (int) (larguraTela * 0.85);
        int alturaCard = (int) (alturaTela * 0.75);
        int xCard = (larguraTela - larguraCard) / 2;
        int yCard = 150;

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

        JLabel lblTituloCard = new JLabel("Qual nível deseja praticar?", SwingConstants.CENTER);
        lblTituloCard.setForeground(Color.WHITE);
        lblTituloCard.setFont(robotoBold32);
        lblTituloCard.setBounds(0, 60, larguraCard, 50);
        cardPrincipal.add(lblTituloCard);

        int largBotao = 280; 
        int altBotao = 320; 
        int espaco = 50; 
        int larguraTotalBotoes = (largBotao * 3) + (espaco * 2);
        int startX = (larguraCard - larguraTotalBotoes) / 2;
        int startY = 200;

        cardPrincipal.add(criarBotaoNivel("Fácil", "Identificação", "Para aprender os nomes", "QuizTec\\images\\labs.png", startX, startY, largBotao, altBotao));
        cardPrincipal.add(criarBotaoNivel("Médio", "Funções", "Para que serve;cada material?", "QuizTec\\images\\biotech.png", startX + largBotao + espaco, startY, largBotao, altBotao));
        cardPrincipal.add(criarBotaoNivel("Difícil", "Sistemas", "Montagem de experimentos", "QuizTec\\images\\fluid_med.png", startX + (largBotao + espaco) * 2, startY, largBotao, altBotao));

        painelFundo.add(header);
        painelFundo.add(cardPrincipal);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeiconified(WindowEvent e) {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
                repaint();
                revalidate();
            }
        });

        setVisible(true);
    }

    private JButton criarBotaoNivel(String txtTopo, String txtMeio, String txtBase, String caminhoIcone, int x, int y, int w, int h) {
        Color corAzulEscuro = new Color(30, 55, 90); 
        JButton b = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                
                try {
                    ImageIcon icon = new ImageIcon(caminhoIcone);
                    Image img = icon.getImage();
                    int iconDim = 100;
                    int iconX = (getWidth() - iconDim) / 2;
                    int iconY = 70; 
                    g2.drawImage(img, iconX, iconY, iconDim, iconDim, null);
                } catch (Exception e) {}

                g2.setColor(corAzulEscuro);
                FontMetrics fm;
                g2.setFont(robotoBold24);
                fm = g2.getFontMetrics();
                g2.drawString(txtTopo, (getWidth() - fm.stringWidth(txtTopo)) / 2, 45);
                g2.setFont(robotoBold36);
                fm = g2.getFontMetrics();
                g2.drawString(txtMeio, (getWidth() - fm.stringWidth(txtMeio)) / 2, getHeight() - 80);
                g2.setFont(robotoBold18); 
                fm = g2.getFontMetrics();
                if (txtBase.contains(";")) {
                    String[] partes = txtBase.split(";");
                    g2.drawString(partes[0], (getWidth() - fm.stringWidth(partes[0])) / 2, getHeight() - 45);
                    g2.drawString(partes[1], (getWidth() - fm.stringWidth(partes[1])) / 2, getHeight() - 25);
                } else {
                    g2.drawString(txtBase, (getWidth() - fm.stringWidth(txtBase)) / 2, getHeight() - 35);
                }
                g2.dispose();
            }
        };

        b.setBounds(x, y, w, h);
        b.setBackground(Color.WHITE);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // AÇÃO: Clique no nível abre o jogo enviando o nome e o nível
        b.addActionListener(e -> {
            this.dispose();
            new JogarQuiz(this.nomeAluno, txtTopo).setVisible(true);
        });

        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(new Color(240, 240, 240)); b.repaint(); }
            @Override public void mouseExited(MouseEvent e) { b.setBackground(Color.WHITE); b.repaint(); }
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
            b.setBounds(0, 0, 60, 60); 
            // VOLTAR: Devolve o nome para o menu
            b.addActionListener(e -> { 
                this.dispose(); 
                new MenuAluno(this.nomeAluno).setVisible(true); 
            });
        } else {
            b.setFont(new Font("Arial", Font.BOLD, 24));
            b.setBounds(x, y, 50, 40); 
        }

        b.setMargin(new Insets(0, 0, 0, 0)); 
        b.setBackground(corInvisivel); 
        b.setForeground(Color.WHITE);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (texto.equals("X")) b.addActionListener(e -> System.exit(0));
        else if (texto.equals("-")) b.addActionListener(e -> setState(Frame.ICONIFIED));

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
            robotoBold32 = baseFont.deriveFont(Font.BOLD, 32f);
            robotoBold24 = baseFont.deriveFont(Font.BOLD, 24f);
            robotoBold36 = baseFont.deriveFont(Font.BOLD, 36f);
            robotoBold18 = baseFont.deriveFont(Font.BOLD, 18f); 
        } catch (Exception e) {
            robotoBold32 = new Font("Arial", Font.BOLD, 32);
            robotoBold24 = new Font("Arial", Font.BOLD, 24);
            robotoBold36 = new Font("Arial", Font.BOLD, 36);
            robotoBold18 = new Font("Arial", Font.BOLD, 18);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SelecaoNivel("Visitante"));
    }
}