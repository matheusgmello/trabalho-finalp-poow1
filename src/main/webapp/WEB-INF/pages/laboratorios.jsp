<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gerenciar Laboratórios - SisBolsa</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .header-actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }

        h1, h2 { color: var(--primary-color); margin: 0; }

        .btn-new {
            background-color: var(--success-color);
            color: white;
            padding: 12px 20px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            display: flex;
            align-items: center;
            gap: 10px;
            transition: background 0.3s;
        }

        .btn-new:hover { background-color: #219150; }

        /* Table Style */
        .table-container { overflow-x: auto; }

        table { width: 100%; border-collapse: collapse; }
        th { background-color: var(--primary-color); color: white; text-align: left; padding: 15px; }
        td { padding: 12px 15px; border-bottom: 1px solid #eee; }
        tr:hover { background-color: #f9f9f9; }

        .badge {
            padding: 5px 10px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: bold;
        }
        .status-ativo { background: #d4edda; color: #155724; }
        .status-em-pausa { background: #fff3cd; color: #856404; }
        .status-concluido { background: #cce5ff; color: #004085; }

        .actions-cell {
            display: flex;
            gap: 10px;
        }

        .btn-icon {
            padding: 5px 10px;
            border-radius: 3px;
            color: white;
            text-decoration: none;
            font-size: 0.9rem;
        }
        .btn-edit { background-color: var(--accent-color); }
        .btn-delete { background-color: var(--danger-color); }

        .error-msg {
            background-color: #f8d7da;
            color: #721c24;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <div class="header-actions">
            <h1>Gerenciar Laboratórios</h1>
            <a href="laboratorio?action=novo" class="btn-new">
                <i class="fas fa-plus"></i> Cadastrar Novo Laboratório
            </a>
        </div>

        <c:if test="${not empty erro}">
            <div class="error-msg">
                <i class="fas fa-exclamation-circle"></i> ${erro}
            </div>
        </c:if>

        <div class="container">
            <h2><i class="fas fa-list"></i> Listagem de Laboratórios</h2>

            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Nome</th>
                            <th>Área de Pesquisa</th>
                            <th>Título do Projeto</th>
                            <th>Status</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="lab" items="${listaLaboratorios}">
                            <tr>
                                <td><strong>${lab.nome}</strong></td>
                                <td>${lab.areaPesquisa}</td>
                                <td>${lab.tituloProjeto}</td>
                                <td>
                                    <span class="badge status-${lab.status.toLowerCase().replace(' ', '-')}">
                                        ${lab.status}
                                    </span>
                                </td>
                                <td class="actions-cell">
                                    <a href="laboratorio?action=detalhes&id=${lab.id}" class="btn-icon" style="background-color: var(--secondary-color);" title="Detalhes"><i class="fas fa-eye"></i></a>
                                    <a href="laboratorio?action=editar&id=${lab.id}" class="btn-icon btn-edit" title="Editar"><i class="fas fa-edit"></i></a>
                                    <a href="laboratorio?action=excluir&id=${lab.id}" class="btn-icon btn-delete" title="Excluir" onclick="return confirm('Tem certeza que deseja excluir este laboratório?')"><i class="fas fa-trash"></i></a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty listaLaboratorios}">
                            <tr>
                                <td colspan="5" style="text-align: center; padding: 30px; color: #999;">
                                    Nenhum laboratório encontrado.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</body>
</html>
