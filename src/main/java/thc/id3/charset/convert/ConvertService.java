package thc.id3.charset.convert;

import static com.mpatric.mp3agic.AbstractID3v2Tag.ID_ALBUM;
import static com.mpatric.mp3agic.AbstractID3v2Tag.ID_ALBUM_ARTIST;
import static com.mpatric.mp3agic.AbstractID3v2Tag.ID_ARTIST;
import static com.mpatric.mp3agic.AbstractID3v2Tag.ID_COMPOSER;
import static com.mpatric.mp3agic.AbstractID3v2Tag.ID_ENCODER;
import static com.mpatric.mp3agic.AbstractID3v2Tag.ID_ORIGINAL_ARTIST;
import static com.mpatric.mp3agic.AbstractID3v2Tag.ID_PUBLISHER;
import static com.mpatric.mp3agic.AbstractID3v2Tag.ID_TITLE;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mpatric.mp3agic.EncodedText;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v2Frame;
import com.mpatric.mp3agic.ID3v2FrameSet;
import com.mpatric.mp3agic.Mp3File;

public class ConvertService {
	private static Logger log = LoggerFactory.getLogger(ConvertService.class);

	private static List<String> tagsToConvert = Arrays.asList(ID_ARTIST, ID_TITLE, ID_ALBUM, ID_COMPOSER, ID_PUBLISHER,
			ID_ORIGINAL_ARTIST, ID_ALBUM_ARTIST, ID_ENCODER);

	public void convert(String source, String target, String fromEncoding, String toEncoding) throws Exception {
		log.info("convert: path [{} > {}], encoding [{} > {}]", source, target, fromEncoding, toEncoding);

		Collection<File> files = collectFiles(source);
		createTargetFolderIfNeeded(target);
		TargetFilePathMaker tgtPathMaker = createTargetFilePathMaker(source, target, toEncoding);

		files.stream().map(f -> convertTagsText(f, fromEncoding, toEncoding))
				.forEach(convertedMp3 -> save(convertedMp3, tgtPathMaker.makeFilePath(convertedMp3.getFilename())));
	}

	private Collection<File> collectFiles(String source) {
		try {
			return FileUtils.listFiles(new File(source), new String[] { "mp3" }, true);
		} catch (Exception e) {
			return Arrays.asList(new File(source));
		}
	}

	private void createTargetFolderIfNeeded(String target) throws Exception {
		if (isFile(target))
			FileUtils.forceMkdir(new File(target).getParentFile());
		else
			FileUtils.forceMkdir(new File(target));
	}

	private static boolean isFile(String target) {
		return target.endsWith(".mp3");
	}

	public String convert(String action, String inputFolder, String outputFolder, String fromEncoding,
			String toEncoding) throws IOException {

		log.debug("convert: action[{}], folder[{} > {}], encoding[{} > {}]", action, inputFolder, outputFolder,
				fromEncoding, toEncoding);

		File folderObj = new File(inputFolder);
		if (!folderObj.isDirectory())
			inputFolder = folderObj.getParent();
		Collection<File> files = FileUtils.listFiles(new File(inputFolder), new String[] { "mp3" }, true);

		List<Map<String, String>> tags = files.stream().map(f -> convertTagsText(f, fromEncoding, toEncoding))
				.filter(f -> f != null).map(f -> save("save".equalsIgnoreCase(action), f, outputFolder))
				.map(f -> convertToMap(f)).collect(Collectors.toList());
		return "id3tag";
	}

