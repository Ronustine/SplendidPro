package com.ronustine.splendid.simplify.ticktok;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * Created by ronustine on 2016/10/29.
 * 倒计时提醒，继承TimerTask。Observer
 */
public class TimeCounter extends TimerTask {

	// 需要提醒的类
    private List<Forgotten> forgottenList;

    /**
     * 添加需要提醒的类
     * @param patientReminder
     */
    public void addReminder(Forgotten forgotten){
        if (null == forgottenList){
        	forgottenList = new ArrayList<Forgotten>();
        }
        forgottenList.add(forgotten);
    }

    /**
     * 移除不需要提醒的类
     */
    public void removeRminder(){
    	forgottenList.remove(forgottenList);
    }

    /**
     * 开始提醒
     */
    private void notifyForgotten(){
        for (Forgotten parientReminder : forgottenList) {
            parientReminder.awakeForgotten();
        }
    }

    /**
     * 重写TimerTask的run()，到时间自动提醒
     */
    @Override
    public void run() {
        notifyForgotten();
    }
}
