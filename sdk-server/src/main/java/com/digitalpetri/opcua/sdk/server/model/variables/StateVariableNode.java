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

package com.digitalpetri.opcua.sdk.server.model.variables;

import java.util.Optional;

import com.digitalpetri.opcua.sdk.core.model.UaMandatory;
import com.digitalpetri.opcua.sdk.core.model.UaOptional;
import com.digitalpetri.opcua.sdk.core.model.variables.StateVariableType;
import com.digitalpetri.opcua.sdk.server.api.UaNamespace;
import com.digitalpetri.opcua.sdk.server.util.UaVariableType;
import com.digitalpetri.opcua.stack.core.types.builtin.DataValue;
import com.digitalpetri.opcua.stack.core.types.builtin.LocalizedText;
import com.digitalpetri.opcua.stack.core.types.builtin.NodeId;
import com.digitalpetri.opcua.stack.core.types.builtin.QualifiedName;
import com.digitalpetri.opcua.stack.core.types.builtin.Variant;
import com.digitalpetri.opcua.stack.core.types.builtin.unsigned.UByte;
import com.digitalpetri.opcua.stack.core.types.builtin.unsigned.UInteger;

@UaVariableType(name = "StateVariableType")
public class StateVariableNode extends BaseDataVariableNode implements StateVariableType {

    public StateVariableNode(UaNamespace namespace,
                             NodeId nodeId,
                             QualifiedName browseName,
                             LocalizedText displayName,
                             Optional<LocalizedText> description,
                             Optional<UInteger> writeMask,
                             Optional<UInteger> userWriteMask,
                             DataValue value,
                             NodeId dataType,
                             Integer valueRank,
                             Optional<UInteger[]> arrayDimensions,
                             UByte accessLevel,
                             UByte userAccessLevel,
                             Optional<Double> minimumSamplingInterval,
                             boolean historizing) {

        super(namespace, nodeId, browseName, displayName, description, writeMask, userWriteMask,
                value, dataType, valueRank, arrayDimensions, accessLevel, userAccessLevel, minimumSamplingInterval, historizing);

    }

    @Override
    @UaMandatory("Id")
    public Object getId() {
        Optional<Object> id = getProperty("Id");

        return id.orElse(null);
    }

    @Override
    @UaOptional("Name")
    public QualifiedName getName() {
        Optional<QualifiedName> name = getProperty("Name");

        return name.orElse(null);
    }

    @Override
    @UaOptional("Number")
    public UInteger getNumber() {
        Optional<UInteger> number = getProperty("Number");

        return number.orElse(null);
    }

    @Override
    @UaOptional("EffectiveDisplayName")
    public LocalizedText getEffectiveDisplayName() {
        Optional<LocalizedText> effectiveDisplayName = getProperty("EffectiveDisplayName");

        return effectiveDisplayName.orElse(null);
    }

    @Override
    public synchronized void setId(Object id) {
        getPropertyNode("Id").ifPresent(n -> {
            n.setValue(new DataValue(new Variant(id)));
        });
    }

    @Override
    public synchronized void setName(QualifiedName name) {
        getPropertyNode("Name").ifPresent(n -> {
            n.setValue(new DataValue(new Variant(name)));
        });
    }

    @Override
    public synchronized void setNumber(UInteger number) {
        getPropertyNode("Number").ifPresent(n -> {
            n.setValue(new DataValue(new Variant(number)));
        });
    }

    @Override
    public synchronized void setEffectiveDisplayName(LocalizedText effectiveDisplayName) {
        getPropertyNode("EffectiveDisplayName").ifPresent(n -> {
            n.setValue(new DataValue(new Variant(effectiveDisplayName)));
        });
    }

}
