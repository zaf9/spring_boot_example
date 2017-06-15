package com.zaf.netty_study.netty.http.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

@Configuration
public class NettyHttpConfigurationBean {

	@Bean(name = "httpServerInitializerEventExecutorGroup")
	public EventExecutorGroup getEventExecutorGroup(
			@Value("${netty.http.server.channelInitializer.eventExecutorGroup.thread.numbers}") int nEventExecutors) {

		return new DefaultEventExecutorGroup(nEventExecutors);
	}
}
