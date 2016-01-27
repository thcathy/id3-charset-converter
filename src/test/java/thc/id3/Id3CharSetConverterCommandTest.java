package thc.id3;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class Id3CharSetConverterCommandTest {
	
	@Mock
	HelpFormatter formatter;
	
	@Test
	public void givenArgsWithHelp_shouldPrintUsage() throws Exception {
		new Id3CharSetConverterCommand(formatter).run(new String[] {"-h"});
		
		verify(formatter, times(1)).printHelp(Mockito.anyString(), Mockito.anyObject());
	}
	
	@Test
	public void givenNoArgs_shouldPrintUsage() throws Exception {
		new Id3CharSetConverterCommand(formatter).run();
		verify(formatter, times(1)).printHelp(Mockito.anyString(), Mockito.anyObject());
	}
	
	
}
