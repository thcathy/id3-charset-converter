package thc.id3.charset.detect;

import java.net.URL;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import com.mpatric.mp3agic.AbstractID3v2Tag;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;

public class CharsetDetectorTest {
	@Test
	public void basicTest() throws Exception {
		URL fileUri = this.getClass().getClassLoader().getResource("mp3/big5.mp3");		
		Mp3File mp3 = new Mp3File(fileUri.getFile());
		final ID3v2 id3v2Tag = mp3.getId3v2Tag();
		
		CharsetDetector detector = new CharsetDetector();
	    detector.setText(ArrayUtils.remove(id3v2Tag.getFrameSets().get(AbstractID3v2Tag.ID_ARTIST).getFrames().get(0).getData(), 0));	    
	    CharsetMatch match = detector.detect();
	    System.out.println(match.getLanguage());
	    System.out.println(match.getConfidence());
	    System.out.println(match.getName());
	    System.out.println(match.getString());
	    
	    // convert
	    System.out.println(new CharsetDetector().getString("黃".getBytes(), "ISO-2022-JP"));
	}
}
