package file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import socket.IDataReceiver;

public class Updater implements IDataReceiver {
	private List<BuildFile> buildFiles;
	private List<String> response = new ArrayList<>();

	private void validate(String pluginFolder, String sourceFolder, String buildFileName) {
		PluginFolder folder = new PluginFolder(Paths.get(pluginFolder));
		Map<String, String> id2Name = folder.getId2name();
		List<Path> xmlFilePath = getBuildFilePath(Paths.get(sourceFolder), buildFileName);
		List<BuildFile> validBuildFiles = new ArrayList<>();
		for (Path path : xmlFilePath) {
			BuildFile buildFile = new BuildFile(path, id2Name);
			buildFile.validate();
			validBuildFiles.add(buildFile);
		}
		buildFiles = new ArrayList<>(validBuildFiles);
	}
	
	private void update() {
		if (buildFiles == null || buildFiles.size() == 0) return;
		for (BuildFile file : buildFiles) {
			file.update();
		}
	}

	private static List<Path> getBuildFilePath(Path path, String fileName) {
		SourceFolderVisitor visitor = new SourceFolderVisitor(fileName);
		try {
			Files.walkFileTree(path, visitor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return visitor.getXmlBuildFiles();
	}

	@Override
	public void execute(String data) {
		response.clear();
		String[] commands = data.split("|");
		for (String command : commands) {
			String[] values = command.split(":");
			String operation = values[0];
			if (operation.equals("validate")) {
				validate(values[1], values[2], values[3]);
				response.add("validate " + values[3] + "successful.");
			} 
			if (operation.equals("update")) {
				update();
			}
		}
	}

	@Override
	public String getResponse() {
		StringBuilder result = new StringBuilder();
		for (String str : response) {
			result.append(str).append("|");
		}
		return result.toString();
	}
}
