package com.sepherot.fcs.data;

import java.io.Serializable;
import java.util.List;

/**
 * Stores the configuration of the application
 *
 * @author      Stephan Tischer
 * @version     0.1.0
 * @since       2018-07-22
 */
public final class SearchConfiguration implements Serializable {
    private List<String> searchTerms;
    private String rootDirectory;
    private List<String> extensions;
    private boolean showLines;
    private boolean caseSensitive;
    private boolean longPaths;
    private boolean recursive;

    /**
     * Create new configuration
     *
     * @param searchTerms Terms to search for
     * @param rootDirectory Directory to search
     * @param extensions Only look into files with the given extensions
     * @param showLines Show lines in which result occurs
     * @param caseSensitive Execute search case sensitive
     * @param longPaths Show full path in results
     * @param recursive Search subdirectories
     */
    public SearchConfiguration(List<String> searchTerms, String rootDirectory, List<String> extensions,
                               boolean showLines, boolean caseSensitive, boolean longPaths, boolean recursive) {
        this.searchTerms = searchTerms;
        this.rootDirectory = rootDirectory;
        this.extensions = extensions;
        this.showLines = showLines;
        this.caseSensitive = caseSensitive;
        this.longPaths = longPaths;
        this.recursive = recursive;
    }

    /**
     * Create new configuration
     *
     * @param searchTerms Terms to search for
     * @param rootDirectory Directory to search
     * @param extensions Only look into files with the given extensions
     */
    public SearchConfiguration(List<String> searchTerms, String rootDirectory, List<String> extensions) {
        this(searchTerms, rootDirectory, extensions, false, false, false, true);
    }

    /**
     * Create new configuration
     */
    public SearchConfiguration() {
        this(null, null, null, false, false, false, true);
    }

    public List<String> getSearchTerms() {
        return searchTerms;
    }

    public void setSearchTerms(List<String> searchTerms) {
        this.searchTerms = searchTerms;
    }

    public String getRootDirectory() {
        return rootDirectory;
    }

    public void setRootDirectory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<String> extensions) {
        this.extensions = extensions;
    }

    public boolean isShowLines() {
        return showLines;
    }

    public void setShowLines(boolean showLines) {
        this.showLines = showLines;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public boolean isLongPaths() {
        return longPaths;
    }

    public void setLongPaths(boolean longPaths) {
        this.longPaths = longPaths;
    }

    public boolean isRecursive() {
        return recursive;
    }

    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }
}
