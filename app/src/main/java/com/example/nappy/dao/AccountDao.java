package com.example.nappy.dao;

import android.content.Context;

import com.example.nappy.beans.AccountTable;
import com.example.nappy.util.DataBaseHolder;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by liufangya on 2017/10/18.
 */

public class AccountDao {

    private static final String TAG = "AccountDao";
    private Context context;
    private Dao<AccountTable, Integer> userDaoOpe;
    private DataBaseHolder helper;

    public AccountDao(Context context){
        this.context=context;
    }

    public int addAccountTable(AccountTable accountTable){
        int result = 0;
        try {
            userDaoOpe=DataBaseHolder.getHelper(context).getUserDao();
            userDaoOpe.create(accountTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<AccountTable> getAccountTableAll(){
        List<AccountTable> mTableList=null;
        try {
            userDaoOpe=DataBaseHolder.getHelper(context).getUserDao();
            mTableList=userDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mTableList;
    }


}
