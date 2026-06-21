package dev.matheus.cadastroBolsistas.service;

import dev.matheus.cadastroBolsistas.dao.ProfessorDAO;
import dev.matheus.cadastroBolsistas.model.Professor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfessorServiceTest {

    @Mock
    private ProfessorDAO dao;

    @InjectMocks
    private ProfessorService professorService;

    @Test
    void inserir_delegaInsercaoParaDAO() throws SQLException {
        Professor p = new Professor();
        p.setNome("Prof Roberto");
        when(dao.inserir(p)).thenReturn(true);

        assertTrue(professorService.inserir(p));
        verify(dao).inserir(p);
    }

    @Test
    void listarTodos_retornaListaRetornadaPeloDAO() throws SQLException {
        ArrayList<Professor> lista = new ArrayList<>();
        lista.add(new Professor());
        lista.add(new Professor());
        when(dao.getProfessores()).thenReturn(lista);

        ArrayList<Professor> resultado = professorService.listarTodos();

        assertEquals(2, resultado.size());
        verify(dao).getProfessores();
    }

    @Test
    void listarTodos_semProfessores_retornaListaVazia() throws SQLException {
        when(dao.getProfessores()).thenReturn(new ArrayList<>());

        assertTrue(professorService.listarTodos().isEmpty());
    }

    @Test
    void buscarPorId_professorExistente_retornaObjeto() throws SQLException {
        Professor p = new Professor();
        p.setId(1);
        p.setNome("Prof Roberto");
        when(dao.getProfessorPorId(1)).thenReturn(p);

        Professor resultado = professorService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Prof Roberto", resultado.getNome());
        verify(dao).getProfessorPorId(1);
    }

    @Test
    void buscarPorId_professorInexistente_retornaNull() throws SQLException {
        when(dao.getProfessorPorId(99)).thenReturn(null);

        assertNull(professorService.buscarPorId(99));
        verify(dao).getProfessorPorId(99);
    }

    @Test
    void atualizar_delegaAtualizacaoParaDAO() throws SQLException {
        Professor p = new Professor();
        p.setId(1);
        when(dao.atualizar(p)).thenReturn(true);

        assertTrue(professorService.atualizar(p));
        verify(dao).atualizar(p);
    }

    @Test
    void excluir_delegaExclusaoParaDAO() throws SQLException {
        when(dao.excluir(5)).thenReturn(true);

        assertTrue(professorService.excluir(5));
        verify(dao).excluir(5);
    }

    @Test
    void buscarPorNome_retornaListaFiltradaPeloDAO() throws SQLException {
        ArrayList<Professor> lista = new ArrayList<>();
        Professor p = new Professor();
        p.setNome("Roberto Mendes");
        lista.add(p);
        when(dao.getProfessoresPorNome("Roberto")).thenReturn(lista);

        ArrayList<Professor> resultado = professorService.buscarPorNome("Roberto");

        assertEquals(1, resultado.size());
        assertEquals("Roberto Mendes", resultado.get(0).getNome());
        verify(dao).getProfessoresPorNome("Roberto");
    }

    @Test
    void buscarPorNome_semResultados_retornaListaVazia() throws SQLException {
        when(dao.getProfessoresPorNome("Inexistente")).thenReturn(new ArrayList<>());

        assertTrue(professorService.buscarPorNome("Inexistente").isEmpty());
    }
}
