package com.example.nappy.inter;

import org.json.JSONObject;

/**
 * Created by liufangya on 2017/10/12.
 */

public interface NetworkOnResult {

    void onSuccess(JSONObject jsonObject);

    void onError();

}
