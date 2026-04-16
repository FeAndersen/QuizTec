import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class ModeloDashboardEditado extends JFrame {

    private Font robotoBold32, robotoBold24, robotoRegular20;

    public ModeloDashboardEditado() {
        carregarFontes();

        // 1. Configurações da Janela (Modo Tela Cheia Sem Bordas)
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit tk = Toolkit.getDefaultToolkit();
        int larguraTela = (int) tk.getScreenSize().getWidth();
        int alturaTela = (int) tk.getScreenSize().getHeight();

        // 2. Painel de Fundo (Imagem do Laboratório)
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

        // 3. Header (Barra Superior Vermelha)
        JPanel header = new JPanel();
        header.setBackground(new Color(178, 0, 0)); // Vermelho Etec
        header.setBounds(0, 0, larguraTela, 80);
        header.setLayout(null);

        // --- Botões de Controle de Janela (Canto Direito) ---
        JButton btnFechar = criarBotaoControle("X", larguraTela - 50, 0);
        JButton btnMin = criarBotaoControle("-", larguraTela - 100, 0);
        header.add(btnFechar);
        header.add(btnMin);

        // --- Botão Voltar (Canto Esquerdo) ---
        JButton btnVoltar = criarBotaoControle("↰", 40, 100);
        header.add(btnVoltar);

        // Título QuizTec no Header
        JLabel txtQuizTec = new JLabel("QuizTec");
        txtQuizTec.setForeground(Color.WHITE);
        txtQuizTec.setFont(robotoBold32);
        txtQuizTec.setBounds(110, 0, 200, 80); // Afastado para não encostar na seta
        header.add(txtQuizTec);

        // Texto "Olá, ..." no Header
        JLabel txtOla = new JLabel("Olá, ...", SwingConstants.RIGHT);
        txtOla.setForeground(Color.WHITE);
        txtOla.setFont(robotoBold24);
        txtOla.setBounds(larguraTela - 450, 0, 300, 80);
        header.add(txtOla);

        // 4. Card Central (Bloco Vermelho Arredondado)
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

        // Conteúdo pontilhado de exemplo dentro do Card
        JLabel placeholder = new JLabel("........................................", SwingConstants.CENTER);
        placeholder.setForeground(Color.WHITE);
        placeholder.setFont(robotoBold32);
        placeholder.setBounds(0, 50, larguraCard, 50);
        cardPrincipal.add(placeholder);

        // 5. Montagem Final
        painelFundo.add(header);
        painelFundo.add(cardPrincipal);

        // 6. Listener Crítico: Garante que a tela volte a renderizar após minimizar
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

    // --- MÉTODO ENCAPSULADO: Fábrica de Botões (Com inteligência de tamanhos) ---
    private JButton criarBotaoControle(String texto, int x, int y) {
        Color corInvisivel = new Color(178, 0, 0); // Camuflado no header
        Color corHover;
        
        if (texto.equals("X")) {
            corHover = new Color(232, 17, 35); // Vermelho Windows
        } else {
            corHover = new Color(100, 100, 100); // Cinza para Minimizar e Voltar
        }

        // paintComponent sobrescrito evita bug do fundo transparente fantasma
        JButton b = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };

        // --- SEPARAÇÃO DE TAMANHOS E FONTES ---
        if (texto.equals("↰")) {
            // Símbolo nativo do Windows em tamanho grande
            b.setFont(new Font("Segoe UI Symbol", Font.BOLD, 48));
            // Botão maior (60x60) e centralizado (y=10) para caber a fonte 48 sem cortar
            b.setBounds(x, 4, 60, 60); 
        } else {
            // "X" e "-"
            b.setFont(new Font("Arial", Font.BOLD, 24));
            // Botão compacto
            b.setBounds(x, y, 50, 40); 
        }

        b.setMargin(new Insets(0, 0, 0, 0)); // Zera as margens para não gerar "..."
        b.setBackground(corInvisivel); 
        b.setForeground(Color.WHITE);

        // Remove decorações padrão do Swing
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setRolloverEnabled(false); 
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Ações de clique exclusivas para cada botão
        if (texto.equals("X")) {
            b.addActionListener(e -> System.exit(0));
        } else if (texto.equals("-")) {
            b.addActionListener(e -> setState(Frame.ICONIFIED));
        } else {
            b.addActionListener(e -> {
                System.out.println("Voltando para a tela anterior...");
                // Para voltar de verdade, descomente as linhas abaixo no futuro:
                // this.dispose();
                // new MenuPrincipal().setVisible(true);
            });
        }

        // Adiciona a animação de passar o mouse (Hover)
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

    // --- MÉTODO: Carregamento de Fontes Personalizadas ---
    private void carregarFontes() {
        try {
            File fontFile = new File("RobotoSerif-Bold.ttf");
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(baseFont);

            robotoBold32 = baseFont.deriveFont(Font.BOLD, 32f);
            robotoBold24 = baseFont.deriveFont(Font.BOLD, 24f);
            robotoRegular20 = baseFont.deriveFont(Font.PLAIN, 20f);
        } catch (Exception e) {
            System.out.println("Aviso: Arquivo .ttf não encontrado. Usando Arial como fallback.");
            robotoBold32 = new Font("Arial", Font.BOLD, 32);
            robotoBold24 = new Font("Arial", Font.BOLD, 24);
            robotoRegular20 = new Font("Arial", Font.PLAIN, 20);
        }
    }

    public static void main(String[] args) {
        new ModeloDashboardEditado();
    }
}