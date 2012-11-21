/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.common;

import java.util.UUID;

/**
 *
 * @author ajuste
 */
public interface IEntity {

    public void setId(UUID id);

    public UUID getId();
}
