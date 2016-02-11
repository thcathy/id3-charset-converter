package thc.id3.charset.convert;

import java.io.File;

import org.apache.commons.io.FileUtils;

public abstract class TargetPathFactory {
	final String path;
	
	TargetPathFactory(String path) {
		this.path = path;
	}

	abstract String makeFilePath(String source);
	
	public static TargetPathFactory getFactory(String src, String tgt, String encoding) {
		if (isFile(src) && isFile(tgt) && !src.equals(tgt))
			return new DirectInput(tgt);

		String tgtPath = isFile(tgt) ? new File(tgt).getParent().toString() : tgt;
		return new ConcatInput(tgtPath, encoding);
	}
	
	private static boolean isFile(String target) {
		return target.endsWith(".mp3");
	}
	
	public TargetPathFactory createFolder() throws Exception {
		if (isFile(path))
			FileUtils.forceMkdir(new File(path).getParentFile());
		else
			FileUtils.forceMkdir(new File(path));
		return this;
	}
		
	public static class DirectInput extends TargetPathFactory {
		public DirectInput(String path) {
			super(path);
		}
	
		@Override
		String makeFilePath(String source) {
			return path;
		}
	}
	
	public static class ConcatInput extends TargetPathFactory {
		final String encoding;
		
		ConcatInput(String path, String encoding) {
			super(path);
			this.encoding = encoding;
		}
	
		@Override
		String makeFilePath(String source) {
			int startPos = source.contains(File.separator) ? source.lastIndexOf(File.separator) : 0;
			int extPos = source.lastIndexOf(".");
			String fileNameWithoutExt = source.substring(startPos, extPos);
			return path + fileNameWithoutExt + "_" + encoding + ".mp3";
		}
	}
}
