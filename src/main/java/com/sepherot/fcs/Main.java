package com.sepherot.fcs;

import org.apache.commons.cli.ParseException;

public class Main {
    public static void main(String[] args) {
        try {
            FileContentSearch fcs = new FileContentSearch(args);
            System.out.println(fcs.search());
        } catch (ParseException e) {
            System.out.println("Parsing commandline arguments failed. Use -h to show possible options.");
            e.printStackTrace();
        }
    }
}
