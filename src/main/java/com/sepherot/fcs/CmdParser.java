package com.sepherot.fcs;

import com.sepherot.fcs.data.SearchConfiguration;
import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Parse the command line parameter an return a configuration used for the following search
 *
 * @author      Stephan Tischer
 * @version     0.1.0
 * @since       2018-07-22
 */
public class CmdParser {
    private CmdParser() {}

    /**
     * Parse commandline parameter and create Configuration from them
     *
     * @param args Commandline parameter
     * @return Configuration for search
     * @throws ParseException
     */
    public static SearchConfiguration parse(String[] args) throws ParseException {
        // Defines special options
        Option extensions = new Option("e", "extensions", true, "Only look into files with the given extensions (separate multiple extensions by space");
        extensions.setArgs(Option.UNLIMITED_VALUES);

        // Define possible options
        Options options = new Options();
        options.addOption(extensions);
        options.addOption("d", "dir", true, "Path to directory which should be searched");
        options.addOption("l", "lines", false, "Show number of line in which term was found");
        options.addOption("cs", "case-sensitive", false, "Execute search case sensitive");
        options.addOption("long", false, "Show complete path to file");
        options.addOption("ns", "no-subdirectories", false,"Exclude subdirectories from the search");
        options.addOption("h", "help", false, "Hilfe anzeigen");

        // Create parser and process commandline arguments
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if(cmd.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("fcs.FileContentSearch", options);
            System.exit(0);
        }

        // Create SearchConfiguration
        SearchConfiguration config = new SearchConfiguration();
        config.setSearchTerms(cmd.getArgList());
        config.setRootDirectory(cmd.getOptionValue("d", "."));
        config.setShowLines(cmd.hasOption("l"));
        config.setCaseSensitive(cmd.hasOption("cs"));
        config.setLongPaths(cmd.hasOption("long"));
        config.setRecursive(!cmd.hasOption("ns"));

        if(cmd.getOptionValues("e") != null) {
            // Remove point at beginning
            List<String> ext = new ArrayList<>();
            for(String e: cmd.getOptionValues("e")) {
                if(e.startsWith("."))
                    ext.add(e.substring(1));
                else
                    ext.add(e);
            }
            config.setExtensions(ext);
        }
        else
            config.setExtensions(null);

        return config;
    }
}
