package dev.matheus.cadastroBolsistas.controller;

import dev.matheus.cadastroBolsistas.model.Bolsista;
import dev.matheus.cadastroBolsistas.model.Laboratorio;
import dev.matheus.cadastroBolsistas.model.Usuario;
import dev.matheus.cadastroBolsistas.model.Projeto;
import dev.matheus.cadastroBolsistas.service.BolsistaService;
import dev.matheus.cadastroBolsistas.service.LaboratorioService;
import dev.matheus.cadastroBolsistas.service.ProfessorService;
import dev.matheus.cadastroBolsistas.service.ProjetoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.ArrayList;

@Controller
@RequestMapping("/laboratorio")
public class LaboratorioController {

    @Autowired
    private LaboratorioService laboratorioService;

    @Autowired
    private BolsistaService bolsistaService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private ProjetoService projetoService;

    @GetMapping
    public String handleGet(@RequestParam(required = false) String action,
                            @RequestParam(required = false) String id,
                            @RequestParam(required = false) String editarProjetoId,
                            HttpSession session,
                            Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");

        if ("novo".equals(action)) {
            if (!usuarioLogado.isAdmin()) {
                return "redirect:/laboratorio";
            }
            try {
                model.addAttribute("professores", professorService.listarTodos());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "cadastro-laboratorio";
        }

        if ("editar".equals(action)) {
            if (!usuarioLogado.isAdmin()) {
                return "redirect:/laboratorio";
            }
            try {
                int labId = Integer.parseInt(id);
                Laboratorio lab = laboratorioService.buscarPorId(labId);
                if (lab != null) {
                    lab.setProjetos(projetoService.listarPorLaboratorio(labId));
                }
                model.addAttribute("laboratorio", lab);
                model.addAttribute("professores", professorService.listarTodos());
                
                if (editarProjetoId != null && !editarProjetoId.trim().isEmpty()) {
                    try {
                        int projId = Integer.parseInt(editarProjetoId);
                        Projeto proj = projetoService.buscarPorId(projId);
                        if (proj != null && proj.getLaboratorioId() == labId) {
                            model.addAttribute("projetoParaEditar", proj);
                        }
                    } catch (NumberFormatException e) {
                        // ignore
                    }
                }
                return "cadastro-laboratorio";
            } catch (NumberFormatException e) {
                model.addAttribute("erro", "ID do laboratorio invalido.");
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("erro", "Erro ao carregar laboratorio.");
            }
        }

        if ("excluir".equals(action)) {
            if (!usuarioLogado.isAdmin()) {
                return "redirect:/laboratorio";
            }
            try {
                laboratorioService.excluir(Integer.parseInt(id));
            } catch (NumberFormatException e) {
                model.addAttribute("erro", "ID do laboratorio invalido.");
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("erro", "Erro ao excluir laboratorio.");
            }
            return "redirect:/laboratorio";
        }

        if ("detalhes".equals(action)) {
            try {
                int labId = Integer.parseInt(id);
                Laboratorio lab = laboratorioService.buscarPorId(labId);
                if (lab == null) {
                    return "redirect:/laboratorio";
                }

                // restringe visualizacao do bolsista ao seu proprio lab
                if (usuarioLogado.isBolsista() && ((Bolsista) usuarioLogado).getLaboratorioId() != labId) {
                    return "redirect:/laboratorio";
                }

                ArrayList<Bolsista> bolsistas = bolsistaService.buscarPorLaboratorio(labId);
                
                // para vincular bolsistas a projetos, precisamos da lista de bolsistas e projetos do lab.
                // para cada bolsista, vamos obter os projetos vinculados a ele para renderizar na tela
                for (Bolsista b : bolsistas) {
                    model.addAttribute("projetosBolsista_" + b.getId(), projetoService.listarPorBolsista(b.getId()));
                }

                boolean podeGerenciar = usuarioLogado.isAdmin() || 
                        (usuarioLogado.isProfessor() && lab.getCoordenadorId() == usuarioLogado.getId());

                model.addAttribute("laboratorio", lab);
                model.addAttribute("bolsistas", bolsistas);
                model.addAttribute("podeGerenciar", podeGerenciar);
                return "detalhes-laboratorio";
            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:/laboratorio";
            }
        }

        return listarLaboratorios(model, usuarioLogado);
    }

    @PostMapping
    public String salvar(@RequestParam(required = false) String id,
                         @RequestParam(required = false) String nome,
                         @RequestParam(required = false) String areaPesquisa,
                         @RequestParam(required = false) String status,
                         @RequestParam(required = false) String capacidade,
                         @RequestParam(required = false) String coordenadorId,
                         HttpSession session,
                         Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");

        if (!usuarioLogado.isAdmin()) {
            return "redirect:/laboratorio";
        }

        Laboratorio lab = new Laboratorio();

        if (!estaVazio(id)) {
            try {
                lab.setId(Integer.parseInt(id));
            } catch (NumberFormatException e) {
                model.addAttribute("erro", "ID do laboratorio invalido.");
                model.addAttribute("laboratorio", lab);
                carregarProfessores(model);
                return "cadastro-laboratorio";
            }
        }

        lab.setNome(limpar(nome));
        lab.setAreaPesquisa(limpar(areaPesquisa));
        lab.setStatus(limpar(status));
        lab.setAtivo(true);

        if (!estaVazio(coordenadorId)) {
            try {
                lab.setCoordenadorId(Integer.parseInt(coordenadorId));
            } catch (NumberFormatException e) {
                // coordenadorId nulo ou invalido
            }
        }

        String erroValidacao = validarLaboratorio(lab, capacidade);
        if (erroValidacao != null) {
            model.addAttribute("erro", erroValidacao);
            model.addAttribute("laboratorio", lab);
            carregarProfessores(model);
            return "cadastro-laboratorio";
        }

        lab.setCapacidade(Integer.parseInt(capacidade));

        try {
            boolean sucesso = lab.getId() > 0 ? laboratorioService.atualizar(lab) : laboratorioService.cadastrar(lab);
            if (sucesso) {
                return "redirect:/laboratorio";
            } else {
                model.addAttribute("erro", "Erro ao salvar laboratorio.");
                model.addAttribute("laboratorio", lab);
                carregarProfessores(model);
                return "cadastro-laboratorio";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("erro", "Erro ao salvar laboratorio: " + e.getMessage());
            model.addAttribute("laboratorio", lab);
            carregarProfessores(model);
            return "cadastro-laboratorio";
        }
    }

    private String listarLaboratorios(Model model, Usuario usuarioLogado) {
        try {
            ArrayList<Laboratorio> lista;
            if (usuarioLogado.isAdmin()) {
                lista = laboratorioService.listarTodos();
            } else if (usuarioLogado.isProfessor()) {
                lista = laboratorioService.listarPorCoordenador(usuarioLogado.getId());
            } else { // bolsista
                lista = laboratorioService.listarTodos();
            }

            // Carrega os projetos de cada laboratório para visualização
            for (Laboratorio lab : lista) {
                lab.setProjetos(projetoService.listarPorLaboratorio(lab.getId()));
            }

            model.addAttribute("listaLaboratorios", lista);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("erro", "Erro ao listar laboratorios: " + e.getMessage());
        }
        return "laboratorios";
    }

    private void carregarProfessores(Model model) {
        try {
            model.addAttribute("professores", professorService.listarTodos());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String validarLaboratorio(Laboratorio lab, String capacidadeStr) {
        if (estaVazio(lab.getNome()) || lab.getNome().length() < 3) {
            return "O nome do laboratorio deve ter pelo menos 3 caracteres.";
        }
        if (estaVazio(lab.getAreaPesquisa())) {
            return "A area de pesquisa e obrigatoria.";
        }
        if (lab.getCoordenadorId() <= 0) {
            return "O professor coordenador e obrigatorio.";
        }
        if (!"Ativo".equals(lab.getStatus()) && !"Em Pausa".equals(lab.getStatus()) && !"Concluido".equals(lab.getStatus())) {
            return "Status do laboratorio invalido.";
        }
        if (estaVazio(capacidadeStr)) {
            return "A capacidade maxima e obrigatoria.";
        }
        try {
            int cap = Integer.parseInt(capacidadeStr);
            if (cap <= 0) {
                return "A capacidade maxima deve ser maior que zero.";
            }
        } catch (NumberFormatException e) {
            return "A capacidade maxima deve ser um numero inteiro.";
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
