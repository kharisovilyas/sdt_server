document.addEventListener('DOMContentLoaded', function() {
    // Устанавливаем значения по умолчанию
    document.getElementById('username').value = 'designer';
    document.getElementById('password').value = 'password';
});

document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    const loginBtn = document.getElementById('loginBtn');
    const loginError = document.getElementById('loginError');
    loginError.classList.add('hidden');
    loginBtn.disabled = true;
    loginBtn.textContent = 'Входим...';

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const landingPage = document.getElementById('landingPage').value;

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`,
            credentials: 'include'
        });

        if (response.ok) {
            // Редиректим на выбранную страницу!
            window.location.href = landingPage;
        } else {
            const errorText = await response.text();
            loginError.textContent = errorText || "Ошибка входа. Проверьте данные!";
            loginError.classList.remove('hidden');
        }
    } catch (err) {
        loginError.textContent = "Ошибка сети.";
        loginError.classList.remove('hidden');
    } finally {
        loginBtn.disabled = false;
        loginBtn.textContent = 'Войти';
    }
});