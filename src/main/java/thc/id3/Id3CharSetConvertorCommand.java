package thc.id3;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Id3CharSetConvertorCommand {
	private static Logger log = LoggerFactory.getLogger(Id3CharSetConvertorCommand.class);
	
	private Id3CharSetConvertorParameters params;
	
	public Id3CharSetConvertorCommand(Id3CharSetConvertorParameters params) {
		this.params = params;
	}

	public void run(String... args) {
		if (params.isHelp()) params.printHelp();
	}
	
	public static void main(String[] args) throws Exception {
				
	}
}