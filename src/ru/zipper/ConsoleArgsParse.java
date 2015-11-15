package ru.zipper;

import java.io.File;
import java.io.IOException;

/**
 * The ConsoleArgsParse class is auxiliary class
 * for console arguments parsing and functions performing depending on them
 */
public class ConsoleArgsParse {
    /**
     * Performs program depending on console arguments
     *
     * @param args given arguments of console
     */
    static public void performArchiver(String[] args) throws IOException {

        if (args.length == 0) {
            return;
        }

        if (args.length == 2 && args[0] != null && args[0].endsWith(".zip") && args[1] != null) {
            Archiver archiver = new ZipArchiver();
            if (!new File(args[1]).isDirectory()) {
                archiver.createZipArchiveWithFile(args[0], args[1]);
            } else {
                archiver.createZipArchiveWithFoldersPacked(args[0], args[1]);
            }
        } else if (args[0] != null && args[0].equals("-u") && args.length == 3 && args[1] != null && args[2] != null) {  //unzip file

            Archiver archiver = new ZipArchiver();
            archiver.createFileFromArchive(args[1], args[2]);

        } else if (args[0] != null && args[0].equals("-u") && args.length == 2 && args[1] != null) {    //unzip file into same folder

            Archiver archiver = new ZipArchiver();
            File archive = new File(args[1]);
            archiver.createFileFromArchive(archive.getParentFile().getAbsolutePath(), args[1]);

        } else if (args[0] != null && args[0].equals("-afa") && args.length == 4 && args[1] != null
                && args[2] != null && args[3] != null) {  //unzip file into same folder

            Archiver archiver = new ZipArchiver();
            archiver.addFileToArchive(args[1], args[2], args[3]);

        } else if (args[0] != null && args[0].equals("-wrc") && args.length == 2 && args[1] != null) {  //write comments

            Archiver archiver = new ZipArchiver();
            archiver.getComment(args[1]);

        } else if (args[0] != null && args[0].equals("-adc") && args.length == 3 && args[1] != null && args[2] != null) {  //add comment

            Archiver archiver = new ZipArchiver();
            archiver.addCommentToArchive(args[1], args[2]);

        } else if (args[0] != null && args[0].equals("-cac") && args.length == 4 && args[1] != null && args[2] != null
                && args[3] != null) {  //create file with comment

            Archiver archiver = new ZipArchiver();
            archiver.createZipArchiveWithFile(args[2], args[1], args[3]);

        }

    }
}
