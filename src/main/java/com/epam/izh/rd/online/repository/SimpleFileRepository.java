package com.epam.izh.rd.online.repository;

import java.io.*;
import java.net.URL;
import java.util.stream.Collectors;

public class SimpleFileRepository implements FileRepository {

    /**
     * Метод рекурсивно подсчитывает количество файлов в директории
     *
     * @param path путь до директори
     * @return файлов, в том числе скрытых
     */
    @Override
    public long countFilesInDirectory(String path) {
        URL resource = getClass().getClassLoader().getResource(path);
        File file = new File(resource.getFile());
        if (file.isFile()) {
            return 1;
        }

        long result = 0;
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            result += countFilesInDirectory(path + "/" + files[i].getName());
        }
        return result;
    }

    /**
     * Метод рекурсивно подсчитывает количество папок в директории, считая корень
     *
     * @param path путь до директории
     * @return число папок
     */
    @Override
    public long countDirsInDirectory(String path) {
        URL resource = getClass().getClassLoader().getResource(path);
        File file = new File(resource.getFile());

        if (file.isFile()) {
            return 0;
        }
        long result = 1;
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            result += countDirsInDirectory(path + "/" + files[i].getName());
        }
        return result;
    }

    /**
     * Метод копирует все файлы с расширением .txt
     *
     * @param from путь откуда
     * @param to   путь куда
     */
    @Override
    public void copyTXTFiles(String from, String to) {
        File sourceDir = new File(from);
        if (!sourceDir.exists()) {
            System.out.println("No such directory");
            ;
        }
        File[] files = sourceDir.listFiles((dir, name) -> name.endsWith(".txt"));
        for (File source : files) {
            if (source.isFile()) {
                File destination = new File(to + File.separator + source.getName());
                copyFile(source, destination);
            }
        }
    }

    private void copyFile(File source, File destination) {
        try (FileInputStream in = new FileInputStream(source);
             FileOutputStream out = new FileOutputStream(destination)) {
            while (in.available() > 0) {
                out.write(in.read());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод создает файл на диске с расширением txt
     *
     * @param path путь до нового файла
     * @param name имя файла
     * @return был ли создан файл
     */
    @Override
    public boolean createFile(String path, String name) {
        String targetFolder = "target/classes/";
        File dir = new File(targetFolder + path);
        dir.mkdir();
        File file = new File(dir.getPath() + File.separator + name);
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Метод считывает тело файла .txt из папки src/main/resources
     *
     * @param fileName имя файла
     * @return контент
     */
    @Override
    public String readFileFromResources(String fileName) {
        URL resource = getClass().getClassLoader().getResource(fileName);
        File file = new File(resource.getFile());
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            return in.lines().collect(Collectors.joining());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
