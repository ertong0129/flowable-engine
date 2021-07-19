/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.engine.impl.bpmn.parser.factory;

import org.flowable.bpmn.model.Activity;
import org.flowable.bpmn.model.BoundaryEvent;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.BusinessRuleTask;
import org.flowable.bpmn.model.CallActivity;
import org.flowable.bpmn.model.CancelEventDefinition;
import org.flowable.bpmn.model.CaseServiceTask;
import org.flowable.bpmn.model.CompensateEventDefinition;
import org.flowable.bpmn.model.ConditionalEventDefinition;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.ErrorEventDefinition;
import org.flowable.bpmn.model.Escalation;
import org.flowable.bpmn.model.EscalationEventDefinition;
import org.flowable.bpmn.model.EventGateway;
import org.flowable.bpmn.model.EventSubProcess;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.ExternalWorkerServiceTask;
import org.flowable.bpmn.model.InclusiveGateway;
import org.flowable.bpmn.model.IntermediateCatchEvent;
import org.flowable.bpmn.model.ManualTask;
import org.flowable.bpmn.model.MessageEventDefinition;
import org.flowable.bpmn.model.ParallelGateway;
import org.flowable.bpmn.model.ReceiveTask;
import org.flowable.bpmn.model.ScriptTask;
import org.flowable.bpmn.model.SendEventServiceTask;
import org.flowable.bpmn.model.SendTask;
import org.flowable.bpmn.model.ServiceTask;
import org.flowable.bpmn.model.Signal;
import org.flowable.bpmn.model.SignalEventDefinition;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.SubProcess;
import org.flowable.bpmn.model.Task;
import org.flowable.bpmn.model.ThrowEvent;
import org.flowable.bpmn.model.TimerEventDefinition;
import org.flowable.bpmn.model.Transaction;
import org.flowable.bpmn.model.UserTask;
import org.flowable.bpmn.model.VariableListenerEventDefinition;
import org.flowable.engine.impl.bpmn.behavior.*;
import org.flowable.engine.impl.bpmn.helper.ClassDelegate;
import org.flowable.engine.impl.bpmn.parser.BpmnParse;
import org.flowable.engine.impl.bpmn.parser.BpmnParser;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.impl.delegate.ActivityBehavior;

/**
 * Factory class used by the {@link BpmnParser} and {@link BpmnParse} to instantiate the behaviour classes. For example when parsing an exclusive gateway, this factory will be requested to create a
 * new {@link ActivityBehavior} that will be set on the element of that step of the process and will implement the spec-compliant behavior of the exclusive gateway.
 * 
 * You can provide your own implementation of this class. This way, you can give different execution semantics to a standard bpmn xml construct. Eg. you could tweak the exclusive gateway to do
 * something completely different if you would want that. Creating your own {@link ActivityBehaviorFactory} is only advisable if you want to change the default behavior of any BPMN default construct.
 * And even then, think twice, because it won't be spec compliant bpmn anymore.
 * 
 * Note that you can always express any custom step as a service task with a class delegation.
 * 
 * The easiest and advisable way to implement your own {@link ActivityBehaviorFactory} is to extend the {@link DefaultActivityBehaviorFactory} class and override the method specific to the
 * {@link ActivityBehavior} you want to change.
 * 
 * An instance of this interface can be injected in the {@link ProcessEngineConfigurationImpl} and its subclasses.
 * 
 * @author Joram Barrez
 */
public interface ActivityBehaviorFactory {

    NoneStartEventActivityBehavior createNoneStartEventActivityBehavior(StartEvent startEvent);

    TaskActivityBehavior createTaskActivityBehavior(Task task);

    ManualTaskActivityBehavior createManualTaskActivityBehavior(ManualTask manualTask);

    ReceiveTaskActivityBehavior createReceiveTaskActivityBehavior(ReceiveTask receiveTask);

    ReceiveEventTaskActivityBehavior createReceiveEventTaskActivityBehavior(ReceiveTask receiveTask, String eventDefinitionKey);

    UserTaskActivityBehavior createUserTaskActivityBehavior(UserTask userTask);

    ClassDelegate createClassDelegateServiceTask(ServiceTask serviceTask);

    ServiceTaskDelegateExpressionActivityBehavior createServiceTaskDelegateExpressionActivityBehavior(ServiceTask serviceTask);

    ServiceTaskExpressionActivityBehavior createServiceTaskExpressionActivityBehavior(ServiceTask serviceTask);

    WebServiceActivityBehavior createWebServiceActivityBehavior(ServiceTask serviceTask, BpmnModel bpmnModel);

    WebServiceActivityBehavior createWebServiceActivityBehavior(SendTask sendTask, BpmnModel bpmnModel);

    MailActivityBehavior createMailActivityBehavior(ServiceTask serviceTask);

