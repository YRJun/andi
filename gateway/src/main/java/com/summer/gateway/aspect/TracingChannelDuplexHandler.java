//package com.summer.gateway.aspect;
//
//import io.micrometer.context.ContextSnapshot;
//import io.micrometer.context.ContextSnapshotFactory;
//import io.netty.channel.ChannelDuplexHandler;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelPromise;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author Renjun Yu
// * @description
// * @date 2024/02/20 14:42
// */
//@Configuration
//public class TracingChannelDuplexHandler extends ChannelDuplexHandler {

//    private ContextSnapshotFactory contextSnapshotFactory;
//
//    public TracingChannelDuplexHandler(ContextSnapshotFactory contextSnapshotFactory) {
//        this.contextSnapshotFactory = contextSnapshotFactory;
//    }
//
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        try (ContextSnapshot.Scope scope = contextSnapshotFactory.setThreadLocalsFrom(ctx.channel())) {
//            ctx.fireChannelRead(msg);
//        }
//    }
//
//    @Override
//    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
//        try (ContextSnapshot.Scope scope = contextSnapshotFactory.setThreadLocalsFrom(ctx.channel())) {
//            ctx.write(msg, promise);
//        }
//    }
//
//    @Override
//    public void flush(ChannelHandlerContext ctx) {
//        try (ContextSnapshot.Scope scope = contextSnapshotFactory.setThreadLocalsFrom(ctx.channel())) {
//            ctx.flush();
//        }
//    }
//}
