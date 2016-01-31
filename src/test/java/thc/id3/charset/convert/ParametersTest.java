package thc.id3.charset.convert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.junit.Test;

public class ParametersTest {
	
	@Test
	public void printHelp_shouldRunWithoutException() throws Exception {
		Parameters.printHelp(new HelpFormatter());
	}
	
	@Test(expected=MissingArgumentException.class)
	public void givenNoArg_shouldRaiseException() throws Exception {
		try {
			Parameters.parse(new String[] {});
		} catch (MissingArgumentException m) {
			assertEquals("Missing source file / folder", m.getMessage());
			throw m;
		}
	}
	
	@Test
	public void givenHelpOption_shouldReturnTrue() throws Exception {
		String[] args = new String[] {"-h"};
		assertTrue(Parameters.parse(args).isHelp());
		
		args = new String[] {"-h", "abc"};
		assertTrue(Parameters.parse(args).isHelp());
		
		args = new String[] {"--help","abc"};
		assertTrue(Parameters.parse(args).isHelp());
	}
	
	@Test
	public void givenNoHelpOption_shouldReturnFalse() throws Exception {
		String[] args = new String[] {"abc"};
		assertEquals(false, Parameters.parse(args).isHelp());
	}
		
	@Test
	public void givenSourcePath_shouldReturnArgs() throws Exception {
		final String source = "abc";
		Parameters params = Parameters.parse(new String[] {source});
		assertEquals(source, params.getSourcePath().get());
	}
	
	@Test
	public void givenSourceAndTargetPath_shouldReturnBothArgs() throws Exception {
		final String sourcePath = "/tmp/test.java";
		final String targetPath = "src/test/java/Id3EncodingConverterCommandTest.java";
		Parameters params = Parameters.parse(new String[] {sourcePath, targetPath});
		
		assertEquals(sourcePath, params.getSourcePath().get());
		assertEquals(targetPath, params.getTargetPath().get());				
	}
	
	@Test
	public void givenNoOptionTargetFile_shouldReturnNoTargetPath() throws Exception {		
		Parameters params = Parameters.parse(new String[] {"abc"});
		assertEquals(Optional.empty(), params.getTargetPath());
	}
	
	@Test
	public void givenTestOption_shouldReturnTrue() throws Exception {
		String[] args = new String[] {"-t", "abc"};
		assertTrue(Parameters.parse(args).isTest());
	}
	
	@Test
	public void givenNoTestOption_shouldReturnFalse() throws Exception {
		String[] args = new String[] {"abc"};
		assertEquals(false, Parameters.parse(args).isTest());
	}
	
	@Test
	public void givenFromCharSet_shouldReturnArgsOrDefault() throws Exception {
		final String fromCharSet = "big5";
		Parameters params = Parameters.parse(new String[] {"-hc", fromCharSet});
		assertEquals(fromCharSet, params.getFromCharSet());
		
		params = Parameters.parse(new String[] {"--from-charset", fromCharSet, "source", "target"});
		assertEquals(fromCharSet, params.getFromCharSet());
		
		params = Parameters.parse(new String[] {"-h"});
		assertEquals("ISO-8859-1", params.getFromCharSet());
	}

	@Test
	public void givenToCharSet_shouldReturnArgsOrDefault() throws Exception {
		final String toCharSet = "big5";
		Parameters params = Parameters.parse(new String[] {"-hC", toCharSet});
		assertEquals(toCharSet, params.getToCharSet());
		
		params = Parameters.parse(new String[] {"--to-charset", toCharSet, "source", "target"});
		assertEquals(toCharSet, params.getToCharSet());
		
		params = Parameters.parse(new String[] {"-h"});
		assertEquals("UTF-8", params.getToCharSet());
	}
}
