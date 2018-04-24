package org.eclipse.kura.kiri.service.provider.kiri;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.kura.KuraException;
import org.eclipse.kura.command.CommandService;
import org.eclipse.kura.configuration.ConfigurableComponent;
import org.eclipse.kura.configuration.ConfigurationService;
import org.eclipse.kura.kiri.service.IRIService;
import org.eclipse.kura.kiri.service.KIRIService;
import org.eclipse.kura.kiri.util.ActionRecorder;
import org.osgi.service.component.ComponentContext;

public class KIRIServiceProvider implements KIRIService, ActionRecorder, ConfigurableComponent {

	protected static String INSTALLATION = "testnetdb";
	protected static String INSTALLATION_LOG = INSTALLATION + ".log";

	////
	//
	// Action recorder
	//
	//
	public static final String ID = "org.eclipse.kura.kiri.kiri";

	@Override
	public String getID() {
		return ID;
	}

	////
	//
	// Parameters
	//
	//
	protected Options options;
	protected Map<String, Object> properties;

	////
	//
	// Registered services
	//
	//
	protected CommandService commandService;
	protected ConfigurationService configurationService;
	protected IRIService iriService;

	protected void setConfigurationService(ConfigurationService service) {
		configurationService = service;
	}

	protected void unsetConfigurationService(ConfigurationService service) {
		configurationService = null;
	}

	protected void setCommandService(CommandService service) {
		commandService = service;
	}

	protected void unsetCommandService(CommandService service) {
		commandService = null;
	}

	protected void setIRIService(IRIService service) {
		iriService = service;
	}

	protected void unsetIRIService(IRIService service) {
		iriService = null;
	}

	////
	//
	// Service methods
	//
	//
	protected void activate(ComponentContext context, Map<String, Object> properties) {
		performRegisteredAction("Activating", this::update, properties);
	}

	protected void updated(ComponentContext context, Map<String, Object> properties) {
		performRegisteredAction("Updating", this::update, properties);
	}

	protected void deactivate(ComponentContext context, Map<String, Object> properties) {
		performRegisteredAction("Deactivating", () -> {
		});
	}

	////
	//
	// KIRI service
	//
	//

	////
	//
	// Functionality
	//
	//
	protected void update(Map<String, Object> properties) {
		createOptions(properties);
		cleanInstallation();
	}

	protected void createOptions(Map<String, Object> properties) {
		this.properties = properties;
		this.options = new Options(properties);
	}

	protected void cleanInstallation() {
		if (options.installationMustBeCleaned()) {
			iriService.stop();
			for (String s : new String[] {INSTALLATION, INSTALLATION_LOG}) {
				deleteFolder(Paths.get(s).toFile());
			}
			iriService.start();
			disableOption(Options.PROPERTY_CLEAN_INSTALLATION);
		}
	}

	private static void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) {
			Arrays.asList(files).forEach(KIRIServiceProvider::deleteFolder);
		}
		folder.delete();
	}

	protected void disableOption(String property) {
		if ((boolean) properties.get(property)) {
			Map<String, Object> map = new HashMap<>(properties);
			map.put(property, false);
			try {
				configurationService.updateConfiguration(ID, map);
			} catch (KuraException e) {
				error("Unable to update configuartion", e);
			}
		}
	}
}
