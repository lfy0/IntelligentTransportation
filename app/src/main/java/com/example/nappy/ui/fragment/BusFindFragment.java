package com.example.nappy.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nappy.R;
import com.example.nappy.adapter.BusDialogItemAdapter;
import com.example.nappy.adapter.BusListAdapter;
import com.example.nappy.beans.BusBean;
import com.example.nappy.beans.BusInfoBean;
import com.example.nappy.beans.BusSumBean;
import com.example.nappy.inter.NetworkOnResult;
import com.example.nappy.network.NetRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 公交查询
 */
public class BusFindFragment extends Fragment {


    private TextView mTvChengzai;
    private Button mBtnXiangqing;
    //折叠listview
    private ExpandableListView mElBusZhanlist;

    private static final String TAG = "BusFindFragment";

    List<String> groupList;
    List<List<BusBean>> childList;
    private BusListAdapter busAdapter;
    private NetRequest BusZhanTaiNet;
    private NetRequest BusCountNet;

    private BusSumBean busSumBean;
    ListView mLvBusInfo;
    Button mBtnBack;


    public BusFindFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bus_find, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        mBtnXiangqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shwoAleDialog();
            }
        });
    }

    private void initData() {
        groupList = new ArrayList<>();
        childList = new ArrayList<>();
        mElBusZhanlist.setGroupIndicator(null);
        groupList.add("中医院站");
        groupList.add("联想大厦站");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAdapter();
        request();
    }

    private void initAdapter() {
        List<BusBean> beans = new ArrayList<>();
        beans.add(new BusBean("一号（101）人     5分钟到达", "距离站台100米"));
        beans.add(new BusBean("二号（101）人     5分钟到达", "距离站台1000米"));
        childList.add(beans);
        childList.add(beans);
        busAdapter = new BusListAdapter(getActivity(), groupList, childList);
        //展开listview
        mElBusZhanlist.setAdapter(busAdapter);
        for (int i=0; i<2; i++) {
            mElBusZhanlist.expandGroup(i);
        }
    }

    //换算成米/分钟
    float dis = 1000 / 3f;

    private void request() {
        BusZhanTaiNet = new NetRequest("distance.bus")
                .setLoopTime(3000)
                .setLoop(true)
                .setNetworkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Log.i(TAG, "onSuccess: " + jsonObject.toString());
                        try {
                            if ("成功".equals(jsonObject.getString("response"))) {
                                jsonObject = jsonObject.getJSONObject("result");
                                //先解析出服务器返回数据
                                BusInfoBean busInfoBean = new Gson().fromJson(jsonObject.toString(), BusInfoBean.class);
                                Log.i(TAG, "onSuccess: " + busInfoBean);
                                //拿服务器数据去适应界面
                                List<BusBean> busList = new ArrayList<BusBean>();
                                busList.add(new BusBean("一号(" + busInfoBean.getOneBusPerson() + ")  "
                                        + ((int) (busInfoBean.getOneBus_OneSite() / dis)) + "分钟到达"
                                        , "距离站台" + busInfoBean.getOneBus_OneSite() + "米"));
                                busList.add(new BusBean("二号(" + busInfoBean.getTwoBusPerson() + ")  "
                                        + ((int) (busInfoBean.getOneBus_TwoSite() / dis)) + "分钟到达"
                                        , "距离站台" + busInfoBean.getOneBus_TwoSite() + "米"));
                                Collections.sort(busList);
                                childList.set(0, busList);
                                List<BusBean> busList2 = new ArrayList<BusBean>();
                                busList2.add(new BusBean("一号(" + busInfoBean.getOneBusPerson() + ")  "
                                        + ((int) (busInfoBean.getTwoBus_OneSite() / dis)) + "分钟到达"
                                        , "距离站台" + busInfoBean.getTwoBus_OneSite() + "米"));
                                busList2.add(new BusBean("二号(" + busInfoBean.getTwoBusPerson() + ")  "
                                        + ((int) (busInfoBean.getTwoBus_TwoSite() / dis)) + "分钟到达"
                                        , "距离站台" + busInfoBean.getTwoBus_TwoSite() + "米"));
                                Collections.sort(busList2);
                                childList.set(1, busList2);

                                Log.i(TAG, "onSuccess: " + childList.size());
                                busAdapter.setChildList(childList);
                                busAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
        //获取承载人数
        BusCountNet = new NetRequest("passenger.bus")
                .setLoop(true)
                .setLoopTime(3000)
                .setNetworkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        try {
                            if ("成功".equals(jsonObject.getString("response"))) {
                                jsonObject = jsonObject.getJSONObject("result");
                                busSumBean = new Gson().fromJson(jsonObject.toString(), BusSumBean.class);
                                mTvChengzai.setText("当前承载能力" + busSumBean.getCount() + "人");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusCountNet.clean();
        BusZhanTaiNet.clean();
    }

    private void initView() {
        mTvChengzai = (TextView) getView().findViewById(R.id.tv_chengzai);
        mBtnXiangqing = (Button) getView().findViewById(R.id.btn_xiangqing);
        mElBusZhanlist = (ExpandableListView) getView().findViewById(R.id.el_bus_zhanlist);
    }

    private void shwoAleDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("公交车载客情况统计");
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.bus_details_layout, null);
        mLvBusInfo = (ListView) view.findViewById(R.id.lv_bus_info);
        mBtnBack = (Button) view.findViewById(R.id.btn_back);
        TextView tvNum=view.findViewById(R.id.tv_num);
        TextView tvPersonNum=view.findViewById(R.id.tv_personNum);
        List<String> personNum=new ArrayList<>();
        if (busSumBean!=null){
            Log.i(TAG, "shwoAleDialog: "+busSumBean.getCount());
            personNum.add(busSumBean.getOneBus()+"");
            personNum.add(busSumBean.getTwoBus()+"");
            personNum.add(busSumBean.getThreeBus()+"");
            personNum.add(busSumBean.getFifteenBus()+"");
        }
        Log.i(TAG, "shwoAleDialog: "+personNum.size());
        BusDialogItemAdapter busDialogItemAdapter=new BusDialogItemAdapter(getActivity(),personNum);
        mLvBusInfo.setAdapter(busDialogItemAdapter);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        tvPersonNum.setText(busSumBean.getCount()+"");
        alertDialog.setView(view);
        alertDialog.show();
    }
}