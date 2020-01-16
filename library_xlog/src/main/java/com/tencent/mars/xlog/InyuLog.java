package com.tencent.mars.xlog;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-06-20  17:33
 */
public class InyuLog{


    /**
     * use e(tag, format, obj) instead
     *
     * @param tag
     * @param msg
     */
    public static void e(final String tag,final String fileName, final String methodName,final int line, final String msg,final Object... obj) {
       Log.e(tag, fileName, methodName, line, msg, obj);
    }


    /**
     * use d(tag, format, obj) instead
     *
     * @param tag
     * @param msg
     */
    public static void d(final String tag,final String fileName, final String methodName,final int line, final String msg,final Object... obj) {
        Log.d(tag, fileName, methodName, line, msg, obj);
    }


    /**
     * use i(tag, format, obj) instead
     *
     * @param tag
     * @param msg
     */
    public static void i(final String tag,final String fileName, final String methodName,final int line, final String msg,final Object... obj) {
        Log.i(tag, fileName, methodName, line, msg, obj);
    }

    /**
     * use printErrStackTrace(tag, format, obj) instead
     *
     * @param tag
     * @param msg
     */
    public static void printErrStackTrace(String tag, Throwable tr, final String fileName, final String methodName, int line,final String msg, final Object... obj){
        Log.printErrStackTrace(tag, tr, fileName, methodName, line, msg, obj);
    }


    /**
     * close log
     */
    public static void close(){
        Log.appenderClose();
    }


    /**
     *
     * @param aysn 是否异步
     */
    public static void flush(boolean aysn){
        Log.appenderFlush(aysn);
    }


}
