package com.wangzhumo.app.utils.rv

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2021/5/19  11:08 上午
 *
 * recyclerView的监听
 */
interface OnItemActionListener {

    /**
     * @param action 发起的事件
     * @param position 位置
     * @param data 数据
     */
    fun onItemAction(action: Int, position: Int, data: Any)
}