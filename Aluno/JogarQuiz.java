package Aluno;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import util.Conexao;
import util.Sessao;


public class JogarQuiz extends JFrame {

    private Font robotoBold32, robotoBold24, robotoBold20, robotoBold28;
    private JLabel txtContador;
    private int questaoAtual = 0;
    private int pontuacao = 0;
    private boolean mostrarFeedback = false;
    private boolean acertou = false;
    private Image imagemAtual = null;
    private JPanel painelFoto;
    private JLabel lblPergunta;
    private JButton btnAjuda;
    private String[] textoAlternativas = {"", "", "", ""};
    private boolean[] corretasAlternativas = {false, false, false, false};
    private JButton [] btnAlternativas = new JButton[4];
    private List<Pergunta> perguntas = new ArrayList<>();

    static class Alternativa {
        String texto;
        boolean correta;
        Alternativa(String texto, boolean correta) {
            this.texto = texto;
            this.correta = correta;
        }
    }

    static class Pergunta {
        int id;
        String enunciado;
        Integer idImagem;
        List<Alternativa> alternativas = new ArrayList<>();
        Pergunta(int id, String enunciado, Integer idImagem) {
            this.id = id;
            this.enunciado = enunciado;
            this.idImagem = idImagem;
        }
    }

    public JogarQuiz() {

        try (Connection con = Conexao.conectar()) {
            PreparedStatement stmtP = con.prepareStatement(
                "SELECT p.id_pergunta, p.enunciado, p.id_imagem " +
                "FROM pergunta p " +
                "JOIN perguntas_sessao ps USING(id_pergunta) " +
                "WHERE ps.id_sessao = ? " +
                "ORDER BY ps.id_pergunta" 
            );

            stmtP.setInt(1, Sessao.idSessao);
            ResultSet rsP = stmtP.executeQuery();
            while (rsP.next()) {
                int idImg = rsP.getInt("id_imagem");
                Integer idImagem = rsP.wasNull() ? null : idImg;
                Pergunta perg = new Pergunta(
                    rsP.getInt("id_pergunta"),
                    rsP.getString("enunciado"),
                    idImagem
                );

                PreparedStatement stmtA = con.prepareStatement(
                    "SELECT resposta, correta FROM alternativa WHERE id_pergunta = ? ORDER BY id_alternativa"
                );

                stmtA.setInt(1, perg.id);
                ResultSet rsA = stmtA.executeQuery();

                while (rsA.next()) {
                    perg.alternativas.add(new Alternativa(
                        rsA.getString("resposta"),
                        rsA.getBoolean("correta")
                    ));
                }

                perguntas.add(perg);
            } 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro ao carregar o quiz: " + ex.getMessage());
            }


            if (perguntas.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Este jogo não tem perguntas cadastradas.");
                dispose();
                new SelecaoNivel().setVisible(true);
                return;
            }



        carregarFontes();
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit tk = Toolkit.getDefaultToolkit();
        int larguraTela = (int) tk.getScreenSize().getWidth();
        int alturaTela = (int) tk.getScreenSize().getHeight();

        JPanel painelFundo = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(new ImageIcon("images\\fundo_etec.jpg").getImage(), 0, 0, getWidth(), getHeight(), this);
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

        JLabel txtOla = new JLabel("Olá, " + Sessao.nomeUsuario, SwingConstants.RIGHT);
        txtOla.setForeground(Color.WHITE);
        txtOla.setFont(robotoBold24);
        txtOla.setBounds(larguraTela - 450, 0, 300, 80);
        header.add(txtOla);

        int larguraCard = (int) (larguraTela * 0.85);
        int alturaCard = (int) (alturaTela * 0.85);
        int xCard = (larguraTela - larguraCard) / 2;
        int yCard = 100;

        JPanel cardPrincipal = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(178, 0, 0));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 80, 80);
            }
        };
        cardPrincipal.setLayout(null);
        cardPrincipal.setOpaque(false);
        cardPrincipal.setBounds(xCard, yCard, larguraCard, alturaCard);

        txtContador = new JLabel("1/" + perguntas.size());
        txtContador.setForeground(Color.WHITE);
        txtContador.setFont(robotoBold28);
        txtContador.setBounds(40, 30, 100, 40);
        cardPrincipal.add(txtContador);

        // Ícone de Ajuda (Help)
        btnAjuda = new JButton();
        try {
            Image img = new ImageIcon("images\\help.png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            btnAjuda.setIcon(new ImageIcon(img));
        } catch (Exception e) {}
        btnAjuda.setBounds(larguraCard - 90, 30, 60, 60);
        btnAjuda.setContentAreaFilled(false);
        btnAjuda.setBorderPainted(false);
        btnAjuda.setFocusPainted(false);
        btnAjuda.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cardPrincipal.add(btnAjuda);

        btnAjuda.addActionListener(e -> {
            java.util.List<Integer> erradas = new java.util.ArrayList();
            for (int i = 0; i < 4; i++) {
                if(!corretasAlternativas[i] && btnAlternativas[i].isEnabled()) {
                    erradas.add(i);
                }
            }
            if (erradas.isEmpty()) return;

            int idx = erradas.get((int)(Math.random() * erradas.size()));
            textoAlternativas[idx] = "";
            btnAlternativas[idx].setBackground(new Color(80, 80, 80));
            btnAlternativas[idx].setEnabled(false);
            btnAlternativas[idx].repaint();

            btnAjuda.setEnabled(false);
        });

        int wImg = 550, hImg = 260, wOpcao = 450, hOpcao = 60;
        int espacoX = 50, espacoY = 25, hPergunta = 50;
        int margemAbaixoImg = 30, margemAbaixoPergunta = 30;

        int alturaTotalConteudo = hImg + margemAbaixoImg + hPergunta + margemAbaixoPergunta + (hOpcao * 2) + espacoY;
        int startYConteudo = (alturaCard - alturaTotalConteudo) / 2;
        int xImg = (larguraCard - wImg) / 2;
        int yImg = startYConteudo; 

        // Área da Foto Estática (No jogo real você carrega a imagem da pergunta aqui)
        painelFoto = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                if (imagemAtual != null) {
                    g.drawImage(imagemAtual, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(200, 200, 200));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        painelFoto.setBounds(xImg, yImg, wImg, hImg);
        cardPrincipal.add(painelFoto);

        // Pergunta
        int yPergunta = yImg + hImg + margemAbaixoImg;
        lblPergunta = new JLabel("", SwingConstants.CENTER);
        lblPergunta.setBounds(xImg - 100, yPergunta, wImg + 200, hPergunta);
        lblPergunta.setFont(robotoBold28);
        lblPergunta.setForeground(Color.WHITE);
        cardPrincipal.add(lblPergunta);

        // Alternativas
        int startXOpcoes = (larguraCard - (wOpcao * 2 + espacoX)) / 2;
        int startYOpcoes = yPergunta + hPergunta + margemAbaixoPergunta;

        btnAlternativas[0] = criarBotaoAlternativa("A", 0, startXOpcoes, startYOpcoes, wOpcao,hOpcao);
        btnAlternativas[1] = criarBotaoAlternativa("B", 1, startXOpcoes + wOpcao + espacoX, startYOpcoes, wOpcao, hOpcao);
        btnAlternativas[2] = criarBotaoAlternativa("C", 2, startXOpcoes, startYOpcoes + hOpcao + espacoY, wOpcao, hOpcao);
        btnAlternativas[3] = criarBotaoAlternativa("D", 3, startXOpcoes + wOpcao + espacoX, startYOpcoes + hOpcao + espacoY, wOpcao, hOpcao);
        for (JButton btn : btnAlternativas) cardPrincipal.add(btn);
        

        painelFundo.add(header);
        painelFundo.add(cardPrincipal);

        configurarGlassPaneFeedback(); // Prepara o painel de animação

        carregarQuestao();
        
        setVisible(true);
    }

    // ==========================================
    // LÓGICA DO FEEDBACK (CERTO / ERRADO)
    // ==========================================
    private void configurarGlassPaneFeedback() {
        JPanel glassPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                if (!mostrarFeedback) return;
                
                // Escurece a tela 
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());

                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(35f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); // Traço bem grosso
                
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                int size = 100; // Tamanho do símbolo

                if (acertou) {
                    // Desenha o V de Certo (Verde)
                    g2.setColor(new Color(30, 180, 80)); 
                    g2.drawLine(cx - size, cy, cx - (size/3), cy + size);
                    g2.drawLine(cx - (size/3), cy + size, cx + size, cy - size);
                } else {
                    // Desenha o X de Errado (Vermelho Claro)
                    g2.setColor(new Color(255, 50, 50));
                    g2.drawLine(cx - size, cy - size, cx + size, cy + size);
                    g2.drawLine(cx + size, cy - size, cx - size, cy + size);
                }
            }
        };
        glassPane.setOpaque(false);
        setGlassPane(glassPane);
    }

    private void processarResposta(boolean isCorreta) {
        acertou = isCorreta;

        if (isCorreta) pontuacao ++;

        for (JButton btn : btnAlternativas) btn.setEnabled(false);
        
        mostrarFeedback = true;
        getGlassPane().setVisible(true);
        getGlassPane().repaint();

        // Pausa a tela por 1.5 segundos para o aluno ver se acertou
        Timer timer = new Timer(1500, e -> {
            mostrarFeedback = false;
            getGlassPane().setVisible(false);
            questaoAtual++;
            
            // Avança questão ou finaliza
            if (questaoAtual < perguntas.size()) {
                carregarQuestao();
                } else {
                dispose();
                new ResultadoQuiz(pontuacao, perguntas.size()).setVisible(true);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void carregarQuestao () {
        Pergunta p = perguntas.get(questaoAtual);
        txtContador.setText((questaoAtual + 1) + "/" + perguntas.size());
        lblPergunta.setText(p.enunciado);

        imagemAtual = null;

        if(p.idImagem != null) {
            try (Connection con = Conexao.conectar()) {
                PreparedStatement stmt = con.prepareStatement(
                    "SELECT arquivo_imagem FROM imagem WHERE id_imagem = ?"
                );
                stmt.setInt(1, p.idImagem);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    imagemAtual = new ImageIcon(rs.getBytes("arquivo_imagem")).getImage();
                }
            } catch (Exception ex) {}
        }
        painelFoto.repaint();

        for (int i = 0; i < 4; i++) {
            if (i < p.alternativas.size()) {
                textoAlternativas[i] = p.alternativas.get(i).texto;
                corretasAlternativas[i] = p.alternativas.get(i).correta;
            } else {
                textoAlternativas[i] = "";
                corretasAlternativas[i] = false;
            }
            btnAlternativas[i].setEnabled(true);
            btnAjuda.setEnabled(true);
            btnAlternativas[i].repaint();
        }
    }

    // ==========================================
    // CRIAÇÃO DA UI
    // ==========================================
    private JButton criarBotaoAlternativa(String letra, int index, int x, int y, int w, int h) {
    Color corAzulEscuro = new Color(30, 55, 90);
    Color corHover = new Color(50, 75, 110);

    JButton b = new JButton() {
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.setColor(Color.WHITE);
            g2.setFont(robotoBold24);
            g2.drawString(letra, 30, 38);
            g2.setFont(robotoBold20);
            g2.drawString(textoAlternativas[index], 100, 38);
            g2.dispose();
        }
    };
    b.setBounds(x, y, w, h);
    b.setBackground(corAzulEscuro);
    b.setFocusable(false);
    b.setBorderPainted(false);
    b.setContentAreaFilled(false);
    b.setCursor(new Cursor(Cursor.HAND_CURSOR));

    b.addMouseListener(new MouseAdapter() {
        @Override public void mouseEntered(MouseEvent e) { b.setBackground(corHover); b.repaint(); }
        @Override public void mouseExited(MouseEvent e) { b.setBackground(corAzulEscuro); b.repaint(); }
    });

    b.addActionListener(e -> processarResposta(corretasAlternativas[index]));
    return b;
}


    private JButton criarBotaoControle(String texto, int x, int y) {
        Color corInvisivel = new Color(0, 0, 0, 0); 
        JButton b = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(getBackground()); g.fillRect(0, 0, getWidth(), getHeight()); super.paintComponent(g);
            }
        };
        if (texto.equals("↰")) {
            b.setFont(new Font("Segoe UI Symbol", Font.BOLD, 48));
            b.setBounds(x, 0, 60, 60); 
            b.addActionListener(e -> { this.dispose(); new SelecaoNivel().setVisible(true); });
        } else {
            b.setFont(new Font("Arial", Font.BOLD, 24));
            b.setBounds(x, y, 50, 40); 
            if (texto.equals("X")) b.addActionListener(e -> System.exit(0));
            else if (texto.equals("-")) b.addActionListener(e -> setState(Frame.ICONIFIED));
        }
        b.setMargin(new Insets(0,0,0,0)); b.setBackground(corInvisivel); b.setForeground(Color.WHITE);
        b.setFocusable(false); b.setBorder(null); b.setContentAreaFilled(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(texto.equals("X") ? new Color(232, 17, 35) : new Color(100,100,100)); }
            @Override public void mouseExited(MouseEvent e) { b.setBackground(corInvisivel); }
        });
        return b;
    }

    private void carregarFontes() {
        try {
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, new File("RobotoSerif-Bold.ttf"));
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(baseFont);
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

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new JogarQuiz()); }
}