package file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class PluginFolderVisitor implements FileVisitor<Path> {
	private static final String SLASH = "/";
	private Map<String, String> id2name = new HashMap<>();
	private Stack<String> dirStack = new Stack<>();
	private Path path;
	public PluginFolderVisitor(Path path) {
		this.path = path;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		if (!dir.equals(path)) {
			dirStack.push(getName(dir));
			dirStack.push(SLASH);
		}
		return FileVisitResult.CONTINUE;
	}

	private String getName(Path dir) {
		return dir.getName(dir.getNameCount() - 1).toString();
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
		parseFile(file);
		return FileVisitResult.CONTINUE;
	}

	private void parseFile(Path file) {
		String name = getName(file);
		if (!name.endsWith("jar")) return;
		PluginName pluginName = new PluginName(getDirName() + name);
		id2name.put(pluginName.getPluginNameWithoutVersionNumber(), pluginName.getName());
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc)
			throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc)
			throws IOException {
		if (dirStack.size() >= 2) {
			dirStack.pop();
			dirStack.pop();
		}
		return FileVisitResult.CONTINUE;
	}
	
	private String getDirName() {
		if (dirStack.isEmpty()) return "";
		else {
			StringBuilder result = new StringBuilder();
			int size = dirStack.size();
			for (int i = 0; i < size; ++i) {
				result.append(dirStack.get(i));
			}
			return result.toString();
		}
	}
	
	public Map<String, String> getId2nameMap() {
		return id2name;
	}
}
