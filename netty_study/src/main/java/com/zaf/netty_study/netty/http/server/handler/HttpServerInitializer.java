package com.zaf.netty_study.netty.http.server.handler;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.EventExecutorGroup;

@Component
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

	@Value("${netty.http.server.channelInitializer.readTimeoutHandler.timeoutSeconds}")
	private int readTimeoutHandlerTimeoutSeconds;

	@Value("${netty.http.server.channelInitializer.httpObjectAggregator.maxContentLength}")
	private int httpObjectAggregatorMaxContentLength;
	
	@Resource(name = "httpServerInitializerEventExecutorGroup")
	private EventExecutorGroup eventExecutorGroup;

	@Autowired
	private SimpleChannelInboundHandler<?> httpServerHandler;	

	@Override
	public void initChannel(SocketChannel socketChannel) {

		ChannelPipeline channelPipeline = socketChannel.pipeline();
		channelPipeline.addLast("readTimeoutHandler", new ReadTimeoutHandler(
				readTimeoutHandlerTimeoutSeconds));
		channelPipeline.addLast("httpServerCodec", new HttpServerCodec());
		channelPipeline.addLast("httpObjectAggregator",
				new HttpObjectAggregator(httpObjectAggregatorMaxContentLength));
		channelPipeline.addLast(eventExecutorGroup, "httpServerHandler",
				httpServerHandler);
	}
}
