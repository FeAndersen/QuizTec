import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.io.File;

public class MenuAluno extends JFrame {

    private Font robotoBold32, robotoBold24, robotoBold22, robotoRegular20;

    public MenuAluno() {
        carregarFontes();

        // 1. Configurações da Janela
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit tk = Toolkit.getDefaultToolkit();
        int larguraTela = (int) tk.getScreenSize().getWidth();
        int alturaTela = (int) tk.getScreenSize().getHeight();

        // 2. Painel de Fundo
        JPanel painelFundo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imagemFundo = new ImageIcon("fundo_etec.jpg");
                g.drawImage(imagemFundo.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        painelFundo.setLayout(null);
        setContentPane(painelFundo);

        // 3. Header (Barra Superior)
        JPanel header = new JPanel();
        header.setBackground(new Color(178, 0, 0));
        header.setBounds(0, 0, larguraTela, 80);
        header.setLayout(null);

        JButton btnFechar = criarBotaoControle("X", larguraTela - 50, 0);
        JButton btnMin = criarBotaoControle("-", larguraTela - 100, 0);
        header.add(btnFechar);
        header.add(btnMin);

        JButton btnVoltar = criarBotaoControle("↰", 40, 10);
        header.add(btnVoltar);

        JLabel txtQuizTec = new JLabel("QuizTec");
        txtQuizTec.setForeground(Color.WHITE);
        txtQuizTec.setFont(robotoBold32);
        txtQuizTec.setBounds(110, 0, 200, 80);
        header.add(txtQuizTec);

        JLabel txtOla = new JLabel("Olá, Aluno!", SwingConstants.RIGHT);
        txtOla.setForeground(Color.WHITE);
        txtOla.setFont(robotoBold24);
        txtOla.setBounds(larguraTela - 450, 0, 300, 80);
        header.add(txtOla);

        // 4. Card Central Vermelho
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

        // --- Título do Card ---
        JLabel lblTituloCard = new JLabel("O que vamos praticar hoje?", SwingConstants.CENTER);
        lblTituloCard.setForeground(Color.WHITE);
        lblTituloCard.setFont(robotoBold32);
        lblTituloCard.setBounds(0, 60, larguraCard, 50);
        cardPrincipal.add(lblTituloCard);

        // --- Layout dos 3 Botões Brancos ---
        int largBotao = 240;
        int altBotao = 240;
        int espaco = 60;

        int larguraTotalBotoes = (largBotao * 3) + (espaco * 2);
        int startX = (larguraCard - larguraTotalBotoes) / 2;

        // Ajuste fino aplicado com base na sua excelente observação!
        int startY = 220;

        JButton btnPratica = criarBotaoCard("Iniciar Prática", "", 1, startX, startY, largBotao, altBotao);
        JButton btnDesempenho = criarBotaoCard("Meu", "desempenho", 2, startX + largBotao + espaco, startY, largBotao,
                altBotao);
        JButton btnSair = criarBotaoCard("Sair", "", 3, startX + (largBotao + espaco) * 2, startY, largBotao, altBotao);

        cardPrincipal.add(btnPratica);
        cardPrincipal.add(btnDesempenho);
        cardPrincipal.add(btnSair);

        // 5. Montagem Final
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

    // --- FÁBRICA DE BOTÕES DO CARD ---
    private JButton criarBotaoCard(String linha1, String linha2, int tipoIcone, int x, int y, int w, int h) {
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

                g2.setColor(corAzulEscuro);
                // Caneta configurada para traços arredondados idênticos à referência
                g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                int cx = getWidth() / 2;
                int cy = getHeight() / 2 - 30; 

                // DESENHO DOS ÍCONES
                if (tipoIcone == 1) {
                    // Ícone 1: Erlenmeyer (Frasco) usando Path2D para linha contínua perfeita
                    Path2D erlenmeyer = new Path2D.Double();
                    erlenmeyer.moveTo(cx - 12, cy - 30); // pescoço esquerdo
                    erlenmeyer.lineTo(cx - 12, cy - 5);  // base do pescoço
                    erlenmeyer.lineTo(cx - 35, cy + 35); // diagonal até a base esquerda
                    erlenmeyer.lineTo(cx + 35, cy + 35); // base plana (chão do frasco)
                    erlenmeyer.lineTo(cx + 12, cy - 5);  // diagonal subindo à direita
                    erlenmeyer.lineTo(cx + 12, cy - 30); // pescoço direito
                    g2.draw(erlenmeyer);
                    
                    // Borda superior (o "lábio" do frasco)
                    g2.drawLine(cx - 18, cy - 30, cx + 18, cy - 30);
                    
                } else if (tipoIcone == 2) {
                    // Ícone 2: Gráfico de Barras
                    g2.fillRect(cx - 28, cy + 5, 12, 25); 
                    g2.fillRect(cx - 6, cy - 25, 12, 55); 
                    g2.fillRect(cx + 16, cy - 10, 12, 40); 
                } else if (tipoIcone == 3) {
                    // Ícone 3: Porta de Saída
                    g2.drawPolyline(new int[]{cx + 5, cx - 20, cx - 20, cx + 5}, new int[]{cy - 25, cy - 25, cy + 25, cy + 25}, 4); 
                    g2.drawLine(cx - 10, cy, cx + 25, cy); 
                    g2.drawLine(cx + 15, cy - 10, cx + 25, cy); 
                    g2.drawLine(cx + 15, cy + 10, cx + 25, cy); 
                }

                // DESENHO DOS TEXTOS
                g2.setFont(robotoBold22);
                FontMetrics fm = g2.getFontMetrics();
                
                if (linha2.isEmpty()) {
                    int txtX = (getWidth() - fm.stringWidth(linha1)) / 2;
                    g2.drawString(linha1, txtX, getHeight() - 50);
                } else {
                    int txtX1 = (getWidth() - fm.stringWidth(linha1)) / 2;
                    g2.drawString(linha1, txtX1, getHeight() - 65);
                    int txtX2 = (getWidth() - fm.stringWidth(linha2)) / 2;
                    g2.drawString(linha2, txtX2, getHeight() - 35);
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

        // Ações de clique personalizadas roteando as telas
        b.addActionListener(e -> {
            if (tipoIcone == 1) { // 1 é o botão Iniciar Prática
                this.dispose(); // Fecha o MenuAluno
                new SelecaoNivel().setVisible(true); // Abre a Seleção de Nível
            } else if (tipoIcone == 2) {
                System.out.println("Abrindo Desempenho...");
                // Aqui você fará a mesma coisa quando criar a tela de Desempenho
            } else if (tipoIcone == 3) {
                this.dispose(); 
                new Login().setVisible(true); // Retorna para o Login ao Sair
            }
        });

        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                b.setBackground(corFundoHover);
                b.repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                b.setBackground(corFundo);
                b.repaint();
            }
        });

        return b;
    }

