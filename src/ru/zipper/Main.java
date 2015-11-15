package ru.zipper;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("use program with following parameters:\n" +
                    "1. To create zip from folder/file: C\\..\\archive.zip C\\..\\file\n" +
                    "2. To unzip file/folder from archive: -u C\\..\\folderWherePutFile C\\..\\archive.zip\n" +
                    "3. To unzip file/folder from archive into same folder: -u C\\..\\archive.zip\n" +
                    "4. To add file to archive: -afa C\\..\\oldArchive.zip C:\\..\\file.txt C\\..\\newArchive.zip\n" +
                    "5. To write comments of archive: -wrc C\\..\\archive.zip\n" +
                    "6. To add comment to archive: -adc C\\..\\archive.zip comment\n" +
                    "7. To create archive with comment: -cac C:\\..\\file.txt  C\\..\\archive.zip\n");
        }

        try {
        ConsoleArgsParse.performArchiver(args);
        } catch (IOException e) {
            System.out.println("Cannot read file. Exception: " + e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Cannot perform action.\n Exception: " + e.toString());
            System.out.println("use TestZipper archive_name file_name");
            e.printStackTrace();
        }
        System.out.println("Success!");

    }
}
