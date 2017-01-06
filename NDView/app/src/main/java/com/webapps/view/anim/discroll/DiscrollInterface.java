package com.webapps.view.anim.discroll;

/**
 * Created by leon on 16/12/28.
 */

public interface DiscrollInterface {

    /**
     * 根据百分比执行动画
     * @param ratio 0~1
     */
    public void onDiscroll(float ratio);

    public void onResetDiscroll();

}
