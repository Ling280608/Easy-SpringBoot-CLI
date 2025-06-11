package com.ling.cli.utils.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author ling
 * @description: UDP请求工具类
 */
public class UDPNettyClientUtils {
    private static final ConcurrentHashMap<String, Channel> channelCache = new ConcurrentHashMap<>();
    private static final EventLoopGroup group = new NioEventLoopGroup();
    private static final Bootstrap bootstrap;
    private static final Logger log = LoggerFactory.getLogger("network");

    static {
        bootstrap = new Bootstrap()
                .group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true);
    }

    public static CompletableFuture<Channel> getChannel(String targetHost, int targetPort){
        return getChannel(null,null,targetHost,targetPort);
    }

    public static CompletableFuture<Channel> getChannel(Integer localPort,String targetHost, int targetPort){
        return getChannel(null,localPort,targetHost,targetPort);
    }

    /**
     * 获取或创建一个到指定目标的UDP通道
     * @param localHost 本地服务器的地址
     * @param localPort 本地服务器的端口
     * @param targetHost 目标服务器的地址
     * @param targetPort 目标服务器的端口
     * @return CompletableFuture<Channel> 一个异步返回Channel对象的Future
     */
    public static CompletableFuture<Channel> getChannel(String localHost,Integer localPort,String targetHost, int targetPort) {
        String key = targetHost + ":" + targetPort;
        Channel channel = channelCache.get(key);
        if (channel != null && channel.isActive()) {
            // 如果缓存中有活跃的通道，直接返回
            return CompletableFuture.completedFuture(channel);
        }

        // 创建一个Future，用于异步返回新建的通道
        CompletableFuture<Channel> future = new CompletableFuture<>();
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) {
                // 初始化udp客户端通道
                ch.pipeline()
                        .addLast(new StringEncoder())
                        .addLast(new StringDecoder());
            }
        });
        ChannelFuture bindChannelFuture = null;
        if(localHost != null && localPort != null){
            bindChannelFuture = bootstrap.bind(new InetSocketAddress(localHost, localPort));
        } else {
            bindChannelFuture = bootstrap.bind(localPort != null? localPort : 0);
        }

                // 创建并绑定UDP通道
        bindChannelFuture.addListener((ChannelFutureListener) cf -> {
                    if (cf.isSuccess()) {
                        // 通道创建成功,获取新创建的Channel,存入缓存
                        Channel newChannel = cf.channel();
                        channelCache.put(key, newChannel);
                        log.info("UDP客户端初始化成功: {}", key);
                        // 添加关闭监听，当通道关闭时从缓存中移除
                        newChannel.closeFuture().addListener((ChannelFutureListener) closeFuture -> {
                            channelCache.remove(key);
                            log.warn("UDP客户端关闭: {}", key);
                        });
                        // 完成Future，返回Channel
                        future.complete(newChannel);
                    } else {
                        // 通道创建失败
                        log.error("UDP客户端初始化失败: {} -> {}", key, cf.cause().getMessage());
                        future.completeExceptionally(cf.cause());
                    }
                });

        return future;  // 返回异步结果
    }

    public static String sendMessage(Integer localPort,String targetHost, int targetPort, String message, boolean waitForResponse) {
        return sendMessage(null,localPort,targetHost, targetPort, message, waitForResponse);
    }

    public static String sendMessage(String targetHost, int targetPort, String message, boolean waitForResponse) {
        return sendMessage(null,null,targetHost, targetPort, message, waitForResponse);
    }

    /**
     * 发送消息到目标服务器
     * @param localHost 本地服务器的地址
     * @param localPort 本地服务器的端口
     * @param targetHost 目标服务器的地址
     * @param targetPort 目标服务器的端口
     * @param message         要发送的消息内容
     * @param waitForResponse 是否等待服务器响应
     * @return 如果等待响应，返回服务器响应或错误信息；否则返回发送状态
     */
    public static String sendMessage(String localHost,Integer localPort,String targetHost, int targetPort, String message, boolean waitForResponse) {
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        InetSocketAddress targetAddress = new InetSocketAddress(targetHost, targetPort);  // 目标服务器地址
        String serverAddress = targetHost + ":" + targetPort;
        getChannel(localHost,localPort,targetHost, targetPort).thenAccept(channel -> {
            if (waitForResponse) {
                // 如果需要等待响应，添加一个临时处理器来接收特定目标的响应
                channel.pipeline().addLast(new SimpleChannelInboundHandler<Object>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
                        // 验证响应是否来自目标服务器
                        if (msg instanceof String) {
                            log.info("收到响应: {} - > {}", serverAddress, msg);
                            responseFuture.complete((String) msg);
                            ctx.pipeline().remove(this);
                        }
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                        // 异常发生时，通知Future并移除处理器
                        responseFuture.completeExceptionally(cause);
                        log.error("发生异常: {} -> {}", serverAddress, cause.getMessage());
                        ctx.pipeline().remove(this);
                    }
                });
                // 发送UDP数据包
                channel.writeAndFlush(new DatagramPacket(
                        Unpooled.copiedBuffer(message, CharsetUtil.UTF_8),
                        targetAddress));
            } else {
                // 不等待响应，直接发送数据包并监听发送结果
                ChannelFuture writeFuture = channel.writeAndFlush(new DatagramPacket(
                        Unpooled.copiedBuffer(message, CharsetUtil.UTF_8),
                        targetAddress));
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
            responseFuture.completeExceptionally(throwable);  // 通道创建失败
            return null;
        });

        try {
            if (waitForResponse) {
                // 等待请求响应，最多2秒
                return responseFuture.get(2, TimeUnit.SECONDS);
            } else {
                // 不等待，直接返回发送结果
                return responseFuture.get();
            }
        } catch (TimeoutException e) {
            log.error("tcp服务响应超时: {} -> {}", serverAddress, e.getMessage());
            return null;  // 超时返回
        } catch (Exception e) {
            log.error("tcp服务响应异常: {} -> {} \n\n", serverAddress, e.getMessage());
            return null;  // 其他错误返回
        }
    }

    /**
     * 关闭连接池，释放所有资源
     */
    public static void shutdown() {
        channelCache.values().forEach(Channel::close);  // 关闭所有缓存的通道
        group.shutdownGracefully();  // 优雅关闭事件循环组
    }
}
