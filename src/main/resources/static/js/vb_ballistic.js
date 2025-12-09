const submitButton = document.getElementById('submitButton');
const promptInput = document.getElementById('promptInput');
const responseContainer = document.getElementById('responseContainer');
const rocketIcon = document.getElementById('rocketIcon');
const progressBarWrapper = document.getElementById('progressBarWrapper');
const progressBarFill = document.getElementById('progressBarFill');
const progressBarText = document.getElementById('progressBarText');
const expandProgress = document.getElementById('expandProgress');
const progressModal = document.getElementById('progressModal');
const closeModal = document.getElementById('closeModal');
const progressStages = document.getElementById('progressStages');
const glassmorphismContainer = document.querySelector('.glassmorphism-container');

let currentTaskId = null;
let pollInterval = null;
let mapProgressData = null;

// --- 1. Авто-рост textarea
promptInput.addEventListener('input', autoGrowTextarea);

function autoGrowTextarea() {
    promptInput.style.height = 'auto';
    promptInput.style.height = (promptInput.scrollHeight) + "px";
}

window.addEventListener('DOMContentLoaded', autoGrowTextarea);

// --- 2. Submit
submitButton.addEventListener('click', async () => {
    const prompt = promptInput.value.trim();
    if (!prompt) {
        promptInput.classList.add('ring-2', 'ring-red-400');
        setTimeout(() => promptInput.classList.remove('ring-2', 'ring-red-400'), 1500);
        return;
    }

    // UI prepare
    showProgressBar();
    setProgress(5, "Создание задачи...");
    rocketIcon.classList.add('animate-spin', 'text-blue-400');
    submitButton.disabled = true;
    responseContainer.innerHTML = '';

    try {
        // --- 1. Отправляем prompt
        const response = await fetch('/api/v1/vb', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(prompt),
        });

        if (!response.ok) throw new Error(`HTTP ${response.status}`);
        currentTaskId = await response.text();
        setProgress(10, "Задача создана...");

        // --- 2. Запускаем опрос прогресса
        pollProgress(currentTaskId);

    } catch (error) {
        setProgress(100, "Ошибка");
        responseContainer.innerHTML = `<div class="sat-card sat-card-row text-red-300">Ошибка: ${error.message}</div>`;
        rocketIcon.classList.remove('animate-spin', 'text-blue-400');
        submitButton.disabled = false;
    }
});

// --- 3. Опрос прогресса
async function pollProgress(taskId) {
    if (pollInterval) clearInterval(pollInterval);

    pollInterval = setInterval(async () => {
        try {
            const response = await fetch(`/api/v1/vb/progress/${taskId}`);
            if (!response.ok) throw new Error(`Ошибка опроса: ${response.status}`);
            const data = await response.json();

            setProgress(data.percent || 0, data.stage || "");

            if (data.status === "COMPLETED") {
                progressRunning(data.mapProgress);
                clearInterval(pollInterval);
                setProgress(100, "Готово!");
                progressBarFill.classList.add('completed');
                displayResponse(data.result || []);
                finishUI();
                submitButton.disabled = false; // Разблокировка кнопки только при COMPLETED
            } else if (data.status === "PROGRESS") {
                progressRunning(data.mapProgress);
            } else if (data.status === "ERROR") {
                clearInterval(pollInterval);
                setProgress(100, "Ошибка");
                responseContainer.innerHTML = `<div class="sat-card sat-card-row text-red-300">Ошибка: ${data.stage}</div>`;
                finishUI();
                submitButton.disabled = false; // Разблокировка при ошибке
            }
        } catch (err) {
            console.warn("Ошибка опроса:", err);
            submitButton.disabled = false; // Разблокировка при ошибке опроса
        }
    }, 1000);
}

function progressRunning(data) {
    if (JSON.stringify(mapProgressData) !== JSON.stringify(data)) {
        mapProgressData = data;
        updateProgressModal();
    }
}

// --- 4. UI утилиты
function showProgressBar() {
    progressBarWrapper.classList.remove('hidden');
    setProgress(0, "Ожидание...");
}

