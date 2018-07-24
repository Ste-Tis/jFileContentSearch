package com.sepherot.fcs;

import com.sepherot.fcs.data.SearchConfiguration;
import com.sepherot.fcs.data.SearchResult;
import org.apache.commons.cli.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Start and manage search
 *
 * @author      Stephan Tischer
 * @version     0.1.0
 * @since       2018-07-23
 */
public class FileContentSearch {
    private SearchConfiguration config;

    /**
     * Start fcs.FileContentSearch
     *
     * @param args Commandline arguments
     */
    public FileContentSearch(String[] args) throws ParseException {
        config = CmdParser.parse(args);
    }

    /**
     * Remove base path from paths to files
     *
     * @param path Path to shorten
     * @return Shortened path
     */
    private String shortenPath(String path) {
        String result = path;

        if(!config.isLongPaths() && path.startsWith(config.getRootDirectory())) {
            int shorten = config.getRootDirectory().length();
            if(!(config.getRootDirectory().endsWith("/") || config.getRootDirectory().endsWith("\\"))) {
                shorten++;
            }
            result = path.substring(shorten);
        }

        return result;
    }

    /**
     * Group results by term and file
     *
     * @param results Search results
     * @return Grouped results
     */
    private Map<String, Map<String, List<Integer>>> groupSearchResults(List<SearchResult> results) {
        Map<String, Map<String, List<Integer>>> groupedResults = new HashMap<>();

        for(SearchResult r: results) {
            if(r == null)
                continue;

            // Add new term
            if(!groupedResults.containsKey(r.getTerm()))
                groupedResults.put(r.getTerm(), new HashMap<>());
            // Add new file to term
            if(!groupedResults.get(r.getTerm()).containsKey(r.getFile()))
                groupedResults.get(r.getTerm()).put(r.getFile(), new ArrayList<>());
            // Add new unique line number
            if(r.getLineNumber() != null && !groupedResults.get(r.getTerm()).get(r.getFile()).contains(r.getLineNumber()))
                groupedResults.get(r.getTerm()).get(r.getFile()).add(r.getLineNumber());
            if(r.getLineNumber() == null && !groupedResults.get(r.getTerm()).get(r.getFile()).contains(0))
                groupedResults.get(r.getTerm()).get(r.getFile()).add(0);
        }

        return groupedResults;
    }

    /**
     * Print search results for simple search without line numbers
     *
     * @param results List with search results
     * @return String with formatted results
     */
    private String formatResultsForSimpleOutput(List<SearchResult> results) {
        StringBuilder resultStr = new StringBuilder();

        Map<String, Map<String, List<Integer>>> groupedResults = groupSearchResults(results);
        for(Map.Entry<String, Map<String, List<Integer>>> term: groupedResults.entrySet()) {
            resultStr.append(String.format("%n>> %s%n", term.getKey()));
            for(Map.Entry<String, List<Integer>> file: term.getValue().entrySet()) {
                resultStr.append(String.format("  %s%n", shortenPath(file.getKey())));
            }
        }

        return resultStr.toString();
    }

    /**
     * Print search results for complex search with line numbers
     *
     * @param results List with search results
     * @return String with formatted results
     */
    private String formatResultsForComplexOutput(List<SearchResult> results) {
        StringBuilder resultStr = new StringBuilder();

        Map<String, Map<String, List<Integer>>> groupedResults = groupSearchResults(results);
        for(Map.Entry<String, Map<String, List<Integer>>> term: groupedResults.entrySet()) {
            resultStr.append(String.format("%n>> %s%n", term.getKey()));
            for(Map.Entry<String, List<Integer>> file: term.getValue().entrySet()) {
                List<String> lineNumbers = new ArrayList<>();
                for(Integer line: file.getValue())
                    lineNumbers.add(String.valueOf(line));

                resultStr.append(String.format(
                    "  %s%n    [%s]%n",
                    shortenPath(file.getKey()),
                    String.join(", ", lineNumbers)
                ));
            }
        }

        return resultStr.toString();
    }

    /**
     * Start search. Prints results to console
     *
     * @return Returns results as formatted string
     */
    public String search() {
        StringBuilder resultStr = new StringBuilder();
        resultStr.append(String.format("Searching in %s for [%s]:%n", config.getRootDirectory(), String.join(", ", config.getSearchTerms())));

        DirWalker dw = new DirWalker();
        dw.setAllowedExtenstions(config.getExtensions());
        dw.setRecursive(config.isRecursive());

        List<SearchResult> results = new ArrayList<>();
        for(String file: dw.listFiles(config.getRootDirectory())) {
            results.addAll(FileInspector.search(
                    file, config.getSearchTerms(), config.isCaseSensitive(), config.isLongPaths()
            ));
        }

        if(config.isShowLines())
            resultStr.append(formatResultsForComplexOutput(results));
        else
            resultStr.append(formatResultsForSimpleOutput(results));

        return resultStr.toString();
    }
}