	private Mp3File save(Mp3File mp3, String outputFile) {
		log.info("Saving {}", outputFile);

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

	private Mp3File save(boolean isSave, Mp3File mp3, String outputFolder) {
		if (isSave) {
			File outputFolderObj = new File(outputFolder);
			String newFilePath = outputFolder + File.separator + new File(mp3.getFilename()).getName();
			try {
				if (!outputFolderObj.exists())
					Files.createDirectories(outputFolderObj.toPath());

				mp3.save(newFilePath);
				mp3 = new Mp3File(newFilePath);
			} catch (ArrayIndexOutOfBoundsException e) {
				try {
					mp3.getId3v2Tag().clearAlbumImage();
					mp3.save(newFilePath);
					mp3 = new Mp3File(newFilePath);
				} catch (Exception e1) {
					log.error("Cannot save mp3: " + mp3.getFilename(), e);
				}
			} catch (Exception e) {
				log.error("Cannot save mp3: " + mp3.getFilename(), e);
			}
		}

		return mp3;
	}

	private Mp3File convertTagsText(File f, String fromEncoding, String toEncoding) {
		log.debug("convertTagsText file: {}", f.getAbsoluteFile());

		try {
			Mp3File mp3 = new Mp3File(f);
			final ID3v2 id3v2Tag = mp3.getId3v2Tag();
			tagsToConvert.forEach(tag -> decodeText(id3v2Tag, tag, fromEncoding, toEncoding));
			return mp3;
		} catch (Exception e) {
			log.warn("Cannot process mp3: " + f.getAbsolutePath(), e);
			return null;
		}
	}

	private Map<String, String> convertToMap(Mp3File f) {
		Map<String, String> tags = new HashMap<>();
		tags.put("FilePath", f.getFilename());

		ID3v2 id3v2Tag = f.getId3v2Tag();
		tags.put(ID_ARTIST, id3v2Tag.getArtist());
		tags.put(ID_TITLE, id3v2Tag.getTitle());
		tags.put(ID_ALBUM, id3v2Tag.getAlbum());
		tags.put(ID_COMPOSER, id3v2Tag.getComposer());
		tags.put(ID_PUBLISHER, id3v2Tag.getPublisher());
		tags.put(ID_ORIGINAL_ARTIST, id3v2Tag.getOriginalArtist());
		tags.put(ID_ALBUM_ARTIST, id3v2Tag.getAlbumArtist());
		tags.put(ID_ENCODER, id3v2Tag.getEncoder());

		log.debug(tags.toString());
		return tags;
	}

	private void decodeText(ID3v2 id3v2Tag, String tagId, String fromEncoding, String toEncoding) {
		ID3v2FrameSet set = id3v2Tag.getFrameSets().get(tagId);
		if (set == null)
			return;
		ID3v2Frame frame = id3v2Tag.getFrameSets().get(tagId).getFrames().get(0);

		try {
			byte[] utfBytes = new String(ArrayUtils.remove(frame.getData(), 0), fromEncoding).trim().getBytes(toEncoding); // remove leading byte and convert
			frame.setData(ArrayUtils.add(utfBytes, 0, EncodedText.TEXT_ENCODING_UTF_8));
		} catch (UnsupportedEncodingException e) {
			log.error("Cannot decode tag {} from {} to {}", tagId, fromEncoding, toEncoding);
		}
	}

	public TargetFilePathMaker createTargetFilePathMaker(String src, String tgt, String encoding) {
		if (isFile(src) && isFile(tgt) && !src.equals(tgt))
			return new DirectInput(tgt, encoding);

		String tgtPath = isFile(tgt) ? new File(tgt).getParent().toString() : tgt;
		return new ConcatInput(tgtPath, encoding);
	}
}

abstract class TargetFilePathMaker {
	final String path;
	final String encoding;

	TargetFilePathMaker(String path, String encoding) {
		this.path = path;
		this.encoding = encoding;
	}

	abstract String makeFilePath(String source);
}

class DirectInput extends TargetFilePathMaker {
	public DirectInput(String path, String encoding) {
		super(path, encoding);
	}

	@Override
	String makeFilePath(String source) {
		return path;
	}
}

class ConcatInput extends TargetFilePathMaker {
	ConcatInput(String path, String encoding) {
		super(path, encoding);
	}

	@Override
	String makeFilePath(String source) {
		int startPos = source.contains(File.separator) ? source.lastIndexOf(File.separator) : 0;
		int extPos = source.lastIndexOf(".");
		String fileNameWithoutExt = source.substring(startPos, extPos);
		return path + fileNameWithoutExt + "_" + encoding + ".mp3";
	}
}
