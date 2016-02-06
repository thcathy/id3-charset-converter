package thc.id3;


import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
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

	public int run(String... args) {
		log.info("Start id3 character set converter");
		
		try {
			Parameters params = Parameters.parse(args);
			if (params.isHelp()) {
				Parameters.printHelp(formatter);
			} else {
				convertService.convert(params.getSourcePath().get(), 
										params.getTargetPath().orElse(params.getSourcePath().get()), 
										params.getCharSet(), !params.isTest());
			}
		} catch (MissingArgumentException m) {
			log.error(m.getMessage());
			Parameters.printHelp(formatter);
			return 1;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return 1;
		}
		return 0;
	}
	
	public static void main(String[] args) throws Exception {
		new Id3CharSetConverterCommand(new HelpFormatter(), new ConvertService()).run(args);
	}
}