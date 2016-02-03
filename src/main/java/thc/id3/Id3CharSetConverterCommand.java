package thc.id3;


import org.apache.commons.cli.HelpFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import thc.id3.charset.convert.ConvertService;
import thc.id3.charset.convert.Parameters;


public class Id3CharSetConverterCommand {
	private static Logger log = LoggerFactory.getLogger(Id3CharSetConverterCommand.class);
	
	private HelpFormatter formatter;
	private ConvertService convertService;
	
	public Id3CharSetConverterCommand(HelpFormatter formatter, ConvertService convertService) {
		this.formatter = formatter;
		this.convertService = convertService;
	}

	public void run(String... args) {
		try {
			Parameters params = Parameters.parse(args);
			if (params.isHelp()) {
				Parameters.printHelp(formatter);
			} else {
				convertService.convert(params.getSourcePath().get(), params.getTargetPath().get(), params.getFromCharSet(), params.getToCharSet());
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			Parameters.printHelp(formatter);
		}
	}
	
	public static void main(String[] args) throws Exception {
		
	}
}