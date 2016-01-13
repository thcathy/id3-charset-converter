package thc.id3;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Id3CharSetConverterCommand {
	private static Logger log = LoggerFactory.getLogger(Id3CharSetConverterCommand.class);
	
	private Id3CharSetConverterParameters params;
	
	public Id3CharSetConverterCommand(Id3CharSetConverterParameters params) {
		this.params = params;
	}

	public void run(String... args) {
		if (params.isHelp()) params.printHelp();
	}
	
	public static void main(String[] args) throws Exception {
		
	}
}