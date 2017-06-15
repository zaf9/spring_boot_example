package com.zaf.netty_study.netty.http.server;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

@Component
@Configuration
@PropertySource(value = "file:${spring.property.path}/config/netty.properties")
public class HttpServerImpl implements IHttpServer {

	private static final Logger logger = LoggerFactory
			.getLogger(HttpServerImpl.class);

	@Value("${netty.http.server.port}")
	private int httpServerPort;

	@Value("${netty.http.server.bossGroup.thread.numbers}")
	private int bossGroupThreadNumbers;

	@Value("${netty.http.server.workerGroup.thread.numbers}")
	private int workerGroupThreadNumbers;

	@Value("${netty.http.server.loglevel}")
	private String httpServerLoglevel;

	@Value("${netty.http.server.channelOption.soBacklog}")
	private int channelOptionSoBacklog;

	@Value("${netty.http.server.channelOption.connectTimeoutMills}")
	private int channelOptionConnectTimeoutMills;

	@Autowired
	private ChannelInitializer<SocketChannel> httpServerInitializer;

	private Channel channel;

	@Override
	@PostConstruct
	public void start() {

		new Thread(new NettyHttpServerThread(), "nettyHttpServerThread")
				.start();
	}

	@Override
	@PreDestroy
	public void stop() {
		logger.info("netty http server beging to stop ...");
		if (this.channel != null) {
			logger.info("netty http server channel isn't null, so need close it.");
			this.channel.close().addListener(ChannelFutureListener.CLOSE);
		}
		logger.info("netty http server stop success!");
	}

	private class NettyHttpServerThread implements Runnable {

		@Override
		public void run() {
			logger.info("netty http server beging to start ...");
			// Configure the server.
			EventLoopGroup bossGroup = new NioEventLoopGroup(
					bossGroupThreadNumbers);
			EventLoopGroup workerGroup = new NioEventLoopGroup(
					workerGroupThreadNumbers);

			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap
					.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, channelOptionSoBacklog)
					.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
							channelOptionConnectTimeoutMills)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new LoggingHandler(httpServerLoglevel))
					.childHandler(httpServerInitializer);

			try {
				channel = serverBootstrap.bind(httpServerPort).sync().channel();
				logger.info("netty http server started success!");
				channel.closeFuture().sync();
			} catch (InterruptedException e) {
				logger.info(
						"netty http server is starting， this process generated exception: ",
						e);
			} finally {
				bossGroup.shutdownGracefully();
				workerGroup.shutdownGracefully();
			}
		}
	}

}
