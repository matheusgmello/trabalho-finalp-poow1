<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Cadastro de Bolsistas</title>
    <style>
        :root {
            --primary-color: #2c3e50;
            --secondary-color: #34495e;
            --accent-color: #3498db;
            --text-color: #ecf0f1;
            --bg-color: #f4f7f6;
            --sidebar-width: 250px;
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

        /* Main Content */
        .main-content {
            margin-left: var(--sidebar-width);
            flex: 1;
            padding: 40px;
        }

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

        .logout-btn:hover {
            background-color: #c0392b;
        }
    </style>
    <!-- Font Awesome for Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <div class="sidebar">
        <h2>SisBolsa</h2>
        <ul>
            <li><a href="dashboard"><i class="fas fa-home" style="margin-right: 10px;"></i> Dashboard</a></li>
            <li><a href="bolsista"><i class="fas fa-user-graduate" style="margin-right: 10px;"></i> Bolsistas</a></li>
            <li><a href="#"><i class="fas fa-flask" style="margin-right: 10px;"></i> Laboratórios</a></li>
        </ul>
        <a href="index.jsp" class="logout-btn"><i class="fas fa-sign-out-alt"></i> Sair</a>
    </div>

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

            <a href="#" class="card">
                <i class="fas fa-flask"></i>
                <h3>Laboratórios</h3>
                <p>Em breve: Gestão de laboratórios e alocação de bolsistas.</p>
            </a>

            <div class="card" style="opacity: 0.7; cursor: default;">
                <i class="fas fa-chart-line"></i>
                <h3>Relatórios</h3>
                <p>Visualize estatísticas e relatórios de bolsas vigentes.</p>
            </div>
        </div>
    </div>

</body>
</html>
