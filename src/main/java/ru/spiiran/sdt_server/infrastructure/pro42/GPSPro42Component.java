package ru.spiiran.sdt_server.infrastructure.pro42;

import org.springframework.stereotype.Component;
import ru.spiiran.sdt_server.infrastructure.dto.DtoPro42Coordinate;
import ru.spiiran.sdt_server.infrastructure.file.pro42.FilePro42;
import ru.spiiran.sdt_server.infrastructure.file.pro42.Pro42DirectoryControl;
import ru.spiiran.sdt_server.infrastructure.client.pro42.RunPro42;

import java.io.IOException;
import java.util.List;

@Component("gpsPro42Component")
public class GPSPro42Component implements Pro42 {
    private final FilePro42 filePro42;
    private final RunPro42 runPro42;
    private final Pro42DirectoryControl pro42DirectoryControl;

    public GPSPro42Component(FilePro42 filePro42, RunPro42 runPro42, Pro42DirectoryControl pro42DirectoryControl) {
        this.filePro42 = filePro42;
        this.runPro42 = runPro42;
        this.pro42DirectoryControl = pro42DirectoryControl;
    }

    @Override
    public List<DtoPro42Coordinate> connectTo(Long satelliteId, String tle) throws IOException, InterruptedException {
        /*
         * Для работы Pro42:
         * 1) Найти templates в АРМ на каждые файлы
         * 2) Вставить в файлы данные моделирования
         * 3) Сохранить в АРМ
         * 4) Отчистить папку Ballistic от предыдущих результатов
         * 5) Перенести файлы из АРМ в Ballistic
         * !) Компилировать файл Pro42 в виртуальном окружении или прямо на сервере !)
         * 6) Запустить из run-directory
         * 7) Прочитать выходные файлы из Ballistic
         *
         */
        /// Инициализация директорий
        pro42DirectoryControl.init("ilyas");
        /// Создает директорию - если первый запуск моделирования
        pro42DirectoryControl.createDirectoriesIfNotExist();
        /// Отчищает диреторию - если не первый запуск моделирования
        pro42DirectoryControl.clearPro42Directory();
        /// Генерирует Файлы из templates с вставкой своих данных
        filePro42.genericPro42Files(satelliteId, tle);
        /// Перенос файлов из АРМ в Ballistic
        pro42DirectoryControl.moveFilesToBallistic();
        /// Инициализация директорий
        runPro42.init("ilyas");
        /// Запуск моделирования + чтение выходных файлов
        return runPro42.copyResponsePro42();
    }
}