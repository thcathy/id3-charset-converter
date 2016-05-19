package thc.id3.charset.convert;

import static com.mpatric.mp3agic.AbstractID3v2Tag.ID_ALBUM;
import static com.mpatric.mp3agic.AbstractID3v2Tag.ID_ALBUM_ARTIST;
import static com.mpatric.mp3agic.AbstractID3v2Tag.ID_ARTIST;
import static com.mpatric.mp3agic.AbstractID3v2Tag.ID_COMPOSER;
import static com.mpatric.mp3agic.AbstractID3v2Tag.ID_COPYRIGHT;
import static com.mpatric.mp3agic.AbstractID3v2Tag.ID_ENCODER;
import static com.mpatric.mp3agic.AbstractID3v2Tag.ID_ORIGINAL_ARTIST;
import static com.mpatric.mp3agic.AbstractID3v2Tag.ID_PUBLISHER;
import static com.mpatric.mp3agic.AbstractID3v2Tag.ID_TITLE;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.mpatric.mp3agic.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.text.CharsetDetector;

public class ConvertService {
	private static Logger log = LoggerFactory.getLogger(ConvertService.class);
	
	public static final String DEFAULT_ID3V1_CHARSET = "ISO-8859-1";
	public static String TO_CHARSET = "UTF-8";

	private static List<String> tagsToConvert = Arrays.asList(ID_ARTIST, ID_TITLE, ID_ALBUM, ID_COMPOSER, ID_PUBLISHER,
			ID_ORIGINAL_ARTIST, ID_ALBUM_ARTIST, ID_ENCODER, ID_COPYRIGHT);

	public void convert(String source, String target, Optional<String> inputCharset, boolean isSave) throws Exception {
		log.info("convert: path [{} > {}], encoding [{}]", source, target, inputCharset.orElse(""));

		Collection<File> files = collectFiles(source);		
		TargetPathFactory tgtPathFactory = TargetPathFactory.getFactory(source, target, TO_CHARSET);
		tgtPathFactory.createFolder();

		files.stream()
				.flatMap(f -> openMp3File(f))				
				.map(mp3 -> convertTagsData(mp3, inputCharset))
				.filter(x -> isSave)
				.forEach(convertedMp3 -> save(convertedMp3, tgtPathFactory.makeFilePath(convertedMp3.getFilename())));
	}

	private Collection<File> collectFiles(String source) {
		try {			
			return FileUtils.listFiles(new File(source), new String[] { "mp3" }, true);
		} catch (Exception e) {
			return Arrays.asList(new File(source));
		}
	}

	private Mp3File save(Mp3File mp3, String outputFile) {
		log.info("Saving mp3 to {}", outputFile);

		try {
			mp3.save(outputFile);
			mp3 = new Mp3File(outputFile);
		} catch (ArrayIndexOutOfBoundsException e) {
			try {
				mp3.getId3v2Tag().clearAlbumImage();
				mp3.save(outputFile);
				mp3 = new Mp3File(outputFile);
			} catch (Exception e1) {
				log.error("Cannot save mp3: " + outputFile, e);
			}
		} catch (Exception e) {
			log.error("Cannot save mp3: " + outputFile, e);
		}
		return mp3;
	}
	
	private Mp3File convertTagsData(Mp3File mp3, Optional<String> inputCharset) {
		if (mp3.hasId3v2Tag())
			return convertV2TagsData(mp3, inputCharset);
		else if (mp3.hasId3v1Tag())
			return convertV1TagsData(mp3, inputCharset);
		
		throw new UnsupportedOperationException("Cannot process mp3 without Id3 tag");
	}

	private Mp3File convertV2TagsData(Mp3File mp3, Optional<String> inputCharset) {
		try {			
			final ID3v2 id3v2Tag = mp3.getId3v2Tag();
			String charset = inputCharset.orElseGet(() -> detectCharsetByV2Title(id3v2Tag));
			tagsToConvert.forEach(tag -> decodeText(id3v2Tag, tag, charset));
			logTag(id3v2Tag);

			return mp3;
		} catch (Exception e) {
			log.warn("Cannot process mp3: " + mp3.getFilename(), e);
			return null;
		}
	}

	public Mp3File convertV1TagsData(Mp3File mp3, Optional<String> inputCharset) {
		try {
			final ID3v1 v1Tag = mp3.getId3v1Tag();
			String charset = inputCharset.orElse(DEFAULT_ID3V1_CHARSET);
			
			final ID3v2 v2Tag = new ID3v24Tag();
			v2Tag.setTitle(convertEncoding(v1Tag.getTitle(), charset));
			v2Tag.setAlbum(convertEncoding(v1Tag.getAlbum(), charset));
			v2Tag.setArtist(convertEncoding(v1Tag.getArtist(), charset));

			logTag(v2Tag);
			mp3.removeId3v1Tag();
			mp3.setId3v2Tag(v2Tag);
			return mp3;
		} catch (Exception e) {
			log.warn("Cannot process mp3: " + mp3.getFilename(), e);
			return null;
		}
	}
	
	private String convertEncoding(String input, String inputCharset) throws UnsupportedEncodingException {
		return new String(new String(input.getBytes(DEFAULT_ID3V1_CHARSET),inputCharset).getBytes(), TO_CHARSET);
	}

	private Stream<Mp3File> openMp3File(File f) {
		log.info("convert file: {}", f.getAbsoluteFile());
		try {
			Mp3File mp3 = new Mp3File(f);
			return Stream.of(mp3);			
		} catch (Exception e) {
			log.warn("Cannot process mp3: " + f.getAbsolutePath(), e);
			return Stream.empty();
		}		
	}

	private String detectCharsetByV2Title(ID3v2 id3v2Tag) {
		return detectCharset(ArrayUtils.remove(id3v2Tag.getFrameSets().get(ID_TITLE).getFrames().get(0).getData(), 0));
	}
	
	private String detectCharset(byte[] input) {
		String charset =  new CharsetDetector()
								.setText(input)
								.detect().getName();
		log.info("Auto Detected Charset: {}", charset);
		return charset;
	}

	private void logTag(ID3v2 tag) {		
		log.info("Artist: {}", tag.getArtist());
		log.info("Title: {}", tag.getTitle());
		log.info("Album: {}", tag.getAlbum());
		log.info("Compose: {}", tag.getComposer());
		log.info("Publisher: {}", tag.getPublisher());
		log.info("Original Artist: {}", tag.getOriginalArtist());
		log.info("Album Artist: {}", tag.getAlbumArtist());
		log.info("Encoder: {}", tag.getEncoder());
		log.info("Copyright: {}", tag.getCopyright());
	}

	private void decodeText(ID3v2 id3v2Tag, String tagId, String charset) {
		ID3v2FrameSet set = id3v2Tag.getFrameSets().get(tagId);
		if (set == null)
			return;
		ID3v2Frame frame = id3v2Tag.getFrameSets().get(tagId).getFrames().get(0);

		try {
			byte[] utfBytes = new String(ArrayUtils.remove(frame.getData(), 0), charset).trim().getBytes(TO_CHARSET); // remove leading byte and convert
			frame.setData(ArrayUtils.add(utfBytes, 0, EncodedText.TEXT_ENCODING_UTF_8));
		} catch (UnsupportedEncodingException e) {
			log.error("Cannot decode tag {} from {} to {}", tagId, charset, TO_CHARSET);
		}
	}
}
