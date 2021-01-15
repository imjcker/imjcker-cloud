package com.imjcker.manager.health.model;

import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ztzh_tanhh 2019/11/29
 **/
@Data
@Entity
@Table
public class Instance {
    @Id
    private String instanceId;
    private String hostName;
    private String appName;
    private String status;//red,green,gray
    private int port;
    private String healthCheckUrl;

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Instance)) return false;

        Instance instance = (Instance) o;

        return new EqualsBuilder()
                .append(instanceId, instance.instanceId)
                .isEquals();
    }
}

