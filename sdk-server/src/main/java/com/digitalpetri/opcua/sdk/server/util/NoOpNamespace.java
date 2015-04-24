/*
 * Copyright 2014
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.digitalpetri.opcua.sdk.server.util;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.digitalpetri.opcua.sdk.core.Reference;
import com.digitalpetri.opcua.sdk.server.api.DataItem;
import com.digitalpetri.opcua.sdk.server.api.EventItem;
import com.digitalpetri.opcua.sdk.server.api.MonitoredItem;
import com.digitalpetri.opcua.sdk.server.api.Namespace;
import com.digitalpetri.opcua.stack.core.StatusCodes;
import com.digitalpetri.opcua.stack.core.UaException;
import com.digitalpetri.opcua.stack.core.types.builtin.DataValue;
import com.digitalpetri.opcua.stack.core.types.builtin.NodeId;
import com.digitalpetri.opcua.stack.core.types.builtin.StatusCode;
import com.digitalpetri.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.digitalpetri.opcua.stack.core.types.builtin.unsigned.UShort;
import com.digitalpetri.opcua.stack.core.types.enumerated.TimestampsToReturn;
import com.digitalpetri.opcua.stack.core.types.structured.ReadValueId;
import com.digitalpetri.opcua.stack.core.types.structured.WriteValue;

import static com.digitalpetri.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

public class NoOpNamespace implements Namespace {

    @Override
    public UShort getNamespaceIndex() {
        return ushort(UShort.MAX_VALUE);
    }

    @Override
    public String getNamespaceUri() {
        return getClass().getSimpleName();
    }

    @Override
    public CompletableFuture<List<Reference>> getReferences(NodeId nodeId) {
        CompletableFuture<List<Reference>> f = new CompletableFuture<>();
        f.completeExceptionally(new UaException(StatusCodes.Bad_NodeIdUnknown));
        return f;
    }

    @Override
    public void read(Double maxAge,
                     TimestampsToReturn timestamps,
                     List<ReadValueId> readValueIds,
                     ReadContext context) {

        List<DataValue> values = Collections.nCopies(
                readValueIds.size(), new DataValue(StatusCodes.Bad_NodeIdUnknown));

        context.getFuture().complete(values);
    }

    @Override
    public void write(List<WriteValue> writeValues, WriteContext context) {
        List<StatusCode> results = Collections.nCopies(
                writeValues.size(), new StatusCode(StatusCodes.Bad_NodeIdUnknown));

        context.getFuture().complete(results);
    }

    @Override
    public void onCreateMonitoredItem(NodeId nodeId,
                                      UInteger attributeId,
                                      double requestedSamplingInterval,
                                      CompletableFuture<Double> revisedSamplingInterval) {

        revisedSamplingInterval.completeExceptionally(new UaException(StatusCodes.Bad_NodeIdUnknown));
    }

    @Override
    public void onModifyMonitoredItem(double requestedSamplingInterval,
                                      CompletableFuture<Double> revisedSamplingInterval) {
        
        revisedSamplingInterval.completeExceptionally(new UaException(StatusCodes.Bad_NodeIdUnknown));
    }

    @Override
    public void onDataItemsCreated(List<DataItem> dataItems) {

    }

    @Override
    public void onDataItemsModified(List<DataItem> dataItems) {

    }

    @Override
    public void onDataItemsDeleted(List<DataItem> dataItems) {

    }

    @Override
    public void onEventItemsCreated(List<EventItem> eventItems) {

    }

    @Override
    public void onEventItemsModified(List<EventItem> eventItems) {

    }

    @Override
    public void onEventItemsDeleted(List<EventItem> eventItems) {

    }

    @Override
    public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {

    }

}