package shop.fly.com.shop.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2016/6/16.
 */
public class FragmentTabAdapter {

    private List<Fragment> fragments;
    private FragmentActivity fragmentActivity; // Fragment所属的Activity
    private int fragmentContentId; // Activity中所要被替换的区域的id
    private int currentTab; // 当前Tab页面索引
    private int fragmentId = 0;
    public FragmentTabAdapter(List<Fragment> fragments, FragmentActivity fragmentActivity, int fragmentContentId, int currentTab) {
        this.fragments = fragments;
        this.fragmentActivity = fragmentActivity;
        this.fragmentContentId = fragmentContentId;
        fragmentId = fragmentContentId;
        this.currentTab = currentTab;

        // 默认显示第一页
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        switch (fragmentId) {
            case 0:
                ft.add(fragmentContentId, fragments.get(0));
                break;
            case 1:
                ft.add(fragmentContentId, fragments.get(1));
                break;
            case 2:
                ft.add(fragmentContentId, fragments.get(2));
                break;
            case 3:
                ft.add(fragmentContentId, fragments.get(3));
                break;
         /*   case 4:
                ft.add(fragmentContentId, fragments.get(4));
                break;*/
            default:
                break;
        }
        ft.commit();
    }

    public void getRadioGroup(int i, View vTitle, boolean show, Bundle bundle) {
        Fragment fragment = fragments.get(i);
        FragmentTransaction ft = obtainFragmentTransaction(i);
        getCurrentFragment().onPause(); // 暂停当前tab
        //                getCurrentFragment().onStop(); // 暂停当前tab
        if(fragment.isAdded()){
            //                    fragment.onStart(); // 启动目标tab的onStart()
            fragment.onResume(); // 启动目标tab的onResume()
        }else{
            if(bundle != null)
                fragment.setArguments(bundle);
            ft.add(fragmentContentId, fragment);
        }
        showTab(i); // 显示目标tab
        ft.commit();
    }


    /**
     * 切换tab
     * @param idx
     */
    private void showTab(int idx){
        for(int i = 0; i < fragments.size(); i++){
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);
            if(idx == i){
                ft.show(fragment);
            }else{
                ft.hide(fragment);
            }
            ft.commit();
        }
        currentTab = idx; // 更新目标tab为当前tab
    }

    /**
     * 获取一个带动画的FragmentTransaction
     * @param index
     * @return
     */
    private FragmentTransaction obtainFragmentTransaction(int index){
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        // 设置切换动画
        //        if(index > currentTab){
        //            ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        //        }else{
        //            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
        //        }
        return ft;
    }

    public Fragment getCurrentFragment(){
        return fragments.get(currentTab);
    }

}
