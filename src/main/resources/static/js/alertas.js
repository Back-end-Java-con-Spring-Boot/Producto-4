document.addEventListener("DOMContentLoaded", function() {

    const alertas = document.querySelectorAll('.alert-success');
    alertas.forEach(alerta => {
        setTimeout(() => {
            if (typeof bootstrap !== 'undefined') {
                const bsAlert = new bootstrap.Alert(alerta);
                bsAlert.close();
            }
        }, 2000);
    });
});