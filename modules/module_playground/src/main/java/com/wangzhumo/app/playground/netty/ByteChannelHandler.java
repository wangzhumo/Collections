package com.wangzhumo.app.playground.netty;

import com.tencent.mars.xlog.Log;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleUserEventChannelHandler;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 1/28/21  9:28 PM
 */
public class ByteChannelHandler extends SimpleUserEventChannelHandler<String> {
    private static final String TAG = "InYuSocket";

    @Override
    protected void eventReceived(ChannelHandlerContext ctx, String evt) throws Exception {
        Log.e(TAG,evt);
    }
}
