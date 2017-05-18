package com.example.pk.tpmresolution.utils;

/**
 * Created by kien on 10/11/16.
 */

public class AppConstants {
    public static final String PREF_KEY_LOGIN_REMEMBERLOGIN = "login_remember";
    public static final String PREF_KEY_LOGIN_USERNAME = "login_username";
    public static final String PREF_KEY_LOGIN_PASSWORD = "login_password";
    public static final String PREF_KEY_LOGIN_TOKEN = "login_token";
    public static final String PREF_KEY_LOGIN_INFO = "login_info";
    public static final String PREF_KEY_MACHINE_ID = "machine_id";

    public static final String DEFAULT_NULL_TEXT = "No data";
    public static final String KEY_MACHINE_ID = "MACHINE_ID";
    public static final String KEY_MODEL_ID = "MODEL_ID";
    public static final String KEY_TOKEN = "TOKEN";
    public static final String KEY_USER_ID = "USER_ID";
    public static final String KEY_DATE = "DATE";
    public static final String KEY_ID = "ID";


    //M.r Đông
    public static final String URL_LOGIN = "http://113.161.168.2:35/api/v1/Account/Login";
    public static final String URL_lOGOUT = "http://113.161.168.2:35/api/v1/Account/LogOut";
    public static final String URL_LOGIN_CHANGEPASS = "http://113.161.168.2:35/api/v1/Account/ChangePassword";

    public static final String URL_GET_MACHINE_DETAIL = "http://113.161.168.2:35/api/v1/Scan/QrScan?token="+KEY_TOKEN+"&machineId="+KEY_MACHINE_ID;
    public static final String URL_LIST_STATUS = "http://113.161.168.2:35/api/v1/GetData/GetMachineStatus";
    public static final String URL_CHANGE_STATUS = "http://113.161.168.2:35/api/v1/MachineStatus/ChangeMachineStatus";

    public static final String URL_REFEREBCE_INFOMATION = "http://113.161.168.2:35/api/v1/Reference/GetReference?token="+KEY_TOKEN+"&modelId="+KEY_MODEL_ID;

    public static final String URL_LIST_UNIT = "http://113.161.168.2:35/api/v1/GetData/GetPartBookPriceUnit";
    public static final String URL_LIST_USER = "http://113.161.168.2:35/api/v1/GetData/GetUser";
    public static final String URL_LIST_PARTBOOK = "http://113.161.168.2:35/api/v1/Maintenance/GetPartBookByModel?modelId="+KEY_MODEL_ID;
    public static final String URL_SEND_REQUEST_MAITENACE = "http://113.161.168.2:35/api/v1/Maintenance/CreateRequest";

    public static final String URL_LIST_STATUS_CHECKLIST = "http://113.161.168.2:35/api/v1/GetData/StatusCheckList";
    public static final String URL_CHANGE_STATUS_CHECKLIST = "http://113.161.168.2:35/api/v1/CheckList/UpdateCheckList";
    public static final String URL_GET_CHECKLIST = "http://113.161.168.2:35/api/v1/CheckList/GetCheckList?token="+KEY_TOKEN+"&machineId="+KEY_MACHINE_ID+"&modelId="+KEY_MODEL_ID+"&date="+KEY_DATE;

    public static final String URL_GET_FROM_CORPARATION = "http://113.161.168.2:35/api/v1/GetData/GetCoporation?token="+KEY_TOKEN;
    public static final String URL_GET_TO_CORPARATION = "http://113.161.168.2:35/api/v1/GetData/GetCoporation";
    public static final String URL_GET_WAREHOUSE = "http://113.161.168.2:35/api/v1/GetData/GetWareHouse";
    public static final String URL_GET_COPORATION = "http://113.161.168.2:35/api/v1/GetData/GetFactoryByCorporation";
    public static final String URL_GET_FACTORY = "http://113.161.168.2:35/api/v1/GetData/GetFactoryByCorporation?idCorp="+KEY_ID;
    public static final String URL_GET_LINE = "http://113.161.168.2:35/api/v1/GetData/GetLineByFactory?idFactory="+KEY_ID;
    public static final String URL_GET_MACHINE_FOR_MOVING = "http://113.161.168.2:35/api/v1/Moving/GetMachineForMoving?corpId=CORPID&&key=KEY";
    public static final String URL_MOVING_MACHINE = "http://113.161.168.2:35/api/v1/Moving/MovingMachine";

}