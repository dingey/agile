package com.di.agile.server.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import com.di.agile.server.util.LogUtil;

public class HttpServer implements Runnable {
	int port = 8081;
	private boolean interrupted = false;
	private int capacity = 10 * 1024 * 1024;// 10MB

	public HttpServer(boolean interrupted) {
		this.interrupted = interrupted;
	}

	public HttpServer(int port) {
		this.port = port;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		try {
			Selector selector = Selector.open();
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			ServerSocket serverSocket = serverSocketChannel.socket();
			serverSocket.setReuseAddress(true);
			try {
				serverSocket.bind(new InetSocketAddress(port));
			} catch (Exception e) {
				System.err.println("绑定端口失败,请检查server.xml中是否设置了port属性");
				return;
			}
			System.out.println("成功绑定端口" + port);
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("服务器启动成功");
			while (!interrupted) {
				int readyChannels = selector.select();
				if (readyChannels == 0)
					continue;
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = keys.iterator();
				while (iterator.hasNext()) {
					SelectionKey key = iterator.next();
					if (key.isAcceptable()) {
						ServerSocketChannel server = (ServerSocketChannel) key.channel();
						SocketChannel socketChannel = server.accept();
						if (socketChannel != null) {
							LogUtil.info("收到了来自"
									+ ((InetSocketAddress) socketChannel.getRemoteAddress()).getHostString() + "的请求");
							socketChannel.configureBlocking(false);
							socketChannel.register(selector, SelectionKey.OP_READ);
						}
					} else if (key.isReadable()) {
						SocketChannel socketChannel = (SocketChannel) key.channel();
						byte[] requestBytes = null;
						try {
							requestBytes = receive(socketChannel);
						} catch (Exception e) {
							LogUtil.error("读取socketChannel出错");
						}
						if (requestBytes != null && requestBytes.length > 0) {
							LogUtil.info(new String(requestBytes));
							LogUtil.info("启动了子线程..");
							new Thread(new HttpHandler(requestBytes, key)).start();
						}
					} else if (key.isWritable()) {
						LogUtil.info(new Date().toLocaleString() + " : 有流写出!");
						SocketChannel socketChannel = (SocketChannel) key.channel();
						socketChannel.shutdownInput();
						socketChannel.close();
					} else {
						serverSocketChannel.close();
					}
					iterator.remove();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private byte[] receive(SocketChannel socketChannel) throws Exception {
		ByteBuffer buffer = ByteBuffer.allocate(capacity);
		byte[] bytes = null;
		int size = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((size = socketChannel.read(buffer)) > 0) {
			buffer.flip();
			bytes = new byte[size];
			buffer.get(bytes);
			baos.write(bytes);
			buffer.clear();
		}
		bytes = baos.toByteArray();
		return bytes;
	}

	public static void main(String[] args) {
		new Thread(new HttpServer(false)).start();
	}
}