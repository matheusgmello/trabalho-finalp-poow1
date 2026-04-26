# Instalação e Execução do SisBolsa

Este documento descreve como preparar o ambiente, subir o banco de dados e executar o sistema SisBolsa pelo IntelliJ IDEA.

## Pré-requisitos

Antes de executar o projeto, instale:

- Java JDK
- Maven
- Docker
- Docker Compose
- WildFly

## 1. Abrir o projeto

Abra a pasta do projeto:

```bash
CadastroBolsistas
```

Ou, pelo terminal:

```bash
cd CadastroBolsistas
```

## 2. Subir o banco PostgreSQL

Na raiz do projeto, execute:

```bash
docker compose up -d
```

Esse comando cria um container PostgreSQL usando o arquivo:

```text
docker-compose.yml
```

Configurações do banco:

```text
Banco: cadastroBolsista
Usuário: postgres
Senha: 1234
Porta local: 5436
```

Durante a criação do container, o Docker executa automaticamente o script:

```text
db/init.sql
```

Esse script cria as tabelas e insere dados iniciais.

## 3. Conferir a conexão com o banco

A conexão usada pelo sistema está configurada em:

```text
src/main/java/dao/ConectaDBPostgres.java
```

Configuração atual:

```text
jdbc:postgresql://localhost:5436/cadastroBolsista
Usuário: postgres
Senha: 1234
```

Se alterar usuário, senha, banco ou porta no Docker, atualize também esse arquivo.

## 4. Rodar pelo IntelliJ IDEA

Abra o projeto no IntelliJ IDEA e aguarde a IDE carregar as dependências do Maven.

Depois, configure um servidor de aplicação compatível com Jakarta Servlet, como WildFly
 
No IntelliJ:

- Vá em `Run > Edit Configurations`.
- Adicione uma configuração do servidor.
- Selecione o artefato do projeto `cadastroBolsista:war exploded` ou `cadastroBolsistas:war exploded`.
- Inicie o servidor pela própria IDE.

O IntelliJ fará a compilação e publicação do projeto automaticamente.

## 5. Acessar o sistema

Após iniciar o servidor pelo IntelliJ, acesse pelo navegador conforme a porta configurada.

Exemplo comum:

```text
http://localhost:8080/cadastroBolsistas
```

## 6. Usuário inicial

O script `db/init.sql` cria um usuário administrador:

```text
E-mail: admin@sisbolsa.com
Senha: teste123
Tipo: ADMIN
```

Use esse usuário para acessar o sistema pela primeira vez.

## 7. Como utilizar

Após entrar no sistema:

1. Acesse o menu `Laboratórios`.
2. Cadastre, edite, liste ou exclua laboratórios.
3. Acesse o menu `Bolsistas`.
4. Cadastre, edite, liste ou exclua bolsistas.
5. Vincule bolsistas aos laboratórios cadastrados.
6. Acesse o menu `Frequência`.
7. Registre horas trabalhadas.
8. Acesse o menu `Relatórios`.
9. Visualize os dados processados pelo sistema.

## 8. Parar o banco de dados

Para parar o container PostgreSQL:

```bash
docker compose down
```

Para parar e remover também o volume com os dados:

```bash
docker compose down -v
```

Use `docker compose down -v` apenas se quiser recriar o banco do zero.
