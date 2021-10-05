package org.openmrs.module.imbpathologyrequest.extension.html;

import org.openmrs.module.web.extension.PatientDashboardTabExt;

public class IMBPathologyRequestEncountersDashboard extends PatientDashboardTabExt {
	
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.openmrs.module.web.extension.PatientDashboardTabExt#getPortletUrl()
	 */
	@Override
	public String getPortletUrl() {
		return "imbPathologyRequestPatientEncounters";
	}
	
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.openmrs.module.web.extension.PatientDashboardTabExt#getRequiredPrivilege
	 * ()
	 */
	@Override
	public String getRequiredPrivilege() {
		return "View Pathology Encounters";
	}
	
	/*
	 * (non-Javadoc)
	 *
	 * @see org.openmrs.module.web.extension.PatientDashboardTabExt#getTabId()
	 */
	@Override
	public String getTabId() {
		return "imbPathologyRequestPatientEncountersID";
	}
	
	/*
	 * (non-Javadoc)
	 *
	 * @see org.openmrs.module.web.extension.PatientDashboardTabExt#getTabName()
	 */
	@Override
	public String getTabName() {
		return "Pathology Encounters";
	}
}
