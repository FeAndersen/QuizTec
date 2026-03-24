import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class CadastroFullScreen extends JFrame {

    public CadastroFullScreen() {
        // 1. Configurações da Janela para FULL HD (1920x1080)
        setTitle("QuizTec - Novo Cadastro (Modo Jogo)");
        
        // Define o tamanho exato da janela
        setSize(1920, 1080); 
        
        // Remove as bordas da janela (barra de título, botões fechar, etc.)
        // Isso faz parecer um jogo de verdade em tela cheia.
        setUndecorated(true);
        
        // Maximiza a janela para ocupar a tela inteira automaticamente
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        
        // --- PAINEL DE FUNDO (A imagem cinza fundo_etec.jpg cobrindo 1920x1080) ---
        JPanel painelFundo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imagemFundo = new ImageIcon("fundo_etec.jpg");
                // Desenha a imagem preenchendo todo o tamanho do painel (1920x1080)
                g.drawImage(imagemFundo.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        painelFundo.setLayout(null); // Layout nulo para centralizar o bloco
        setContentPane(painelFundo);

        // --- BLOCO CENTRALIZADO (1500x850 - Proporcional ao Full HD) ---
        // Vamos criar um container invisível para segurar os dois lados
        JPanel blocoCentral = new JPanel();
        blocoCentral.setLayout(null);
        blocoCentral.setOpaque(false);
        
        // Cálculo para centralizar o bloco de 1500x850 na tela de 1920x1080
        // X: (1920 - 1500) / 2 = 210
        // Y: (1080 - 850) / 2 = 115
        blocoCentral.setBounds(210, 115, 1500, 850);

        // --- LADO ESQUERDO (BRANCO SÓLIDO) ---
        JPanel ladoBranco = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                // Desenha o arredondamento de 69 pixels (Raio Total)
                g2.fillRoundRect(0, 0, 1500, 850, 69, 69);
                // Cobre a metade direita (a partir de X=700) para o meio ficar reto
                g2.fillRect(700, 0, 100, 850); 
            }
        };
        ladoBranco.setLayout(null);
        ladoBranco.setBounds(0, 0, 750, 850); // Metade esquerda
        ladoBranco.setOpaque(false);

        // Logos (Ajustados para o tamanho maior do bloco)
        ImageIcon etecIcon = new ImageIcon("Logo_etec.jpg");
        Image etecImg = etecIcon.getImage().getScaledInstance(450, 250, Image.SCALE_SMOOTH);
        JLabel labelEtec = new JLabel(new ImageIcon(etecImg));
        labelEtec.setBounds(150, 150, 450, 250);
        ladoBranco.add(labelEtec);

        ImageIcon cpsIcon = new ImageIcon("Logo_cps.jpg");
        Image cpsImg = cpsIcon.getImage().getScaledInstance(400, 200, Image.SCALE_SMOOTH);
        JLabel labelCps = new JLabel(new ImageIcon(cpsImg));
        labelCps.setBounds(175, 500, 400, 200);
        ladoBranco.add(labelCps);

        // --- LADO DIREITO (VERMELHO SÓLIDO 100%) ---
        JPanel ladoVermelho = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(178, 0, 0)); // Vermelho 100% opaco
                // Desenha o arredondamento de 69 pixels (Raio Total, espelhado)
                g2.fillRoundRect(-750, 0, 1500, 850, 69, 69);
                // Cobre a metade esquerda (a partir de X=0) para o meio ficar reto
                g2.fillRect(0, 0, 100, 850);
            }
        };
        ladoVermelho.setLayout(null);
        ladoVermelho.setBounds(750, 0, 750, 850); // Metade direita
        ladoVermelho.setOpaque(false);

        // Título e Campos (Ajustados para o tamanho maior do bloco)
        JLabel txtTitulo = new JLabel("Novo Cadastro");
        txtTitulo.setForeground(Color.WHITE);
        txtTitulo.setFont(new Font("Serif", Font.BOLD, 54)); // Fonte maior
        txtTitulo.setBounds(200, 80, 400, 70);
        ladoVermelho.add(txtTitulo);

        JTextField campoEmail = new JTextField(" Inserir email");
        campoEmail.setBounds(125, 230, 500, 60); // Campos maiores
        campoEmail.setBackground(new Color(217, 217, 217));
        campoEmail.setFont(new Font("Arial", Font.PLAIN, 20));
        ladoVermelho.add(campoEmail);

        JPasswordField campoSenha = new JPasswordField();
        campoSenha.setBounds(125, 330, 500, 60);
        campoSenha.setBackground(new Color(217, 217, 217));
        ladoVermelho.add(campoSenha);

        JPasswordField confirmaSenha = new JPasswordField();
        confirmaSenha.setBounds(125, 430, 500, 60);
        confirmaSenha.setBackground(new Color(217, 217, 217));
        ladoVermelho.add(confirmaSenha);

        JButton btnSeguir = new JButton("Seguir");
        btnSeguir.setBounds(125, 580, 500, 80); // Botão maior
        btnSeguir.setBackground(new Color(26, 55, 94)); // Azul escuro
        btnSeguir.setForeground(Color.WHITE);
        btnSeguir.setFont(new Font("Arial", Font.BOLD, 28));
        btnSeguir.setFocusPainted(false);
        ladoVermelho.add(btnSeguir);

        // Montagem final
        blocoCentral.add(ladoBranco);
        blocoCentral.add(ladoVermelho);
        painelFundo.add(blocoCentral);

        setVisible(true);
    }

    public static void main(String[] args) {
        // Executar o programa
        new CadastroFullScreen();
    }
}