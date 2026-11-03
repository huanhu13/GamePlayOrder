package com.game.service;

import com.game.dao.PlaymateDao;
import com.game.dao.UserDao;
import com.game.entity.Playmate;
import com.game.entity.User;

import java.math.BigDecimal;
import java.util.List;

/**
 * 陪玩师业务层：申请入驻、管理员审核、列表查询
 */
public class PlaymateService {

    private PlaymateDao playmateDao = new PlaymateDao();
    private UserDao userDao = new UserDao();

    /**
     * 用户申请成为陪玩师（提交后进入待审核状态）
     */
    public void apply(User loginUser, String gameName, String area, String price, String intro) throws Exception {
        // ---------- 基础校验 ----------
        if (isEmpty(gameName)) {
            throw new IllegalArgumentException("擅长游戏不能为空");
        }
        if (isEmpty(area)) {
            throw new IllegalArgumentException("大区/段位不能为空");
        }
        BigDecimal priceVal;
        try {
            priceVal = new BigDecimal(price);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("价格必须是数字");
        }
        if (priceVal.compareTo(BigDecimal.ZERO) <= 0 || priceVal.compareTo(new BigDecimal("9999")) > 0) {
            throw new IllegalArgumentException("价格必须大于 0 且不超过 9999");
        }
        // ---------- 业务校验：不能重复申请 ----------
        if (playmateDao.findByUserId(loginUser.getId()) != null) {
            throw new IllegalArgumentException("您已提交过陪玩师申请，请勿重复提交");
        }
        // ---------- 入库 ----------
        Playmate p = new Playmate();
        p.setUserId(loginUser.getId());
        p.setGameName(gameName);
        p.setArea(area);
        p.setPrice(priceVal);
        p.setIntro(intro);
        p.setStatus(0); // 待审核
        playmateDao.insert(p);
    }

    /**
     * 管理员审核陪玩师申请
     * @param pass true=通过，false=驳回
     */
    public void audit(User admin, int playmateId, boolean pass) throws Exception {
        // ---------- 权限校验 ----------
        if (admin.getRole() != 2) {
            throw new IllegalArgumentException("只有管理员才能审核");
        }
        Playmate p = playmateDao.findById(playmateId);
        if (p == null) {
            throw new IllegalArgumentException("陪玩师记录不存在");
        }
        if (p.getStatus() != 0) {
            throw new IllegalArgumentException("该申请已审核过，不能重复操作");
        }
        // ---------- 更新审核状态 ----------
        playmateDao.updateStatus(playmateId, pass ? 1 : 2);
        // 审核通过时，将关联用户角色升级为陪玩师(1)
        if (pass) {
            userDao.updateRole(p.getUserId(), 1);
        }
    }

    /** 查询审核通过的陪玩师列表（供用户浏览下单） */
    public List<Playmate> listApproved() throws Exception {
        return playmateDao.listByStatus(1);
    }

    /** 按状态查询列表（管理员审核页用），status 传 null 查全部 */
    public List<Playmate> listByStatus(Integer status) throws Exception {
        return playmateDao.listByStatus(status);
    }

    /** 根据主键查询陪玩师 */
    public Playmate findById(int id) throws Exception {
        return playmateDao.findById(id);
    }

    /** 判断字符串是否为空 */
    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
