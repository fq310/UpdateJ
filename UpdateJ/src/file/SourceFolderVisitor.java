package file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class SourceFolderVisitor implements FileVisitor<Path> {
	private List<Path> xmlBuildFiles = new ArrayList<>();
	private String fileName;
	public SourceFolderVisitor(String fileName) {
		this.fileName = fileName;
	}

	public List<Path> getXmlBuildFiles() {
		return xmlBuildFiles;
	}
	@Override
	public FileVisitResult postVisitDirectory(Path arg0, IOException arg1)
			throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path arg0, BasicFileAttributes arg1)
			throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path path, BasicFileAttributes arg1)
			throws IOException {
		if (path.getFileName().endsWith(fileName))
			xmlBuildFiles.add(path);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path arg0, IOException arg1)
			throws IOException {
		return FileVisitResult.CONTINUE;
	}

}
