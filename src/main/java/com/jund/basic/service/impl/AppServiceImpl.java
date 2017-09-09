package com.jund.basic.service.impl;

import com.jund.basic.service.AppService;
import com.jund.basic.service.MenuService;
import com.jund.framework.core.annotation.Logging;
import com.jund.framework.core.exception.VpRuntimeException;
import com.jund.framework.jpa.base.repository.BaseRepository;
import com.jund.framework.jpa.base.service.impl.BaseServiceImpl;
import com.jund.framework.jpa.util.HqlUtil;
import com.jund.framework.mvc.RestConst;
import com.jund.platformwork.security.model.App;
import com.jund.platformwork.security.model.Menu;
import com.jund.platformwork.security.model.RoleApp;
import com.jund.security.repository.AppRepository;
import com.jund.security.repository.RoleAppRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AppServiceImpl extends BaseServiceImpl<App, Long> implements AppService {

    @Autowired
    private MenuService menuService;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private RoleAppRepository roleAppRepository;

    @Override
    protected BaseRepository<App, Long> getRepository() {
        return appRepository;
    }

    @Transactional
    @Logging(title = "启用/撤销", data = "修改应用{0}状态为{1}")
    public void updateStatus(Long[] ids, Integer status) {
        for (Long id : ids) {
            App app = appRepository.findOne(id);
            app.setStatus(status);
            appRepository.save(app);
        }
    }

    public App findByAppCode(String appCode){
    	return appRepository.findByAppCode(appCode);
    }
    
    public List<App> findAll() {
        Order order = new Order(Direction.ASC, "id");
        Sort sort = new Sort(order);
        return appRepository.findAll(sort);
    }

    public Page<App> findAll(App app, Pageable pageable) {
        List<Object> parameters = new ArrayList<Object>();

        String hql = prepareHqlForSelect(app, parameters);
        String orderhql = HqlUtil.appendOrderHql(hql, pageable.getSort());

        Page<App> page = appRepository.findAllByHql(pageable, orderhql, parameters.toArray());

        List<App> appList = page.getContent();
        if (CollectionUtils.isEmpty(appList)) {
            return new PageImpl<App>(new ArrayList<App>(), pageable, 0L);
        }

        return new PageImpl<App>(appList, pageable, page.getTotalElements());
    }

    private String prepareHqlForSelect(App app, List<Object> parameters) {
        StringBuilder hql = new StringBuilder();
        hql.append(" select distinct t from App t ");
        hql.append(" where 1=1 ");
        
        if (app != null) {

            String appCode = app.getAppCode();
            if (StringUtils.isNotBlank(appCode)) {
                hql.append(" and upper(t.appCode) like ? ");
                parameters.add(appCode);
            }

            String appName = app.getAppName();
            if (StringUtils.isNotBlank(appName)) {
                hql.append(" and upper(t.appName) like ? ");
                parameters.add(appName);
            }
        }
        logger.debug("hql: ", hql.toString());
        return hql.toString();
    }

    public List<Long> findAppIdByRoleId(Long roleId) {
        List<Long> appIds = new ArrayList<Long>();

        List<RoleApp> roleApps = roleAppRepository.findByRoleId(roleId);
        if (CollectionUtils.isNotEmpty(roleApps)) {
            for (RoleApp roleApp : roleApps) {
                appIds.add(roleApp.getAppId());
            }
        }

        return appIds;
    }

    @Transactional
    public void delete(Long... ids) {
    	judgement(ids);
    	
        for (long id : ids) {
            roleAppRepository.deleteByAppId(id);
        }
        
        super.delete(ids);
    }

    @Transactional
    public void save(App app) {
        if (app.getId() == null){
        	App checkCode = new App();
        	checkCode.setAppCode(app.getAppCode());
            Example<App> example = Example.of(checkCode);
            if (exists(example)) {
                throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "该应用编码已经存在，无法使用！");
            }
        }
        appRepository.save(app);
    }

    public void judgement(Long[] ids) {
        List<Object> hasMenus = new ArrayList<Object>();

        List<String> appList = new ArrayList<String>();
        for (Long id : ids) {
            List<Menu> menulist = menuService.findExistMenuByAppId(id);

            App app = appRepository.findOne(id);
            if (CollectionUtils.isNotEmpty(menulist)) {
                hasMenus.add(menulist);
                appList.add(app.getAppName());
            }
        }

        if (CollectionUtils.isNotEmpty(hasMenus)) {
            throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "您要删除的应用" + appList + "下存在菜单,不能删除！");
        }
    }
}
