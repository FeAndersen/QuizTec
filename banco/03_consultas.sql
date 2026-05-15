USE quiztec;

-- RELATÓRIOS PEDAGÓGICOS

-- desempenho geral de todos os alunos
SELECT a.nome_aluno AS Nome, t.serie AS Série, t.letra AS Letra, s.nome_sessao AS Sessão, d.nome_dificuldade AS Dificuldade,
p.pontuacao_total AS 'Pontuação Total', p.status_partida AS 'Status Partida', p.data_hora_inicio AS 'Data Hora Início'
FROM partida p JOIN aluno a USING (id_aluno)
				JOIN turma t USING (id_turma)
                JOIN sessao s USING (id_sessao)
                JOIN dificuldade d USING (id_dificuldade)
WHERE p.status_partida = 'finalizado'
ORDER BY a.nome_aluno, p.data_hora_inicio DESC;

-- historico de partidas de um aluno em especifico
SELECT s.nome_sessao AS Sessão, d.nome_dificuldade AS Dificuldade, p.pontuacao_total AS Pontuação, p.status_partida AS 'Status Partida',
p.data_hora_inicio AS 'Data Hora Início', p.data_hora_fim AS 'Data Hora Fim', TIMESTAMPDIFF (MINUTE, p.data_hora_inicio, p.data_hora_fim) AS 'Duração Minutos'
FROM partida p JOIN sessao s USING (id_sessao)
				JOIN dificuldade d USING (id_dificuldade)
WHERE p.id_aluno = ? -- WHERE p.id_aluno = 1
ORDER BY p.data_hora_inicio DESC;

-- taxa de acerto por pergunta
SELECT pe.id_pergunta, pe.enunciado AS Enunciado, pe.tipo_pergunta AS 'Tipo Pergunta', COUNT(*) AS 'Total Respostas',
SUM(CASE WHEN alt.correta = 1 THEN 1 ELSE 0 END) AS Acertos, ROUND(SUM(CASE WHEN alt.correta = 1 THEN 1 ELSE 0 END) * 100.0 / COUNT(*), 1) AS 'Taxa de Acerto'
FROM resposta_aluno ra JOIN pergunta pe USING (id_pergunta)
						JOIN alternativa alt USING (id_alternativa)
GROUP BY pe.id_pergunta, pe.enunciado, pe.tipo_pergunta
ORDER BY 'Taxa de Acerto' ASC;


-- CONSULTAS DE APLICAÇÃO
-- buscar aluno pelo email
SELECT a.id_aluno, a.nome_aluno AS Aluno, a.senha_aluno AS Senha, t.serie AS Série, t.letra AS Letra
FROM aluno a JOIN turma t USING (id_turma)
WHERE a.email_aluno = ? ; -- WHERE a.email_aluno = 'pedromilans@aluno.etec.sp.gov.br';

-- buscar professor pelo email
SELECT pr.id_professor, pr.nome_professor AS Professor, pr.senha_professor AS Senha
FROM professor pr
WHERE email_professor = ?; -- WHERE email_professor = 'joaojunior@etec.sp.gov.br'

-- lista de sessões ativas
SELECT s.id_sessao, s.nome_sessao AS Sessão, s.quantidade_perguntas AS 'Quantidade Perguntas', d.nome_dificuldade AS Dificuldade,
d.multiplicador_pontos AS 'Multiplicador de Pontos', pr.nome_professor AS Professor
FROM sessao s JOIN dificuldade d USING (id_dificuldade)
			  JOIN professor pr USING (id_professor)
WHERE s.ativa = TRUE
ORDER BY d.ordem, s.nome_sessao;

-- buscar perguntas de uma sessao
SELECT pe.id_pergunta, pe.enunciado AS Enunciado, pe.tipo_pergunta AS 'Tipo Pergunta', pe.pontuacao AS Pontuação,
pe.id_imagem
FROM perguntas_sessao ps JOIN pergunta pe USING (id_pergunta)
WHERE ps.id_sessao = ? AND pe.ativo = TRUE -- WHERE ps.id_sessao = 1 AND pe.ativo = TRUE
ORDER BY RAND();

-- buscar alternativas de uma pergunta
SELECT id_alternativa, resposta AS Alternativa, tipo_alternativa AS 'Tipo Alternativa', id_imagem
FROM alternativa
WHERE id_pergunta = ? -- WHERE id_pergunta = 1
ORDER BY RAND();

-- verificar se a alternativa selecionada pelo aluno é a correta
SELECT correta
FROM alternativa
WHERE id_alternativa = ? ; -- WHERE id_alternativa = 1;

-- buscar ajudas disponíveis para uma pergunta 
SELECT id_ajuda, tipo_ajuda AS 'Tipo Ajuda', conteudo_ajuda AS Conteúdo, custo_pontos AS 'Custo de pontos'
FROM ajuda
WHERE id_pergunta = ?; -- WHERE id_pergunta = 1;



