import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SelecaoCadastro extends JFrame {

    private Font fontTitulo, robotoSemiBold40, robotoRegular20;

    public SelecaoCadastro() {
        carregarFontes();
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit tk = Toolkit.getDefaultToolkit();
        int larguraTela = (int) tk.getScreenSize().getWidth();
        int alturaTela = (int) tk.getScreenSize().getHeight();

        // 1. Painel de Fundo
        JPanel painelFundo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Caminho atualizado para a pasta images
                ImageIcon imagemFundo = new ImageIcon("images/fundo_etec.jpg");
                g.drawImage(imagemFundo.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        painelFundo.setLayout(null);
        setContentPane(painelFundo);

        // 2. Bloco Central (Branco e Vermelho)
        int larguraBloco = (int) (larguraTela * 0.85);
        int alturaBloco = (int) (alturaTela * 0.85);
        int xCentro = (larguraTela - larguraBloco) / 2;
        int yCentro = (alturaTela - alturaBloco) / 2;

        JPanel blocoCentral = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Shape formaArredondada = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 80, 80);
                g2.setClip(formaArredondada);
                
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth() / 2, getHeight());
                
                g2.setColor(new Color(178, 0, 0));
                g2.fillRect(getWidth() / 2, 0, (getWidth() / 2) + 1, getHeight());
                
                g2.dispose();
            }
        };
        blocoCentral.setLayout(null);
        blocoCentral.setOpaque(false);
        blocoCentral.setBounds(xCentro, yCentro, larguraBloco, alturaBloco);

        // Logos (Atualizados para a pasta images)
        int centroY = alturaBloco / 2;
        adicionarLogo(blocoCentral, "images/Logo_etec.jpg", larguraBloco / 4, centroY - 100, 0.25);
        adicionarLogo(blocoCentral, "images/Logo_cps.jpg", larguraBloco / 4, centroY + 130, 0.18);

        // Título
        JLabel txtTitulo = new JLabel("Novo cadastro como", SwingConstants.CENTER);
        txtTitulo.setForeground(Color.WHITE);
        txtTitulo.setFont(fontTitulo.deriveFont(48f));
        txtTitulo.setBounds(larguraBloco / 2, centroY - 220, larguraBloco / 2, 100);
        blocoCentral.add(txtTitulo);

        // Opções (Agora passando o caminho da imagem PNG)
        int largOpcao = 260;
        int altOpcao = 260;
        int espaco = 50;
        int startX = (larguraBloco / 2) + ((larguraBloco / 2 - (largOpcao * 2 + espaco)) / 2);
        int startY = centroY - 60;

        blocoCentral.add(criarBotaoOpcao("Professor", "images/professor_bom.png", startX, startY, largOpcao, altOpcao));
        blocoCentral.add(criarBotaoOpcao("Aluno", "images/aluno.png", startX + largOpcao + espaco, startY, largOpcao, altOpcao));

        painelFundo.add(blocoCentral);

        // 3. Botões de Controle e Voltar
        JButton btnFechar = criarBotaoControle("X", larguraTela - 50, 0);
        JButton btnMin = criarBotaoControle("-", larguraTela - 100, 0);
        JButton btnVoltar = criarBotaoControle("↰", 0, 0); 

        painelFundo.add(btnFechar);
        painelFundo.add(btnMin);
        painelFundo.add(btnVoltar);
        
        painelFundo.setComponentZOrder(btnFechar, 0);
        painelFundo.setComponentZOrder(btnMin, 0);
        painelFundo.setComponentZOrder(btnVoltar, 0);

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

    private JButton criarBotaoOpcao(String tipo, String caminhoIcone, int x, int y, int w, int h) {
        Color corAzul = new Color(30, 55, 90);
        Color corHover = new Color(45, 75, 115);

        JButton b = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Desenhar a imagem PNG branca enviada
                try {
                    ImageIcon icon = new ImageIcon(caminhoIcone);
                    Image img = icon.getImage();
                    
                    int iconDim = 110; // Tamanho ideal para esses botões (que são um pouco menores que os cards principais)
                    int iconX = (getWidth() - iconDim) / 2;
                    int iconY = 40; 
                    
                    g2.drawImage(img, iconX, iconY, iconDim, iconDim, null);
                } catch (Exception e) {
                    System.out.println("Erro ao carregar imagem em: " + caminhoIcone);
                }

                // Renderização do Texto
                g2.setColor(Color.WHITE);
                g2.setFont(robotoSemiBold40.deriveFont(32f));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(tipo, (getWidth() - fm.stringWidth(tipo)) / 2, getHeight() - 40);
                
                g2.dispose();
            }
        };

        b.setBounds(x, y, w, h);
        b.setBackground(corAzul);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);

        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(corHover); b.repaint(); }
            @Override public void mouseExited(MouseEvent e) { b.setBackground(corAzul); b.repaint(); }
        });

        // Mantém a exata lógica de redirecionamento que você tinha antes
        b.addActionListener(e -> {
            this.dispose();
            new Cadastro().setVisible(true);
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
            b.addActionListener(e -> {
                this.dispose();
                new Login().setVisible(true); 
            });
        } else {
            b.setFont(new Font("Arial", Font.BOLD, 24));
            b.setBounds(x, y, 50, 40);
            if (texto.equals("X")) b.addActionListener(e -> System.exit(0));
            else b.addActionListener(e -> setState(Frame.ICONIFIED));
        }

        b.setMargin(new Insets(0, 0, 0, 0)); 
        b.setBackground(corInvisivel);
        b.setForeground(Color.WHITE);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
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
            Map<TextAttribute, Object> atributos = new HashMap<>();
            atributos.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_EXTRABOLD);
            atributos.put(TextAttribute.SIZE, 64f);
            fontTitulo = baseFont.deriveFont(atributos);
            robotoSemiBold40 = baseFont.deriveFont(Font.BOLD, 36f);
            robotoRegular20 = baseFont.deriveFont(Font.PLAIN, 18f);
        } catch (Exception e) {
            fontTitulo = new Font("Serif", Font.BOLD, 64);
            robotoSemiBold40 = new Font("sansserif", Font.BOLD, 35);
            robotoRegular20 = new Font("sansserif", Font.PLAIN, 18);
        }
    }

    private void adicionarLogo(JPanel painel, String path, int xCentro, int yPos, double escala) {
        try {
            ImageIcon icon = new ImageIcon(path);
            int novaAlt = (int) (painel.getHeight() * escala);
            int novaLarg = (icon.getIconWidth() * novaAlt) / icon.getIconHeight();
            Image img = icon.getImage().getScaledInstance(novaLarg, novaAlt, Image.SCALE_SMOOTH);
            JLabel label = new JLabel(new ImageIcon(img));
            label.setBounds(xCentro - (novaLarg / 2), yPos - (novaAlt / 2), novaLarg, novaAlt);
            painel.add(label);
        } catch (Exception e) {
            System.out.println("Erro ao carregar: " + path);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SelecaoCadastro());
    }
}