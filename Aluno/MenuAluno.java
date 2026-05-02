package Aluno;
import javax.swing.*;

import Cadastro.Login;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class MenuAluno extends JFrame {

    private Font robotoBold32, robotoBold24, robotoBold36;
    private String nomeAluno;

    public MenuAluno(String nome) {
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
        header.add(criarBotaoControle("↰", 0, 0));

        JLabel txtQuizTec = new JLabel("QuizTec");
        txtQuizTec.setForeground(Color.WHITE);
        txtQuizTec.setFont(robotoBold32);
        txtQuizTec.setBounds(110, 0, 200, 80);
        header.add(txtQuizTec);

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

        JLabel lblTituloCard = new JLabel("O que vamos praticar hoje?", SwingConstants.CENTER);
        lblTituloCard.setForeground(Color.WHITE);
        lblTituloCard.setFont(robotoBold32);
        lblTituloCard.setBounds(0, 60, larguraCard, 50);
        cardPrincipal.add(lblTituloCard);

        int largBotao = 280; 
        int altBotao = 320; 
        int espaco = 50; 
        int larguraTotalBotoes = (largBotao * 3) + (espaco * 2);
        int startX = (larguraCard - larguraTotalBotoes) / 2;
        int startY = 180;

        // Botoes agora usam as imagens PNG da pasta images/
        cardPrincipal.add(criarBotaoCard("Iniciar Prática", "", "QuizTec\\images\\science.png", startX, startY, largBotao, altBotao, 1));
        cardPrincipal.add(criarBotaoCard("Meu", "desempenho", "QuizTec\\images\\bar_chart.png", startX + largBotao + espaco, startY, largBotao, altBotao, 2));
        cardPrincipal.add(criarBotaoCard("Sair", "", "QuizTec\\images\\logout.png", startX + (largBotao + espaco) * 2, startY, largBotao, altBotao, 3));

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

    private JButton criarBotaoCard(String linha1, String linha2, String caminhoIcone, int x, int y, int w, int h, int idAcao) {
        Color corFundo = Color.WHITE;
        Color corFundoHover = new Color(240, 240, 240);
        Color corAzulEscuro = new Color(30, 55, 90);

        JButton b = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                
                // Desenhar a imagem PNG
                try {
                    ImageIcon icon = new ImageIcon(caminhoIcone);
                    Image img = icon.getImage();
                    int iconDim = 100;
                    int iconX = (getWidth() - iconDim) / 2;
                    int iconY = 50; 
                    g2.drawImage(img, iconX, iconY, iconDim, iconDim, null);
                } catch (Exception e) {
                    System.out.println("Erro ao carregar: " + caminhoIcone);
                }

                g2.setColor(corAzulEscuro);
                g2.setFont(robotoBold36);
                FontMetrics fm = g2.getFontMetrics();
                if (linha2.isEmpty()) {
                    g2.drawString(linha1, (getWidth() - fm.stringWidth(linha1)) / 2, getHeight() - 60);
                } else {
                    g2.drawString(linha1, (getWidth() - fm.stringWidth(linha1)) / 2, getHeight() - 75);
                    g2.drawString(linha2, (getWidth() - fm.stringWidth(linha2)) / 2, getHeight() - 35);
                }
                g2.dispose();
            }
        };

        b.setBounds(x, y, w, h);
        b.setBackground(corFundo);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorderPainted(false);

      b.addActionListener(e -> {
            if (idAcao == 1) { 
                this.dispose(); 
                new SelecaoNivel(this.nomeAluno).setVisible(true); 
            }
            else if (idAcao == 3) { 
                this.dispose(); 
                new Login().setVisible(true); 
            }
        });

        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(corFundoHover); b.repaint(); }
            @Override public void mouseExited(MouseEvent e) { b.setBackground(corFundo); b.repaint(); }
        });

        return b;
    }

    private JButton criarBotaoControle(String texto, int x, int y) {
        Color corInvisivel = new Color(178, 0, 0); 
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
            b.addActionListener(e -> { this.dispose(); new Login().setVisible(true); });
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
        } catch (Exception e) {
            robotoBold32 = new Font("Arial", Font.BOLD, 32);
            robotoBold24 = new Font("Arial", Font.BOLD, 24);
            robotoBold36 = new Font("Arial", Font.BOLD, 36);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuAluno("Visitante"));
    }
}