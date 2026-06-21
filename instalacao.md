# Instalação e Execução do SisBolsa

## Pré-requisitos

| Ferramenta | Versão mínima | Observação |
|---|---|---|
| Java JDK | 21 | Necessário para compilar e rodar |
| Maven | 3.9+ | Gerenciamento de dependências e build |
| Docker + Docker Compose | Qualquer atual | Recomendado para o banco de dados |
| IntelliJ IDEA | Qualquer | Opcional — o projeto roda via Maven também |

> **PostgreSQL local** é uma alternativa ao Docker. Veja a Opção B abaixo.

---

## 1. Clonar o repositório

```bash
git clone <url-do-repositorio>
cd CadastroBolsistas
```

---

## 2. Configurar o banco de dados

### Opção A — Docker Compose (recomendado)

Na raiz do projeto, execute:

```bash
docker compose up -d
```

O Docker sobe o PostgreSQL na porta `5436` e executa o `db/init.sql` automaticamente,
criando as tabelas e inserindo os dados iniciais.

Comandos úteis:

```bash
# Parar (mantém os dados)
docker compose down

# Parar e apagar todos os dados
docker compose down -v

# Ver logs do container
docker compose logs -f
```

Configurações usadas pelo container:

```
Banco:   cadastroBolsista
Usuário: postgres
Senha:   1234
Porta:   5436
```

---

### Opção B — PostgreSQL local (sem Docker)

1. Abra o pgAdmin.
2. Crie um banco de dados chamado exatamente `cadastroBolsista` (B maiúsculo).
3. Abra o Query Tool no banco criado.
4. Execute o arquivo `db/init.sql`.

Após isso, ajuste a porta no arquivo de conexão:

```
src/main/java/dev/matheus/cadastroBolsistas/dao/ConectaDBPostgres.java
```

Altere a URL de `5436` para `5432` (porta padrão do PostgreSQL local):

```java
"jdbc:postgresql://localhost:5432/cadastroBolsista"
```

---

## 3. Rodar a aplicação

### Via Maven (recomendado para desenvolvimento)

```bash
mvn spring-boot:run
```

O Tomcat embarcado sobe na porta `8080`. Não é necessário instalar WildFly ou outro servidor.

### Via build WAR + WildFly (opcional, para deploy)

```bash
# Gera o WAR em target/cadastroBolsistas.war
mvn clean package
```

No IntelliJ IDEA:
1. Vá em `Run > Edit Configurations`.
2. Adicione uma configuração de servidor WildFly.
3. Selecione o artefato `cadastroBolsistas:war exploded`.
4. Inicie o servidor.

---

## 4. Rodar os testes

```bash
mvn test
```

A suíte possui 78 testes unitários e de controller (JUnit 5 + Mockito + MockMvc).
Nenhum teste requer banco de dados ativo — todos os DAOs são mockados.

---

## 5. Acessar o sistema

```
http://localhost:8080
```

---

## 6. Credenciais iniciais

O `db/init.sql` cria os seguintes usuários para teste:

### Administrador
```
E-mail: admin@sisbolsa.com
Senha:  12345678
Tipo:   ADMIN
```

### Professores coordenadores
```
roberto.mendes@sisbolsa.com  / 12345678  → Lab de Desenvolvimento de Software
carla.souza@sisbolsa.com     / 12345678  → Lab de Ciencias Biologicas
felipe.andrade@sisbolsa.com  / 12345678  → Lab de Engenharia Mecatronica
```

### Bolsistas (exemplos)
```
thiago.rocha@sisbolsa.com    / 12345678  → Lab de Desenvolvimento de Software
camila.pires@sisbolsa.com    / 12345678  → Lab de Desenvolvimento de Software
diego.almeida@sisbolsa.com   / 12345678  → Lab de Ciencias Biologicas
bruno.carvalho@sisbolsa.com  / 12345678  → Lab de Engenharia Mecatronica
```

> As senhas são armazenadas como hash SHA-256. O `init.sql` já contém os hashes
> pré-calculados — não é necessário nenhuma migração manual.

---

## 7. Cadastrar novos administradores

O sistema suporta no máximo 3 administradores. Para cadastrar um novo:

1. Faça login com uma conta ADMIN existente.
2. Acesse **Usuários** no menu lateral.
3. Clique em **Novo Usuário** e selecione o tipo `ADMIN`.

---

## 8. Estrutura do banco

O arquivo `db/init.sql` cria e popula as seguintes tabelas:

| Tabela | Conteúdo inicial |
|---|---|
| `professor` | 3 professores coordenadores |
| `laboratorio` | 3 laboratórios vinculados aos professores |
| `projeto` | 6 projetos (2 por laboratório) |
| `bolsista` | 7 bolsistas + 1 administrador |
| `bolsista_projeto` | Vínculos entre bolsistas e projetos |
| `frequencia` | 28 registros de horas (4 por bolsista) |

Para reiniciar o banco do zero:

```bash
docker compose down -v
docker compose up -d
```
