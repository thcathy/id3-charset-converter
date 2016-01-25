package thc.id3.charset.convert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.apache.commons.cli.MissingArgumentException;
import org.junit.Test;

import thc.id3.charset.convert.Parameters;

public class ParametersTest {
	
	@Test(expected=MissingArgumentException.class)
	public void givenNoArg_shouldRaiseException() throws Exception {
		try {
			Parameters.parse(new String[] {});
		} catch (MissingArgumentException m) {
			assertEquals("Require argument input file or input folder", m.getMessage());
			throw m;
		}
	}
	
	@Test
	public void givenHelpOption_shouldReturnTrue() throws Exception {
		String[] args = new String[] {"-h"};
		assertTrue(Parameters.parse(args).isHelp());
		
		args = new String[] {"-hi", "abc"};
		assertTrue(Parameters.parse(args).isHelp());
		
		args = new String[] {"--help","--input-file", "abc"};
		assertTrue(Parameters.parse(args).isHelp());
	}
	
	@Test
	public void givenNoHelpOption_shouldReturnFalse() throws Exception {
		String[] args = new String[] {"-i", "abc"};
		assertEquals(false, Parameters.parse(args).isHelp());
	}
	
	@Test(expected=MissingArgumentException.class)
	public void givenOptionInputFileWithoutArgs_shouldRaiseException() throws Exception {
		try {
			Parameters.parse(new String[] {"-i"});
		} catch (MissingArgumentException m) {
			assertEquals("Missing argument for option: i", m.getMessage());
			throw m;
		}
	}
	
	@Test
	public void givenOptionInputFileWithArgs_shouldReturnArgs() throws Exception {
		final String inputFileName = "abc";
		Parameters params = Parameters.parse(new String[] {"-i", inputFileName});
		assertEquals(inputFileName, params.getInputFile().get());		
				
		params = Parameters.parse(new String[] {"--input-file", inputFileName});
		assertEquals(inputFileName, params.getInputFile().get());
	}
	
	@Test
	public void givenNoOptionInputFile_shouldReturnNoInputFile() throws Exception {		
		Parameters params = Parameters.parse(new String[] {"-I", "folder"});
		assertEquals(Optional.empty(), params.getInputFile());		
	}
	
	@Test(expected=MissingArgumentException.class)
	public void givenOptionOutputFileWithoutArgs_shouldRaiseException() throws Exception {
		Parameters.parse(new String[] {"-o"});
	}
	
	@Test
	public void givenOptionOutputFileWithArgs_shouldReturnArgs() throws Exception {
		final String outputFileName = "src/test/java/Id3EncodingConverterCommandTest.java";
		Parameters params = Parameters.parse(new String[] {"-ho", outputFileName});
		assertEquals(outputFileName, params.getOutputFile().get());		
				
		params = Parameters.parse(new String[] {"-h", "--output-file", outputFileName});
		assertEquals(outputFileName, params.getOutputFile().get());
	}
	
	@Test
	public void givenNoOptionOutputFile_shouldReturnNoOutputFile() throws Exception {		
		Parameters params = Parameters.parse(new String[] {"-i", "abc"});
		assertEquals(Optional.empty(), params.getOutputFile());
	}
	
	
	@Test
	public void givenInputFolderWithArgs_shouldReturnArgs() throws Exception {
		final String inputFolder = "/tmp/mp3";
		Parameters params = Parameters.parse(new String[] {"-I", inputFolder});
		assertEquals(inputFolder, params.getInputFolder().get());		
				
		params = Parameters.parse(new String[] {"--input-folder", inputFolder});
		assertEquals(inputFolder, params.getInputFolder().get());
	}
	
	@Test
	public void givenNoOutputFolderOption_shouldReturnNoOutputFolder() throws Exception {		
		Parameters params = Parameters.parse(new String[] {"-h"});
		assertEquals(Optional.empty(), params.getOutputFolder());
	}
	
	@Test
	public void givenOutputFolderWithArgs_shouldReturnArgs() throws Exception {
		final String outputFolder = "/tmp/output";
		Parameters params = Parameters.parse(new String[] {"-hO", outputFolder});
		assertEquals(outputFolder, params.getOutputFolder().get());		
				
		params = Parameters.parse(new String[] {"-h", "--output-folder", outputFolder});
		assertEquals(outputFolder, params.getOutputFolder().get());
	}
	
	@Test
	public void givenNoInputFolderOption_shouldReturnNoInputFolder() throws Exception {		
		Parameters params = Parameters.parse(new String[] {"-i", "folder"});
		assertEquals(Optional.empty(), params.getInputFolder());
	}
	
	@Test
	public void givenTestOption_shouldReturnTrue() throws Exception {
		String[] args = new String[] {"-ti", "abc"};
		assertTrue(Parameters.parse(args).isTest());
	}
	
	@Test
	public void givenNoTestOption_shouldReturnFalse() throws Exception {
		String[] args = new String[] {"-i", "abc"};
		assertEquals(false, Parameters.parse(args).isTest());
	}
	
	@Test
	public void givenFromCharSet_shouldReturnArgsOrDefault() throws Exception {
		final String fromCharSet = "big5";
		Parameters params = Parameters.parse(new String[] {"-hc", fromCharSet});
		assertEquals(fromCharSet, params.getFromCharSet());
		
		params = Parameters.parse(new String[] {"-h"});
		assertEquals("ISO-8859-1", params.getFromCharSet());
	}

	@Test
	public void givenToCharSet_shouldReturnArgsOrDefault() throws Exception {
		final String toCharSet = "big5";
		Parameters params = Parameters.parse(new String[] {"-hC", toCharSet});
		assertEquals(toCharSet, params.getToCharSet());
		
		params = Parameters.parse(new String[] {"-h"});
		assertEquals("UTF-8", params.getToCharSet());
	}
}
