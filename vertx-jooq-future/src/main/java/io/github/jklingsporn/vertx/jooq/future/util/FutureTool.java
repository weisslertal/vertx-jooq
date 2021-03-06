package io.github.jklingsporn.vertx.jooq.future.util;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import me.escoffier.vertx.completablefuture.VertxCompletableFuture;

import java.util.concurrent.CompletableFuture;

/**
 * Created by jensklingsporn on 18.04.17.
 */
public class FutureTool {
    private FutureTool(){}

    /**
     * @param blockingCodeHandler
     * @param vertx
     * @param <T>
     * @return a CompletableFuture that is completed when the blocking code has been executed by Vertx.
     */
    public static <T> CompletableFuture<T> executeBlocking(Handler<Future<T>> blockingCodeHandler,Vertx vertx){
        VertxCompletableFuture<T> future = new VertxCompletableFuture<>(vertx);
        vertx.executeBlocking(blockingCodeHandler, createCompletionHandler(future));
        return future;
    }

    private static <T> Handler<AsyncResult<T>> createCompletionHandler(VertxCompletableFuture<T> future) {
        return h->{
            if(h.succeeded()){
                future.complete(h.result());
            }else{
                future.completeExceptionally(h.cause());
            }
        };
    }

    /**
     *
     * @param e
     * @param vertx
     * @param <T>
     * @return a CompletableFuture that is failed.
     */
    public static <T> CompletableFuture<T> failedFuture(Throwable e,Vertx vertx){
        CompletableFuture<T> future = new VertxCompletableFuture<>(vertx);
        future.completeExceptionally(e);
        return future;
    }

}
