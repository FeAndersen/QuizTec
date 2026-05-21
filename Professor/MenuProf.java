package Professor;
import javax.swing.*;

import Cadastro.Login;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class MenuProf extends JFrame {

    private Font robotoBold32, robotoBold24, robotoBold36;

    public MenuProf() {
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
        
        // O botão de voltar no MenuProf normalmente leva de volta pro Login
        header.add(criarBotaoControle("↰", 0, 0)); 

        JLabel txtQuizTec = new JLabel("QuizTec");
        txtQuizTec.setForeground(Color.WHITE);
        txtQuizTec.setFont(robotoBold32);
        txtQuizTec.setBounds(110, 0, 200, 80);
        header.add(txtQuizTec);

        JLabel txtOla = new JLabel("Olá, professor", SwingConstants.RIGHT);
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

        JLabel lblTituloCard = new JLabel("O que vamos fazer hoje professor?", SwingConstants.CENTER);
        lblTituloCard.setForeground(Color.WHITE);
        lblTituloCard.setFont(robotoBold32);
        lblTituloCard.setBounds(0, 60, larguraCard, 50);
        cardPrincipal.add(lblTituloCard);

        // Botões Brancos Principais
        int largBotao = 280; 
        int altBotao = 320; 
        int espaco = 50; 
        int larguraTotalBotoes = (largBotao * 3) + (espaco * 2);
        int startX = (larguraCard - larguraTotalBotoes) / 2;
        int startY = 200;

        // Repare que adicionei um número (1, 2, 3) no final de cada chamada para identificar a ação!
        cardPrincipal.add(criarBotaoCard("Criar novo", "jogo", "images\\add.png", startX, startY, largBotao, altBotao, 1));
        cardPrincipal.add(criarBotaoCard("Editar os seus", "jogos", "images\\edit.png", startX + largBotao + espaco, startY, largBotao, altBotao, 2));
        cardPrincipal.add(criarBotaoCard("Gerenciar", "Perfis", "images\\person_add_disabled.png", startX + (largBotao + espaco) * 2, startY, largBotao, altBotao, 3));

        painelFundo.add(header);
        painelFundo.add(cardPrincipal);

        setVisible(true);
    }

    // Método atualizado: Adicionei "int idAcao" nos parâmetros
    private JButton criarBotaoCard(String linha1, String linha2, String caminhoIcone, int x, int y, int w, int h, int idAcao) {
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
        b.setBackground(Color.WHITE);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorderPainted(false);

        // Efeito Hover
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(new Color(240, 240, 240)); b.repaint(); }
            @Override public void mouseExited(MouseEvent e) { b.setBackground(Color.WHITE); b.repaint(); }
        });

        // ==========================================
        // AQUI ESTÁ A LÓGICA DE REDIRECIONAMENTO
        // ==========================================
        b.addActionListener(e -> {
            this.dispose(); // Fecha o Menu do Professor
            
            if (idAcao == 1) {
                new CriarSelecaoNivel().setVisible(true); // Vai criar um novo jogo
            } 
            else if (idAcao == 2) {
                new SeusJogosCriados().setVisible(true);  // Vai ver o histórico de jogos
            } 
            else if (idAcao == 3) {
                new SeusAlunos().setVisible(true);        // Vai para o gerenciamento de alunos
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
            // O botão de voltar do MenuProf geralmente desloga e volta pro Login
            b.addActionListener(e -> { this.dispose(); new Login().setVisible(true); });
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
        b.setFocusPainted(false);
        b.setFocusable(false); // Mata a bordinha do Windows
        b.setBorder(null);     // Mata a bordinha do Windows
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
        SwingUtilities.invokeLater(() -> new MenuProf());
    }
}