package org.eclipse.kura.kiri.service.provider.iri;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.kura.configuration.ConfigurableComponent;
import org.eclipse.kura.kiri.coordinator.PeriodicCoordinator;
import org.eclipse.kura.kiri.service.IRIService;
import org.eclipse.kura.kiri.util.ActionRecorder;
import org.osgi.service.component.ComponentContext;

import com.iota.iri.IRI;

public class IRIServiceProvider implements IRIService, ActionRecorder, ConfigurableComponent {

	////
	//
	// Action recorder
	//
	//
	public static final String ID = "org.eclipse.kura.kiri.iri";

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
	protected boolean connected;
	protected ExecutorService iriWorker, coordinatorWorker;
	protected Future<?> iriHandle, coordinatorHandle;
	protected PeriodicCoordinator coordinator;

	////
	//
	// Registered services
	//
	//
	/* NOTHING */

	////
	//
	// Service methods
	//
	//
	protected void activate(ComponentContext context, Map<String, Object> properties) {
		performRegisteredAction("Activating", this::activate, properties);
	}

	protected void updated(ComponentContext context, Map<String, Object> properties) {
		performRegisteredAction("Updating", this::update, properties);
	}

	protected void deactivate(ComponentContext context, Map<String, Object> properties) {
		performRegisteredAction("Deactivating", this::deactivate);
	}

	////
	//
	// IRI service
	//
	//
	@Override
	public void start() {
		if (!connected)
			startWorkers();
	}

	@Override
	public void stop() {
		shutdownWorkers();
	}

	////
	//
	// Functionality
	//
	//
	protected void activate(Map<String, Object> properties) {
		createOptions(properties);
	}

	protected void update(Map<String, Object> properties) {
		shutdownWorkers();
		createOptions(properties);
		startWorkers();
	}

	protected void deactivate() {
		shutdownWorkers();
	}

	protected void createOptions(Map<String, Object> properties) {
		options = new Options(properties);
	}

	protected void shutdownWorkers() {
		if (coordinatorHandle != null) {
			shutdownCoordinator();
			coordinatorHandle.cancel(true);
			coordinatorWorker = null;
		}

		if (coordinatorWorker != null) {
			coordinatorWorker.shutdown();
			coordinatorWorker = null;
		}

		if (iriHandle != null) {
			IRI.stop();
			iriHandle.cancel(true);
			iriHandle = null;
		}

		if (iriWorker != null) {
			iriWorker.shutdown();
			iriWorker = null;
		}

		connected = false;
	}

	protected void shutdownCoordinator() {
		if (connected) {
			info("Shutting down IOTA coordinator, please hold tight...");
			try {
				if (coordinator != null) {
					coordinator.stop();
					coordinator = null;
				}
				info("Shut down IOTA coordinator");
			} catch (final Exception e) {
				error("Exception occurred shutting down IOTA coordinator: ", e);
			}
		}
	}

	protected void startWorkers() {
		if (options.isEnable()) {
			iriWorker = Executors.newSingleThreadExecutor();
			iriHandle = iriWorker.submit(this::setupIRI);

			coordinatorWorker = Executors.newSingleThreadExecutor();
			coordinatorHandle = coordinatorWorker.submit(this::setupCoordinator);
		}
	}

	protected void setupIRI() {
		try {
			connected = true;
			String[] args = new String[] {
					"--testnet",
					"--testnet-no-coo-validation",
					"--remote",
					"--mwm", options.getMWM().toString(),
					"-p", options.getPort().toString(),
					"-u", options.getUdpPort().toString(),
					"-t", options.getTcpPort().toString(),
					"--send-limit", options.getSendLimit(),
					"--max-peers", options.getMaxPeers().toString() };
			IRI.main(args);
		} catch (Exception e) {
			connected = false;
			iriHandle = null;
			iriWorker.shutdown();
			iriWorker = null;
			error("Fail", e);
		}
	}

	protected void setupCoordinator() {
		try {
			coordinator = new PeriodicCoordinator(options.getPort().toString(), options.getCoordinatorInterval());
		} catch (Exception e) {
			coordinatorHandle = null;
			coordinatorWorker.shutdown();
			coordinatorWorker = null;
			error("Fail", e);
		}
	}
}
