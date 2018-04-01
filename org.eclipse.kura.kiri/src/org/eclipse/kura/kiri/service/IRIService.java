package org.eclipse.kura.kiri.service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.kura.configuration.ConfigurableComponent;
import org.eclipse.kura.kiri.util.ActionRecorder;
import org.osgi.service.component.ComponentContext;

import com.iota.iri.IRI;

public class IRIService implements ActionRecorder, ConfigurableComponent {

	////
	//
	// Action recorder
	//
	//
	public static final String ID = "org.eclipse.kura.iri";

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
	protected ExecutorService worker;
	protected Future<?> handle;

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
	// Functionality
	//
	//
	protected void activate(Map<String, Object> properties) {
		createOptions(properties);
	}

	protected void update(Map<String, Object> properties) {
		shutdownIRI();
		createOptions(properties);
		startIRI();
	}

	protected void deactivate() {
		shutdownIRI();
	}

	protected void createOptions(Map<String, Object> properties) {
		options = new Options(properties);
	}

	private void shutdownIRI() {
		if (connected) {
			info("Shutting down IOTA node, please hold tight...");
			try {
				IRI.ixi.shutdown();
				IRI.api.shutDown();
				IRI.iota.shutdown();
			} catch (final Exception e) {
				error("Exception occurred shutting down IOTA node: ", e);
			}
			connected = false;
		}
	}

	protected void shutdownWorker() {
		if (handle != null) {
			shutdownIRI();
			handle.cancel(true);
			handle = null;
		}

		if (worker != null) {
			worker.shutdown();
			worker = null;
		}
	}

	private void startIRI() {
		if (options.isEnable()) {
			worker = Executors.newSingleThreadExecutor();
			handle = worker.submit(this::setupIRI);
		}
	}

	private void setupIRI() {
		try {
			connected = true;
			String[] args = new String[] {
					"--testnet",
					"--remote",
					"-p", options.getPort().toString(),
					"-u", options.getUdpPort().toString(),
					"-t", options.getTcpPort().toString(),
					"--send-limit", options.getSendLimit(),
					"--max-peers", options.getMaxPeers().toString() };
			IRI.main(args);
		} catch (IOException e) {
			connected = false;
			handle = null;
			error("Fail", e);
		}
	}
}
