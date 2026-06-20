package dev.matheus.cadastroBolsistas.controller;

import dev.matheus.cadastroBolsistas.model.Bolsista;
import dev.matheus.cadastroBolsistas.model.Laboratorio;
import dev.matheus.cadastroBolsistas.model.Projeto;
import dev.matheus.cadastroBolsistas.model.Usuario;
import dev.matheus.cadastroBolsistas.service.BolsistaService;
import dev.matheus.cadastroBolsistas.service.LaboratorioService;
import dev.matheus.cadastroBolsistas.service.ProjetoService;
import dev.matheus.cadastroBolsistas.util.StringUtil;
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
@RequestMapping("/projeto")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private LaboratorioService laboratorioService;

    @Autowired
    private BolsistaService bolsistaService;

    @GetMapping
    public String listar(@RequestParam(required = false) String buscaNome,
                         @RequestParam(required = false) String labId,
                         HttpSession session,
                         Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        try {
            model.addAttribute("todosLaboratorios", laboratorioService.listarTodos());

            Integer filterLabId = null;
            if (labId != null && !labId.trim().isEmpty()) {
                try {
                    filterLabId = Integer.parseInt(labId);
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
            ArrayList<Projeto> lista = projetoService.buscarProjetos(buscaNome, filterLabId);

            for (Projeto p : lista) {
                Laboratorio lab = laboratorioService.buscarPorId(p.getLaboratorioId());
                if (lab != null) {
                    p.setNomeLaboratorio(lab.getNome());
                    model.addAttribute("coordenador_" + p.getId(), lab.getCoordenador());
                }
                ArrayList<Bolsista> membros = bolsistaService.buscarPorProjeto(p.getId());
                model.addAttribute("membros_" + p.getId(), membros);
                
                boolean podeGerenciar = laboratorioService.podeGerenciar(usuarioLogado, p.getLaboratorioId());
                model.addAttribute("podeGerenciar_" + p.getId(), podeGerenciar);
            }

            model.addAttribute("listaProjetos", lista);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "projetos";
    }

    @GetMapping("/detalhes")
    public String detalhes(@RequestParam String id,
                           HttpSession session,
                           Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        try {
            int projId = Integer.parseInt(id);
            Projeto proj = projetoService.buscarPorId(projId);
            if (proj == null) {
                return "redirect:/projeto";
            }

            Laboratorio lab = laboratorioService.buscarPorId(proj.getLaboratorioId());
            if (lab != null) {
                proj.setNomeLaboratorio(lab.getNome());
                model.addAttribute("coordenador", lab.getCoordenador());
                model.addAttribute("coordenadorId", lab.getCoordenadorId());
            }

            if (usuarioLogado.isBolsista() && ((Bolsista) usuarioLogado).getLaboratorioId() != proj.getLaboratorioId()) {
                return "redirect:/projeto?erro=Sem permissao para visualizar detalhes deste projeto.";
            }

            ArrayList<Bolsista> membrosProjeto = bolsistaService.buscarPorProjeto(projId);
            ArrayList<Bolsista> bolsistasLab = bolsistaService.buscarPorLaboratorio(proj.getLaboratorioId());

            boolean podeGerenciar = laboratorioService.podeGerenciar(usuarioLogado, proj.getLaboratorioId());

            model.addAttribute("projeto", proj);
            model.addAttribute("laboratorio", lab);
            model.addAttribute("membros", membrosProjeto);
            model.addAttribute("bolsistasLab", bolsistasLab);
            model.addAttribute("podeGerenciar", podeGerenciar);
            return "detalhes-projeto";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/projeto";
        }
    }

    @GetMapping("/novo")
    public String formularioNovo(@RequestParam(required = false) String labId,
                                 HttpSession session,
                                 Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
        if (usuarioLogado == null) {
            return "redirect:/login";
        }
        if (usuarioLogado.isBolsista()) {
            return "redirect:/projeto";
        }

        Projeto proj = new Projeto();
        proj.setAtivo(true);
        if (labId != null) {
            try {
                proj.setLaboratorioId(Integer.parseInt(labId));
            } catch (NumberFormatException e) {
                // ignore
            }
        }

        try {
            ArrayList<Laboratorio> labsDisponiveis = new ArrayList<>();
            if (usuarioLogado.isAdmin()) {
                labsDisponiveis = laboratorioService.listarTodos();
            } else if (usuarioLogado.isProfessor()) {
                labsDisponiveis = laboratorioService.listarPorCoordenador(usuarioLogado.getId());
            }
            model.addAttribute("laboratorios", labsDisponiveis);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        model.addAttribute("projeto", proj);
        return "cadastro-projeto";
    }

    @GetMapping("/editar")
    public String formularioEditar(@RequestParam String id,
                                   HttpSession session,
                                   Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
        if (usuarioLogado == null) {
            return "redirect:/login";
        }
        if (usuarioLogado.isBolsista()) {
            return "redirect:/projeto";
        }

        try {
            int projId = Integer.parseInt(id);
            Projeto proj = projetoService.buscarPorId(projId);
            if (proj == null) {
                return "redirect:/projeto";
            }
            if (!laboratorioService.podeGerenciar(usuarioLogado, proj.getLaboratorioId())) {
                return "redirect:/projeto";
            }

            ArrayList<Laboratorio> labsDisponiveis = new ArrayList<>();
            if (usuarioLogado.isAdmin()) {
                labsDisponiveis = laboratorioService.listarTodos();
            } else if (usuarioLogado.isProfessor()) {
                labsDisponiveis = laboratorioService.listarPorCoordenador(usuarioLogado.getId());
            }
            model.addAttribute("laboratorios", labsDisponiveis);
            model.addAttribute("projeto", proj);
            return "cadastro-projeto";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/projeto";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@RequestParam(required = false) Integer id,
                         @RequestParam String nome,
                         @RequestParam String descricao,
                         @RequestParam int laboratorioId,
                         @RequestParam(required = false) String origem,
                         HttpSession session,
                         Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        try {
            if (!laboratorioService.podeGerenciar(usuarioLogado, laboratorioId)) {
                return "redirect:/laboratorio";
            }

            Projeto p;
            if (id != null && id > 0) {
                p = projetoService.buscarPorId(id);
                if (p == null || p.getLaboratorioId() != laboratorioId) {
                    if ("projeto".equals(origem)) {
                        return "redirect:/projeto";
                    }
                    if ("editar".equals(origem)) {
                        return "redirect:/laboratorio/editar?id=" + laboratorioId;
                    }
                    return "redirect:/laboratorio/detalhes?id=" + laboratorioId;
                }
            } else {
                p = new Projeto();
                p.setLaboratorioId(laboratorioId);
                p.setAtivo(true);
            }
            p.setNome(nome != null ? nome.trim() : "");
            p.setDescricao(descricao != null ? descricao.trim() : "");
            
            if (p.getNome().isEmpty()) {
                if ("projeto".equals(origem)) {
                    return "redirect:/projeto/novo?erro=O nome do projeto nao pode ser vazio";
                }
                if ("editar".equals(origem)) {
                    return "redirect:/laboratorio/editar?id=" + laboratorioId + "&erro=O nome do projeto nao pode ser vazio";
                }
                return "redirect:/laboratorio/detalhes?id=" + laboratorioId;
            }

            if (p.getId() > 0) {
                projetoService.atualizar(p);
            } else {
                projetoService.cadastrar(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if ("projeto".equals(origem)) {
            return "redirect:/projeto";
        }
        if ("editar".equals(origem)) {
            return "redirect:/laboratorio/editar?id=" + laboratorioId;
        }
        return "redirect:/laboratorio/detalhes?id=" + laboratorioId;
    }

    @GetMapping("/desativar")
    public String desativar(@RequestParam int id,
                            @RequestParam int labId,
                            @RequestParam(required = false) String origem,
                            HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        try {
            if (!laboratorioService.podeGerenciar(usuarioLogado, labId)) {
                return "redirect:/laboratorio";
            }
            projetoService.excluir(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if ("projeto".equals(origem)) {
            return "redirect:/projeto";
        }
        if ("editar".equals(origem)) {
            return "redirect:/laboratorio/editar?id=" + labId;
        }
        return "redirect:/laboratorio/detalhes?id=" + labId;
    }

    @PostMapping("/vincular")
    public String vincularBolsista(@RequestParam int bolsistaId,
                                   @RequestParam int projetoId,
                                   @RequestParam int labId,
                                   @RequestParam(required = false) String origem,
                                   HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        try {
            if (!laboratorioService.podeGerenciar(usuarioLogado, labId)) {
                return "redirect:/laboratorio";
            }
            projetoService.vincularBolsista(bolsistaId, projetoId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if ("detalhes-projeto".equals(origem)) {
            return "redirect:/projeto/detalhes?id=" + projetoId;
        }
        return "redirect:/laboratorio/detalhes?id=" + labId;
    }

    @GetMapping("/desvincular")
    public String desvincularBolsista(@RequestParam int bolsistaId,
                                      @RequestParam int projetoId,
                                      @RequestParam int labId,
                                      @RequestParam(required = false) String origem,
                                      HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        try {
            if (!laboratorioService.podeGerenciar(usuarioLogado, labId)) {
                return "redirect:/laboratorio";
            }
            projetoService.desvincularBolsista(bolsistaId, projetoId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if ("detalhes-projeto".equals(origem)) {
            return "redirect:/projeto/detalhes?id=" + projetoId;
        }
        return "redirect:/laboratorio/detalhes?id=" + labId;
    }
}
