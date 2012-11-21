/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.common.exception;

import java.util.UUID;

/**
 *
 * @author ajuste
 */
public class RecordNotFoundException extends Exception {

    protected Class recordClass;
    protected UUID recordId;
    protected String additionalInformation;

    public RecordNotFoundException(Exception cause) {
        super(cause);
    }

    public RecordNotFoundException(Exception cause, Class recordClass, UUID recordId, String additionalInformation) {
        this(cause);
        this.recordClass = recordClass;
        this.recordId = recordId;
        this.additionalInformation = additionalInformation;
    }
}