package com.ling.cli.utils.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.*;

/**
 * @author ling
 * @version 1.0
 * @description: TCP请求工具类
 */
public class TCPNettyClientUtils {
    private final static ConcurrentHashMap<String, Channel> connectionCache = new ConcurrentHashMap<>();
    private final static EventLoopGroup group = new NioEventLoopGroup();
    private final static Bootstrap bootstrap;
    private static final Logger log = LoggerFactory.getLogger("network");

    static {
        bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)
                .option(ChannelOption.TCP_NODELAY, true);
    }

    /**
     * 获取或创建一个到指定目标的TCP通道
     * @param targetHost 目标服务器的地址
     * @param targetPort 目标服务器的端口
     * @return CompletableFuture<Channel> 一个异步返回Channel对象的Future
     */
    public static CompletableFuture<Channel> getConnection(String targetHost, int targetPort) {
        String key = targetHost + ":" + targetPort;
        Channel channel = connectionCache.get(key);
        if (channel != null && channel.isActive()) {
            return CompletableFuture.completedFuture(channel);
        }

        CompletableFuture<Channel> future = new CompletableFuture<>();
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) {
                ch.pipeline()
                        .addLast(new StringDecoder(), new StringEncoder());
            }
        });

        bootstrap.connect(targetHost, targetPort).addListener((ChannelFutureListener) cf -> {
            if (cf.isSuccess()) {
                Channel newChannel = cf.channel();
                connectionCache.put(key, newChannel);
                log.info("连接成功: {}", key);
                newChannel.closeFuture().addListener((ChannelFutureListener) closeFuture -> {
                    connectionCache.remove(key);
                    log.warn("连接关闭: {}", key);
                });
                future.complete(newChannel);
            } else {
                log.error("连接失败: {} -> {}", key, cf.cause().getMessage());
                future.completeExceptionally(cf.cause());
            }
        });

        return future;
    }

    public static String sendMessage(String host, int port, String message) {
        return sendMessage(host, port, message, true);
    }

    /**
     * 发送消息到目标服务器
     * @param targetHost 目标服务器的地址
     * @param targetPort 目标服务器的端口
     * @param message         要发送的消息内容
     * @param waitForResponse 是否等待服务器响应
     * @return 如果等待响应，返回服务器响应或错误信息；否则返回发送状态
     */
    public static String sendMessage(String targetHost, int targetPort, String message, boolean waitForResponse) {
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        String serverAddress = targetHost + ":" + targetPort;
        getConnection(targetHost, targetPort).thenAccept(channel -> {
            log.info("发送请求: {} -> {}", message, serverAddress);
            if (waitForResponse) {
                channel.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                        responseFuture.complete(msg);
                        log.info("收到响应: {} - > {}", serverAddress, msg);
                        ctx.pipeline().remove(this);
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                        responseFuture.completeExceptionally(cause);
                        log.error("发生异常: {} -> {}", serverAddress, cause.getMessage());
                        ctx.pipeline().remove(this);
                    }
                });
                channel.writeAndFlush(message);
            } else {
                ChannelFuture writeFuture = channel.writeAndFlush(message);
                writeFuture.addListener(future -> {
                    if (future.isSuccess()) {
                        responseFuture.complete(null);
                        log.info("发送成功: {} -> {}", serverAddress, message);
                    } else {
                        responseFuture.completeExceptionally(future.cause());
                        log.error("发送失败: {} -> {}", serverAddress, future.cause().getMessage());
                    }
                });
            }
        }).exceptionally(throwable -> {
            log.error("tcp服务连接异常: {} -> {}", serverAddress, throwable.getMessage());
            responseFuture.completeExceptionally(throwable);
            return null;
        });

        try {
            if (waitForResponse) {
                return responseFuture.get(2, TimeUnit.SECONDS);
            } else {
                return responseFuture.get();
            }
        } catch (TimeoutException e) {
            log.error("tcp服务响应超时: {} -> {}", serverAddress, e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("tcp服务响应异常: {} -> {} \n\n", serverAddress, e.getMessage());
            return null;
        }
    }

    /**
     * @description: 关闭所有连接
     */
    public static void shutdown() {
        connectionCache.values().forEach(Channel::close);
        group.shutdownGracefully();
    }

}