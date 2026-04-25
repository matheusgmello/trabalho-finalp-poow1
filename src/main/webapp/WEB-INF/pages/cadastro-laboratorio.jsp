<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${laboratorio != null ? 'Editar' : 'Novo'} Laboratório - SisBolsa</title>
    <style>
        :root {
            --primary-color: #2c3e50;
            --secondary-color: #34495e;
            --accent-color: #3498db;
            --text-color: #ecf0f1;
            --bg-color: #f4f7f6;
            --sidebar-width: 250px;
            --success-color: #27ae60;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            display: flex;
            background-color: var(--bg-color);
        }

        /* Sidebar */
        .sidebar {
            width: var(--sidebar-width);
            height: 100vh;
            background-color: var(--primary-color);
            color: var(--text-color);
            position: fixed;
            display: flex;
            flex-direction: column;
            padding: 20px 0;
        }

        .sidebar h2 {
            text-align: center;
            font-size: 1.5rem;
            margin-bottom: 30px;
            color: var(--accent-color);
        }

        .sidebar ul {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .sidebar ul li {
            padding: 15px 25px;
            transition: background 0.3s;
        }

        .sidebar ul li:hover {
            background-color: var(--secondary-color);
        }

        .sidebar ul li a {
            color: var(--text-color);
            text-decoration: none;
            display: flex;
            align-items: center;
            font-weight: 500;
        }

        .logout-btn {
            margin-top: auto;
            margin-bottom: 20px;
            padding: 15px 25px;
            background-color: #e74c3c;
            text-align: center;
            color: white;
            text-decoration: none;
            font-weight: bold;
            transition: background 0.3s;
        }

        /* Main Content */
        .main-content {
            margin-left: var(--sidebar-width);
            flex: 1;
            padding: 30px;
        }

        .container {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.05);
            max-width: 800px;
            margin: 0 auto;
        }

        h1 { color: var(--primary-color); text-align: center; margin-bottom: 30px; }

        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }

        .form-group { display: flex; flex-direction: column; }
        .form-group.full-width { grid-column: span 2; }

        label { font-weight: 600; margin-bottom: 8px; color: var(--secondary-color); }
        input, select { padding: 12px; border: 1px solid #ddd; border-radius: 5px; font-size: 14px; }

        .actions {
            grid-column: span 2;
            display: flex;
            gap: 15px;
            margin-top: 20px;
        }

        .btn {
            padding: 12px 25px;
            border-radius: 5px;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
            text-align: center;
            flex: 1;
            border: none;
        }

        .btn-submit { background-color: var(--success-color); color: white; }
        .btn-cancel { background-color: #95a5a6; color: white; }

        .btn:hover { opacity: 0.9; }

        .error-msg {
            background-color: #f8d7da;
            color: #721c24;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            grid-column: span 2;
        }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <div class="sidebar">
        <h2>SisBolsa</h2>
        <ul>
            <li><a href="dashboard"><i class="fas fa-home" style="margin-right: 10px;"></i> Dashboard</a></li>
            <li><a href="bolsista"><i class="fas fa-user-graduate" style="margin-right: 10px;"></i> Bolsistas</a></li>
            <li><a href="laboratorio"><i class="fas fa-flask" style="margin-right: 10px;"></i> Laboratórios</a></li>
        </ul>
        <a href="index.jsp" class="logout-btn"><i class="fas fa-sign-out-alt"></i> Sair</a>
    </div>

    <div class="main-content">
        <div class="container">
            <h1><i class="fas fa-flask"></i> ${laboratorio != null ? 'Editar' : 'Cadastrar Novo'} Laboratório</h1>
            
            <c:if test="${not empty erro}">
                <div class="error-msg">
                    <i class="fas fa-exclamation-circle"></i> ${erro}
                </div>
            </c:if>

            <form action="laboratorio" method="post">
                <input type="hidden" name="id" value="${laboratorio.id}">
                <div class="form-grid">
                    <div class="form-group full-width">
                        <label for="nome">Nome do Laboratório</label>
                        <input type="text" name="nome" id="nome" value="${laboratorio.nome}" required>
                    </div>
                    <div class="form-group">
                        <label for="areaPesquisa">Área de Pesquisa</label>
                        <input type="text" name="areaPesquisa" id="areaPesquisa" value="${laboratorio.areaPesquisa}" required>
                    </div>
                    <div class="form-group">
                        <label for="status">Status</label>
                        <select name="status" id="status" required>
                            <option value="Ativo" ${laboratorio.status == 'Ativo' ? 'selected' : ''}>Ativo</option>
                            <option value="Em Pausa" ${laboratorio.status == 'Em Pausa' ? 'selected' : ''}>Em Pausa</option>
                            <option value="Concluido" ${laboratorio.status == 'Concluido' ? 'selected' : ''}>Concluído</option>
                        </select>
                    </div>
                    <div class="form-group full-width">
                        <label for="tituloProjeto">Título do Projeto</label>
                        <input type="text" name="tituloProjeto" id="tituloProjeto" value="${laboratorio.tituloProjeto}" required>
                    </div>
                    <div class="actions">
                        <button type="submit" class="btn btn-submit"><i class="fas fa-save"></i> Salvar Laboratório</button>
                        <a href="laboratorio" class="btn btn-cancel">Cancelar</a>
                    </div>
                </div>
            </form>
        </div>
    </div>

</body>
</html>
