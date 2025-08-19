const form = document.getElementById('tleForm');
const result = document.getElementById('result');
const errorContainer = document.getElementById('error-container');

function createErrorNotification(message, priority) {
    const errorDiv = document.createElement('div');
    errorDiv.className = `error-notification ${priority.toLowerCase()}`;
    errorDiv.innerHTML = `
        <span>${message}</span>
        <button class="close-btn">×</button>
    `;
    errorContainer.appendChild(errorDiv);

    // Закрытие уведомления
    errorDiv.querySelector('.close-btn').addEventListener('click', () => {
        errorDiv.style.opacity = '0';
        setTimeout(() => errorDiv.remove(), 300);
    });

    // Автоматическое закрытие через 5 секунд
    setTimeout(() => {
        errorDiv.style.opacity = '0';
        setTimeout(() => errorDiv.remove(), 300);
    }, 5000);
}

form.addEventListener('submit', async (e) => {
    e.preventDefault();
    result.textContent = '';
    errorContainer.innerHTML = ''; // Очищаем предыдущие ошибки

    const noradId = document.getElementById('noradId').value.trim();

    // Валидация ввода
    if (!noradId || isNaN(noradId) || noradId <= 0) {
        createErrorNotification('Пожалуйста, введите корректный NORAD ID (положительное число)', 'high');
        return;
    }

    try {
        const response = await fetch('/api/v1/tle/noradId', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(noradId)
        });

        if (!response.ok) {
            const errorData = await response.json();
            const statusCode = response.status;
            let errorMessage = errorData.message || 'Неизвестная ошибка';
            let priority = 'low';

            if (statusCode === 401) {
                errorMessage = `Ошибка аутентификации: ${errorMessage}`;
                priority = 'high';
            } else if (statusCode === 404) {
                errorMessage = `Спутник не найден: ${errorMessage}`;
                priority = 'medium';
            } else if (statusCode === 400) {
                errorMessage = `Некорректный запрос: ${errorMessage}`;
                priority = 'high';
            } else {
                errorMessage = `Ошибка сервера: ${errorMessage}`;
                priority = 'low';
            }

            createErrorNotification(errorMessage, priority);
            return;
        }

        const tleText = await response.text();
        const lines = tleText.trim().split('\n').filter(line => line.trim() !== '');

        if (lines.length < 2) {
            createErrorNotification('Полученные данные не содержат TLE в ожидаемом формате', 'medium');
            return;
        }

        let outputLines = lines.length >= 3 && !lines[0].startsWith('1 ') && !lines[0].startsWith('2 ')
            ? lines.slice(1, 3)
            : lines.slice(0, 2);

        result.textContent = outputLines.join('\n');

    } catch (err) {
        createErrorNotification('Не удалось выполнить запрос: ' + err.message, 'low');
    }
});