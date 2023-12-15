import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.*;
import java.util.*;

/**
 * Класс для управления файлами.
 */
public class FileManager {
    private static final Logger logger = LogManager.getLogger(FileManager.class);

    /**
     * Точка входа в приложение.
     * @param args Аргументы командной строки.
     */
    public static void main(String[] args) {
        System.setProperty("log4j.configurationFile", "C:\\Users\\Dimam\\IdeaProjects\\kyrcach\\log4j2.xml");
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        context.reconfigure();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            int choice = getUserChoice(scanner);

            if (choice == 3) {
                break;
            }

            switch (choice) {
                case 1:
                    copyFile(scanner);
                    break;
                case 2:
                    displayFileSize(scanner);
                    break;
                default:
                    logger.error("Ошибка: Некорректный выбор.");
            }
        }

        scanner.close();
    }

    /**
     * Получает выбор пользователя.
     * @param scanner Объект Scanner для ввода пользователя.
     * @return Выбор пользователя.
     */
    private static int getUserChoice(Scanner scanner) {
        System.out.print("Что вы хотите сделать? (1 - Копировать файл, 2 - Вывести список файлов, 3 - Выйти): ");
        try {
            return scanner.nextInt();
        }
        catch (InputMismatchException e) {
            scanner.nextLine(); // очистка буфера сканера
            return -1;
        }
    }

    /**
     * Копирует файл из одного места в другое.
     * @param scanner Объект Scanner для ввода пользователя.
     */
    private static void copyFile(Scanner scanner) {
        System.out.print("Введите полный путь к исходному файлу без пробелов (например, C:\\путь\\к\\файлу.txt): ");
        String sourceFilePath = scanner.next();

        System.out.print("Введите полный путь к целевой директории без пробелов (например, C:\\путь\\к\\директория): ");
        String targetDirectoryPath = scanner.next();

        try {
            File sourceFile = new File(sourceFilePath);

            if (!sourceFile.exists() || !sourceFile.isFile()) {
                logger.error("Ошибка: Исходный файл не существует.");
                return;
            }

            if (!isValidDirectory(targetDirectoryPath)) {
                return;
            }

            String targetFilePath = targetDirectoryPath + File.separator + sourceFile.getName();
            copyFileContents(sourceFile, targetFilePath);

        } catch (FileNotFoundException e) {
            logger.error("Ошибка: Недостаточно прав для записи в целевую директорию.");
        } catch (IOException e) {
            logger.error("Ошибка: Не удалось записать файл.");
        }
    }

    /**
     * Копирует содержимое файла в другую директорию.
     * @param sourceFile      Исходный файл.
     * @param targetFilePath  Путь к целевому файлу.
     * @throws IOException    Если произошла ошибка ввода/вывода.
     */
    private static void copyFileContents(File sourceFile, String targetFilePath) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(sourceFile);
             FileOutputStream outputStream = new FileOutputStream(targetFilePath)) {

            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            logger.info("Файл успешно скопирован в " + targetFilePath);

        }
    }

    /**
     * Выводит размер файлов в указанной директории.
     * @param scanner Объект Scanner для ввода пользователя.
     */
    private static void displayFileSize(Scanner scanner) {
        System.out.print("Введите полный путь к директории без пробелов (например, C:\\путь\\к\\директория): ");
        String directoryPath = scanner.next();

        if (!isValidDirectory(directoryPath)) {
            return;
        }

        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files == null || files.length == 0) {
            logger.error("Ошибка: В директории " + directoryPath + " нет файлов.");
            return;
        }

        logger.info("Вывод размеров файлов в директории " + directoryPath + ":");
        for (File file : files) {
            if (file.isFile()) {
                System.out.println(file.getName() + ": " + file.length() + " байт");
            }
        }
    }

    /**
     * Проверяет, является ли указанный путь допустимой директорией.
     * @param directoryPath Путь к директории.
     * @return true, если директория допустима, в противном случае false.
     */
    private static boolean isValidDirectory(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            logger.error("Ошибка: Указанной директории не существует.");
            return false;
        }
        return true;
    }
}
