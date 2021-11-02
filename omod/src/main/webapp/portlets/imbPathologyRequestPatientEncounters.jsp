<%@ include file="/WEB-INF/template/include.jsp" %>
<openmrs:htmlInclude file="/scripts/easyAjax.js" />

<openmrs:htmlInclude file="/scripts/jquery/dataTables/css/dataTables.css" />
<openmrs:htmlInclude file="/scripts/jquery/dataTables/js/jquery.dataTables.min.js" />

<openmrs:htmlInclude file="/scripts/jquery-ui/js/jquery-ui-1.7.2.custom.min.js" />
<link href="<openmrs:contextPath/>/scripts/jquery-ui/css/<spring:theme code='jqueryui.theme.name' />/jquery-ui.custom.css" type="text/css" rel="stylesheet" />

<openmrs:globalProperty key="dashboard.encounters.showViewLink" var="showViewLink" defaultValue="true"/>
<openmrs:globalProperty key="dashboard.encounters.showEditLink" var="showEditLink" defaultValue="true"/>

<div id="displayEncounterPopup">
	<div id="displayEncounterPopupLoading"><openmrs:message code="general.loading"/></div>
	<iframe id="displayEncounterPopupIframe" width="100%" height="100%" marginWidth="0" marginHeight="0" frameBorder="0" scrolling="auto"></iframe>
</div>

<script type="text/javascript">
	$j(document).ready(function() {
		$j('#displayEncounterPopup').dialog({
				title: 'dynamic',
				autoOpen: false,
				draggable: false,
				resizable: false,
				width: '95%',
				modal: true,
				open: function(a, b) { $j('#displayEncounterPopupLoading').show(); }
		});
	});

	function loadUrlIntoEncounterPopup(title, urlToLoad) {
		$j("#displayEncounterPopupIframe").attr("src", urlToLoad);
		$j('#displayEncounterPopup')
			.dialog('option', 'title', title)
			.dialog('option', 'height', $j(window).height() - 50) 
			.dialog('open');
	}
</script>

<c:if test="${model.showPagination == 'true'}">
<script type="text/javascript">
	$j(document).ready(function() {
		$j('#portlet${model.portletUUID} #patientEncountersTable').dataTable({
			"sPaginationType": "two_button",
			"bAutoWidth": false,
			"bFilter": false,
			"aaSorting": [[3,'asc']], // initial sorting uses the samer order given by ForEachEncounter (Encounter.datetime by default)
			"iDisplayLength": 20,
			"aoColumns": [
				{ "bVisible": false, "sType": "numeric" },
				{ "bVisible": ${showViewLink}, "iDataSort": 0 }, // sort this column by using the first invisible column for encounterIds,
            	{ "iDataSort": 3 }, // sort the date in this column by using the next invisible column for time in milliseconds
            	{ "bVisible": false, "sType": "numeric" },
            	null,
            	null,
            	null,
            	null,
            	null,
            	null
        	],
			"oLanguage": {
					"sLengthMenu": 'Show <select><option value="20">20</option><option value="50">50</option><option value="100">100</option></select> entries',
					"sZeroRecords": '<openmrs:message code="Encounter.no.previous"/>'
			}
		} );
		$j("#displayEncounterPopupIframe").load(function() { $j('#displayEncounterPopupLoading').hide(); });
	} );
</script>
</c:if>

<%--
Parameters
	model.num == \d  limits the number of encounters shown to the value given
	model.showPagination == 'true' lists off the encounters in a paginated table
	model.hideHeader == 'true' hides the 'All Encounter' header above the table listing
	model.hideFormEntry == 'true' does not show the "Enter Forms" popup no matter what the gp has
	model.formEntryReturnUrl == what URL to return to when a form has been cancelled or successfully filled out
--%>

<div id="portlet${model.portletUUID}">
<div id="encounterPortlet">
<%--
<div id="formEntryDialog">
				<openmrs:portlet url="personFormEntry" personId="${patient.personId}" id="encounterTabFormEntryPopup" parameters="showLastThreeEncounters=false|returnUrl=${model.formEntryReturnUrl}"/>
