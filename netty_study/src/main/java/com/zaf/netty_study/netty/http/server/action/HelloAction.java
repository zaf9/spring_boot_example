package com.zaf.netty_study.netty.http.server.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

@Controller("helloAction")
public class HelloAction extends AbstractHttpServerAction {

	private static final Logger logger = LoggerFactory.getLogger(HelloAction.class);

	@Override
	public void doAction(ChannelHandlerContext ctx, FullHttpRequest request, String uri, String clientIp,
			int clientPort) {

		logger.info("doAction HelloAction start for client ip: {} port: {} uri: {}", clientIp, clientPort, uri);

		this.doResponse(ctx, "Hello World!");

		logger.info("doAction HelloAction end for client ip: {} port: {} uri: {}", clientIp, clientPort, uri);

	}

}
