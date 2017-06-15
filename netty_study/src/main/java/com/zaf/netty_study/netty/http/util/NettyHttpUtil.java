package com.zaf.netty_study.netty.http.util;

import java.net.InetSocketAddress;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpRequest;

public final class NettyHttpUtil {

	/**
	 * this is tool class, private constructor to prevent create instance.
	 */
	private NettyHttpUtil() {
		// do nothing.
	}
	
	private static final String zeroIp = "0.0.0.0";
	private static final int zeroPort = 0;

	public final static String getHttpRemoteIp(final Channel channel,
			final HttpRequest request) {

		String clientIp = null;
		if (request != null)
			clientIp = request.headers().get("X-Forwarded-For");

		if (clientIp == null) {
			InetSocketAddress inetSocketAddress = (InetSocketAddress) channel
					.remoteAddress();
			if (inetSocketAddress == null)
				clientIp = zeroIp;
			else
				clientIp = inetSocketAddress.getAddress().getHostAddress();
		}

		return clientIp;
	}

	public final static int getHttpRemotePort(final Channel channel) {

		InetSocketAddress inetSocketAddress = (InetSocketAddress) channel
				.remoteAddress();
		if (inetSocketAddress == null)
			return zeroPort;
		else
			return inetSocketAddress.getPort();
	}

	public final static String getHttpLocalIp(final Channel channel) {

		InetSocketAddress inetSocketAddress = (InetSocketAddress) channel
				.localAddress();
		if (inetSocketAddress == null)
			return zeroIp;
		else
			return inetSocketAddress.getAddress().getHostAddress();
	}

	public final static int getHttpLocalPort(final Channel channel) {

		InetSocketAddress inetSocketAddress = (InetSocketAddress) channel
				.localAddress();
		if (inetSocketAddress == null)
			return zeroPort;
		else
			return inetSocketAddress.getPort();
	}

}
