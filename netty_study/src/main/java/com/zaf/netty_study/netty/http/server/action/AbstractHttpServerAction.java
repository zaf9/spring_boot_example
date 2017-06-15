package com.zaf.netty_study.netty.http.server.action;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public abstract class AbstractHttpServerAction implements IHttpServerAction {

	/**
	 * get the request content
	 * 
	 * @param request
	 *            FullHttpRequest
	 * 
	 * @return the request content
	 */
	protected String getContent(FullHttpRequest request) {

		return request.content().toString(CharsetUtil.UTF_8);
	}

	/**
	 * build response object and send to client
	 * 
	 * @param ctx
	 *            ChannelHandlerContext
	 * @param status
	 *            HttpResponseStatus
	 */
	protected final void doResponse(ChannelHandlerContext ctx, HttpResponseStatus status) {

		// Build the response object.
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);

		// send response
		sendResponse(ctx, response);

	}

	/**
	 * build response object and send to client
	 * 
	 * @param ctx
	 *            ChannelHandlerContext
	 * 
	 * @param responseContent
	 *            the response content
	 */
	protected final void doResponse(ChannelHandlerContext ctx, String responseContent) {

		// convert content
		ByteBuf byteBuf = Unpooled.copiedBuffer(responseContent, CharsetUtil.UTF_8);

		// Build the response object.
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
		response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/xml; charset=UTF-8");
		response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, byteBuf.readableBytes());

		// send response
		sendResponse(ctx, response);
	}

	private final void sendResponse(ChannelHandlerContext ctx, FullHttpResponse response) {

		ctx.write(response).addListener(ChannelFutureListener.CLOSE);
	}

}
