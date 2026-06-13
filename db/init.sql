-- Criação das Tabelas
CREATE TABLE IF NOT EXISTS professor (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    foto_url VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS laboratorio (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    area_pesquisa VARCHAR(255),
    status VARCHAR(50),
    capacidade INTEGER DEFAULT 10,
    coordenador_id INTEGER REFERENCES professor(id),
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS bolsista (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    data_nascimento DATE,
    curso VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    matricula VARCHAR(50),
    cpf VARCHAR(20),
    telefone VARCHAR(20),
    ativo BOOLEAN DEFAULT TRUE,
    laboratorio_id INTEGER REFERENCES laboratorio(id),
    tipo_usuario VARCHAR(20) DEFAULT 'BOLSISTA',
    foto_url VARCHAR(255),
    funcao VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS projeto (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    laboratorio_id INTEGER REFERENCES laboratorio(id),
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS bolsista_projeto (
    bolsista_id INTEGER REFERENCES bolsista(id) ON DELETE CASCADE,
    projeto_id INTEGER REFERENCES projeto(id) ON DELETE CASCADE,
    PRIMARY KEY (bolsista_id, projeto_id)
);

CREATE TABLE IF NOT EXISTS frequencia (
    id SERIAL PRIMARY KEY,
    bolsista_id INTEGER REFERENCES bolsista(id) ON DELETE CASCADE,
    data DATE NOT NULL,
    horas_trabalhadas DOUBLE PRECISION NOT NULL,
    descricao TEXT,
    ativo BOOLEAN DEFAULT TRUE
);

-- Dados Iniciais
INSERT INTO professor (nome, email, senha, ativo) VALUES
('Dr. Alan Turing', 'turing@sisbolsa.com', 'teste123', true),
('Dra. Marie Curie', 'curie@sisbolsa.com', 'teste123', true),
('Dr. Isaac Asimov', 'asimov@sisbolsa.com', 'teste123', true),
('Dr. Grace Hopper', 'hopper@sisbolsa.com', 'teste123', true),
('Dra. Rosalind Franklin', 'franklin@sisbolsa.com', 'teste123', true),
('Dr. Nikola Tesla', 'tesla@sisbolsa.com', 'teste123', true);

INSERT INTO laboratorio (nome, area_pesquisa, status, capacidade, coordenador_id, ativo) VALUES
('Lab de Inteligência Artificial', 'Computação', 'Ativo', 15, 1, true),
('Lab de Química Orgânica', 'Química', 'Ativo', 8, 2, true),
('Lab de Robótica', 'Engenharia', 'Ativo', 10, 3, true),
('Lab de Redes e Segurança', 'Computação', 'Ativo', 12, 4, true),
('Lab de Biotecnologia', 'Biologia', 'Ativo', 6, 5, true),
('Lab de Energias Renováveis', 'Engenharia', 'Em Pausa', 10, 6, true);

INSERT INTO projeto (nome, descricao, laboratorio_id, ativo) VALUES
('IA Generativa na Educação', 'Pesquisa de inteligência artificial aplicada à educação', 1, true),
('Síntese de Novos Medicamentos', 'Desenvolvimento de novos compostos químicos', 2, true),
('Drones de Entrega', 'Prototipação e controle de aeronaves não tripuladas', 3, true),
('Detecção de Intrusão em Redes', 'Algoritmos de segurança e análise de pacotes', 4, true),
('Edição Genômica com CRISPR', 'Estudos práticos sobre edição genética', 5, true),
('Eficiência em Painéis Solares', 'Aprimoramento de células fotovoltaicas', 6, true);

INSERT INTO bolsista (nome, senha, data_nascimento, email, curso, tipo_usuario, ativo) VALUES
('Administrador', 'teste123', '1990-01-01', 'admin@sisbolsa.com', 'Gestão', 'ADMIN', true);

INSERT INTO bolsista (nome, senha, data_nascimento, email, curso, tipo_usuario, ativo, laboratorio_id, funcao) VALUES
('João Silva',       'teste123', '2001-03-15', 'joao@teste.com',       'Ciência da Computação', 'BOLSISTA', true,  1, 'Desenvolvedor Backend'),
('Maria Oliveira',   'teste123', '2000-07-22', 'maria@teste.com',      'Química',                'BOLSISTA', true,  2, 'Pesquisadora Química'),
('Carlos Souza',     'teste123', '2002-11-05', 'carlos@teste.com',     'Engenharia Mecânica',    'BOLSISTA', true,  3, 'Prototipador Hardware'),
('Ana Lima',         'teste123', '2001-06-30', 'ana@teste.com',        'Ciência da Computação',  'BOLSISTA', true,  4, 'Analista de Segurança'),
('Pedro Ferreira',   'teste123', '2003-01-18', 'pedro@teste.com',      'Biologia',               'BOLSISTA', true,  5, 'Auxiliar de Laboratório'),
('Fernanda Costa',   'teste123', '2000-09-12', 'fernanda@teste.com',   'Engenharia Elétrica',    'BOLSISTA', true,  6, 'Projetista de Circuitos'),
('Lucas Martins',    'teste123', '2002-04-27', 'lucas@teste.com',      'Ciência da Computação',  'BOLSISTA', true,  1, 'Pesquisador de IA'),
('Beatriz Santos',   'teste123', '2001-12-03', 'beatriz@teste.com',    'Química',                'BOLSISTA', true,  2, 'Analista de Compostos'),
('Rafael Alves',     'teste123', '2003-08-09', 'rafael@teste.com',     'Engenharia de Software', 'BOLSISTA', false, 3, 'Desenvolvedor Embarcados'),
('Juliana Nunes',    'teste123', '2000-05-14', 'juliana@teste.com',    'Biomedicina',            'BOLSISTA', true,  5, 'Pesquisadora Biotec');

-- Vincula os bolsistas aos seus respectivos projetos
INSERT INTO bolsista_projeto (bolsista_id, projeto_id) VALUES
(2, 1),
(3, 2),
(4, 3),
(5, 4),
(6, 5),
(7, 6),
(8, 1),
(9, 2),
(10, 3),
(11, 5);

-- Algumas Frequências Iniciais
INSERT INTO frequencia (bolsista_id, data, horas_trabalhadas, descricao, ativo) VALUES
(2,  CURRENT_DATE - INTERVAL '1 day',  4.0, 'Pesquisa de algoritmos de aprendizado', true),
(3,  CURRENT_DATE - INTERVAL '1 day',  5.0, 'Experimentos de síntese em laboratório', true),
(4,  CURRENT_DATE - INTERVAL '2 days', 3.5, 'Montagem e calibração de protótipo', true),
(5,  CURRENT_DATE - INTERVAL '1 day',  6.0, 'Análise de vulnerabilidades em redes', true),
(6,  CURRENT_DATE - INTERVAL '3 days', 4.0, 'Cultivo e análise de amostras', true),
(7,  CURRENT_DATE - INTERVAL '2 days', 5.5, 'Testes de eficiência em painéis solares', true),
(8,  CURRENT_DATE - INTERVAL '1 day',  4.0, 'Desenvolvimento de modelo de classificação', true),
(9,  CURRENT_DATE - INTERVAL '4 days', 3.0, 'Preparação de reagentes', true),
(10, CURRENT_DATE - INTERVAL '2 days', 6.0, 'Extração e análise de DNA', true),
(11, CURRENT_DATE - INTERVAL '1 day',  4.5, 'Implementação de módulo de autenticação', true);
