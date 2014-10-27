package com.inductiveautomation.opcua.sdk.core.model.objects;

import com.inductiveautomation.opcua.stack.core.types.builtin.LocalizedText;

public interface StateMachineType extends BaseObjectType {

    LocalizedText getCurrentState();

    LocalizedText getLastTransition();

    void setCurrentState(LocalizedText currentState);

    void setLastTransition(LocalizedText lastTransition);

    void atomicSet(Runnable runnable);

}
