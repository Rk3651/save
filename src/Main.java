import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        // Создаем три экземпляра класса GameProgress
        GameProgress game1 = new GameProgress(100, 5, 3, 150.75);
        GameProgress game2 = new GameProgress(80, 3, 6, 250.50);
        GameProgress game3 = new GameProgress(120, 8, 9, 300.25);

        // Сохраняем сериализованные объекты GameProgress в папку savegames
        saveGame("D:/Games/savegames/save1.dat", game1);
        saveGame("D:/Games/savegames/save2.dat", game2);
        saveGame("D:/Games/savegames/save3.dat", game3);

        // Создаем архив с сохранениями
        zipFiles("D:/Games/savegames/zip.zip", List.of(
                "D:/Games/savegames/save1.dat",
                "D:/Games/savegames/save2.dat",
                "D:/Games/savegames/save3.dat"
        ));

        // Удаляем оригинальные файлы сохранений
        deleteFiles(List.of(
                "D:/Games/savegames/save1.dat",
                "D:/Games/savegames/save2.dat",
                "D:/Games/savegames/save3.dat"
        ));
    }

    public static void saveGame(String filePath, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
            System.out.println("Game progress saved: " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to save game progress: " + filePath);
            e.printStackTrace();
        }
    }

    public static void zipFiles(String zipFilePath, List<String> filesToZip) {
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            for (String filePath : filesToZip) {
                File file = new File(filePath);
                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(file);
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }

                    fis.close();
                    zos.closeEntry();
                    System.out.println("File zipped: " + filePath);
                } else {
                    System.out.println("File not found: " + filePath);
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to zip files: " + zipFilePath);
            e.printStackTrace();
        }
    }

    public static void deleteFiles(List<String> filePaths) {
        for (String filePath : filePaths) {
            File file = new File(filePath);
            if (file.delete()) {
                System.out.println("File deleted: " + filePath);
            } else {
                System.out.println("Failed to delete file: " + filePath);
            }
        }
    }
}