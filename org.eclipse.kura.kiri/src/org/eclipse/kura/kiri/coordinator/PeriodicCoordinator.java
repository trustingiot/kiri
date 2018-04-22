package org.eclipse.kura.kiri.coordinator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.kura.kiri.util.Loggable;

import com.iota.iri.IRI;

import jota.IotaAPI;
import jota.dto.response.GetAttachToTangleResponse;
import jota.dto.response.GetNodeInfoResponse;
import jota.dto.response.GetTransactionsToApproveResponse;
import jota.model.Bundle;
import jota.model.Transaction;
import jota.utils.Converter;

/**
 * Copy & adapted from https://github.com/schierlm/private-iota-testnet
 */
public class PeriodicCoordinator implements Loggable {

	public static final String NULL_HASH = "999999999999999999999999999999999999999999999999999999999999999999999999999999999";
	public static final String TESTNET_COORDINATOR_ADDRESS = "XNZBYAST9BETSDNOVQKKTBECYIPMF9IPOZRWUPFQGVH9HJW9NDSQVIPVBWU9YKECRYGDSJXYMZGHZDXCA";
	public static final String NULL_ADDRESS = "999999999999999999999999999999999999999999999999999999999999999999999999999999999";
	public static final int TAG_TRINARY_SIZE = 81;

	private String host;
	private String port;
	private Integer interval;
	private ScheduledExecutorService executor;
	private IotaAPI api;
	private boolean shutdown;

	private PeriodicCoordinator() {
		super();
		host = "localhost";
		executor = Executors.newSingleThreadScheduledExecutor();
	}

	public PeriodicCoordinator(String port, Integer interval) {
		this();
		this.port = port;
		this.interval = interval;
		this.api = new IotaAPI.Builder().host(host).port(this.port).build();
		this.shutdown = false;

		generateMilestone();
	}

	protected void generateMilestone() {
		if (!shutdown) {
			if (IRI.isInitialized()) {
				try {
					GetNodeInfoResponse nodeInfo = api.getNodeInfo();
					int updatedMilestone = nodeInfo.getLatestMilestoneIndex() + 1;
					if (nodeInfo.getLatestMilestone().equals(NULL_HASH)) {
						newMilestone(api, NULL_HASH, NULL_HASH, updatedMilestone);
					} else {
						GetTransactionsToApproveResponse x = api.getTransactionsToApprove(10);
						newMilestone(api, x.getTrunkTransaction(), x.getBranchTransaction(), updatedMilestone);
					}
					info("New milestone " + updatedMilestone + " created.");
				} catch (Exception e) {
					error(e.getLocalizedMessage(), e);
				}
			}

			executor.schedule(this::generateMilestone, interval, TimeUnit.SECONDS);
		}
	}

	static void newMilestone(IotaAPI api, String tip1, String tip2, long index) throws Exception {
		final Bundle bundle = new Bundle();
		String tag = Converter.trytes(Converter.trits(index, TAG_TRINARY_SIZE));
		long timestamp = System.currentTimeMillis() / 1000;
		bundle.addEntry(1, TESTNET_COORDINATOR_ADDRESS, 0, tag, timestamp);
		bundle.addEntry(1, NULL_ADDRESS, 0, tag, timestamp);
		bundle.finalize(null);
		bundle.addTrytes(Collections.<String>emptyList());
		List<String> trytes = new ArrayList<>();
		for (Transaction trx : bundle.getTransactions()) {
			trytes.add(trx.toTrytes());
		}
		Collections.reverse(trytes);
		GetAttachToTangleResponse rrr = api.attachToTangle(tip1, tip2, 13,
				(String[]) trytes.toArray(new String[trytes.size()]));
		api.storeTransactions(rrr.getTrytes());
		api.broadcastTransactions(rrr.getTrytes());
	}

	public void stop() {
		if (executor != null) {
			shutdown = true;
			executor.shutdown();
			executor = null;
		}
	}
}
