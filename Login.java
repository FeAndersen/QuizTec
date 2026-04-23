import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
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

        // Painel de Fundo
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

        // --- Lógica do Bloco Central Mantida ---
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
        adicionarLogo(blocoCentral, "QuizTec\\images\\Logo_etec.jpg", larguraBloco / 4, centroY - 100, 0.25);
        adicionarLogo(blocoCentral, "QuizTec\\images\\Logo_cps.jpg", larguraBloco / 4, centroY + 130, 0.18);

        int fieldW = (int) (larguraBloco * 0.32);
        int fieldH = 55;
        int fieldX = (int) (larguraBloco * 0.75) - (fieldW / 2);

        JLabel txtTitulo = new JLabel("Login");
        txtTitulo.setForeground(Color.WHITE);
        txtTitulo.setFont(fontTitulo);
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
        lblLink.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            dispose(); // Fecha o Login
            new SelecaoCadastro().setVisible(true); // Abre a tela de escolha de Cadastro
    }
});
        

        painelFundo.add(blocoCentral);

        // --- NOVO PADRÃO DE BOTÕES ---
        // Posicionados no canto superior direito, com um recuo de 10px do teto
        JButton btnFechar = criarBotaoControle("X", larguraTela - 50, 0);
        JButton btnMin = criarBotaoControle("-", larguraTela - 100, 0);

        painelFundo.add(btnFechar);
        painelFundo.add(btnMin);

        // Garante que os botões fiquem sempre na frente de tudo
        painelFundo.setComponentZOrder(btnFechar, 0);
        painelFundo.setComponentZOrder(btnMin, 0);

        // --- CORREÇÃO DO MINIMIZAR ---
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

    // --- NOVO MÉTODO ENCAPSULADO (100% Invisível até o Hover) ---
    private JButton criarBotaoControle(String texto, int x, int y) {

        // A MÁGICA AQUI: O quarto número é o Alpha (transparência).
        // 0 significa totalmente transparente (invisível sobre a foto de fundo)
        Color corInvisivel = new Color(0, 0, 0, 0);
        Color corHover;

        if (texto.equals("X")) {
            corHover = new Color(232, 17, 35); // Vermelho vivo do Windows
        } else {
            corHover = new Color(100, 100, 100); // Cinza para o Minimizar
        }

        JButton b = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                // Desenha o fundo transparente ou colorido (dependendo do mouse)
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };

        b.setFont(new Font("Arial", Font.BOLD, 24));
        b.setBounds(x, y, 50, 40);
        b.setMargin(new Insets(0, 0, 0, 0)); // Evita o bug do "..."

        b.setBackground(corInvisivel); // Inicia 100% transparente
        b.setForeground(Color.WHITE); // Texto sempre branco

        // Limpeza dos padrões do Java
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setRolloverEnabled(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Ações de clique
        if (texto.equals("X")) {
            b.addActionListener(e -> System.exit(0));
        } else if (texto.equals("-")) {
            b.addActionListener(e -> setState(Frame.ICONIFIED));
        }

        // Lógica do Efeito Hover
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                b.setBackground(corHover); // Fica Vermelho ou Cinza
                b.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                b.setBackground(corInvisivel); // Volta a ser 100% transparente
                b.repaint();
            }
        });

        return b;
    }

    // --- MÉTODOS MANTIDOS ---
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login());
    }
}