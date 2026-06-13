<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Cadastro de Bolsistas</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/dashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <header>
            <h1>Painel de Controle</h1>
            <div class="welcome-msg">
                Bem-vindo, <strong>${usuario.nome}</strong>!
            </div>
        </header>

        <div class="cards-container">
            <a href="bolsista" class="card">
                <i class="fas fa-user-graduate"></i>
                <h3>Gerenciar ${usuario.admin ? 'Usuários' : 'Bolsistas'}</h3>
                <p>Cadastre, visualize e pesquise ${usuario.admin ? 'usuários' : 'bolsistas acadêmicos'} no sistema.</p>
            </a>

            <a href="laboratorio" class="card">
                <i class="fas fa-flask"></i>
                <h3>Laboratórios</h3>
                <p>Gerencie laboratórios, áreas de pesquisa e projetos vinculados.</p>
            </a>

            <a href="relatorio" class="card">
                <i class="fas fa-chart-line"></i>
                <h3>Relatórios</h3>
                <p>Visualize estatísticas e relatórios de bolsas vigentes.</p>
            </a>
        </div>
    </div>

</body>
</html>
