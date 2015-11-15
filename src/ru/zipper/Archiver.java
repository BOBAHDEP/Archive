package ru.zipper;

import java.io.IOException;

/**
 * Interface for operations with archives
 */
public interface Archiver {

    /**
     * method creates new zip archive with files
     *
     * @param zipArchiveName name of created zip archive
     * @param fileName       name of added file or folder
     * @throws IOException
     */
    public void createZipArchiveWithFile(String zipArchiveName, String fileName)
            throws IOException;

    /**
     * method creates files from zip archive
     *
     * @param folderName     name folder where files are created
     * @param zipArchiveName name of zip archive
     * @throws IOException
     */
    public void createFileFromArchive(String folderName, String zipArchiveName)
            throws IOException;

    /**
     * method creates new zip archive with folder which may also contain folders
     *
     * @param fileName       name of added folder
     * @param zipArchiveName name of created zip archive
     * @throws IOException
     */
    public void createZipArchiveWithFoldersPacked(String zipArchiveName, String fileName)
            throws IOException;

    /**
     * gets comment of archive and prints them to console
     *
     * @param zipFile zip file
     * @throws IOException
     */
    public void getComment(String zipFile) throws IOException;

    /**
     * method creates new zip archive with files and comment
     *
     * @param zipArchiveName name of created zip archive
     * @param fileName       name of added file or folder
     * @param comment        name of comment
     * @throws IOException
     */
    public void createZipArchiveWithFile(String zipArchiveName, String fileName, String comment)
            throws IOException;

    /**
     * adds file to existing archive which contains only files
     *
     * @param zipFilePath    previous archive
     * @param file           file to add to archive
     * @param newZipFilePath new archive with added file
     */
    public void addFileToArchive(String zipFilePath, String file, String newZipFilePath) throws IOException;

    /**
     * add comment to existing zip file
     *
     * @param zipFilePath file to which comment is added
     * @param comment     comment
     * @throws IOException
     */
    public void addCommentToArchive(String zipFilePath, String comment) throws IOException;
}
