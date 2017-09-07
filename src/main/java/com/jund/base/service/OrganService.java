package com.jund.base.service;

import com.jund.framework.jpa.base.service.BaseService;
import com.jund.platformwork.security.model.Organ;
import com.jund.platformwork.security.model.dto.OrganDTO;
import com.jund.platformwork.security.model.dto.OrganForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrganService extends BaseService<Organ, Long> {

	
	/**
	 * 根据机构层次, 查询启用机构列表，如果参数为空，查询所有启用的机构
	 * @param orgLevel
	 * @return
	 */
	List<Organ> findEnableByLevel(Integer orgLevel);
	
	/**
	 * 根据pid查询直接下级机构，如果pid为空，则查询无上级的机构
	 * 通过sql
	 * @param pid
	 * @return
	 */
	List<Organ> findOrganByPidAndStatus(Long pid, Integer... status);
	
	/**
	 * 根据父级机构的id，查询后代机构
	 * @param pid
	 * @param status
	 * @return
	 */
	List<OrganDTO> findAllChildByPid(Long pid, Integer... status);
	
	/**
	 * 根据父级机构的序列号，查询后代机构，有时需要查询有状态的后代机构
	 * @param pseq
	 * @param status
	 * @return
	 */
	List<Organ> findAllChildByPseq(String pseq, Integer... status);
	
	/**
	 * 根据orgCode查询，唯一性校验
	 * @param orgCode
	 * @return
	 */
	Organ findOrganByOrgCode(String orgCode);
	
	/**
	 * 根据orgName查询，唯一性校验
	 * @param orgName
	 * @return
	 */
	Organ findOrganByOrgName(String orgName);
	
	/**
	 * 判断上级机构是否合法，不能将自己或者自己的所有下级设置为父机构
	 * @param id
	 * @param pid
	 * @return
	 */
	boolean checkParentId(Long id, Long pid);
	
	/**
	 * 是否启用
	 * @param ids
	 * 
	 */
	void isStartStatus(Long[] ids);
	
	/**
	 * 是否撤销
	 * @param ids
	 * 
	 */
	void isCancelStatus(Long[] ids);
	
	/**
	 * 启用/撤销
	 * @param organForm
	 */
	void updateStatus(Long[] ids, Integer status);
	
	/**
	 * 根据父机构id，生成新的序列号
	 * @param pid
	 * @return
	 */
	String genNewOrgSeq(Long pid);
	
	/**
	 * 保存机构
	 * @param orgForm
	 */
	void saveWithParent(Organ organ, Long pid, Long oldPId);

	/**
	 * 分页
	 * @return
	 */
	Page<Organ> findAll(OrganForm organForm, Pageable pageable);
	
}
