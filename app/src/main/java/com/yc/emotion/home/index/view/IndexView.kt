package com.yc.emotion.home.index.view

import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.base.view.StateDefaultImpl
import com.yc.emotion.home.index.domain.bean.SexInfo
import com.yc.emotion.home.mine.domain.bean.LiveInfo
import com.yc.emotion.home.model.bean.IndexInfo

/**
 *
 * Created by suns  on 2019/11/7 16:41.
 */
interface IndexView : IView, StateDefaultImpl {


    fun showIndexInfo(indexInfo: IndexInfo)

    fun showIcon(t: List<SexInfo>)

    fun showIndexCaches(detailInfos: IndexInfo)
    fun showIndexLiveInfos(data: List<LiveInfo>)

}