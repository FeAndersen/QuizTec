package Aluno;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class HistoricoAluno extends JFrame {

    private Font robotoBold36, robotoBold24, robotoBold14, robotoBold10;
    private String nomeAluno; // Armazena o nome do aluno logado

    public HistoricoAluno(String nome) {
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
                Image imgFundo = new ImageIcon("images\\fundo_etec.jpg").getImage();
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

        // Exibe o nome dinâmico do aluno
        JLabel txtOla = new JLabel("Olá, " + nomeAluno, SwingConstants.RIGHT);
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

        JLabel lblTitulo = new JLabel("Histórico", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(robotoBold36);
        lblTitulo.setBounds(0, 40, larguraCard, 40);
        cardPrincipal.add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("Seus resultados de jogos e seu ranking", SwingConstants.CENTER);
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

        JPanel painelHistorico = new JPanel();
        painelHistorico.setLayout(null); 
        painelHistorico.setOpaque(false);
        painelHistorico.setBackground(new Color(0,0,0,0));

        int yItem = 10;
        int hItem = 80;
        int espacoItem = 15;

        // Dados simulados (No futuro, você buscará isso do banco usando o nomeAluno)
        String[] nomesJogos = {"Quiz de Vidrarias - 1º Ano A", "Quiz de Função - 1º Ano D", "Quiz de Vidrarias - 1º Ano A", "Quiz de Sistemas - 1º Ano D"};
        String[] icones = {"images\\labs.png", "images\\biotech.png", "images\\biotech.png", "images\\fluid_med.png"};
        int[] acertosArr = {9, 9, 9, 9};
        int[] ptsArr = {1933, 1933, 1933, 1933};

        for (int i = 0; i < nomesJogos.length; i++) {
            painelHistorico.add(criarItemHistorico(nomesJogos[i], icones[i], acertosArr[i], ptsArr[i], 0, yItem, wLista, hItem));
            yItem += hItem + espacoItem;
        }

        painelHistorico.setPreferredSize(new Dimension(wLista, yItem));

        JScrollPane scrollPane = new JScrollPane(painelHistorico);
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

    private JPanel criarItemHistorico(String nomeJogo, String iconePath, int acertos, int pontos, int x, int y, int w, int h) {
        Color corAzulEscuro = new Color(30, 55, 90);

        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                try {
                    Image imgLvl = new ImageIcon(iconePath).getImage();
                    g2.drawImage(imgLvl, 20, 10, 60, 60, null);
                } catch (Exception e) {}

                g2.setColor(corAzulEscuro);

                int xAcertos = getWidth() - 320;
                try {
                    Image imgChart = new ImageIcon("images\\insert_chart.png").getImage();
                    g2.drawImage(imgChart, xAcertos, 10, 40, 40, null);
                } catch (Exception e) {}
                g2.setFont(robotoBold10);
                g2.drawString("Acertos", xAcertos, 65);
                g2.setFont(robotoBold24);
                g2.drawString(acertos + " / 10", xAcertos + 55, 40);

                int xPontos = getWidth() - 170;
                try {
                    Image imgLeader = new ImageIcon("images\\leaderboard.png").getImage();
                    g2.drawImage(imgLeader, xPontos, 10, 40, 40, null);
                } catch (Exception e) {}
                g2.setFont(robotoBold10);
                g2.drawString("Sua pontuação", xPontos - 15, 65);
                g2.setFont(robotoBold24);
                g2.drawString(String.valueOf(pontos), xPontos + 55, 40);

                g2.dispose();
            }
        };
        p.setLayout(null);
        p.setBounds(x, y, w, h);
        p.setOpaque(false);

        JLabel lblNome = new JLabel(nomeJogo);
        lblNome.setFont(robotoBold24);
        lblNome.setForeground(corAzulEscuro);
        lblNome.setBounds(100, 15, w - 400, 30);
        p.add(lblNome);

        JLabel lblData = new JLabel("jogado em 20/3/2026");
        lblData.setFont(robotoBold14);
        lblData.setForeground(corAzulEscuro);
        lblData.setBounds(100, 45, w - 400, 20);
        p.add(lblData);

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
            b.setBounds(x, 0,   60, 60);
            b.addActionListener(e -> { 
                this.dispose(); 
                new MenuAluno().setVisible(true); // Volta passando o nome
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
            robotoBold14 = baseFont.deriveFont(Font.BOLD, 14f);
            robotoBold10 = baseFont.deriveFont(Font.BOLD, 10f);
        } catch (Exception e) {
            robotoBold36 = new Font("Arial", Font.BOLD, 36);
            robotoBold24 = new Font("Arial", Font.BOLD, 24);
            robotoBold14 = new Font("Arial", Font.BOLD, 14);
            robotoBold10 = new Font("Arial", Font.BOLD, 10);
        }
    }

    public static void main(String[] args) { 
        SwingUtilities.invokeLater(() -> new HistoricoAluno("Aluno Teste")); 
    }
}