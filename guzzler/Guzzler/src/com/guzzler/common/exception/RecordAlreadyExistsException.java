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
public class RecordAlreadyExistsException extends Exception {

    protected Class recordClass;
    protected UUID recordId;
    protected String additionalInformation;

    public RecordAlreadyExistsException(Exception cause) {
        super(cause);
    }

    public RecordAlreadyExistsException(Exception cause, Class recordClass, UUID recordId, String additionalInformation) {
        this(cause);
        this.recordClass = recordClass;
        this.recordId = recordId;
        this.additionalInformation = additionalInformation;
    }
}