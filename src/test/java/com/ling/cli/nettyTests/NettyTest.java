package com.ling.cli.nettyTests;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author ling
 * @description: TODO
 */
@Slf4j
public class NettyTest {
    /**
     * -------------------------FileCopy Test-----------------------------------------
     */
    @Test
    public void fileChannel() throws Exception {
        // 文件复制
        long startTime = System.currentTimeMillis();
        FileInputStream fileInputStream = new FileInputStream(new File("D:\\迅雷下载\\龙之家族 (更新至 第2季06).mp4"));
        FileChannel inputChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream(new File("D:\\迅雷下载\\copy.mp4"));
        FileChannel outputChannel = fileOutputStream.getChannel();

        inputChannel.transferTo(0, inputChannel.size(), outputChannel);

        inputChannel.close();
        outputChannel.close();
        log.info("耗时：{}", System.currentTimeMillis() - startTime);
    }

    /**
     * -------------------------SocketChannel Test------------------------------------
     */
    @Test
    public void fileSocketChannelServer() throws Exception {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(true);
        serverChannel.socket().bind(new InetSocketAddress(8686));

        while (true) {
            SocketChannel newSocket = serverChannel.accept();

            if (newSocket != null) {
                this.readFile(newSocket);
            }
        }

    }

    /**
     * @param socket 文件传输socket
     * @description: 读取文件
     */
    private void readFile(SocketChannel socket) throws IOException {
        String s = RandomUtil.randomString(5);
        FileOutputStream fileOutputStream = new FileOutputStream(new File("D:\\迅雷下载\\" + s + ".mkv"));
        FileChannel fileChannel = fileOutputStream.getChannel();
        while (true) {
            ByteBuffer allocate = ByteBuffer.allocate(4096);
            int read = socket.read(allocate);
            if (read == -1) break;
            allocate.flip();
            fileChannel.write(allocate);
        }
        fileChannel.close();
        socket.close();
    }

    @Test
    public void fileSocketChannelClient() throws IOException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 8686));

        FileInputStream fileInputStream = new FileInputStream(new File("D:\\迅雷下载\\MyWorld.mkv"));
        FileChannel fileChannel = fileInputStream.getChannel();
        while (true) {
            ByteBuffer allocate = ByteBuffer.allocate(4096);
            int read = fileChannel.read(allocate);
            if (read == -1) break;
            log.info("读取到数据：{}", read);
            allocate.flip();
            socketChannel.write(allocate);
        }
        fileChannel.close();
        socketChannel.close();
    }

    /**
     * -------------------------DatagramChannel Test-----------------------------------------
     */
    @Test
    public void datagramChannelServer() throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.bind(new InetSocketAddress("localhost", 8787));

        int count = 0;
        while (true) {
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            SocketAddress receive = datagramChannel.receive(buffer);
            buffer.flip();
            log.info("读取到数据：{}", buffer.array().length);
            buffer.clear();
            count++;
            log.info("累计：{}", count);
        }
    }

    @Test
    public void datagramChannelClient() throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.bind(new InetSocketAddress("localhost", 8686));

        FileInputStream fileInputStream = new FileInputStream(new File("D:\\迅雷下载\\MyWorld.mkv"));
        FileChannel fileChannel = fileInputStream.getChannel();
        int count = 0;
        while (true) {
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            int read = fileChannel.read(buffer);
            if (read == -1) break;
            log.info("读取到数据：{}", read);
            buffer.flip();
            datagramChannel.send(buffer, new InetSocketAddress("localhost", 8787));
            count++;
            log.info("累计：{}", count);
        }
    }

    /**
     * -------------------------NIO DisCardServer Test-----------------------------------------
     */
    @Test
    public void disCardServer() throws IOException {
        // 获取Selector选择器，Selector是实现nio多路复用的核心
        Selector selector = Selector.open();

        ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.bind(new InetSocketAddress("localhost", 8787));
        server.register(selector, SelectionKey.OP_ACCEPT);

        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey item = iterator.next();
                if (item.isAcceptable()) {
                    // 如果是连接事件
                    SocketChannel accept = server.accept();
                    accept.configureBlocking(false);
                    // accept.register(selector, SelectionKey.OP_READ);
                    log.info("客户端连接成功：{}", accept.getRemoteAddress());
                    accept.close();
                    log.info("客户端丢弃");
                }
                iterator.remove();
            }

        }
    }

    @Test
    public void disCardClient() throws IOException {
        SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 8787));
        // client.bind(new InetSocketAddress("localhost", 8788));
    }

    /**
     * -------------------------EchoServer Test-----------------------------------------
     */
    @Test
    public void echoServer() throws IOException {
        Selector selector = Selector.open();

        ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.bind(new InetSocketAddress("localhost", 8787));
        server.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            // 查询如果没有事件发生就可以起做其他的操作了，无需阻塞在这里
            if (selector.select(1000) == 0) {
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey item = iterator.next();
                if (item.isAcceptable()) {
                    // 如果是连接事件
                    SocketChannel accept = server.accept();
                    accept.configureBlocking(false);
                    accept.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(4096));
                    log.info("客户端连接成功：{}", accept.getRemoteAddress());
                    accept.write(ByteBuffer.wrap("连接成功".getBytes()));
                } else if (item.isReadable()) {
                    try {
                        // 如果是读事件
                        SocketChannel channel = (SocketChannel) item.channel();

                        ByteBuffer buffer = (ByteBuffer) item.attachment();
                        int read = channel.read(buffer);
                        if (read == -1) {
                            log.info("客户端断开连接");
                            item.cancel();
                            continue;
                        }
                        buffer.flip();
                        log.info("收到客户端消息：{}", new String(buffer.array(), 0, buffer.limit()));
                        channel.write(buffer);
                        buffer.clear();
                    } catch (IOException e) {
                        // 处理客户端暴力断开的情况
                        log.info("客户端断开连接");
                        item.cancel();
                    }
                }

            }
            iterator.remove();
        }
    }


    @Test
    public void echoClient() throws IOException {
        SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 8787));
        client.configureBlocking(true);
        // 获取键盘输入
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入：");
        while (true) {
            ByteBuffer allocate = ByteBuffer.allocate(4096);
            int read = client.read(allocate);
            if (read > 0) {
                log.info("收到服务端消息：{}", new String(allocate.array(), 0, read));
            }
            String inStr = scanner.next();
            client.write(ByteBuffer.wrap(inStr.getBytes()));
        }
    }

}
