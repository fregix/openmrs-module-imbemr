/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.imbemr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.FormService;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.Module;
import org.openmrs.module.ModuleActivator;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.htmlformentry.HtmlFormEntryService;
import org.openmrs.module.htmlformentryui.HtmlFormUtil;
import org.openmrs.module.namephonetics.NamePhoneticsConstants;
import org.openmrs.module.registrationcore.RegistrationCoreConstants;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.TaskDefinition;
import org.openmrs.scheduler.tasks.ProcessHL7InQueueTask;
import org.openmrs.ui.framework.resource.ResourceFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class ImbEmrActivator extends BaseModuleActivator {
	
	protected Log log = LogFactory.getLog(getClass());
	
	/**
	 * @see ModuleActivator#willRefreshContext()
	 */
	public void willRefreshContext() {
		log.info("Refreshing IMB EMR Module");
	}
	
	/**
	 * @see ModuleActivator#contextRefreshed()
	 */
	public void contextRefreshed() {
		log.info("IMB EMR Module refreshed");
	}
	
	/**
	 * @see ModuleActivator#willStart()
	 */
	public void willStart() {
		log.info("Starting IMB EMR Module");
	}
	
	/**
	 * @see ModuleActivator#started()
	 */
	public void started() {
		try {
	        AdministrationService administrationService = Context.getAdministrationService();
	        AppFrameworkService appFrameworkService = Context.getService(AppFrameworkService.class);
	        SchedulerService schedulerService = Context.getSchedulerService();
	
	        appFrameworkService.disableApp("registrationapp.basicRegisterPatient");
	        appFrameworkService.disableApp("coreapps.awaitingAdmission");
	
	        administrationService.saveGlobalProperty(new GlobalProperty("registrationcore.patientNameSearch",
	                "registrationcore.ExistingPatientNameSearch"));

	        setupNamePhoneticsGlobalProperties(administrationService);
	        setupRegistrationcoreGlobalProperties(administrationService);
	        setupConceptManagementAppsGlobalProperties(administrationService);
	        setupHtmlForms();
	        setupHL7ProcessingTask(schedulerService);
		} 
		catch (Exception e) {
            Module mod = ModuleFactory.getModuleById(ImbEmrConstants.MODULE_ID);
            ModuleFactory.stopModule(mod);
            throw new RuntimeException("failed to setup the required modules", e);
        }

        log.info("Reference Application Module started");
	}
	
	public void setupConceptManagementAppsGlobalProperties(AdministrationService administrationService) {
		setGlobalProperty(administrationService, "conceptmanagementapps.snomedCtConceptSource",
		    "1ADDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
	}

    private void setupNamePhoneticsGlobalProperties(AdministrationService administrationService) {
		setGlobalProperty(administrationService, NamePhoneticsConstants.GIVEN_NAME_GLOBAL_PROPERTY, "Soundex");
		setGlobalProperty(administrationService, NamePhoneticsConstants.MIDDLE_NAME_GLOBAL_PROPERTY, "Soundex");
		setGlobalProperty(administrationService, NamePhoneticsConstants.FAMILY_NAME_GLOBAL_PROPERTY, "Soundex");
		setGlobalProperty(administrationService, NamePhoneticsConstants.FAMILY_NAME2_GLOBAL_PROPERTY, "Soundex");
	}
	
	private void setupRegistrationcoreGlobalProperties(AdministrationService administrationService) {
		setGlobalProperty(administrationService, RegistrationCoreConstants.GP_PATIENT_NAME_SEARCH, "registrationcore.NamePhoneticsPatientNameSearch");
		setGlobalProperty(administrationService, RegistrationCoreConstants.GP_FAST_SIMILAR_PATIENT_SEARCH_ALGORITHM, "registrationcore.NamePhoneticsPatientSearchAlgorithm");
		setGlobalProperty(administrationService, RegistrationCoreConstants.GP_PRECISE_SIMILAR_PATIENT_SEARCH_ALGORITHM, "registrationcore.BasicExactPatientSearchAlgorithm");
	}
	
    private void setGlobalProperty(AdministrationService administrationService, String propertyName, String propertyValue) {
        GlobalProperty gp = administrationService.getGlobalPropertyObject(propertyName);
        if (gp == null) {
            gp = new GlobalProperty(propertyName, propertyValue);
        }
        gp.setPropertyValue(propertyValue);
        administrationService.saveGlobalProperty(gp);
    }
    
    private void setupHtmlForms() throws Exception {
        try {
             ResourceFactory resourceFactory = ResourceFactory.getInstance();
             FormService formService = Context.getFormService();
             HtmlFormEntryService htmlFormEntryService = Context.getService(HtmlFormEntryService.class);

 			 List<String> htmlforms = Arrays.asList("imbemr:htmlforms/vitals.xml",
			    "imbemr:htmlforms/simpleVisitNote.xml", "imbemr:htmlforms/simpleAdmission.xml",
			    "imbemr:htmlforms/simpleDischarge.xml", "imbemr:htmlforms/simpleTransfer.xml");

             for (String htmlform : htmlforms) {
                 HtmlFormUtil.getHtmlFormFromUiResource(resourceFactory, formService, htmlFormEntryService, htmlform);
             }
        }
        catch (Exception e) {
             // this is a hack to get component test to pass until we find the proper way to mock this
             if (ResourceFactory.getInstance().getResourceProviders() == null) {
                 log.error("Unable to load HTML forms--this error is expected when running component tests");
             }
             else {
                 throw e;
             }
        }
     }
    
	public void setupHL7ProcessingTask(SchedulerService schedulerService) {
		boolean processHL7taskSet = false;
		Collection<TaskDefinition> registeredTasks = schedulerService.getRegisteredTasks();
		for (TaskDefinition registeredTask : registeredTasks) {
			if (ProcessHL7InQueueTask.class.getName().equals(registeredTask.getTaskClass())) {
				processHL7taskSet = true;
				break;
			}
		}
		
		if (!processHL7taskSet) {
			TaskDefinition processHL7Task = new TaskDefinition();
			processHL7Task.setTaskClass(ProcessHL7InQueueTask.class.getName());
			processHL7Task.setName(ImbEmrConstants.PROCESS_HL7_TASK_NAME);
			processHL7Task.setStartOnStartup(true);
			processHL7Task.setRepeatInterval(ImbEmrConstants.PROCESS_HL7_TASK_INTERVAL);
			
			schedulerService.saveTask(processHL7Task);
		}
	}

    /**
	 * @see ModuleActivator#willStop()
	 */
	public void willStop() {
		log.info("Stopping Reference Application Module");
	}
	
	/**
	 * @see ModuleActivator#stopped()
	 */
	public void stopped() {
		log.info("Reference Application Module stopped");

        Context.getService(AppFrameworkService.class).enableApp("registrationapp.basicRegisterPatient");
	}
	
}
