package thc.id3;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.apache.commons.cli.MissingArgumentException;
import org.junit.Test;

public class Id3CharSetConvertorParametersTest {
	
	@Test(expected=MissingArgumentException.class)
	public void givenNoArg_shouldRaiseException() throws Exception {
		try {
			Id3CharSetConvertorParameters.parse(new String[] {});
		} catch (MissingArgumentException m) {
			assertEquals("Require argument input file or input folder", m.getMessage());
			throw m;
		}
	}
	
	@Test
	public void givenHelpOption_shouldReturnTrue() throws Exception {
		String[] args = new String[] {"-h"};
		assertTrue(Id3CharSetConvertorParameters.parse(args).isHelp());
		
		args = new String[] {"-hi", "abc"};
		assertTrue(Id3CharSetConvertorParameters.parse(args).isHelp());
		
		args = new String[] {"--help","--input-file", "abc"};
		assertTrue(Id3CharSetConvertorParameters.parse(args).isHelp());
	}
	
	@Test
	public void givenNoHelpOption_shouldReturnFalse() throws Exception {
		String[] args = new String[] {"-i", "abc"};
		assertEquals(false, Id3CharSetConvertorParameters.parse(args).isHelp());
	}
	
	@Test(expected=MissingArgumentException.class)
	public void givenOptionInputFileWithoutArgs_shouldRaiseException() throws Exception {
		try {
			Id3CharSetConvertorParameters.parse(new String[] {"-i"});
		} catch (MissingArgumentException m) {
			assertEquals("Missing argument for option: i", m.getMessage());
			throw m;
		}
	}
	
	@Test
	public void givenOptionInputFileWithArgs_shouldReturnArgs() throws Exception {
		final String inputFileName = "abc";
		Id3CharSetConvertorParameters params = Id3CharSetConvertorParameters.parse(new String[] {"-i", inputFileName});
		assertEquals(inputFileName, params.getInputFile().get());		
				
		params = Id3CharSetConvertorParameters.parse(new String[] {"--input-file", inputFileName});
		assertEquals(inputFileName, params.getInputFile().get());
	}
	
	@Test
	public void givenNoOptionInputFile_shouldReturnNoInputFile() throws Exception {		
		Id3CharSetConvertorParameters params = Id3CharSetConvertorParameters.parse(new String[] {"-I", "folder"});
		assertEquals(Optional.empty(), params.getInputFile());		
	}
	
	@Test(expected=MissingArgumentException.class)
	public void givenOptionOutputFileWithoutArgs_shouldRaiseException() throws Exception {
		Id3CharSetConvertorParameters.parse(new String[] {"-o"});
	}
	
	@Test
	public void givenOptionOutputFileWithArgs_shouldReturnArgs() throws Exception {
		final String outputFileName = "src/test/java/Id3EncodingConvertorCommandTest.java";
		Id3CharSetConvertorParameters params = Id3CharSetConvertorParameters.parse(new String[] {"-o", outputFileName});
		assertEquals(outputFileName, params.getOutputFile().get());		
				
		params = Id3CharSetConvertorParameters.parse(new String[] {"--output-file", outputFileName});
		assertEquals(outputFileName, params.getOutputFile().get());
	}
	
	@Test
	public void givenNoOptionOutputFile_shouldReturnNoOutputFile() throws Exception {		
		Id3CharSetConvertorParameters params = Id3CharSetConvertorParameters.parse(new String[] {"-i", "abc"});
		assertEquals(Optional.empty(), params.getOutputFile());
	}
	
	@Test
	public void givenInputFolderOption_shouldReturnInputputFolder() throws Exception {
		final String inputFolder = "/tmp/mp3";
		Id3CharSetConvertorParameters params = Id3CharSetConvertorParameters.parse(new String[] {"-I", inputFolder});
		assertEquals(inputFolder, params.getInputFolder().get());		
				
		params = Id3CharSetConvertorParameters.parse(new String[] {"--input-folder", inputFolder});
		assertEquals(inputFolder, params.getInputFolder().get());
	}
	
	@Test
	public void givenInputFolderWithArgs_shouldReturnArgs() throws Exception {
		final String inputFolder = "/tmp/mp3";
		Id3CharSetConvertorParameters params = Id3CharSetConvertorParameters.parse(new String[] {"-I", inputFolder});
		assertEquals(inputFolder, params.getInputFolder().get());		
				
		params = Id3CharSetConvertorParameters.parse(new String[] {"--input-folder", inputFolder});
		assertEquals(inputFolder, params.getInputFolder().get());
	}
	
	@Test
	public void givenNoOutputFolderOption_shouldReturnNoOutputFolder() throws Exception {		
		Id3CharSetConvertorParameters params = Id3CharSetConvertorParameters.parse(new String[] {});
		assertEquals(Optional.empty(), params.getOutputFolder());
	}
	
	@Test
	public void givenOutputFolderOption_shouldReturnOutputFolder() throws Exception {
		final String outputFolder = "/tmp/output";
		Id3CharSetConvertorParameters params = Id3CharSetConvertorParameters.parse(new String[] {"-O", outputFolder});
		assertEquals(outputFolder, params.getOutputFolder().get());		
				
		params = Id3CharSetConvertorParameters.parse(new String[] {"--output-folder", outputFolder});
		assertEquals(outputFolder, params.getOutputFolder().get());
	}
	
	@Test
	public void givenOutputFolderWithArgs_shouldReturnArgs() throws Exception {
		final String outputFolder = "/tmp/output";
		Id3CharSetConvertorParameters params = Id3CharSetConvertorParameters.parse(new String[] {"-O", outputFolder});
		assertEquals(outputFolder, params.getOutputFolder().get());		
				
		params = Id3CharSetConvertorParameters.parse(new String[] {"--output-folder", outputFolder});
		assertEquals(outputFolder, params.getOutputFolder().get());
	}
	
	@Test
	public void givenNoInputFolderOption_shouldReturnNoInputFolder() throws Exception {		
		Id3CharSetConvertorParameters params = Id3CharSetConvertorParameters.parse(new String[] {"-i", "folder"});
		assertEquals(Optional.empty(), params.getInputFolder());
	}
	
	@Test
	public void givenTestOption_shouldReturnTrue() throws Exception {
		String[] args = new String[] {"-ti", "abc"};
		assertTrue(Id3CharSetConvertorParameters.parse(args).isTest());
	}
	
	@Test
	public void givenNoTestOption_shouldReturnFalse() throws Exception {
		String[] args = new String[] {"-i", "abc"};
		assertEquals(false, Id3CharSetConvertorParameters.parse(args).isTest());
	}
}
