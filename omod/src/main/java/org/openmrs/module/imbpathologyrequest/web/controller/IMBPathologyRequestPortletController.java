package org.openmrs.module.imbpathologyrequest.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.Form;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.module.Extension;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.module.htmlformentry.HtmlFormEntryService;
import org.openmrs.module.htmlformentry.HtmlFormEntryUtil;
import org.openmrs.module.web.extension.FormEntryHandler;
import org.openmrs.web.controller.PortletController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("**/imbPathologyRequestPatientEncounters.portlet")
public class IMBPathologyRequestPortletController extends PortletController {
	
	private static final Log log = LogFactory.getLog(IMBPathologyRequestPortletController.class);
	
	@SuppressWarnings("unchecked")
	@Override
	protected void populateModel(HttpServletRequest request, Map<String, Object> model) {
		//Person person = (Person)model.get("patient");
		int patientId=(Integer) model.get("patientId");
		Person person=(Person) Context.getPatientService().getPatient(patientId);

		if (person == null)
			throw new IllegalArgumentException("This portlet may only be used in the context of a Person");
		
		int providerlocationAttribute=0;
				if (Context.getAuthenticatedUser().getPerson().getAttribute("Health Center")!=null) {
					providerlocationAttribute=Integer.parseInt(Context.getAuthenticatedUser().getPerson()
							.getAttribute("Health Center").getValue());
				}
		if (!model.containsKey("providerlocationAttribute") && providerlocationAttribute!=0 ) {
			model.put("providerlocationAttribute", providerlocationAttribute);
		}
		int patientLocationAttribute =0;
		if (person.getAttribute("Health Center")!=null){
				patientLocationAttribute= Integer.parseInt(person.getAttribute("Health Center").getValue());
	}
		if (!model.containsKey("patientLocationAttribute") && patientLocationAttribute!=0) {
			model.put("patientLocationAttribute", patientLocationAttribute);
		}

		HtmlFormEntryService service = HtmlFormEntryUtil.getService();
		List<HtmlForm> allHtmlforms=service.getAllHtmlForms();
		List<HtmlForm> pathologyHtmlforms=new ArrayList<HtmlForm>();
		int pathologyEncounterType=Integer.parseInt(Context.getAdministrationService().getGlobalProperty("imbpathologyrequest.pathologyEncounterType"));
		for (HtmlForm htmlForm:allHtmlforms) {
			if (htmlForm.getForm().getEncounterType().getEncounterTypeId()==pathologyEncounterType){
				pathologyHtmlforms.add(htmlForm);
			}
		}
model.put("pathologyhtmlForms",pathologyHtmlforms);

		List<Encounter> encounters=Context.getEncounterService().getEncountersByPatientId(patientId);
		List<Encounter> pathologyEncounters=new ArrayList<Encounter>();
		for (Encounter enc:encounters) {
			if (enc.getEncounterType().getEncounterTypeId() == pathologyEncounterType){
				pathologyEncounters.add(enc);
			}

		}
		model.put("pathologyEncounters",pathologyEncounters);

		if (log.isDebugEnabled())
			log.debug("In PortletControllerUtil....");
		
		if (model.containsKey("formToEditUrlMap")) {
			return;
		}
		
		Map<Form, String> viewUrlMap = new HashMap<Form, String>();
		Map<Form, String> editUrlMap = new HashMap<Form, String>();
		int pathologyEncounterTypeId = Integer.parseInt(Context.getAdministrationService().getGlobalProperty(
		    "imbpathologyrequest.pathologyEncounterType"));
		List<Extension> handlers = ModuleFactory.getExtensions("org.openmrs.module.web.extension.FormEntryHandler",
		    Extension.MEDIA_TYPE.html);
		if (handlers != null) {
			for (Extension ext : handlers) {
				FormEntryHandler handler = (FormEntryHandler) ext;
				{ // view
					Collection<Form> toView = handler.getFormsModuleCanView();
					if (toView != null) {
						if (handler.getViewFormUrl() == null)
							throw new IllegalArgumentException("form entry handler " + handler.getClass()
							        + " is trying to handle viewing forms but specifies no URL");
						for (Form form : toView) {
							if (form.getEncounterType().getEncounterTypeId() == pathologyEncounterTypeId)
								viewUrlMap.put(form, handler.getViewFormUrl());
						}
					}
				}
				{ // edit
					Collection<Form> toEdit = handler.getFormsModuleCanEdit();
					if (toEdit != null) {
						if (handler.getEditFormUrl() == null)
							throw new IllegalArgumentException("form entry handler " + handler.getClass()
							        + " is trying to handle editing forms but specifies no URL");
						for (Form form : toEdit) {
							if (form.getEncounterType().getEncounterTypeId() == pathologyEncounterTypeId)
								editUrlMap.put(form, handler.getEditFormUrl());
						}
					}
				}
			}
		}
		model.put("formToViewUrlMap", viewUrlMap);
		model.put("formToEditUrlMap", editUrlMap);
		
	}
}
