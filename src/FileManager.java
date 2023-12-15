import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.*;
import java.util.*;

/**
 * ����� ��� ���������� �������.
 */
public class FileManager {
    private static final Logger logger = LogManager.getLogger(FileManager.class);

    /**
     * ����� ����� � ����������.
     * @param args ��������� ��������� ������.
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
                    logger.error("������: ������������ �����.");
            }
        }

        scanner.close();
    }

    /**
     * �������� ����� ������������.
     * @param scanner ������ Scanner ��� ����� ������������.
     * @return ����� ������������.
     */
    private static int getUserChoice(Scanner scanner) {
        System.out.print("��� �� ������ �������? (1 - ���������� ����, 2 - ������� ������ ������, 3 - �����): ");
        try {
            return scanner.nextInt();
        }
        catch (InputMismatchException e) {
            scanner.nextLine(); // ������� ������ �������
            return -1;
        }
    }

    /**
     * �������� ���� �� ������ ����� � ������.
     * @param scanner ������ Scanner ��� ����� ������������.
     */
    private static void copyFile(Scanner scanner) {
        System.out.print("������� ������ ���� � ��������� ����� ��� �������� (��������, C:\\����\\�\\�����.txt): ");
        String sourceFilePath = scanner.next();

        System.out.print("������� ������ ���� � ������� ���������� ��� �������� (��������, C:\\����\\�\\����������): ");
        String targetDirectoryPath = scanner.next();

        try {
            File sourceFile = new File(sourceFilePath);

            if (!sourceFile.exists() || !sourceFile.isFile()) {
                logger.error("������: �������� ���� �� ����������.");
                return;
            }

            if (!isValidDirectory(targetDirectoryPath)) {
                return;
            }

            String targetFilePath = targetDirectoryPath + File.separator + sourceFile.getName();
            copyFileContents(sourceFile, targetFilePath);

        } catch (FileNotFoundException e) {
            logger.error("������: ������������ ���� ��� ������ � ������� ����������.");
        } catch (IOException e) {
            logger.error("������: �� ������� �������� ����.");
        }
    }

    /**
     * �������� ���������� ����� � ������ ����������.
     * @param sourceFile      �������� ����.
     * @param targetFilePath  ���� � �������� �����.
     * @throws IOException    ���� ��������� ������ �����/������.
     */
    private static void copyFileContents(File sourceFile, String targetFilePath) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(sourceFile);
             FileOutputStream outputStream = new FileOutputStream(targetFilePath)) {

            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            logger.info("���� ������� ���������� � " + targetFilePath);

        }
    }

    /**
     * ������� ������ ������ � ��������� ����������.
     * @param scanner ������ Scanner ��� ����� ������������.
     */
    private static void displayFileSize(Scanner scanner) {
        System.out.print("������� ������ ���� � ���������� ��� �������� (��������, C:\\����\\�\\����������): ");
        String directoryPath = scanner.next();

        if (!isValidDirectory(directoryPath)) {
            return;
        }

        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files == null || files.length == 0) {
            logger.error("������: � ���������� " + directoryPath + " ��� ������.");
            return;
        }

        logger.info("����� �������� ������ � ���������� " + directoryPath + ":");
        for (File file : files) {
            if (file.isFile()) {
                System.out.println(file.getName() + ": " + file.length() + " ����");
            }
        }
    }

    /**
     * ���������, �������� �� ��������� ���� ���������� �����������.
     * @param directoryPath ���� � ����������.
     * @return true, ���� ���������� ���������, � ��������� ������ false.
     */
    private static boolean isValidDirectory(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            logger.error("������: ��������� ���������� �� ����������.");
            return false;
        }
        return true;
    }
}
