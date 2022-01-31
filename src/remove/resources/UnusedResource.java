package remove.resources;

public class UnusedResource {

	public UnusedResource(String file) {
		this.file = file;
		this.xmlName = null;
		this.resourceType = getResourceFromFile(file);
	}

	public UnusedResource(String file, String xmlName) {
		this.file = file;
		this.xmlName = xmlName;
		this.resourceType = getResourceFromFile(file);
	}

	public UnusedResource(String file, String xmlName, ResourceTypeEnum resourceType) {
		this.file = file;
		this.xmlName = xmlName;
		this.resourceType = resourceType;
	}

	private String file;
	private ResourceTypeEnum resourceType;
	private String xmlName;

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public ResourceTypeEnum getResourceType() {
		return resourceType;
	}

	public void setModule(ResourceTypeEnum resourceType) {
		this.resourceType = resourceType;
	}

	public String getXmlName() {
		return xmlName;
	}

	public void setXmlName(String xmlName) {
		this.xmlName = xmlName;
	}

	private ResourceTypeEnum getResourceFromFile(String file) {
		ResourceTypeEnum resourceType;

		if (file.contains("res/layout")) {
			resourceType = ResourceTypeEnum.LAYOUT;
		} else if (file.contains("/res/drawable")) {
			resourceType = ResourceTypeEnum.DRAWABLE;
		} else if (file.contains("/res/anim")) {
			resourceType = ResourceTypeEnum.ANIM;
		} else if (file.contains("/res/color")) {
			resourceType = ResourceTypeEnum.COLOR;
		} else if (file.contains("/res/font")) {
			resourceType = ResourceTypeEnum.FONT;
		} else if (file.contains("/res/menu")) {
			resourceType = ResourceTypeEnum.MENU;
		} else if (file.contains("/res/navigation")) {
			resourceType = ResourceTypeEnum.NAVIGATION;
		} else if (file.contains("/res/transition")) {
			resourceType = ResourceTypeEnum.TRANSITION;
		} else if (file.contains("/res/xml")) {
			resourceType = ResourceTypeEnum.XML;
		} else if (file.contains("/res/values")) {
			resourceType = ResourceTypeEnum.VALUES;
		} else {
			resourceType = ResourceTypeEnum.OTHER;
		}
		return resourceType;
	}
}