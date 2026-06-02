package Professor;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CriarPerguntas extends JFrame {

    private Font robotoBold32, robotoBold24, robotoBold20, robotoBold28;
    
    // --- VARIÁVEIS DE ESTADO DO JOGO ---
    private String nivelDificuldade; // RECEBE O NÍVEL AQUI
    private List<Pergunta> listaPerguntas = new ArrayList<>();
    private int indiceAtual = 0;
    
    // --- REFERÊNCIAS DA TELA ---
    private int opcaoCorreta = -1; 
    private JButton[] btnBolinhas = new JButton[4];
    private JTextField txtPergunta;
    private JTextField[] txtOpcoes = new JTextField[4];
    private JButton btnCarregarFoto;
    private JLabel txtContador;
    private Image imagemAtualSelecionada = null; 

    class Pergunta {
        String fotoCaminho = "";
        String enunciado = "";
        String[] alternativas = {"", "", "", ""};
        int correta = -1;
    }

    // CONSTRUTOR AGORA EXIGE O NÍVEL
    public CriarPerguntas(String nivel) {
        this.nivelDificuldade = nivel; // Salva o nível escolhido para usar depois

        carregarFontes();
        listaPerguntas.add(new Pergunta());

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
        txtQuizTec.setFont(robotoBold32);
        txtQuizTec.setBounds(110, 0, 200, 80);
        header.add(txtQuizTec);

        JLabel txtOla = new JLabel("Olá, professor", SwingConstants.RIGHT);
        txtOla.setForeground(Color.WHITE);
        txtOla.setFont(robotoBold24);
        txtOla.setBounds(larguraTela - 450, 0, 300, 80);
        header.add(txtOla);

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

        txtContador = new JLabel("1/1");
        txtContador.setForeground(Color.WHITE);
        txtContador.setFont(robotoBold28);
        txtContador.setBounds(40, 30, 100, 40);
        cardPrincipal.add(txtContador);

        JButton btnConcluir = criarBotaoImagem("QuizTec\\images\\concluir.png", larguraCard - 100, 20, 60, 60);
        btnConcluir.addActionListener(e -> {
            salvarEstadoAtual();
            abrirPopupSalvarJogo();
        });
        cardPrincipal.add(btnConcluir);

        int wImg = 550, hImg = 260, wOpcao = 450, hOpcao = 60;
        int espacoX = 50, espacoY = 25, hPergunta = 50;
        int margemAbaixoImg = 30, margemAbaixoPergunta = 30;

        int alturaTotalConteudo = hImg + margemAbaixoImg + hPergunta + margemAbaixoPergunta + (hOpcao * 2) + espacoY;
        int startYConteudo = (alturaCard - alturaTotalConteudo) / 2;

        int xImg = (larguraCard - wImg) / 2;
        int yImg = startYConteudo; 

        btnCarregarFoto = new JButton("Carregar foto") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                
                if (imagemAtualSelecionada != null) {
                    int imgW = imagemAtualSelecionada.getWidth(this);
                    int imgH = imagemAtualSelecionada.getHeight(this);

                    // Calcula escala mantendo proporção (contain)
                    double escala = Math.min(
                        (double) getWidth()  / imgW,
                        (double) getHeight() / imgH
                    );

                    int drawW = (int) (imgW * escala);
                    int drawH = (int) (imgH * escala);

                    // Centraliza
                    int drawX = (getWidth()  - drawW) / 2;
                    int drawY = (getHeight() - drawH) / 2;

                    // Fundo neutro atrás da imagem (para as "barras" do letterbox)
                    g2.setColor(new Color(30, 30, 30));
                    g2.fillRect(0, 0, getWidth(), getHeight());

                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2.drawImage(imagemAtualSelecionada, drawX, drawY, drawW, drawH, this);
                } else {
                    g2.setColor(new Color(200, 200, 200));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    super.paintComponent(g);
                }

                g2.setColor(new Color(50, 150, 255));
                g2.setStroke(new BasicStroke(6f));
                g2.drawRect(3, 3, getWidth() - 6, getHeight() - 6);
                g2.dispose();
            }
        };
        btnCarregarFoto.setBounds(xImg, yImg, wImg, hImg);
        btnCarregarFoto.setFont(robotoBold24);
        btnCarregarFoto.setForeground(new Color(50, 50, 50));
        btnCarregarFoto.setContentAreaFilled(false);
        btnCarregarFoto.setFocusPainted(false);
        btnCarregarFoto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCarregarFoto.addActionListener(e -> escolherImagem());
        cardPrincipal.add(btnCarregarFoto);

        int wSeta = 60, hSeta = 80;
        int ySeta = yImg + (hImg - hSeta) / 2;

        JButton btnEsquerda = criarBotaoSeta(true, xImg - wSeta - 60, ySeta, wSeta, hSeta);
        JButton btnDireita = criarBotaoSeta(false, xImg + wImg + 60, ySeta, wSeta, hSeta);
        cardPrincipal.add(btnEsquerda);
        cardPrincipal.add(btnDireita);

        int yPergunta = yImg + hImg + margemAbaixoImg;
        txtPergunta = criarCampoTextoTransparente("Escreva sua pergunta", xImg - 100, yPergunta, wImg + 200, hPergunta, robotoBold28);
        txtPergunta.setHorizontalAlignment(JTextField.CENTER);
        cardPrincipal.add(txtPergunta);

        int startXOpcoes = (larguraCard - (wOpcao * 2 + espacoX)) / 2;
        int startYOpcoes = yPergunta + hPergunta + margemAbaixoPergunta;

        cardPrincipal.add(criarPainelOpcao("A", startXOpcoes, startYOpcoes, wOpcao, hOpcao, 0));
        cardPrincipal.add(criarPainelOpcao("B", startXOpcoes + wOpcao + espacoX, startYOpcoes, wOpcao, hOpcao, 1));
        cardPrincipal.add(criarPainelOpcao("C", startXOpcoes, startYOpcoes + hOpcao + espacoY, wOpcao, hOpcao, 2));
        cardPrincipal.add(criarPainelOpcao("D", startXOpcoes + wOpcao + espacoX, startYOpcoes + hOpcao + espacoY, wOpcao, hOpcao, 3));

        painelFundo.add(header);
        painelFundo.add(cardPrincipal);

        prepararPopupGlassPane(); 

        setVisible(true);
    }

    private void salvarEstadoAtual() {
        Pergunta p = listaPerguntas.get(indiceAtual);
        if (!txtPergunta.getText().equals("Escreva sua pergunta")) { p.enunciado = txtPergunta.getText(); } else { p.enunciado = ""; }
        for (int i = 0; i < 4; i++) {
            if (!txtOpcoes[i].getText().equals("escreva a opção")) { p.alternativas[i] = txtOpcoes[i].getText(); } else { p.alternativas[i] = ""; }
        }
        p.correta = opcaoCorreta;
    }

    private void atualizarTela() {
        Pergunta p = listaPerguntas.get(indiceAtual);
        txtPergunta.setText(p.enunciado.isEmpty() ? "Escreva sua pergunta" : p.enunciado);
        txtPergunta.setForeground(p.enunciado.isEmpty() ? new Color(255,255,255,150) : Color.WHITE);

        for (int i = 0; i < 4; i++) {
            txtOpcoes[i].setText(p.alternativas[i].isEmpty() ? "escreva a opção" : p.alternativas[i]);
            txtOpcoes[i].setForeground(p.alternativas[i].isEmpty() ? new Color(255,255,255,150) : Color.WHITE);
        }

        opcaoCorreta = p.correta;
        for (JButton b : btnBolinhas) { if (b != null) b.repaint(); }

        if (!p.fotoCaminho.isEmpty()) { imagemAtualSelecionada = new ImageIcon(p.fotoCaminho).getImage(); } 
        else { imagemAtualSelecionada = null; }
        
        btnCarregarFoto.repaint();
        txtContador.setText((indiceAtual + 1) + "/" + listaPerguntas.size());
    }

    private void escolherImagem() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecione uma imagem para a pergunta");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imagens (JPG, PNG)", "jpg", "png", "jpeg"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File arquivoSelecionado = fileChooser.getSelectedFile();
            listaPerguntas.get(indiceAtual).fotoCaminho = arquivoSelecionado.getAbsolutePath();
            atualizarTela();
        }
    }

    private void navegar(boolean paraEsquerda) {
        salvarEstadoAtual();
        if (paraEsquerda) {
            if (indiceAtual > 0) { indiceAtual--; atualizarTela(); }
        } else {
            if (indiceAtual == listaPerguntas.size() - 1) { listaPerguntas.add(new Pergunta()); }
            indiceAtual++;
            atualizarTela();
        }
    }

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

    private void abrirPopupSalvarJogo() {
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
        // --- AS DUAS LINHAS FAZEM O X NÃO VIRAR "..." ---
        btnFecharPopup.setMargin(new Insets(0, 0, 0, 0)); 
        btnFecharPopup.setBorder(null);
        // --- FIM DAS DUAS LINHAS  ---
        btnFecharPopup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFecharPopup.addActionListener(e -> glassPane.setVisible(false));
        popupCard.add(btnFecharPopup);

        JLabel lblTitulo = new JLabel("De um nome ao jogo", SwingConstants.CENTER);
        lblTitulo.setFont(robotoBold28);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(0, 60, wPopup, 40);
        popupCard.add(lblTitulo);

        JTextField txtNomeJogo = new JTextField("Inserir nome");
        txtNomeJogo.setHorizontalAlignment(JTextField.CENTER);
        txtNomeJogo.setFont(robotoBold20);
        txtNomeJogo.setBackground(new Color(220, 220, 220));
        txtNomeJogo.setBounds((wPopup - 350)/2, 130, 350, 45);
        txtNomeJogo.setBorder(null);
        txtNomeJogo.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { if(txtNomeJogo.getText().equals("Inserir nome")) txtNomeJogo.setText(""); }
            public void focusLost(FocusEvent e) { if(txtNomeJogo.getText().isEmpty()) txtNomeJogo.setText("Inserir nome"); }
        });
        popupCard.add(txtNomeJogo);

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
            String nomeJogo = txtNomeJogo.getText();
            System.out.println("====== JOGO SALVO ======");
            System.out.println("Nome: " + nomeJogo);
            System.out.println("Nível de Dificuldade: " + nivelDificuldade); // <--- AQUI EXIBIMOS O DADO QUE VIAJOU
            System.out.println("Total de Perguntas: " + listaPerguntas.size());
            
            glassPane.setVisible(false);
            JOptionPane.showMessageDialog(this, "Jogo salvo com sucesso!\nNível: " + nivelDificuldade);
        });
        
        popupCard.add(btnConfirmar);
        glassPane.add(popupCard);
        glassPane.setVisible(true);
    }

    private JPanel criarPainelOpcao(String letra, int x, int y, int w, int h, int index) {
        JPanel painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 55, 90));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        painel.setLayout(null);
        painel.setBounds(x, y, w, h);
        painel.setOpaque(false);

        JLabel lblLetra = new JLabel(letra, SwingConstants.CENTER);
        lblLetra.setFont(robotoBold24);
        lblLetra.setForeground(Color.WHITE);
        lblLetra.setBounds(10, 0, 50, h);
        painel.add(lblLetra);

        txtOpcoes[index] = criarCampoTextoTransparente("escreva a opção", 60, 0, w - 130, h, robotoBold20);
        painel.add(txtOpcoes[index]);

        JButton btnBolinha = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int d = 24; 
                int cx = (getWidth() - d) / 2;
                int cy = (getHeight() - d) / 2;

                if (opcaoCorreta == index) {
                    g2.setColor(Color.WHITE);
                    g2.fillOval(cx, cy, d, d);
                    
                    g2.setColor(new Color(178, 0, 0));
                    g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(cx + 6, cy + 12, cx + 10, cy + 16);
                    g2.drawLine(cx + 10, cy + 16, cx + 18, cy + 8);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawOval(cx, cy, d, d);
                }
                g2.dispose();
            }
        };
        btnBolinha.setBounds(w - 60, 0, 60, h);
        btnBolinha.setContentAreaFilled(false);
        btnBolinha.setBorderPainted(false);
        btnBolinha.setFocusPainted(false);
        btnBolinha.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnBolinha.addActionListener(e -> {
            opcaoCorreta = index;
            for (JButton b : btnBolinhas) { if(b != null) b.repaint(); }
        });
        
        btnBolinhas[index] = btnBolinha;
        painel.add(btnBolinha);

        return painel;
    }

    private JButton criarBotaoSeta(boolean esquerda, int x, int y, int w, int h) {
        JButton b = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                Path2D seta = new Path2D.Double();
                if (esquerda) {
                    seta.moveTo(w - 10, 10);
                    seta.lineTo(10, h / 2);
                    seta.lineTo(w - 10, h - 10);
                } else {
                    seta.moveTo(10, 10);
                    seta.lineTo(w - 10, h / 2);
                    seta.lineTo(10, h - 10);
                }
                g2.draw(seta);
                g2.dispose();
            }
        };
        b.setBounds(x, y, w, h);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> navegar(esquerda));
        return b;
    }

    private JButton criarBotaoImagem(String caminho, int x, int y, int w, int h) {
        JButton b = new JButton();
        try {
            ImageIcon icon = new ImageIcon(caminho);
            Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            b.setIcon(new ImageIcon(img));
        } catch (Exception e) {}
        b.setBounds(x, y, w, h);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JTextField criarCampoTextoTransparente(String placeholder, int x, int y, int w, int h, Font font) {
        JTextField campo = new JTextField(placeholder);
        campo.setBounds(x, y, w, h);
        campo.setFont(font);
        campo.setForeground(new Color(255, 255, 255, 150));
        campo.setCaretColor(Color.WHITE); 
        campo.setOpaque(false); 
        campo.setBorder(null);

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(Color.WHITE);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().trim().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(new Color(255, 255, 255, 150));
                }
            }
        });
        return campo;
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
            b.addActionListener(e -> { this.dispose(); new CriarSelecaoNivel().setVisible(true); });
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
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- ADICIONE ESTAS 3 LINHAS ---
        b.setFocusPainted(false); // Desliga o desenho do foco ao clicar
        b.setFocusable(false);    // Impede que o botão receba foco pelo teclado
        b.setBorder(null);        // Garante que não sobrou nenhuma borda do Windows

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
            robotoBold28 = baseFont.deriveFont(Font.BOLD, 28f);
            robotoBold24 = baseFont.deriveFont(Font.BOLD, 24f);
            robotoBold20 = baseFont.deriveFont(Font.PLAIN, 20f); 
        } catch (Exception e) {
            robotoBold32 = new Font("Arial", Font.BOLD, 32);
            robotoBold28 = new Font("Arial", Font.BOLD, 28);
            robotoBold24 = new Font("Arial", Font.BOLD, 24);
            robotoBold20 = new Font("Arial", Font.PLAIN, 20);
        }
    }

    public static void main(String[] args) {
        // Agora, para rodar só essa tela direto para testar, é preciso passar um nível
        SwingUtilities.invokeLater(() -> new CriarPerguntas("Fácil (Teste)").setVisible(true));
    }
}