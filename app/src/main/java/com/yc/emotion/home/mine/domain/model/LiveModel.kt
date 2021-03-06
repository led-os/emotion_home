package com.yc.emotion.home.mine.domain.model

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.constant.URLConfig
import com.yc.emotion.home.mine.domain.bean.LiveInfo
import rx.Observable

/**
 * Created by suns  on 2020/6/5 09:21.
 */
class LiveModel(override var context: Context?) : IModel {


    /**
     * 导师登录房间
     */
    fun liveLogin(username: String?, password: String?): Observable<ResultInfo<LiveInfo>> {
        return HttpCoreEngin.get(context).rxpost(URLConfig.ANCHOR_LOGIN_URL, object : TypeReference<ResultInfo<LiveInfo>>() {}.type, mutableMapOf(
                "username" to username,
                "password" to password
        ), true, true, true) as Observable<ResultInfo<LiveInfo>>
    }

    /**
     * 创建房间
     */
    fun createRoom(userId: String?): Observable<ResultInfo<LiveInfo>> {
        return HttpCoreEngin.get(context).rxpost(URLConfig.CREATE_ROOM_URL, object : TypeReference<ResultInfo<LiveInfo>>() {}.type, mutableMapOf(
                "user_id" to userId
        ), true, true, true) as Observable<ResultInfo<LiveInfo>>
    }

}