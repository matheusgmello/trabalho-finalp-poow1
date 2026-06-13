package dev.matheus.cadastroBolsistas.controller;

import dev.matheus.cadastroBolsistas.model.Bolsista;
import dev.matheus.cadastroBolsistas.model.Frequencia;
import dev.matheus.cadastroBolsistas.model.Laboratorio;
import dev.matheus.cadastroBolsistas.model.Usuario;
import dev.matheus.cadastroBolsistas.service.BolsistaService;
import dev.matheus.cadastroBolsistas.service.FrequenciaService;
import dev.matheus.cadastroBolsistas.service.LaboratorioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/*
 * responsavel pelo crud de frequencia.
 * get: lista registros e carrega formulario de edicao (action=editar|excluir).
 * post: registra novo ou atualiza existente (id > 0).
 * admin pode registrar para qualquer bolsista, editar e excluir qualquer registro.
 * professor pode registrar e verificar a frequencia dos bolsistas do seu laboratorio.
 * bolsista comum so pode registrar e editar os proprios.
 */
@Controller
@RequestMapping("/frequencia")
public class FrequenciaController {

    @Autowired
    private FrequenciaService frequenciaService;

    @Autowired
    private BolsistaService bolsistaService;

    @Autowired
    private LaboratorioService laboratorioService;

    @GetMapping
    public String handleGet(@RequestParam(required = false) String action,
                            @RequestParam(required = false) String id,
                            HttpSession session,
                            Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");

        if ("editar".equals(action)) {
            try {
                int freqId = Integer.parseInt(id);
                Frequencia f = frequenciaService.buscarPorId(freqId);
                if (f == null) {
                    model.addAttribute("erro", "Registro de frequencia nao encontrado.");
                } else if (!podeGerenciarFrequencia(usuarioLogado, f)) {
                    return "redirect:/frequencia";
                } else {
                    model.addAttribute("frequenciaEdicao", f);
                }
            } catch (NumberFormatException e) {
                model.addAttribute("erro", "ID da frequencia invalido.");
            } catch (SQLException e) {
                e.printStackTrace();
                model.addAttribute("erro", "Erro ao buscar frequencia.");
            }
            carregarPagina(model, usuarioLogado);
            return "frequencia";
        }

        if ("excluir".equals(action)) {
            try {
                int freqId = Integer.parseInt(id);
                Frequencia f = frequenciaService.buscarPorId(freqId);
                if (f != null && podeGerenciarFrequencia(usuarioLogado, f)) {
                    frequenciaService.excluir(freqId);
                } else {
                    model.addAttribute("erro", "Sem permissao para excluir frequencia.");
                }
            } catch (NumberFormatException e) {
                model.addAttribute("erro", "ID da frequencia invalido.");
            } catch (SQLException e) {
                e.printStackTrace();
                model.addAttribute("erro", "Erro ao excluir frequencia.");
            }
            return "redirect:/frequencia";
        }

        carregarPagina(model, usuarioLogado);
        return "frequencia";
    }

