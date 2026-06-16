-- ativando banco de dados
DROP SCHEMA IF EXISTS quiztec;
CREATE SCHEMA quiztec;
USE quiztec;
-- SELECT @@default_storage_engine; usado durante desenvolvimento para verificar engine padrão
-- criando tabela professor
CREATE TABLE professor (
id_professor INT AUTO_INCREMENT PRIMARY KEY,
nome_professor VARCHAR(100) NOT NULL,
email_professor VARCHAR(255) NOT NULL UNIQUE,
senha_professor VARCHAR(255) NOT NULL
);

-- criando tabela imagem
CREATE TABLE imagem (
id_imagem INT AUTO_INCREMENT PRIMARY KEY,
arquivo_imagem BLOB NOT NULL,
tipo_imagem ENUM('jpg', 'jpeg', 'png', 'webp', 'gif') NOT NULL,
nome_imagem VARCHAR(30) NOT NULL,
descricao_imagem MEDIUMTEXT NULL
);

-- criando tabela dificuldade 
CREATE TABLE dificuldade (
id_dificuldade INT AUTO_INCREMENT PRIMARY KEY,
nome_dificuldade VARCHAR(20) NOT NULL UNIQUE,
ordem TINYINT UNSIGNED NOT NULL CHECK(ordem BETWEEN 1 AND 10),
multiplicador_pontos DECIMAL(2,1) NOT NULL CHECK(multiplicador_pontos > 0)
);

-- criando tabela pergunta
CREATE TABLE pergunta (
id_pergunta INT AUTO_INCREMENT PRIMARY KEY,
enunciado VARCHAR(250) NOT NULL,
tipo_pergunta ENUM('material_funcao', 'funcao_material', 'material_sistema', 'sistema_material', 'identificacao') NOT NULL,
pontuacao SMALLINT UNSIGNED NOT NULL CHECK(pontuacao > 0),
ativo BOOLEAN NOT NULL DEFAULT TRUE,
id_imagem INT NULL,
FOREIGN KEY (id_imagem) REFERENCES imagem (id_imagem)
			ON UPDATE CASCADE ON DELETE SET NULL
);

-- criando tabela sessao
CREATE TABLE sessao (
id_sessao INT AUTO_INCREMENT PRIMARY KEY,
nome_sessao VARCHAR(30) NOT NULL,
quantidade_perguntas TINYINT UNSIGNED NOT NULL CHECK(quantidade_perguntas > 0),
data_criacao DATE NOT NULL DEFAULT (CURDATE()),
ativa BOOLEAN NOT NULL DEFAULT TRUE,
id_professor INT NOT NULL,
id_dificuldade INT NOT NULL,
FOREIGN KEY (id_professor) REFERENCES professor (id_professor)
			ON UPDATE CASCADE ON DELETE RESTRICT,
FOREIGN KEY (id_dificuldade) REFERENCES dificuldade (id_dificuldade)
			ON UPDATE CASCADE ON DELETE RESTRICT,
UNIQUE (nome_sessao, id_professor)
);

-- criando tabela turma
CREATE TABLE turma (
id_turma INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
serie TINYINT UNSIGNED NOT NULL CHECK(serie BETWEEN 1 AND 3),
letra CHAR(1) NOT NULL CHECK(letra REGEXP '^[A-Z]$'),
ano_letivo YEAR NOT NULL DEFAULT(YEAR(CURDATE())),
id_professor INT NOT NULL,
FOREIGN KEY (id_professor) REFERENCES professor (id_professor) 
			ON DELETE RESTRICT ON UPDATE CASCADE,
UNIQUE (serie, letra, ano_letivo, id_professor)
);

-- criando tabela aluno
CREATE TABLE aluno (
id_aluno INT AUTO_INCREMENT PRIMARY KEY,
nome_aluno VARCHAR(100) NOT NULL,
email_aluno VARCHAR(255) NOT NULL UNIQUE,
senha_aluno VARCHAR(255) NOT NULL,
id_turma INT NULL,
FOREIGN KEY (id_turma) REFERENCES turma (id_turma)
			ON DELETE RESTRICT ON UPDATE CASCADE
);



