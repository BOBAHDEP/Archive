package ru.zipper;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Class for work with zip archives
 */

public class ZipArchiver implements Archiver {

    /**
     * creates zip archive of given file
     *
     * @param zipArchiveName name of zip archive to be created
     * @param fileName       name of added file
     * @throws IOException
     */
    public void createZipArchiveWithFile(String zipArchiveName, String fileName) throws IOException {
        createZipArchiveWithFile(zipArchiveName, fileName, null);
    }

    /**
     * connects two streams and writes to zipOutputStream
     *
     * @param inputStream     file input stream
     * @param zipOutputStream zip out stream
     * @throws IOException
     */
    protected void writeFromFisToZos(FileInputStream inputStream, ZipOutputStream zipOutputStream) throws IOException {
        byte[] buf = new byte[8000];
        int length;
        while (true) {
            length = inputStream.read(buf);
            if (length < 0) break;
            zipOutputStream.write(buf, 0, length);
        }
    }

    /**
     * creates files or folders from given archive
     *
     * @param folderName     name folder where files are created
     * @param zipArchiveName name of zip archive
     * @throws IOException
     */
    @Override
    public void createFileFromArchive(String folderName, String zipArchiveName) throws IOException {
        System.out.println(folderName + " " + zipArchiveName);
        if (!new File(folderName).exists() && !new File(folderName).mkdirs()) {
            throw new IOException();
        }
        ZipFile zipFile = new ZipFile(zipArchiveName);
        Enumeration<? extends ZipEntry> entries;
        for (entries = zipFile.entries(); entries.hasMoreElements(); ) {
            ZipEntry zipEntry = entries.nextElement();
            if (zipEntry.isDirectory()) {
                File fileFolder = new File(folderName + "\\" + zipEntry.getName());
                if (!fileFolder.mkdirs()) {
                    throw new IOException();
                }
                continue;
            }
            InputStream fileInputStream = zipFile.getInputStream(zipEntry);
            FileOutputStream fileOutputStream = new FileOutputStream(folderName + "\\" + zipEntry.getName());

            writeFromZipToFile(fileOutputStream, fileInputStream);

            fileInputStream.close();
            fileOutputStream.close();
        }
    }

    /**
     * connects two streams and writes to outputStream
     *
     * @param outputStream   file to write to
     * @param zipInputStream zip stream to get data from
     * @throws IOException
     */
    protected void writeFromZipToFile(FileOutputStream outputStream, InputStream zipInputStream) throws IOException {
        byte[] buf = new byte[8000];
        int length;
        while (true) {
            length = zipInputStream.read(buf);
            if (length < 0) break;
            outputStream.write(buf, 0, length);
        }
    }

    /**
     * creates zip file which can contain folders
     *
     * @param zipArchiveName name of created zip archive
     * @param folderName     folder to be zipped
     * @throws IOException
     */
    @Override
    public void createZipArchiveWithFoldersPacked(String zipArchiveName, String folderName) throws IOException {
        // The folder path to be zipped
        File directoryToZip = new File(folderName);

        List<File> fileList = new ArrayList<File>();
        getAllFiles(directoryToZip, fileList);
        File zipFile = new File(zipArchiveName);
        if (!zipFile.getParentFile().mkdirs()) {
            throw new IOException();
        }
        writeZipFile(directoryToZip, fileList, zipFile);
    }

