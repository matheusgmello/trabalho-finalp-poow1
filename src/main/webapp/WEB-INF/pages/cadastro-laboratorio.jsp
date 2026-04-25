<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${laboratorio != null ? 'Editar' : 'Novo'} Laboratório - SisBolsa</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <div class="container">
            <h1><i class="fas fa-flask"></i> ${laboratorio != null ? 'Editar' : 'Cadastrar Novo'} Laboratório</h1>
            
            <c:if test="${not empty erro}">
                <div class="error-msg" style="background-color: #f8d7da; color: #721c24; padding: 15px; border-radius: 5px; margin-bottom: 20px;">
                    <i class="fas fa-exclamation-circle"></i> ${erro}
                </div>
            </c:if>

            <form action="laboratorio" method="post" id="formLab">
                <input type="hidden" name="id" value="${laboratorio.id}">
                <div class="form-grid" style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
                    <div class="form-group" style="grid-column: span 2; display: flex; flex-direction: column;">
                        <label style="font-weight: 600; margin-bottom: 8px;">Nome do Laboratório</label>
                        <input type="text" name="nome" id="nome" value="${laboratorio.nome}" required minlength="3" style="padding: 12px; border: 1px solid #ddd; border-radius: 5px;">
                    </div>
                    <div class="form-group" style="display: flex; flex-direction: column;">
                        <label style="font-weight: 600; margin-bottom: 8px;">Área de Pesquisa</label>
                        <input type="text" name="areaPesquisa" id="areaPesquisa" value="${laboratorio.areaPesquisa}" required style="padding: 12px; border: 1px solid #ddd; border-radius: 5px;">
                    </div>
                    <div class="form-group" style="display: flex; flex-direction: column;">
                        <label style="font-weight: 600; margin-bottom: 8px;">Professor Coordenador</label>
                        <input type="text" name="coordenador" id="coordenador" value="${laboratorio.coordenador}" required style="padding: 12px; border: 1px solid #ddd; border-radius: 5px;">
                    </div>
                    <div class="form-group" style="display: flex; flex-direction: column;">
                        <label style="font-weight: 600; margin-bottom: 8px;">Capacidade Máxima</label>
                        <input type="number" name="capacidade" id="capacidade" value="${laboratorio.id > 0 ? laboratorio.capacidade : 10}" required min="1" style="padding: 12px; border: 1px solid #ddd; border-radius: 5px;">
                    </div>
                    <div class="form-group" style="display: flex; flex-direction: column;">
                        <label style="font-weight: 600; margin-bottom: 8px;">Status</label>
                        <select name="status" id="status" required style="padding: 12px; border: 1px solid #ddd; border-radius: 5px;">
                            <option value="">Selecione...</option>
                            <option value="Ativo" ${laboratorio.status == 'Ativo' ? 'selected' : ''}>Ativo</option>
                            <option value="Em Pausa" ${laboratorio.status == 'Em Pausa' ? 'selected' : ''}>Em Pausa</option>
                            <option value="Concluido" ${laboratorio.status == 'Concluido' ? 'selected' : ''}>Concluído</option>
                        </select>
                    </div>
                    <div class="form-group" style="grid-column: span 2; display: flex; flex-direction: column;">
                        <label style="font-weight: 600; margin-bottom: 8px;">Título do Projeto</label>
                        <input type="text" name="tituloProjeto" id="tituloProjeto" value="${laboratorio.tituloProjeto}" required minlength="5" style="padding: 12px; border: 1px solid #ddd; border-radius: 5px;">
                    </div>
                    <div class="actions" style="grid-column: span 2; display: flex; gap: 15px; margin-top: 20px;">
                        <button type="submit" class="btn btn-submit" style="background-color: var(--success-color); color: white; padding: 12px 25px; border-radius: 5px; font-weight: bold; border: none; flex: 1; cursor: pointer;"><i class="fas fa-save"></i> Salvar Laboratório</button>
                        <a href="laboratorio" class="btn btn-cancel" style="background-color: #95a5a6; color: white; padding: 12px 25px; border-radius: 5px; font-weight: bold; text-decoration: none; text-align: center; flex: 1;">Cancelar</a>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <script src="js/validacao-laboratorio.js"></script>
</body>
</html>
