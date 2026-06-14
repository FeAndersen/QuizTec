package Cadastro;
import Professor.MenuProf;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;
import util.Conexao;
import util.Sessao;

public class CadastroProf extends JFrame {

    private Font fontTitulo, robotoSemiBold40, robotoRegular20;

    public CadastroProf() {
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
                ImageIcon imagemFundo = new ImageIcon("images\\fundo_etec.jpg");
                g.drawImage(imagemFundo.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        painelFundo.setLayout(null);
        setContentPane(painelFundo);

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

        int centroY = alturaBloco / 2;
        adicionarLogo(blocoCentral, "images\\Logo_etec.jpg", larguraBloco / 4, centroY - 100, 0.25);
        adicionarLogo(blocoCentral, "images\\Logo_cps.jpg", larguraBloco / 4, centroY + 130, 0.18);

        int fieldW = (int) (larguraBloco * 0.35); 
        int fieldH = 50; 
        int fieldX = (int) (larguraBloco * 0.75) - (fieldW / 2);
        int espacoY = 15; 

        JLabel txtTitulo = new JLabel("Novo Cadastro", SwingConstants.CENTER);
        txtTitulo.setForeground(Color.WHITE);
        txtTitulo.setFont(fontTitulo.deriveFont(55f)); 
        txtTitulo.setBounds(larguraBloco / 2, centroY - 250, larguraBloco / 2, 80); 
        blocoCentral.add(txtTitulo);

        int startYCampos = centroY - 140;
        JTextField campoEmail = criarCampo("Inserir email", fieldX, startYCampos, fieldW, fieldH);
        JTextField campoNome = criarCampo("Inserir seu nome completo", fieldX, startYCampos + (fieldH + espacoY), fieldW, fieldH);
        JTextField campoSenha = criarCampo("Inserir senha", fieldX, startYCampos + (fieldH + espacoY) * 2, fieldW, fieldH);
        JTextField campoConfirmacao = criarCampo("Confirmação da senha", fieldX, startYCampos + (fieldH + espacoY) * 3, fieldW, fieldH);
        
        
        
        blocoCentral.add(campoEmail);
        blocoCentral.add(campoNome);
        blocoCentral.add(campoSenha);
        blocoCentral.add(campoConfirmacao);

        JButton btnSeguir = new JButton("Seguir") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 55, 90)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        btnSeguir.setBounds(fieldX, startYCampos + (fieldH + espacoY) * 4 + 20, fieldW, 60);
        btnSeguir.setForeground(Color.WHITE);
        btnSeguir.setFont(robotoSemiBold40.deriveFont(32f));
        btnSeguir.setContentAreaFilled(false);
        btnSeguir.setBorderPainted(false);
        btnSeguir.setFocusPainted(false);
        btnSeguir.setBorder(null); // Remove qualquer borda residual
        btnSeguir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnSeguir.addActionListener(e -> {
            String email = campoEmail.getText().trim();
            String nome = campoNome.getText().trim();
            String senha = campoSenha.getText().trim();
            String confirmacao = campoConfirmacao.getText().trim();

            if(email.isEmpty() || nome.isEmpty() || senha.isEmpty() || confirmacao.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
                return;
            }

            if(!senha.equals(confirmacao)) {
                JOptionPane.showMessageDialog(null, "As senhas não coincidem");
                return;
            }

            if(!email.endsWith("@cps.sp.gov.br") || email.endsWith("@aluno.cps.sp.gov.br")) {
                JOptionPane.showMessageDialog(null, "Use um email de professor: xxxxxx@cps.sp.gov.br");
                return;
            }

            String senhaHash = BCrypt.hashpw(senha, BCrypt.gensalt());

            try (Connection con = Conexao.conectar()) {
                PreparedStatement stmt = con.prepareStatement (
                    "INSERT INTO professor (nome_professor, email_professor, senha_professor) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                );
                stmt.setString(1, nome);
                stmt.setString(2, email);
                stmt.setString(3, senhaHash);
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    Sessao.idUsuario = rs.getInt(1);
                    Sessao.tipoUsuario = "professor";
                    Sessao.nomeUsuario = nome;
                }

                dispose();
                new MenuProf().setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro ao cadastrar: " + ex.getMessage());
            }
        });
        blocoCentral.add(btnSeguir);

        painelFundo.add(blocoCentral);

        painelFundo.add(criarBotaoControle("X", larguraTela - 50, 0));
        painelFundo.add(criarBotaoControle("-", larguraTela - 100, 0));
        painelFundo.add(criarBotaoControle("↰", 0, 0)); 

        setVisible(true);
    }

    private JTextField criarCampo(String placeholder, int x, int y, int w, int h) {
        JTextField campo = new JTextField(placeholder);
        campo.setBounds(x, y, w, h);
        campo.setBackground(new Color(220, 220, 220)); 
        campo.setFont(robotoRegular20);
        campo.setForeground(Color.DARK_GRAY);
        campo.setHorizontalAlignment(JTextField.CENTER);
        campo.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); 

        campo.addFocusListener(new FocusAdapter() {
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
            b.addActionListener(e -> { this.dispose(); new SelecaoCadastro().setVisible(true); });
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
        b.setFocusPainted(false);
        b.setBorder(null); // Mata a borda de foco do Swing
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(corHover); b.repaint(); }
            @Override public void mouseExited(MouseEvent e) { b.setBackground(corInvisivel); b.repaint(); }
        });
        return b;
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
        } catch (Exception e) { System.out.println("Erro ao carregar: " + path); }
    }

    private void carregarFontes() {
        try {
            File fontFile = new File("RobotoSerif-Bold.ttf");
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            Map<TextAttribute, Object> atributos = new HashMap<>();
            atributos.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_EXTRABOLD);
            fontTitulo = baseFont.deriveFont(atributos);
            robotoSemiBold40 = baseFont.deriveFont(Font.BOLD, 36f);
            robotoRegular20 = baseFont.deriveFont(Font.PLAIN, 18f);
        } catch (Exception e) {
            fontTitulo = new Font("Serif", Font.BOLD, 64);
            robotoSemiBold40 = new Font("sansserif", Font.BOLD, 35);
            robotoRegular20 = new Font("sansserif", Font.PLAIN, 18);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CadastroProf());
    }
}