package Aluno;
import javax.swing.*;

import Professor.MenuProf;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SeusAlunos extends JFrame {

    private Font robotoBold36, robotoBold24, robotoBold18, robotoBold14;

    // ==========================================
    // MOCK DATABASE (Banco de Dados Simulado)
    // ==========================================
    public static class Aluno {
        public String nome;

        public Aluno(String nome) {
            this.nome = nome;
        }
    }
    
    public static List<Aluno> bancoDeAlunos = new ArrayList<>();

    public SeusAlunos() {
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
                ImageIcon imagemFundo = new ImageIcon("QuizTec\\images\\fundo_etec.jpg");
                g.drawImage(imagemFundo.getImage(), 0, 0, getWidth(), getHeight(), this);
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

        JLabel txtOla = new JLabel("Olá, professor", SwingConstants.RIGHT);
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

        JLabel lblTitulo = new JLabel("Seus Alunos", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(robotoBold36);
        lblTitulo.setBounds(0, 40, larguraCard, 40);
        cardPrincipal.add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("Você tem " + bancoDeAlunos.size() + " alunos no QuizTec", SwingConstants.CENTER);
        lblSubtitulo.setForeground(Color.WHITE);
        lblSubtitulo.setFont(robotoBold24);
        lblSubtitulo.setBounds(0, 85, larguraCard, 30);
        cardPrincipal.add(lblSubtitulo);

        boolean[] isHovered = {false}; 
        
        JButton btnAddAluno = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (isHovered[0]) {
                    g2.setColor(new Color(0, 160, 80, 50)); 
                    g2.fillOval(2, 2, getWidth() - 4, getHeight() - 4);
                }

                g2.setColor(new Color(0, 160, 80)); 
                g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawOval(2, 2, getWidth() - 4, getHeight() - 4);
                
                try {
                    ImageIcon icon = new ImageIcon("QuizTec\\images\\person_add.png");
                    Image img = icon.getImage();
                    int iconDim = 36; 
                    int cx = (getWidth() - iconDim) / 2;
                    int cy = (getHeight() - iconDim) / 2;
                    g2.drawImage(img, cx, cy, iconDim, iconDim, null);
                } catch (Exception e) {}
                
                g2.dispose();
            }
        };
        btnAddAluno.setBounds(larguraCard - 100, 30, 56, 56);
        btnAddAluno.setContentAreaFilled(false);
        btnAddAluno.setBorderPainted(false);
        btnAddAluno.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnAddAluno.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { isHovered[0] = true; btnAddAluno.repaint(); }
            @Override public void mouseExited(MouseEvent e) { isHovered[0] = false; btnAddAluno.repaint(); }
        });
        
        btnAddAluno.addActionListener(e -> abrirPopupAdicionarAluno());
        cardPrincipal.add(btnAddAluno);

        JPanel linha = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        linha.setBounds((larguraCard - 800)/2, 130, 800, 3);
        linha.setOpaque(false);
        cardPrincipal.add(linha);

        int wLista = (int) (larguraCard * 0.90); 
        int hLista = alturaCard - 180;
        int xLista = (larguraCard - wLista) / 2;

        JPanel painelAlunos = new JPanel();
        painelAlunos.setLayout(null); 
        painelAlunos.setOpaque(false);

        int espacoX = 40; 
        int espacoY = 20;
        int hCard = 60; 
        int wCard = (wLista - espacoX) / 2; 

        int maxY = 0; 

        for (int i = 0; i < bancoDeAlunos.size(); i++) {
            int linhaAtual = i / 2; 
            int colunaAtual = i % 2; 
            
            int xAluno = colunaAtual * (wCard + espacoX);
            int yAluno = 10 + linhaAtual * (hCard + espacoY);
            
            painelAlunos.add(criarPainelAluno(bancoDeAlunos.get(i), xAluno, yAluno, wCard, hCard));
            
            maxY = yAluno + hCard + 20; 
        }

        painelAlunos.setPreferredSize(new Dimension(wLista, Math.max(maxY, hLista)));

        JScrollPane scrollPane = new JScrollPane(painelAlunos);
        scrollPane.setBounds(xLista, 160, wLista, hLista);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); 
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0,0)); 
        
        cardPrincipal.add(scrollPane);
        painelFundo.add(header);
        painelFundo.add(cardPrincipal);

        prepararPopupGlassPane();
        setVisible(true);
    }

    // ==========================================
    // POP-UPS DE AÇÃO
    // ==========================================
    private void prepararPopupGlassPane() {
        JPanel glassPane = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 180));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        glassPane.setOpaque(false);
        setGlassPane(glassPane);
    }

    private void abrirPopupAdicionarAluno() {
        abrirPopup("Adicionar Novo Aluno", "Nome Completo do Aluno", true);
    }

    private void abrirPopupRenomearAluno(String nomeAntigo) {
        abrirPopup("Renomear Aluno", nomeAntigo, false);
    }

    // Função genérica que serve tanto para Adicionar quanto Renomear
    private void abrirPopup(String titulo, String textoCampo, boolean isAdicionar) {
        JPanel glassPane = (JPanel) getGlassPane();
        glassPane.removeAll();

        int wPopup = 500;
        int hPopup = 350;
        int xPopup = (getWidth() - wPopup) / 2;
        int yPopup = (getHeight() - hPopup) / 2;

        JPanel popupCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(178, 0, 0)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.setColor(new Color(0, 0, 0, 50));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 40, 40);
                g2.dispose();
            }
        };
        popupCard.setLayout(null);
        popupCard.setBounds(xPopup, yPopup, wPopup, hPopup);
        popupCard.setOpaque(false);

        JButton btnFecharPopup = new JButton("X");
        btnFecharPopup.setFont(new Font("Arial", Font.BOLD, 24));
        btnFecharPopup.setForeground(Color.WHITE);
        btnFecharPopup.setBounds(wPopup - 60, 20, 40, 40);
        btnFecharPopup.setContentAreaFilled(false);
        btnFecharPopup.setBorderPainted(false);
        // --- AS DUAS LINHAS GARANTEM O X NÃO VIRAR "..." ---
        btnFecharPopup.setMargin(new Insets(0, 0, 0, 0)); 
        btnFecharPopup.setBorder(null);
        // --- FIM DAS DUAS LINHAS SALVADORAS ---
        btnFecharPopup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFecharPopup.addActionListener(e -> glassPane.setVisible(false));
        popupCard.add(btnFecharPopup);

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(robotoBold24);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(0, 60, wPopup, 40);
        popupCard.add(lblTitulo);

        JTextField txtNomeAluno = new JTextField(textoCampo);
        txtNomeAluno.setHorizontalAlignment(JTextField.CENTER);
        txtNomeAluno.setFont(robotoBold18);
        txtNomeAluno.setBackground(new Color(220, 220, 220));
        txtNomeAluno.setBounds((wPopup - 350)/2, 130, 350, 45);
        txtNomeAluno.setBorder(null);
        
        if (isAdicionar) {
            txtNomeAluno.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) { if(txtNomeAluno.getText().equals(textoCampo)) txtNomeAluno.setText(""); }
                public void focusLost(FocusEvent e) { if(txtNomeAluno.getText().isEmpty()) txtNomeAluno.setText(textoCampo); }
            });
        }
        popupCard.add(txtNomeAluno);

        JButton btnConfirmar = new JButton("Confirmar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 55, 90)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };
        btnConfirmar.setFont(robotoBold24);
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setBounds((wPopup - 250)/2, 210, 250, 50);
        btnConfirmar.setContentAreaFilled(false);
        btnConfirmar.setBorderPainted(false);
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnConfirmar.addActionListener(e -> {
            String nome = txtNomeAluno.getText();
            if(!nome.isEmpty() && !nome.equals("Nome Completo do Aluno")) {
                if (isAdicionar) {
                    bancoDeAlunos.add(new Aluno(nome));
                } else {
                    for (Aluno a : bancoDeAlunos) {
                        if (a.nome.equals(textoCampo)) { // Procura pelo nome antigo
                            a.nome = nome; // Substitui pelo novo
                            break;
                        }
                    }
                }
                glassPane.setVisible(false);
                this.dispose();
                new SeusAlunos().setVisible(true);
            }
        });
        
        popupCard.add(btnConfirmar);
        glassPane.add(popupCard);
        glassPane.setVisible(true);
    }

    private JPanel criarPainelAluno(Aluno aluno, int x, int y, int w, int h) {
        Color corAzulEscuro = new Color(30, 55, 90);

        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), h, h); 
            }
        };
        p.setLayout(null);
        p.setBounds(x, y, w, h);
        p.setOpaque(false);

        JLabel lblNome = new JLabel(aluno.nome);
        lblNome.setFont(robotoBold18); 
        lblNome.setForeground(corAzulEscuro);
        lblNome.setBounds(30, 0, w - 180, h); 
        p.add(lblNome);

        int btnW = 60;
        int btnH = h;
        int startXBtns = w - (btnW * 2) - 20;

        p.add(criarBotaoAcao("Renomear", "QuizTec\\images\\rename.png", startXBtns, 0, btnW, btnH));
        p.add(criarBotaoAcao("Remover", "QuizTec\\images\\person_remove.png", startXBtns + btnW, 0, btnW, btnH));

        return p;
    }

    private JButton criarBotaoAcao(String texto, String caminhoIcone, int x, int y, int w, int h) {
        Color corAzulEscuro = new Color(30, 55, 90);
        Color corHover = new Color(240, 240, 240);

        JButton b = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                int cx = getWidth() / 2;
                int cy = getHeight() / 2 - 8;

                try {
                    Image img = new ImageIcon(caminhoIcone).getImage();
                    int iconDim = 26; 
                    g2.drawImage(img, cx - (iconDim/2), cy - (iconDim/2), iconDim, iconDim, null);
                } catch (Exception e) {}

                g2.setColor(corAzulEscuro);
                g2.setFont(robotoBold14.deriveFont(10f)); 
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(texto, (getWidth() - fm.stringWidth(texto)) / 2, getHeight() - 8);

                g2.dispose();
            }
        };

        b.setBounds(x, y, w, h);
        b.setBackground(new Color(0,0,0,0)); 
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(corHover); b.repaint(); }
            @Override public void mouseExited(MouseEvent e) { b.setBackground(new Color(0,0,0,0)); b.repaint(); }
        });
        
        // Ações de Remover e Renomear
        b.addActionListener(e -> {
            String nomeProcurado = ((JLabel)b.getParent().getComponent(0)).getText();
            
            if(texto.equals("Remover")) {
                bancoDeAlunos.removeIf(a -> a.nome.equals(nomeProcurado));
                SwingUtilities.getWindowAncestor(b).dispose();
                new SeusAlunos().setVisible(true);
            } 
            else if(texto.equals("Renomear")) {
                abrirPopupRenomearAluno(nomeProcurado); // Abre o modal para digitar o novo nome
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
            b.setBounds(x, 0, 60, 80); 
            b.addActionListener(e -> { this.dispose(); new MenuProf().setVisible(true); });
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
        
        // As duas linhas de código que matam as bordas nativas brancas do Windows/Java
        b.setFocusable(false);
        b.setBorder(BorderFactory.createEmptyBorder()); 
        
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
            robotoBold36 = baseFont.deriveFont(Font.BOLD, 36f);
            robotoBold24 = baseFont.deriveFont(Font.BOLD, 24f);
            robotoBold18 = baseFont.deriveFont(Font.BOLD, 18f);
            robotoBold14 = baseFont.deriveFont(Font.BOLD, 14f);
        } catch (Exception e) {
            robotoBold36 = new Font("Arial", Font.BOLD, 36);
            robotoBold24 = new Font("Arial", Font.BOLD, 24);
            robotoBold18 = new Font("Arial", Font.BOLD, 18);
            robotoBold14 = new Font("Arial", Font.BOLD, 14);
        }
    }

    public static void main(String[] args) {
        if (SeusAlunos.bancoDeAlunos.isEmpty()) {
            SeusAlunos.bancoDeAlunos.add(new Aluno("Luan Silva Oliveira"));
        }
        SwingUtilities.invokeLater(() -> new SeusAlunos());
    }
}