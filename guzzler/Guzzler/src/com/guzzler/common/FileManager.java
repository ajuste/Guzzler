/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.common;

import android.os.Environment;
import android.util.Log;
import com.guzzler.common.exception.FileAccessException;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author ajuste
 */
public class FileManager {

    private static FileManager instance;
    private boolean useExternalStorage;
    private String applicationRootName;
    private File systemRoot;
    private File applicationRoot;

    private FileManager(boolean userExternalStorage, String rootDirectory) {
        this.useExternalStorage = userExternalStorage;
        this.applicationRootName = rootDirectory;
    }

    /**
     * Initializes the class. Don't use it.
     *
     * @param useExternalStorage True to use external storage.
     * @param rootDirectory Root directory name for the application.
     * @throws FileAccessException If any error ocurred.
     */
    public static void initialize(boolean useExternalStorage, String rootDirectory) throws FileAccessException {
        FileManager.instance = new FileManager(useExternalStorage, rootDirectory);
        FileManager.instance.ensureAppRootDirectory();
    }

    public static FileManager getInstance() {
        return FileManager.instance;
    }

    public boolean checkAccessToExternalStoreage() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public File ensureFileOnDirectory(File directory, String filename) throws FileAccessException {
        File file = new File(directory, filename);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new FileAccessException("Unable to ensure file on directory");
                }
            } catch (IOException ex) {
                throw new FileAccessException("Unable to ensure file on directory", ex);
            }
        }
        return directory;
    }

    /**
     * Ensure the existence of a directory inside the application root.
     *
     * @param directoryName The directory name to ensure.
     * @return The ensured directory.
     */
    public File ensureDirectoryOnAppRoot(String directoryName) throws FileAccessException {
        File directory = new File(this.applicationRoot, directoryName);

        if (!directory.exists()) {
            if (!directory.mkdir()) {
                throw new FileAccessException("Unable to ensure directory on root");
            }
        }
        return directory;
    }

    /**
     * Ensure the application root by creating if does not exists.
     *
     * @throws FileAccessException Thrown if directory was not created.
     */
    private void ensureAppRootDirectory() throws FileAccessException {
        File appRoot;
        try {
            //  Get root directory (external or internal cell memory)
            if (this.useExternalStorage) {
                if (this.checkAccessToExternalStoreage()) {
                    this.systemRoot = Environment.getExternalStorageDirectory();
                } else {
                    throw new FileAccessException("Unable to access external storage");
                }
            } else {
                this.systemRoot = Environment.getRootDirectory();
            }
            //  Ensure application root.
            appRoot = new File(this.systemRoot, this.applicationRootName);
            if (!appRoot.exists()) {
                if (!appRoot.mkdir()) {
                    throw new FileAccessException("Unable to create root directory: " + this.applicationRootName);
                }
            }
            //  Finally assign for later use.
            this.applicationRoot = appRoot;
        } catch (Exception ex) {
            Log.e(FileManager.class.getSimpleName(), "Failed to ensure application root directory", ex);
            throw new FileAccessException(ex);
        }
    }
}