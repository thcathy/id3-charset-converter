package thc.id3;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class Id3CharSetConverterCommandTest {
	
	@Mock
	Id3CharSetConverterParameters params;
	
	@Test
	public void givenArgsWithHelp_shouldPrintUsage() throws Exception {
		// given 
		when(params.isHelp()).thenReturn(true);
		
		new Id3CharSetConverterCommand(params).run();
		
		verify(params, times(1)).printHelp();
	}
	
	public void givenNoArgs_shouldGivenException() throws Exception {
		new Id3CharSetConverterCommand(params).run();
	}
}
