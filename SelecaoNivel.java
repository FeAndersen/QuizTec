import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.io.File;

public class SelecaoNivel extends JFrame {

    private Font robotoBold32, robotoBold24, robotoBold20, robotoBold16, robotoRegular12;

    public SelecaoNivel() {
        carregarFontes();

        // Configurações da Janela
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit tk = Toolkit.getDefaultToolkit();
        int larguraTela = (int) tk.getScreenSize().getWidth();
        int alturaTela = (int) tk.getScreenSize().getHeight();

        // Painel de Fundo
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

        // Header (Barra Superior Vermelha)
        JPanel header = new JPanel();
        header.setBackground(new Color(178, 0, 0));
        header.setBounds(0, 0, larguraTela, 80);
        header.setLayout(null);

        JButton btnFechar = criarBotaoControle("X", larguraTela - 50, 0);
        JButton btnMin = criarBotaoControle("-", larguraTela - 100, 0);
        header.add(btnFechar);
        header.add(btnMin);

        // Botão de Voltar
        JButton btnVoltar = criarBotaoControle("↰", 40, 10);
        header.add(btnVoltar);

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

        // Card Central Vermelho
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

        // Título do Card
        JLabel lblTituloCard = new JLabel("Qual nível deseja praticar?", SwingConstants.CENTER);
        lblTituloCard.setForeground(Color.WHITE);
        lblTituloCard.setFont(robotoBold32);
        lblTituloCard.setBounds(0, 60, larguraCard, 50);
        cardPrincipal.add(lblTituloCard);

        // --- Layout dos 3 Botões de Nível ---
        int largBotao = 260; 
        int altBotao = 300; 
        int espaco = 60; 
        
        int larguraTotalBotoes = (largBotao * 3) + (espaco * 2);
        int startX = (larguraCard - larguraTotalBotoes) / 2;
        int startY = 210; // Um pouco mais alto para acomodar botões mais altos

        JButton btnFacil = criarBotaoNivel("Fácil", "Identificação", "Para aprender os nomes", 1, startX, startY, largBotao, altBotao);
        JButton btnMedio = criarBotaoNivel("Médio", "Funções", "Para que serve cada material?", 2, startX + largBotao + espaco, startY, largBotao, altBotao);
        JButton btnDificil = criarBotaoNivel("Difícil", "Sistemas", "Montagem de experimentos", 3, startX + (largBotao + espaco) * 2, startY, largBotao, altBotao);

        cardPrincipal.add(btnFacil);
        cardPrincipal.add(btnMedio);
        cardPrincipal.add(btnDificil);

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

    // --- NOVA FÁBRICA: Botões de Nível (Com 3 textos e novos ícones) ---
    private JButton criarBotaoNivel(String txtTopo, String txtMeio, String txtBase, int tipoIcone, int x, int y, int w, int h) {
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
                g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                int cx = getWidth() / 2;
                int cy = getHeight() / 2 - 20; // Centro do ícone

                // DESENHO DOS ÍCONES VETORIZADOS
                if (tipoIcone == 1) {
                    // Tubo de Ensaio
                    g2.drawRoundRect(cx - 15, cy - 35, 30, 65, 15, 15); // Corpo
                    g2.drawLine(cx - 22, cy - 35, cx + 22, cy - 35);    // Tampa/Borda
                    g2.drawLine(cx - 15, cy, cx + 15, cy);              // Nível do líquido
                    g2.setStroke(new BasicStroke(3f));
                    g2.drawLine(cx - 5, cy + 15, cx + 5, cy + 15);      // Reflexo/Bolha
                } else if (tipoIcone == 2) {
                    // Microscópio
                    g2.drawLine(cx - 20, cy + 35, cx + 20, cy + 35); // Base
                    g2.drawLine(cx, cy + 35, cx, cy + 20);           // Haste da base
                    g2.drawLine(cx - 15, cy + 20, cx + 15, cy + 20); // Platina (Mesa)
                    // Braço curvo
                    g2.drawArc(cx - 25, cy - 25, 30, 45, 90, 180); 
                    // Tubo ocular
                    g2.setStroke(new BasicStroke(8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(cx - 5, cy - 20, cx + 10, cy + 5);
                } else if (tipoIcone == 3) {
                    // Sistemas (Seringa e Frasco)
                    // Seringa (Esquerda)
                    g2.drawRect(cx - 25, cy - 15, 12, 35);           // Corpo
                    g2.drawLine(cx - 19, cy - 15, cx - 19, cy - 30); // Êmbolo haste
                    g2.drawLine(cx - 28, cy - 30, cx - 10, cy - 30); // Êmbolo topo
                    g2.drawLine(cx - 19, cy + 20, cx - 19, cy + 35); // Agulha
                    // Frasco/Bolsa (Direita)
                    g2.drawRoundRect(cx + 5, cy - 10, 25, 30, 15, 15); // Bolsa
                    g2.drawLine(cx + 17, cy + 20, cx + 17, cy + 30);   // Tubo inferior
                    g2.drawLine(cx + 17, cy - 10, cx + 17, cy - 20);   // Gancho superior
                }

                // DESENHO DOS TEXTOS
                FontMetrics fm;
                
                // Texto Topo (Fácil, Médio, Difícil)
                g2.setFont(robotoBold16);
                fm = g2.getFontMetrics();
                g2.drawString(txtTopo, (getWidth() - fm.stringWidth(txtTopo)) / 2, 40);

                // Texto Meio (Identificação, Funções, Sistemas)
                g2.setFont(robotoBold24);
                fm = g2.getFontMetrics();
                g2.drawString(txtMeio, (getWidth() - fm.stringWidth(txtMeio)) / 2, getHeight() - 60);

                // Texto Base (Descrição)
                g2.setFont(robotoRegular12);
                fm = g2.getFontMetrics();
                g2.drawString(txtBase, (getWidth() - fm.stringWidth(txtBase)) / 2, getHeight() - 35);

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

        b.addActionListener(e -> System.out.println("Nível selecionado: " + txtTopo));

        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { b.setBackground(corFundoHover); b.repaint(); }
            @Override
            public void mouseExited(MouseEvent e) { b.setBackground(corFundo); b.repaint(); }
        });

        return b;
    }

    // --- MÉTODOS MANTIDOS: Header e Fontes ---
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
            // AÇÃO DO BOTÃO VOLTAR
            b.addActionListener(e -> {
                this.dispose(); // Fecha esta tela
                new MenuAluno().setVisible(true); // Abre o Menu do Aluno
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
            @Override
            public void mouseEntered(MouseEvent e) { b.setBackground(corHover); b.repaint(); }
            @Override
            public void mouseExited(MouseEvent e) { b.setBackground(corInvisivel); b.repaint(); }
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
            robotoBold20 = baseFont.deriveFont(Font.BOLD, 20f); 
            robotoBold16 = baseFont.deriveFont(Font.BOLD, 16f); // Fonte média
            robotoRegular12 = baseFont.deriveFont(Font.PLAIN, 14f); // Fonte pequena (ajustado para 14p para melhor leitura)
        } catch (Exception e) {
            robotoBold32 = new Font("Arial", Font.BOLD, 32);
            robotoBold24 = new Font("Arial", Font.BOLD, 24);
            robotoBold20 = new Font("Arial", Font.BOLD, 20);
            robotoBold16 = new Font("Arial", Font.BOLD, 16);
            robotoRegular12 = new Font("Arial", Font.PLAIN, 14);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SelecaoNivel());
    }
}