import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.io.File;

public class Cadastro extends JFrame {

    private Font robotoBold64, robotoSemiBold40, robotoRegular20;
    public Cadastro() {
        carregarFontes();
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit tk = Toolkit.getDefaultToolkit();
        int larguraTela = (int) tk.getScreenSize().getWidth();
        int alturaTela = (int) tk.getScreenSize().getHeight();

        // --- PAINEL DE FUNDO (fundo_etec.jpg) ---
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

        // --- BOTÕES DE CONTROLE (NOVIDADE) ---
        JPanel painelControles = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        painelControles.setOpaque(false);
        painelControles.setBounds(larguraTela - 250, 0, 250, 60);

        JButton btnMin = criarBotaoBarra("-");
        JButton btnFechar = criarBotaoBarra("X");
        btnFechar.setBackground(new Color(180, 0, 0));

        // Lógica dos botões
        btnMin.addActionListener(e -> setState(Frame.ICONIFIED));

        btnFechar.addActionListener(e -> System.exit(0));

        painelControles.add(btnMin);
        painelControles.add(btnFechar);
        painelFundo.add(painelControles);

        // --- LISTENER PARA CORRIGIR O MINIMIZAR ---
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeiconified(WindowEvent e) {
                repaint();
                revalidate();
            }
        });

        // --- SEU BLOCO CENTRAL ORIGINAL (SEM ALTERAÇÕES) ---
        int larguraBloco = (int) (larguraTela * 0.8);
        int alturaBloco = (int) (alturaTela * 0.8);
        int xCentro = (larguraTela - larguraBloco) / 2;
        int yCentro = (alturaTela - alturaBloco) / 2;

        JPanel blocoCentral = new JPanel();
        blocoCentral.setLayout(null);
        blocoCentral.setOpaque(false);
        blocoCentral.setBounds(xCentro, yCentro, larguraBloco, alturaBloco);

        JPanel ladoBranco = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, larguraBloco, alturaBloco, 69, 69);
                g2.fillRect(larguraBloco / 2 - 50, 0, 100, alturaBloco);
            }
        };
        ladoBranco.setLayout(null);
        ladoBranco.setBounds(0, 0, larguraBloco / 2, alturaBloco);
        ladoBranco.setOpaque(false);

        int centroYBranco = alturaBloco / 2;
        adicionarLogo(ladoBranco, "Logo_etec.jpg", larguraBloco / 4, centroYBranco - 120, 0.28);
        adicionarLogo(ladoBranco, "Logo_cps.jpg", larguraBloco / 4, centroYBranco + 120, 0.20);

        JPanel ladoVermelho = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(178, 0, 0));
                g2.fillRoundRect(-larguraBloco / 2, 0, larguraBloco, alturaBloco, 69, 69);
                g2.fillRect(0, 0, 50, alturaBloco);
            }
        };
        ladoVermelho.setLayout(null);
        ladoVermelho.setBounds(larguraBloco / 2, 0, larguraBloco / 2, alturaBloco);
        ladoVermelho.setOpaque(false);

        int fieldW = (int) (larguraBloco * 0.35);
        int fieldH = 60;
        int fieldX = (larguraBloco / 4) - (fieldW / 2);
        int centroYVermelho = alturaBloco / 2;

        JLabel txtTitulo = new JLabel("Novo Cadastro", SwingConstants.CENTER);
        txtTitulo.setForeground(Color.WHITE);
        txtTitulo.setFont(robotoBold64);
        txtTitulo.setBounds(fieldX - 50, centroYVermelho - 300, fieldW + 100, 250);
        ladoVermelho.add(txtTitulo);

        ladoVermelho.add(criarCampo("Inserir email", fieldX, centroYVermelho - 110, fieldW, fieldH));
        ladoVermelho.add(criarCampo("Inserir senha", fieldX, centroYVermelho - 40, fieldW, fieldH));
        ladoVermelho.add(criarCampo("confirmação da senha", fieldX, centroYVermelho + 30, fieldW, fieldH));

        JButton btnSeguir = new JButton("Seguir") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(26, 55, 94));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                super.paintComponent(g);
            }
        };
        btnSeguir.setBounds(fieldX, centroYVermelho + 130, fieldW, 75);
        btnSeguir.setForeground(Color.WHITE);
        btnSeguir.setFont(robotoSemiBold40);
        btnSeguir.setContentAreaFilled(false);
        btnSeguir.setBorderPainted(false);
        btnSeguir.setFocusPainted(false);
        ladoVermelho.add(btnSeguir);

        blocoCentral.add(ladoBranco);
        blocoCentral.add(ladoVermelho);
        painelFundo.add(blocoCentral);

        setVisible(true);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowDeiconified(java.awt.event.WindowEvent e) {
                // Quando o usuário clica no ícone para voltar:
                setExtendedState(JFrame.MAXIMIZED_BOTH); // Garante a tela cheia
                repaint(); // Redesenha as logos e o fundo
                revalidate();
            }
        });
    }

    // Auxiliar para criar os botões da barra superior
    private JButton criarBotaoBarra(String simbolo) {
        JButton btn = new JButton(simbolo);
        btn.setPreferredSize(new Dimension(45, 30));
        btn.setBackground(new Color(40, 40, 40));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(null);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        return btn;
    }

    private void carregarFontes() {
        try {
            File fontFile = new File("RobotoSerif-Bold.ttf");
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(baseFont);
            robotoBold64 = baseFont.deriveFont(Font.BOLD, 64f);
            robotoSemiBold40 = baseFont.deriveFont(Font.BOLD, 40f);
            robotoRegular20 = baseFont.deriveFont(Font.PLAIN, 20f);
        } catch (Exception e) {
            robotoBold64 = new Font("Arial", Font.BOLD, 64);
            robotoSemiBold40 = new Font("Arial", Font.BOLD, 40);
            robotoRegular20 = new Font("Arial", Font.PLAIN, 20);
        }
    }

    private JTextField criarCampo(String placeholder, int x, int y, int w, int h) {
        JTextField campo = new JTextField(placeholder);
        campo.setBounds(x, y, w, h);
        campo.setBackground(new Color(217, 217, 217));
        campo.setFont(robotoRegular20);
        campo.setForeground(Color.DARK_GRAY);
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
                    campo.setForeground(Color.DARK_GRAY);
                }
            }
        });
        return campo;
    }

    private void adicionarLogo(JPanel painel, String path, int xCentro, int yPos, double escala) {
        ImageIcon icon = new ImageIcon(path);
        int novaAlt = (int) (painel.getHeight() * escala);
        int novaLarg = (icon.getIconWidth() * novaAlt) / icon.getIconHeight();
        Image img = icon.getImage().getScaledInstance(novaLarg, novaAlt, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(img));
        label.setBounds(xCentro - (novaLarg / 2), yPos - (novaAlt / 2), novaLarg, novaAlt);
        painel.add(label);
    }

    public static void main(String[] args) {
        new Cadastro();
    }
}