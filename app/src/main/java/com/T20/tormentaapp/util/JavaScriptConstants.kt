package com.T20.tormentaapp.util

object JavaScriptConstants {

    const val CAPTURAR_BLOB = """
        (function() {
            // Intercepta window.URL.createObjectURL
            var originalCreateObjectURL = window.URL.createObjectURL;
            window.URL.createObjectURL = function(blob) {
                var url = originalCreateObjectURL(blob);
                if (blob && blob.type === 'application/pdf') {
                    var reader = new FileReader();
                    reader.onload = function(e) {
                        var base64 = e.target.result.split(',')[1];
                        AndroidBlobDownload.salvarPDF(base64, 'ficha_' + Date.now() + '.pdf');
                    };
                    reader.readAsDataURL(blob);
                } else if (blob && blob.type === 'application/json') {
                    var reader = new FileReader();
                    reader.onload = function(e) {
                        var base64 = e.target.result.split(',')[1];
                        AndroidBlobDownload.salvarJSON(base64, 'foundry_' + Date.now() + '.json');
                    };
                    reader.readAsDataURL(blob);
                }
                return url;
            };

            // Captura cliques em botões de download
            document.addEventListener('click', function(e) {
                var target = e.target.closest('button, a, div[role="button"]');
                if (!target) return;
                var text = target.innerText || '';
                if (text.includes('Gerar PDF') || text.includes('Exportar para Foundry')) {
                    setTimeout(function() {
                        var links = document.querySelectorAll('a[download]');
                        links.forEach(function(link) {
                            if (link.href && link.href.startsWith('blob:')) {
                                fetch(link.href)
                                    .then(res => res.blob())
                                    .then(blob => {
                                        var reader = new FileReader();
                                        reader.onload = function(e) {
                                            var base64 = e.target.result.split(',')[1];
                                            if (blob.type === 'application/pdf') {
                                                AndroidBlobDownload.salvarPDF(base64, 'ficha_' + Date.now() + '.pdf');
                                            } else if (blob.type === 'application/json') {
                                                AndroidBlobDownload.salvarJSON(base64, 'foundry_' + Date.now() + '.json');
                                            }
                                        };
                                        reader.readAsDataURL(blob);
                                    });
                            }
                        });
                    }, 1000);
                }
            });
        })();
    """
}