    MailActivityBehavior createMailActivityBehavior(SendTask sendTask);

    // We do not want a hard dependency on the Mule module, hence we return
    // ActivityBehavior and instantiate the delegate instance using a string instead of the Class itself.
    ActivityBehavior createMuleActivityBehavior(ServiceTask serviceTask);

    ActivityBehavior createMuleActivityBehavior(SendTask sendTask);

    ActivityBehavior createCamelActivityBehavior(ServiceTask serviceTask);

    ActivityBehavior createCamelActivityBehavior(SendTask sendTask);

    ActivityBehavior createDmnActivityBehavior(ServiceTask serviceTask);

    ActivityBehavior createDmnActivityBehavior(SendTask sendTask);

    ActivityBehavior createHttpActivityBehavior(ServiceTask serviceTask);

    ShellActivityBehavior createShellActivityBehavior(ServiceTask serviceTask);

    ActivityBehavior createBusinessRuleTaskActivityBehavior(BusinessRuleTask businessRuleTask);

    ScriptTaskActivityBehavior createScriptTaskActivityBehavior(ScriptTask scriptTask);
    
    SendEventTaskActivityBehavior createSendEventTaskBehavior(SendEventServiceTask sendEventServiceTask);

    ExternalWorkerTaskActivityBehavior createExternalWorkerTaskBehavior(ExternalWorkerServiceTask externalWorkerServiceTask);

    ExclusiveGatewayActivityBehavior createExclusiveGatewayActivityBehavior(ExclusiveGateway exclusiveGateway);

    ParallelGatewayActivityBehavior createParallelGatewayActivityBehavior(ParallelGateway parallelGateway);

    InclusiveGatewayActivityBehavior createInclusiveGatewayActivityBehavior(InclusiveGateway inclusiveGateway);

    EventBasedGatewayActivityBehavior createEventBasedGatewayActivityBehavior(EventGateway eventGateway);

    SequentialMultiInstanceBehavior createSequentialMultiInstanceBehavior(Activity activity, AbstractBpmnActivityBehavior innerActivityBehavior);

    ParallelMultiInstanceBehavior createParallelMultiInstanceBehavior(Activity activity, AbstractBpmnActivityBehavior innerActivityBehavior);

    SubProcessActivityBehavior createSubprocessActivityBehavior(SubProcess subProcess);

    EventSubProcessActivityBehavior createEventSubprocessActivityBehavior(EventSubProcess eventSubProcess);
    
    EventSubProcessConditionalStartEventActivityBehavior createEventSubProcessConditionalStartEventActivityBehavior(StartEvent startEvent,
            ConditionalEventDefinition conditionalEventDefinition, String conditionExpression);

    EventSubProcessErrorStartEventActivityBehavior createEventSubProcessErrorStartEventActivityBehavior(StartEvent startEvent);
    
    EventSubProcessEscalationStartEventActivityBehavior createEventSubProcessEscalationStartEventActivityBehavior(StartEvent startEvent);

    EventSubProcessMessageStartEventActivityBehavior createEventSubProcessMessageStartEventActivityBehavior(StartEvent startEvent, MessageEventDefinition messageEventDefinition);

    EventSubProcessSignalStartEventActivityBehavior createEventSubProcessSignalStartEventActivityBehavior(StartEvent startEvent, SignalEventDefinition signalEventDefinition, Signal signal);

    EventSubProcessTimerStartEventActivityBehavior createEventSubProcessTimerStartEventActivityBehavior(StartEvent startEvent, TimerEventDefinition timerEventDefinition);
    
    EventSubProcessEventRegistryStartEventActivityBehavior createEventSubProcessEventRegistryStartEventActivityBehavior(StartEvent startEvent, String eventDefinitionKey);
    
    EventSubProcessVariableListenerlStartEventActivityBehavior createEventSubProcessVariableListenerlStartEventActivityBehavior(StartEvent startEvent, VariableListenerEventDefinition variableListenerEventDefinition);
    
    AdhocSubProcessActivityBehavior createAdhocSubprocessActivityBehavior(SubProcess subProcess);

    CallActivityBehavior createCallActivityBehavior(CallActivity callActivity);
    
    CaseTaskActivityBehavior createCaseTaskBehavior(CaseServiceTask caseServiceTask);

    TransactionActivityBehavior createTransactionActivityBehavior(Transaction transaction);

    IntermediateCatchEventActivityBehavior createIntermediateCatchEventActivityBehavior(IntermediateCatchEvent intermediateCatchEvent);

    IntermediateCatchMessageEventActivityBehavior createIntermediateCatchMessageEventActivityBehavior(IntermediateCatchEvent intermediateCatchEvent,
            MessageEventDefinition messageEventDefinition);
    
