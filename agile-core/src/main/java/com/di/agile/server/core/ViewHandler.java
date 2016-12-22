package com.di.agile.server.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import com.di.agile.core.server.bean.HttpRequest;
import com.di.agile.core.server.bean.HttpResponse;
import com.di.agile.server.util.LogUtil;
import com.di.agile.server.util.SessionUtil;

/**
 * @author di
 */
public class ViewHandler implements Runnable {
	// 就绪的I/O键
	private SelectionKey key;
	private Selector selector;
	private ByteBuffer buffer;
	private SocketChannel channel;
	private HttpRequest request;
	private HttpResponse response;
	private String sessionId;

	public ViewHandler(String requestHeader, SelectionKey key) {
		this.key = key;
		request = new HttpRequest(requestHeader);
		response = new HttpResponse();
		response.setDomain(request.getHost().trim());
		if (!SessionUtil.contains(request.getSessionId())) {
			sessionId = SessionUtil.generateSessionId();
		} else {
			sessionId = request.getSessionId();
		}
		response.setSessionId(sessionId);
	}

	@Override
	public void run() {
		handler();
	}

	public void handler() {
		// 从context中得到相应的参数
		buffer = ByteBuffer.allocate(1024);
		selector = key.selector();
		channel = (SocketChannel) key.channel();
		RequestHandler.process(sessionId, request, response);
		LogUtil.info("path : " + request.getPath());
		buffer = ByteBuffer.allocate(20240);
		LogUtil.info(response.writer());
		if (response.getBodys() != null) {
			buffer.put(response.writerFile());
		} else {
			buffer.put(response.writer().getBytes());
		}
		// 从写模式，切换到读模式
		buffer.flip();
		try {
			channel.register(selector, SelectionKey.OP_WRITE);
			channel.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
