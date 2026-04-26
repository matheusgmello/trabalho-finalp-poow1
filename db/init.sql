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
('Lab de Robótica', 'Engenharia', 'Drones de Entrega', 'Ativo', 10, 'Dr. Isaac Asimov');

INSERT INTO bolsista (nome, senha, data_nascimento, email, curso, tipo_usuario, ativo) VALUES 
('Administrador', 'teste123', '1990-01-01', 'admin@sisbolsa.com', 'Gestão', 'ADMIN', true);

INSERT INTO bolsista (nome, senha, email, curso, tipo_usuario, ativo, laboratorio_id) VALUES 
('João Silva', 'teste123', 'joao@teste.com', 'Ciência da Computação', 'BOLSISTA', true, 1),
('Maria Oliveira', 'teste123', 'maria@teste.com', 'Química', 'BOLSISTA', true, 2);

-- Algumas Frequências Iniciais
INSERT INTO frequencia (bolsista_id, data, horas_trabalhadas, descricao) VALUES 
(2, CURRENT_DATE - INTERVAL '1 day', 4.0, 'Pesquisa de algoritmos'),
(3, CURRENT_DATE - INTERVAL '1 day', 5.0, 'Experimentos em laboratório');
