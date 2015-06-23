package com.netflix.eureka2.testkit.netrouter.internal;

import java.util.concurrent.TimeUnit;

import com.netflix.eureka2.testkit.netrouter.NetworkLink;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * @author Tomasz Bak
 */
public class NetworkLinkImpl implements NetworkLink {

    private volatile boolean isConnected = true;

    private final Subject<LinkEvent, LinkEvent> linkEventSubject = new SerializedSubject<>(PublishSubject.<LinkEvent>create());

    public Observable<LinkEvent> linkEvents() {
        return linkEventSubject;
    }

    @Override
    public boolean isUp() {
        return isConnected;
    }

    @Override
    public Observable<Void> connect() {
        isConnected = true;
        LinkEvent event = new LinkEvent(this);
        linkEventSubject.onNext(event);
        return event.processed();
    }

    @Override
    public void connect(long timeout, TimeUnit timeUnit) {
        connect().timeout(timeout, timeUnit).toBlocking().firstOrDefault(null);
    }

    @Override
    public Observable<Void> disconnect() {
        isConnected = false;
        LinkEvent event = new LinkEvent(this);
        linkEventSubject.onNext(event);
        return event.processed();
    }

    @Override
    public void disconnect(long timeout, TimeUnit timeUnit) {
        disconnect().timeout(timeout, timeUnit).toBlocking().firstOrDefault(null);
    }

    @Override
    public void limitBandwidthTo(int throughput, BandwidthUnit bandwidthUnit) {
        throw new IllegalStateException("not implemented yet");
    }

    @Override
    public boolean openUnlimitedBandwidth() {
        throw new IllegalStateException("not implemented yet");
    }

    @Override
    public void injectLatency(long time, TimeUnit timeUnit) {
        throw new IllegalStateException("not implemented yet");
    }

    @Override
    public void injectLatency(long time, long jitterAmplitude, TimeUnit timeUnit) {
        throw new IllegalStateException("not implemented yet");
    }
}
