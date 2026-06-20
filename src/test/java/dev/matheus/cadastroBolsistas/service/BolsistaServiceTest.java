package dev.matheus.cadastroBolsistas.service;

import dev.matheus.cadastroBolsistas.dao.BolsistaDAO;
import dev.matheus.cadastroBolsistas.dao.LaboratorioDAO;
import dev.matheus.cadastroBolsistas.model.Bolsista;
import dev.matheus.cadastroBolsistas.model.Laboratorio;
import dev.matheus.cadastroBolsistas.model.Professor;
import dev.matheus.cadastroBolsistas.model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BolsistaServiceTest {

    @Mock
    private BolsistaDAO dao;

    @Mock
    private LaboratorioDAO laboratorioDao;

    @InjectMocks
    private BolsistaService bolsistaService;

    @Test
    void podeGerenciar_adminSempreRetornaTrue() throws SQLException {
        Professor admin = new Professor();
        admin.setTipoUsuario("ADMIN");

        Bolsista bolsista = new Bolsista();
        bolsista.setId(5);

        assertTrue(bolsistaService.podeGerenciar(admin, bolsista));
        verifyNoInteractions(laboratorioDao);
    }

    @Test
    void podeGerenciar_professorCoordenadorDaboLaboratorio() throws SQLException {
        Professor professor = new Professor();
        professor.setId(10);
        professor.setTipoUsuario("PROFESSOR");

        Bolsista bolsista = new Bolsista();
        bolsista.setId(5);
        bolsista.setLaboratorioId(3);

        Laboratorio lab = new Laboratorio();
        lab.setId(3);
        lab.setCoordenadorId(10);

        when(laboratorioDao.getLaboratorioPorId(3)).thenReturn(lab);

        assertTrue(bolsistaService.podeGerenciar(professor, bolsista));
        verify(laboratorioDao).getLaboratorioPorId(3);
    }

    @Test
    void podeGerenciar_professorDeOutroLaboratorio() throws SQLException {
        Professor professor = new Professor();
        professor.setId(10);
        professor.setTipoUsuario("PROFESSOR");

        Bolsista bolsista = new Bolsista();
        bolsista.setId(5);
        bolsista.setLaboratorioId(3);

        Laboratorio lab = new Laboratorio();
        lab.setId(3);
        lab.setCoordenadorId(99);

        when(laboratorioDao.getLaboratorioPorId(3)).thenReturn(lab);

        assertFalse(bolsistaService.podeGerenciar(professor, bolsista));
        verify(laboratorioDao).getLaboratorioPorId(3);
    }

    @Test
    void podeGerenciar_professorEBolsistaSemLaboratorio() throws SQLException {
        Professor professor = new Professor();
        professor.setId(10);
        professor.setTipoUsuario("PROFESSOR");

        Bolsista bolsista = new Bolsista();
        bolsista.setId(5);
        bolsista.setLaboratorioId(0);

        assertFalse(bolsistaService.podeGerenciar(professor, bolsista));
        verifyNoInteractions(laboratorioDao);
    }

    @Test
    void podeGerenciar_bolsistaRetornaFalse() throws SQLException {
        Bolsista bolsistaLogado = new Bolsista();
        bolsistaLogado.setTipoUsuario("BOLSISTA");

        Bolsista bolsistaAlvo = new Bolsista();
        bolsistaAlvo.setId(5);

        assertFalse(bolsistaService.podeGerenciar(bolsistaLogado, bolsistaAlvo));
        verifyNoInteractions(laboratorioDao);
    }

    @Test
    void podeGerenciar_usuarioNullRetornaFalse() throws SQLException {
        Bolsista bolsista = new Bolsista();
        bolsista.setId(5);

        assertFalse(bolsistaService.podeGerenciar(null, bolsista));
        verifyNoInteractions(laboratorioDao);
    }

    @Test
    void podeGerenciar_bolsistaAlvoNullRetornaFalse() throws SQLException {
        Professor professor = new Professor();
        professor.setTipoUsuario("PROFESSOR");

        assertFalse(bolsistaService.podeGerenciar(professor, null));
        verifyNoInteractions(laboratorioDao);
    }

    @Test
    void inserir_setaAtivoTrueAntesDeDelegar() throws SQLException {
        Bolsista bolsista = new Bolsista();
        bolsista.setAtivo(false);

        when(dao.inserir(any(Bolsista.class))).thenReturn(true);

        boolean result = bolsistaService.inserir(bolsista);

        assertTrue(result);
        ArgumentCaptor<Bolsista> captor = ArgumentCaptor.forClass(Bolsista.class);
        verify(dao).inserir(captor.capture());
        
        Bolsista capturado = captor.getValue();
        assertTrue(capturado.isAtivo());
    }
}
