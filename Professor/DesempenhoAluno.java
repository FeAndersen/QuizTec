package Professor;
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

public class DesempenhoAluno extends JFrame {

    private Font robotoBold36, robotoBold24, robotoBold14, robotoBold10;

    static class Partida {
        String nomeJogo, nivel, dataHoraFim;
        int pontuacaoTotal, totalPerguntas, acertos;

        Partida(String nomeJogo, String nivel, int pontuacaoTotal, int totalPerguntas, String dataHoraFim, int acertos) {
            this.nomeJogo = nomeJogo;
            this.nivel = nivel;
            this.pontuacaoTotal = pontuacaoTotal;
            this.totalPerguntas = totalPerguntas;
            this.dataHoraFim = dataHoraFim;
            this.acertos = acertos;
        }
    }

    public DesempenhoAluno(int idAluno, String nomeAluno) {
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
        txtQuizTec.setFont(robotoBold36);
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
        cardPrincipal.setBounds(xCard, 100, larguraCard, alturaCard);

        JLabel lblTitulo = new JLabel("Desempenho de " + nomeAluno, SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(robotoBold36);
        lblTitulo.setBounds(0, 40, larguraCard, 40);
        cardPrincipal.add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("Resultados de quizzes deste aluno", SwingConstants.CENTER);
        lblSubtitulo.setForeground(Color.WHITE);
        lblSubtitulo.setFont(robotoBold24);
        lblSubtitulo.setBounds(0, 85, larguraCard, 30);
        cardPrincipal.add(lblSubtitulo);

        JPanel linha = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(Color.WHITE); g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        linha.setBounds((larguraCard - 800) / 2, 130, 800, 3);
        linha.setOpaque(false);
        cardPrincipal.add(linha);

        int wLista = (int) (larguraCard * 0.85);
        int hLista = alturaCard - 180;
        int xLista = (larguraCard - wLista) / 2;

        JPanel painelHistorico = new JPanel();
        painelHistorico.setLayout(null);
        painelHistorico.setOpaque(false);

        int yItem = 10;
        int hItem = 80;
        int espacoItem = 15;

        List<Partida> partidas = new ArrayList<>();

        try (Connection con = Conexao.conectar()) {
            PreparedStatement stmt = con.prepareStatement(
                "SELECT s.nome_sessao, d.nome_dificuldade, p.acertos, p.pontuacao_total, s.quantidade_perguntas, p.data_hora_fim " +
                "FROM partida p " +
                "JOIN sessao s USING(id_sessao) " +
                "JOIN dificuldade d USING(id_dificuldade) " +
                "WHERE p.id_aluno = ? AND p.status_partida = 'finalizado' " +
                "ORDER BY p.data_hora_fim DESC"
            );
            stmt.setInt(1, idAluno);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                partidas.add(new Partida(
                    rs.getString("nome_sessao"),
                    rs.getString("nome_dificuldade"),
                    rs.getInt("pontuacao_total"),
                    rs.getInt("quantidade_perguntas"),
                    rs.getString("data_hora_fim"),
                    rs.getInt("acertos")
                ));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar desempenho: " + ex.getMessage());
        }

        if (partidas.isEmpty()) {
            JLabel lblVazio = new JLabel("Este aluno ainda não jogou nenhum quiz.", SwingConstants.CENTER);
            lblVazio.setFont(robotoBold24);
            lblVazio.setForeground(Color.WHITE);
            lblVazio.setBounds(0, hLista / 2 - 20, wLista, 40);
            painelHistorico.add(lblVazio);
        }

        for (Partida partida : partidas) {
            String icone;
            switch (partida.nivel) {
                case "MEDIO": icone = "images\\biotech.png"; break;
                case "DIFICIL": icone = "images\\fluid_med.png"; break;
                default: icone = "images\\labs.png"; break;
            }
            int acertos = partida.acertos;
            painelHistorico.add(criarItemHistorico(
                partida.nomeJogo, icone, acertos, partida.totalPerguntas,
                partida.pontuacaoTotal, partida.dataHoraFim, 0, yItem, wLista, hItem
            ));
            yItem += hItem + espacoItem;
        }

        painelHistorico.setPreferredSize(new Dimension(wLista, Math.max(yItem, hLista)));

        JScrollPane scrollPane = new JScrollPane(painelHistorico);
        scrollPane.setBounds(xLista, 150, wLista, hLista);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

        cardPrincipal.add(scrollPane);
        painelFundo.add(header);
        painelFundo.add(cardPrincipal);
        setVisible(true);
    }

    private JPanel criarItemHistorico(String nomeJogo, String iconePath, int acertos, int total, int pontos, String dataHora, int x, int y, int w, int h) {
        Color corAzulEscuro = new Color(30, 55, 90);

        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                try { g2.drawImage(new ImageIcon(iconePath).getImage(), 20, 10, 60, 60, null); } catch (Exception e) {}
                g2.setColor(corAzulEscuro);
                int xAcertos = getWidth() - 320;
                try { g2.drawImage(new ImageIcon("images\\insert_chart.png").getImage(), xAcertos, 10, 40, 40, null); } catch (Exception e) {}
                g2.setFont(robotoBold10);
                g2.drawString("Acertos", xAcertos, 65);
                g2.setFont(robotoBold24);
                g2.drawString(acertos + " / " + total, xAcertos + 55, 40);
                int xPontos = getWidth() - 170;
                try { g2.drawImage(new ImageIcon("images\\leaderboard.png").getImage(), xPontos, 10, 40, 40, null); } catch (Exception e) {}
                g2.setFont(robotoBold10);
                g2.drawString("Pontuação", xPontos - 5, 65);
                g2.setFont(robotoBold24);
                g2.drawString(String.valueOf(pontos), xPontos + 55, 40);
                g2.dispose();
            }
        };
        p.setLayout(null);
        p.setBounds(x, y, w, h);
        p.setOpaque(false);

