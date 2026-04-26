# SisBolsa - Cadastro de Bolsistas e Laboratórios

Sistema web desenvolvido para gerenciamento de bolsistas, laboratórios de pesquisa e registros de frequência. O projeto permite cadastrar usuários bolsistas, organizar bolsistas por laboratório, registrar horas trabalhadas e visualizar relatórios analíticos com base nas informações cadastradas.

## Descrição do Sistema

O SisBolsa é uma aplicação web para apoio à gestão de bolsistas em laboratórios acadêmicos. O sistema possui autenticação por login, controle de sessão, cadastro de bolsistas, cadastro de laboratórios, registro de frequência e geração de relatórios.

Usuários administradores podem gerenciar bolsistas e laboratórios. Usuários bolsistas podem acessar o sistema, visualizar informações e registrar sua própria frequência.

## Preview do Projeto
![Preview](docs/images/previw.gif)

## Arquitetura do Projeto

O projeto segue uma arquitetura em camadas baseada no padrão MVC.

- `model`: classes que representam as entidades do sistema.
- `controller`: Servlets responsáveis por receber requisições HTTP e direcionar fluxos.
- `service`: camada de regras de negócio.
- `dao`: camada de acesso ao banco de dados usando JDBC.
- `webapp`: páginas JSP, arquivos CSS, JavaScript e componentes visuais.
- `db`: scripts SQL de criação e população inicial do banco.

Fluxo principal:

1. O usuário acessa uma página JSP.
2. A requisição é enviada para um Servlet.
3. O Servlet chama uma classe Service.
4. A Service chama uma classe DAO.
5. A DAO executa comandos SQL no PostgreSQL.
6. O resultado retorna para o Servlet.
7. O Servlet encaminha os dados para a JSP.

## Funcionalidades

### Autenticação e Sessão

- Login por e-mail e senha.
- Armazenamento do usuário autenticado na sessão.
- Bloqueio de acesso às páginas internas quando não há usuário logado.
- Diferenciação entre usuário `ADMIN` e usuário `BOLSISTA`.

### CRUD de Bolsistas

O sistema permite:

- Cadastrar bolsista.
- Listar bolsistas.
- Editar bolsista.
- Excluir bolsista.
- Buscar bolsistas por nome.
- Buscar bolsistas por curso.
- Exportar lista de bolsistas em CSV.

Campos principais:

- Nome
- Data de nascimento
- Curso
- E-mail
- Matrícula
- CPF
- Telefone
- Senha
- Laboratório
- Tipo de usuário
- Status ativo
- Foto

### CRUD de Laboratórios

O sistema permite:

- Cadastrar laboratório.
- Listar laboratórios.
- Editar laboratório.
- Excluir laboratório.
- Visualizar detalhes do laboratório.
- Listar bolsistas vinculados ao laboratório.

Campos principais:

- Nome
- Área de pesquisa
- Título do projeto
- Status
- Capacidade máxima
- Coordenador

### Registro de Frequência

O sistema permite que bolsistas registrem suas horas trabalhadas.

Campos principais:

- Bolsista
- Data
- Horas trabalhadas
- Descrição das atividades

Administradores conseguem visualizar os registros de frequência e excluir registros.

### Relatórios

O sistema possui uma tela de relatórios que processa as informações cadastradas e exibe:

- Total de bolsistas.
- Total de bolsistas ativos.
- Total de laboratórios.
- Quantidade de bolsistas por curso.
- Quantidade de laboratórios por status.

Essa funcionalidade atende ao requisito de processamento das informações inseridas nos CRUDs.

## Estrutura do Banco de Dados

O banco de dados possui três tabelas principais:

- `laboratorio`
- `bolsista`
- `frequencia`

Relacionamentos:

- Um laboratório pode possuir vários bolsistas.
- Um bolsista pertence a zero ou um laboratório.
- Um bolsista pode possuir vários registros de frequência.
- Uma frequência pertence a um bolsista.

## Modelo ER
![Modelo ER](docs/images/diagrama-er.png)

## Script do Banco de Dados

O script principal está em:

```text
db/init.sql
```

Ele cria as tabelas:

- `laboratorio`
- `bolsista`
- `frequencia`

Também insere dados iniciais para teste, incluindo laboratórios, bolsistas e um usuário administrador.

## Instalação e Execução

As instruções completas para instalar e rodar o projeto estão no arquivo:

[instalacao.md](instalacao.md)

## Usuário Inicial

O script `db/init.sql` cria um usuário administrador para acesso inicial:

```text
E-mail: admin@sisbolsa.com
Senha: teste123
Tipo: ADMIN
```

Também são criados bolsistas e laboratórios de exemplo.

## Como Utilizar

1. Acesse a tela de login.
2. Entre com o usuário administrador.
3. Use o menu lateral para navegar entre as áreas do sistema.
4. Cadastre laboratórios.
5. Cadastre bolsistas e vincule-os a laboratórios.
6. Registre frequências.
7. Acesse a tela de relatórios para visualizar os dados processados.
