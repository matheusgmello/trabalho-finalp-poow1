package dev.matheus.cadastroBolsistas.controller;

import dev.matheus.cadastroBolsistas.model.Bolsista;
import dev.matheus.cadastroBolsistas.model.Cargo;
import dev.matheus.cadastroBolsistas.model.Professor;
import dev.matheus.cadastroBolsistas.model.Laboratorio;
import dev.matheus.cadastroBolsistas.model.Usuario;
import dev.matheus.cadastroBolsistas.service.BolsistaService;
import dev.matheus.cadastroBolsistas.service.ProfessorService;
import dev.matheus.cadastroBolsistas.service.LaboratorioService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/*
 * responsavel pelo crud de bolsistas.
 * get: lista, busca, exibe formulario e exclui (action=novo|editar|excluir|exportar).
 * post: salva novo bolsista ou atualiza existente (id > 0).
 * bolsista comum so visualiza membros do proprio lab/squad, e edita apenas a si mesmo.
 * professor visualiza e gerencia os bolsistas do seu laboratorio.
 */
@Controller
@RequestMapping("/bolsista")
public class BolsistaController {

    @Autowired
    private BolsistaService bolsistaService;

    @Autowired
    private LaboratorioService laboratorioService;

    @Autowired
    private ProfessorService professorService;