    IntermediateCatchConditionalEventActivityBehavior createIntermediateCatchConditionalEventActivityBehavior(IntermediateCatchEvent intermediateCatchEvent,
            ConditionalEventDefinition conditionalEventDefinition, String conditionExpression);

    IntermediateCatchTimerEventActivityBehavior createIntermediateCatchTimerEventActivityBehavior(IntermediateCatchEvent intermediateCatchEvent, TimerEventDefinition timerEventDefinition);

    IntermediateCatchEventRegistryEventActivityBehavior createIntermediateCatchEventRegistryEventActivityBehavior(IntermediateCatchEvent intermediateCatchEvent, String eventDefinitionKey);

    IntermediateCatchSignalEventActivityBehavior createIntermediateCatchSignalEventActivityBehavior(IntermediateCatchEvent intermediateCatchEvent,
            SignalEventDefinition signalEventDefinition, Signal signal);
    
    IntermediateCatchVariableListenerEventActivityBehavior createIntermediateCatchVariableListenerEventActivityBehavior(IntermediateCatchEvent intermediateCatchEvent, 
            VariableListenerEventDefinition variableListenerEventDefinition);
    
    IntermediateThrowNoneEventActivityBehavior createIntermediateThrowNoneEventActivityBehavior(ThrowEvent throwEvent);

    IntermediateThrowSignalEventActivityBehavior createIntermediateThrowSignalEventActivityBehavior(ThrowEvent throwEvent, SignalEventDefinition signalEventDefinition, Signal signal);
    
    IntermediateThrowEscalationEventActivityBehavior createIntermediateThrowEscalationEventActivityBehavior(ThrowEvent throwEvent, EscalationEventDefinition escalationEventDefinition, Escalation escalation);

    IntermediateThrowCompensationEventActivityBehavior createIntermediateThrowCompensationEventActivityBehavior(ThrowEvent throwEvent, CompensateEventDefinition compensateEventDefinition);

    NoneEndEventActivityBehavior createNoneEndEventActivityBehavior(EndEvent endEvent);

    ErrorEndEventActivityBehavior createErrorEndEventActivityBehavior(EndEvent endEvent, ErrorEventDefinition errorEventDefinition);
    
    EscalationEndEventActivityBehavior createEscalationEndEventActivityBehavior(EndEvent endEvent, EscalationEventDefinition escalationEventDefinition, Escalation escalation);

    CancelEndEventActivityBehavior createCancelEndEventActivityBehavior(EndEvent endEvent);

    TerminateEndEventActivityBehavior createTerminateEndEventActivityBehavior(EndEvent endEvent);

    SignalEndEventActivityBehavior createSignalEndEventActivityBehavior(EndEvent endEvent,
                                                                        SignalEventDefinition signalEventDefinition, Signal signal);

    BoundaryEventActivityBehavior createBoundaryEventActivityBehavior(BoundaryEvent boundaryEvent, boolean interrupting);

    BoundaryCancelEventActivityBehavior createBoundaryCancelEventActivityBehavior(CancelEventDefinition cancelEventDefinition);

    BoundaryTimerEventActivityBehavior createBoundaryTimerEventActivityBehavior(BoundaryEvent boundaryEvent, TimerEventDefinition timerEventDefinition, boolean interrupting);

    BoundarySignalEventActivityBehavior createBoundarySignalEventActivityBehavior(BoundaryEvent boundaryEvent, SignalEventDefinition signalEventDefinition, Signal signal, boolean interrupting);

    BoundaryMessageEventActivityBehavior createBoundaryMessageEventActivityBehavior(BoundaryEvent boundaryEvent, MessageEventDefinition messageEventDefinition, boolean interrupting);
    
    BoundaryConditionalEventActivityBehavior createBoundaryConditionalEventActivityBehavior(BoundaryEvent boundaryEvent, ConditionalEventDefinition conditionalEventDefinition,
            String conditionExpression, boolean interrupting);
    
    BoundaryEscalationEventActivityBehavior createBoundaryEscalationEventActivityBehavior(BoundaryEvent boundaryEvent, EscalationEventDefinition escalationEventDefinition, Escalation escalation, boolean interrupting);

    BoundaryCompensateEventActivityBehavior createBoundaryCompensateEventActivityBehavior(BoundaryEvent boundaryEvent, CompensateEventDefinition compensateEventDefinition, boolean interrupting);
    
    BoundaryEventRegistryEventActivityBehavior createBoundaryEventRegistryEventActivityBehavior(BoundaryEvent boundaryEvent, String eventDefinitionKey, boolean interrupting);
    
    BoundaryVariableListenerEventActivityBehavior createBoundaryVariableListenerEventActivityBehavior(BoundaryEvent boundaryEvent, VariableListenerEventDefinition variableListenerEventDefinition, boolean interrupting);
}