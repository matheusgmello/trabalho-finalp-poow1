package dev.matheus.cadastroBolsistas.controller;

import dev.matheus.cadastroBolsistas.model.Bolsista;
import dev.matheus.cadastroBolsistas.service.BolsistaService;
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
 * bolsista comum so pode editar o proprio cadastro.
 */
@Controller
@RequestMapping("/bolsista")
public class BolsistaController {

    @Autowired
    private BolsistaService bolsistaService;

    @Autowired
    private LaboratorioService laboratorioService;

    @GetMapping
    public String handleGet(@RequestParam(required = false) String action,
                            @RequestParam(required = false) String id,
                            @RequestParam(required = false) String buscaNome,
                            @RequestParam(required = false) String buscaCurso,
                            HttpSession session,
                            HttpServletResponse response,
                            Model model) throws IOException {

        Bolsista usuarioLogado = (Bolsista) session.getAttribute("usuario");

        if ("exportar".equals(action)) {
            exportarParaCSV(response);
            return null;
        }

        if ("novo".equals(action) || "editar".equals(action)) {
            try {
                model.addAttribute("laboratorios", laboratorioService.listarTodos());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if ("editar".equals(action)) {
                try {
                    int bolsistaId = Integer.parseInt(id);
                    if (!usuarioLogado.isAdmin() && bolsistaId != usuarioLogado.getId()) {
                        return "redirect:/bolsista";
                    }
                    Bolsista b = bolsistaService.listarTodos().stream()
                            .filter(bol -> bol.getId() == bolsistaId)
                            .findFirst().orElse(null);
                    model.addAttribute("bolsista", b);
                } catch (Exception e) {
                    e.printStackTrace();
                    model.addAttribute("erro", "Bolsista invalido para edicao.");
                }
            }
            return "cadastro-bolsista";
        }

        if ("excluir".equals(action) && usuarioLogado.isAdmin()) {
            try {
                bolsistaService.excluir(Integer.parseInt(id));
            } catch (SQLException e) {
                e.printStackTrace();
                model.addAttribute("erro", "Erro ao excluir bolsista.");
            } catch (NumberFormatException e) {
                model.addAttribute("erro", "ID do bolsista invalido.");
            }
            return "redirect:/bolsista";
        }

        try {
            ArrayList<Bolsista> lista;
            if (buscaNome != null && !buscaNome.isEmpty()) {
                lista = bolsistaService.buscarPorNome(buscaNome);
            } else if (buscaCurso != null && !buscaCurso.isEmpty()) {
                lista = bolsistaService.buscarPorCurso(buscaCurso);
            } else {
                lista = bolsistaService.listarTodos();
            }
            model.addAttribute("listabolsistas", lista);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("erro", "Erro ao listar bolsistas: " + e.getMessage());
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
                         HttpSession session,
                         Model model) {

        Bolsista usuarioLogado = (Bolsista) session.getAttribute("usuario");

        int bolsistaId = 0;
        if (!estaVazio(id)) {
            try {
                bolsistaId = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                model.addAttribute("erro", "ID do bolsista invalido.");
                carregarLaboratorios(model);
                return "cadastro-bolsista";
            }
        }

        // bolsista comum so pode editar o proprio registro
        if (!usuarioLogado.isAdmin() && (bolsistaId == 0 || bolsistaId != usuarioLogado.getId())) {
            return "redirect:/bolsista";
        }

        Bolsista b = new Bolsista();
        b.setId(bolsistaId);
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

        String erroValidacao = validarBolsista(b, dataNascimento, laboratorioId, usuarioLogado);
        if (erroValidacao != null) {
            model.addAttribute("erro", erroValidacao);
            model.addAttribute("bolsista", b);
            carregarLaboratorios(model);
            return "cadastro-bolsista";
        }

        b.setDataNascimento(LocalDate.parse(dataNascimento));

        if (!estaVazio(laboratorioId)) {
            int labId = Integer.parseInt(laboratorioId);
            try {
                if (b.getId() == 0 && !laboratorioService.temVaga(labId)) {
                    model.addAttribute("erro", "Laboratorio atingiu a capacidade maxima!");
                    model.addAttribute("bolsista", b);
                    carregarLaboratorios(model);
                    return "cadastro-bolsista";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            b.setLaboratorioId(labId);
        }

        try {
            boolean sucesso = b.getId() > 0 ? bolsistaService.atualizar(b) : bolsistaService.inserir(b);
            if (sucesso) {
                return "redirect:/bolsista";
            } else {
                model.addAttribute("erro", "Problemas ao salvar o bolsista.");
                model.addAttribute("bolsista", b);
                carregarLaboratorios(model);
                return "cadastro-bolsista";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("erro", "Algo aconteceu: " + e.getMessage());
            model.addAttribute("bolsista", b);
            carregarLaboratorios(model);
            return "cadastro-bolsista";
        }
    }

    // gera e envia o arquivo csv com a lista de bolsistas para download
    private void exportarParaCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=bolsistas.csv");
        try (PrintWriter writer = response.getWriter()) {
            writer.println("ID,Nome,Email,Curso,Laboratorio,Status");
            for (Bolsista b : bolsistaService.listarTodos()) {
                writer.println(b.getId() + "," + b.getNome() + "," + b.getEmail() + "," +
                        b.getCurso() + "," + b.getNomeLaboratorio() + "," + (b.isAtivo() ? "Ativo" : "Inativo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void carregarLaboratorios(Model model) {
        try {
            model.addAttribute("laboratorios", laboratorioService.listarTodos());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String validarBolsista(Bolsista b, String dataNascimentoStr, String laboratorioIdStr, Bolsista usuarioLogado) {
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

    private String limpar(String valor) {
        return valor != null ? valor.trim() : "";
    }

    private boolean estaVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
