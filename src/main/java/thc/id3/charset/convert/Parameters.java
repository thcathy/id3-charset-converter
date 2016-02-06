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
	final static String OPTION_CHARSET = "charset";
	
	final static String DEFAULT_CHARSET = "BIG5";
	
	final static Options options = buildOptions();
	
	final CommandLine commandLine; 
	
	private Parameters(CommandLine commandLine) {
		this.commandLine = commandLine;		
	}
	
	private static Options buildOptions() {
		Options options = new Options();
		options.addOption("h", OPTION_HELP, false, "print help message");
		options.addOption("t", OPTION_TEST, false, "test run without saving files");
		options.addOption(Option.builder("c")
							.desc("source CHARSET, default=" + DEFAULT_CHARSET)
							.longOpt(OPTION_CHARSET)
							.hasArg().argName("CHARSET")
							.build());		
		return options;
	}

	public static Parameters parse(String[] args) throws Exception {		
		CommandLineParser parser = new DefaultParser();
		Parameters params = new Parameters(parser.parse(options, args));
		
		if (params.noSourcePathAndHelp()) 
			throw new MissingArgumentException("Missing source file / folder");
		else 
			return params;
	}

	private boolean noSourcePathAndHelp() {
		return !(this.getSourcePath().isPresent() || this.isHelp());
	}

	public boolean isHelp() {
		return commandLine.hasOption(OPTION_HELP);
	}
	
	public Optional<String> getSourcePath() {
		try {
			return Optional.of(commandLine.getArgs()[0]);
		} catch (ArrayIndexOutOfBoundsException e) {
			return Optional.empty();
		}
	}
	
	public Optional<String> getTargetPath() {
		try {
			return Optional.of(commandLine.getArgs()[1]);
		} catch (ArrayIndexOutOfBoundsException e) {
			return Optional.empty();
		}
	}
	
	public boolean isTest() {
		return commandLine.hasOption(OPTION_TEST);
	}
	
	public String getCharSet() {
		return commandLine.hasOption(OPTION_CHARSET) ? commandLine.getOptionValue(OPTION_CHARSET) : DEFAULT_CHARSET;
	}
		
	public static void printHelp(HelpFormatter formatter) {
		formatter.printHelp("id3-charset-converter [-c <CHARSET>] [-h] [-t] source [target]", "Convert the ID3 tags of all mp3 under source to UTF-8 and save to target", options, "");
	}
	
	public static void main(String... args) {
		Parameters.printHelp(new HelpFormatter());
	}
}

