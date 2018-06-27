/**
 * 
 */
package org.zdxue.microservice.xxx.exception;

/**
 * @author zdxue
 *
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1653257222557341992L;

    public ServiceException() {
    }

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(String msg, Throwable e) {
        super(msg, e);
    }

}
