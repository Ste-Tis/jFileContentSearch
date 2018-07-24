package com.sepherot.fcs;

import static org.junit.Assert.*;

import com.sepherot.fcs.data.SearchResult;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileInspectorTests {
    private String rootPath;
    private String sub1Path;
    private String sub2Path;
    private String fileTxtPath;
    private String filePyPath;
    private String fileLogPath;

    public FileInspectorTests() throws IOException {
        Path tempDir = Files.createTempDirectory("root");

        rootPath = tempDir.toAbsolutePath().toString();
        sub1Path = Paths.get(rootPath, "sub1").toAbsolutePath().toString();
        sub2Path = Paths.get(rootPath, "sub2").toAbsolutePath().toString();
        fileTxtPath = Paths.get(rootPath, "lorem.txt").toAbsolutePath().toString();
        filePyPath = Paths.get(sub1Path, "amon.py").toAbsolutePath().toString();
        fileLogPath = Paths.get(sub1Path, "amarth.log").toAbsolutePath().toString();

        String txtContent = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr,\n" +
                "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat,\n" +
                "sed diam voluptua.";
        String pyContent = "#! /usr/bin/env python\n" +
                "# -*- coding: utf-8 -*-\n" +
                "from dataclasses import dataclass\n" +
                "import os\n" +
                "\n\n" +
                "__author__ = 'Stephan Tischer'\n" +
                "_date__ = '2018-07-04'\n" +
                "__version__ = '0.1.0'\n" +
                "\n\n" +
                "@dataclass\n" +
                "class File:\n" +
                "  name: str\n" +
                "  path: str\n" +
                "  extension: str\n" +
                "\n\n" +
                "  def __init__(self, path):\n" +
                "      self.path = path\n" +
                "      self.name = os.path.basename(path)\n" +
                "      self.extension = os.path.splitext(path)[1].replace('.', '').lower()\n";
        String logContent = "2018-07-04 21:29:53 INFO - New request\n" +
                "2018-07-04 21:30:35 INFO - New request\n" +
                "2018-07-04 21:30:45 WARN - Invalid request\n";

        removeTestDirectory();

        File rootDir = new File(rootPath);
        rootDir.mkdir();
        File sub1Dir = new File(sub1Path);
        sub1Dir.mkdir();
        File sub2Dir = new File(sub2Path);
        sub2Dir.mkdir();

        Path txtFile = Paths.get(fileTxtPath);
        Files.write(txtFile, txtContent.getBytes());
        Path pyFile = Paths.get(filePyPath);
        Files.write(pyFile, pyContent.getBytes());
        Path logFile = Paths.get(fileLogPath);
        Files.write(logFile, logContent.getBytes());
    }

    @Override
    public void finalize() throws IOException {
        removeTestDirectory();
    }

    private void removeTestDirectory() throws IOException {
        File f = new File(rootPath);
        if(f.exists())
            FileUtils.deleteDirectory(new File(rootPath));
    }

    @Test
    public void executeSearch_termInFile_returnFile() {
        // Arrange
        String term = "sed";

        // Act
        SearchResult result = FileInspector.executeSearch(fileTxtPath, term);

        // Assert
        assertNotNull(result);
        assertNull(result.getLineNumber());
        assertEquals(fileTxtPath, result.getFile());
    }

    @Test
    public void executeSearch_termCaseSensitive_returnNull() {
        // Arrange
        String term = "Sed";

        // Act
        SearchResult result = FileInspector.executeSearch(fileTxtPath, term, true);

        // Assert
        assertNull(result);
    }

    @Test
    public void executeSearch_termNotInFile_returnNull() {
        // Arrange
        String term = "notFound";

        // Act
        SearchResult result = FileInspector.executeSearch(fileTxtPath, term);

        // Assert
        assertNull(result);
    }

    @Test
    public void executeSearch_termHasLineBreak_returnFile() {
        // Arrange
        String term = "elitr,\nsed";

        // Act
        SearchResult result = FileInspector.executeSearch(fileTxtPath, term);

        // Assert
        assertNotNull(result);
        assertNull(result.getLineNumber());
        assertEquals(fileTxtPath, result.getFile());
    }

    @Test
    public void executeSearchShowLines_termInFile_returnFile() {
        // Arrange
        String term = "sed";

        // Act
        List<SearchResult> results = FileInspector.executeSearchShowLines(fileTxtPath, term);

        // Assert
        assertEquals(2, results.size());
        assertEquals(fileTxtPath, results.get(0).getFile());
        assertNotNull(results.get(0).getLineNumber());
        assertEquals(2, results.get(0).getLineNumber().intValue());
        assertEquals(fileTxtPath, results.get(1).getFile());
        assertNotNull(results.get(1).getLineNumber());
        assertEquals(3, results.get(1).getLineNumber().intValue());
    }

    @Test
    public void executeSearchShowLines_termCaseSensitive_returnEmptyList() {
        // Arrange
        String term = "Sed";

        // Act
        List<SearchResult> results = FileInspector.executeSearchShowLines(fileTxtPath, term, true);

        // Assert
        assertEquals(0, results.size());
    }

    @Test
    public void executeSearchShowLines_termNotInFile_returnEmptyList() {
        // Arrange
        String term = "notFound";

        // Act
        List<SearchResult> results = FileInspector.executeSearchShowLines(fileTxtPath, term);

        // Assert
        assertEquals(0, results.size());
    }

    @Test
    public void executeSearchShowLines_termHasLineBreak_returnFile() {
        // Arrange
        String term = "elitr,\nsed";

        // Act
        List<SearchResult> results = FileInspector.executeSearchShowLines(fileTxtPath, term);

        // Assert
        assertEquals(1, results.size());
        assertEquals(fileTxtPath, results.get(0).getFile());
        assertNotNull(results.get(0).getLineNumber());
        assertEquals(1, results.get(0).getLineNumber().intValue());
    }

    @Test
    public void executeSearchShowLines_termHasLineBreak_multipleHits() {
        // Arrange
        String term = "str\n  ";

        // Act
        List<SearchResult> results = FileInspector.executeSearchShowLines(filePyPath, term);

        // Assert
        assertEquals(2, results.size());
        assertEquals(filePyPath, results.get(0).getFile());
        assertNotNull(results.get(0).getLineNumber());
        assertEquals(14, results.get(0).getLineNumber().intValue());
        assertEquals(filePyPath, results.get(1).getFile());
        assertNotNull(results.get(1).getLineNumber());
        assertEquals(15, results.get(1).getLineNumber().intValue());
    }

    @Test
    public void search_callsExecuteSearch_returnOnlyOneResult() {
        // Arrange
        List<String> term = new ArrayList<>();
        term.add("sed");

        // Act
        List<SearchResult> results = FileInspector.search(fileTxtPath, term);

        // Assert
        assertEquals(1, results.size());
    }

    @Test
    public void search_callsExecuteSearchShowLines_returnMultipleResults() {
        // Arrange
        List<String> term = new ArrayList<>();
        term.add("sed");

        // Act
        List<SearchResult> results = FileInspector.search(fileTxtPath, term, false, true);

        // Assert
        assertEquals(2, results.size());
    }

    @Test
    public void search_multipleTerms_returnMultipleFiles() {
        // Arrange
        List<String> term = new ArrayList<>();
        term.add("sed");
        term.add("WARN");

        // Act
        List<SearchResult> results = FileInspector.search(fileTxtPath, term);

        // Assert
        assertEquals(2, results.size());
    }
}
