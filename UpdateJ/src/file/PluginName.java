package file;

public class PluginName {
	private String name;

	public String getName() {
		return name;
	}

	public PluginName(String name) {
		this.name = name;
	}
	
	public String getPluginNameWithoutVersionNumber() {
		String nameWithoutVersionNumber;
		if (name.contains("/")) {
			nameWithoutVersionNumber = name.substring(0, name.indexOf('_')) +
					name.substring(name.indexOf('/') + 1);
		} else {
			nameWithoutVersionNumber = name.substring(0, name.indexOf('_')) + ".jar";
		}
		return nameWithoutVersionNumber;
	}
}
