package rh6pd.resources;

import org.drools.builder.ResourceType;

public class FtlResourceType extends ResourceType{

	public FtlResourceType(String name, String description,
			String defaultExtension, String[] otherExtensions) {
		super(name, description, defaultExtension, otherExtensions);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5165925997221769884L;
	
	 /** Freemarker template */
    public static final ResourceType FTL        = addResourceTypeToRegistry( "FTL",
                                                                             "Freemarker template",
                                                                             "ftl");

}
