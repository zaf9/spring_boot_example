package com.zaf.netty_study.netty.http.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zaf.netty_study.netty.http.server.action.IHttpServerActionMap;
import com.zaf.netty_study.netty.http.util.NettyHttpUtil;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObject;

@Component
@Sharable
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

	private static final Logger logger = LoggerFactory
			.getLogger(HttpServerHandler.class);

	@Autowired
	private IHttpServerActionMap httpServerActionMap;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
		if (msg instanceof FullHttpRequest) {
			FullHttpRequest request = (FullHttpRequest) msg;

			String uri = request.getUri();
			String clientIp = NettyHttpUtil.getHttpRemoteIp(ctx.channel(),
					request);
			int clientPort = NettyHttpUtil.getHttpRemotePort(ctx.channel());
			logger.debug("handleHttp start for client ip: {} port: {} uri: {}",
					clientIp, clientPort, uri);
			httpServerActionMap.get(uri).doAction(ctx, request, uri, clientIp,
					clientPort);
			logger.debug("handleHttp end for client ip: {} port: {} uri: {}",
					clientIp, clientPort, uri);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

		logger.error(
				"HttpServerHandler exceptionCaught for client ip: {} port: {} exception: {}",
				NettyHttpUtil.getHttpRemoteIp(ctx.channel(), null),
				NettyHttpUtil.getHttpRemotePort(ctx.channel()),
				cause.toString());
		ctx.close();
	}
}
