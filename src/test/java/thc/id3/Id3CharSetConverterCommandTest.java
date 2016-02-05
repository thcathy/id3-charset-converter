package thc.id3;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import thc.id3.charset.convert.ConvertService;

@RunWith(MockitoJUnitRunner.class)
public class Id3CharSetConverterCommandTest {
	
	@Mock
	HelpFormatter formatter;
	ConvertService convertService = new ConvertService();
	
	@Test
	public void givenArgsWithHelp_shouldPrintUsage() throws Exception {
		new Id3CharSetConverterCommand(formatter, convertService).run(new String[] {"-h"});
		
		verify(formatter, times(1)).printHelp(Mockito.anyString(), Mockito.anyObject());
	}
	
	@Test
	public void givenNoArgs_shouldPrintUsage() throws Exception {
		new Id3CharSetConverterCommand(formatter, convertService).run();
		verify(formatter, times(1)).printHelp(Mockito.anyString(), Mockito.anyObject());
	}
	
	@Test
	public void givenHelpAndInputFile_shouldPrintHelpOnly() throws Exception {
		new Id3CharSetConverterCommand(formatter, convertService).run(new String[] {"-h", "abc"});
		
		verify(formatter, times(1)).printHelp(Mockito.anyString(), Mockito.anyObject());
	}
	
	@Test
	public void givenSourceFileAndTargetFile_shouldConvertId3AndSaveToTargetFile() throws Exception {
		URL fileUri = this.getClass().getClassLoader().getResource("mp3/big5.mp3");
		File f = new File(fileUri.getPath());		
		String outputFilePath = f.getParent() + "/converted.mp3";
		
		new Id3CharSetConverterCommand(formatter, convertService).run(new String[] {"-c", "big5", fileUri.getPath(), outputFilePath});		
		checkMp3Tag(outputFilePath);
				
		FileUtils.forceDelete(new File(outputFilePath)); // clean up
	}

	private void checkMp3Tag(String outputFilePath) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3File mp3 = new Mp3File(outputFilePath);
		ID3v2 tag = mp3.getId3v2Tag();
		assertEquals("李克勤", tag.getArtist());
		assertEquals("我克勤", tag.getAlbum());
		assertEquals("戀愛為何物 (feat. AGA)", tag.getTitle());
	}
	
	@Test
	public void givenSourceFileWithoutTargetFile_shouldConvertId3AndSaveToDefaultTargetFile() throws Exception {
		URL fileUri = this.getClass().getClassLoader().getResource("mp3/big5.mp3");
		File f = new File(fileUri.getPath());		
		String outputFilePath = f.getParent() + "/big5_UTF-8.mp3";
		
		new Id3CharSetConverterCommand(formatter, convertService).run(new String[] {"-c", "big5", fileUri.getPath()});		
		checkMp3Tag(outputFilePath);
				
		FileUtils.forceDelete(new File(outputFilePath)); // clean up
	}
	
	@Test
	public void givenSourceFolderAndTargetFolder_shouldConvertAllFile() throws Exception {
		URL fileUri = this.getClass().getClassLoader().getResource("mp3/big5.mp3");
		String sourceFolder = new File(fileUri.getPath()).getParent();
		String targetFolder = "tmp";
		
		new Id3CharSetConverterCommand(formatter, convertService).run(new String[] {"-c", "big5", sourceFolder, targetFolder});
		checkMp3Tag(targetFolder + "/big5_UTF-8.mp3");
		checkMp3Tag(targetFolder + "/sub_folder_big5_UTF-8.mp3");
		
		// clean up
		FileUtils.forceDelete(new File(targetFolder));
	}
	
	@SuppressWarnings("unchecked")
	@Test
    public void testLogging() throws Exception {
		Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		final Appender mockAppender = Mockito.mock(Appender.class);
		Mockito.when(mockAppender.getName()).thenReturn("MOCK");
		root.addAppender(mockAppender);

		URL fileUri = this.getClass().getClassLoader().getResource("mp3/big5.mp3");
		File f = new File(fileUri.getPath());
		
		String outputFilePath = f.getParent() + "/big5_utf8.mp3";
		
		new Id3CharSetConverterCommand(formatter, convertService).run(new String[] {"-c", "big5", fileUri.getPath(), outputFilePath});
		FileUtils.forceDelete(new File(outputFilePath));

	    verify(mockAppender).doAppend(Mockito.argThat(new ArgumentMatcher() {
		      @Override
		      public boolean matches(final Object argument) {
		        return ((LoggingEvent)argument).getFormattedMessage().contains("convertTagsText file: /Users/thcathy/git/id3-charset-converter/bin/mp3/big5.mp3");
		      }
	    }));
	}
}
