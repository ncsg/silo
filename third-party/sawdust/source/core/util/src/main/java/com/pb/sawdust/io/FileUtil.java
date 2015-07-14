package com.pb.sawdust.io;

import com.pb.sawdust.util.SystemType;
import com.pb.sawdust.util.exceptions.RuntimeIOException;

import java.io.*;
import java.io.FileReader;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * The {@code FileUtil} class provides static convenience methods for dealing with files and directories. It mainly
 * wraps methods calls in {@code java.io.File}.
 *
 * @author crf <br/>
 *         Started: Dec 7, 2008 1:46:09 PM
 */
public class FileUtil {

    private FileUtil() {}

    /**
     * Get the line separator/terminator for the platform the JVM is running on.
     *
     * @return this platform's line terminator.
     */
    public static String getLineSeparator() {
        return System.getProperty("line.separator");
    }

    /**
     * Get the line separator for a specified file. This method checks only the first line of the file to make the
     * determination, and if only one line (sans a line terminator) exists in the file, then the result of
     * {@link #getLineSeparator()} is returned.
     *
     * @param file
     *        The file in question.
     *
     * @return the line separator used in the file, or the default line separator if no determination could be made.
     *
     * @throws IllegalArgumentException if {@code file} does not exist.
     */
    public static String getLineSeparator(File file) {
        if (!file.exists())
            throw new IllegalArgumentException("File does not exist: " + file);
        IterableFileReader.LineIterableReader reader =  IterableFileReader.getLineIterableFileWithLineTerminator(file);
        String line = reader.iterator().next();
        reader.close();
        for (SystemType.SystemFamily family : SystemType.SystemFamily.values())
            if (line.endsWith(family.getLineSeparator()))
                return family.getLineSeparator();
        return getLineSeparator();
    }

    /**
     * Get the line separator for a specified file. This method checks only the first line of the file to make the
     * determination, and if only one line (sans a line terminator) exists in the file, then the result of
     * {@link #getLineSeparator()} is returned.
     *
     * @param file
     *        The file in question.
     *
     * @return the line separator used in the file, or the default line separator if no determination could be made.
     *
     * @throws IllegalArgumentException if {@code file} does not exist.
     */
    public static String getLineSeparator(String file) {
        return getLineSeparator(new File(file));
    }

    /**
     * Delete a file.
     *
     * @param path
     *        The path specifying the file to delete.
     *
     * @return {@code true} if the file was deleted successfully, {@code false} otherwise.
     *
     * @throws IllegalArgumentException if {@code path} does not represent a file.
     */
    public static boolean deleteFile(String path) {
        File f = new File(path);
        if (f.isFile())
            return f.delete();
        else
            throw new IllegalArgumentException("Not a file: " + f);
    }

    /**
     * Empty a directory without deleting it. If the directory contains subdirectories, then those subdirectories
     * are recursively emptied, but not deleted.
     *
     * @param path
     *        The directory to empty.
     *
     * @return {@code true} if the directory was emptied successfully, {@code false} otherwise.
     *
     * @throws IllegalArgumentException if {@code path} does not represent a directory.
     */
    public static boolean emptyDir(String path) {
        return emptyDir(new File(path));
    }

