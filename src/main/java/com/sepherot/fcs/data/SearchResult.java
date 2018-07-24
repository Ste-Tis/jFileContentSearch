package com.sepherot.fcs.data;

import java.io.Serializable;

/**
 * Store all fcs.data for one search result
 *
 * @author      Stephan Tischer
 * @version     0.1.0
 * @since       2018-07-22
 */
public final class SearchResult implements Serializable {
    /**
     * Save name of file
     */
    private String file;

    /**
     * Save search term found
     */
    private String term;

    /**
     * Save number of line in which term was found
     */
    private Integer lineNumber;

    /**
     * Create new search result
     *
     * @param file Filename
     * @param term Search term found in file
     * @param lineNumber Number of line with term
     */
    public SearchResult(String file, String term, Integer lineNumber) {
        this.file = file;
        this.term = term;
        this.lineNumber = lineNumber;
    }

    /**
     * Compare two search results
     *
     * @param obj Object to compare to
     * @return Returns TRUE, if both are the same, otherwise FALSE
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof SearchResult))
            return false;

        SearchResult other = (SearchResult)obj;
        return (this.file.equals(other.file) && this.term.equals(other.term) && this.lineNumber.equals(other.lineNumber));
    }

    /**
     * Calculate hash value for instance
     *
     * @return Hash value
     */
    @Override
    public int hashCode() {
        int result = 1337;

        result *= file.hashCode();
        result *= term.hashCode();
        result *= lineNumber.hashCode();

        return result;
    }

    /**
     * Return current state of class as string
     *
     * @return Current state of class
     */
    @Override
    public String toString() {
        return String.format("SearchResult(File: %s; Term: %s; Line number: %s", this.file, this.term, this.lineNumber);
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }
}