    @PostMapping
    public String salvar(@RequestParam(required = false) String id,
                         @RequestParam(required = false) String data,
                         @RequestParam(required = false) String horas,
                         @RequestParam(required = false) String descricao,
                         @RequestParam(required = false) String bolsistaId,
                         HttpSession session,
                         Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");

        String erroValidacao = validarFrequencia(data, horas, limpar(descricao));
        if (erroValidacao != null) {
            model.addAttribute("erro", erroValidacao);
            carregarPagina(model, usuarioLogado);
            return "frequencia";
        }

        int freqId = 0;
        if (!estaVazio(id)) {
            try {
                freqId = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                model.addAttribute("erro", "ID da frequencia invalido.");
                carregarPagina(model, usuarioLogado);
                return "frequencia";
            }
        }

        // fluxo de atualizacao de registro existente
        if (freqId > 0) {
            try {
                Frequencia existente = frequenciaService.buscarPorId(freqId);
                if (existente == null) {
                    model.addAttribute("erro", "Registro de frequencia nao encontrado.");
                    carregarPagina(model, usuarioLogado);
                    return "frequencia";
                }
                if (!podeGerenciarFrequencia(usuarioLogado, existente)) {
                    return "redirect:/frequencia";
                }
                existente.setData(LocalDate.parse(data));
                existente.setHorasTrabalhadas(Double.parseDouble(horas));
                existente.setDescricao(limpar(descricao));

                if (frequenciaService.atualizar(existente)) {
                    return "redirect:/frequencia";
                } else {
                    model.addAttribute("erro", "Erro ao atualizar frequencia.");
                    carregarPagina(model, usuarioLogado);
                    return "frequencia";
                }
            } catch (SQLException e) {
                e.printStackTrace();
                model.addAttribute("erro", "Erro ao atualizar frequencia.");
                carregarPagina(model, usuarioLogado);
                return "frequencia";
            }
        }

        // fluxo de novo registro
        int idBolsista;
        if (usuarioLogado.isAdmin() || usuarioLogado.isProfessor()) {
            if (estaVazio(bolsistaId)) {
                model.addAttribute("erro", "Selecione o bolsista para registrar a frequencia.");
                carregarPagina(model, usuarioLogado);
                return "frequencia";
            }
            try {
                idBolsista = Integer.parseInt(bolsistaId);
                Bolsista b = bolsistaService.buscarPorId(idBolsista);
                if (usuarioLogado.isProfessor() && !podeGerenciarBolsista(usuarioLogado, b)) {
                    model.addAttribute("erro", "Sem permissao para registrar frequencia para este bolsista.");
                    carregarPagina(model, usuarioLogado);
                    return "frequencia";
                }
            } catch (Exception e) {
                model.addAttribute("erro", "Bolsista invalido.");
                carregarPagina(model, usuarioLogado);
                return "frequencia";
            }
        } else {
            // bolsista comum sempre registra para si mesmo
            idBolsista = usuarioLogado.getId();
        }

        Frequencia f = new Frequencia();
        f.setBolsistaId(idBolsista);
        f.setData(LocalDate.parse(data));
        f.setHorasTrabalhadas(Double.parseDouble(horas));
        f.setDescricao(limpar(descricao));

        try {
            if (frequenciaService.registrar(f)) {
                return "redirect:/frequencia";
            } else {
                model.addAttribute("erro", "Erro ao registrar frequencia.");
                carregarPagina(model, usuarioLogado);
                return "frequencia";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("erro", "Erro ao registrar frequencia.");
            carregarPagina(model, usuarioLogado);
            return "frequencia";
        }
    }

    private void carregarPagina(Model model, Usuario usuarioLogado) {
        try {
            if (usuarioLogado.isAdmin()) {
                model.addAttribute("listaFrequencia", frequenciaService.listarTodas());
                model.addAttribute("listaBolsistas", bolsistaService.listarTodos());
            } else if (usuarioLogado.isProfessor()) {
                ArrayList<Laboratorio> labsCoordenados = laboratorioService.listarPorCoordenador(usuarioLogado.getId());
                ArrayList<Frequencia> listaFreq = new ArrayList<>();
                ArrayList<Bolsista> listaBolsistas = new ArrayList<>();
                for (Laboratorio l : labsCoordenados) {
                    listaFreq.addAll(frequenciaService.listarPorLaboratorio(l.getId()));
                    listaBolsistas.addAll(bolsistaService.buscarPorLaboratorio(l.getId()));
                }
                model.addAttribute("listaFrequencia", listaFreq);
                model.addAttribute("listaBolsistas", listaBolsistas);
            } else {
                model.addAttribute("listaFrequencia", frequenciaService.listarPorBolsista(usuarioLogado.getId()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean podeGerenciarFrequencia(Usuario usuarioLogado, Frequencia f) {
        if (usuarioLogado.isAdmin()) return true;
        if (usuarioLogado.isBolsista() && f.getBolsistaId() == usuarioLogado.getId()) return true;
        if (usuarioLogado.isProfessor()) {
            try {
                Bolsista b = bolsistaService.buscarPorId(f.getBolsistaId());
                return podeGerenciarBolsista(usuarioLogado, b);
            } catch (SQLException e) {
                return false;
            }
        }
        return false;
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

    private String validarFrequencia(String dataStr, String horasStr, String descricao) {
        if (estaVazio(dataStr)) {
            return "A data da frequencia e obrigatoria.";
        }
        try {
            LocalDate d = LocalDate.parse(dataStr);
            if (d.isAfter(LocalDate.now())) {
                return "A data da frequencia nao pode ser futura.";
            }
        } catch (Exception e) {
            return "A data da frequencia e invalida.";
        }
        if (estaVazio(horasStr)) {
            return "A quantidade de horas e obrigatoria.";
        }
        try {
            double horas = Double.parseDouble(horasStr);
            if (horas <= 0) {
                return "A quantidade de horas deve ser maior que zero.";
            }
            if (horas > 24) {
                return "A quantidade de horas nao pode ser maior que 24.";
            }
        } catch (NumberFormatException e) {
            return "A quantidade de horas deve ser um numero valido.";
        }
        if (descricao != null && descricao.length() > 500) {
            return "A descricao deve ter no maximo 500 caracteres.";
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
