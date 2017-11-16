package com.example.nappy.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nappy.R;
import com.example.nappy.inter.NetworkOnResult;
import com.example.nappy.network.NetRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by liufangya on 2017/10/11.
 * 路况查询
 */
public class TrafficQueryFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private int[] colors = {
            0,
            Color.parseColor("#6ab82e"),
            Color.parseColor("#ece93a"),
            Color.parseColor("#f49b25"),
            Color.parseColor("#e33532"),
            Color.parseColor("#b01e23"),
    };//不同警戒状态

    private TextView mTvTrafficHuacheng1;
    private TextView mTvTrafficHuacheng2;
    private TextView mTvTrafficHuacheng3;
    private TextView mTvTrafficHuacheng4;

    private TextView mTvTrafficXueyuan;
    private TextView mTvTrafficLianxiang;
    private TextView mTvTrafficXingfu;
    private TextView mTvTrafficYiyuan;
    private TextView mTvTrafficTingchechang;

    private TextView mTextWendu;
    private TextView mTextSidu;
    private TextView mTextPm25;

    private TextView mTextYmd;
    private TextView mTextWeek;
    private static final String TAG = "TrafficQueryFragment";
    private ImageView mIvFinshMap;

    public TrafficQueryFragment() {

    }

    public static TrafficQueryFragment newInstance(String param1, String param2) {
        TrafficQueryFragment fragment = new TrafficQueryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_traffic_query, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDate();
        initData();
        initTraffic();
        initEvent();
    }

    private void initTraffic() {
        netRequestTraffic = new NetRequest("status.war")
                .setLoop(true)
                .setLoopTime(3000)
                .setNetworkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        try {
                            String isSuccess = jsonObject.getString("response");
                            if ("成功".equals(isSuccess)) {
                                JSONObject jsonObj = jsonObject.getJSONObject("result");
                                int one = jsonObj.getInt("one");
                                int two = jsonObj.getInt("two");
                                int three = jsonObj.getInt("three");
                                int four = jsonObj.getInt("four");
                                int five = jsonObj.getInt("five");
                                int six = jsonObj.getInt("six");
                                int seven = jsonObj.getInt("seven");
                                mTvTrafficXueyuan.setBackgroundColor(colors[one]);
                                mTvTrafficLianxiang.setBackgroundColor(colors[two]);
                                mTvTrafficYiyuan.setBackgroundColor(colors[three]);
                                mTvTrafficXingfu.setBackgroundColor(colors[four]);
                                setHuanChengBack(colors[five]);
                                mTvTrafficHuacheng4.setBackgroundColor(colors[six]);
                                mTvTrafficTingchechang.setBackgroundColor(colors[seven]);
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

    private void initEvent() {
        mIvFinshMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                netRequestSensor.clean();
                initData();
            }

        });
    }

    private NetRequest netRequestSensor;
    private NetRequest netRequestTraffic;

    private void initData() {
        netRequestSensor = new NetRequest("environment.war ")
                .setLoopTime(3000)
                .setLoop(true)
                .setNetworkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        try {
                            String isSuccess = jsonObject.getString("response");
                            if ("成功".equals(isSuccess)) {
                                JSONObject jsonObj = jsonObject.getJSONObject("result");
                                String humidity = jsonObj.getString("humidity");
                                String pm2 = jsonObj.getString("pm2");
                                String temperature = jsonObj.getString("temperature");
                                mTextWendu.setText("温度:" + temperature + " C");
                                mTextSidu.setText("湿度:" + humidity + " ug/m3");
                                mTextPm25.setText("PM2.5:" + pm2 + "%");
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

    private void initDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfWeek = new SimpleDateFormat("EEEE");
        mTextYmd.setText(sdf.format(date));
        mTextWeek.setText(getWeekOfDate(date));
    }

    public void onButtonPressed(Object object) {
        if (mListener != null) {
            mListener.onFragmentInteraction(object);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initView() {
        mTvTrafficHuacheng1 = (TextView) getView().findViewById(R.id.tv_traffic_huacheng1);
        mTvTrafficHuacheng2 = (TextView) getView().findViewById(R.id.tv_traffic_huacheng2);
        mTvTrafficHuacheng3 = (TextView) getView().findViewById(R.id.tv_traffic_huacheng3);
        mTvTrafficHuacheng4 = (TextView) getView().findViewById(R.id.tv_traffic_huacheng4);
        mTvTrafficXueyuan = (TextView) getView().findViewById(R.id.tv_traffic_xueyuan);
        mTvTrafficLianxiang = (TextView) getView().findViewById(R.id.tv_traffic_lianxiang);
        mTvTrafficXingfu = (TextView) getView().findViewById(R.id.tv_traffic_xingfu);
        mTvTrafficYiyuan = (TextView) getView().findViewById(R.id.tv_traffic_yiyuan);
        mTvTrafficTingchechang = (TextView) getView().findViewById(R.id.tv_traffic_tingchechang);
        mTextWendu = (TextView) getView().findViewById(R.id.text_wendu);
        mTextSidu = (TextView) getView().findViewById(R.id.text_sidu);
        mTextPm25 = (TextView) getView().findViewById(R.id.text_pm2_5);
        mTextYmd = (TextView) getView().findViewById(R.id.text_ymd);
        mTextWeek = (TextView) getView().findViewById(R.id.text_week);
        mIvFinshMap = (ImageView) getView().findViewById(R.id.iv_finshMap);
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Object object);
    }


    public void setHuanChengBack(int color) {
        mTvTrafficHuacheng1.setBackgroundColor(color);
        mTvTrafficHuacheng2.setBackgroundColor(color);
        mTvTrafficHuacheng3.setBackgroundColor(color);
    }


    /**
     * 获取当前日期是星期几<br>
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        netRequestSensor.clean();
        netRequestTraffic.clean();
    }
}
