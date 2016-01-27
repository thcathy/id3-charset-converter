package thc.id3;


import org.apache.commons.cli.HelpFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import thc.id3.charset.convert.Parameters;


public class Id3CharSetConverterCommand {
	private static Logger log = LoggerFactory.getLogger(Id3CharSetConverterCommand.class);
	
	private HelpFormatter formatter;
	
	public Id3CharSetConverterCommand(HelpFormatter formatter) {
		this.formatter = formatter;
	}

	public void run(String... args) {
		try {
			Parameters params = Parameters.parse(args);
			if (params.isHelp()) Parameters.printHelp(formatter);
		} catch (Exception e) {
			log.error(e.getMessage());
			Parameters.printHelp(formatter);
		}
	}
	
	public static void main(String[] args) throws Exception {
		
	}
}