</div>
--%>

	<openmrs:globalProperty var="enableFormEntryInEncounters" key="FormEntry.enableOnEncounterTab" defaultValue="false"/>

	<c:if test="${enableFormEntryInEncounters && !model.hideFormEntry}">
		<openmrs:hasPrivilege privilege="Form Entry">
			<div id="formEntryDialog">
				<openmrs:portlet url="personFormEntry" personId="${patient.personId}" id="encounterTabFormEntryPopup" parameters="showLastThreeEncounters=false|returnUrl=${model.formEntryReturnUrl}"/>
			</div>

			<button class="showFormEntryDialog" style="margin-left: 2em; margin-bottom: 0.5em"><openmrs:message code="FormEntry.fillOutForm"/></button>
			
			<script type="text/javascript">
				$j(document).ready(function() {
					$j("#formEntryDialog").dialog({
						title: '<openmrs:message code="FormEntry.fillOutForm" javaScriptEscape="true"/>',
						autoOpen: false,
						draggable: false,
						resizable: false,
						width: '90%',
						modal: true
					});
					$j('button.showFormEntryDialog').click(function() {
						$j('#formEntryDialog').dialog('open');
					});
				});
			</script>

		</openmrs:hasPrivilege>
	</c:if>
<openmrs:globalProperty var="pathologyEncounterTypeId" key="imbpathologyrequest.pathologyEncounterType"  />
<openmrs:globalProperty var="pathologyFullAllowedLocationID" key="imbpathologyrequest.pathologyFullAllowedLocationID"  />
	<openmrs:hasPrivilege privilege="View Pathology Encounters">



<div id="pathologyencounters">
                        <table cellspacing="0" cellpadding="2" id="patientEncountersTable" width="100%">
                            <thead bgcolor="42978f">
							    <tr>
                                    <th class="hidden"> hidden Encounter id </th>
                                    <th class="encounterView" align="center" style="color:white;"><c:if test="${showViewLink == 'true'}">
                                        <openmrs:message code="general.view"/>
                                    </c:if></th>
                                    <th class="encounterDatetimeHeader" style="color:white;"> <openmrs:message code="Encounter.datetime"/> </th>
                                    <th class="hidden"> hidden Sorting Order (by Encounter.datetime) </th>
                                    <th class="encounterTypeHeader" style="color:white;"> <openmrs:message code="Encounter.type"/>     </th>
                                    <th class="encounterProviderHeader" style="color:white;"> <openmrs:message code="Encounter.provider"/> </th>
                                    <th class="encounterFormHeader" style="color:white;"> <openmrs:message code="Encounter.form"/>     </th>
                                    <th class="encounterLocationHeader" style="color:white;"> <openmrs:message code="Encounter.location"/> </th>
                                    <th class="encounterEntererHeader" style="color:white;"> <openmrs:message code="Encounter.enterer"/> </th>
							    </tr>
						    </thead>
						    <tbody>
                                <c:forEach var="enc" items="${ model.pathologyEncounters }">
                                <%-- <openmrs:forEachEncounter encounters="${model.pathologyEncounters}" sortBy="encounterDatetime" descending="true" var="enc" num="${model.num}"> --%>
                            		<c:if test="${enc.encounterType.encounterTypeId == pathologyEncounterTypeId && (pathologyFullAllowedLocationID == model.providerlocationAttribute || model.patientLocationAttribute == model.providerlocationAttribute)}">





                                        <tr class='${status.index % 2 == 0 ? "evenRow" : "oddRow"}'>
                                            <td class="hidden">
                                                <%--  this column contains the encounter id and will be used for sorting in the dataTable's encounter edit column --%>
                                                ${enc.encounterId}
                                            </td>
                                            <td class="encounterView" align="center">
                                                <c:if test="${showViewLink}">
                                                <a href="${ pageContext.request.contextPath }/module/htmlformentry/htmlFormEntry.form?encounterId=${ enc.encounterId }">
                                                                            <img src="${pageContext.request.contextPath}/images/file.gif" title="<openmrs:message code="general.view"/>" border="0" />
                                                </a>
                                                </c:if>
                                            </td>
                                            <td class="encounterDatetime">
                                                <openmrs:formatDate date="${enc.encounterDatetime}" type="small" />
                                            </td>
                                            <td class="hidden">
                                            <%--  this column contains the sorting order provided by ForEachEncounterTag (by encounterDatetime) --%>
                                            <%--  and will be used for the initial sorting and sorting in the dataTable's encounterDatetime column --%>
                                                ${count}
                                            </td>
                                            <td class="encounterType"><openmrs:format encounterType="${enc.encounterType}"/></td>
                                            <td class="encounterProvider"><openmrs:format encounterProviders="${enc.providersByRoles}"/></td>
                                            <td class="encounterForm">${enc.form.name}</td>
                                            <td class="encounterLocation"><openmrs:format location="${enc.location}"/></td>
                                            <td class="encounterEnterer">${enc.creator.personName}</td>
                                        </tr>





                                    </c:if>
                                 </c:forEach>
                                <%--</openmrs:forEachEncounter> --%>

                                <c:if test="${pathologyFullAllowedLocationID != model.providerlocationAttribute && model.patientLocationAttribute != model.providerlocationAttribute}">
                                    <tr><td colspan=10> <font color="red">Not allowed to see pathology data because of your location. Please contact Administrator if you really want to see this patient.</font> </td></tr>
                                </c:if>
                            </tbody>
                        </table>
