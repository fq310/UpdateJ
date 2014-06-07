package file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import exception.UpdateException;

public class PluginFolder {
	private Path path;
	private Map<String, String> id2name;
	public PluginFolder(Path path) {
		this.path = path;
		try {
			search();
		} catch (IOException e) {
			e.printStackTrace();
			throw new UpdateException("Search plugin folder failed. Path: " + path);
		}
	}
	
	private void search() throws IOException {
		PluginFolderVisitor visitor = new PluginFolderVisitor(path);
		Files.walkFileTree(path, visitor);
		id2name = visitor.getId2nameMap();
	}
	
	public Map<String, String> getId2name() {
		return id2name;
	}
}