function setProgress(percent, text) {
    progressBarFill.style.width = `${Math.max(0, Math.min(percent, 100))}%`;
    progressBarText.textContent = text || "";
}

function finishUI() {
    rocketIcon.classList.remove('animate-spin', 'text-blue-400');
}

// --- 5. Кнопка раскрытия этапов

expandProgress.addEventListener('click', () => {
    const isOpen = progressModal.classList.contains('open');

    if (isOpen) {
        // 🔒 Закрытие
        progressModal.classList.remove('open');
        glassmorphismContainer.classList.remove('modal-open');
        expandProgress.classList.remove('expanded');
    } else {
        // 🚀 Открытие
        if (mapProgressData) {
            renderProgressStages(mapProgressData);
            progressModal.classList.add('open');
            glassmorphismContainer.classList.add('modal-open');
            expandProgress.classList.add('expanded');
        } else {
            alert("Данные о прогрессе недоступны.");
        }
    }
});

function updateProgressModal() {
    if (progressModal.classList.contains('open') && mapProgressData) {
        renderProgressStages(mapProgressData);
    }
}

// Функция для рендеринга этапов в модальном окне
function renderProgressStages(mapProgress) {
    progressStages.innerHTML = '';

    const stageNames = {
        "ru.spiiran.sdt_server.application.dto.llm.DtoLLMFilterResponse": "Анализ запроса LLM: Анализ естественного текста",
        "ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBResponse": "Подходящие КА из БД: Атрибутная выборка",
        "ru.spiiran.sdt_server.application.dto.selection.DtoSelectionResponse": `ИМ для региона ${mapProgress['ru.spiiran.sdt_server.application.dto.llm.DtoLLMFilterResponse']?.[0]?.filters?.coverage || 'N/A'}`,
    };

    Object.entries(mapProgress).forEach(([key, value]) => {
        const stageName = stageNames[key] || key;
        const accordion = document.createElement('div');
        accordion.classList.add('accordion');

        const header = document.createElement('div');
        header.classList.add('accordion-header');
        header.innerHTML = `${stageName} <span class="arrow">▼</span>`;
        accordion.appendChild(header);

        const content = document.createElement('div');
        content.classList.add('accordion-content');
        accordion.appendChild(content);

        if (key.includes("DtoLLMFilterResponse")) {
            value.forEach((item, index) => {
                const filters = item.filters;
                const filterDiv = document.createElement('div');
                filterDiv.innerHTML = `
                    <p><strong>Покрытие:</strong> ${filters.coverage || 'N/A'}</p>
                    <p><strong>Тип орбиты:</strong> ${filters.orbitType || 'N/A'}</p>
                    <p><strong>Масса:</strong> ${filters.mass || 'N/A'}</p>
                    <p><strong>Форм-фактор:</strong> ${filters.formFactor || 'N/A'}</p>
                    <p><strong>Статус:</strong> ${filters.status || 'N/A'}</p>
                `;
                content.appendChild(filterDiv);
            });
        } else if (key.includes("DtoVBResponse")) {
            const count = value.length;
            header.innerHTML = `${stageName} (${count} КА) <span class="arrow">▼</span>`;

            value.forEach((item, index) => {
                const subAccordion = document.createElement('div');
                subAccordion.classList.add('sub-accordion');

                const subHeader = document.createElement('div');
                subHeader.classList.add('sub-accordion-header');
                subHeader.innerHTML = `${item.tle ? item.tle.split('\n')[0] : 'N/A'} <span class="arrow">▼</span>`;
                subAccordion.appendChild(subHeader);

                const subContent = document.createElement('div');
                subContent.classList.add('sub-accordion-content');
                const model = item.modelSat;
                subContent.innerHTML = `
                    <p><strong>TLE:</strong> <pre class="font-mono inline">${item.tle ? item.tle.replace(/\n/g, '<br>') : 'N/A'}</pre></p>
                    <p><strong>Тип орбиты:</strong> ${model.orbitType || 'N/A'}</p>
                    <p><strong>Высота:</strong> ${model.altitude || 'N/A'} км</p>
                    <p><strong>Масса:</strong> ${model.mass || 'N/A'} кг</p>
                    <p><strong>Форм-фактор:</strong> ${model.formFactor || 'N/A'}</p>
                    <p><strong>Статус:</strong> ${model.status ? 'Активный' : 'Неактивный'}</p>
                    <p><strong>Дата:</strong> ${model.date ? new Date(model.date).toLocaleDateString("ru-RU") : 'N/A'}</p>
                `;
                subAccordion.appendChild(subContent);

                subHeader.addEventListener('click', () => {
                    subContent.style.display = subContent.style.display === 'block' ? 'none' : 'block';
                    subHeader.querySelector('.arrow').textContent = subContent.style.display === 'block' ? '▲' : '▼';
                    subHeader.querySelector('.arrow').classList.toggle('open');
                });

                content.appendChild(subAccordion);
            });
        } else if (key.includes("DtoSelectionResponse")) {
            const count = value.length;
            header.innerHTML = `${stageName} (${count} КА) <span class="arrow">▼</span>`;

            value.forEach((item, index) => {
                const subAccordion = document.createElement('div');
                subAccordion.classList.add('sub-accordion');

                const subHeader = document.createElement('div');
                subHeader.classList.add('sub-accordion-header');
                subHeader.innerHTML = `${item.preFiltrationResponse.tle ? item.preFiltrationResponse.tle.split('\n')[0] : 'N/A'} <span class="arrow">▼</span>`;
                subAccordion.appendChild(subHeader);

                const subContent = document.createElement('div');
                subContent.classList.add('sub-accordion-content');
                const seesSec = item.timeRegion.seesRegion * 120;
                const notSeeSec = item.timeRegion.notSeeRegion * 120;
                subContent.innerHTML = `
                    <p><strong>Время видимости:</strong> ${seesSec} с</p>
                    <p><strong>Время невидимости:</strong> ${notSeeSec} с</p>
                `;
                subAccordion.appendChild(subContent);

                subHeader.addEventListener('click', () => {
                    subContent.style.display = subContent.style.display === 'block' ? 'none' : 'block';
                    subHeader.querySelector('.arrow').textContent = subContent.style.display === 'block' ? '▲' : '▼';
                    subHeader.querySelector('.arrow').classList.toggle('open');
                });

                content.appendChild(subAccordion);
            });
        }

        header.addEventListener('click', () => {
            content.style.display = content.style.display === 'block' ? 'none' : 'block';
            header.querySelector('.arrow').textContent = content.style.display === 'block' ? '▲' : '▼';
            header.querySelector('.arrow').classList.toggle('open');
        });

        progressStages.appendChild(accordion);
    });
}

