package com.jund.basis.apis.form;

import java.io.Serializable;

public class AppForm implements Serializable {

    private Long[] id;

    private Integer status;

    public Long[] getId() {
        return id;
    }

    public void setId(Long[] id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