    @GetMapping
    public String handleGet(@RequestParam(required = false) String action,
                            @RequestParam(required = false) String id,
                            @RequestParam(required = false) String tipo,
                            @RequestParam(required = false) String buscaNome,
                            @RequestParam(required = false) String buscaCurso,
                            HttpSession session,
                            HttpServletResponse response,
                            Model model) throws IOException {

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
        if (usuarioLogado == null) {
            return "redirect:/login";
        }
        if (usuarioLogado.isBolsista()) {
            return "redirect:/dashboard";
        }

        if ("exportar".equals(action)) {
            exportarParaCSV(response, usuarioLogado);
            return null;
        }

        if ("novo".equals(action) || "editar".equals(action)) {
            carregarLaboratoriosDisponiveis(model, usuarioLogado);

            if ("editar".equals(action)) {
                try {
                    int userId = Integer.parseInt(id);
                    Usuario u;
                    if ("PROFESSOR".equals(tipo)) {
                        u = professorService.buscarPorId(userId);
                    } else {
                        u = bolsistaService.buscarPorId(userId);
                    }

                    if (u == null) {
                        return "redirect:/bolsista";
                    }

                    if ("PROFESSOR".equals(tipo)) {
                        if (!usuarioLogado.isAdmin()) {
                            return "redirect:/bolsista";
                        }
                    } else {
                        Bolsista b = (Bolsista) u;
                        if (!podeGerenciarBolsista(usuarioLogado, b) && b.getId() != usuarioLogado.getId()) {
                            return "redirect:/bolsista";
                        }
                    }
                    model.addAttribute("bolsista", u);
                } catch (Exception e) {
                    e.printStackTrace();
                    model.addAttribute("erro", "Usuário inválido para edição.");
                }
            }
            return "cadastro-bolsista";
        }

        if ("excluir".equals(action)) {
            try {
                int userId = Integer.parseInt(id);
                if ("PROFESSOR".equals(tipo)) {
                    if (usuarioLogado.isAdmin()) {
                        professorService.excluir(userId);
                    } else {
                        model.addAttribute("erro", "Sem permissão para excluir.");
                    }
                } else {
                    Bolsista b = bolsistaService.buscarPorId(userId);
                    if (b != null && (usuarioLogado.isAdmin() || (usuarioLogado.isProfessor() && podeGerenciarBolsista(usuarioLogado, b)))) {
                        bolsistaService.excluir(userId);
                    } else {
                        model.addAttribute("erro", "Sem permissão para excluir.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                model.addAttribute("erro", "Erro ao excluir usuário.");
            } catch (NumberFormatException e) {
                model.addAttribute("erro", "ID do usuário inválido.");
            }
            return "redirect:/bolsista";
        }

        try {
            ArrayList<Usuario> lista = new ArrayList<>();
            if (buscaNome != null && !buscaNome.isEmpty()) {
                lista.addAll(bolsistaService.buscarPorNome(buscaNome));
                if (usuarioLogado.isAdmin()) {
                    lista.addAll(professorService.buscarPorNome(buscaNome));
                }
            } else if (buscaCurso != null && !buscaCurso.isEmpty()) {
                lista.addAll(bolsistaService.buscarPorCurso(buscaCurso));
            } else {
                lista.addAll(bolsistaService.listarTodos());
                if (usuarioLogado.isAdmin()) {
                    lista.addAll(professorService.listarTodos());
                }
            }

            if (tipo != null && !tipo.trim().isEmpty() && !"editar".equals(action) && !"excluir".equals(action)) {
                String tipoFiltro = tipo.trim().toUpperCase();
                ArrayList<Usuario> listaFiltrada = new ArrayList<>();
                for (Usuario u : lista) {
                    if (tipoFiltro.equals(u.getTipoUsuario())) {
                        listaFiltrada.add(u);
                    }
                }
                lista = listaFiltrada;
            }

            // Popular o nomeLaboratorio para os professores com a lista de laboratórios coordenados por eles
            for (Usuario u : lista) {
                if (u.isProfessor()) {
                    ArrayList<Laboratorio> labs = laboratorioService.listarPorCoordenador(u.getId());
                    if (labs != null && !labs.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < labs.size(); i++) {
                            sb.append(labs.get(i).getNome());
                            if (i < labs.size() - 1) {
                                sb.append(", ");
                            }
                        }
                        u.setNomeLaboratorio(sb.toString());
                    } else {
                        u.setNomeLaboratorio("Nenhum");
                    }
                }
            }

            lista = filtrarUsuariosPorPermissao(lista, usuarioLogado);
            model.addAttribute("listabolsistas", lista);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("erro", "Erro ao listar usuários: " + e.getMessage());
        }

        return "bolsistas";
    }

    @PostMapping
    public String salvar(@RequestParam(required = false) String id,
                         @RequestParam(required = false) String nome,
                         @RequestParam(required = false) String dataNascimento,
                         @RequestParam(required = false) String curso,
                         @RequestParam(required = false) String email,
                         @RequestParam(required = false) String matricula,
                         @RequestParam(required = false) String cpf,
                         @RequestParam(required = false) String telefone,
                         @RequestParam(required = false) String senha,
                         @RequestParam(required = false) String laboratorioId,
                         @RequestParam(required = false) String tipoUsuario,
                         @RequestParam(required = false) String fotoUrl,
                         @RequestParam(required = false) String cargo,
                         @RequestParam(required = false) String bio,
                         HttpSession session,
                         Model model) {

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
        if (usuarioLogado == null) {
            return "redirect:/login";
        }
        if (usuarioLogado.isBolsista()) {
            return "redirect:/dashboard";
        }

        int userId = 0;
        if (!estaVazio(id)) {
            try {
                userId = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                model.addAttribute("erro", "ID inválido.");
                carregarLaboratoriosDisponiveis(model, usuarioLogado);
                return "cadastro-bolsista";
            }
        }

        // Se for professor, as permissões e fluxo são diferentes
        if ("PROFESSOR".equals(tipoUsuario)) {
            // Apenas admin pode criar/editar professores
            if (!usuarioLogado.isAdmin()) {
                return "redirect:/bolsista";
            }

            Professor p = new Professor();
            p.setId(userId);
            p.setNome(limpar(nome));
            p.setEmail(limpar(email));
            p.setSenha(limpar(senha));
            p.setAtivo(true);
            p.setFotoUrl(limpar(fotoUrl));
            p.setBio(limpar(bio));

            String erroValidacao = validarProfessor(p, usuarioLogado);
            if (erroValidacao != null) {
                model.addAttribute("erro", erroValidacao);
                model.addAttribute("bolsista", p);
                carregarLaboratoriosDisponiveis(model, usuarioLogado);
                return "cadastro-bolsista";
            }

            try {
                boolean sucesso = p.getId() > 0 ? professorService.atualizar(p) : professorService.inserir(p);
                if (sucesso) {
                    return "redirect:/bolsista";
                } else {
                    model.addAttribute("erro", "Problemas ao salvar o professor.");
                    model.addAttribute("bolsista", p);
                    carregarLaboratoriosDisponiveis(model, usuarioLogado);
                    return "cadastro-bolsista";
                }
            } catch (SQLException e) {
                e.printStackTrace();
                model.addAttribute("erro", "Algo aconteceu: " + e.getMessage());
                model.addAttribute("bolsista", p);
                carregarLaboratoriosDisponiveis(model, usuarioLogado);
                return "cadastro-bolsista";
            }
        }

        // Se for bolsista ou admin comum:
        // bolsista comum so pode editar o proprio registro
        if (!usuarioLogado.isAdmin() && !usuarioLogado.isProfessor() && (userId == 0 || userId != usuarioLogado.getId())) {
            return "redirect:/bolsista";
        }

        Bolsista b = new Bolsista();
        b.setId(userId);
        b.setNome(limpar(nome));
        b.setCurso(limpar(curso));
        b.setEmail(limpar(email));
        b.setMatricula(limpar(matricula));
        b.setCpf(limpar(cpf));
        b.setTelefone(limpar(telefone));
        b.setSenha(limpar(senha));
        b.setAtivo(true);
        b.setTipoUsuario(!estaVazio(tipoUsuario) ? tipoUsuario : "BOLSISTA");
        b.setFotoUrl(limpar(fotoUrl));
        b.setCargo(Cargo.deString(limpar(cargo)));
        b.setBio(limpar(bio));

        String erroValidacao = validarBolsista(b, dataNascimento, laboratorioId, usuarioLogado);
        if (erroValidacao != null) {
            model.addAttribute("erro", erroValidacao);
            model.addAttribute("bolsista", b);
            carregarLaboratoriosDisponiveis(model, usuarioLogado);
            return "cadastro-bolsista";
        }

        b.setDataNascimento(LocalDate.parse(dataNascimento));

        if (!estaVazio(laboratorioId)) {
            int labId = Integer.parseInt(laboratorioId);
            
            // se for professor, verifica se ele e o coordenador desse laboratorio
            if (usuarioLogado.isProfessor() && !podeGerenciarLab(usuarioLogado, labId)) {
                model.addAttribute("erro", "Sem permissao para vincular bolsista a este laboratorio.");
                model.addAttribute("bolsista", b);
                carregarLaboratoriosDisponiveis(model, usuarioLogado);
                return "cadastro-bolsista";
            }

            try {
                if (b.getId() == 0 && !laboratorioService.temVaga(labId)) {
                    model.addAttribute("erro", "Laboratorio atingiu a capacidade maxima!");
                    model.addAttribute("bolsista", b);
                    carregarLaboratoriosDisponiveis(model, usuarioLogado);
                    return "cadastro-bolsista";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            b.setLaboratorioId(labId);
        } else {
            b.setLaboratorioId(0);
            b.setCargo(null);
        }

        try {
            boolean sucesso = b.getId() > 0 ? bolsistaService.atualizar(b) : bolsistaService.inserir(b);
            if (sucesso) {
                return "redirect:/bolsista";
            } else {
                model.addAttribute("erro", "Problemas ao salvar o bolsista.");
                model.addAttribute("bolsista", b);
                carregarLaboratoriosDisponiveis(model, usuarioLogado);
                return "cadastro-bolsista";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("erro", "Algo aconteceu: " + e.getMessage());
            model.addAttribute("bolsista", b);
            carregarLaboratoriosDisponiveis(model, usuarioLogado);
            return "cadastro-bolsista";
        }
    }

    private void exportarParaCSV(HttpServletResponse response, Usuario usuarioLogado) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=usuarios.csv");
        try (PrintWriter writer = response.getWriter()) {
            writer.println("ID,Nome,Email,Curso,Laboratorio,Status,Cargo,Tipo");
            ArrayList<Usuario> lista = new ArrayList<>();
            lista.addAll(bolsistaService.listarTodos());
            if (usuarioLogado.isAdmin()) {
                lista.addAll(professorService.listarTodos());
            }

            // Popular o nomeLaboratorio para os professores com a lista de laboratórios coordenados por eles
            for (Usuario u : lista) {
                if (u.isProfessor()) {
                    ArrayList<Laboratorio> labs = laboratorioService.listarPorCoordenador(u.getId());
                    if (labs != null && !labs.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < labs.size(); i++) {
                            sb.append(labs.get(i).getNome());
                            if (i < labs.size() - 1) {
                                sb.append(", ");
                            }
                        }
                        u.setNomeLaboratorio(sb.toString());
                    } else {
                        u.setNomeLaboratorio("Nenhum");
                    }
                }
            }

            lista = filtrarUsuariosPorPermissao(lista, usuarioLogado);
            for (Usuario u : lista) {
                String curso = u instanceof Bolsista ? ((Bolsista) u).getCurso() : "";
                Cargo cargoObj = u instanceof Bolsista ? ((Bolsista) u).getCargo() : null;
                String cargoStr = cargoObj != null ? cargoObj.getDescricao() : "";
                writer.println(u.getId() + "," + u.getNome() + "," + u.getEmail() + "," +
                        curso + "," + u.getNomeLaboratorio() + "," + (u.isAtivo() ? "Ativo" : "Inativo") + "," +
                        cargoStr + "," + u.getTipoUsuario());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void carregarLaboratoriosDisponiveis(Model model, Usuario usuarioLogado) {
        try {
            if (usuarioLogado.isAdmin()) {
                model.addAttribute("laboratorios", laboratorioService.listarTodos());
            } else if (usuarioLogado.isProfessor()) {
                model.addAttribute("laboratorios", laboratorioService.listarPorCoordenador(usuarioLogado.getId()));
            }
            model.addAttribute("cargos", Cargo.values());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Usuario> filtrarUsuariosPorPermissao(ArrayList<Usuario> list, Usuario usuarioLogado) throws SQLException {
        if (usuarioLogado.isAdmin()) {
            return list;
        }
        if (usuarioLogado.isProfessor()) {
            ArrayList<Laboratorio> labsCoordenados = laboratorioService.listarPorCoordenador(usuarioLogado.getId());
            ArrayList<Usuario> filtrados = new ArrayList<>();
            for (Usuario u : list) {
                if (u instanceof Bolsista) {
                    Bolsista b = (Bolsista) u;
                    boolean pertence = labsCoordenados.stream().anyMatch(l -> l.getId() == b.getLaboratorioId());
                    if (pertence) {
                        filtrados.add(b);
                    }
                }
            }
            return filtrados;
        }
        if (usuarioLogado.isBolsista()) {
            int labId = ((Bolsista) usuarioLogado).getLaboratorioId();
            ArrayList<Usuario> filtrados = new ArrayList<>();
            for (Usuario u : list) {
                if (u instanceof Bolsista) {
                    Bolsista b = (Bolsista) u;
                    if (b.getLaboratorioId() == labId) {
                        filtrados.add(b);
                    }
                }
            }
            return filtrados;
        }
        return new ArrayList<>();
    }

    private boolean podeGerenciarBolsista(Usuario usuarioLogado, Bolsista b) {
        if (usuarioLogado.isAdmin()) return true;
        if (usuarioLogado.isProfessor() && b != null) {
            try {
                ArrayList<Laboratorio> labs = laboratorioService.listarPorCoordenador(usuarioLogado.getId());
                return labs.stream().anyMatch(l -> l.getId() == b.getLaboratorioId());
            } catch (SQLException e) {
                return false;
            }
        }
        return false;
    }

    private boolean podeGerenciarLab(Usuario usuarioLogado, int labId) {
        if (usuarioLogado.isAdmin()) return true;
        if (usuarioLogado.isProfessor()) {
            try {
                ArrayList<Laboratorio> labs = laboratorioService.listarPorCoordenador(usuarioLogado.getId());
                return labs.stream().anyMatch(l -> l.getId() == labId);
            } catch (SQLException e) {
                return false;
            }
        }
        return false;
    }

    private String validarBolsista(Bolsista b, String dataNascimentoStr, String laboratorioIdStr, Usuario usuarioLogado) {
        if (estaVazio(b.getNome()) || b.getNome().length() < 3) {
            return "O nome do bolsista deve ter pelo menos 3 caracteres.";
        }
        if (estaVazio(dataNascimentoStr)) {
            return "A data de nascimento e obrigatoria.";
        }
        try {
            LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr);
            if (dataNascimento.isAfter(LocalDate.now())) {
                return "A data de nascimento nao pode ser futura.";
            }
        } catch (Exception e) {
            return "A data de nascimento e invalida.";
        }
        if (estaVazio(b.getCurso())) {
            return "O curso e obrigatorio.";
        }
        if (estaVazio(b.getEmail()) || !b.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            return "Informe um e-mail valido.";
        }
        if (estaVazio(b.getMatricula())) {
            return "A matricula e obrigatoria.";
        }
        if (estaVazio(b.getSenha()) || b.getSenha().length() < 6) {
            return "A senha deve ter pelo menos 6 caracteres.";
        }
        if (!"ADMIN".equals(b.getTipoUsuario()) && !"BOLSISTA".equals(b.getTipoUsuario())) {
            return "Tipo de usuario invalido.";
        }
        if (!usuarioLogado.isAdmin() && "ADMIN".equals(b.getTipoUsuario())) {
            return "Apenas administradores podem definir outro administrador.";
        }
        if (!estaVazio(laboratorioIdStr)) {
            try {
                if (Integer.parseInt(laboratorioIdStr) <= 0) {
                    return "Laboratorio invalido.";
                }
            } catch (NumberFormatException e) {
                return "Laboratorio invalido.";
            }
        }
        return null;
    }

    private String validarProfessor(Professor p, Usuario usuarioLogado) {
        if (estaVazio(p.getNome()) || p.getNome().length() < 3) {
            return "O nome do professor deve ter pelo menos 3 caracteres.";
        }
        if (estaVazio(p.getEmail()) || !p.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            return "Informe um e-mail válido.";
        }
        if (estaVazio(p.getSenha()) || p.getSenha().length() < 6) {
            return "A senha deve ter pelo menos 6 caracteres.";
        }
        if (!usuarioLogado.isAdmin()) {
            return "Apenas administradores podem definir outro professor.";
        }
        return null;
    }

    private String limpar(String valor) {
        return valor != null ? valor.trim() : "";
    }

    private boolean estaVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
