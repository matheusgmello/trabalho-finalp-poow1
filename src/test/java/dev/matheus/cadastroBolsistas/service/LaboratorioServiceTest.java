package dev.matheus.cadastroBolsistas.service;

import dev.matheus.cadastroBolsistas.dao.LaboratorioDAO;
import dev.matheus.cadastroBolsistas.dao.ProjetoDAO;
import dev.matheus.cadastroBolsistas.model.Bolsista;
import dev.matheus.cadastroBolsistas.model.Laboratorio;
import dev.matheus.cadastroBolsistas.model.Professor;
import dev.matheus.cadastroBolsistas.model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LaboratorioServiceTest {

    @Mock
    private LaboratorioDAO dao;

    @Mock
    private ProjetoDAO projetoDao;

    @InjectMocks
    private LaboratorioService laboratorioService;

    @Test
    void podeGerenciar_adminSempreRetornaTrue() throws SQLException {
        // Usuario admin (isAdmin()=true)
        Professor admin = new Professor();
        admin.setTipoUsuario("ADMIN");

        assertTrue(laboratorioService.podeGerenciar(admin, 999));
        // dao.getLaboratorioPorId NUNCA deve ser chamado
        verifyNoInteractions(dao);
    }

    @Test
    void podeGerenciar_professorCoordenadorRetornaTrue() throws SQLException {
        // Professor com id=10
        Professor professor = new Professor();
        professor.setId(10);
        professor.setTipoUsuario("PROFESSOR");

        Laboratorio lab = new Laboratorio();
        lab.setId(5);
        lab.setCoordenadorId(10);

        when(dao.getLaboratorioPorId(5)).thenReturn(lab);

        assertTrue(laboratorioService.podeGerenciar(professor, 5));
        verify(dao).getLaboratorioPorId(5);
    }

    @Test
    void podeGerenciar_professorNaoCoordenadorRetornaFalse() throws SQLException {
        // Professor com id=10
        Professor professor = new Professor();
        professor.setId(10);
        professor.setTipoUsuario("PROFESSOR");

        Laboratorio lab = new Laboratorio();
        lab.setId(5);
        lab.setCoordenadorId(99);

        when(dao.getLaboratorioPorId(5)).thenReturn(lab);

        assertFalse(laboratorioService.podeGerenciar(professor, 5));
        verify(dao).getLaboratorioPorId(5);
    }

    @Test
    void podeGerenciar_professorLabInexistenteRetornaFalse() throws SQLException {
        Professor professor = new Professor();
        professor.setId(10);
        professor.setTipoUsuario("PROFESSOR");

        when(dao.getLaboratorioPorId(5)).thenReturn(null);

        assertFalse(laboratorioService.podeGerenciar(professor, 5));
        verify(dao).getLaboratorioPorId(5);
    }

    @Test
    void podeGerenciar_bolsistaRetornaFalse() throws SQLException {
        Bolsista bolsista = new Bolsista();
        bolsista.setTipoUsuario("BOLSISTA");

        assertFalse(laboratorioService.podeGerenciar(bolsista, 5));
        verifyNoInteractions(dao);
    }

    @Test
    void podeGerenciar_usuarioNullRetornaFalse() throws SQLException {
        assertFalse(laboratorioService.podeGerenciar(null, 5));
        verifyNoInteractions(dao);
    }

    @Test
    void temVaga_retornaTrueQuandoLabTemEspaco() throws SQLException {
        Laboratorio lab = new Laboratorio();
        lab.setId(1);
        lab.setCapacidade(10);

        when(dao.getLaboratorioPorId(1)).thenReturn(lab);
        when(dao.contarBolsistasNoLaboratorio(1)).thenReturn(5);

        assertTrue(laboratorioService.temVaga(1));
        verify(dao).getLaboratorioPorId(1);
        verify(dao).contarBolsistasNoLaboratorio(1);
    }

    @Test
    void temVaga_retornaFalseQuandoLabEstaLotado() throws SQLException {
        Laboratorio lab = new Laboratorio();
        lab.setId(1);
        lab.setCapacidade(10);

        when(dao.getLaboratorioPorId(1)).thenReturn(lab);
        when(dao.contarBolsistasNoLaboratorio(1)).thenReturn(10);

        assertFalse(laboratorioService.temVaga(1));
        verify(dao).getLaboratorioPorId(1);
        verify(dao).contarBolsistasNoLaboratorio(1);
    }

    @Test
    void temVaga_retornaFalseQuandoLabNaoExiste() throws SQLException {
        when(dao.getLaboratorioPorId(1)).thenReturn(null);

        assertFalse(laboratorioService.temVaga(1));
        verify(dao).getLaboratorioPorId(1);
        verify(dao, never()).contarBolsistasNoLaboratorio(anyInt());
    }
}
