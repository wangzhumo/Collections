package com.wangzhumo.app.playground;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wangzhumo.app.base.IRoute;
import com.wangzhumo.app.origin.base.BaseBindingActivity;
import com.wangzhumo.app.playground.databinding.ActivityInYuSocketBinding;
import com.wangzhumo.app.playground.netty.ByteChannelHandler;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 1/28/21  9:18 PM
 * <p>
 * 模拟一下Socket
 */
@Route(path = IRoute.SOCKET_ACTIVITY)
public class InYuSocketActivity extends BaseBindingActivity<ActivityInYuSocketBinding> {

    private static final int TIMEOUT_TIME = 5;

    private static final String TAG = "InYuSocket";
    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        vBinding.connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });
    }


    private void connect() {
        //初始化线程组
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class).group(nioEventLoopGroup);
        bootstrap.option(ChannelOption.TCP_NODELAY, true); //无阻塞
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true); //长连接
        bootstrap.option(ChannelOption.SO_TIMEOUT, TIMEOUT_TIME); //收发超时
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                        .addLast(new ByteArrayDecoder())  //接收解码方式
                        .addLast(new ByteArrayEncoder())  //发送编码方式
                        .addLast(new ByteChannelHandler()); //处理数据接收
            }
        });

        //开始建立连接并监听返回
        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("39.96.55.171",51003));
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                Log.d(TAG, "connect success !");
            }
        });
    }
}