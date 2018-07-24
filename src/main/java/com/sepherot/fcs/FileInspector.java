package com.sepherot.fcs;

import com.sepherot.fcs.data.SearchResult;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Processes file content to search for terms
 *
 * @author Stephan Tischer
 * @version     0.1.0
 * @since       2018-07-22
 */
public class FileInspector {
    private FileInspector() {}

    /**
     * Return the whole content of the given file
     *
     * @param path Path to file
     * @return Content of the file
     */
    protected static String readFile(String path) {
        File f = new File(path);
        if (!f.exists() || !f.isFile())
            return "";

        try {
            return new String(
                    Files.readAllBytes(Paths.get(path))
            );
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Calculates the number of lines in the given string
     *
     * @param content String to count lines in
     * @param offset Offset to add to the result
     * @return Number of lines
     */
    protected static int getNumberOfLines(String content, int offset) {
        String lineSeparator = "\n";
        int lastIndex = 0;
        int count = 1;

        while(lastIndex != -1) {
            lastIndex = content.indexOf(lineSeparator, lastIndex);
            if(lastIndex != -1) {
                count++;
                lastIndex += 1;
            }
        }

        return count + offset;
    }

    /**
     * Calculates the number of lines in the given string
     *
     * @param content String to count lines in
     * @return Number of lines
     */
    protected static int getNumberOfLines(String content) {
        return getNumberOfLines(content, 0);
    }

    /**
     * Split content into two paths at the given position
     *
     * @param content Content to split
     * @param pos Position to split content at (-1 = don't split, move everything into first part)
     * @return Returns tuple containing the both results of the split
     */
    protected static Pair<String, String> splitContent(String content, int pos) {
        if(pos == -1)
            return new Pair<>(content, "");
        return new Pair<>(content.substring(0, pos), content.substring(pos));
    }

    /**
     * Execute a simple search in the given content
     *
     * @param path Path to file (only needed to create complete SearchResult)
     * @param term String to search for
     * @param caseSensitive Search is case sensitive
     * @return Returns a SearchResult or NULL if nothing was found
     */
    public static SearchResult executeSearch(String path, String term, boolean caseSensitive) {
        String content = readFile(path);

        if(!caseSensitive) {
            content = content.toLowerCase();
            term = term.toLowerCase();
        }

        if(content.indexOf(term) != -1)
            return new SearchResult(path, term, null);
        return null;
    }

    /**
     * Execute a simple search in the given content
     *
     * @param path Path to file (only needed to create complete SearchResult)
     * @param term String to search for
     * @return Returns a SearchResult or NULL if nothing was found
     */
    public static SearchResult executeSearch(String path, String term) {
        return executeSearch(path, term, false);
    }

    /**
     * Execute a search also tracking lines in which the term was found
     *
     * @param path Path to file (only needed to create complete SearchResult
     * @param term String to search for
     * @param caseSensitive Search is case sensitive
     * @return Returns a list with all SearchResults
     */
    public static List<SearchResult> executeSearchShowLines(String path, String term, boolean caseSensitive) {
        List<SearchResult> results = new ArrayList<>();
        String curContent = readFile(path);
        String untilTerm;
        String lineSeparator = "\n";
        int lineNumber = 0;

        if(!caseSensitive) {
            curContent = curContent.toLowerCase();
            term = term.toLowerCase();
        }

        int posTerm = curContent.indexOf(term);
        int posLineEnd = curContent.indexOf(lineSeparator, posTerm);
        while(posTerm != -1) {
            // Split content on first line end after first match
            if(posLineEnd != -1) {
                Pair<String, String> split = splitContent(curContent, posLineEnd + lineSeparator.length());
                untilTerm = split.getKey().substring(0, split.getKey().length() - lineSeparator.length());
                curContent = split.getValue();
            }
            else {
                Pair<String, String> split = splitContent(curContent, -1);
                untilTerm = split.getKey();
                curContent = split.getValue();
            }

            // Get line number in which term was found
            lineNumber = getNumberOfLines(untilTerm, lineNumber);
            results.add(new SearchResult(path, term, lineNumber));

            // Rerun search in rest of content
            posTerm = curContent.indexOf(term);
            posLineEnd = curContent.indexOf(lineSeparator, posTerm);
        }

        return results;
    }

    /**
     * Execute a search also tracking lines in which the term was found
     *
     * @param path Path to file (only needed to create complete SearchResult
     * @param term String to search for
     * @return Returns a list with all SearchResults
     */
    public static List<SearchResult> executeSearchShowLines(String path, String term) {
        return executeSearchShowLines(path, term, false);
    }

    /**
     * Searches in the given file for the given terms
     *
     * @param path Path to file
     * @param terms Search terms
     * @param caseSensitive Search is case sensitive
     * @param showLines Track lines of appearance
     * @return List with search results
     */
    public static List<SearchResult> search(String path, List<String> terms, boolean caseSensitive, boolean showLines) {
        List<SearchResult> results = new ArrayList<>();

        for(String term: terms) {
            if(showLines)
                results.addAll(executeSearchShowLines(path, term, caseSensitive));
            else
                results.add(executeSearch(path, term, caseSensitive));
        }

        return results;
    }

    /**
     * Searches in the given file for the given terms
     *
     * @param path Path to file
     * @param terms Search terms
     * @return List with search results
     */
    public static List<SearchResult> search(String path, List<String> terms) {
        return search(path, terms, false, false);
    }
}
