package org.openmrs.module.imbemr;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.LocationService;
import org.openmrs.module.emrapi.EmrApiProperties;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.TaskDefinition;
import org.openmrs.scheduler.tasks.ProcessHL7InQueueTask;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Collection;

/**
 *
 */
public class ImbEmrActivatorComponentTest extends BaseModuleContextSensitiveTest {
    
    @Autowired
    private SchedulerService schedulerService;
    
    /**
     * Tests that if process hl7 task is set up correctly
     *
     * @throws Exception
     */
	@Test
	public void testSetupOfProcessHL7Task() throws Exception {
		new ImbEmrActivator().started();
		Collection<TaskDefinition> registeredTasks = schedulerService.getRegisteredTasks();
		TaskDefinition processHL7Task = null;
		for (TaskDefinition registeredTask : registeredTasks) {
			if (ProcessHL7InQueueTask.class.getName().equals(registeredTask.getTaskClass())) {
				processHL7Task = registeredTask;
			}
		}
		Assert.assertNotNull(processHL7Task);
	}

}
