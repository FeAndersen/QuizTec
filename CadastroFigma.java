import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CadastroFigma extends JFrame {

    public CadastroFigma() {
        // 1. Configurações da Janela - Modo Jogo (Tela Cheia)
        setUndecorated(true); 
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Toolkit tk = Toolkit.getDefaultToolkit();
        int larguraTela = (int) tk.getScreenSize().getWidth();
        int alturaTela = (int) tk.getScreenSize().getHeight();

        // --- PAINEL DE FUNDO (A imagem cinza fundo_etec.jpg) ---
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

        // --- CÁLCULO DO BLOCO CENTRAL PROPORCIONAL (80% da tela) ---
        int larguraBloco = (int) (larguraTela * 0.8);
        int alturaBloco = (int) (alturaTela * 0.8);
        int xCentro = (larguraTela - larguraBloco) / 2;
        int yCentro = (alturaTela - alturaBloco) / 2;

        JPanel blocoCentral = new JPanel();
        blocoCentral.setLayout(null);
        blocoCentral.setOpaque(false);
        blocoCentral.setBounds(xCentro, yCentro, larguraBloco, alturaBloco);

        // --- LADO ESQUERDO (BRANCO) ---
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

        // --- CORREÇÃO DAS LOGOS (Redimensionamento Proporcional) ---

        // 1. Logo ETEC (Logo_etec.jpg)
        ImageIcon iconeEtecOriginal = new ImageIcon("Logo_etec.jpg");
        // Redimensiona a logo para caber (ex: 30% da altura do bloco)
        int novaAltEtec = (int)(alturaBloco * 0.3);
        int novaLargEtec = (iconeEtecOriginal.getIconWidth() * novaAltEtec) / iconeEtecOriginal.getIconHeight();
        Image imgEtec = iconeEtecOriginal.getImage().getScaledInstance(novaLargEtec, novaAltEtec, Image.SCALE_SMOOTH);
        JLabel labelLogoEtec = new JLabel(new ImageIcon(imgEtec));
        
        // Centraliza Horizontalmente e posiciona no quadrante superior
        labelLogoEtec.setBounds((larguraBloco / 4) - (novaLargEtec / 2), 
                               (alturaBloco * 1 / 3) - (novaAltEtec / 2), 
                               novaLargEtec, novaAltEtec);
        ladoBranco.add(labelLogoEtec);


        // 2. Logo CPS (Logo_cps.jpg)
        ImageIcon iconeCpsOriginal = new ImageIcon("Logo_cps.jpg");
        // Redimensiona a logo para caber (ex: 20% da altura do bloco)
        int novaAltCps = (int)(alturaBloco * 0.2);
        int novaLargCps = (iconeCpsOriginal.getIconWidth() * novaAltCps) / iconeCpsOriginal.getIconHeight();
        Image imgCps = iconeCpsOriginal.getImage().getScaledInstance(novaLargCps, novaAltCps, Image.SCALE_SMOOTH);
        JLabel labelLogoCPS = new JLabel(new ImageIcon(imgCps));
        
        // Centraliza Horizontalmente e posiciona no quadrante inferior
        labelLogoCPS.setBounds((larguraBloco / 4) - (novaLargCps / 2), 
                              (alturaBloco * 4 / 6) - (novaAltCps / 2), 
                              novaLargCps, novaAltCps);
        ladoBranco.add(labelLogoCPS);


        // --- LADO DIREITO (VERMELHO SÓLIDO) ---
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

        // TÍTULO: Novo Cadastro (Roboto Serif BOLD 64)
        JLabel txtTitulo = new JLabel("Novo Cadastro");
        txtTitulo.setForeground(Color.WHITE);
        txtTitulo.setFont(new Font("Roboto Serif", Font.BOLD, 64)); 
        txtTitulo.setBounds((int)(larguraBloco*0.08), 60, 500, 80);
        ladoVermelho.add(txtTitulo);

        // CONFIGURAÇÃO DOS INPUTS COM PLACEHOLDER (Texto Centralizado)
        int fieldWidth = (int)(larguraBloco * 0.35);
        int fieldHeight = (int)(alturaTela * 0.05);
        int fieldX = (larguraBloco / 4) - (fieldWidth / 2); // Centraliza horizontalmente no lado vermelho

        JTextField campoEmail = criarCampo("Inserir email", fieldX, 220, fieldWidth, fieldHeight);
        ladoVermelho.add(campoEmail);

        JTextField campoSenha = criarCampo("Inserir senha", fieldX, 320, fieldWidth, fieldHeight);
        ladoVermelho.add(campoSenha);

        JTextField campoConfirma = criarCampo("confirmação da senha", fieldX, 420, fieldWidth, fieldHeight);
        ladoVermelho.add(campoConfirma);

        // --- NOVIDADE: BOTÃO AZUL ARREDONDADO (Style Figma) ---
        // Usamos um JButton customizado para desenhar a borda arredondada
        JButton btnSeguir = new JButton("Seguir") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(26, 55, 94)); // Azul escuro
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Arredondamento do botão
                super.paintComponent(g);
            }
        };
        btnSeguir.setBounds(fieldX, 550, fieldWidth, 80);
        btnSeguir.setForeground(Color.WHITE);
        // Fonte Roboto Serif SemiBold Simulado (BOLD) e Tamanho 40
        btnSeguir.setFont(new Font("Roboto Serif", Font.BOLD, 40)); 
        btnSeguir.setFocusPainted(false);
        btnSeguir.setContentAreaFilled(false); // Remove o fundo quadrado padrão do botão
        btnSeguir.setBorderPainted(false); // Remove a borda padrão do botão
        ladoVermelho.add(btnSeguir);

        blocoCentral.add(ladoBranco);
        blocoCentral.add(ladoVermelho);
        painelFundo.add(blocoCentral);

        setVisible(true);
    }

    // Função para criar campos com Placeholder (Texto que some ao clicar)
    private JTextField criarCampo(String placeholder, int x, int y, int w, int h) {
        JTextField campo = new JTextField(placeholder);
        campo.setBounds(x, y, w, h);
        campo.setBackground(new Color(217, 217, 217)); // Cinza claro
        campo.setFont(new Font("Roboto Serif", Font.PLAIN, 20)); 
        campo.setForeground(Color.DARK_GRAY);
        campo.setHorizontalAlignment(JTextField.CENTER); // Centraliza texto conforme sua imagem
        campo.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // Padding interno

        campo.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(Color.DARK_GRAY);
                }
            }
        });
        return campo;
    }

    public static void main(String[] args) {
        new CadastroFigma();
    }
}