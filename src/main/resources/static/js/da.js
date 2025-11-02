document.addEventListener("DOMContentLoaded", function () {
    // Récupérer les données depuis les attributs data-*
    const chartData = document.getElementById('chart-data');
    const labels = JSON.parse(chartData.getAttribute('data-filieres'));
    const dataValues = JSON.parse(chartData.getAttribute('data-nombre-etudiants'));

    let predefinedColors = ['#007bff', '#28a745', '#dc3545', '#ffc107', '#17a2b8', '#6610f2', '#e83e8c', '#6f42c1', '#20c997', '#fd7e14'];

    function generateRandomColors(count, existingColors) {
        let colors = [...existingColors];
        while (colors.length < count) {
            let color = `hsl(${Math.floor(Math.random() * 360)}, 70%, 60%)`;
            colors.push(color);
        }
        return colors;
    }

    let colors = generateRandomColors(labels.length, predefinedColors);

    // Graphique en camembert
    const ctx1 = document.getElementById('pieChart').getContext('2d');
    new Chart(ctx1, {
        type: 'pie',
        data: {
            labels: labels,
            datasets: [{
                data: dataValues,
                backgroundColor: colors,
            }]
        }
    });

    // Graphique en barres
    const ctx2 = document.getElementById('barChart').getContext('2d');
    new Chart(ctx2, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Nombre des Étudiants',
                data: dataValues,
                backgroundColor: colors
            }]
        },
        options: {
            responsive: true,
            scales: {
                yAxes: [{
                    ticks: { beginAtZero: true }
                }]
            }
        }
    });
});