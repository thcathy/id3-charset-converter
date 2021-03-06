package thc.id3;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import thc.id3.charset.convert.ConvertService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class Id3CharSetConverterCommandTest {
	
	@Mock
	HelpFormatter formatter;
	ConvertService convertService = new ConvertService();
	
	@Test
	public void givenArgsWithHelp_shouldPrintUsage() throws Exception {
		new Id3CharSetConverterCommand(formatter, convertService).run(new String[] {"-h"});
		
		verify(formatter, times(1)).printHelp(Mockito.anyString(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject());
	}
	
	@Test
	public void givenNoArgs_shouldPrintUsage() throws Exception {
		new Id3CharSetConverterCommand(formatter, convertService).run();
		verify(formatter, times(1)).printHelp(Mockito.anyString(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject());
	}
	
	@Test
	public void givenHelpAndInputFile_shouldPrintHelpOnly() throws Exception {
		new Id3CharSetConverterCommand(formatter, convertService).run(new String[] {"-h", "abc"});
		
		verify(formatter, times(1)).printHelp(Mockito.anyString(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject());
	}
	
	@Test
	public void givenSourceFileAndTargetFile_shouldConvertId3AndSaveToTargetFile() throws Exception {
		URL fileUri = this.getClass().getClassLoader().getResource("mp3/big5.mp3");
		File f = new File(fileUri.getPath());		
		String outputFilePath = f.getParent() + "/converted.mp3";
		
		new Id3CharSetConverterCommand(formatter, convertService).run(new String[] {"-c", "big5", fileUri.getPath(), outputFilePath});		
		checkBig5Mp3Tag(outputFilePath);
				
		FileUtils.forceDelete(new File(outputFilePath)); // clean up
	}

	private void checkBig5Mp3Tag(String outputFilePath) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3File mp3 = new Mp3File(outputFilePath);
		ID3v2 tag = mp3.getId3v2Tag();
		assertEquals("李克勤", tag.getArtist());
		assertEquals("我克勤", tag.getAlbum());
		assertEquals("戀愛為何物 (feat. AGA)", tag.getTitle());
	}
	
	private void checkBig5bMp3Tag(String outputFilePath) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3File mp3 = new Mp3File(outputFilePath);
		ID3v2 tag = mp3.getId3v2Tag();
		assertEquals("謝安琪", tag.getArtist());
		assertEquals("山林道", tag.getTitle());
	}
	
	@Test
	public void givenSourceFileWithoutTargetFile_shouldConvertId3AndSaveToDefaultTargetFile() throws Exception {
		URL fileUri = this.getClass().getClassLoader().getResource("mp3/big5.mp3");
		File f = new File(fileUri.getPath());		
		String outputFilePath = f.getParent() + "/big5_UTF-8.mp3";
		
		new Id3CharSetConverterCommand(formatter, convertService).run(new String[] {"-c", "big5", fileUri.getPath()});		
		checkBig5Mp3Tag(outputFilePath);
				
		FileUtils.forceDelete(new File(outputFilePath)); // clean up
	}
	
	@Test
	public void givenSourceFileAndTargetIsFolder_shouldConvertId3AndSaveToTargetFolder() throws Exception {
		URL fileUri = this.getClass().getClassLoader().getResource("mp3/big5.mp3");
		String targetFolder = "tmp";
		String targetFilePath = targetFolder + "/big5_UTF-8.mp3";
		
		new Id3CharSetConverterCommand(formatter, convertService).run(new String[] {"-c", "big5", fileUri.getPath(), targetFolder});		
		checkBig5Mp3Tag(targetFilePath);
				
		FileUtils.forceDelete(new File(targetFolder)); // clean up
	}
	
	@Test
	public void givenSourceFolderWithoutTargetFolder_shouldConvertId3AndSaveToSourceFolder() throws Exception {
		URL fileUri = this.getClass().getClassLoader().getResource("mp3/sub_folder/sub_folder_big5.mp3");
		String sourceFolder = new File(fileUri.getPath()).getParent();
				
		try {
			new Id3CharSetConverterCommand(formatter, convertService).run(new String[] {"-c", "big5", sourceFolder});			
			checkBig5Mp3Tag(sourceFolder + "/sub_folder_big5_UTF-8.mp3");
		} finally {
			FileUtils.forceDelete(new File(sourceFolder + "/sub_folder_big5_UTF-8.mp3"));
		}
	}
	
	@Test
	public void givenSourceFolderAndTargetFile_shouldConvertId3AndSaveToTargetFileParent() throws Exception {
		URL fileUri = this.getClass().getClassLoader().getResource("mp3/big5.mp3");
		String sourceFolder = new File(fileUri.getPath()).getParent();
		String targetFolder = "tmp";
		String targetFilePath = targetFolder + "/big5_UTF-8.mp3";
				
		try {
			new Id3CharSetConverterCommand(formatter, convertService).run(new String[] {"-c", "big5", sourceFolder, targetFilePath});
			checkBig5Mp3Tag(targetFolder + "/big5_UTF-8.mp3");
			checkBig5Mp3Tag(targetFolder + "/sub_folder_big5_UTF-8.mp3");
		} finally {
			FileUtils.forceDelete(new File(targetFolder));	// cleanup
		}
	}
	
	@Test
	public void givenSourceFolderAndTargetFolder_shouldConvertAllFile() throws Exception {
		URL fileUri = this.getClass().getClassLoader().getResource("mp3/big5.mp3");
		String sourceFolder = new File(fileUri.getPath()).getParent();
		String targetFolder = "tmp";
		
		try {
			new Id3CharSetConverterCommand(formatter, convertService).run(new String[] {"-c", "big5", sourceFolder, targetFolder});
			checkBig5Mp3Tag(targetFolder + "/big5_UTF-8.mp3");
			checkBig5Mp3Tag(targetFolder + "/sub_folder_big5_UTF-8.mp3");
		} finally {
			FileUtils.forceDelete(new File(targetFolder)); // cleanup
		}
	}
	
	@Test
	public void givenTestWithSourceFolder_shouldNotSaveFile() throws Exception {
		URL fileUri = this.getClass().getClassLoader().getResource("mp3/big5.mp3");
		String sourceFolder = new File(fileUri.getPath()).getParent();
		String targetFolder = "tmp";
		
		try {
			new Id3CharSetConverterCommand(formatter, convertService).run(new String[] {"-tc", "big5", sourceFolder, targetFolder});
			assertFalse(new File(targetFolder + "/big5_UTF-8.mp3").exists());
			assertFalse(new File(targetFolder + "/sub_folder_big5_UTF-8.mp3").exists());
		} finally {
			FileUtils.forceDelete(new File(targetFolder));	
		}
	}
	
	@Test
	public void givenSourceFileWithoutCharset_shouldConvertByAutoDetect() throws Exception {
		URL fileUri = this.getClass().getClassLoader().getResource("mp3/big5_b.mp3");
		String targetFolder = "tmp";
		String targetFilePath = targetFolder + "/big5_b_UTF-8.mp3";
		
		try {
			new Id3CharSetConverterCommand(formatter, convertService).run(new String[]{fileUri.getPath(), targetFolder});
			checkBig5bMp3Tag(targetFilePath);
		} finally {
			FileUtils.forceDelete(new File(targetFolder)); // clean up
		}
	}
	
	@Test
	public void givenMp3WithID3TagV1_shouldConvertToTagV2() throws Exception {
		URL fileUri = this.getClass().getClassLoader().getResource("mp3/ID3Tagv1.mp3");
		File f = new File(fileUri.getPath());
		String outputFilePath = f.getParent() + "/converted.mp3";

		try {
			new Id3CharSetConverterCommand(formatter, convertService).run(new String[]{"-c", "GB18030", fileUri.getPath(), outputFilePath});
			checkGB18030Mp3Tag(outputFilePath);
		} finally {
			FileUtils.forceDelete(new File(outputFilePath)); // clean up
		}
	}

	@Test
	public void givenParameter_d_shouldConvertFileLastModifiedWithinInputDay() throws Exception {
		URL newFileUri = this.getClass().getClassLoader().getResource("mp3/big5.mp3");
		URL oldFileUri = this.getClass().getClassLoader().getResource("mp3/big5_b.mp3");
		new File(newFileUri.toURI()).setLastModified(new Date().getTime() - 10000); // 10s before
		new File(oldFileUri.toURI()).setLastModified(new Date().getTime() - 259200000); // 3 days before
		String sourceFolder = new File(newFileUri.getPath()).getParent();
		String targetFolder = "tmp";

		try {
			new Id3CharSetConverterCommand(formatter, convertService).run(new String[] {"-c", "big5", "-d", "2", sourceFolder, targetFolder});
			checkBig5Mp3Tag(targetFolder + "/big5_UTF-8.mp3");
			assertFalse(new File(targetFolder + "/big5_b_UTF-8.mp3").exists());
		} finally {
			FileUtils.forceDelete(new File(targetFolder)); // cleanup
		}
	}
	
	private void checkGB18030Mp3Tag(String outputFilePath) throws InvalidDataException, IOException, UnsupportedTagException {
		Mp3File mp3 = new Mp3File(outputFilePath);
		ID3v2 tag = mp3.getId3v2Tag();
		assertEquals("曹格", tag.getArtist());
		assertEquals("我们是朋友", tag.getAlbum());
		assertEquals("美丽人生", tag.getTitle());
	}
}