-- criando tabela alternativa
CREATE TABLE alternativa (
id_alternativa INT AUTO_INCREMENT PRIMARY KEY,
resposta MEDIUMTEXT NOT NULL,
tipo_alternativa ENUM('texto', 'imagem', 'mista') NOT NULL,
correta BOOLEAN NOT NULL DEFAULT 0,
id_pergunta INT NOT NULL,
id_imagem INT NULL,
FOREIGN KEY (id_pergunta) REFERENCES pergunta (id_pergunta)
			ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY (id_imagem) REFERENCES imagem (id_imagem)
			ON DELETE SET NULL ON UPDATE CASCADE
);



-- criando tabela ajuda
CREATE TABLE ajuda (
id_ajuda INT AUTO_INCREMENT PRIMARY KEY,
tipo_ajuda ENUM('eliminar_alternativa', 'dica_textual') NOT NULL,
conteudo_ajuda MEDIUMTEXT NULL,
custo_pontos SMALLINT UNSIGNED NOT NULL,
id_pergunta INT NOT NULL,
FOREIGN KEY (id_pergunta) REFERENCES pergunta (id_pergunta)
			ON DELETE CASCADE ON UPDATE CASCADE
);


-- criando tabela partida
CREATE TABLE partida (
id_partida INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
id_aluno INT NOT NULL,
id_sessao INT NOT NULL,
pontuacao_total INT UNSIGNED NOT NULL DEFAULT 0,
acertos INT NOT NULL DEFAULT 0,
data_hora_inicio TIMESTAMP NOT NULL DEFAULT current_timestamp,
data_hora_fim TIMESTAMP NULL,
status_partida ENUM('em_andamento', 'finalizado', 'ausente') NOT NULL DEFAULT 'em_andamento',
FOREIGN KEY (id_aluno) REFERENCES aluno (id_aluno)
			ON UPDATE CASCADE ON DELETE RESTRICT,
FOREIGN KEY (id_sessao) REFERENCES sessao (id_sessao)
			ON UPDATE CASCADE ON DELETE RESTRICT
);

-- criando tabela ajuda_usada
CREATE TABLE ajuda_usada (
id_ajuda_usada INT AUTO_INCREMENT PRIMARY KEY,
id_ajuda INT NOT NULL,
id_partida INT NOT NULL,
pontos_descontados SMALLINT UNSIGNED NOT NULL,
FOREIGN KEY (id_ajuda) REFERENCES ajuda (id_ajuda)
			ON UPDATE RESTRICT ON DELETE RESTRICT,
FOREIGN KEY (id_partida) REFERENCES partida (id_partida)
			ON UPDATE RESTRICT ON DELETE CASCADE
);

-- criando tabela Resposta Aluno
CREATE TABLE resposta_aluno(
id_resposta INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
id_partida INT NOT NULL,
id_pergunta INT NOT NULL,
id_alternativa INT NOT NULL,
pontos_obtidos SMALLINT UNSIGNED NOT NULL,
data_hora_resposta DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (id_partida) REFERENCES partida (id_partida)
			ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY (id_pergunta) REFERENCES pergunta (id_pergunta)
			ON UPDATE CASCADE ON DELETE RESTRICT,
FOREIGN KEY (id_alternativa) REFERENCES alternativa (id_alternativa)
			ON UPDATE CASCADE ON DELETE RESTRICT,
UNIQUE (id_partida, id_pergunta)
);
-- criando tabela associativa Perguntas Sessão
CREATE TABLE perguntas_sessao(
id_sessao INT NOT NULL,
id_pergunta INT NOT NULL,
PRIMARY KEY(id_sessao, id_pergunta),
FOREIGN KEY (id_sessao) REFERENCES sessao (id_sessao)
			ON UPDATE CASCADE ON DELETE RESTRICT,
FOREIGN KEY (id_pergunta) REFERENCES pergunta (id_pergunta)
			ON UPDATE CASCADE ON DELETE RESTRICT
);





 

