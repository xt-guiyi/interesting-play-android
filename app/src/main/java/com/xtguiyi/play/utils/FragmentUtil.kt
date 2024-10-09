package com.xtguiyi.play.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit

/**
 *  Fragment工具类
 */
object FragmentUtil {

    /**
     * 替换现有的 Fragment 为提供的 Fragment。
     *
     * @param fragmentManager 用于执行事务的 FragmentManager。
     * @param containerId 容器视图的 ID，用于放置 Fragment。
     * @param fragment 要放置在容器中的新 Fragment。
     * @param tag Fragment 的可选标签名。
     * @param enterAnim 进入动画资源 ID。
     * @param exitAnim 退出动画资源 ID。
     * @param popEnterAnim 被分离方进入动画资源 ID。
     * @param popExitAnim 被分离方退出动画资源 ID。
     */
    fun replaceFragment(
        fragmentManager: FragmentManager,
        containerId: Int,
        fragment: Fragment,
        tag: String? = null,
        enterAnim: Int? = null,
        exitAnim: Int? = null,
        popEnterAnim: Int? = null,
        popExitAnim: Int? = null,
    ): FragmentUtil {
        fragmentManager.commit {
            setReorderingAllowed(true)
            setCustomAnimations(
                enterAnim ?: -1,
                exitAnim ?: -1,
                popEnterAnim ?: -1,
                popExitAnim ?: -1
            )
            replace(containerId, fragment, tag)
            setPrimaryNavigationFragment(fragment)
            if (tag != null) addToBackStack(tag)
        }
        return this@FragmentUtil
    }

    /**
     * 添加一个 Fragment 到容器中。
     *
     * @param fragmentManager 用于执行事务的 FragmentManager。
     * @param containerId 容器视图的 ID，用于放置 Fragment。
     * @param fragment 要添加的 Fragment。
     * @param tag Fragment 的可选标签名。
     * @param isAddToBackStack 是否添加到返回堆栈。
     * @param enterAnim 进入动画资源 ID。
     * @param exitAnim 退出动画资源 ID。
     * @param popEnterAnim 被分离方进入动画资源 ID。
     * @param popExitAnim 被分离方退出动画资源 ID。
     */
    fun addFragment(
        fragmentManager: FragmentManager,
        containerId: Int,
        fragment: Fragment,
        tag: String? = null,
        isAddToBackStack: Boolean = false,
        enterAnim: Int? = null,
        exitAnim: Int? = null,
        popEnterAnim: Int? = null,
        popExitAnim: Int? = null,
    ): FragmentUtil {
        fragmentManager.commit {
            setReorderingAllowed(true)
            if(enterAnim != null && exitAnim != null && popEnterAnim != null && popExitAnim != null) {
                setCustomAnimations(
                    enterAnim,
                    exitAnim,
                    popEnterAnim,
                    popExitAnim
                )
            }
            add(containerId, fragment, tag)
            setPrimaryNavigationFragment(fragment)
            if(isAddToBackStack)  addToBackStack(tag)
        }
        return this@FragmentUtil
    }

    /**
     * 显示一个已经添加的 Fragment。
     *
     * @param fragmentManager 用于执行事务的 FragmentManager。
     * @param fragment 要显示的 Fragment。
     */
    fun showFragment(fragmentManager: FragmentManager, fragment: Fragment): FragmentUtil {
        fragmentManager.commit {
            show(fragment)
            setPrimaryNavigationFragment(fragment)
        }
        return this@FragmentUtil
    }

    /**
     * 显示一个已经添加的 Fragment（根据标签）
     * @param fragmentManager FragmentManager 实例。
     * @param tag Fragment 的标签。
     */
    fun showFragment(fragmentManager: FragmentManager, tag: String) {
        val fragment = fragmentManager.findFragmentByTag(tag)
        if (fragment != null) {
            fragmentManager.commit {
                show(fragment)
                setPrimaryNavigationFragment(fragment)
            }
        }
    }

    /**
     * 隐藏一个已经添加的 Fragment。
     *
     * @param fragmentManager 用于执行事务的 FragmentManager。
     * @param fragment 要隐藏的 Fragment。
     */
    fun hideFragment(fragmentManager: FragmentManager, fragment: Fragment): FragmentUtil {
        fragmentManager.commit {
            hide(fragment)
        }
        return this@FragmentUtil
    }

    /**
     * 隐藏一个已经添加的 Fragment(根据标签)
     * @param fragmentManager FragmentManager 实例。
     * @param tag Fragment 的标签。
     */
    fun hideFragment(fragmentManager: FragmentManager, tag: String) {
        val fragment = fragmentManager.findFragmentByTag(tag)
        if (fragment != null) {
            fragmentManager.commit {
                hide(fragment)
            }
        }
    }

    /**
     * 移除一个已经添加的 Fragment。
     *
     * @param fragmentManager 用于执行事务的 FragmentManager。
     * @param fragment 要移除的 Fragment。
     */
    fun removeFragment(fragmentManager: FragmentManager, fragment: Fragment): FragmentUtil {
        fragmentManager.commit {
            remove(fragment)
        }
        return this@FragmentUtil
    }

    /**
     * 移除一个已经添加的 Fragment。(根据标签)
     *
     * @param fragmentManager 用于执行事务的 FragmentManager。
     * @param tag 要移除的 Fragment标签·。
     */
    fun removeFragmentByTag(fragmentManager: FragmentManager, tag: String): FragmentUtil {
        fragmentManager.commit {
            removeFragmentByTag(fragmentManager, tag)
        }
        return this@FragmentUtil
    }

    /**
     * 通过primaryNavigationFragment,获取当前显示的 Fragment。
     * @param fragmentManager 用于获取 Fragment 的 FragmentManager。
     * @return 当前显示的 Fragment，如果没有显示的 Fragment 返回 null。
     */
    fun getCurrentFragment(fragmentManager: FragmentManager): Fragment? {
        return fragmentManager.primaryNavigationFragment
    }

    /**
     * 通过容器 ID 获取当前显示的 Fragment。
     *
     * @param fragmentManager 用于获取 Fragment 的 FragmentManager。
     * @param containerId 容器视图的 ID。
     * @return 当前显示的 Fragment，如果没有显示的 Fragment 返回 null。
     */
    fun getCurrentFragmentByContainerId(
        fragmentManager: FragmentManager,
        containerId: Int
    ): Fragment? {
        val fragments = fragmentManager.fragments
        for (fragment in fragments) {
            if (fragment.isVisible && fragment.id == containerId) {
                return fragment
            }
        }
        return null
    }
}

