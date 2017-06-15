package com.zaf.netty_study.netty.http.server.action;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface IHttpServerAction {

	
	/**
	 *   handle http request
	 * 
	 * @param ChannelHandlerContext ctx
	 * @param FullHttpRequest request
	 * @param String uri
	 * @param String clientIp
	 * @param int clientPort
	 * 
	 */
	public void doAction(ChannelHandlerContext ctx, FullHttpRequest request,
			final String uri, final String clientIp, final int clientPort);
}
