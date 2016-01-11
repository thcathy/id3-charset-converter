package thc.id3;
import java.util.Optional;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class Id3CharSetConvertorParameters {
	final static String OPTION_HELP = "help";
	final static String OPTION_TEST = "test";
	final static String OPTION_INPUT_FILE = "input-file";
	final static String OPTION_OUTPUT_FILE = "output-file";	
	final static String OPTION_INPUT_FOLDER = "input-folder";
	final static String OPTION_OUTPUT_FOLDER = "output-folder";
	final static Options options = buildOptions();
	
	final CommandLine commandLine; 
	
	private Id3CharSetConvertorParameters(CommandLine commandLine) {
		this.commandLine = commandLine;		
	}
	
	private static Options buildOptions() {
		Options options = new Options();
		options.addOption("h", OPTION_HELP, false, "print help message");
		options.addOption("t", OPTION_TEST, false, "test run without saving files");
		options.addOption(Option.builder("i")
						    .desc("input FILE to convert")
						    .longOpt(OPTION_INPUT_FILE)
						    .hasArg().argName("FILE")
						    .build());
		options.addOption(Option.builder("o")
						    .desc("coverted and save as FILE")
						    .longOpt(OPTION_OUTPUT_FILE)
						    .hasArg().argName("FILE")
						    .build());
		options.addOption(Option.builder("I")
						    .desc("input FOLDER, includes files and sub directories")
						    .longOpt(OPTION_INPUT_FOLDER)
						    .hasArg().argName("FOLDER")
						    .build());
		options.addOption(Option.builder("O")
						    .desc("output FOLDER")
						    .longOpt(OPTION_OUTPUT_FOLDER)
						    .hasArg().argName("FOLDER")
						    .build());
		return options;
	}

	public static Id3CharSetConvertorParameters parse(String[] args) throws Exception {		
		CommandLineParser parser = new DefaultParser();
		Id3CharSetConvertorParameters params = new Id3CharSetConvertorParameters(parser.parse(options, args));
		
		if (params.noInputFileAndFolder()) 
			throw new MissingArgumentException("Require argument input file or input folder");
		else 
			return params;
	}

	private boolean noInputFileAndFolder() {
		return !(this.getInputFile().isPresent() || this.getInputFolder().isPresent());
	}

	public boolean isHelp() {
		return commandLine.hasOption(OPTION_HELP);
	}
	
	public Optional<String> getInputFile() {
		return Optional.ofNullable(commandLine.getOptionValue(OPTION_INPUT_FILE));
	}
	
	public Optional<String> getOutputFile() {
		return Optional.ofNullable(commandLine.getOptionValue(OPTION_OUTPUT_FILE));
	}
	
	public Optional<String> getInputFolder() {
		return Optional.ofNullable(commandLine.getOptionValue(OPTION_INPUT_FOLDER));
	}
	
	public Optional<String> getOutputFolder() {
		return Optional.ofNullable(commandLine.getOptionValue(OPTION_OUTPUT_FOLDER));
	}
	
	public boolean isTest() {
		return commandLine.hasOption(OPTION_TEST);
	}
	
	public void printHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "java xxxxxxxxx", options);
	}

}