    // --- MÉTODOS MANTIDOS: Botões Superiores do Header ---
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
            b.setBounds(x, 4, 60, 60);
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

        if (texto.equals("X"))
            b.addActionListener(e -> System.exit(0));
        else if (texto.equals("-"))
            b.addActionListener(e -> setState(Frame.ICONIFIED));

        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                b.setBackground(corHover);
                b.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                b.setBackground(corInvisivel);
                b.repaint();
            }
        });

        return b;
    }

    // --- MÉTODOS MANTIDOS: Fontes ---
    private void carregarFontes() {
        try {
            File fontFile = new File("RobotoSerif-Bold.ttf");
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(baseFont);

            robotoBold32 = baseFont.deriveFont(Font.BOLD, 32f);
            robotoBold24 = baseFont.deriveFont(Font.BOLD, 24f);
            robotoBold22 = baseFont.deriveFont(Font.BOLD, 22f);
            robotoRegular20 = baseFont.deriveFont(Font.PLAIN, 20f);
        } catch (Exception e) {
            robotoBold32 = new Font("Arial", Font.BOLD, 32);
            robotoBold24 = new Font("Arial", Font.BOLD, 24);
            robotoBold22 = new Font("Arial", Font.BOLD, 22);
            robotoRegular20 = new Font("Arial", Font.PLAIN, 20);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuAluno());
    }
}