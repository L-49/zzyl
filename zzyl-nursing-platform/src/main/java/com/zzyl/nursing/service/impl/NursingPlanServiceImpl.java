package com.zzyl.nursing.service.impl;

import java.util.List;
import com.zzyl.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.zzyl.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.zzyl.nursing.domain.NursingProjectPlan;
import com.zzyl.nursing.mapper.NursingPlanMapper;
import com.zzyl.nursing.domain.NursingPlan;
import com.zzyl.nursing.service.INursingPlanService;

/**
 * 护理计划Service业务层处理
 * 
 * @author lsj
 * @date 2026-10-14
 */
@Service
public class NursingPlanServiceImpl implements INursingPlanService 
{
    @Autowired
    private NursingPlanMapper nursingPlanMapper;

    /**
     * 查询护理计划
     * 
     * @param id 护理计划主键
     * @return 护理计划
     */
    @Override
    public NursingPlan selectNursingPlanById(Long id)
    {
        return nursingPlanMapper.selectNursingPlanById(id);
    }

    /**
     * 查询护理计划列表
     * 
     * @param nursingPlan 护理计划
     * @return 护理计划
     */
    @Override
    public List<NursingPlan> selectNursingPlanList(NursingPlan nursingPlan)
    {
        return nursingPlanMapper.selectNursingPlanList(nursingPlan);
    }

    /**
     * 新增护理计划
     * 
     * @param nursingPlan 护理计划
     * @return 结果
     */
    @Transactional
    @Override
    public int insertNursingPlan(NursingPlan nursingPlan)
    {
        nursingPlan.setCreateTime(DateUtils.getNowDate());
        int rows = nursingPlanMapper.insertNursingPlan(nursingPlan);
        insertNursingProjectPlan(nursingPlan);
        return rows;
    }

    /**
     * 修改护理计划
     * 
     * @param nursingPlan 护理计划
     * @return 结果
     */
    @Transactional
    @Override
    public int updateNursingPlan(NursingPlan nursingPlan)
    {
        nursingPlan.setUpdateTime(DateUtils.getNowDate());
        nursingPlanMapper.deleteNursingProjectPlanByPlanId(nursingPlan.getId());
        insertNursingProjectPlan(nursingPlan);
        return nursingPlanMapper.updateNursingPlan(nursingPlan);
    }

    /**
     * 批量删除护理计划
     * 
     * @param ids 需要删除的护理计划主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteNursingPlanByIds(Long[] ids)
    {
        nursingPlanMapper.deleteNursingProjectPlanByPlanIds(ids);
        return nursingPlanMapper.deleteNursingPlanByIds(ids);
    }

    /**
     * 删除护理计划信息
     * 
     * @param id 护理计划主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteNursingPlanById(Long id)
    {
        nursingPlanMapper.deleteNursingProjectPlanByPlanId(id);
        return nursingPlanMapper.deleteNursingPlanById(id);
    }

    /**
     * 新增护理计划和项目关联信息
     * 
     * @param nursingPlan 护理计划对象
     */
    public void insertNursingProjectPlan(NursingPlan nursingPlan)
    {
        List<NursingProjectPlan> nursingProjectPlanList = nursingPlan.getNursingProjectPlanList();
        Long id = nursingPlan.getId();
        if (StringUtils.isNotNull(nursingProjectPlanList))
        {
            List<NursingProjectPlan> list = new ArrayList<NursingProjectPlan>();
            for (NursingProjectPlan nursingProjectPlan : nursingProjectPlanList)
            {
                nursingProjectPlan.setPlanId(id);
                list.add(nursingProjectPlan);
            }
            if (list.size() > 0)
            {
                nursingPlanMapper.batchNursingProjectPlan(list);
            }
        }
    }
}
