package com.jadic.biz;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jadic.biz.bean.ModuleStatus;
import com.jadic.cmd.req.CmdModuleStatusReq;
import com.jadic.db.DBOper;
import com.jadic.utils.KKTool;

/**
 * 实时更新终端模块状态
 * @author 	Jadic
 * @created 2014-8-5
 */
public class ThreadTerminalModuleStatus extends AbstractThreadDisposeDataFromQueue<CmdModuleStatusReq> {
    
    private DBOper dbOper;
    
    private Set<Long> terminalIdSet;//有终端模块状态数据的终端ID集合
    
    public ThreadTerminalModuleStatus() {
        dbOper = DBOper.getDBOper();
        terminalIdSet = new HashSet<Long>();
    }

    @Override
    public void run() {
        CmdModuleStatusReq cmdReq = null;
        while (!isInterrupted()) {
            while ((cmdReq = getQueuePollData()) != null) {
                disposeCmd(cmdReq);
            }
            waitNewData();
        }
    }
    
    private void disposeCmd(CmdModuleStatusReq cmdReq) {
        long terminalId = Long.parseLong(KKTool.byteArrayToHexStr(cmdReq.getTerminalId()));
        StringBuilder sqlBuilder = new StringBuilder();
        List<ModuleStatus>msList = cmdReq.getMsList();
        List<Object> params = new ArrayList<Object>();
        if (terminalIdSet.add(terminalId)) {//新增   
            sqlBuilder.append("insert into tab_terminal_status ");
            sqlBuilder.append("(terminalId, onlinestatus, lastonlinetime");
            for (ModuleStatus ms : msList) {
                sqlBuilder.append(", m").append(ms.getModuleId()).append("status");
            }            
            sqlBuilder.append(") ");
            sqlBuilder.append("values (?, 1, SYSDATE()");
            for (int i = 0; i < msList.size(); i ++) {
                sqlBuilder.append(", ?");
            }
            sqlBuilder.append(")");
            
            params.add(terminalId);
            for (ModuleStatus ms : msList) {
                params.add(ms.getModuleStatus());
            }
            dbOper.updateTerminalStatus(sqlBuilder.toString(), params);
        } else {
            sqlBuilder.append("update tab_terminal_status ");
            sqlBuilder.append("set onlinestatus=1, lastonlinetime=SYSDATE(), ");
            for (ModuleStatus ms : msList) {
                sqlBuilder.append(", m").append(ms.getModuleId()).append("status=?");
            }            
            sqlBuilder.append(" where terminalid=? ");
            for (ModuleStatus ms : msList) {
                params.add(ms.getModuleStatus());
            }
            params.add(terminalId);
            dbOper.updateTerminalStatus(sqlBuilder.toString(), params);
        }
    }

}
