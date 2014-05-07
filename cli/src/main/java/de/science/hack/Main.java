/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version. 
 */
package de.science.hack;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import toxi.geom.mesh.TriangleMesh;

import static org.apache.commons.io.FileUtils.listFiles;
import static org.apache.commons.cli.OptionBuilder.*;

/**
 * Entry point for the jetstreams models. <br/>
 * It accepts .txt or .csv files as a data input.
 *
 * @author Mario
 */
public class Main {

    private static final String[] EXT = new String[]{"txt", "csv"};
    private static final String DEFAULT_OUT = "model.stl";

    private final CommandLineParser parser;
    private final Options options;
    private final WindModelBuilder windModelBuilder;
    private final JetStreamModelWriter jetStreamModelWriter;


    public Main() {
        parser = new GnuParser();
        
        options = new Options();
        options.addOption(buildOption(CliArg.D));
        options.addOption(buildOption(CliArg.F));
        options.addOption(buildOption(CliArg.O));
        
        windModelBuilder = new WindModelBuilder();
        jetStreamModelWriter = new JetStreamModelWriter();
    }
    
    private Option buildOption(CliArg arg) {
        return withArgName(arg.getLongKey()).hasArg().withDescription(arg.getDescription()).create(arg.getShortKey());
    }

    /**
     * This method process the arguments.
     * @param args
     * @throws ParseException 
     */
    private void process(String [] args) throws ParseException {
        CommandLine commandLine = parser.parse(options, args);
        
        getInputFiles(commandLine).stream().forEach((f) -> {
            TriangleMesh mesh = windModelBuilder.build(f);
            jetStreamModelWriter.addWindModel(mesh);
        });
        
        jetStreamModelWriter.write(getOutput(commandLine));
    }
    
    private Collection<File> getInputFiles(CommandLine commandLine){
       Collection<File> files = new ArrayList<>();
       if(commandLine.hasOption(CliArg.F.getShortKey())){
           String fileName = commandLine.getOptionValue(CliArg.F.getShortKey());
           files.add(new File(fileName));
       }else if(commandLine.hasOption(CliArg.D.getShortKey())){
           String dirName = commandLine.getOptionValue(CliArg.D.getShortKey());
           File directory = new File(dirName);
           files.addAll(listFiles(directory, EXT, false));
       }
       return files;
    }
 
    
    /**
     * This method returns the output file from the command line.
     * @param commandLine
     * @return 
     */
    private String getOutput(CommandLine commandLine){
        String val = commandLine.getOptionValue(CliArg.O.getShortKey());
        return (val == null) ? getDefaultOut() : val;//return default if not provided
    }

    private String getDefaultOut() {
        File file = new File(System.getProperty("user.dir"), DEFAULT_OUT);
        return file.getPath();
    }
    
    public static void main(String[] args) throws ParseException {
        Main m = new Main();
        m.process(args);
    }
}
