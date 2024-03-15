package itssc.kontron.erpmperformancetests.service;


import java.util.Map;

public interface CamundaRestService {

  void startProcess(String businessKey, Map<String, Object> variables);

  String getProcessState(String processInstanceId);

  long getActiveCount();

  long getFinishedCount();

  long getActiveCount(String processDefinitionKey);

  boolean isActive(String processInstanceId);

}