// --- 6. Отображение результата
function displayResponse(data) {
    if (!data || !Array.isArray(data) || data.length === 0) {
        responseContainer.innerHTML = `<div class="sat-card sat-card-row text-yellow-200">Нет подходящих аппаратов по вашему запросу.</div>`;
        return;
    }

    responseContainer.innerHTML = data.map(item => `
        <div class="sat-card">
            <div class="sat-card-title">TLE: <pre class="font-mono">${item.tle ? item.tle.replace(/\n/g, '<br>') : 'N/A'}</pre></div>
            <div class="sat-card-row"><strong>Покрытие:</strong> ${item.modelSat?.coverage || 'N/A'}</div>
            <div class="sat-card-row"><strong>Тип орбиты:</strong> ${item.modelSat?.orbitType || 'N/A'}</div>
            <div class="sat-card-row"><strong>Высота:</strong> ${item.modelSat?.altitude || 'N/A'} км</div>
            <div class="sat-card-row"><strong>Масса:</strong> ${item.modelSat?.mass || 'N/A'} кг</div>
            <div class="sat-card-row"><strong>Форм-фактор:</strong> ${item.modelSat?.formFactor || 'N/A'}</div>
            <div class="sat-card-row"><strong>Статус:</strong> ${item.modelSat?.status ? 'Активный' : 'Неактивный'}</div>
            <div class="sat-card-date"><strong>Дата:</strong> ${item.modelSat?.date ? new Date(item.modelSat.date).toLocaleDateString("ru-RU") : 'N/A'}</div>
        </div>
    `).join('');
}

