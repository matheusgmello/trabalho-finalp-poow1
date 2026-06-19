-- Limpa Tabelas Existentes
DROP TABLE IF EXISTS frequencia CASCADE;
DROP TABLE IF EXISTS bolsista_projeto CASCADE;
DROP TABLE IF EXISTS projeto CASCADE;
DROP TABLE IF EXISTS bolsista CASCADE;
DROP TABLE IF EXISTS laboratorio CASCADE;
DROP TABLE IF EXISTS professor CASCADE;

-- Criação das Tabelas
CREATE TABLE IF NOT EXISTS professor (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    foto_url VARCHAR(255),
    bio TEXT
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
    cargo VARCHAR(50), -- Renomeado de funcao para cargo para consistência com o backend (Cargo.java)
    bio TEXT
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

-- Dados Iniciais - Professores Coordenadores
INSERT INTO professor (nome, email, senha, ativo, foto_url, bio) VALUES
('Dr. Alan Turing', 'turing@sisbolsa.com', 'teste123', true, 'https://i.pravatar.cc/150?img=68', 'Pioneiro da computação teórica e inteligência artificial. Coordenador do Lab de IA e pesquisador nível 1A do CNPq.'),
('Dra. Marie Curie', 'curie@sisbolsa.com', 'teste123', true, 'https://i.pravatar.cc/150?img=49', 'Pesquisadora pioneira no estudo da radioatividade, com foco em novas aplicações biológicas e medicina de precisão.'),
('Dr. Isaac Asimov', 'asimov@sisbolsa.com', 'teste123', true, 'https://i.pravatar.cc/150?img=11', 'Bioquímico e idealizador das leis da robótica. Atua no desenvolvimento de sistemas robóticos autônomos e segurança cibernética.'),
('Dra. Grace Hopper', 'hopper@sisbolsa.com', 'teste123', true, 'https://i.pravatar.cc/150?img=47', 'Desenvolvedora do primeiro compilador de software e professora titular. Especialista em segurança de redes e criptologia.'),
('Dra. Rosalind Franklin', 'franklin@sisbolsa.com', 'teste123', true, 'https://i.pravatar.cc/150?img=35', 'Biofísica e especialista em cristalografia. Coordena estudos avançados em engenharia genética e biotecnologia celular.'),
('Dr. Nikola Tesla', 'tesla@sisbolsa.com', 'teste123', true, 'https://i.pravatar.cc/150?img=33', 'Pesquisador em engenharia elétrica e telecomunicações. Coordena estudos sobre sistemas de distribuição de energia renovável.');

-- Dados Iniciais - Laboratórios
INSERT INTO laboratorio (nome, area_pesquisa, status, capacidade, coordenador_id, ativo) VALUES
('Lab de Inteligência Artificial', 'Computação', 'Ativo', 15, 1, true),
('Lab de Química Orgânica', 'Química', 'Ativo', 8, 2, true),
('Lab de Robótica', 'Engenharia', 'Ativo', 10, 3, true),
('Lab de Redes e Segurança', 'Computação', 'Ativo', 12, 4, true),
('Lab de Biotecnologia', 'Biologia', 'Ativo', 6, 5, true),
('Lab de Energias Renováveis', 'Engenharia', 'Em Pausa', 10, 6, true);

-- Dados Iniciais - Projetos
INSERT INTO projeto (nome, descricao, laboratorio_id, ativo) VALUES
('IA Generativa na Educação', 'Pesquisa e desenvolvimento de sistemas tutoriais inteligentes baseados em LLMs para personalização do aprendizado acadêmico.', 1, true),
('Síntese de Novos Medicamentos', 'Modelagem computacional e síntese laboratorial de novos compostos químicos para tratamento de doenças raras.', 2, true),
('Drones de Entrega', 'Desenvolvimento de hardware, controle dinâmico e algoritmos de desvio de obstáculos para aeronaves autônomas de entrega.', 3, true),
('Detecção de Intrusão em Redes', 'Criação de modelos de aprendizado de máquina aplicados à detecção precoce de anomalias em tráfego de rede corporativa.', 4, true),
('Edição Genômica com CRISPR', 'Otimização de guias de RNA para o sistema CRISPR-Cas9 aplicados à edição de genes específicos de resistência a patógenos.', 5, true),
('Eficiência em Painéis Solares', 'Pesquisa de novas películas refletoras e posicionadores automáticos em painéis solares para otimização da absorção luminosa.', 6, true);

-- Dados Iniciais - Bolsistas / Admins
INSERT INTO bolsista (nome, senha, data_nascimento, email, curso, matricula, cpf, telefone, ativo, laboratorio_id, tipo_usuario, foto_url, cargo, bio) VALUES
('Administrador do Sistema', 'teste123', '1990-01-01', 'admin@sisbolsa.com', 'Gestão de Sistemas', 'ADM001', '999.999.999-99', '(11) 99999-9999', true, NULL, 'ADMIN', 'https://i.pravatar.cc/150?img=60', NULL, 'Administrador geral da plataforma de gestão. Responsável pela gestão de cadastros, aprovação de laboratórios e auditoria geral dos dados.'),
('João Silva', 'teste123', '2001-03-15', 'joao@teste.com', 'Ciência da Computação', '2021100123', '111.222.333-44', '(11) 91111-2222', true, 1, 'BOLSISTA', 'https://i.pravatar.cc/150?img=12', 'DESENVOLVEDOR', 'Desenvolvedor backend com foco em Java e arquiteturas baseadas em microsserviços. Interessado em algoritmos de busca e processamento distribuído.'),
('Maria Oliveira', 'teste123', '2000-07-22', 'maria@teste.com', 'Química', '2020100456', '222.333.444-55', '(11) 92222-3333', true, 2, 'BOLSISTA', 'https://i.pravatar.cc/150?img=43', 'PESQUISADOR', 'Pesquisadora em bioquímica, dedicada a ensaios biológicos in vitro e análise espectrofotométrica de compostos moleculares complexos.'),
('Carlos Souza', 'teste123', '2002-11-05', 'carlos@teste.com', 'Engenharia Mecânica', '2022100789', '333.444.555-66', '(11) 93333-4444', true, 3, 'BOLSISTA', 'https://i.pravatar.cc/150?img=59', 'LIDER_TECNICO', 'Líder técnico da equipe de hardware. Focado no design estrutural de braços robóticos e integração de sensores inerciais de alta precisão.'),
('Ana Lima', 'teste123', '2001-06-30', 'ana@teste.com', 'Ciência da Computação', '2021100987', '444.555.666-77', '(11) 94444-5555', true, 4, 'BOLSISTA', 'https://i.pravatar.cc/150?img=45', 'DESENVOLVEDOR', 'Desenvolvedora front-end e analista de redes. Focada no desenvolvimento de painéis interativos de monitoramento de incidentes de segurança cibernética.'),
('Pedro Ferreira', 'teste123', '2003-01-18', 'pedro@teste.com', 'Biologia', '2023100654', '555.666.777-88', '(11) 95555-6666', true, 5, 'BOLSISTA', 'https://i.pravatar.cc/150?img=52', 'AUXILIAR', 'Auxiliar de laboratório encarregado do cultivo microbiológico, calibração e assepsia de instrumentos e controle de inventário de insumos.'),
('Fernanda Costa', 'teste123', '2000-09-12', 'fernanda@teste.com', 'Engenharia Elétrica', '2020100321', '666.777.888-99', '(11) 96666-7777', true, 6, 'BOLSISTA', 'https://i.pravatar.cc/150?img=20', 'PESQUISADOR', 'Pesquisadora em eletrônica de potência. Dedica-se a ensaios experimentais de conversores CC-CC para painéis fotovoltaicos bifaciais.'),
('Lucas Martins', 'teste123', '2002-04-27', 'lucas@teste.com', 'Ciência da Computação', '2022100147', '777.888.999-00', '(11) 97777-8888', true, 1, 'BOLSISTA', 'https://i.pravatar.cc/150?img=18', 'PESQUISADOR', 'Pesquisador em inteligência artificial. Trabalha na sintonia fina de modelos pré-treinados de linguagem para classificação de respostas estudantis.'),
('Beatriz Santos', 'teste123', '2001-12-03', 'beatriz@teste.com', 'Química', '2021100258', '888.999.000-11', '(11) 98888-9999', true, 2, 'BOLSISTA', 'https://i.pravatar.cc/150?img=30', 'DESIGNER', 'Designer de IHC para aplicações científicas. Responsável pela modelagem visual e arquitetura de informação dos softwares gerados no laboratório.'),
('Rafael Alves', 'teste123', '2003-08-09', 'rafael@teste.com', 'Engenharia de Software', '2023100369', '999.000.111-22', '(11) 99999-0000', true, 3, 'BOLSISTA', 'https://i.pravatar.cc/150?img=65', 'DESENVOLVEDOR', 'Desenvolvedor de sistemas embarcados. Focado na programação de firmwares em C++ para microcontroladores aplicados na comunicação do drone.'),
('Juliana Nunes', 'teste123', '2000-05-14', 'juliana@teste.com', 'Biomedicina', '2020100741', '000.111.222-33', '(11) 90000-1111', true, 5, 'BOLSISTA', 'https://i.pravatar.cc/150?img=28', 'PESQUISADOR', 'Pesquisadora em biologia molecular. Realiza análises bioinformáticas e alinhamento de sequências genéticas para validação de clones mutados.');

-- Vincula os bolsistas aos seus respectivos projetos (tabela bolsista_projeto)
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

-- Frequências Iniciais (Múltiplas por Bolsista para histórico completo e realista)
INSERT INTO frequencia (bolsista_id, data, horas_trabalhadas, descricao, ativo) VALUES
-- João Silva (ID 2)
(2,  CURRENT_DATE - INTERVAL '1 day',  4.0, 'Estudo dirigido sobre Transformers e sintonia fina de modelos Llama 3.', true),
(2,  CURRENT_DATE - INTERVAL '2 days', 4.0, 'Implementação da API Flask para servir o modelo de classificação de redações.', true),
(2,  CURRENT_DATE - INTERVAL '3 days', 4.0, 'Reunião semanal com o coordenador Alan Turing para apresentação de métricas F1-Score.', true),

-- Maria Oliveira (ID 3)
(3,  CURRENT_DATE - INTERVAL '1 day',  5.0, 'Preparação de catalisadores metálicos para reações de oxidação sob temperatura controlada.', true),
(3,  CURRENT_DATE - INTERVAL '2 days', 5.0, 'Purificação do produto sintetizado por meio de cromatografia em coluna rápida.', true),
(3,  CURRENT_DATE - INTERVAL '4 days', 4.0, 'Análise dos espectros obtidos por ressonância magnética nuclear (RMN).', true),

-- Carlos Souza (ID 4)
(4,  CURRENT_DATE - INTERVAL '2 days', 3.5, 'Soldagem dos conectores da placa de controle principal e fixação de motores do drone.', true),
(4,  CURRENT_DATE - INTERVAL '3 days', 4.0, 'Configuração da controladora de voo e testes iniciais de calibração do giroscópio.', true),
(4,  CURRENT_DATE - INTERVAL '5 days', 4.5, 'Modelagem em CAD 3D de um novo suporte leve de bateria resistente a impactos.', true),

-- Ana Lima (ID 5)
(5,  CURRENT_DATE - INTERVAL '1 day',  6.0, 'Coleta de pacotes maliciosos simulados em ambiente controlado com Wireshark.', true),
(5,  CURRENT_DATE - INTERVAL '2 days', 6.0, 'Configuração de regras personalizadas no Snort (IDS) para alertar contra varreduras Nmap.', true),

-- Pedro Ferreira (ID 6)
(6,  CURRENT_DATE - INTERVAL '3 days', 4.0, 'Preparo de meios de cultura ágar LB e esterilização na autoclave.', true),
(6,  CURRENT_DATE - INTERVAL '4 days', 4.0, 'Semeadura das colônias mutadas sob fluxo laminar estéril.', true),

-- Fernanda Costa (ID 7)
(7,  CURRENT_DATE - INTERVAL '2 days', 5.5, 'Medição da curva I-V dos painéis solares sob variação de temperatura controlada.', true),
(7,  CURRENT_DATE - INTERVAL '3 days', 5.5, 'Desenvolvimento do algoritmo MPPT (Maximum Power Point Tracking) em código C.', true),

-- Lucas Martins (ID 8)
(8,  CURRENT_DATE - INTERVAL '1 day',  4.0, 'Tratamento inicial de dataset com remoção de stopwords e lematização em Python.', true),
(8,  CURRENT_DATE - INTERVAL '2 days', 4.0, 'Treinamento de classificador de texto Naive Bayes para base de comparação preliminar.', true),

-- Beatriz Santos (ID 9)
(9,  CURRENT_DATE - INTERVAL '4 days', 3.0, 'Criação dos wireframes de baixa fidelidade para o sistema de catálogo de polímeros.', true),
(9,  CURRENT_DATE - INTERVAL '5 days', 3.0, 'Condução de teste de usabilidade informal com três usuários do laboratório.', true),

-- Rafael Alves (ID 10)
(10, CURRENT_DATE - INTERVAL '2 days', 6.0, 'Programação do protocolo de comunicação SPI entre o sensor inercial e o microcontrolador.', true),
(10, CURRENT_DATE - INTERVAL '3 days', 6.0, 'Depuração de código e tratamento de interrupções de hardware para estabilizar telemetria.', true),

-- Juliana Nunes (ID 11)
(11, CURRENT_DATE - INTERVAL '1 day',  4.5, 'Pesquisa e alinhamento de sequências usando BLAST para verificar sucesso da inserção CRISPR.', true),
(11, CURRENT_DATE - INTERVAL '2 days', 4.5, 'Geração de gráficos comparativos de expressão gênica e elaboração de relatório técnico.', true);
