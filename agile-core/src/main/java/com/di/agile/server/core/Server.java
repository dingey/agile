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

public class Server implements Runnable {
	int port = 8081;
	private boolean interrupted = false;

	public Server(boolean interrupted) {
		this.interrupted = interrupted;
	}

	public Server(int port) {
		this.port = port;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		try {
			// 打开一个选择器
			Selector selector = Selector.open();
			// 打开ServerSocketChannel通道
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			// 得到ServerSocket对象
			ServerSocket serverSocket = serverSocketChannel.socket();
			// ServerSocketChannel通道监听server.xml中设置的端口
			serverSocket.setReuseAddress(true);
			try {
				serverSocket.bind(new InetSocketAddress(port));
			} catch (Exception e) {
				System.err.println("绑定端口失败,请检查server.xml中是否设置了port属性");
				return;
			}
			System.out.println("成功绑定端口" + port);
			// 将通道设置为非阻塞模式
			serverSocketChannel.configureBlocking(false);
			// 将serverSocketChannel注册给选择器,并绑定ACCEPT事件
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("服务器启动成功");
			while (!interrupted) {
				// 查询就绪的通道数量
				int readyChannels = selector.select();
				// 没有就绪的则继续进行循环
				if (readyChannels == 0)
					continue;
				// 获得就绪的selectionKey的set集合
				Set<SelectionKey> keys = selector.selectedKeys();
				// 获得set集合的迭代器
				Iterator<SelectionKey> iterator = keys.iterator();
				while (iterator.hasNext()) {
					SelectionKey key = iterator.next();
					if (key.isAcceptable()) {
						// 该key有ACCEPT事件
						// 将监听得到的channel强转为ServerSocketChannel
						ServerSocketChannel server = (ServerSocketChannel) key.channel();
						// 得到接收到的SocketChannel
						SocketChannel socketChannel = server.accept();
						if (socketChannel != null) {
							LogUtil.info("收到了来自"
									+ ((InetSocketAddress) socketChannel.getRemoteAddress()).getHostString() + "的请求");
							// 将socketChannel设置为阻塞模式
							socketChannel.configureBlocking(false);
							// 将socketChannel注册到选择器
							socketChannel.register(selector, SelectionKey.OP_READ);
						}
					} else if (key.isReadable()) {
						// 该key有Read事件
						SocketChannel socketChannel = (SocketChannel) key.channel();
						String requestHeader = "";
						// 拿出通道中的Http头请求
						try {
							requestHeader = receive(socketChannel);
						} catch (Exception e) {
							LogUtil.error("读取socketChannel出错");
							return;
						}
						// 启动线程处理该请求,if条件判断一下，防止心跳包
						if (requestHeader.length() > 0) {
							LogUtil.info(requestHeader);
							LogUtil.info("启动了子线程..");
							new Thread(new ViewHandler(requestHeader, key)).start();
						}
					} else if (key.isWritable()) {
						// 该key有Write事件
						LogUtil.info(new Date().toLocaleString() + " : 有流写出!");
						SocketChannel socketChannel = (SocketChannel) key.channel();
						socketChannel.shutdownInput();
						socketChannel.close();
					} else {
						serverSocketChannel.close();
					}
					// 从key集合中删除key，这一步很重要，就是因为没写这句，Selector.select()方法一直返回的是0
					// 原因分析可能是不从集合中删除，就不会回到I/O就绪事件中
					iterator.remove();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String receive(SocketChannel socketChannel) throws Exception {
		// 声明一个1024大小的缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte[] bytes = null;
		int size = 0;
		// 定义一个字节数组输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 将socketChannel中的数据写入到buffer中，此时的buffer为写模式，size为写了多少个字节
		while ((size = socketChannel.read(buffer)) > 0) {
			// 将写模式改为读模式
			// The limit is set to the current position and then the position is
			// set to zero.
			// 将limit设置为之前的position，而将position置为0，更多java nio的知识会写成博客的
			buffer.flip();
			bytes = new byte[size];
			// 将Buffer写入到字节数组中
			buffer.get(bytes);
			// 将字节数组写入到字节缓冲流中
			baos.write(bytes);
			// 清空缓冲区
			buffer.clear();
		}
		// 将流转回字节数组
		bytes = baos.toByteArray();
		return new String(bytes);
	}

	public static void main(String[] args) {
		new Thread(new Server(false)).start();
	}
}