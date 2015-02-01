package org.openmrs.module.imbemr;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.LocationService;
import org.openmrs.module.emrapi.EmrApiProperties;
import org.openmrs.module.emrapi.utils.MetadataUtil;
import org.openmrs.module.referencemetadata.ReferenceMetadataProperties;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.TaskDefinition;
import org.openmrs.scheduler.tasks.ProcessHL7InQueueTask;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Collection;

import static org.junit.Assert.assertThat;
import static org.openmrs.test.OpenmrsMatchers.hasUuid;

/**
 *
 */
public class ImbEmrActivatorComponentTest extends BaseModuleContextSensitiveTest {

    @Autowired
    private EmrApiProperties emrApiProperties;

    @Autowired
    @Qualifier("adminService")
    private AdministrationService administrationService;

    @Autowired
    private LocationService locationService;
    
    @Autowired
    private SchedulerService schedulerService;

    @Test
    public void testSetUpAdtGlobalProperties() throws Exception {
        MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "Reference_Application_Visit_and_Encounter_Types");

        ImbEmrActivator referenceApplicationActivator = new ImbEmrActivator();
        referenceApplicationActivator.setupEmrApiGlobalProperties(administrationService);

        assertThat(emrApiProperties.getAdmissionEncounterType(), hasUuid(ReferenceMetadataProperties.ADMISSION_ENCOUNTER_TYPE_UUID));
        assertThat(emrApiProperties.getExitFromInpatientEncounterType(), hasUuid(ReferenceMetadataProperties.DISCHARGE_ENCOUNTER_TYPE_UUID));
        assertThat(emrApiProperties.getTransferWithinHospitalEncounterType(), hasUuid(ReferenceMetadataProperties.TRANSFER_ENCOUNTER_TYPE_UUID));
        assertThat(emrApiProperties.getCheckInEncounterType(), hasUuid(ReferenceMetadataProperties.CHECK_IN_ENCOUNTER_TYPE_UUID));

        assertThat(emrApiProperties.getAtFacilityVisitType(), hasUuid(ReferenceMetadataProperties.FACILITY_VISIT_TYPE_UUID));
    }
    
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
