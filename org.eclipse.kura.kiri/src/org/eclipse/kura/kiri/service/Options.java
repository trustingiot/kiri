package org.eclipse.kura.kiri.service;

import java.util.Map;

/**
 * IRIService's options
 */
public class Options extends org.eclipse.kura.kiri.util.Options {

	public static final String PROPERTY_ENABLE = "enable";
	public static final String PROPERTY_PORT = "port";
	public static final String PROPERTY_UDP_PORT = "udp.port";
	public static final String PROPERTY_TCP_PORT = "tcp.port";
	public static final String PROPERTY_SEND_LIMIT = "send.limit";
	public static final String PROPERTY_MAX_PEERS = "max.peers";
	public static final String PROPERTY_COORDINATOR_INTERVAL = "coordinator.interval";

	public static final boolean PROPERTY_ENABLE_DEFAULT = false;
	public static final Integer PROPERTY_PORT_DEFAULT = 14700;
	public static final Integer PROPERTY_UDP_PORT_DEFAULT = 14600;
	public static final Integer PROPERTY_TCP_PORT_DEFAULT = 14600;
	public static final String PROPERTY_SEND_LIMIT_DEFAULT = "1.0";
	public static final Integer PROPERTY_MAX_PEERS_DEFAULT = 8;
	public static final Integer PROPERTY_COORDINATOR_INTERVAL_DEFAULT = 60;

	protected final boolean enable;
	protected final Integer port;
	protected final Integer udpPort;
	protected final Integer tcpPort;
	protected final String sendLimit;
	protected final Integer maxPeers;
	protected final Integer coordinatorInterval;

	public Options(Map<String, Object> properties) {
		super(properties);
		enable = read(PROPERTY_ENABLE, PROPERTY_ENABLE_DEFAULT);
		port = read(PROPERTY_PORT, PROPERTY_PORT_DEFAULT);
		udpPort = read(PROPERTY_UDP_PORT, PROPERTY_UDP_PORT_DEFAULT);
		tcpPort = read(PROPERTY_TCP_PORT, PROPERTY_TCP_PORT_DEFAULT);
		sendLimit = read(PROPERTY_SEND_LIMIT, PROPERTY_SEND_LIMIT_DEFAULT);
		maxPeers = read(PROPERTY_MAX_PEERS, PROPERTY_MAX_PEERS_DEFAULT);
		coordinatorInterval = read(PROPERTY_COORDINATOR_INTERVAL, PROPERTY_COORDINATOR_INTERVAL_DEFAULT);
	}

	public boolean isEnable() {
		return enable;
	}

	public Integer getPort() {
		return port;
	}

	public Integer getUdpPort() {
		return udpPort;
	}

	public Integer getTcpPort() {
		return tcpPort;
	}

	public String getSendLimit() {
		return sendLimit;
	}

	public Integer getMaxPeers() {
		return maxPeers;
	}

	public Integer getCoordinatorInterval() {
		return coordinatorInterval;
	}
}
