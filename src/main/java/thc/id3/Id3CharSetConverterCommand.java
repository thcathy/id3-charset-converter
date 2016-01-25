package thc.id3;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import thc.id3.charset.convert.Parameters;


public class Id3CharSetConverterCommand {
	private static Logger log = LoggerFactory.getLogger(Id3CharSetConverterCommand.class);
	
	private Parameters params;
	
	public Id3CharSetConverterCommand(Parameters params) {
		this.params = params;
	}

	public void run(String... args) {
		if (params.isHelp()) params.printHelp();
	}
	
	public static void main(String[] args) throws Exception {
		
	}
}