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
    <link rel="stylesheet" href="css/cadastro-laboratorio.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <div class="container">
            <h1><i class="fas fa-flask"></i> ${laboratorio != null ? 'Editar' : 'Cadastrar Novo'} Laboratório</h1>
            
            <c:if test="${not empty erro}">
                <div class="error-msg">
                    <i class="fas fa-exclamation-circle"></i> ${erro}
                </div>
            </c:if>

            <form action="laboratorio" method="post" id="formLab">
                <input type="hidden" name="id" value="${laboratorio.id}">
                <div class="form-grid">
                    <div class="form-group form-group-full">
                        <label>Nome do Laboratório</label>
                        <input type="text" name="nome" id="nome" value="${laboratorio.nome}" required minlength="3">
                    </div>
                    <div class="form-group">
                        <label>Área de Pesquisa</label>
                        <input type="text" name="areaPesquisa" id="areaPesquisa" value="${laboratorio.areaPesquisa}" required>
                    </div>
                    <div class="form-group">
                        <label>Professor Coordenador</label>
                        <input type="text" name="coordenador" id="coordenador" value="${laboratorio.coordenador}" required>
                    </div>
                    <div class="form-group">
                        <label>Capacidade Máxima</label>
                        <input type="number" name="capacidade" id="capacidade" value="${laboratorio.id > 0 ? laboratorio.capacidade : 10}" required min="1">
                    </div>
                    <div class="form-group">
                        <label>Status</label>
                        <select name="status" id="status" required>
                            <option value="">Selecione...</option>
                            <option value="Ativo" ${laboratorio.status == 'Ativo' ? 'selected' : ''}>Ativo</option>
                            <option value="Em Pausa" ${laboratorio.status == 'Em Pausa' ? 'selected' : ''}>Em Pausa</option>
                            <option value="Concluido" ${laboratorio.status == 'Concluido' ? 'selected' : ''}>Concluído</option>
                        </select>
                    </div>
                    <div class="form-group form-group-full">
                        <label>Título do Projeto</label>
                        <input type="text" name="tituloProjeto" id="tituloProjeto" value="${laboratorio.tituloProjeto}" required minlength="5">
                    </div>
                    <div class="actions">
                        <button type="submit" class="btn btn-submit"><i class="fas fa-save"></i> Salvar Laboratório</button>
                        <a href="laboratorio" class="btn btn-cancel">Cancelar</a>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <script src="js/validacao-laboratorio.js"></script>
</body>
</html>
