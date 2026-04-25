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
    <style>
        header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 40px;
            background: white;
            padding: 20px 30px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }

        header h1 {
            margin: 0;
            font-size: 1.8rem;
            color: var(--primary-color);
        }

        .welcome-msg {
            font-size: 1.1rem;
            color: #666;
        }

        /* Cards Container */
        .cards-container {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 25px;
        }

        .card {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 10px 20px rgba(0,0,0,0.05);
            transition: transform 0.3s, box-shadow 0.3s;
            text-decoration: none;
            color: inherit;
            display: flex;
            flex-direction: column;
            align-items: center;
            text-align: center;
        }

        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 30px rgba(0,0,0,0.1);
        }

        .card i {
            font-size: 3rem;
            color: var(--accent-color);
            margin-bottom: 15px;
        }

        .card h3 {
            margin: 10px 0;
            color: var(--primary-color);
            font-size: 1.4rem;
        }

        .card p {
            color: #777;
            margin-bottom: 0;
        }
    </style>
    <!-- Font Awesome for Icons -->
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
                <h3>Gerenciar Bolsistas</h3>
                <p>Cadastre, visualize e pesquise bolsistas acadêmicos no sistema.</p>
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
