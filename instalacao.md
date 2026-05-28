# Instalação e Execução do SisBolsa

Este documento descreve como preparar o ambiente e executar o sistema SisBolsa pelo IntelliJ IDEA.

## Pré-requisitos

Antes de executar o projeto, instale:

- Java JDK
- Maven
- PostgreSQL (com pgAdmin) **ou** Docker e Docker Compose
- WildFly

## 1. Abrir o projeto

Abra a pasta do projeto no IntelliJ IDEA e aguarde a IDE carregar as dependências do Maven.

## 2. Configurar o banco de dados

Escolha uma das opções abaixo:

### Opção A — PostgreSQL local com pgAdmin (recomendado para laboratório)

1. Abra o pgAdmin.
2. Clique com botão direito em **Databases → Create → Database**.
3. Informe o nome: `cadastroBolsista` (exatamente assim, com B maiúsculo).
4. Clique em **Save**.
5. Clique com botão direito no banco criado → **Query Tool**.
6. Vá em **File → Open File**, navegue até a pasta do projeto e selecione `db/init.sql`.
7. Clique em **Execute (F5)**.

Configurações esperadas pelo sistema:

```text
Banco:   cadastroBolsista
Usuário: postgres
Senha:   1234
Porta:   5432
```

### Opção B — Docker Compose

Na raiz do projeto, execute:

```bash
docker compose up -d
```

O Docker sobe o PostgreSQL e executa o `db/init.sql` automaticamente.

Para parar (mantém os dados):

```bash
docker compose down
```

Para parar e apagar os dados:

```bash
docker compose down -v
```

## 3. Conferir a conexão com o banco

A conexão está configurada em:

```text
src/main/java/dao/ConectaDBPostgres.java
```

Configuração atual:

```text
jdbc:postgresql://localhost:5432/cadastroBolsista
Usuário: postgres
Senha:   1234
```

Se as credenciais do seu PostgreSQL forem diferentes, atualize esse arquivo antes de rodar.

## 4. Rodar pelo IntelliJ IDEA

1. Vá em `Run > Edit Configurations`.
2. Adicione uma configuração de servidor WildFly.
3. Selecione o artefato `cadastroBolsistas:war exploded`.
4. Inicie o servidor pela IDE.

O IntelliJ compilará e publicará o projeto automaticamente.

## 5. Acessar o sistema

```text
http://localhost:8080/cadastroBolsistas
```

## 6. Primeiro acesso

O `db/init.sql` já cria um administrador de exemplo:

```text
E-mail: admin@sisbolsa.com
Senha:  teste123
```

Caso queira criar um novo administrador do zero (banco vazio), acesse a tela de login e clique em **Cadastrar administrador**. O sistema permite até 3 administradores.

## 7. Como utilizar

Após entrar no sistema:

1. Acesse o menu **Laboratórios** — cadastre, edite, liste ou exclua laboratórios.
2. Acesse o menu **Bolsistas** — cadastre, edite, liste ou exclua bolsistas e vincule-os a laboratórios.
3. Acesse o menu **Frequência** — registre e edite horas trabalhadas.
4. Acesse o menu **Relatórios** — visualize os dados processados pelo sistema.
