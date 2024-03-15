package itssc.kontron.erpmperformancetests.service.impl;

import itssc.kontron.erpmperformancetests.service.CamundaRestService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CamundaRestServiceImpl implements CamundaRestService {

  private final HistoryService historyService;
  private final RuntimeService runtimeService;

  public CamundaRestServiceImpl(HistoryService historyService, RuntimeService runtimeService) {
    this.historyService = historyService;
    this.runtimeService = runtimeService;
  }

  @Override
  public void startProcess(String processDefinitionKey, Map<String, Object> variables) {
    runtimeService.createProcessInstanceByKey(processDefinitionKey)
                  .setVariables(variables)
                  .execute();
  }

  @Override
  public String getProcessState(String processInstanceId) {
    return historyService.createHistoricProcessInstanceQuery()
                         .processInstanceId(processInstanceId)
                         .singleResult()
                         .getState();
  }

  @Override
  public long getActiveCount() {
    return historyService.createHistoricProcessInstanceQuery()
                         .active().count();
  }

  @Override
  public long getFinishedCount() {
    return historyService.createHistoricProcessInstanceQuery()
                         .finished().count();
  }

  @Override
  public long getActiveCount(String processDefinitionKey) {
    return historyService.createHistoricProcessInstanceQuery()
                         .processDefinitionKey(processDefinitionKey)
                         .active()
                         .count();
  }

  @Override
  public boolean isActive(String processInstanceId) {
    return historyService.createHistoricProcessInstanceQuery()
                         .processInstanceId(processInstanceId)
                         .finished()
                         .list()
                         .size() == 1;
  }

}
