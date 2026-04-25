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
});
