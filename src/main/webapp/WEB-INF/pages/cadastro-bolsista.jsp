<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${bolsista != null ? 'Editar' : 'Novo'} Bolsista - SisBolsa</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <div class="container" style="max-width: 800px; margin: 0 auto;">
            <h1><i class="fas fa-user-plus"></i> ${bolsista != null ? 'Editar' : 'Cadastrar Novo'} Bolsista</h1>
            
            <c:if test="${not empty erro}">
                <div class="error-msg" style="background-color: #f8d7da; color: #721c24; padding: 15px; border-radius: 5px; margin-bottom: 20px;">
                    <i class="fas fa-exclamation-circle"></i> ${erro}
                </div>
            </c:if>

            <form action="bolsista" method="post" id="formBolsista">
                <input type="hidden" name="id" value="${bolsista.id}">
                <div class="form-grid" style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
                    <div class="form-group" style="grid-column: span 2; display: flex; flex-direction: column;">
                        <label style="font-weight: 600; margin-bottom: 8px;">Nome Completo</label>
                        <input type="text" name="nome" id="nome" value="${bolsista.nome}" required minlength="3" style="padding: 12px; border: 1px solid #ddd; border-radius: 5px;">
                    </div>
                    <div class="form-group" style="display: flex; flex-direction: column;">
                        <label style="font-weight: 600; margin-bottom: 8px;">Data de Nascimento</label>
                        <input type="date" name="dataNascimento" id="dataNascimento" value="${bolsista.dataNascimento}" required style="padding: 12px; border: 1px solid #ddd; border-radius: 5px;">
                    </div>
                    <div class="form-group" style="display: flex; flex-direction: column;">
                        <label style="font-weight: 600; margin-bottom: 8px;">Curso</label>
                        <input type="text" name="curso" id="curso" value="${bolsista.curso}" required style="padding: 12px; border: 1px solid #ddd; border-radius: 5px;">
                    </div>
                    <div class="form-group" style="display: flex; flex-direction: column;">
                        <label style="font-weight: 600; margin-bottom: 8px;">E-mail Acadêmico</label>
                        <input type="email" name="email" id="email" value="${bolsista.email}" required style="padding: 12px; border: 1px solid #ddd; border-radius: 5px;">
                    </div>
                    <div class="form-group" style="display: flex; flex-direction: column;">
                        <label style="font-weight: 600; margin-bottom: 8px;">Matrícula</label>
                        <input type="text" name="matricula" id="matricula" value="${bolsista.matricula}" required style="padding: 12px; border: 1px solid #ddd; border-radius: 5px;">
                    </div>
                    <c:if test="${usuario.admin}">
                        <div class="form-group" style="display: flex; flex-direction: column;">
                            <label style="font-weight: 600; margin-bottom: 8px;">Tipo de Usuário</label>
                            <select name="tipoUsuario" style="padding: 12px; border: 1px solid #ddd; border-radius: 5px;">
                                <option value="BOLSISTA" ${bolsista.tipoUsuario == 'BOLSISTA' ? 'selected' : ''}>Bolsista Comum</option>
                                <option value="ADMIN" ${bolsista.tipoUsuario == 'ADMIN' ? 'selected' : ''}>Administrador</option>
                            </select>
                        </div>
                    </c:if>
                    <div class="form-group" style="display: flex; flex-direction: column;">
                        <label style="font-weight: 600; margin-bottom: 8px;">Laboratório</label>
                        <select name="laboratorioId" style="padding: 12px; border: 1px solid #ddd; border-radius: 5px;">
                            <option value="">Selecione...</option>
                            <c:forEach var="lab" items="${laboratorios}">
                                <option value="${lab.id}" ${bolsista.laboratorioId == lab.id ? 'selected' : ''}>${lab.nome}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group" style="display: flex; flex-direction: column;">
                        <label style="font-weight: 600; margin-bottom: 8px;">URL da Foto de Perfil</label>
                        <input type="text" name="fotoUrl" value="${bolsista.fotoUrl}" placeholder="https://..." style="padding: 12px; border: 1px solid #ddd; border-radius: 5px;">
                    </div>
                    <div class="form-group" style="display: flex; flex-direction: column;">
                        <label style="font-weight: 600; margin-bottom: 8px;">Senha</label>
                        <input type="password" name="senha" id="senha" value="${bolsista.senha}" required minlength="6" style="padding: 12px; border: 1px solid #ddd; border-radius: 5px;">
                    </div>
                    <div class="actions" style="grid-column: span 2; display: flex; gap: 15px; margin-top: 20px;">
                        <button type="submit" class="btn btn-submit" style="background-color: var(--success-color); color: white; padding: 12px 25px; border-radius: 5px; border: none; flex: 1; cursor: pointer; font-weight: bold;"><i class="fas fa-save"></i> Salvar</button>
                        <a href="bolsista" class="btn btn-cancel" style="background-color: #95a5a6; color: white; padding: 12px 25px; border-radius: 5px; text-decoration: none; text-align: center; flex: 1; font-weight: bold;">Cancelar</a>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <script src="js/validacao-bolsista.js"></script>
</body>
</html>
