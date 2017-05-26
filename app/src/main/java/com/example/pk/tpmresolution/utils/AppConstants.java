package com.example.pk.tpmresolution.utils;

/**
 * Created by kien on 10/11/16.
 */

public class AppConstants {
    public static final String TAG = "tpm";
    public static final String ip = "203.113.151.193:35";
    public static final String PREF_KEY_LOGIN_REMEMBERLOGIN = "login_remember";
    public static final String PREF_KEY_LOGIN_USERNAME = "login_username";
    public static final String PREF_KEY_LOGIN_PASSWORD = "login_password";
    public static final String PREF_KEY_LOGIN_TOKEN = "login_token";
    public static final String PREF_KEY_LOGIN_INFO = "login_info";
    public static final String PREF_KEY_MACHINE_ID = "machine_id";

    public static final String DEFAULT_NULL_TEXT = "No data";
    public static final String KEY_MACHINE_ID = "MACHINE_ID";
    public static final String KEY_TOOL_ID = "TOOL_ID";
    public static final String KEY_MODEL_ID = "MODEL_ID";
    public static final String KEY_TOKEN = "TOKEN";
    public static final String KEY_USER_ID = "USER_ID";
    public static final String KEY_DATE = "DATE";
    public static final String KEY_ID = "ID";


    //M.r Đông
    public static final String URL_LOGIN = ip+"/api/v1/Account/Login";
    public static final String URL_lOGOUT = ip+"/api/v1/Account/LogOut";
    public static final String URL_LOGIN_CHANGEPASS = ip+"/api/v1/Account/ChangePassword";

    public static final String URL_GET_MACHINE_DETAIL = ip+"/api/v1/Scan/QrScan?token="+KEY_TOKEN+"&machineId="+KEY_MACHINE_ID;
    public static final String URL_LIST_STATUS = ip+"/api/v1/GetData/GetMachineStatus";
    public static final String URL_CHANGE_STATUS = ip+"/api/v1/MachineStatus/ChangeMachineStatus";

    public static final String URL_REFEREBCE_INFOMATION = ip+"/api/v1/Reference/GetReference?token="+KEY_TOKEN+"&modelId="+KEY_MODEL_ID;

    public static final String URL_LIST_UNIT = ip+"/api/v1/GetData/GetPartBookPriceUnit";
    public static final String URL_LIST_USER = ip+"/api/v1/GetData/GetUser";
    public static final String URL_LIST_PARTBOOK = ip+"/api/v1/Maintenance/GetPartBookByModel?modelId="+KEY_MODEL_ID;
    public static final String URL_SEND_REQUEST_MAITENACE = ip+"/api/v1/Maintenance/CreateRequest";

    public static final String URL_LIST_STATUS_CHECKLIST = ip+"/api/v1/GetData/StatusCheckList";
    public static final String URL_CHANGE_STATUS_CHECKLIST = ip+"/api/v1/CheckList/UpdateCheckList";
    public static final String URL_GET_CHECKLIST = ip+"/api/v1/CheckList/GetCheckList?token="+KEY_TOKEN+"&machineId="+KEY_MACHINE_ID+"&modelId="+KEY_MODEL_ID+"&date="+KEY_DATE;

    public static final String URL_GET_FROM_CORPARATION = ip+"/api/v1/GetData/GetCoporation?token="+KEY_TOKEN;
    public static final String URL_GET_TO_CORPARATION = ip+"/api/v1/GetData/GetCoporation";
    public static final String URL_GET_WAREHOUSE = ip+"/api/v1/GetData/GetWareHouse";
    public static final String URL_GET_COPORATION = ip+"/api/v1/GetData/GetFactoryByCorporation";
    public static final String URL_GET_FACTORY = ip+"/api/v1/GetData/GetFactoryByCorporation?idCorp="+KEY_ID;
    public static final String URL_GET_LINE = ip+"/api/v1/GetData/GetLineByFactory?idFactory="+KEY_ID;
    public static final String URL_GET_MACHINE_FOR_MOVING = ip+"/api/v1/Moving/GetMachineForMoving?corpId=CORPID&&key=KEY";
    public static final String URL_MOVING_MACHINE = ip+"/api/v1/Moving/MovingMachine";

    public static final String URL_TOOL_MANGAGER = ip+"/api/v1/Scan/ToolScan?token="+KEY_TOKEN+"&toolId="+KEY_TOOL_ID;;

    public static final String URL_GET_TOOl_MANAGEMENT = "http://113.161.168.2:35/api/v1/Scan/ToolScan";

}