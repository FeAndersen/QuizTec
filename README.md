# 🎓 QuizTec - Etec / Centro Paula Souza

O **QuizTec** é um aplicativo desktop educacional desenvolvido em **Java (Swing / AWT)** focado na criação e resolução de quizzes interativos. O projeto foi desenhado com uma interface gráfica simples, moderna, responsiva oferecendo uma experiência de usuário (UX) extremamente leve e de fácil entendimento

Este projeto é parte do Trabalho Integrador (TI) da Mauá com parceria com a Etec Júlio de Mesquita (Santo André).

---

## 🚀 Funcionalidades Principais

O sistema possui fluxos separados para **Professores** e **Alunos**, garantindo que cada usuário tenha acesso apenas às ferramentas pertinentes ao seu perfil.

### 👨‍🏫 Área do Professor

* **Cadastro/Login Especializado:** Formulários customizados sem as bordas nativas do SO.
* **Criação de Jogos:**
  * Seleção de nível de dificuldade (Fácil, Médio, Difícil).
  * Inserção de imagens locais (`JFileChooser`).
  * Formulário dinâmico com botões customizados de seleção de alternativa correta.
* **Gerenciamento de Jogos (Histórico):** Lista com rolagem suave contendo todos os quizzes criados, com opções de edição, renomeação e exclusão.
* **Gerenciamento de Alunos:** Adição, edição e remoção de alunos da base de dados através de modais interativos (Pop-ups GlassPane).

### 🧑‍🎓 Área do Aluno

* **Cadastro Dinâmico:** Uso de `JComboBox` totalmente reestilizado para seleção de turma.
* **Seleção de Quizzes:** Listagem dos quizzes disponíveis enviados pelo professor.
* **Gameplay Interativo:** * Renderização de perguntas e imagens.
  * Feedback visual imediato e animado (Certo/Errado) sobreposto à tela usando `GlassPane`.
* **Resultados:** * Tela de fim de jogo com pontuação e opções de "Jogar Novamente" ou voltar ao menu.
  * Tela de "Histórico" exibindo pontuação total, acertos e datas das partidas.

---

## 💻 Tecnologias Utilizadas

* **Linguagem:** Java (JDK 8+)
* **Interface Gráfica (GUI):** Java Swing & AWT
* **Renderização Avançada:** * Uso massivo de `Graphics2D` para cantos arredondados, ícones gerados via código (geometria matemática) e Anti-aliasing (`RenderingHints`).
  * Modais transparentes (Overlay) utilizando o recurso de `GlassPane` do JFrame.
* **Fontes Customizadas:** Importação nativa da família de fontes *Roboto Serif*.

---

## 📁 Estrutura de Pastas (Importante)

Para que a interface gráfica renderize corretamente e encontre todos os ícones, o projeto exige a seguinte estrutura no repositório:

```text
QuizTec/
│
├── QuizTec/images/              # Pasta obrigatória de recursos visuais
│   ├── fundo_etec.jpg           # Fundo padrão de todas as telas
│   ├── Logo_etec.jpg            # Logo Etec
│   ├── Logo_cps.jpg             # Logo Centro Paula Souza
│   ├── labs.png                 # Ícone nível Fácil
│   ├── biotech.png              # Ícone nível Médio
│   ├── fluid_med.png            # Ícone nível Difícil
│   ├── person_add.png           # Ícone adicionar aluno
│   ├── person_remove.png        # Ícone remover aluno
│   ├── edit.png                 # Ícone de edição
│   ├── rename.png               # Ícone renomear
│   ├── delete.png               # Ícone lixeira
│   ├── trophy.png               # Ícone troféu (Tela de resultado)
│   ├── replay.png               # Ícone jogar novamente
│   ├── menu.png                 # Ícone voltar ao menu
│   ├── insert_chart.png         # Ícone de acertos
│   ├── leaderboard.png          # Ícone de ranking
│   └── help.png                 # Ícone de interrogação
│
├── RobotoSerif-Bold.ttf         # Arquivo de Fonte (obrigatório na raiz)
│
├── Login.java                   # Telas do fluxo de autenticação...
├── CadastroProf.java            
├── CadastroAluno.java           
├── MenuProf.java                # Telas do fluxo do Professor...
├── CriarSelecaoNivel.java       
├── CriarPerguntas.java          
├── SeusJogosCriados.java        
├── SeusAlunos.java              
├── MenuAluno.java               # Telas do fluxo do Aluno...
├── EscolherJogoAluno.java       
├── JogarQuiz.java               
├── ResultadoQuiz.java           
└── HistoricoAluno.java
