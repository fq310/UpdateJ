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
		try {
			String[] commands = data.split("\\|");
			for (String command : commands) {
				if (command.length() == 0) continue;
				String[] values = command.split("\\?");
				if (values.length != 4) continue;
				String operation = values[0];
				String pluginFolder = values[1];
				String sourceFolder = values[2];
				String buildFile = values[3];
				if (invalidPath(pluginFolder, sourceFolder, buildFile)) continue;
				if (operation.equals("validate")) {
					validate(pluginFolder, sourceFolder, buildFile);
					response.add("validate " + buildFile + " successful.");
				} 
				if (operation.equals("update")) {
					validate(pluginFolder, sourceFolder, buildFile);
					update();
				}
			}
		} catch (Exception e) {
			response.add("@Exception occurred.");
			response.add(e.getMessage());
		}
	}

	private boolean invalidPath(String pluginFolder, String sourceFolder,
			String buildFile) {
		if (pluginFolder == null || sourceFolder == null || buildFile == null ||
				pluginFolder.length() == 0 || sourceFolder.length() == 0 || sourceFolder.length() == 0)
			return true;
		return false;
	}

	@Override
	public String getResponse() {
		StringBuilder result = new StringBuilder();
		for (String str : response) {
			result.append(str).append(" ");
		}
		return result.toString();
	}
}
