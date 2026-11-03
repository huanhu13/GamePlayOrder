package com.game.service;

import com.game.dao.SettlementDao;
import com.game.entity.Settlement;

import java.util.List;

/**
 * 结算业务层：结算记录查询
 * （结算的生成逻辑在 ReviewService 中：用户评价完成后自动结算）
 */
public class SettlementService {

    private SettlementDao settlementDao = new SettlementDao();

    /** 查询全部结算记录（管理员查看） */
    public List<Settlement> listAll() throws Exception {
        return settlementDao.listAll();
    }
}
