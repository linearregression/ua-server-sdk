/*
 * digitalpetri OPC-UA SDK
 *
 * Copyright (C) 2015 Kevin Herron
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.digitalpetri.opcua.sdk.server.subscriptions;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import com.digitalpetri.opcua.stack.core.application.services.ServiceRequest;
import com.digitalpetri.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.digitalpetri.opcua.stack.core.types.structured.PublishRequest;
import com.digitalpetri.opcua.stack.core.types.structured.PublishResponse;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublishQueue {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final LinkedList<ServiceRequest<PublishRequest, PublishResponse>> serviceQueue = new LinkedList<>();

    private final LinkedHashMap<UInteger, WaitingSubscription> waitList = new LinkedHashMap<>();

    /**
     * Add a Publish {@link ServiceRequest} to the queue.
     * <p>
     * If there are wait-listed Subscriptions this request will be used immediately, otherwise it will be queued for
     * later use by a Subscription whose publish timer has expired and has notifications to send.
     *
     * @param service the Publish {@link ServiceRequest}.
     */
    public synchronized void addRequest(ServiceRequest<PublishRequest, PublishResponse> service) {
        List<WaitingSubscription> waitingSubscriptions = Lists.newArrayList(waitList.values());

        if (waitingSubscriptions.isEmpty()) {
            serviceQueue.add(service);

            logger.debug("Queued PublishRequest, size={}", serviceQueue.size());
        } else {
            WaitingSubscription subscription = null;

            int maxPriority = 0;
            long minWaitingSince = Long.MAX_VALUE;

            for (WaitingSubscription waiting : waitingSubscriptions) {
                int priority = waiting.getSubscription().getPriority();
                long waitingSince = waiting.getWaitingSince().getTime();

                if (priority > maxPriority) {
                    maxPriority = priority;
                    minWaitingSince = Long.MAX_VALUE;
                }
                if (priority >= maxPriority && waitingSince < minWaitingSince) {
                    minWaitingSince = waitingSince;
                    subscription = waiting;
                }
            }

            if (subscription != null) {
                waitList.remove(subscription.subscription.getId());

                logger.debug("Delivering PublishRequest to Subscription [id={}]",
                        subscription.getSubscription().getId());

                subscription.subscription.onPublish(service);
            } else {
                serviceQueue.add(service);
            }
        }
    }

    /**
     * Add a subscription to the wait list.
     * <p>
     * A subscription should be added to the wait list when either:
     * <p>
     * a) The previous Publish response indicated that there were still more Notifications ready to be transferred and
     * there were no more Publish requests queued to transfer them.
     * <p>
     * b) The publishing timer of a Subscription expired and there were either Notifications to be sent or a keep-alive
     * Message to be sent.
     *
     * @param subscription the subscription to wait-list.
     */
    public synchronized void addSubscription(Subscription subscription) {
        if (waitList.isEmpty() && !serviceQueue.isEmpty()) {
            subscription.onPublish(serviceQueue.poll());
        } else {
            waitList.putIfAbsent(subscription.getId(), new WaitingSubscription(subscription));
        }
    }

    public synchronized boolean isEmpty() {
        return serviceQueue.isEmpty();
    }

    public synchronized boolean isNotEmpty() {
        return !isEmpty();
    }

    public synchronized ServiceRequest<PublishRequest, PublishResponse> poll() {
        return serviceQueue.poll();
    }

    public static class WaitingSubscription {

        private final Date waitingSince = new Date();

        private final Subscription subscription;

        public WaitingSubscription(Subscription subscription) {
            this.subscription = subscription;
        }

        public Subscription getSubscription() {
            return subscription;
        }

        public Date getWaitingSince() {
            return waitingSince;
        }

    }

}
