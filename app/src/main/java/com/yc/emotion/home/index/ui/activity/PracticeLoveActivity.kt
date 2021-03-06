package com.yc.emotion.home.index.ui.activity

import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.net.contains.HttpConfig
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.base.ui.widget.LoadDialog
import com.yc.emotion.home.index.adapter.LoveHealingAdapter
import com.yc.emotion.home.model.bean.AResultInfo
import com.yc.emotion.home.model.bean.LoveHealingBean
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.utils.CommonInfoHelper
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.activity_practice_love.*
import rx.Subscriber
import java.util.*

/**
 * Created by mayn on 2019/6/18.
 */

//LoveHealingActivity
class PracticeLoveActivity : BaseSameActivity() {


    private var loveHealingBeans: MutableList<LoveHealingBean>? = null
    private var mAdapter: LoveHealingAdapter? = null
    private val PAGE_SIZE = 15
    private var PAGE_NUM = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_practice_love
    }

    override fun initViews() {
        //        mLoveEngine = new LoveEngine(this);


        initRecyclerView()
        lazyLoad()

    }

    private fun initRecyclerView() {

        val layoutManager = LinearLayoutManager(this)
        child_main_t2_t2_rv.layoutManager = layoutManager
        layoutManager.orientation = RecyclerView.VERTICAL
        mAdapter = LoveHealingAdapter(loveHealingBeans)
        child_main_t2_t2_rv.adapter = mAdapter
        //设置增加或删除条目的动画
        child_main_t2_t2_rv.itemAnimator = DefaultItemAnimator()
        initListener()
    }

    private fun initListener() {
        swipeRefreshLayout.setColorSchemeResources(R.color.red_crimson)
        swipeRefreshLayout.setOnRefreshListener {
            PAGE_NUM = 1
            netData(true)
        }

        mAdapter?.setOnLoadMoreListener({ netData(false) }, child_main_t2_t2_rv)

        mAdapter?.setOnItemClickListener { adapter, view, position ->
            val item = mAdapter?.getItem(position)
            item?.let {
                if (LoveHealingBean.VIEW_ITEM == it.type) {
                    LoveUpDownPhotoActivity.startLoveUpDownPhotoActivity(this@PracticeLoveActivity, position - 2, "lovewords/recommend")
                }
            }
        }

    }


    protected fun lazyLoad() {
        MobclickAgent.onEvent(this, ConstantKey.UM_HONEYEDWORDS_ID)
        netData(false)
    }

    private fun netData(isRefesh: Boolean) {
        //        loveHealingBeans = (List<LoveHealingBean>) mCacheWorker.getCache(this, "maint2_t2_lovewords_recommend");
        if (PAGE_NUM == 1) {
            CommonInfoHelper.getO(this, "maint2_t2_lovewords_recommend", object : TypeReference<List<LoveHealingBean>>() {

            }.type, object : CommonInfoHelper.OnParseListener<List<LoveHealingBean>> {


                override fun onParse(o: List<LoveHealingBean>?) {
                    loveHealingBeans = o as MutableList<LoveHealingBean>?
                    if (loveHealingBeans != null && loveHealingBeans!!.size > 0) {
                        mAdapter?.setNewData(o)
                    }
                }
            })
        }

        if (PAGE_NUM == 1 && !isRefesh) {
            mLoadingDialog = LoadDialog(this)
            mLoadingDialog?.showLoadingDialog()
        }
        mLoveEngine?.recommendLovewords(UserInfoHelper.instance.getUid().toString(), PAGE_NUM.toString(), PAGE_SIZE.toString(), "lovewords/recommend")
                ?.subscribe(object : Subscriber<AResultInfo<List<LoveHealingBean>>>() {

                    override fun onNext(t: AResultInfo<List<LoveHealingBean>>?) {
                        if (PAGE_NUM == 1 && !isRefesh) {
                            mLoadingDialog?.dismissLoadingDialog()
                        }
                        if (swipeRefreshLayout.isRefreshing) {
                            swipeRefreshLayout.isRefreshing = false
                        }
                        t?.let {
                            if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                                val loveHealingBeanList = t.data
                                createNewData(loveHealingBeanList)
                            }
                        }

                    }

                    override fun onCompleted() {
                        if (PAGE_NUM == 1 && !isRefesh) {
                            mLoadingDialog?.dismissLoadingDialog()
                        }
                        if (swipeRefreshLayout.isRefreshing) {
                            swipeRefreshLayout.isRefreshing = false
                        }
                    }

                    override fun onError(e: Throwable?) {
                        if (PAGE_NUM == 1 && !isRefesh) {
                            mLoadingDialog?.dismissLoadingDialog()
                        }
                        if (swipeRefreshLayout.isRefreshing) {
                            swipeRefreshLayout.isRefreshing = false
                        }
                    }
                })
    }

    private fun createNewData(loveHealingBeanList: List<LoveHealingBean>?) {
        loveHealingBeans = ArrayList()
        if (PAGE_NUM == 1) {
            loveHealingBeans?.add(LoveHealingBean(LoveHealingBean.VIEW_TITLE, "title_img"))
            loveHealingBeans?.add(LoveHealingBean(LoveHealingBean.VIEW_ITEM_ITEM, "为你推荐 "))
        }
        if (loveHealingBeanList != null) {
            for (i in loveHealingBeanList.indices) {
                val loveHealingBean = loveHealingBeanList[i]
                loveHealingBeans?.add(LoveHealingBean(LoveHealingBean.VIEW_ITEM, loveHealingBean.chat_count, loveHealingBean.chat_name, loveHealingBean.id, loveHealingBean.quiz_sex, loveHealingBean.search_type))
            }
        }

        if (PAGE_NUM == 1) {
            mAdapter?.setNewData(loveHealingBeans as List<LoveHealingBean?>?)

            CommonInfoHelper.setO(this, loveHealingBeans, "maint2_t2_lovewords_recommend")
        } else {
            loveHealingBeans?.let {

                mAdapter?.addData(it)
            }
        }
        if (loveHealingBeanList != null && loveHealingBeanList.isNotEmpty()) {
            mAdapter?.loadMoreComplete()
            PAGE_NUM++
        } else {
            mAdapter?.loadMoreEnd()
        }

    }


    override fun offerActivityTitle(): String {
        return "实战情话"
    }
}