</div>

<div id="formEntryDialog">
	<table cellspacing="0" cellpadding="2" id="patientEncountersTable" width="100%" border="1" style="border-collapse: collapse; border-color: 42978f;">

    	<!-- <center><u><spring:message code="htmlformentry.patientDashboard.enterForm"/></u></center> -->
    	<thead bgcolor="42978f">
                <tr>
                    <th  style="color:white;"> Enter Form </th>
                </tr>
        </thead>
        <tbody>
        	<c:forEach var="hf" items="${ model.pathologyhtmlForms }">
        	<tr valign="top">
    		<td>
                <a href="${ pageContext.request.contextPath }/module/htmlformentry/htmlFormEntry.form?personId=${ model.personId }&patientId=${ model.personId }&returnUrl=&formId=${hf.form.formId}">
                    ${ hf.name }
                </a>
    		</td>
    		</tr>
    		</c:forEach>
    	</tbody>
    </tr>
    </table>
</div>

	</openmrs:hasPrivilege>
	
	<openmrs:htmlInclude file="/dwr/interface/DWRObsService.js" />
	<openmrs:htmlInclude file="/dwr/interface/DWRPatientService.js" />
	<openmrs:htmlInclude file="/dwr/engine.js" />
	<openmrs:htmlInclude file="/dwr/util.js" />
	<script type="text/javascript">
		<!-- // begin

		<%--
		var obsTableCellFunctions = [
			function(data) { return "" + data.encounter; },
			function(data) { return "" + data.conceptName; },
			function(data) { return "" + data.value; },
			function(data) { return "" + data.datetime; }
		];
		--%>


		function handleGetObservations(encounterId) { 
			<%--
			DWRObsService.getObservations(encounterId, handleRefreshObsData);
			document.getElementById("encounterId").value = encounterId;
			<c:choose>
				<c:when test="${viewEncounterWhere == 'newWindow'}">
					var formWindow = window.open('${pageContext.request.contextPath}/admin/encounters/encounterDisplay.list?encounterId=' + encounterId, '${enc.encounterId}', 'toolbar=no,width=800,height=600,resizable=yes,scrollbars=yes');
					formWindow.focus();
				</c:when>
				<c:when test="${viewEncounterWhere == 'oneNewWindow'}">
					var formWindow = window.open('${pageContext.request.contextPath}/admin/encounters/encounterDisplay.list?encounterId=' + encounterId, 'formWindow', 'toolbar=no,width=800,height=600,resizable=yes,scrollbars=yes');
					formWindow.focus();
				</c:when>
				<c:otherwise>
					window.location = '${pageContext.request.contextPath}/admin/encounters/encounterDisplay.list?encounterId=' + encounterId;
				</c:otherwise>
			</c:choose>
			--%>
			loadUrlIntoEncounterPopup('Test title', '${pageContext.request.contextPath}/admin/encounters/encounterDisplay.list?encounterId=' + encounterId);
		}

		<%--
		function handleRefreshObsData(data) {
  			handleRefreshTable('obsTable', data, obsTableCellFunctions);
		}
		--%>

		function handleRefreshTable(id, data, func) {
			dwr.util.removeAllRows(id);
			dwr.util.addRows(id, data, func, {
				cellCreator:function(options) {
				    var td = document.createElement("td");
				    return td;
				},
				escapeHtml:false
			});
		}

		function showHideDiv(divId) {
			var div = document.getElementById(divId);
			if ( div ) {
				if (div.style.display != "") { 
					div.style.display = "";
				} else { 
					div.style.display = "none";
				}				
			}
		}
		
		function handleAddObs(encounterField, conceptField, valueTextField, obsDateField) {
			var encounterId = dwr.util.getValue($(encounterField));
			var conceptId = dwr.util.getValue($(conceptField));
			var valueText = dwr.util.getValue($(valueTextField));
			var obsDate = dwr.util.getValue($(obsDateField));
			var patientId = ${model.patient.patientId};			
			//alert("Adding obs for encounter (" + encounterId + "): " + conceptId + " = " + valueText + " " + obsDate);  
			DWRObsService.createObs(patientId, encounterId, conceptId, valueText, obsDate);
			handleGetObservations(encounterId);
		}
			
	
		//refreshObsTable();

		// end -->
		
	</script>
</div>
</div>
