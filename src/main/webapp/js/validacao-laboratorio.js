document.addEventListener('DOMContentLoaded', function() {
    const formLab = document.getElementById('formLab');
    if (formLab) {
        formLab.onsubmit = function(e) {
            const nome = document.getElementById('nome').value;
            const titulo = document.getElementById('tituloProjeto').value;
            
            if (nome.trim().length < 3) {
                alert('O nome do laboratório deve ter pelo menos 3 caracteres.');
                e.preventDefault();
                return false;
            }
            if (titulo.trim().length < 5) {
                alert('O título do projeto deve ter pelo menos 5 caracteres.');
                e.preventDefault();
                return false;
            }
        };
    }
});
