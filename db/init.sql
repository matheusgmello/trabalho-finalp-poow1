-- Criação das Tabelas
CREATE TABLE IF NOT EXISTS laboratorio (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    area_pesquisa VARCHAR(255),
    titulo_projeto VARCHAR(255),
    status VARCHAR(50),
    capacidade INTEGER DEFAULT 10,
    coordenador VARCHAR(255)
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
    foto_url VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS frequencia (
    id SERIAL PRIMARY KEY,
    bolsista_id INTEGER REFERENCES bolsista(id) ON DELETE CASCADE,
    data DATE NOT NULL,
    horas_trabalhadas DOUBLE PRECISION NOT NULL,
    descricao TEXT
);

-- Dados Iniciais
INSERT INTO laboratorio (nome, area_pesquisa, titulo_projeto, status, capacidade, coordenador) VALUES
('Lab de Inteligência Artificial', 'Computação', 'IA Generativa na Educação', 'Ativo', 15, 'Dr. Alan Turing'),
('Lab de Química Orgânica', 'Química', 'Síntese de Novos Medicamentos', 'Ativo', 8, 'Dra. Marie Curie'),
('Lab de Robótica', 'Engenharia', 'Drones de Entrega', 'Ativo', 10, 'Dr. Isaac Asimov'),
('Lab de Redes e Segurança', 'Computação', 'Detecção de Intrusão em Redes', 'Ativo', 12, 'Dr. Grace Hopper'),
('Lab de Biotecnologia', 'Biologia', 'Edição Genômica com CRISPR', 'Ativo', 6, 'Dra. Rosalind Franklin'),
('Lab de Energias Renováveis', 'Engenharia', 'Eficiência em Painéis Solares', 'Inativo', 10, 'Dr. Nikola Tesla');

INSERT INTO bolsista (nome, senha, data_nascimento, email, curso, tipo_usuario, ativo) VALUES
('Administrador', 'teste123', '1990-01-01', 'admin@sisbolsa.com', 'Gestão', 'ADMIN', true);

INSERT INTO bolsista (nome, senha, data_nascimento, email, curso, tipo_usuario, ativo, laboratorio_id) VALUES
('João Silva',       'teste123', '2001-03-15', 'joao@teste.com',       'Ciência da Computação', 'BOLSISTA', true,  1),
('Maria Oliveira',   'teste123', '2000-07-22', 'maria@teste.com',      'Química',                'BOLSISTA', true,  2),
('Carlos Souza',     'teste123', '2002-11-05', 'carlos@teste.com',     'Engenharia Mecânica',    'BOLSISTA', true,  3),
('Ana Lima',         'teste123', '2001-06-30', 'ana@teste.com',        'Ciência da Computação',  'BOLSISTA', true,  4),
('Pedro Ferreira',   'teste123', '2003-01-18', 'pedro@teste.com',      'Biologia',               'BOLSISTA', true,  5),
('Fernanda Costa',   'teste123', '2000-09-12', 'fernanda@teste.com',   'Engenharia Elétrica',    'BOLSISTA', true,  6),
('Lucas Martins',    'teste123', '2002-04-27', 'lucas@teste.com',      'Ciência da Computação',  'BOLSISTA', true,  1),
('Beatriz Santos',   'teste123', '2001-12-03', 'beatriz@teste.com',    'Química',                'BOLSISTA', true,  2),
('Rafael Alves',     'teste123', '2003-08-09', 'rafael@teste.com',     'Engenharia de Software', 'BOLSISTA', false, 3),
('Juliana Nunes',    'teste123', '2000-05-14', 'juliana@teste.com',    'Biomedicina',            'BOLSISTA', true,  5);

-- Algumas Frequências Iniciais
INSERT INTO frequencia (bolsista_id, data, horas_trabalhadas, descricao) VALUES
(2,  CURRENT_DATE - INTERVAL '1 day',  4.0, 'Pesquisa de algoritmos de aprendizado'),
(3,  CURRENT_DATE - INTERVAL '1 day',  5.0, 'Experimentos de síntese em laboratório'),
(4,  CURRENT_DATE - INTERVAL '2 days', 3.5, 'Montagem e calibração de protótipo'),
(5,  CURRENT_DATE - INTERVAL '1 day',  6.0, 'Análise de vulnerabilidades em redes'),
(6,  CURRENT_DATE - INTERVAL '3 days', 4.0, 'Cultivo e análise de amostras'),
(7,  CURRENT_DATE - INTERVAL '2 days', 5.5, 'Testes de eficiência em painéis solares'),
(8,  CURRENT_DATE - INTERVAL '1 day',  4.0, 'Desenvolvimento de modelo de classificação'),
(9,  CURRENT_DATE - INTERVAL '4 days', 3.0, 'Preparação de reagentes'),
(11, CURRENT_DATE - INTERVAL '1 day',  4.5, 'Implementação de módulo de autenticação'),
(12, CURRENT_DATE - INTERVAL '2 days', 6.0, 'Extração e análise de DNA');
