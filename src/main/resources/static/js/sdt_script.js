document.addEventListener('DOMContentLoaded', () => {
    // --- 0. AUTH & INIT ---
    const userRole = document.body.dataset.userRole;
    if (!userRole) { window.location.href = '/login'; return; }

    // --- 1. DATABASE (полная версия) ---
    const database = {
        ka: {
            title: "Космический аппарат",
            systemConfigsFile: "twBase.json",
            elementTypes: { "acos": { label: "СУД (Конфигурации)", file: "acos_system_configs.json" }, "eps": { label: "СЭП (Конфигурации)", file: "eps_system_configs.json" }, "fsw": { label: "БПО (Конфигурации)", file: "fsw_system_configs.json" }, "orbit": { label: "Орбита", file: "orbit.json" } },
            files: { "twBase.json": { "sc_main": { "label": "Главная конфигурация КА", "acos_config": "sc2", "eps_config": "eps_config_smallsat", "fsw_config": "fsw_config_full_ops", "orbit": "LEO_500" }, "sc_test": { "label": "Тестовая конфигурация КА", "acos_config": "sc3", "eps_config": "eps_config_cubesat", "fsw_config": "fsw_config_minimal", "orbit": "SSO_800" } }, "acos_system_configs.json": { "sc2": { "Label": "sc2", "Moments of Inertia": "10.0  20.0  30.0" }, "sc3": { "Label": "sc3", "Moments of Inertia": "10.0  20.0  30.0" } }, "eps_system_configs.json": { "eps_config_cubesat": { "label": "Конфигурация для CubeSat" }, "eps_config_smallsat": { "label": "Конфигурация для малого КА" } }, "fsw_system_configs.json": { "fsw_config_minimal": { "label": "Минимальная конфигурация БПО" }, "fsw_config_full_ops": { "label": "Полнофункциональная конфигурация БПО" } }, "orbit.json": { "LEO_500": { "Type": "LEO", "Altitude_km": 500 }, "SSO_800": { "Type": "SSO", "Altitude_km": 800 } } },
            permissions: { editSystem: userRole === 'main_designer', editElements: false }
        },
        sud: {
            title: "Система упр. движением",
            systemConfigsFile: "acos_system_configs.json",
            elementTypes: { "wheels": { label: "Маховики", file: "wheels.json" }, "starsenses": { label: "Звездные датчики", file: "starsenses.json" } },
            files: { "acos_system_configs.json": { "sc2": { "Label": "sc2", "Moments of Inertia": "10.0  20.0  30.0", "Products of Inertia": "0.0  0.0  0.0", "Geometry Input File": "IonCruiser.obj", "Number of wheels": { "blocks": [{ "name": "example1", "n": 4 }], "orientation": [ { "mainAxis": "Z", "mainAxizInBody": { "x": 1, "y": 0, "z": 0 }, "secnAxizInBody": { "x": 0, "y": 0, "z": -1 } }, { "mainAxis": "Z", "mainAxizInBody": { "x": 0, "y": 1, "z": 0 }, "secnAxizInBody": { "x": 1, "y": 0, "z": 0 } }, { "mainAxis": "Z", "mainAxizInBody": { "x": 0, "y": 0, "z": 1 }, "secnAxizInBody": { "x": 1, "y": 0, "z": 0 } }, { "mainAxis": "Z", "mainAxizInBody": { "x": 0, "y": 0, "z": 1 }, "secnAxizInBody": { "x": 1, "y": 0, "z": 0 } } ] }, "Number of Star Trackers": { "blocks": [{ "name": "ST1", "n": 1 }], "orientation": [{ "mainAxis": "Z", "mainAxizInBody": { "x": 1, "y": 0, "z": 0 }, "secnAxizInBody": { "x": 0, "y": 0, "z": -1 } }] } }, "sc3": { "Label": "sc3", "Moments of Inertia": "10.0  20.0  30.0", "Products of Inertia": "0.0  0.0  0.0", "Geometry Input File": "IonCruiser.obj", "Number of wheels": { "blocks": [{ "name": "example1", "n": 3 }], "orientation": [ { "mainAxis": "Z", "mainAxizInBody": { "x": 1, "y": 0, "z": 0 }, "secnAxizInBody": { "x": 0, "y": 0, "z": -1 } }, { "mainAxis": "Z", "mainAxizInBody": { "x": 0, "y": 1, "z": 0 }, "secnAxizInBody": { "x": 1, "y": 0, "z": 0 } }, { "mainAxis": "Z", "mainAxizInBody": { "x": 0, "y": 0, "z": 1 }, "secnAxizInBody": { "x": 1, "y": 0, "z": 0 } } ] }, "Number of Star Trackers": { "blocks": [{ "name": "ST1", "n": 1 }], "orientation": [{ "mainAxis": "Z", "mainAxizInBody": { "x": 1, "y": 0, "z": 0 }, "secnAxizInBody": { "x": 0, "y": 0, "z": -1 } }] } } }, "wheels.json": { "template": { "Torque (N-m), Momentum (N-m-sec)": "0.14    50.0", "Inertia": 0.025, "File": "NONE" }, "example1": { "Torque (N-m), Momentum (N-m-sec)": "0.14   50.0", "Inertia": 0.05, "File": "NONE" } }, "starsenses.json": { "template": { "Sample": 0.2, "Boresight": "Z" }, "ST1": { "Sample": 0.25, "Boresight": "Z" } } },
            permissions: { editSystem: userRole === 'acs_engineer', editElements: userRole === 'acs_engineer' }
        },
        sep: {
            title: "Система электропитания",
            systemConfigsFile: "eps_system_configs.json",
            elementTypes: { "solarpanels": { label: "Солнечные панели", file: "solarpanels.json" }, "batteries": { label: "АКБ", file: "batteries.json" }, "pcu": { label: "Блоки упр. питанием", file: "pcu.json" } },
            files: { "eps_system_configs.json": { "eps_config_cubesat": { "Label": "Конфигурация для CubeSat" }, "eps_config_smallsat": { "Label": "Конфигурация для малого КА" } }, "solarpanels.json": { "template": { "Type": "GaAs" }, "sp_small_fixed": { "Type": "GaAs" } }, "batteries.json": { "template": { "Type": "Li-Ion" }, "bat_li_ion_100wh": { "Type": "Li-Ion" } }, "pcu.json": { "template": { "Input_Voltage_Range_V": "24-32" } } },
            permissions: { editSystem: userRole === 'eps_engineer', editElements: userRole === 'eps_engineer' }
        },
        bpo: {
            title: "Бортовое ПО",
            systemConfigsFile: "fsw_system_configs.json",
            elementTypes: { "os": { label: "ОС", file: "os.json" }, "app_modules": { label: "Прикладные модули", file: "app_modules.json" } },
            files: { "fsw_system_configs.json": { "fsw_config_minimal": { "Label": "Минимальная конфигурация" }, "fsw_config_full_ops": { "Label": "Полнофункциональная конфигурация" } }, "os.json": { "freertos_10": { "Name": "FreeRTOS" }, "rtems_5": { "Name": "RTEMS" } }, "app_modules.json": { "mod_telemetry_basic": { "Description": "Базовый модуль ТМ" } } },
            permissions: { editSystem: userRole === 'software_programmer', editElements: userRole === 'software_programmer' }
        },
        ballistics: {
            title: "Баллистика",
            systemConfigsFile: "missions.json",
            elementTypes: { "initial_state": { label: "Начальные состояния", file: "initial_state.json" }, "propagators": { label: "Пропагаторы", file: "propagators.json" } },
            files: { "missions.json": { "mission_leo_observation": { "Label": "Миссия наблюдения на НОО" } }, "initial_state.json": { "tle_iss": { "type": "TLE" } }, "propagators.json": { "sgp4": { "name": "SGP4" } } },
            permissions: { editSystem: userRole === 'ballistics', editElements: userRole === 'ballistics' }
        }
    };
    const roleNames = { main_designer: "Главный конструктор", acs_engineer: "Инженер СУД", eps_engineer: "Инженер СЭП", software_programmer: "Программист БПО", ballistics: "Баллистик" };

    // --- 2. DOM & STATE ---
    const elements = {
        nav: document.getElementById('tab-navigation'),
        userRoleDisplay: document.getElementById('user-role-display'),
        systemTitle: document.getElementById('system-title'),
        configDropdown: document.getElementById('config-dropdown'),
        calculateBtn: document.getElementById('calculate-btn'),
        elementTree: document.getElementById('element-tree'),
        editorTitle: document.getElementById('editor-title'),
        jsonEditorWrapper: document.getElementById('json-editor-wrapper'),
        editJsonBtn: document.getElementById('edit-json-btn'),
        saveJsonBtn: document.getElementById('save-json-btn'),
        cancelJsonBtn: document.getElementById('cancel-json-btn'),
        resultsContent: document.getElementById('results-content')
    };

    let appState = {
        activeTab: 'ka', activeEditorFile: null, activeInstanceKey: null, activeTreeNode: null,
        isBaseMode: true, isEditingJson: false
    };

    // --- 3. CORE LOGIC ---
    function switchTab(tabId) {
        appState.activeTab = tabId;

        elements.nav.querySelectorAll('.nav-item').forEach(item => {
            item.classList.toggle('active', item.dataset.tab === tabId);
        });

        const sys = database[tabId];
        if (!sys) return;

        elements.systemTitle.textContent = sys.title;
        populateConfigDropdown();
        buildElementTree();

        // НОВОЕ: По умолчанию показываем главный файл конфигурации системы
        resetEditorState(true);
        selectNode(null); // Сбрасываем выбор в дереве
        showFileJson(sys.systemConfigsFile, sys.permissions.editSystem);

        elements.configDropdown.value = 'base';
        onConfigChange();
    }

    function displayInEditor(data, canEdit, title) {
        elements.jsonEditorWrapper.innerHTML = '';
        elements.editorTitle.textContent = title;
        const canEnterEditMode = canEdit && appState.isBaseMode;

        elements.editJsonBtn.classList.toggle('hidden', !canEnterEditMode || appState.isEditingJson);
        elements.saveJsonBtn.classList.toggle('hidden', !appState.isEditingJson);
        elements.cancelJsonBtn.classList.toggle('hidden', !appState.isEditingJson);

        if (appState.isEditingJson && canEnterEditMode) {
            const textarea = document.createElement('textarea');
            textarea.value = JSON.stringify(data, null, 2);
            textarea.spellcheck = false;
            elements.jsonEditorWrapper.appendChild(textarea);
            textarea.focus();
        } else {
            const pre = document.createElement('pre');
            pre.textContent = JSON.stringify(data, null, 2);
            elements.jsonEditorWrapper.appendChild(pre);
        }
    }

    function showFileJson(fileKey, canEdit) {
        resetEditorState(false);
        appState.activeEditorFile = fileKey;
        appState.activeInstanceKey = null;
        displayInEditor(database[appState.activeTab].files[fileKey], canEdit, fileKey);
    }

    function showInstanceJson(fileKey, instanceKey, canEdit) {
        resetEditorState(false);
        appState.activeEditorFile = fileKey;
        appState.activeInstanceKey = instanceKey;
        displayInEditor(database[appState.activeTab].files[fileKey][instanceKey], canEdit, `${instanceKey} (${fileKey})`);
    }

    function resetEditorState(fullReset) {
        if (fullReset) {
            selectNode(null);
            appState.activeEditorFile = null;
            appState.activeInstanceKey = null;
            elements.jsonEditorWrapper.innerHTML = '<div class="placeholder-text">Выберите элемент слева.</div>';
            elements.editorTitle.textContent = 'Описание';
        }
        appState.isEditingJson = false;
        elements.editJsonBtn.classList.add('hidden');
        elements.saveJsonBtn.classList.add('hidden');
        elements.cancelJsonBtn.classList.add('hidden');
    }

    // --- TREE & UI ---
    function buildElementTree() {
        elements.elementTree.innerHTML = '';
        const sys = database[appState.activeTab];

        // Убрали узел "Описание системы". Теперь дерево содержит только типы элементов.
        for (const typeKey in sys.elementTypes) {
            const typeInfo = sys.elementTypes[typeKey];
            const instances = sys.files[typeInfo.file];
            const hasChildren = instances && Object.keys(instances).length > 0;

            const typeNode = createNode(typeKey, typeInfo.label, hasChildren, 'type');

            typeNode.labelSpan.addEventListener('click', (e) => {
                e.stopPropagation(); selectNode(typeNode.li);
                showFileJson(typeInfo.file, sys.permissions.editElements);
            });

            if (hasChildren) {
                const ul = document.createElement('ul'); ul.className = 'nested';
                for (const instKey in instances) {
                    const instNode = createNode(instKey, instKey, false, 'instance');
                    instNode.nodeContent.addEventListener('click', (e) => {
                        e.stopPropagation(); selectNode(instNode.li);
                        showInstanceJson(typeInfo.file, instKey, sys.permissions.editElements);
                    });
                    ul.appendChild(instNode.li);
                }
                typeNode.li.appendChild(ul);
            }
            elements.elementTree.appendChild(typeNode.li);
        }
    }

    function createNode(id, labelText, hasChildren, type) {
        const li = document.createElement('li');
        li.className = `tree-node ${hasChildren ? 'has-children' : ''} ${type === 'instance' ? 'instance-node' : ''}`;
        li.dataset.id = id;
        const nodeContent = document.createElement('div');
        nodeContent.className = 'node-content';
        const arrow = document.createElement('span');
        arrow.className = 'tree-arrow';
        const labelSpan = document.createElement('span');
        labelSpan.className = 'node-label';
        labelSpan.textContent = labelText;
        labelSpan.title = labelText;
        nodeContent.appendChild(arrow);
        nodeContent.appendChild(labelSpan);
        li.appendChild(nodeContent);
        if (hasChildren) {
            arrow.addEventListener('click', (e) => {
                e.stopPropagation();
                li.classList.toggle('expanded');
            });
        }
        return { li, nodeContent, arrow, labelSpan };
    }

    function selectNode(li) {
        if(appState.activeTreeNode) appState.activeTreeNode.classList.remove('active');
        appState.activeTreeNode = li;
        if (li) appState.activeTreeNode.classList.add('active');
    }

    function populateConfigDropdown() {
        const sys = database[appState.activeTab];
        elements.configDropdown.innerHTML = '<option value="base">Базовая конфигурация (редактирование)</option>';
        const configs = sys.files[sys.systemConfigsFile] || {};
        for (const id in configs) {
            const opt = document.createElement('option');
            opt.value = id;
            opt.textContent = `Просмотр: ${configs[id].Label || configs[id].label || id}`;
            elements.configDropdown.appendChild(opt);
        }
    }

    function onConfigChange() {
        appState.isBaseMode = elements.configDropdown.value === 'base';
        elements.calculateBtn.disabled = appState.isBaseMode;
        appState.isEditingJson = false;

        // Перерисовываем текущий открытый файл с новыми правами
        if (appState.activeEditorFile) {
            const sys = database[appState.activeTab];
            const isSystemFile = appState.activeEditorFile === sys.systemConfigsFile;
            const canEdit = isSystemFile ? sys.permissions.editSystem : sys.permissions.editElements;

            if (appState.activeInstanceKey) {
                showInstanceJson(appState.activeEditorFile, appState.activeInstanceKey, canEdit);
            } else {
                showFileJson(appState.activeEditorFile, canEdit);
            }
        }

        if (appState.isBaseMode) {
            elements.resultsContent.innerHTML = '<div class="placeholder-text">Выберите конфигурацию для расчета.</div>';
        } else {
            elements.resultsContent.innerHTML = '<div class="placeholder-text">Нажмите "Рассчитать".</div>';
        }
    }

    // --- EVENT LISTENERS ---
    elements.nav.addEventListener('click', (e) => {
        e.preventDefault();
        const navItem = e.target.closest('.nav-item');
        if (navItem && !navItem.classList.contains('active')) switchTab(navItem.dataset.tab);
    });

    elements.configDropdown.addEventListener('change', onConfigChange);

    elements.editJsonBtn.addEventListener('click', () => {
        if (!appState.activeEditorFile) return;
        appState.isEditingJson = true;
        onConfigChange(); // Перерисовываем с текущими данными, но в режиме редактирования
    });

    elements.cancelJsonBtn.addEventListener('click', () => {
        appState.isEditingJson = false;
        onConfigChange(); // Перерисовываем с исходными данными
    });

    elements.saveJsonBtn.addEventListener('click', () => {
        if (!appState.activeEditorFile || !appState.isBaseMode || !appState.isEditingJson) return;
        const ta = elements.jsonEditorWrapper.querySelector('textarea');
        if (!ta) return;
        try {
            const updatedData = JSON.parse(ta.value);
            const sys = database[appState.activeTab];
            if (appState.activeInstanceKey) {
                sys.files[appState.activeEditorFile][appState.activeInstanceKey] = updatedData;
            } else {
                sys.files[appState.activeEditorFile] = updatedData;
                if (appState.activeEditorFile === sys.systemConfigsFile) {
                    populateConfigDropdown();
                    elements.configDropdown.value = 'base';
                }
                buildElementTree(); // Перестроить дерево на случай изменения ключей
            }
            appState.isEditingJson = false;
            onConfigChange();
        } catch (e) { alert("Ошибка JSON: " + e.message); }
    });

    elements.calculateBtn.addEventListener('click', () => {
        if (appState.isBaseMode) return;
        elements.resultsContent.innerHTML = `
            <div class="result-card">
                <div class="result-line"><span class="result-label">Конфигурация</span><span class="result-value">${elements.configDropdown.options[elements.configDropdown.selectedIndex].text.replace('Просмотр: ', '')}</span></div>
                <div class="result-line"><span class="result-label">Полная масса</span><span class="result-value">${(Math.random() * 50 + 100).toFixed(2)} кг</span></div>
                <div class="result-line"><span class="result-label">Пиковое потребление</span><span class="result-value">${(Math.random() * 100 + 200).toFixed(1)} Вт</span></div>
            </div>
            <div class="result-card">
                <div class="result-line"><span class="result-label">Точность ориентации</span><span class="result-value">${(Math.random() * 0.05).toFixed(4)}°</span></div>
            </div>`;
    });

    // --- START ---
    elements.userRoleDisplay.textContent = `Роль: ${roleNames[userRole] || userRole}`;
    switchTab('ka');
});