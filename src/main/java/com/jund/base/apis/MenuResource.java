package com.jund.base.apis;

import com.jund.base.entity.MenuEntity;
import com.jund.base.service.MenuService;
import com.jund.framework.mvc.RestConst;
import com.jund.framework.mvc.base.BaseResource;
import com.jund.framework.mvc.response.ResponseInfo;
import com.jund.framework.mvc.response.RestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zhijund on 2017/9/2.
 */
@RestController
@RequestMapping(value = "/api/v1/menu")
public class MenuResource extends BaseResource{

    @Autowired
    private MenuService menuService;

    @GetMapping
    public ResponseInfo<MenuEntity> findAll(){
        return new ResponseInfo<>(menuService.findAllMenu());
    }

    @GetMapping(value = "/user/{userName}")
    public ResponseInfo<MenuEntity> findMenus(@PathVariable("userName") String userName){
        return new ResponseInfo<>(menuService.findMenusByUserName(userName));
    }

    @GetMapping(value = "/role/{roleId}")
    public ResponseInfo<Long[]> findMenuIds(@PathVariable("roleId") Long roleId){
        return new ResponseInfo<>(menuService.findMenuIdsByRoleId(roleId));
    }

    @GetMapping(value = "/{menuId}")
    public ResponseInfo<MenuEntity> detail(@PathVariable("menuId") Long menuId){
        return new ResponseInfo<>(menuService.findOne(menuId));
    }

    @PostMapping
    public ResponseInfo saveMenu(@RequestBody MenuEntity menu){
        menuService.saveMenu(menu);
        return new ResponseInfo<>();
    }

    @DeleteMapping(value = "/{menuId}")
    public ResponseInfo deleteMenu(@PathVariable("menuId") Long menuId){
        menuService.deleteOne(menuId);
        return new ResponseInfo<>();
    }

    @GetMapping(value = "/check/{menuCode}")
    public ResponseInfo checkMenuCode(@PathVariable("menuCode") String menuCode){
        if (!menuService.checkMenuCode(menuCode)){
            throw new RestException(RestConst.ReturnCode.VALID_ERROR,"菜单编码已存在！");
        }
        return new ResponseInfo<>();
    }


}
