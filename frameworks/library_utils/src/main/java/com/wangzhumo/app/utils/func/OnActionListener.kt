package com.wangzhumo.app.utils.func

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2021/5/19  11:08 上午
 *
 * 监听
 */
interface OnActionListener {

    /**
     * @param action 发起的事件
     * @param data 数据
     */
    fun onAction(action: Int, data: Any)
}