        JLabel lblNome = new JLabel(nomeJogo);
        lblNome.setFont(robotoBold24);
        lblNome.setForeground(corAzulEscuro);
        lblNome.setBounds(100, 15, w - 400, 30);
        p.add(lblNome);

        JLabel lblData = new JLabel("jogado em " + dataHora);
        lblData.setFont(robotoBold14);
        lblData.setForeground(corAzulEscuro);
        lblData.setBounds(100, 45, w - 400, 20);
        p.add(lblData);

        return p;
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
            b.addActionListener(e -> { this.dispose(); new SeusAlunos().setVisible(true); });
        } else {
            b.setFont(new Font("Arial", Font.BOLD, 24));
            b.setBounds(x, y, 50, 40);
            if (texto.equals("X")) b.addActionListener(e -> System.exit(0));
            else if (texto.equals("-")) b.addActionListener(e -> setState(Frame.ICONIFIED));
        }
        b.setMargin(new Insets(0,0,0,0)); b.setBackground(corInvisivel); b.setForeground(Color.WHITE);
        b.setFocusable(false); b.setBorder(null); b.setContentAreaFilled(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(texto.equals("X") ? new Color(232,17,35) : new Color(100,100,100)); }
            @Override public void mouseExited(MouseEvent e) { b.setBackground(corInvisivel); }
        });
        return b;
    }

    private void carregarFontes() {
        try {
            Font base = Font.createFont(Font.TRUETYPE_FONT, new File("RobotoSerif-Bold.ttf"));
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(base);
            robotoBold36 = base.deriveFont(Font.BOLD, 36f);
            robotoBold24 = base.deriveFont(Font.BOLD, 24f);
            robotoBold14 = base.deriveFont(Font.BOLD, 14f);
            robotoBold10 = base.deriveFont(Font.BOLD, 10f);
        } catch (Exception e) {
            robotoBold36 = new Font("Arial", Font.BOLD, 36);
            robotoBold24 = new Font("Arial", Font.BOLD, 24);
            robotoBold14 = new Font("Arial", Font.BOLD, 14);
            robotoBold10 = new Font("Arial", Font.BOLD, 10);
        }
    }
}

