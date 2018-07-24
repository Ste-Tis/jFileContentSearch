package com.sepherot.fcs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Functionality to walk through directories and read file content
 *
 * @author Stephan Tischer
 * @version     0.1.0
 * @since       2018-07-22
 */
public class DirWalker {
    private List<String> allowedExtensions;
    private boolean recursive;

    /**
     * Create new dir walker
     *
     * @param allowedExtensions List with all extensions to include in search, set to NULL to look at all files
     * @param recursive Also look into subdirectories
     */
    public DirWalker(List<String> allowedExtensions, boolean recursive) {
        this.allowedExtensions = cleanExtensions(allowedExtensions);
        this.recursive = recursive;
    }

    /**
     * Create new dir walker
     */
    public DirWalker() {
        this(null, true);
    }

    /**
     * Prepare all extensions for later useage
     *
     * @param extensions Extensions to prepare
     * @return List with prepared extensions
     */
    private static List<String> cleanExtensions(List<String> extensions) {
        if(extensions == null)
            return new ArrayList<>();

        List<String> cleaned = new ArrayList<>();
        for(String ext: extensions) {
            cleaned.add(ext.replace(".", "").toLowerCase());
        }
        return cleaned;
    }

    /**
     * Checks if a given file should be included in the search results, because of the extension
     *
     * @param file Name or full path of the file to check
     * @return Returns TRUE if file is allowed, otherwise FALSE
     */
    protected boolean hasAllowedExtension(String file) {
        if(getAllowedExtensions().isEmpty())
            return true;

        for(String extension: getAllowedExtensions()) {
            if(file.toLowerCase().endsWith("." + extension))
                return true;
        }
        return false;
    }

    public List<String> getAllowedExtensions() {
        return allowedExtensions;
    }

    public void setAllowedExtenstions(List<String> allowedExtensions) {
        this.allowedExtensions = cleanExtensions(allowedExtensions);
    }

    public boolean getRecursive() {
        return recursive;
    }

    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }

    /**
     * Lists all files which are saved under the given path
     *
     * @param path Path for directory to check
     * @return List with all files
     */
    public List<String> listFiles(String path) {
        List<String> files = new ArrayList<>();

        File file = new File(path);
        if(file.exists() && file.isDirectory()) {
            for(File f: file.listFiles()) {
                if(f.isDirectory()) {
                    if(getRecursive()) {
                        files.addAll(listFiles(f.getPath()));
                    }
                }
                else if(hasAllowedExtension(f.getPath())) {
                    files.add(f.getPath());
                }
            }
        }

        return files;
    }
}
