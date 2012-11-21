/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guzzler.common.exception;

/**
 *
 * @author ajuste
 */
public class FileAccessException extends Exception {

    public FileAccessException() {
    }

    public FileAccessException(String detailMessage) {
        super(detailMessage);
    }

    public FileAccessException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public FileAccessException(Throwable throwable) {
        super(throwable);
    }
}