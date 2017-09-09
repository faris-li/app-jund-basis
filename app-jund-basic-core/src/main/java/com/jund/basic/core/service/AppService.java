package com.jund.basic.core.service;

import com.jund.framework.jpa.base.service.BaseService;
import com.jund.platformwork.security.model.App;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AppService extends BaseService<App, Long> {

    /**
     * 启用、撤销
     *
     * @param ids
     * @param status
     */
    void updateStatus(Long[] ids, Integer status);

    /**
     * 分页查询应用
     *
     * @param app
     * @param pageable
     * @return
     */
    Page<App> findAll(App app, Pageable pageable);

    /**
     * 查询指定角色拥有的应用列表appId
     *
     * @param roleId
     * @return
     */
    List<Long> findAppIdByRoleId(Long roleId);

    /**
     * 保存
     *
     * @param app
     */
    void save(App app);

    /**
     * 应用是否可删除
     *
     * @param ids
     */
    void judgement(Long[] ids);
    
    /**
     * 根据appCode查应用
     * @param appCode
     * @return
     */
    App findByAppCode(String appCode);

}
