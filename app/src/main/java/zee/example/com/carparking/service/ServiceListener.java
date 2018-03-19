package zee.example.com.carparking.service;

/**
 * Created by Zul Qarnain on 3/14/2018.
 */

public interface ServiceListener<T> {
    void success(T obj);
    void fail(ServiceError error);
}
