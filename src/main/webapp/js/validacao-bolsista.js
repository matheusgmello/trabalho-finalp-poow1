document.addEventListener('DOMContentLoaded', function() {
    const formBolsista = document.getElementById('formBolsista');
    if (formBolsista) {
        formBolsista.onsubmit = function(e) {
            const senha = document.getElementById('senha').value;
            const nome = document.getElementById('nome').value;
            
            if (nome.trim().length < 3) {
                alert('O nome deve ter pelo menos 3 caracteres.');
                e.preventDefault();
                return false;
            }
            if (senha.length < 6) {
                alert('A senha deve ter pelo menos 6 caracteres.');
                e.preventDefault();
                return false;
            }
        };
    }

    const tipoUsuario = document.getElementById('tipoUsuario');
    const groupDataNasc = document.getElementById('group-dataNascimento');
    const groupCurso = document.getElementById('group-curso');
    const groupMatricula = document.getElementById('group-matricula');
    const groupLaboratorio = document.getElementById('group-laboratorio');
    const groupFuncao = document.getElementById('group-funcao');

    const inputDataNasc = document.getElementById('dataNascimento');
    const inputCurso = document.getElementById('curso');
    const inputMatricula = document.getElementById('matricula');
    const selectLaboratorio = document.getElementById('laboratorioId');
    const inputFuncao = document.getElementById('funcao');

    function toggleFields() {
        if (!tipoUsuario) return;
        const val = tipoUsuario.value;
        if (val === 'PROFESSOR') {
            if (groupDataNasc) groupDataNasc.style.display = 'none';
            if (groupCurso) groupCurso.style.display = 'none';
            if (groupMatricula) groupMatricula.style.display = 'none';
            if (groupLaboratorio) groupLaboratorio.style.display = 'none';
            if (groupFuncao) groupFuncao.style.display = 'none';

            if (inputDataNasc) { inputDataNasc.disabled = true; inputDataNasc.required = false; }
            if (inputCurso) { inputCurso.disabled = true; inputCurso.required = false; }
            if (inputMatricula) { inputMatricula.disabled = true; inputMatricula.required = false; }
            if (selectLaboratorio) { selectLaboratorio.disabled = true; }
            if (inputFuncao) { inputFuncao.disabled = true; }
        } else {
            if (groupDataNasc) groupDataNasc.style.display = '';
            if (groupCurso) groupCurso.style.display = '';
            if (groupMatricula) groupMatricula.style.display = '';
            if (groupLaboratorio) groupLaboratorio.style.display = '';
            if (groupFuncao) groupFuncao.style.display = '';

            if (inputDataNasc) { inputDataNasc.disabled = false; inputDataNasc.required = true; }
            if (inputCurso) { inputCurso.disabled = false; inputCurso.required = true; }
            if (inputMatricula) { inputMatricula.disabled = false; inputMatricula.required = true; }
            if (selectLaboratorio) { selectLaboratorio.disabled = false; }
            if (inputFuncao) { inputFuncao.disabled = false; }
        }
    }

    if (tipoUsuario) {
        tipoUsuario.addEventListener('change', toggleFields);
        toggleFields();
    }
});
