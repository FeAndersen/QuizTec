import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.font.TextAttribute; // Importante para o super negrito
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Login extends JFrame {

    private Font fontTitulo, robotoSemiBold40, robotoRegular20;

    public Login() {
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
                ImageIcon imagemFundo = new ImageIcon("fundo_etec.jpg");
                g.drawImage(imagemFundo.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        painelFundo.setLayout(null);
        setContentPane(painelFundo);

        JPanel painelControles = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        painelControles.setOpaque(false);
        painelControles.setBounds(larguraTela - 120, 0, 120, 50);

        JButton btnMin = criarBotaoBarra("-");
        JButton btnFechar = criarBotaoBarra("X");
        btnFechar.setBackground(new Color(180, 0, 0));

        btnMin.addActionListener(e -> setState(Frame.ICONIFIED));
        btnFechar.addActionListener(e -> System.exit(0));

        painelControles.add(btnMin);
        painelControles.add(btnFechar);
        painelFundo.add(painelControles);

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
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                g2.setColor(new Color(178, 0, 0));
                g2.fillRect(getWidth() / 2, 0, (getWidth() / 2) + 1, getHeight());
                
                g2.dispose();
            }
        };
        blocoCentral.setLayout(null);
        blocoCentral.setOpaque(false);
        blocoCentral.setBounds(xCentro, yCentro, larguraBloco, alturaBloco);

        int centroY = alturaBloco / 2;
        adicionarLogo(blocoCentral, "Logo_etec.jpg", larguraBloco / 4, centroY - 100, 0.25);
        adicionarLogo(blocoCentral, "Logo_cps.jpg", larguraBloco / 4, centroY + 130, 0.18);

        int fieldW = (int) (larguraBloco * 0.32);
        int fieldH = 55;
        int fieldX = (int) (larguraBloco * 0.75) - (fieldW / 2);

        JLabel txtTitulo = new JLabel("Login");
        txtTitulo.setForeground(Color.WHITE);
        txtTitulo.setFont(fontTitulo);
        // Espaço extra na largura (+100) para garantir que a fonte mais grossa caiba
        txtTitulo.setBounds(fieldX, centroY - 240, fieldW + 100, 100); 
        blocoCentral.add(txtTitulo);

        blocoCentral.add(criarCampo("Inserir email", fieldX, centroY - 90, fieldW, fieldH));
        blocoCentral.add(criarCampo("Inserir senha", fieldX, centroY + 0, fieldW, fieldH));

        JButton btnEntrar = new JButton("Entrar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 55, 90));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        btnEntrar.setBounds(fieldX, centroY + 110, fieldW, 70);
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFont(robotoSemiBold40);
        btnEntrar.setContentAreaFilled(false);
        btnEntrar.setBorderPainted(false);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        blocoCentral.add(btnEntrar);

        JLabel lblLink = new JLabel("<html><u>não tem login de acesso?</u></html>");
        lblLink.setForeground(Color.WHITE);
        lblLink.setFont(new Font("Arial", Font.PLAIN, 18));
        lblLink.setBounds(fieldX, centroY + 190, fieldW, 30);
        lblLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        blocoCentral.add(lblLink);

        painelFundo.add(blocoCentral);
        setVisible(true);
    }

    private void carregarFontes() {
        try {
            File fontFile = new File("RobotoSerif-Bold.ttf");
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            
            // LÓGICA PARA O "SUPER NEGRITO"
            Map<TextAttribute, Object> atributos = new HashMap<>();
            atributos.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_EXTRABOLD); // Peso extra!
            atributos.put(TextAttribute.SIZE, 64f); // Tamanho fixo 64
            
            fontTitulo = baseFont.deriveFont(atributos);
            robotoSemiBold40 = baseFont.deriveFont(Font.BOLD, 36f);
            robotoRegular20 = baseFont.deriveFont(Font.PLAIN, 18f);
        } catch (Exception e) {
            Map<TextAttribute, Object> fallback = new HashMap<>();
            fallback.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_EXTRABOLD);
            fallback.put(TextAttribute.SIZE, 64f);
            fallback.put(TextAttribute.FAMILY, "Serif");
            
            fontTitulo = Font.getFont(fallback);
            robotoSemiBold40 = new Font("sansserif", Font.BOLD, 35);
            robotoRegular20 = new Font("sansserif", Font.PLAIN, 18);
        }
    }

    private JTextField criarCampo(String placeholder, int x, int y, int w, int h) {
        JTextField campo = new JTextField(placeholder);
        campo.setBounds(x, y, w, h);
        campo.setBackground(new Color(220, 220, 220));
        campo.setFont(robotoRegular20);
        campo.setForeground(Color.GRAY);
        campo.setHorizontalAlignment(JTextField.CENTER);
        campo.setBorder(null);
        campo.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(Color.GRAY);
                }
            }
        });
        return campo;
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
            System.out.println("Erro ao carregar imagem: " + path);
        }
    }

    private JButton criarBotaoBarra(String simbolo) {
        JButton btn = new JButton(simbolo);
        btn.setPreferredSize(new Dimension(40, 30));
        btn.setBackground(new Color(50, 50, 50));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(null);
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login());
    }
}