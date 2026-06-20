package dev.matheus.cadastroBolsistas.service;

import dev.matheus.cadastroBolsistas.dao.BolsistaDAO;
import dev.matheus.cadastroBolsistas.dao.ProfessorDAO;
import dev.matheus.cadastroBolsistas.model.Bolsista;
import dev.matheus.cadastroBolsistas.model.Professor;
import dev.matheus.cadastroBolsistas.model.Usuario;
import dev.matheus.cadastroBolsistas.util.SecurityUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private BolsistaDAO bolsistaDao;

    @Mock
    private ProfessorDAO professorDao;

    @InjectMocks
    private LoginService loginService;

    @Test
    void autenticar_retornaBolsistaQuandoCredenciaisValidas() throws SQLException {
        String email = "bolsista@teste.com";
        String senha = "teste123";
        String hashSenha = SecurityUtil.hashSenha(senha);

        Bolsista mockBolsista = new Bolsista();
        mockBolsista.setEmail(email);

        when(bolsistaDao.autenticar(email, hashSenha)).thenReturn(mockBolsista);

        Usuario resultado = loginService.autenticar(email, senha);

        assertNotNull(resultado);
        assertEquals(email, resultado.getEmail());
        verify(bolsistaDao).autenticar(email, hashSenha);
        verifyNoInteractions(professorDao);
    }

    @Test
    void autenticar_tentaProfessorQuandoBolsistaRetornaNull() throws SQLException {
        String email = "professor@teste.com";
        String senha = "teste123";
        String hashSenha = SecurityUtil.hashSenha(senha);

        Professor mockProfessor = new Professor();
        mockProfessor.setEmail(email);

        when(bolsistaDao.autenticar(email, hashSenha)).thenReturn(null);
        when(professorDao.autenticar(email, hashSenha)).thenReturn(mockProfessor);

        Usuario resultado = loginService.autenticar(email, senha);

        assertNotNull(resultado);
        assertEquals(email, resultado.getEmail());
        verify(bolsistaDao).autenticar(email, hashSenha);
        verify(professorDao).autenticar(email, hashSenha);
    }

    @Test
    void autenticar_retornaNullQuandoAmbosOsDAOsRetornamNull() throws SQLException {
        String email = "inexistente@teste.com";
        String senha = "teste123";
        String hashSenha = SecurityUtil.hashSenha(senha);

        when(bolsistaDao.autenticar(email, hashSenha)).thenReturn(null);
        when(professorDao.autenticar(email, hashSenha)).thenReturn(null);

        Usuario resultado = loginService.autenticar(email, senha);

        assertNull(resultado);
        verify(bolsistaDao).autenticar(email, hashSenha);
        verify(professorDao).autenticar(email, hashSenha);
    }

    @Test
    void autenticar_hasheiaSenhaAntesDePassarParaODAO() throws SQLException {
        String email = "bolsista@teste.com";
        String senha = "teste123";
        String hashSenha = SecurityUtil.hashSenha(senha);

        loginService.autenticar(email, senha);

        // Verifica que o DAO foi chamado com o hash correto, e não com texto puro "teste123"
        verify(bolsistaDao).autenticar(email, hashSenha);
        verify(bolsistaDao, never()).autenticar(email, senha);
    }
}
