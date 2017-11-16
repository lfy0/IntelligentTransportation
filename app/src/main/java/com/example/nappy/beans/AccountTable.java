package com.example.nappy.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by liufangya on 2017/10/18.
 */
@DatabaseTable(tableName = "accounts")
public class AccountTable {

    @DatabaseField()
    String carId;//车牌号
    @DatabaseField()
    int carMoney;//充值金额
    @DatabaseField()
    int carBalance;//账户余额
    @DatabaseField()
    String accountPerson;//充值人
    @DatabaseField()
    String accountTime;//充值时间
    @DatabaseField()
    String accountWeek;

    public AccountTable() {

    }

    public AccountTable(String carId, int carMoney, int carBalance, String accountPerson, String accountTime, String accountWeek) {
        this.carId = carId;
        this.carMoney = carMoney;
        this.carBalance = carBalance;
        this.accountPerson = accountPerson;
        this.accountTime = accountTime;
        this.accountWeek = accountWeek;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public int getCarMoney() {
        return carMoney;
    }

    public void setCarMoney(int carMoney) {
        this.carMoney = carMoney;
    }

    public int getCarBalance() {
        return carBalance;
    }

    public void setCarBalance(int carBalance) {
        this.carBalance = carBalance;
    }

    public String getAccountPerson() {
        return accountPerson;
    }

    public void setAccountPerson(String accountPerson) {
        this.accountPerson = accountPerson;
    }

    public String getAccountTime() {
        return accountTime;
    }

    public void setAccountTime(String accountTime) {
        this.accountTime = accountTime;
    }

    public String getAccountWeek() {
        return accountWeek;
    }

    public void setAccountWeek(String accountWeek) {
        this.accountWeek = accountWeek;
    }

    @Override
    public String toString() {
        return "AccountTable{" +
                "carId='" + carId + '\'' +
                ", carMoney=" + carMoney +
                ", carBalance=" + carBalance +
                ", accountPerson='" + accountPerson + '\'' +
                ", accountTime='" + accountTime + '\'' +
                ", accountWeek='" + accountWeek + '\'' +
                '}';
    }
}
