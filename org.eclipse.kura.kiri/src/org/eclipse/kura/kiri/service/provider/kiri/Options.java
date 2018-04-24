package org.eclipse.kura.kiri.service.provider.kiri;

import java.util.Map;

/**
 * KIRIService's options
 */
public class Options extends org.eclipse.kura.kiri.util.Options {

	public static final String PROPERTY_CLEAN_INSTALLATION = "clean.installation";

	public static final boolean PROPERTY_CLEAN_INSTALLATION_DEFAULT = false;

	protected final boolean cleanInstallation;

	public Options(Map<String, Object> properties) {
		super(properties);
		cleanInstallation = read(PROPERTY_CLEAN_INSTALLATION, PROPERTY_CLEAN_INSTALLATION_DEFAULT);
	}

	public boolean installationMustBeCleaned() {
		return cleanInstallation;
	}
}
