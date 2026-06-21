# Roteiro de Apresentacao — SisBolsa (Codigo)

---

## LoginService.java
`service/LoginService.java`

Aqui esta o service responsavel pelo login. O ponto mais importante e que a senha nunca chega ao banco em texto puro — antes de qualquer consulta, ela passa 
pelo hashSenha da SecurityUtil, que gera um hash SHA-256. Alem disso, o sistema tenta autenticar primeiro como bolsista e, se nao encontrar, cai num fallback
para professor. Os dois tipos de usuario usam a mesma tela de login e o sistema resolve internamente quem e quem.

---

## AuthInterceptor.java
`config/AuthInterceptor.java`

Esse e o interceptor de autenticacao — funciona como um porteiro. Toda requisicao que chega ao sistema passa pelo metodo preHandle antes de chegar a qualquer
controller. Ele verifica se existe um usuario na sessao HTTP; se nao existir, redireciona para o login. 
Ha uma lista de rotas publicas que ficam de fora dessa verificacao, como a tela de login e os arquivos de CSS e JavaScript. 
Fora essas excecoes, nenhuma pagina e acessivel sem autenticacao.

---

## LaboratorioService.java
`service/LaboratorioService.java`

Esse service centraliza as regras de negocio dos laboratorios. O metodo podeGerenciar e chamado antes de qualquer edicao ou exclusao: se o usuario for admin,
libera direto sem consultar o banco; se for professor, busca o laboratorio no banco e verifica se ele e o coordenador. 
Tem tambem o metodo temVaga, que compara a capacidade maxima do lab com o numero atual de bolsistas vinculados — se estiver cheio, o cadastro e 
bloqueado antes de chegar ao banco.

---

## BolsistaController.java
`controller/BolsistaController.java`

Aqui esta o controller que cuida do CRUD de bolsistas e professores. Cada acao tem sua propria rota Spring MVC — /bolsista/novo, /bolsista/editar, 
/bolsista/excluir — sem o antigo padrao de parametro ?action= que misturava tudo em um metodo so. 
Antes de salvar qualquer dado, o controller chama os metodos de validacao e verifica as permissoes do usuario logado. 
Professores so conseguem editar bolsistas do proprio laboratorio; apenas admins mexem em outros professores.

---

## BolsistaDAO.java
`dao/BolsistaDAO.java`

Aqui esta a camada de acesso ao banco. O projeto usa JDBC direto, sem ORM, entao cada consulta e escrita manualmente em SQL. 
Toda query usa PreparedStatement com parametros marcados com interrogacao — o banco recebe os valores separados da estrutura da query e 
nunca interpreta o conteudo do usuario como codigo SQL, o que previne SQL injection. 
O metodo extrairBolsista transforma uma linha do ResultSet em objeto Java coluna por coluna. 
E as exclusoes sao logicas: o campo ativo e setado para false, o registro nunca e deletado fisicamente do banco.

---

## Ordem de abertura no IntelliJ

1. `service/LoginService.java`
2. `config/AuthInterceptor.java`
3. `service/LaboratorioService.java`
4. `controller/BolsistaController.java`
5. `dao/BolsistaDAO.java`

Abra todos antes de gravar e use Ctrl+Tab para alternar sem perder tempo.