    /**
     * get all files in folder
     *
     * @param dir      direction containing files. Must be a directory
     * @param fileList list of files to be returned
     * @throws IOException
     */
    private void getAllFiles(File dir, List<File> fileList) throws IOException {

        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            fileList.add(file);
            if (file.isDirectory()) {
                getAllFiles(file, fileList);
            }
        }

    }

    /**
     * creates zip file with given files
     *
     * @param directoryToZip directory to put zip file
     * @param fileList       files to be zipped
     */
    private void writeZipFile(File directoryToZip, List<File> fileList, File zipFile) throws IOException {

        FileOutputStream fos = new FileOutputStream(zipFile);
        ZipOutputStream zos = new ZipOutputStream(fos);

        for (File file : fileList) {
            if (!file.isDirectory()) { // we only zip files, not directories
                addToZip(directoryToZip, file, zos);
            }
        }

        zos.close();
        fos.close();
    }

    /**
     * adds file to ZipOutputStream
     *
     * @param directoryToZip directory of zip file
     * @param file           file to be added
     * @param zos            ZipOutputStream to write to
     * @throws IOException
     */
    private void addToZip(File directoryToZip, File file, ZipOutputStream zos) throws IOException {

        FileInputStream fis = new FileInputStream(file);
        //relative path that is relative to the directory being zipped
        String zipFilePath = file.getCanonicalPath().substring(directoryToZip.getCanonicalPath().length() + 1, file.getCanonicalPath().length());
        ZipEntry zipEntry = new ZipEntry(zipFilePath);
        zos.putNextEntry(zipEntry);

        writeFromFisToZos(fis, zos);

        zos.closeEntry();
        fis.close();
    }

    /**
     * adds file to existing archive which contains only files
     *
     * @param zipFilePath       previous zip file
     * @param fileName          file to be added
     * @param newZipFilePath    new zip with file added
     */
    @Override
    public void addFileToArchive(String zipFilePath, String fileName, String newZipFilePath) throws IOException {
        if (!new File(newZipFilePath).getParentFile().exists() && !new File(newZipFilePath).getParentFile().mkdirs()) {
            throw new IOException();
        }
        ZipFile zipFile = new ZipFile(zipFilePath);
        Enumeration<? extends ZipEntry> entries;
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(newZipFilePath));    //write to
        for (entries = zipFile.entries(); entries.hasMoreElements(); ) {
            ZipEntry zipEntry = entries.nextElement();
            InputStream fileInputStream = zipFile.getInputStream(zipEntry);                     //read from


            ZipEntry ze = new ZipEntry(zipEntry.getName());
            zos.putNextEntry(ze);

            writeFromZipToZip(zos, fileInputStream);

            fileInputStream.close();

        }
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        ZipEntry ze = new ZipEntry(file.getName());
        zos.putNextEntry(ze);

        writeFromFisToZos(fis, zos);
        zos.close();
        fis.close();
    }

    /**
     * connects two streams and writes to outputStream
     *
     * @param outputStream   file to write to
     * @param zipInputStream zip stream to get data from
     * @throws IOException
     */
    protected void writeFromZipToZip(ZipOutputStream outputStream, InputStream zipInputStream) throws IOException {
        byte[] buf = new byte[8000];
        int length;
        while (true) {
            length = zipInputStream.read(buf);
            if (length < 0) break;
            outputStream.write(buf, 0, length);
        }
    }

    /**
     * gets comment of archive and prints them to console
     *
     * @param zipArchiveName zip file
     * @throws IOException
     */
    @Override
    public void getComment(String zipArchiveName) throws IOException {
        ZipFile zipFile = new ZipFile(zipArchiveName);
        if (zipFile.getComment() != null) {
            System.out.println(zipFile.getName() + "comment: " + zipFile.getComment());
        }
    }

    /**
     * creates zip archive of given file with comment
     *
     * @param zipArchiveName name of zip archive to be created
     * @param fileName       name of added file
     * @param comment        name of comment to archive
     * @throws IOException
     */
    @Override
    public void createZipArchiveWithFile(String zipArchiveName, String fileName, String comment) throws IOException {
        File zipArchive = new File(zipArchiveName).getParentFile();
        if (!zipArchive.exists()) {
            if (!zipArchive.mkdirs()) {
                throw new IOException();
            }
        }
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipArchiveName));
        if (comment != null) {
            zos.setComment(comment);
        }
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        ZipEntry ze = new ZipEntry(file.getName());
        zos.putNextEntry(ze);

        writeFromFisToZos(fis, zos);

        fis.close();
        zos.closeEntry();
        zos.close();
    }

    /**
     * add comment to existing zip file. Creates new file with comment named
     * as previous zip file + "$$"; Deletes previous one; Renames new one
     *
     * @param zipFilePath file to which comment is added
     * @param comment     comment
     * @throws IOException
     */
    @Override
    public void addCommentToArchive(String zipFilePath, String comment) throws IOException {
        ZipFile zipFile = new ZipFile(zipFilePath);
        Enumeration<? extends ZipEntry> entries;
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath + "$$"));    //write to
        zos.setComment(comment);
        for (entries = zipFile.entries(); entries.hasMoreElements(); ) {
            ZipEntry zipEntry = entries.nextElement();
            InputStream fileInputStream = zipFile.getInputStream(zipEntry);                     //read from


            ZipEntry ze = new ZipEntry(zipEntry.getName());
            zos.putNextEntry(ze);

            writeFromZipToZip(zos, fileInputStream);

            fileInputStream.close();
        }
        zipFile.close();
        zos.close();
        File zipFileFile = new File(zipFilePath);
        if (!zipFileFile.delete()) {
            throw new IOException();
        }
        File newZipFile = new File(zipFilePath + "$$");
        if (!newZipFile.renameTo(new File(zipFilePath))) {
            throw new IOException();
        }
    }
}

