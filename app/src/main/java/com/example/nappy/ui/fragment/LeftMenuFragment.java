package com.example.nappy.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nappy.R;
import com.example.nappy.app.AppClient;
import com.example.nappy.beans.MenuBean;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;
/**
 * Created by liufangya on 2017/10/11.
 */
public class LeftMenuFragment extends Fragment {

    private ListView lvList;

    private List<MenuBean> menuBeen = new ArrayList<>();

    public interface  MenuItemSelect{
        void selectMenu(int select);
    }

    private MenuItemSelect menuItemSelect;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MenuItemSelect) {
            menuItemSelect = (MenuItemSelect) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MenuItemSelect");
        }
    }

    public void onButtonPressed(int select) {
        if (menuItemSelect != null) {
            menuItemSelect.selectMenu(select);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_left_menu, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAdapter();
        initEvent();
    }

    private void initEvent() {
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!"管理员".equals(AppClient.getString("identity"))&&position>=4){//如果不是管理员，就不添加红绿灯管理选项，这时候索引应该加1，跳过红绿灯管理的选项
                    position++;
                }
                onButtonPressed(position);
//                switch (position){
//                    case 0:
//
//                        break;
//                    case 1:
//                        onButtonPressed(position);
//                        break;
//                    case 2:
//                        onButtonPressed(position);
//                        break;
//                    case 3:
//                        onButtonPressed(position);
//                        break;
//                    case 4:
//                        onButtonPressed(position);
//                        break;
//                    case 5:
//                        onButtonPressed(position);
//                        break;
//                    default:
//                        break;
//                }
            }
        });
    }

    private void initAdapter() {
        menuBeen.add(new MenuBean(R.drawable.menu_item_icon1, "路况查询"));
        menuBeen.add(new MenuBean(R.drawable.menu_item_icon2, "车辆违章"));
        menuBeen.add(new MenuBean(R.drawable.menu_item_icon7, "公交查询"));
        menuBeen.add(new MenuBean(R.drawable.menu_item_icon3, "账户管理"));
        if ("管理员".equals(AppClient.getString("identity")))
            menuBeen.add(new MenuBean(R.drawable.menu_item_icon4, "红绿灯管理"));
        menuBeen.add(new MenuBean(R.drawable.menu_item_icon5, "个人中心"));
        menuBeen.add(new MenuBean(R.drawable.menu_item_icon8,"生活助手"));
        menuBeen.add(new MenuBean(R.drawable.menu_item_icon9,"数据分析"));
        menuBeen.add(new MenuBean(R.drawable.menu_item_icon6,"创意题"));
        lvList.setAdapter(new MenuAdapter());
    }



    private void initView(View view) {
        lvList =view.findViewById(R.id.lv_list);
        Log.i(TAG, "initView: "+lvList);
    }


    private class MenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return menuBeen.size();
        }

        @Override
        public Object getItem(int position) {
            return menuBeen.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MenuBean menu = menuBeen.get(position);
            ViewHoler viewHoler;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity())
                        .inflate(R.layout.menu_list_item, parent, false);
                viewHoler = new ViewHoler();
                viewHoler.icon = convertView.findViewById(R.id.iv_image);
                viewHoler.text = convertView.findViewById(R.id.tv_text);
                convertView.setTag(viewHoler);
            } else {
                viewHoler = (ViewHoler) convertView.getTag();
            }
            viewHoler.icon.setBackgroundResource(menu.getIcon());
            viewHoler.text.setText(menu.getTitle());
            return convertView;
        }

        public class ViewHoler {
            ImageView icon;
            TextView text;
        }
    }


}
