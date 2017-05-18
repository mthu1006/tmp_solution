package com.example.pk.tpmresolution.model;

/**
 * Created by mthu1 on 4/13/2017.
 */

public class RequestMaintenace {
    String ID;
    String Model;
    String RequestDate;
    String MachineId;
    String AcceptUser;
    String Reason;
    String Name;
    String Location;
    String Unit;
    String MNTN_SEQ;
    String ACPT_DT;
    String MNTN_RSN;
    String PWNC_USR_ID;
    String PWNC_USR_NM;
    String CRT_USR_ID;
    String UPDT_USR_ID;
    String CRT_DTTM;
    String UPDT_DTTM;
    String PART_CD;
    String PART_NM;
    String REQ_DT;
    String MCHN_MST_NM;
    int Qty;
    int Price;
    String[] listUnit;
    String[] listUnitCode;
    String[] PartBooks;
    String[] ListAcceptUsers;


    public String[] getListAcceptUsers() {
        return ListAcceptUsers;
    }

    public void setListAcceptUsers(String[] listAcceptUsers) {
        ListAcceptUsers = listAcceptUsers;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getMNTN_SEQ() {
        return MNTN_SEQ;
    }

    public void setMNTN_SEQ(String MNTN_SEQ) {
        this.MNTN_SEQ = MNTN_SEQ;
    }

    public String getACPT_DT() {
        return ACPT_DT;
    }

    public void setACPT_DT(String ACPT_DT) {
        this.ACPT_DT = ACPT_DT;
    }

    public String getMNTN_RSN() {
        return MNTN_RSN;
    }

    public void setMNTN_RSN(String MNTN_RSN) {
        this.MNTN_RSN = MNTN_RSN;
    }

    public String getPWNC_USR_ID() {
        return PWNC_USR_ID;
    }

    public void setPWNC_USR_ID(String PWNC_USR_ID) {
        this.PWNC_USR_ID = PWNC_USR_ID;
    }

    public String getPWNC_USR_NM() {
        return PWNC_USR_NM;
    }

    public void setPWNC_USR_NM(String PWNC_USR_NM) {
        this.PWNC_USR_NM = PWNC_USR_NM;
    }

    public String getCRT_USR_ID() {
        return CRT_USR_ID;
    }

    public void setCRT_USR_ID(String CRT_USR_ID) {
        this.CRT_USR_ID = CRT_USR_ID;
    }

    public String getUPDT_USR_ID() {
        return UPDT_USR_ID;
    }

    public void setUPDT_USR_ID(String UPDT_USR_ID) {
        this.UPDT_USR_ID = UPDT_USR_ID;
    }

    public String getCRT_DTTM() {
        return CRT_DTTM;
    }

    public void setCRT_DTTM(String CRT_DTTM) {
        this.CRT_DTTM = CRT_DTTM;
    }

    public String getUPDT_DTTM() {
        return UPDT_DTTM;
    }

    public void setUPDT_DTTM(String UPDT_DTTM) {
        this.UPDT_DTTM = UPDT_DTTM;
    }

    public String getPART_CD() {
        return PART_CD;
    }

    public void setPART_CD(String PART_CD) {
        this.PART_CD = PART_CD;
    }

    public String getPART_NM() {
        return PART_NM;
    }

    public void setPART_NM(String PART_NM) {
        this.PART_NM = PART_NM;
    }

    public String getREQ_DT() {
        return REQ_DT;
    }

    public void setREQ_DT(String REQ_DT) {
        this.REQ_DT = REQ_DT;
    }

    public String getMCHN_MST_NM() {
        return MCHN_MST_NM;
    }

    public void setMCHN_MST_NM(String MCHN_MST_NM) {
        this.MCHN_MST_NM = MCHN_MST_NM;
    }

    public String[] getPartBooks() {
        return PartBooks;
    }

    public void setPartBooks(String[] partBooks) {
        PartBooks = partBooks;
    }

    public String[] getListUnit() {
        return listUnit;
    }

    public String[] getListUnitCode() {
        return listUnitCode;
    }

    public void setListUnitCode(String[] listUnitCode) {
        this.listUnitCode = listUnitCode;
    }

    public void setListUnit(String[] listUnit) {
        this.listUnit = listUnit;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getRequestDate() {
        return RequestDate;
    }

    public void setRequestDate(String requestDate) {
        RequestDate = requestDate;
    }

    public String getMachineId() {
        return MachineId;
    }

    public void setMachineId(String machineId) {
        MachineId = machineId;
    }

    public String getAcceptUser() {
        return AcceptUser;
    }

    public void setAcceptUser(String acceptUser) {
        AcceptUser = acceptUser;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }
}
