package com.jund.basic.core.service.impl;

import com.jund.basic.core.service.OrganService;
import com.jund.framework.core.annotation.Logging;
import com.jund.framework.core.exception.VpRuntimeException;
import com.jund.framework.core.util.BeanUtil;
import com.jund.framework.jpa.base.repository.BaseRepository;
import com.jund.framework.jpa.base.service.impl.BaseServiceImpl;
import com.jund.framework.jpa.util.HqlUtil;
import com.jund.framework.mvc.RestConst;
import com.jund.platformwork.security.model.Organ;
import com.jund.platformwork.security.model.User;
import com.jund.platformwork.security.model.dto.OrganDTO;
import com.jund.platformwork.security.model.dto.OrganForm;
import com.jund.security.Constants;
import com.jund.security.repository.OrganRepository;
import com.jund.security.repository.UserRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrganServiceImpl extends BaseServiceImpl<Organ, Long> implements OrganService {

	@Autowired
    private UserRepository userRepository;
	
    @Autowired
    private OrganRepository organRepository;

    @Override
    protected BaseRepository<Organ, Long> getRepository() {
        return organRepository;
    }

    public List<Organ> findEnableByLevel(Integer orgLevel) {
        String sql = "select * from plt_sec_organ where 1=1 " + ((orgLevel != null) ? " and org_Level = " + orgLevel : "") + " and status = 1";
        return organRepository.findAllBySql(sql);
    }

    public Page<Organ> findAll(OrganForm organForm, Pageable pageable) {
        List<Object> parameters = new ArrayList<Object>();
        String hql = prepareHqlForSelect(organForm, parameters);
        String orderhql = HqlUtil.appendOrderHql(hql, pageable.getSort());
        Page<Organ> page = organRepository.findAllByHql(pageable, orderhql, parameters.toArray());
        List<Organ> organList = page.getContent();
        if (CollectionUtils.isEmpty(organList)) {
            return new PageImpl<Organ>(new ArrayList<Organ>(), pageable, 0L);
        }
       
        return new PageImpl<Organ>(organList, pageable, page.getTotalElements());
    }

    public List<Organ> findOrganByPidAndStatus(Long pid, Integer... status) {
        String sql = " select t.* from plt_sec_organ t where 1= 1";
        if (!ArrayUtils.isEmpty(status)) {
            sql += " and status = " + Constants.INTEGER_VALUE_TRUE;
        }
        
        if (pid != null) {
            sql += " and pid = " + pid;
        } else {
            sql += " and pid is null";
        }
        
        return organRepository.findAllBySql(sql);
    }

    public List<OrganDTO> findAllChildByPid(Long pid, Integer... status) {
        String parentOrgSeq = organRepository.getOne(pid).getOrgSeq();
        if (ArrayUtils.isEmpty(status)) {
            return converToDto(organRepository.findAllChildByPseq(parentOrgSeq));
        } else {
            return converToDto(organRepository.findAllChildByPseq(parentOrgSeq, status[0]));
        }
    }

    public List<Organ> findAllChildByPseq(String pseq, Integer... status) {
        if (ArrayUtils.isEmpty(status)) {
            return organRepository.findAllChildByPseq(pseq);
        } else {
            return organRepository.findAllChildByPseq(pseq, status[0]);
        }
    }

    public Organ findOrganByOrgCode(String orgCode) {
        return organRepository.findOrganByOrgCode(orgCode);
    }

    public Organ findOrganByOrgName(String orgName) {
        return organRepository.findOrganByOrgName(orgName);
    }

    public boolean checkParentId(Long id, Long pid) {
        Organ org = organRepository.findOne(id);
        Organ parent = organRepository.findOne(pid);
        if (org == null || parent == null) {
            return true;
        }
        if (parent.getOrgSeq().startsWith(org.getOrgSeq())) {
            logger.warn("不能将其子机构作为父机构！");
            return false;
        }
        return true;
    }

    public void isStartStatus(Long[] ids) {
        List<String> codeList = new ArrayList<String>();
        for (Long id : ids) {
            Organ organ = organRepository.findWithParentById(id);
            if (null == organ.getParentOrgan()) {
                codeList.add(organ.getOrgCode());
                logger.debug(codeList.toString());
            }
        }
        
        if (codeList.size() > 0) {
            throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "机构" + codeList + "没有上级机构，请先进入编辑页面修改其上级机构！");
        }
    }

    public void isCancelStatus(Long[] ids) {
        /**
         * 表示机构或后代机构下有用户的机构号的列表
         */
        List<String> userOrgList = new ArrayList<String>();
        /**
         * 表示含有用户的后代机构的机构号的列表
         */
        List<String> inChildOrgList = new ArrayList<String>();

        for (Long id : ids) {
            Organ organ = findOne(id);
            /**
             * 获取所有后代机构
             */
            List<Organ> childList = findAllChildByPseq(organ.getOrgSeq(), Constants.INTEGER_VALUE_TRUE);
            boolean hasUserFlag = false;
            for (Organ child : childList) {
                List<User> users = userRepository.findUserEnableByOrgId(child.getId());
                if (CollectionUtils.isNotEmpty(users)) {
                    hasUserFlag = true;
                    break;
                }
            }
            
            if (hasUserFlag) {
                //当前机构或后代机构中拥有生效的用户
                userOrgList.add(organ.getOrgCode());
            } else {
                if (childList.size() > 1) {
                    //当前机构拥有后代机构，且当前机构和后代机构中都不拥有生效的用户
                    inChildOrgList.add(organ.getOrgCode());
                }
            }
        }
        if (userOrgList.size() > 0) {
            throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "机构" + userOrgList + "或其下级机构有用户，请先删除机构下的用户。");
        } else if (inChildOrgList.size() > 0) {
            throw new VpRuntimeException(RestConst.ReturnCode.VALID_WARNNING, "您要撤销的机构" + inChildOrgList + "下存在未撤销子机构，是否强制撤销该机构以及子机构？");
        }
    }

    @Transactional
    @Logging(title = "启用&撤销", data = "将机构{0}状态改为{1}")
    public void updateStatus(Long[] ids, Integer status) {
    	if(status == Constants.INTEGER_VALUE_TRUE){
    		isStartStatus(ids);
    	}else{
    		isCancelStatus(ids);
    	}
    	
        for (Long id : ids) {
            if (status == Constants.INTEGER_VALUE_TRUE) {
            	List<String> codeList = new ArrayList<String>();
                //启用时，将当前机构的状态改为启用状态
                Organ organ = organRepository.findWithParentById(id);
                if (null == organ.getParentOrgan().getOrgCode()) {
                    codeList.add(organ.getOrgCode());
                    logger.debug(codeList.toString());
                }
                
                organ.setStatus(status);
                organRepository.save(organ);
            } else {
                //撤销时，须将所有后代机构都修改位撤销状态
                Organ organ = findOne(id);
                List<Organ> allChildren = findAllChildByPseq(organ.getOrgSeq(), Constants.INTEGER_VALUE_TRUE);
                for (Organ child : allChildren) {
                    child.setStatus(Constants.INTEGER_VALUE_FALSE);
                    organRepository.save(child);
                }

            }
        }
    }

    @Transactional
    @Logging(title = "保存机构信息", data = "机构信息{0},上级机构{1}")
    public void saveWithParent(Organ organ, Long pid, Long oldPId) {
        organ.setOrgLevel(0);
        if (pid != null) {
            Organ pOrg = findOne(pid);
            organ.setParentOrgan(pOrg);
            organ.setOrgLevel(pOrg.getOrgLevel() + 1);
        }
        
        if (organ.getId() != null) {
        	if(pid != null){
        		boolean result = checkParentId(organ.getId(), pid);
        		if (!result) {
        			throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "上级机构不合法，不能将自己的子级设置为父机构！");
        		}
        	}
    		
            if (pid != null && !pid.equals(oldPId)) {
                //上级机构更改后，更新本机构及下级机构的序列号
                String newOrgSeq = genNewOrgSeq(pid);
                String oldOrgSeq = organ.getOrgSeq();
                organ.setOrgSeq(newOrgSeq);
                updateAllSubOrgSeq(newOrgSeq, oldOrgSeq);
            } else {
                organ.setParentOrgan(null);
            }
        } else {
            organ.setStatus(1);
            organ.setOrgSeq(genNewOrgSeq(pid));
        }
        organRepository.save(organ);
    }

    public String genNewOrgSeq(Long pid) {
        String maxOrgSeq = findMaxSecByPid(pid);
        //如果父机构下已经有子级，取子级中最大的序列号，再加1
        if (StringUtils.isNotEmpty(maxOrgSeq) && !"0".equals(maxOrgSeq)) {
            return String.format("%0" + maxOrgSeq.length() + "d", Long.valueOf(maxOrgSeq) + 1);
        }
        //如果父机构下没有子级，使用父级序列号再拼接3位字符
        StringBuilder orgSeqBuffer = new StringBuilder();
        if (pid != null) {
            orgSeqBuffer.append(findOne(pid).getOrgSeq());
        }
        orgSeqBuffer.append("001");
        return orgSeqBuffer.toString();
    }

    public String findMaxSecByPid(Long pid) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from plt_sec_organ  as o where 1=1 and ");
        if (pid == null) {
            sql.append(" o.pid is null order by o.org_seq desc ");
        } else {
            sql.append(" o.pid = ?  and o.org_seq is not null order by o.org_seq desc ");
        }
        
        List<Organ> orgList = null;
        if (pid != null) {
            orgList = organRepository.findAllBySql(sql.toString(), pid);
        } else {
            orgList = organRepository.findAllBySql(sql.toString());
        }
        return (orgList.size() > 0) ? orgList.get(0).getOrgSeq() : null;
    }

    /**
     * @param newSeq
     * @param oldSeq
     */
    @Transactional
    public void updateAllSubOrgSeq(String newSeq, String oldSeq) {
        StringBuilder sql = new StringBuilder();
        sql.append(" update plt_sec_organ t ");
        sql.append(" set t.org_Seq = concat(?, substr(t.org_Seq, ?)), t.org_Level = (t.org_Level + ?) ");
        sql.append(" where t.org_Seq like ?||'%' ");
        logger.debug(sql.toString());

        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(newSeq);
        paramsList.add(oldSeq.length() + 1);
        paramsList.add(newSeq.length() / 3 - oldSeq.length() / 3);
        paramsList.add(oldSeq);

        organRepository.excuteSql(sql.toString(), paramsList.toArray());
    }

    private List<OrganDTO> converToDto(List<Organ> orgList) {
        List<OrganDTO> dtoList = new ArrayList<OrganDTO>();
        for (Organ organ : orgList) {
            OrganDTO dto = new OrganDTO();
            try {
                BeanUtil.copyProperties(dto, organ);
            } catch (Exception e) {
                logger.error("", e);
            }
            if (null != organ.getParentOrgan()) {
                dto.setPid(organ.getParentOrgan().getId());
            }
            dtoList.add(dto);
        }
        return dtoList;
    }

    private String prepareHqlForSelect(OrganForm organ, List<Object> parameters) {
        StringBuilder hql = new StringBuilder();
        hql.append(" select distinct t from Organ t left join fetch t.parentOrgan p");
        hql.append(" where 1=1 ");
        if (organ != null) {

            Long orgId = organ.getId();
            if (orgId != null) {
                hql.append(" and t.id = ? ");
                parameters.add(orgId);
            }

            String orgSeq = organ.getOrgSeq();
            if (StringUtils.isNotBlank(orgSeq)) {
                hql.append(" and t.orgSeq like ? ");
                parameters.add("%" + orgSeq + "%");
            }

            String orgCode = organ.getOrgCode();
            if (StringUtils.isNotBlank(orgCode)) {
                hql.append(" and t.orgCode like ? ");
                parameters.add("%" + orgCode + "%");
            }

            Integer status = organ.getStatus();
            if (status != null) {
                hql.append(" and t.status = ? ");
                parameters.add(status);
            }

            Integer orgLevel = organ.getOrgLevel();
            if (orgLevel != null) {
                hql.append(" and t.orgLevel = ? ");
                parameters.add(status);
            }

            String orgName = organ.getOrgName();
            if (StringUtils.isNotBlank(orgName)) {
                hql.append(" and t.orgName like ? ");
                parameters.add("%" + orgName + "%");
            }

            if (organ.getParentOrgan() != null) {
                Long parentOrganId = organ.getParentOrgan().getId();
                if (parentOrganId != null) {
                    hql.append(" and p.id = ?");
                    parameters.add(parentOrganId);
                }
            }
        }
        logger.debug("hql: ", hql.toString());
        return hql.toString();
    }


}
