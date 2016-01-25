package thc.id3.charset.convert;
import java.util.Optional;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class Parameters {
	final static String OPTION_HELP = "help";
	final static String OPTION_TEST = "test";
	final static String OPTION_INPUT_FILE = "input-file";
	final static String OPTION_OUTPUT_FILE = "output-file";	
	final static String OPTION_INPUT_FOLDER = "input-folder";
	final static String OPTION_OUTPUT_FOLDER = "output-folder";
	final static String OPTION_FROM_CHARSET = "from-charset";
	final static String OPTION_TO_CHARSET = "to-charset";
	
	final static String DEFAULT_FROM_CHARSET = "ISO-8859-1";
	final static String DEFAULT_TO_CHARSET = "UTF-8";
	
	final static Options options = buildOptions();
	
	final CommandLine commandLine; 
	
	private Parameters(CommandLine commandLine) {
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
		options.addOption(Option.builder("c")
							.desc("input CHARSET, default=" + DEFAULT_FROM_CHARSET)
							.longOpt(OPTION_FROM_CHARSET)
							.hasArg().argName("CHARSET")
							.build());
		options.addOption(Option.builder("C")
							.desc("output CHARSET, default=" + DEFAULT_TO_CHARSET)
							.longOpt(OPTION_TO_CHARSET)
							.hasArg().argName("CHARSET")
							.build());
		
		return options;
	}

	public static Parameters parse(String[] args) throws Exception {		
		CommandLineParser parser = new DefaultParser();
		Parameters params = new Parameters(parser.parse(options, args));
		
		if (params.noInputFileAndFolderAndHelp()) 
			throw new MissingArgumentException("Require argument input file or input folder");
		else 
			return params;
	}

	private boolean noInputFileAndFolderAndHelp() {
		return !(this.getInputFile().isPresent() || this.getInputFolder().isPresent() || this.isHelp());
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
	
	public String getFromCharSet() {
		return commandLine.hasOption(OPTION_FROM_CHARSET) ? commandLine.getOptionValue(OPTION_FROM_CHARSET) : DEFAULT_FROM_CHARSET;
	}
	
	public String getToCharSet() {
		return commandLine.hasOption(OPTION_TO_CHARSET) ? commandLine.getOptionValue(OPTION_TO_CHARSET) : DEFAULT_TO_CHARSET;
	}
	
	public void printHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "java xxxxxxxxx", options);
	}
}