    /**
     * Empty a directory without deleting it. If the directory contains subdirectories, then those subdirectories
     * are recursively emptied, but not deleted.
     *
     * @param dir
     *        The directory to empty.
     *
     * @return {@code true} if the directory was emptied successfully, {@code false} otherwise.
     *
     * @throws IllegalArgumentException if {@code dir} is not a directory.
     */
    public static boolean emptyDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) //some error - possibly deleted in interim
                for (String aChildren : children) {
                    File subDir = new File(dir, aChildren);
                    boolean success = subDir.isDirectory() ? emptyDir(subDir) : subDir.delete();
                    if (!success)
                        return success;
                }
        } else {
            throw new IllegalArgumentException("Not a directory: " + dir);
        }
        return true;
    }

    /**
     * Clear a directory without deleting it. If the directory contains subdirectories, then those subdirectories
     * are deleted.
     *
     * @param path
     *        The directory to clear.
     *
     * @return {@code true} if the directory was cleared successfully, {@code false} otherwise.
     *
     * @throws IllegalArgumentException if {@code path} does not represent a directory.
     */
    public static boolean clearDir(String path) {
        return clearDir(new File(path));
    }

    /**
     * Clear a directory without deleting it. If the directory contains subdirectories, then those subdirectories
     * are deleted.
     *
     * @param dir
     *        The directory to clear.
     *
     * @return {@code true} if the directory was cleared successfully, {@code false} otherwise.
     *
     * @throws IllegalArgumentException if {@code dir} is not a directory.
     */
    public static boolean clearDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) //some error - possibly deleted in interim
                for (String aChildren : children) {
                    File subDir = new File(dir, aChildren);
                    boolean success = subDir.isDirectory() ? deleteDir(subDir) : subDir.delete();
                    if (!success) {
                        return false;
                    }
                }
        } else {
            throw new IllegalArgumentException("Not a directory: " + dir);
        }
        return true;
    }

    /**
     * Delete the specified directory and all of its contents.
     *
     * @param path
     *        The path of the directory to delete.
     *
     * @return {@code true} if the directory was deleted successfully, {@code false} otherwise.
     *
     * @throws IllegalArgumentException if {@code path} does not represent a directory.
     */
    public static boolean deleteDir(String path) {
        return deleteDir(new File(path));
    }

    /**
     * Delete the specified directory and all of its contents.
     *
     * @param dir
     *        The path of the directory to delete.
     *
     * @return {@code true} if the directory was deleted successfully, {@code false} otherwise.
     *
     * @throws IllegalArgumentException if {@code dir} is not a directory.
     */
    public static boolean deleteDir(File dir) {
        return clearDir(dir) && dir.delete();
    }

    /**
     * Empty the specified directory when the JVM exits. If the directory contains subdirectories, then those subdirectories
     * are recursively emptied, but not deleted.
     *
     * @param path
     *        The path of the directory to empty.
     *
     * @throws IllegalArgumentException if {@code path} does not represent a directory.
     */
    public static void emptyDirOnExit(String path) {
        emptyDirOnExit(new File(path));
    }

    /**
     * Empty the specified directory when the JVM exits. If the directory contains subdirectories, then those subdirectories
     * are recursively emptied, but not deleted.
     *
     * @param dir
     *        The path of the directory to clear.
     *
     * @throws IllegalArgumentException if {@code dir} is not a directory.
     */
    public static void emptyDirOnExit(final File dir) {
        if (dir.isDirectory()) {  Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    emptyDirOnExit(dir);
                }
            });
        } else {
            throw new IllegalArgumentException("Not a directory: " + dir);
        }
    }

    /**
     * Clear the specified directory when the JVM exits. If the directory contains subdirectories, then those subdirectories
     * are deleted.
     *
     * @param path
     *        The path of the directory to clear.
     *
     * @throws IllegalArgumentException if {@code path} does not represent a directory.
     */
    public static void clearDirOnExit(String path) {
        clearDirOnExit(new File(path));
    }

    /**
     * Clear the specified directory when the JVM exits. If the directory contains subdirectories, then those subdirectories
     * are deleted.
     *
     * @param dir
     *        The path of the directory to clear.
     *
     * @throws IllegalArgumentException if {@code dir} is not a directory.
     */
    public static void clearDirOnExit(final File dir) {
        if (dir.isDirectory()) {  Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    clearDir(dir);
                }
            });
        } else {
            throw new IllegalArgumentException("Not a directory: " + dir);
        }
    }

    /**
     * Delete the specified directory and all of its contents when the JVM exits.
     *
     * @param path
     *        The path of the directory to delete.
     *
     * @throws IllegalArgumentException if {@code path} does not represent a directory.
     */
    public static void deleteDirOnExit(String path) {
        deleteDirOnExit(new File(path));
    }

    /**
     * Delete the specified directory and all of its contents when the JVM exits.
     *
     * @param dir
     *        The path of the directory to delete.
     *
     * @throws IllegalArgumentException if {@code dir} is not a directory.
     */
    public static void deleteDirOnExit(final File dir) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                deleteDir(dir);
            }
        });
    }

    /**
     * Copy a file.
     *
     * @param sourceFile
     *        The source file to copy.
     *
     * @param destFile
     *        The destination file.
     *
     * @param overwrite
     *        If {@code true}, {@code destFile} will be overwritten if it exists. If {@code false}, then {@code destFile}
     *        will not be overwritten and this method will return {@code false}.
     *
     * @return {@code true} if the file was copied successfully, {@code false} if not.
     *
     * @throws IllegalArgumentException if {@code sourceFile} does not exist.
     */
    public static boolean copyFile(File sourceFile, File destFile, boolean overwrite) {
        if (!sourceFile.exists())
            throw new IllegalArgumentException("Source file does not exist: " + sourceFile);
        FileChannel source = null;
        FileChannel dest = null;
        try {
            if (destFile.exists()) {
                if (!overwrite)
                    return false; //already exists, don't overwrite
            } else if (!destFile.exists()) {
                destFile.createNewFile();
            }
            source = new FileInputStream(sourceFile).getChannel();
            dest = new FileOutputStream(destFile).getChannel();
            dest.transferFrom(source,0,source.size());
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        } finally {
            if (source != null)
                try {
                    source.close();
                } catch (IOException e) {
                    //swallow
                }
            if (dest != null)
                try {
                    dest.close();
                } catch (IOException e) {
                    //swallow
                }
        }
        return true;
    }

    /**
     * Copy a file.
     *
     * @param sourceFile
     *        The source file to copy.
     *
     * @param destFile
     *        The destination file.
     *
     * @param overwrite
     *        If {@code true}, {@code destFile} will be overwritten if it exists. If {@code false}, then {@code destFile}
     *        will not be overwritten and this method will return {@code false}.
     *
     * @return {@code true} if the file was copied successfully, {@code false} if not.
     *
     * @throws IllegalArgumentException if {@code sourceFile} does not exist.
     */
    public static boolean copyFile(String sourceFile, String destFile, boolean overwrite) {
        return copyFile(new File(sourceFile),new File(destFile),overwrite);
    }

    /**
     * Copy a file, overwriting the destination file if necessary.
     *
     * @param sourceFile
     *        The source file to copy.
     *
     * @param destFile
     *        The destination file.
     *
     * @return {@code true} if the file was copied successfully, {@code false} if not.
     *
     * @throws IllegalArgumentException if {@code sourceFile} does not exist.
     */
    public static boolean copyFile(File sourceFile, File destFile) {
        return copyFile(sourceFile,destFile,true);
    }

    /**
     * Copy a file, overwriting the destination file if necessary.
     *
     * @param sourceFile
     *        The source file to copy.
     *
     * @param destFile
     *        The destination file.
     *
     * @return {@code true} if the file was copied successfully, {@code false} if not.
     *
     * @throws IllegalArgumentException if {@code sourceFile} does not exist.
     */
    public static boolean copyFile(String sourceFile, String destFile) {
        return copyFile(sourceFile,destFile,true);
    }

    /**
     * Copy an entire directory to a new location. Anything that does not pass through the provided file filter
     * will not be copied.  {@code sourceDir} will be copied to {@code new File(destDir,sourceDir.getName())}; that is,
     * the source directory is copied, not just its contents.
     *
     * @param sourceDir
     *        The source directory.
     *
     * @param destDir
     *        The destination directory.
     *
     * @param filter
     *        Filter to specify if a file/folder should be copied or not.
     *
     * @throws IllegalArgumentException if either {@code sourceDir} or {@code destDir} is not a directory or does not exist.
     */
    public static void copyDir(File sourceDir, File destDir, FileFilter filter) {
        if (!sourceDir.exists())
            throw new IllegalArgumentException("Source directory does not exist: " + sourceDir);
        if (!sourceDir.isDirectory())
            throw new IllegalArgumentException("Source directory is not a directory: " + sourceDir);
        if (!destDir.exists())
            throw new IllegalArgumentException("Destination directory does not exist: " + sourceDir);
        if (!destDir.isDirectory())
            throw new IllegalArgumentException("Destination directory is not a directory: " + sourceDir);
        File destination = new File(destDir,sourceDir.getName());
        if (filter.accept(sourceDir) && !destination.exists())
            destination.mkdir();
        for (File f : sourceDir.listFiles(filter))
            if (f.isDirectory())
                copyDir(f,destination,filter);
            else
                copyFile(f,destination);
    }

    /**
     * Copy an entire directory to a new location. Anything that does not pass through the provided file filter
     * will not be copied.  {@code sourceDir} will be copied to {@code new File(destDir,sourceDir.getName())}; that is,
     * the source directory is copied, not just its contents.
     *
     * @param sourceDir
     *        The source directory.
     *
     * @param destDir
     *        The destination directory.
     *
     * @param filter
     *        Filter to specify if a file/folder should be copied or not.
     *
     * @throws IllegalArgumentException if either {@code sourceDir} or {@code destDir} is not a directory or does not exist.
     */
    public static void copyDir(String sourceDir, String destDir, FileFilter filter) {
        copyDir(new File(sourceDir),new File(destDir),filter);
    }

    /**
     * Copy an entire directory to a new location. {@code sourceDir} will be copied to {@code new File(destDir,sourceDir.getName())};
     * that is, the source directory is copied, not just its contents.  Any previously existing files will be overwritten.
     *
     * @param sourceDir
     *        The source directory.
     *
     * @param destDir
     *        The destination directory.
     *
     * @throws IllegalArgumentException if either {@code sourceDir} or {@code destDir} is not a directory or does not exist.
     */
    public static void copyDir(File sourceDir, File destDir) {
        copyDir(sourceDir,destDir,getTransparentFileFilter());
    }

    /**
     * Copy an entire directory to a new location. {@code sourceDir} will be copied to {@code new File(destDir,sourceDir.getName())};
     * that is, the source directory is copied, not just its contents.  Any previously existing files will be overwritten.
     *
     * @param sourceDir
     *        The source directory.
     *
     * @param destDir
     *        The destination directory.
     *
     * @throws IllegalArgumentException if either {@code sourceDir} or {@code destDir} is not a directory or does not exist.
     */
    public static void copyDir(String sourceDir, String destDir) {
        copyDir(sourceDir,destDir,getTransparentFileFilter());
    }

    /**
     * Move a file or directory to a new location.  This is just a wrapper for {@code File.renameTo(File)}.  This method
     * does not allow a destination file/dir to be overwritten.
     *
     * @param originalPath
     *        The origin file.
     *
     * @param newPath
     *        The new file.
     *
     * @return {@code true} if the move was successful, {@code false} if not.
     *
     * @throws IllegalArgumentException if {@code originalPath} does not exist or {@code newPath} already exists.
     */
    public static boolean move(File originalPath, File newPath) {
        if (!originalPath.exists())
            throw new IllegalArgumentException("Origin path does not exist: " + originalPath);
        if (newPath.exists())
            throw new IllegalArgumentException("New path already exists: " + newPath);
        return originalPath.renameTo(newPath);
    }

    /**
     * Move a file or directory to a new location.  This method does not allow a destination file/dir to be overwritten.
     *
     * @param originalPath
     *        The origin file.
     *
     * @param newPath
     *        The new file.
     *
     * @return {@code true} if the move was successful, {@code false} if not.
     *
     * @throws IllegalArgumentException if {@code originalPath} does not exist or {@code newPath} already exists.
     */
    public static boolean move(String originalPath, String newPath) {
        return move(new File(originalPath),new File(newPath));
    }

    /**
     * Get a file filter from a filename filter.
     *
     * @param filter
     *        The input filter.
     *
     * @return a file filter wrapping {@code filter}.
     */
    public static FileFilter fileFilter(final FilenameFilter filter) {
        return new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return filter.accept(pathname.getParentFile(),pathname.getName());
            }
        };
    }

    /**
     * Get a "transparent" file filter which allows all files through.
     *
     * @return a file filter accepting all files.
     */
    public static FileFilter getTransparentFileFilter() {
        return new FileFilter() {
            public boolean accept(File pathname) {
                return true;
            }
        };

    }

    /**
     * Get a {@code BufferedReader} to use to read a specified file.
     *
     * @param file
     *        The file to use with the reader.
     *
     * @return a buffered reader which can be used to read {@code file}.
     *
     * @throws RuntimeIOException if {@code file} is not found.
     */
    public static BufferedReader openFile(File file) {
        try {
            return new BufferedReader(getFileStream(file,null));
        }
        catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    /**
     * Get a {@code BufferedReader} to use to read a specified file with a specific character encoding.
     *
     * @param file
     *        The file to use with the reader.
     *
     * @param charset
     *        The character set to use when reading this file.
     *
     * @return a buffered reader which can be used to read {@code file}.
     *
     * @throws RuntimeIOException if {@code file} is not found.
     */
    public static BufferedReader openFile(File file, Charset charset) {
        try {
            return new BufferedReader(getFileStream(file,charset));
        }
        catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    private static Reader getFileStream(File f, Charset charset) throws IOException {
        if (f.getPath().contains("!")) {
            InputStream s = ClassLoader.getSystemResourceAsStream(f.getName());
            return charset == null ? new InputStreamReader(s) : new InputStreamReader(s,charset);
        } else {
            return charset == null ? new FileReader(f) : new InputStreamReader(new FileInputStream(f),charset);
        }
    }

}