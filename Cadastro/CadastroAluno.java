package Cadastro;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import Cadastro.modelo.Aluno; // Import único e correto para o modelo

import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CadastroAluno extends JFrame {

    private Font fontTitulo, robotoSemiBold40, robotoRegular20;
    private JTextField txtEmail, txtNome, txtSenha, txtConfirmarSenha;
    private JComboBox<String> comboTurma;

    public CadastroAluno() {
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
        adicionarLogo(blocoCentral, "QuizTec\\images\\Logo_etec.jpg", larguraBloco / 4, centroY - 100, 0.25);
        adicionarLogo(blocoCentral, "QuizTec\\images\\Logo_cps.jpg", larguraBloco / 4, centroY + 130, 0.18);

        int fieldW = (int) (larguraBloco * 0.35); 
        int fieldH = 45; 
        int fieldX = (int) (larguraBloco * 0.75) - (fieldW / 2);
        int espacoY = 12; 

        JLabel txtTitulo = new JLabel("Novo Cadastro", SwingConstants.CENTER);
        txtTitulo.setForeground(Color.WHITE);
        txtTitulo.setFont(fontTitulo.deriveFont(55f)); 
        txtTitulo.setBounds(larguraBloco / 2, centroY - 260, larguraBloco / 2, 80); 
        blocoCentral.add(txtTitulo);

        int startYCampos = centroY - 160;
        txtEmail = criarCampo("Inserir email", fieldX, startYCampos, fieldW, fieldH);
        blocoCentral.add(txtEmail);

        txtNome = criarCampo("Inserir nome completo", fieldX, startYCampos + (fieldH + espacoY), fieldW, fieldH);
        blocoCentral.add(txtNome);

        txtSenha = criarCampo("Inserir senha", fieldX, startYCampos + (fieldH + espacoY) * 2, fieldW, fieldH);
        blocoCentral.add(txtSenha);

        txtConfirmarSenha = criarCampo("Confirmação da senha", fieldX, startYCampos + (fieldH + espacoY) * 3, fieldW, fieldH);
        blocoCentral.add(txtConfirmarSenha);

        comboTurma = criarDropdownTurma(fieldX, startYCampos + (fieldH + espacoY) * 4, fieldW, fieldH);
        blocoCentral.add(comboTurma);

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
        btnSeguir.setBounds(fieldX, startYCampos + (fieldH + espacoY) * 5 + 15, fieldW, 60);
        btnSeguir.setForeground(Color.WHITE);
        btnSeguir.setFont(robotoSemiBold40.deriveFont(32f));
        btnSeguir.setContentAreaFilled(false);
        btnSeguir.setBorderPainted(false);
        btnSeguir.setFocusPainted(false);
        btnSeguir.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // LÓGICA DO BOTÃO ARRUMADA AQUI:
        btnSeguir.addActionListener(e -> {
            String email = txtEmail.getText();
            String nome = txtNome.getText();
            String senha = txtSenha.getText();
            String confirma = txtConfirmarSenha.getText();
            String turma = (String) comboTurma.getSelectedItem();

            if (nome.equals("Inserir nome completo") || email.equals("Inserir email") || senha.isEmpty() || turma.equals("Inserir turma")) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos corretamente!");
                return;
            }

            if (!senha.equals(confirma)) {
                JOptionPane.showMessageDialog(null, "As senhas não coincidem!");
                return;
            }

            // Criando o objeto Aluno (Certifique-se que Aluno.java tem o construtor correto)
            Aluno novoAluno = new Aluno(nome, email, senha, turma);
            System.out.println("Back-end processado para: " + novoAluno.getNome());

            JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso!");
            this.dispose();
            // Descomente a linha abaixo quando criar a tela MenuAluno
            // new MenuAluno().setVisible(true); 
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

    private JComboBox<String> criarDropdownTurma(int x, int y, int w, int h) {
        String[] turmas = {"Inserir turma", "1º Ano A", "1º Ano B", "1º Ano C", "1º Ano D", "2º Ano A", "3º Ano A"};
        JComboBox<String> combo = new JComboBox<>(turmas);
        combo.setBounds(x, y, w, h);
        combo.setFont(robotoRegular20);
        combo.setBackground(new Color(220, 220, 220));
        combo.setForeground(Color.DARK_GRAY);
        combo.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        
        DefaultListCellRenderer dlcr = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                if (isSelected) {
                    label.setBackground(new Color(200, 200, 200));
                } else {
                    label.setBackground(new Color(220, 220, 220));
                }
                return label;
            }
        };
        combo.setRenderer(dlcr);
        
        combo.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(Color.DARK_GRAY);
                        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        int cx = getWidth() / 2;
                        int cy = getHeight() / 2;
                        g2.drawLine(cx - 5, cy - 2, cx, cy + 3);
                        g2.drawLine(cx, cy + 3, cx + 5, cy - 2);
                        g2.dispose();
                    }
                };
                button.setBackground(new Color(220, 220, 220));
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setFocusPainted(false);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                return button;
            }
        });
        return combo;
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
            b.addActionListener(e -> { this.dispose(); /* new SelecaoCadastro().setVisible(true); */ });
        } else {
            b.setFont(new Font("Arial", Font.BOLD, 24));
            b.setBounds(x, y, 50, 40); 
            if (texto.equals("X")) b.addActionListener(e -> System.exit(0));
            else if (texto.equals("-")) b.addActionListener(e -> setState(Frame.ICONIFIED));
        }

        b.setBackground(corInvisivel); 
        b.setForeground(Color.WHITE);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
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
        SwingUtilities.invokeLater(() -> new CadastroAluno());
    }
}