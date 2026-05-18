
USE quiztec;

-- populando a tabela dificuldade
INSERT INTO dificuldade
VALUES (default, 'FACIL', 1, 1.0), (default, 'MEDIO', 2, 1.5), (default, 'DIFICIL', 3, 2.0);


-- populando tabela professor
INSERT INTO professor (nome_professor, email_professor, senha_professor)
VALUES ('João da Silva Júnior', 'joaojunior@etec.sp.gov.br', '$2a$12$MGFOFKGYSF9mRYmqXQP2guSXPYIZnNJN1yw5/WkgDFIm9Lt8SwLd6'),
('Maria Joana Carvalho', 'mariacarvalho@etec.sp.gov.br', '$2a$12$PA13sgHRBALPIiwrazYTd.Wp1skrgXueZvkh7pezz3Ag/v.Gfdt0W');


-- populando tabela turma
INSERT INTO turma (serie, letra, id_professor)
VALUES (1, 'A', 1), (1, 'B', 1), (2, 'A', 2);


-- populando tabela aluno
INSERT INTO aluno (nome_aluno, email_aluno, senha_aluno, id_turma)
VALUES ('Cássio Roberto Ramos', 'cassioramos@aluno.etec.sp.gov.br', '$2a$12$I2INboomLJ.yqTlqmtC4Xulvxzpcifekqe8P9mMbBw9niwHjtFKjK', 1),
('Yuri Alberto Junior', 'yurialberto@aluno.etec.sp.gov.br', '$2a$12$1rUqzs1druFK7ick070FH.mJA.5z8DrDw0rsfz/eqSLVF6eq5Bcti', 2),
('Pedro Milans', 'pedromilans@aluno.etec.sp.gov.br', '$2a$12$IVzqyUO7a5fos6QZv/NiT.NwGloL5eOUPCnwRPdlVjFJNX2js/eR2', 3),
('Breno Bidon da Silva', 'brenobidon@aluno.etec.sp.gov.br', '$2a$12$Dj3v5MQuUV1825G9XeWv6O6S7IoI5ZKITs5SLDnflfTUHlsAn7kgm', 2);


-- populando a tabela imagem
INSERT INTO imagem (arquivo_imagem, tipo_imagem, nome_imagem, descricao_imagem)
VALUES (UNHEX('89504E47'), 'png', 'becker_50ml.png', 'Béquer de vidro com graduação, capacidade de 50ml'),
(UNHEX('89504E47'), 'png', 'proveta_100ml.png', 'Proveta graduada de 100ml para medição de volumes'),
(UNHEX('89504E47'), 'png', 'erlenmeyer.png', 'Erlenmeyer de 250ml para reações químicas'),
(UNHEX('89504E47'), 'png', 'pipeta_volumetrica.png', 'Pipeta volumétrica de 25ml para medições precisas');


  

-- populando a tabela pergunta
INSERT INTO pergunta (enunciado, tipo_pergunta, pontuacao, id_imagem)
VALUES('Qual o nome desta vidraria?', 'identificacao', 10, 1), ('Para que serve este equipamento?', 'material_funcao', 15, 2),
('Qual material é usado para medir volumes com precisão?', 'funcao_material', 20, NULL);


-- populando tabela alternativa
INSERT INTO alternativa (resposta, tipo_alternativa, correta, id_pergunta, id_imagem)
VALUES ('Béquer', 'texto', 1, 1, NULL), ('Proveta', 'texto', 0, 1, NULL), ('Erlenmeyer', 'texto', 0, 1, NULL), ('Balão volumétrico', 'texto', 0, 1, NULL),
('Medir volumes de líquidos com precisão moderada', 'texto', 1, 2, NULL), ('Aquecer substâncias diretamente na chama do bico de Bunsen', 'texto', 0, 2, NULL),
('Realizar reações químicas que liberam gases sob agitação', 'texto', 0, 2, NULL), ('Filtrar misturas heterogêneas com auxílio de papel filtro', 'texto', 0, 2, NULL),
('Pipeta volumétrica', 'imagem', 1, 3, 4), ('Béquer', 'imagem', 0, 3, 1), ('Erlenmeyer', 'imagem', 0, 3, 3), ('Proveta', 'imagem', 0, 3, 2);


-- populando tabela ajuda
INSERT INTO ajuda (tipo_ajuda, conteudo_ajuda, custo_pontos, id_pergunta)
VALUES ('eliminar_alternativa', NULL, 5, 1), ('dica_textual', 'Observe atentamente o formato deste material. 
Ele é cilíndrico, alongado e possui marcações numéricas em sua lateral. 
Equipamentos com graduação servem para realizar mensurações.
 Pense em qual grandeza física pode ser medida em um recipiente desse formato.', 10, 2);


-- populando tabela sessao
INSERT INTO sessao (nome_sessao, quantidade_perguntas, id_professor, id_dificuldade)
VALUES ('Vidrarias básicas', 3, 1, 1), ('Sistemas de filtração', 2, 2, 3);


-- populando perguntas_sessao
INSERT INTO perguntas_sessao (id_sessao, id_pergunta)
VALUES (1, 1), (1, 2), (1, 3), (2, 2), (2, 3);


-- populando tabela partida 
INSERT INTO partida (id_aluno, id_sessao, pontuacao_total, data_hora_fim, status_partida)
VALUES (1, 1, 25, (current_timestamp + INTERVAL 3 HOUR), 'finalizado'), (2, 2, default, NULL, default), 
(3, 1, default, (current_timestamp + INTERVAL 10 MINUTE), 'finalizado');




-- populando tabela resposta_aluno 
INSERT INTO resposta_aluno (id_partida, id_pergunta, id_alternativa, pontos_obtidos)
VALUES (1, 1, 1, 10), (1, 2, 5, 15), (1, 3, 10, 0), (2, 2, 6, 0), (2, 3, 11, 0), (3, 1, 2, 0), (3, 2, 6, 0), (3, 3, 11, 0);




INSERT INTO ajuda_usada (id_ajuda, id_partida, pontos_descontados)
VALUES (2, 2, 10);




