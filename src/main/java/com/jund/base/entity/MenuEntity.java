package com.jund.base.entity;

import com.jund.framework.jpa.base.entity.BaseEntity;

import java.io.Serializable;

/**
 * Created by zhijund on 2017/9/2.
 */
public class MenuEntity extends BaseEntity implements Serializable, Cloneable {

    public MenuEntity clone() {
        MenuEntity cloneMenu = null;
        try {
            cloneMenu = (MenuEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cloneMenu;
    }

    public String toString() {
        return null;
    }

    public boolean equals(Object o) {
        return false;
    }

    public int hashCode() {
        return 0;
    }
}
