package thc.id3;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.net.URL;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;

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
	public void givenInputFileAndOutputFile_shouldConvertId3AndSaveNewFile() throws Exception {
		URL fileUri = this.getClass().getClassLoader().getResource("mp3/big5.mp3");
		File f = new File(fileUri.getPath());
		
		String outputFilePath = f.getParent() + "/big5_utf8.mp3";
		
		new Id3CharSetConverterCommand(formatter, convertService).run(new String[] {"-c", "big5", fileUri.getPath(), outputFilePath});
		
		Mp3File mp3 = new Mp3File(outputFilePath);
		ID3v2 tag = mp3.getId3v2Tag();
		assertEquals("李克勤", tag.getArtist());
		assertEquals("我克勤", tag.getAlbum());
		assertEquals("戀愛為何物 (feat. AGA)", tag.getTitle());
		
		// clean up
		FileUtils.forceDelete(new File(outputFilePath));
	}
}
