/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under
 * the terms of the CPAL v1.0 license, a copy of which has been included with this
 * distribution in the LICENSE.md file.
 */

package org.mule.modules.taleo;

import org.mule.api.annotations.ConnectionStrategy;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Default;
import org.mule.modules.taleo.api.EntityTypeEnum;
import org.mule.modules.taleo.client.TaleoException;
import org.mule.modules.taleo.model.*;
import org.mule.modules.taleo.strategy.TaleoConnectorConnectionManagement;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Cloud Connector
 *
 * @author MuleSoft, Inc.
 */
@Connector(name = "taleo", schemaVersion = "1.0", friendlyName = "Taleo", minMuleVersion = "3.4")
public class TaleoConnector {


    @ConnectionStrategy
    private TaleoConnectorConnectionManagement connectionManagement;

    public TaleoConnectorConnectionManagement getConnectionManagement() {
        return connectionManagement;
    }

    public void setConnectionManagement(TaleoConnectorConnectionManagement connectionManagement) {
        this.connectionManagement = connectionManagement;
    }

    /**
     * Upsert an existing employee record (create new and/or update existing).
     * Upsert is the universal create and update command for Taleo Business Edition API. When using upsertEmployee as create command, use all the field except for employeeID, When using upsertEmployee as update command, use all fields or fields will be defined NULL (same as updateEmployee).
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:upsert-employee }
     *
     * @param employeeNumber Defined Employee Number
     * @param employee       Employee required fields: Status, lastName, ewsLogin, ewsPassword. Specify an ID to update an existing one.
     * @return Employee Id
     * @throws TaleoException Exception
     */
    @Processor
    public long upsertEmployee(
            String employeeNumber,
            @Default("#[payload]") EmployeeBean employee)
            throws TaleoException {
        return connectionManagement.getClient().upsertEmployee(employeeNumber, employee);
    }

    /**
     * Append a resume attachment to a candidate ID.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:set-binary-resume }
     *
     * @param candidateId      ID of candidate
     * @param fileName         Filename of binary file with defined file extension. The following MIME types are acceptable:
     *                         <ul>
     *                         <li>Text = text/plain</li>
     *                         <li>html = text/html</li>
     *                         <li>RTF = text/richtext</li>
     *                         <li>Word = application/msword</li>
     *                         <li>DocX = application/vnd.openxmlformats- officedocument.wordprocessingml.document</li>
     *                         <li>PDF = application/pdf</li>
     *                         </ul>
     * @param binaryAttachment Attachment
     * @throws TaleoException Exception
     */
    @Processor
    public void setBinaryResume(
            long candidateId,
            String fileName,
            ByteArr binaryAttachment)
            throws TaleoException {
        connectionManagement.getClient().setBinaryResume(candidateId, fileName, binaryAttachment);
    }

    /**
     * Delete an offer instance
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:delete-offer }
     *
     * @param offerId ID of Offer
     * @throws TaleoException Exception
     */
    @Processor
    public void deleteOffer(
            long offerId)
            throws TaleoException {
        connectionManagement.getClient().deleteOffer(offerId);
    }

    /**
     * Query all requisitions based on defined key (fieldname) and value (data looking for).
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:search-requisition }
     *
     * @param searchParams Map of search parameters always contains:key: name of field to search as a string. value: search parameter as string. For more details check <a href="http://tbe.taleo.net/products/TBE_API_Guide.pdf">Taleo Business Guide</a>
     * @return The results that match the criteria
     * @throws TaleoException Exception
     */
    @Processor
    public SearchResultArr searchRequisition(
            @Default("#[payload]") java.util.Map<String, Object> searchParams)
            throws TaleoException {
        Map searchMapParams = toTaleoMap(searchParams);
        return connectionManagement.getClient().searchRequisition(searchMapParams);
    }

    /**
     * Delete a contact log
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:delete-contact-log }
     *
     * @param contactLogId ID of contact log
     * @throws TaleoException Exception
     */
    @Processor
    public void deleteContactLog(
            long contactLogId)
            throws TaleoException {
        connectionManagement.getClient().deleteContactLog(contactLogId);
    }

    /**
     * Get event details by event ID
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-event-by-id }
     *
     * @param eventId ID of Event instance
     * @return Calendar Event
     * @throws TaleoException Exception
     */
    @Processor
    public CalendarEventBean getEventById(
            long eventId)
            throws TaleoException {
        return connectionManagement.getClient().getEventById(eventId);
    }

    /**
     * Create a contact record that somebody sent an email to the user (INBOUND EMAIL).
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-email-log }
     *
     * @param email   Email address of candidate, contact, or user (must already exist)
     * @param subject Subject of email message
     * @param body    Body of email message
     * @param date    Calendar of email message
     * @return Provided Contact Log ID
     * @throws TaleoException Exception
     */
    @Processor
    public long createEmailLog(
            String email,
            String subject,
            String body,
            GregorianCalendar date)
            throws TaleoException {
        return connectionManagement.getClient().createEmailLog(email, subject, body, date);
    }

    /**
     * Get the all the application modules the company code has turned on.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-enabled-services }
     *
     * @return String of system modules configured where:
     * <ul><li>'perform' = Taleo Business Edition Perform</li>
     * <li>'onBoarding' = Taleo Business Edition onBoarding</li>
     * <li>'recruit' = Taleo Business Edition Recruit</li>
     * <li>'backup' = Taleo Business Edition on-Demand BackUp Service</li>
     * <li>'offcp' = Taleo Business Edition OFFCP Compliance Package</li>
     * <li>'tsc' = Taleo Schedule Center for Recruit</li>
     * <li>'tvr'= Taleo Voice Response for Recruit</li>
     * <li>'generic'= Taleo Business Edition Generic Package for Recruit</li>
     * <li>'staffing' = Taleo Business Edition Staffing Package for Recruit</li>
     * <li>'pureDiscovery' = Pure Discovery Advanced Sourcing for Recruit</li></ul>
     * @throws TaleoException Exception
     */
    @Processor
    public String getEnabledServices()
            throws TaleoException {
        return connectionManagement.getClient().getEnabledServices();
    }

    /**
     * Update an existing candidate. Update requests replace all data fields in the candidateBean (i.e. data is not appended or upserted)
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:update-candidate }
     *
     * @param candidate Candidate
     * @throws TaleoException Exception
     */
    @Processor
    public void updateCandidate(
            @Default("#[payload]") CandidateBean candidate)
            throws TaleoException {
        connectionManagement.getClient().updateCandidate(candidate);
    }

    /**
     * Create a requisition
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-requisition }
     *
     * @param requisition Requisition required fields: Status, title, location.
     * @return Provided Requisition ID
     * @throws TaleoException Exception
     */
    @Processor
    public long createRequisition(
            @Default("#[payload]") RequisitionBean requisition)
            throws TaleoException {
        return connectionManagement.getClient().createRequisition(requisition);
    }

    /**
     * Lookup picklist values for a given field. Metadata and picklist fields are any field (standard or custom) that associate drop-down list of values for data consistency.
     * getLookup ‘fieldName’ works for standard system fields solely. Where custom fields require lookup, getLookup call requires actual database field value.
     * True database field value can be found by ‘View Source’ of the GUI page displaying the appropriate field.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-lookup }
     *
     * @param fieldName Field name
     * @return Array of fieldname values (display name and integration code name).
     * @throws TaleoException Exception
     */
    @Processor
    public LookupArr getLookup(
            String fieldName)
            throws TaleoException {
        return connectionManagement.getClient().getLookup(fieldName);
    }

    /**
     * Delete a candidate
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:delete-candidate }
     *
     * @param candidateId ID of Candidate
     * @throws TaleoException Exception
     */
    @Processor
    public void deleteCandidate(
            long candidateId)
            throws TaleoException {
        connectionManagement.getClient().deleteCandidate(candidateId);
    }

    /**
     * Get a Department details
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-department-by-name }
     *
     * @param departmentName Name of the department
     * @return Department
     * @throws TaleoException Exception
     */
    @Processor
    public DepartmentBean getDepartmentByName(
            String departmentName)
            throws TaleoException {
        return connectionManagement.getClient().getDepartmentByName(departmentName);
    }

    /**
     * Update an existing contact. Update requests replace all data fields in the contactBean (i.e. data is not appended or upserted).
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:update-contact }
     *
     * @param contact Contact required fields: accountId, lastName.
     * @throws TaleoException Exception
     */
    @Processor
    public void updateContact(
            @Default("#[payload]") ContactBean contact)
            throws TaleoException {
        connectionManagement.getClient().updateContact(contact);
    }


    /**
     * Get a Users system history within Taleo Business Edition.
     * This object is used to fetch a history log entry which reflects a past event or action related to Users. History records can only be created by the server, they cannot be modified or deleted. History log includes server side ID, test describing history record, complete text of email message (for email events) and creation date.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-user-history }
     *
     * @param userId ID of User
     * @return History Bean
     * @throws TaleoException Exception
     */
    @Processor
    public HistoryBeanArr getUserHistory(
            long userId)
            throws TaleoException {
        return connectionManagement.getClient().getUserHistory(userId);
    }


    /**
     * Get an account history.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-account-history }
     *
     * @param accountId ID of Account
     * @return This object is used to fetch a history log entry which reflects a past event or action related to accounts.
     * @throws TaleoException Exception
     */
    @Processor
    public HistoryBeanArr getAccountHistory(
            long accountId)
            throws TaleoException {
        return connectionManagement.getClient().getAccountHistory(accountId);
    }

    /**
     * Update or create a Region instance. Upsert is used to both create or update data within a specific Region. Using the Region ID AND Region Name attribute updates or replaces existing Region details
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:upsert-region }
     *
     * @param region Region required fields: RegionID, RegionName
     * @return Region ID
     * @throws TaleoException Exception
     */
    @Processor
    public long upsertRegion(
            @Default("#[payload]") RegionBean region)
            throws TaleoException {
        return connectionManagement.getClient().upsertRegion(region);
    }


    /**
     * Get offer details by offer ID.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-offer-by-id }
     *
     * @param offerId ID of offer instance
     * @return Offer
     * @throws TaleoException Exception
     */
    @Processor
    public OfferBean getOfferById(
            long offerId)
            throws TaleoException {
        return connectionManagement.getClient().getOfferByID(offerId);
    }


    /**
     * Get a contact log details by contact log ID
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-contact-log-by-id }
     *
     * @param contactLogId ID of contact log
     * @return Contact Log information
     * @throws TaleoException Exception
     */
    @Processor
    public ContactLogBean getContactLogById(
            long contactLogId)
            throws TaleoException {
        return connectionManagement.getClient().getContactLogById(contactLogId);
    }

    /**
     * Get interview details by interview ID
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-interview-by-id }
     *
     * @param interviewId ID of interview instance
     * @return Interview
     * @throws TaleoException Exception
     */
    @Processor
    public InterviewBean getInterviewById(
            long interviewId)
            throws TaleoException {
        return connectionManagement.getClient().getInterviewById(interviewId);
    }


    /**
     * Get the system props of a company code using valid session variable.
     * System-level properties return as string-string map:
     * <ul><li>HiredStatuses : Comma-separated ids of statuses for hired candidates</li>
     * <li>OpenedStatuses : ￼Comma-separated ids of statuses for opened requisitionss</li>
     * <li>￼MyViewStatuses : Comma-separated ids of statuses for candidates shown on “My View” page</li>
     * <li>PipelineStatuses : ￼Comma-separated ids of statuses for candidates shown on “Pipeline” page</li></ul>
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-system-props }
     *
     * @return Array of System Property Values.
     * @throws TaleoException Exception
     */
    @Processor
    public java.util.Map<String, Object> getSystemProps()
            throws TaleoException {
        return fromTaleoMap(connectionManagement.getClient().getSystemProps());
    }


    private java.util.Map<String, Object> fromTaleoMap(Map systemProps) {
        java.util.Map<String, Object> map = new java.util.HashMap<String, Object>();
        for (MapItem item : systemProps.getItem()) {
            map.put((String) item.getKey(), item.getPropertyValue());
        }
        return map;
    }

    /**
     * Retrieve all field values within an entity.
     * Returns all fields within the entity including:
     * <ul><li>displayName (application field name)</li>
     * <li>integrationName (API / external field name)</li>
     * <li>isCustom (whether the field is a standard or custom field in TBE)</li>
     * <li>isReadOnly (whether the field is writable or read-only)</li>
     * <li>isRequired (whether the field is application-required for data entry)</li>
     * <li>type (the type of field defined)</li></ul>
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-metadata }
     *
     * @param entityType ￼￼￼Type of related entity:
     *                   <ul><li>ACCT = accounts</li>
     *                   <li>CAND = candidates</li>
     *                   <li>CTCT = contacts</li>
     *                   <li>EMPL = employees</li>
     *                   <li>REQU = requisitions</li>
     *                   <li>WORK = users</li></ul>
     * @return Array of fieldname values (display name and integration code name) and other fields.
     * @throws TaleoException Exception
     */
    @Processor
    public MetadataArr getMetadata(
            EntityTypeEnum entityType)
            throws TaleoException {
        return connectionManagement.getClient().getMetadata(entityType);
    }

    /**
     * Get all public events within a start date and end date
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-public-events }
     *
     * @param startDate Start of start date interval.
     * @param endDate   End of start date interval.
     * @return Array of Event IDs
     * @throws TaleoException Exception
     */
    @Processor
    public LongArr getPublicEvents(
            GregorianCalendar startDate,
            GregorianCalendar endDate)
            throws TaleoException {
        return connectionManagement.getClient().getPublicEvents(startDate, endDate);
    }


    /**
     * Get the primary resume binary attachment associated with a candidate.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-binary-resume }
     *
     * @param candidateId ID of candidate
     * @return binary attachment in base64Binary
     * @throws TaleoException Exception
     */
    @Processor
    public ByteArr getBinaryResume(
            long candidateId)
            throws TaleoException {
        return connectionManagement.getClient().getBinaryResume(candidateId);
    }


    /**
     * Get all background checks for a specific Candidate ID
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-background-checks-by-candidate }
     *
     * @param candidateId ID of candidate
     * @return BackGround Check IDI(s)
     * @throws TaleoException Exception
     */
    @Processor
    public LongArr getBackgroundChecksByCandidate(
            long candidateId)
            throws TaleoException {
        return connectionManagement.getClient().getBackgroundChecksByCandidate(candidateId);
    }


    /**
     * Update or create a Location instance.
     * <p/>
     * Upsert is used to both create or update data within a specific Location. Using the Location ID AND Location Name attribute updates or replaces existing Location details.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:upsert-location }
     *
     * @param location Location
     * @return ID of Location
     * @throws TaleoException Exception
     */
    @Processor
    public long upsertLocation(
            @Default("#[payload]") LocationBean location)
            throws TaleoException {
        return connectionManagement.getClient().upsertLocation(location);
    }

    /**
     * Update an existing attachment. Note: Update requests replaces the attachment file (i.e. data is not appended or upserted)
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:update-attachment }
     *
     * @param attachmentId          Attachment ID
     * @param attachmentDescription Attachment Description
     * @param attachmentName        Attachment Name
     * @param contentType           Content Type
     * @param binaryResume          Binary resume
     * @throws TaleoException Exception
     */
    @Processor
    public void updateAttachment(
            long attachmentId,
            String attachmentDescription,
            String attachmentName,
            String contentType,
            @Default("#[payload]") ByteArr binaryResume)
            throws TaleoException {
        connectionManagement.getClient().updateAttachment(attachmentId, attachmentDescription, attachmentName, contentType, binaryResume);
    }


    /**
     * Create a User. Users are all the people with access to your Taleo Business Edition system including hiring managers, recruiters, interviewers, staffing agencies, etc.
     * For more details check <a href="http://tbe.taleo.net/products/TBE_API_Guide.pdf">Taleo Business Guide</a>
     * User’s role:
     * <ul><li>'A' = Administrator</li>
     * <li>'M' = Hiring Manager</li>
     * <li>'R' = Recruiter</li>
     * <li>'E’ = Employee</li>
     * <li>'G' = Agency</li>
     * <li>‘-‘ = no access</li></ul>
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-user }
     *
     * @param user User required fields: status, email, lastName, loginName, role
     * @return Provided User ID
     * @throws TaleoException Exception
     */
    @Processor
    public long createUser(
            @Default("#[payload]") UserBean user)
            throws TaleoException {
        return connectionManagement.getClient().createUser(user);
    }


    /**
     * Login a Partner.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:login-partner }
     *
     * @param orgCode           Organization code
     * @param partnerCode       Partner Code
     * @param currentTimeMillis time
     * @param digest            Digest
     * @return Session Id of partner
     * @throws TaleoException Exception
     */
    @Processor
    public String loginPartner(
            String orgCode,
            String partnerCode,
            long currentTimeMillis,
            String digest)
            throws TaleoException {
        return connectionManagement.getClient().loginPartner(orgCode, partnerCode, currentTimeMillis, digest);
    }


    /**
     * Create an offer for a specific candidate.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-offer }
     *
     * @param offer Offer Required fields: Status, candidateId, employmentType, manager, requisitionId, title
     * @return Provided Offer ID
     * @throws TaleoException Exception
     */
    @Processor
    public long createOffer(
            @Default("#[payload]") OfferBean offer)
            throws TaleoException {
        return connectionManagement.getClient().createOffer(offer);
    }

    /**
     * Get candidate detailed information by ID.By default all flags are true
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-candidate-details-by-id }
     *
     * @param candidateId         ID of candidate
     * @param includeRequisitions True/False to include or not this information
     * @param includeInterviews   True/False to include or not this information
     * @param includeReferences   True/False to include or not this information
     * @param includeHistory      True/False to include or not this information
     * @param includeAttachments  True/False to include or not this information
     * @param includeOffers       True/False to include or not this information
     * @return CandidateDetails
     * @throws TaleoException Exception
     */
    @Processor
    public CandidateDetailsBean getCandidateDetailsById(
            long candidateId,
            @Default("true") boolean includeRequisitions,
            @Default("true") boolean includeInterviews,
            @Default("true") boolean includeReferences,
            @Default("true") boolean includeHistory,
            @Default("true") boolean includeAttachments,
            @Default("true") boolean includeOffers)
            throws TaleoException {
        return connectionManagement.getClient().getCandidateDetailsById(candidateId, includeRequisitions,
                includeInterviews, includeReferences,
                includeHistory, includeAttachments, includeOffers);
    }

    /**
     * Update an existing contact log. Update requests replace all data fields in the contactLogBean (i.e. data is not appended or upserted).
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:update-contact-log }
     *
     * @param contactLog ContactoLog required fields: entityId,EntityType, id, subject, typeNo.
     * @throws TaleoException Exception
     */
    @Processor
    public void updateContactLog(
            @Default("#[payload]") ContactLogBean contactLog)
            throws TaleoException {
        connectionManagement.getClient().updateContactLog(contactLog);
    }

    /**
     * Create a contact record that the user sent somebody an email (OUTBOUND EMAIL). Tries to match outgoing email address against candidate, then contact, then user.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-email-sent-log }
     *
     * @param email   Email address of candidate, contact, or user (must already exist)
     * @param subject Subject of email message
     * @param body    Body of email message
     * @param date    Calendar of email message
     * @return ￼￼￼Provided Contact Log ID
     * @throws TaleoException Exception
     */
    @Processor
    public long createEmailSentLog(
            String email,
            String subject,
            String body,
            GregorianCalendar date)
            throws TaleoException {
        return connectionManagement.getClient().createEmailSentLog(email, subject, body, date);
    }


    /**
     * Create a contact log
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-contact-log }
     *
     * @param contactLog ContactLog required fields: creator, typeNo, entityType, entityId, subject
     * @return Provided Contact Log ID
     * @throws TaleoException Exception
     */
    @Processor
    public long createContactLog(
            @Default("#[payload]") ContactLogBean contactLog)
            throws TaleoException {
        return connectionManagement.getClient().createContactLog(contactLog);
    }


    /**
     * Get attachment data by ID
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-attachment-data }
     *
     * @param attachmentId ID of attachment
     * @return Attachment data
     * @throws TaleoException Exception
     */
    @Processor
    public ByteArr getAttachmentData(
            long attachmentId)
            throws TaleoException {
        return connectionManagement.getClient().getAttachmentData(attachmentId);
    }


    /**
     * Get all Regions. This functions similar to getLookUp Request.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-regions }
     *
     * @return Array of Regions.
     * @throws TaleoException Exception
     */
    @Processor
    public RegionArr getRegions()
            throws TaleoException {
        return connectionManagement.getClient().getRegions();
    }


    /**
     * Create a candidate
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-candidate }
     *
     * @param candidate Candidate required fields Status, email, lastname
     * @return Provided Candidate ID
     * @throws TaleoException Exception
     */
    @Processor
    public long createCandidate(
            @Default("#[payload]") CandidateBean candidate)
            throws TaleoException {
        return connectionManagement.getClient().createCandidate(candidate);
    }


    /**
     * Get a Location details
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-location-by-id }
     *
     * @param locationId ID of location
     * @return Location
     * @throws TaleoException Exception
     */
    @Processor
    public LocationBean getLocationById(
            long locationId)
            throws TaleoException {
        return connectionManagement.getClient().getLocationById(locationId);
    }

    /**
     * Create a department
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-department }
     *
     * @param department Required field: Name
     * @return Provided Department ID
     * @throws TaleoException Exception
     */
    @Processor
    public long createDepartment(
            @Default("#[payload]") DepartmentBean department)
            throws TaleoException {
        return connectionManagement.getClient().createDepartment(department);
    }


    /**
     * Query all contacts based on defined key (field name) and value (data looking for)
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:search-contact }
     *
     * @param searchParams Map of search parameters always contains:key: name of field to search as a string. value: search parameter as string. For more details check <a href="http://tbe.taleo.net/products/TBE_API_Guide.pdf">Taleo Business Guide</a>
     * @return The results that match the criteria
     * @throws TaleoException Exception
     */
    @Processor
    public SearchResultArr searchContact(
            @Default("#[payload]") java.util.Map<String, Object> searchParams)
            throws TaleoException {
        Map searchMapParams = toTaleoMap(searchParams);
        return connectionManagement.getClient().searchContact(searchMapParams);
    }

    /**
     * Update an existing reference instance.
     * Update requests replace all data fields in the referenceBean (i.e. data is not appended or upserted).
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:update-reference }
     *
     * @param reference Reference required fields: id, status, candidateId, refName.
     * @throws TaleoException Exception
     */
    @Processor
    public void updateReference(
            @Default("#[payload]") ReferenceBean reference)
            throws TaleoException {
        connectionManagement.getClient().updateReference(reference);
    }


    /**
     * Get all Locations.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-locations }
     *
     * @return Array of Location
     * @throws TaleoException Exception
     */
    @Processor
    public LocationArr getLocations()
            throws TaleoException {
        return connectionManagement.getClient().getLocations();
    }


    /**
     * Get reference(s) associated with any Taleo Business Edition candidate
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-references-by-candidate }
     *
     * @param candidateId ID of candidate
     * @return Array of reference IDs
     * @throws TaleoException Exception
     */
    @Processor
    public LongArr getReferencesByCandidate(
            long candidateId)
            throws TaleoException {
        return connectionManagement.getClient().getReferencesByCandidate(candidateId);
    }


    /**
     * Get an User details
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-user-by-id }
     *
     * @param userId User ID
     * @return User details
     * @throws TaleoException Exception
     */
    @Processor
    public UserBean getUserById(
            long userId)
            throws TaleoException {
        return connectionManagement.getClient().getUserById(userId);
    }


    /**
     * Get task(s) associated with any Taleo Business Edition entity that supports storing/creation of task events
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-task-by-entity }
     *
     * @param entityType Type of related entity: ACCT = accounts CAND = candidates EMPL = employees REQU = requisitions WORK = users
     * @param entityId   ID of associated entity
     * @return Array of task IDs
     * @throws TaleoException Exception
     */
    @Processor
    public LongArr getTaskByEntity(
            String entityType,
            long entityId)
            throws TaleoException {
        return connectionManagement.getClient().getTaskByEntity(entityType, entityId);
    }

    /**
     * Remove an existing candidate assignment from a requisition (both based on ID variables)
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:remove-candidate }
     *
     * @param candidateId   ID of candidate
     * @param requisitionId ID of requisition
     * @throws TaleoException Exception
     */
    @Processor
    public void removeCandidate(
            long candidateId,
            long requisitionId)
            throws TaleoException {
        connectionManagement.getClient().removeCandidate(candidateId, requisitionId);
    }


    /**
     * Get a Department details
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-department-by-id }
     *
     * @param departmentId ID of department
     * @return Department
     * @throws TaleoException Exception
     */
    @Processor
    public DepartmentBean getDepartmentById(
            long departmentId)
            throws TaleoException {
        return connectionManagement.getClient().getDepartmentById(departmentId);
    }


    /**
     * Create an employee
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-employee }
     *
     * @param employee Employee with status, employeeNumber, lastName, ewsLogin, ewsPassword
     * @return Provided Employee ID
     * @throws TaleoException Exception
     */
    @Processor
    public long createEmployee(
            @Default("#[payload]") EmployeeBean employee)
            throws TaleoException {
        return connectionManagement.getClient().createEmployee(employee);
    }


    /**
     * Get task details by task ID
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-task-by-id }
     *
     * @param taskId ID of Task instance
     * @return Task
     * @throws TaleoException Exception
     */
    @Processor
    public TaskBean getTaskById(
            long taskId)
            throws TaleoException {
        return connectionManagement.getClient().getTaskById(taskId);
    }


    /**
     * Get contact log(s) associated with any Taleo Business Edition entity that supports storing of contact logs
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-contact-logs-by-entity }
     *
     * @param entityType Type of related entity: ACCT = accounts CAND = candidates EMPL = employees REQU = requisitions WORK = users
     * @param entityId   ID of associated entity
     * @return Array of contact log IDs
     * @throws TaleoException Exception
     */
    @Processor
    public LongArr getContactLogsByEntity(
            String entityType,
            long entityId)
            throws TaleoException {
        return connectionManagement.getClient().getContactLogsByEntity(entityType, entityId);
    }

    /**
     * Delete a reference instance.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:delete-reference }
     *
     * @param referenceId ID of Reference
     * @throws TaleoException Exception
     */
    @Processor
    public void deleteReference(
            long referenceId)
            throws TaleoException {
        connectionManagement.getClient().deleteReference(referenceId);
    }


    /**
     * Get reference details by reference ID.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-reference-by-id }
     *
     * @param referenceId ID of Reference Instance
     * @return Reference
     * @throws TaleoException Exception
     */
    @Processor
    public ReferenceBean getReferenceById(
            long referenceId)
            throws TaleoException {
        return connectionManagement.getClient().getReferenceById(referenceId);
    }


    /**
     * Create a Region
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-region }
     *
     * @param region Required fields: name.
     * @return Provided Region ID
     * @throws TaleoException Exception
     */
    @Processor
    public long createRegion(
            @Default("#[payload]") RegionBean region)
            throws TaleoException {
        return connectionManagement.getClient().createRegion(region);
    }

    /**
     * Delete a contact
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:delete-contact }
     *
     * @param contactId ID of contact
     * @throws TaleoException Exception
     */
    @Processor
    public void deleteContact(
            long contactId)
            throws TaleoException {
        connectionManagement.getClient().deleteContact(contactId);
    }


    /**
     * Get all candidates by Requisition ID
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-candidates-by-requisition }
     *
     * @param requisitionId Requisition ID
     * @return Candidate ID(s)
     * @throws TaleoException Exception
     */
    @Processor
    public LongArr getCandidatesByRequisition(
            long requisitionId)
            throws TaleoException {
        return connectionManagement.getClient().getCandidatesByRequisition(requisitionId);
    }


    /**
     * Get a specific background check by ID
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-background-check-by-id }
     *
     * @param backgroundCheckId ID of Background Check
     * @return BackgroundCheckBean
     * @throws TaleoException Exception
     */
    @Processor
    public BackgroundCheckBean getBackgroundCheckById(
            long backgroundCheckId)
            throws TaleoException {
        return connectionManagement.getClient().getBackgroundCheckById(backgroundCheckId);
    }

    /**
     * Remove an association of a user to a requisition.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:remove-associated-user }
     *
     * @param requisitionId Requisition ID
     * @param association   Picklist of Association Values:
     *                      <ul><li>‘O’ = Owner</li>
     *                      <li>‘A’ = approvers (in order)</li>
     *                      <li>‘F’ = offer approvers</li></ul>
     * @param userId        ID of User
     * @throws TaleoException Exception
     */
    @Processor
    public void removeAssociatedUser(
            long requisitionId,
            String association,
            long userId)
            throws TaleoException {
        connectionManagement.getClient().removeAssociatedUser(requisitionId, association, userId);
    }


    /**
     * Get requisition details by requisition ID
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-requisition-by-id }
     *
     * @param requisitionId ID of requisition
     * @return Requisition
     * @throws TaleoException Exception
     */
    @Processor
    public RequisitionBean getRequisitionById(
            long requisitionId)
            throws TaleoException {
        return connectionManagement.getClient().getRequisitionById(requisitionId);
    }

    /**
     * Update an existing requisition instance. Update requests replace all data fields in the requisitionBean (i.e. data is not appended or upserted)
     * Update requests replace all data fields in the requisitionBean (i.e. data is not appended or upserted). Send all elements back or the field values will be returned NULL.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:update-requisition }
     *
     * @param requisition Requisition required fields: id, status, location, title
     * @throws TaleoException Exception
     */
    @Processor
    public void updateRequisition(
            @Default("#[payload]") RequisitionBean requisition)
            throws TaleoException {
        connectionManagement.getClient().updateRequisition(requisition);
    }


    /**
     * Get offer(s) associated with any Taleo Business Edition candidate.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-offers }
     *
     * @param candidateId ID of candidate
     * @return Array of offer IDs
     * @throws TaleoException Exception
     */
    @Processor
    public LongArr getOffers(
            long candidateId)
            throws TaleoException {
        return connectionManagement.getClient().getOffers(candidateId);
    }


    /**
     * Delete an Account.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:delete-account }
     *
     * @param accountId ￼￼ID of Account
     * @throws TaleoException Exception
     */
    @Processor
    public void deleteAccount(
            long accountId)
            throws TaleoException {
        connectionManagement.getClient().deleteAccount(accountId);
    }

    /**
     * Append an offer letter attachment by offer ID
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:set-binary-offer }
     *
     * @param offerId  ID of Offer instance
     * @param fileName Filename of binary file with defined file extension.
     *                 <ul><li>The following MIME types are acceptable:</li>
     *                 <li>Text = text/plain</li>
     *                 <li>html = text/html</li>
     *                 <li>RTF = text/richtext</li>
     *                 <li>Word = application/msword</li>
     *                 <li>DocX = application/vnd.openxmlformats- officedocument.wordprocessingml.document</li>
     *                 <li>PDF = application/pdf</li></ul>
     * @param content  base64Binary
     * @throws TaleoException Exception
     */
    @Processor
    public void setBinaryOffer(
            long offerId,
            String fileName,
            @Default("#[payload]") ByteArr content)
            throws TaleoException {
        connectionManagement.getClient().setBinaryOffer(offerId, fileName, content);
    }


    /**
     * Create a Contact
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-contact }
     *
     * @param contact Required field: lastName
     * @return Provided Contact ID
     * @throws TaleoException Exception
     */
    @Processor
    public long createContact(
            @Default("#[payload]") ContactBean contact)
            throws TaleoException {
        return connectionManagement.getClient().createContact(contact);
    }


    /**
     * Get the rolling entity specific to a candidate’s work history form details
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-candidate-work-history }
     *
     * @param candidateId ID of candidate
     * @return Work History Information
     * @throws TaleoException Exception
     */
    @Processor
    public WorkHistoryArr getCandidateWorkHistory(
            long candidateId)
            throws TaleoException {
        return connectionManagement.getClient().getCandidateWorkHistory(candidateId);
    }

    /**
     * Submit a candidate to a requisition(s)
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:submit-candidate }
     *
     * @param candidateId    ID of candidate
     * @param requisitionIds Array of requisition IDs
     * @throws TaleoException Exception
     */
    @Processor
    public void submitCandidate(
            long candidateId,
            LongArr requisitionIds)
            throws TaleoException {
        connectionManagement.getClient().submitCandidate(candidateId, requisitionIds);
    }

    /**
     * Create an attachment for a specific ID associated with a specific entity (i.e. Candidate, Employee, Account, etc.).
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-entity-attachment }
     *
     * @param entityType  Entity type
     * @param entityId    ID of associated entity
     * @param description Description of the file
     * @param fileName    Binary File Name
     * @param contentType Content Type
     * @param data        Attachment data
     * @return Provided Attachment ID
     * @throws TaleoException Exception
     */
    @Processor
    public long createEntityAttachment(
            String entityType,
            long entityId,
            String description,
            String fileName,
            String contentType,
            @Default("#[payload]") ByteArr data)
            throws TaleoException {
        return connectionManagement.getClient().createEntityAttachment(entityType, entityId, description, fileName, contentType, data);
    }


    /**
     * Delete an interview instance.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:delete-interview }
     *
     * @param interviewId ID of interview
     * @throws TaleoException Exception
     */
    @Processor
    public void deleteInterview(
            long interviewId)
            throws TaleoException {
        connectionManagement.getClient().deleteInterview(interviewId);
    }

    /**
     * Post a specific requisition to a supported posting location. For more details check <a href="http://tbe.taleo.net/products/TBE_API_Guide.pdf">Taleo Business Guide</a>
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:post-requisition }
     *
     * @param requisitionId ID of Requisition
     * @param posterId      ID of the posting location, for example a specific Taleo Business Edition Career Website (picklist value ID #)
     * @param formVersion   ￼ID of the required application form to be used within the posting location (picklist value ID #)
     * @throws TaleoException Exception
     */
    @Processor
    public void postRequisition(
            long requisitionId,
            long posterId,
            int formVersion)
            throws TaleoException {
        connectionManagement.getClient().postRequisition(requisitionId, posterId, formVersion);
    }


    /**
     * Get interview(s) associated with any Taleo Business Edition candidate
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-interviews-by-candidate }
     *
     * @param candidateId ID of candidate
     * @return Array of interview ID
     * @throws TaleoException Exception
     */
    @Processor
    public LongArr getInterviewsByCandidate(
            long candidateId)
            throws TaleoException {
        return connectionManagement.getClient().getInterviewsByCandidate(candidateId);
    }


    /**
     * Creates a new candidate with contact information parsed with the original resume appended to the candidate record, and sends back:
     * Candidate ID (either newly created or existing if duplicate)
     * Duplicate Value where 0 equals none existing and 1 equals duplicate exists
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:parse-resume-into-candidate }
     *
     * @param binaryAttachment base64Binary file
     * @param referredBy       Name of referral source
     * @param fileName         Name of the resume file including extension.
     *                         The following MIME types are acceptable:
     *                         <ul><li>Text = text/plain</li>
     *                         <li>html = text/html</li>
     *                         <li>RTF = text/richtext</li>
     *                         <li>Word = application/msword</li>
     *                         <li>DocX = application/vnd.openxmlformats- officedocument.wordprocessingml.document</li>
     *                         <li>PDF = application/pdf</li></ul>
     * @return candidateId or dup (duplicate value)
     * @throws TaleoException Exception
     */
    @Processor
    public CandidateInsertResultBean parseResumeIntoCandidate(
            @Default("#[payload]") ByteArr binaryAttachment,
            String referredBy,
            String fileName)
            throws TaleoException {
        return connectionManagement.getClient().parseResumeIntoCandidate(binaryAttachment, referredBy, fileName);
    }

    /**
     * Delete a requisition instance
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:delete-requisition }
     *
     * @param requisitionId ID of requisition.
     * @throws TaleoException Exception
     */
    @Processor
    public void deleteRequisition(
            long requisitionId)
            throws TaleoException {
        connectionManagement.getClient().deleteRequisition(requisitionId);
    }

    /**
     * Delete a department
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:delete-department }
     *
     * @param departmentId ID of department
     * @throws TaleoException Exception
     */
    @Processor
    public void deleteDepartment(
            long departmentId)
            throws TaleoException {
        connectionManagement.getClient().deleteDepartment(departmentId);
    }

    /**
     * Get employee general information by Number
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-employee-by-number }
     *
     * @param employeeNumber Defined Employee Number
     * @return Employee
     * @throws TaleoException Exception
     */
    @Processor
    public EmployeeBean getEmployeeByNumber(
            String employeeNumber)
            throws TaleoException {
        return connectionManagement.getClient().getEmployeeByNumber(employeeNumber);
    }

    /**
     * Update an existing calendar event. Update requests replace all data fields in the calendarEventBean (i.e. data is not appended or upserted).
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:update-event }
     *
     * @param event CalendarEvent required fields: Id, status, duration, entityId, entityType, startDate, eventId, eventType, personId, personType.
     * @throws TaleoException Exception
     */
    @Processor
    public void updateEvent(
            @Default("#[payload]") CalendarEventBean event)
            throws TaleoException {
        connectionManagement.getClient().updateEvent(event);
    }


    /**
     * Create a reference for a specific candidate
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-reference }
     *
     * @param reference Reference required fields: status, candidateId, refName
     * @return Provided Reference ID
     * @throws TaleoException Exception
     */
    @Processor
    public long createReference(
            @Default("#[payload]") ReferenceBean reference)
            throws TaleoException {
        return connectionManagement.getClient().createReference(reference);
    }


    /**
     * Get attachment(s) by candidate id.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-attachments }
     *
     * @param candidateId ID of Candidate
     * @return Attachment IDs
     * @throws TaleoException Exception
     */
    @Processor
    public LongArr getAttachments(
            long candidateId)
            throws TaleoException {
        return connectionManagement.getClient().getAttachments(candidateId);
    }

    /**
     * Delete a task instance
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:delete-task }
     *
     * @param taskId ID of Task
     * @throws TaleoException Exception
     */
    @Processor
    public void deleteTask(
            long taskId)
            throws TaleoException {
        connectionManagement.getClient().deleteTask(taskId);
    }


    /**
     * Update an existing task event. Update requests replace all data fields in the taskBean (i.e. data is not appended or upserted).
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:update-task }
     *
     * @param task Task required fields: id, status, creator, priority subject, eventiId, eventType, personId, personType, startDate
     * @throws TaleoException Exception
     */
    @Processor
    public void updateTask(
            @Default("#[payload]") TaskBean task)
            throws TaleoException {
        connectionManagement.getClient().updateTask(task);
    }

    /**
     * Get event(s) associated with any Taleo Business Edition entity that supports storing/creation of calendar events.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-event-by-entity }
     *
     * @param entityType Type of related entity: ACCT = accounts CAND = candidates EMPL = employees REQU = requisitions WORK = users
     * @param entityId   ID of associated entity
     * @return Array of event IDs
     * @throws TaleoException Exception
     */
    @Processor
    public LongArr getEventByEntity(
            String entityType,
            long entityId)
            throws TaleoException {
        return connectionManagement.getClient().getEventByEntity(entityType, entityId);
    }

    /**
     * Delete an employee by employee number
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:delete-employee-by-number }
     *
     * @param employeeNumber Number of Employee
     * @throws TaleoException Exception
     */
    @Processor
    public void deleteEmployeeByNumber(
            String employeeNumber)
            throws TaleoException {
        connectionManagement.getClient().deleteEmployeeByNumber(employeeNumber);
    }

    /**
     * Update an existing offer instance
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:update-offer }
     * <p/>
     * Update requests replace all data fields in the offerBean (i.e. data is not appended or upserted).
     *
     * @param offer Offer required fields: id, status, candidateId, manager, requisitionId, title.
     * @throws TaleoException Exception
     */
    @Processor
    public void updateOffer(
            @Default("#[payload]") OfferBean offer)
            throws TaleoException {
        connectionManagement.getClient().updateOffer(offer);
    }


    /**
     * Get a specific rolling entity for a candidate or employee.
     * There is no ‘ALL’ function; a call is configured for the pulling of each rolling entity type:
     * <ul><li>WORK_HISTORY</li>
     * <li>RESIDENCE_HISTORY</li>
     * <li>EDUCATION</li>
     * <li>REFERENCES</li>
     * <li>CERTIFICATES_LICENCES</li></ul>
     *
     * @param rollingEntityType Acceptable Values:
     *                          <ul><li>WORK_HISTORY</li>
     *                          <li>RESIDENCE_HISTORY</li>
     *                          <li>EDUCATION</li>
     *                          <li>REFERENCES</li>
     *                          <li>CERTIFICATES_LICENCES</li></ul>
     *                          <p/>
     *                          {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-rolling-entities }
     * @param entityType        Acceptable Value : CAND
     * @param entityId          candidate ID
     * @return Requested rolling entity
     * @throws TaleoException Exception
     */
    @Processor
    public FlexRollingEntityBeanArr getRollingEntities(
            String rollingEntityType,
            String entityType,
            long entityId)
            throws TaleoException {
        return connectionManagement.getClient().getRollingEntities(rollingEntityType, entityType, entityId);
    }


    /**
     * Get a Candidate transaction history
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-candidate-history }
     *
     * @param candidateId ID of Candidate
     * @return History for
     * @throws TaleoException Exception
     */
    @Processor
    public HistoryBeanArr getCandidateHistory(
            long candidateId)
            throws TaleoException {
        return connectionManagement.getClient().getCandidateHistory(candidateId);
    }


    /**
     * Create an event
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-event }
     *
     * @param event Event required fields: status, duration, entityId, andtityType, startDate, eventiId, eventType, personId, personType.
     * @return Provided Event ID
     * @throws TaleoException Exception
     */
    @Processor
    public long createEvent(
            @Default("#[payload]") CalendarEventBean event)
            throws TaleoException {
        return connectionManagement.getClient().createEvent(event);
    }


    /**
     * Get all tasks associated with a user
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-task-by-user }
     *
     * @param userId    User ID
     * @param startDate startDate interval for due items
     * @param endDate   endDate interval for due items
     * @return Array of task IDs
     * @throws TaleoException Exception
     */
    @Processor
    public LongArr getTaskByUser(
            long userId,
            GregorianCalendar startDate,
            GregorianCalendar endDate)
            throws TaleoException {
        return connectionManagement.getClient().getTaskByUser(userId, startDate, endDate);
    }


    /**
     * Retrieve the token results of a login session ID.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-login-token }
     *
     * @param sessionId Session ID
     * @return Login Session ID
     * @throws TaleoException Exception
     */
    @Processor
    public String getLoginToken(
            String sessionId)
            throws TaleoException {
        return connectionManagement.getClient().getLoginToken(sessionId);
    }

    /**
     * Delete a Region
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:delete-region }
     *
     * @param regionId ID of Region
     * @throws TaleoException Exception
     */
    @Processor
    public void deleteRegion(
            long regionId)
            throws TaleoException {
        connectionManagement.getClient().deleteRegion(regionId);
    }

    /**
     * Update an existing BackgroundCheck instance. Update requests replace all data fields in the backgroundCheckBean (i.e. data is not appended or upserted).
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:update-background-check }
     *
     * @param backgroundCheck Background required fields: candidataId, checkerName, comments.
     * @throws TaleoException Exception
     */
    @Processor
    public void updateBackgroundCheck(
            @Default("#[payload]") BackgroundCheckBean backgroundCheck)
            throws TaleoException {
        connectionManagement.getClient().updateBackgroundCheck(backgroundCheck);
    }


    /**
     * Create a Location.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-location }
     *
     * @param location Location required fields: locationName.
     * @return Provided Location ID
     * @throws TaleoException Exception
     */
    @Processor
    public long createLocation(
            @Default("#[payload]") LocationBean location)
            throws TaleoException {
        return connectionManagement.getClient().createLocation(location);
    }

    /**
     * Get a users association within a given requisition.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-associated-users }
     *
     * @param requisitionId Requisition ID
     * @param association   Association Values Picklist of Association Values:
     *                      <ul><li>‘O’ = Owner</li>
     *                      <li>‘A’ = approvers (in order) ‘F’ = offer approvers (in order)</li>
     *                      <li>‘V’ = agencies</li></ul>
     * @return Array of User IDs
     * @throws TaleoException Exception
     */
    @Processor
    public LongArr getAssociatedUsers(
            long requisitionId,
            String association)
            throws TaleoException {
        return connectionManagement.getClient().getAssociatedUsers(requisitionId, association);
    }


    /**
     * Create an interview instance for a specific candidate.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-interview }
     *
     * @param interview Interview with status, candidateId, interviewType, requisitionId and startDate.
     * @return Provided interview ID
     * @throws TaleoException Exception
     */
    @Processor
    public long createInterview(
            @Default("#[payload]") InterviewBean interview)
            throws TaleoException {
        return connectionManagement.getClient().createInterview(interview);
    }

    /**
     * Update an existing account. Update requests replace all data fields in the accountBean (i.e. data is not appended or upserted).
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:update-account }
     *
     * @param account AcountBean required fields: id, name
     * @throws TaleoException Exception
     */
    @Processor
    public void updateAccount(
            @Default("#[payload]") AccountBean account)
            throws TaleoException {
        connectionManagement.getClient().updateAccount(account);
    }


    /**
     * Get a Requisition transaction history.
     * This object is used to fetch a history log entry which reflects a past event or action related to requisition. History records can only be created by the server, they cannot be modified or deleted. History log includes server side ID, test describing history record, complete text of email message (for email events) and creation date.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-requisition-history }
     *
     * @param requisitionId ID of Requisition
     * @return HistoryBean
     * @throws TaleoException Exception
     */
    @Processor
    public HistoryBeanArr getRequisitionHistory(
            long requisitionId)
            throws TaleoException {
        return connectionManagement.getClient().getRequisitionHistory(requisitionId);
    }

    /**
     * Unpost a specific requisition to a posting location
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:unpost-requisition }
     *
     * @param requisitionId ID of requisition
     * @param posterId      ID of the posting location, for example a specific Taleo Business Edition Career Website (picklist value ID #).
     * @throws TaleoException Exception
     */
    @Processor
    public void unpostRequisition(
            long requisitionId,
            long posterId)
            throws TaleoException {
        connectionManagement.getClient().unpostRequisition(requisitionId, posterId);
    }


    /**
     * Get attachment(s) associated with any Taleo Business Edition entity that supports attachment upload.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-entity-attachments }
     *
     * @param entityType Entity Type.
     * @param entityId   ID of associated entity
     * @return List of attachments IDs
     * @throws TaleoException Exception
     */
    @Processor
    public LongArr getEntityAttachments(
            String entityType,
            long entityId)
            throws TaleoException {
        return connectionManagement.getClient().getEntityAttachments(entityType, entityId);
    }


    /**
     * Update an existing employee. Note: Update requests replace all data fields in the employeeBean (i.e. data is not appended)
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:update-employee }
     *
     * @param employee Employee required fields: id, status, employeeNumber, lastName set, ewsLogin, ewsPassword.
     * @throws TaleoException Exception
     */
    @Processor
    public void updateEmployee(
            @Default("#[payload]") EmployeeBean employee)
            throws TaleoException {
        connectionManagement.getClient().updateEmployee(employee);
    }


    /**
     * The endpoint receives a message to create an account, and sends a correlated message ￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼of account ID.
     * Accounts are the companies and organizations involved with your business such as employers, customers, search firms, agencies, etc.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-account }
     *
     * @param account AcountBean with name
     * @return Provided Account ID
     * @throws TaleoException Exception
     */
    @Processor
    public long createAccount(
            @Default("#[payload]") AccountBean account)
            throws TaleoException {
        return connectionManagement.getClient().createAccount(account);
    }

    /**
     * Delete an attachment
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:delete-attachment }
     *
     * @param attachmentId ID of attachment
     * @throws TaleoException Exception
     */
    @Processor
    public void deleteAttachment(
            long attachmentId)
            throws TaleoException {
        connectionManagement.getClient().deleteAttachment(attachmentId);

    }

    /**
     * Create a user with permission attributes
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-user-with-permissions }
     *
     * @param user               User required fields: status, email, lastName, loginName, role
     * @param additionalEntities A map containing:
     *                           <ul><li>approver (Boolean)</li>
     *                           <li>offerAppr (Boolean)</li>
     *                           <li>reviewManager(Boolean)</li>
     *                           <li>reviewApprover(Boolean)</li></ul>
     * @return Provided user ID
     * @throws TaleoException Exception
     */
    @Processor
    public long createUserWithPermissions(
            UserBean user,
            @Default("#[payload]") java.util.Map<String, Object> additionalEntities)
            throws TaleoException {
        return connectionManagement.getClient().createUserWithPermissions(user, toTaleoMap(additionalEntities));
    }


    /**
     * Query all Users based on defined key (field name) and value (data looking for).
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:search-user }
     *
     * @param searchParams Map of search parameters always contains:key: name of field to search as a string. value: search parameter as string. For more details check <a href="http://tbe.taleo.net/products/TBE_API_Guide.pdf">Taleo Business Guide</a>
     * @return Search results that match the criteria
     * @throws TaleoException Exception
     */
    @Processor
    public SearchResultArr searchUser(
            @Default("#[payload]") java.util.Map<String, Object> searchParams)
            throws TaleoException {
        Map searchMapParams = toTaleoMap(searchParams);
        return connectionManagement.getClient().searchUser(searchMapParams);
    }


    /**
     * Update or create a department instance.
     * <p/>
     * Upsert is used to both create or update data within a specific department. Using the department ID AND department Name attribute updates or replaces existing department details. Requires both
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:upsert-department }
     *
     * @param department Department required fields: departmentID, departmentName.
     * @return Department ID
     * @throws TaleoException Exception
     */
    @Processor
    public long upsertDepartment(
            @Default("#[payload]") DepartmentBean department)
            throws TaleoException {
        return connectionManagement.getClient().upsertDepartment(department);
    }

    /**
     * Delete an employee
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:delete-employee }
     *
     * @param employeeId ID of Employee
     * @throws TaleoException Exception
     */
    @Processor
    public void deleteEmployee(
            long employeeId)
            throws TaleoException {
        connectionManagement.getClient().deleteEmployee(employeeId);
    }


    /**
     * Get a contact details
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-contact-by-id }
     *
     * @param contactId ID of contact
     * @return Contact
     * @throws TaleoException Exception
     */
    @Processor
    public ContactBean getContactById(
            long contactId)
            throws TaleoException {
        return connectionManagement.getClient().getContactById(contactId);
    }

    /**
     * Delete a specific rolling entity instance based on ID
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:delete-rolling-entity }
     *
     * @param rollingEntityId ID of rolling entity
     * @throws TaleoException Exception
     */
    @Processor
    public void deleteRollingEntity(
            long rollingEntityId)
            throws TaleoException {
        connectionManagement.getClient().deleteRollingEntity(rollingEntityId);
    }


    /**
     * Get a candidate general information by ID
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-candidate-by-id }
     *
     * @param candidateId ID of Candidate
     * @return Candidate Bean
     * @throws TaleoException Exception
     */
    @Processor
    public CandidateBean getCandidateById(
            long candidateId)
            throws TaleoException {
        return connectionManagement.getClient().getCandidateById(candidateId);
    }


    /**
     * Get requisition(s) associated with any Taleo Business Edition candidate
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-requisitions }
     *
     * @param candidateId ID of candidate
     * @return requisition IDs associated with the candidate ID
     * @throws TaleoException Exception
     */
    @Processor
    public LongArr getRequisitions(
            long candidateId)
            throws TaleoException {
        return connectionManagement.getClient().getRequisitions(candidateId);
    }


    /**
     * Remove Linkage between two entities in Taleo Business Edition.<p/>
     * Links are what tie one entity attribute to another. For example, linking a candidate to a requisition, or an attachment to a candidate.
     * <p/> Full list of entities include:
     * <ul><li>Account = ACCT</li>
     * <li>Candidate = CAND</li>
     * <li>Contact = CTCT</li>
     * <li>Requisition = REQU</li>
     * <li>User = WORK</li></ul>
     * <p/>Where the following linkage is available:
     * <ul><li>ACCT to REQU</li>
     * <li>CTCT to REQU</li>
     * <li>ACCT to CAND</li>
     * <li>CTCT to CAND</li>
     * <li>CAND to WORK</li></ul>
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:remove-link }
     *
     * @param entityType1 entityType as provided in above list.
     * @param entityId1   ID of entity type 1
     * @param entityType2 entityType as provided in above list.
     * @param entityId2   ID of entity type 2
     * @throws TaleoException Exception
     */
    @Processor
    public void removeLink(
            String entityType1,
            long entityId1,
            String entityType2,
            long entityId2)
            throws TaleoException {
        connectionManagement.getClient().removeLink(entityType1, entityId1, entityType2, entityId2);
    }


    /**
     * Get a Location details
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-location-by-name }
     *
     * @param locationName Name of Location
     * @return Location
     * @throws TaleoException Exception
     */
    @Processor
    public LocationBean getLocationByName(
            String locationName)
            throws TaleoException {
        return connectionManagement.getClient().getLocationByName(locationName);
    }


    /**
     * Create a task.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-task }
     *
     * @param task Task required fields: status, creator, priority, subject, eventyId, evenType, personId, personType, startDate
     * @return Provided Task ID
     * @throws TaleoException Exception
     */
    @Processor
    public long createTask(
            @Default("#[payload]") TaskBean task)
            throws TaleoException {
        return connectionManagement.getClient().createTask(task);
    }


    /**
     * Update an existing rolling entity instance.
     * Upsert is used to both create or update data within a specific rolling entity. Using the ID attribute updates or replaces existing rolling entity instance, whereas not using an entityId will create a new rolling entity for the entity ID.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:upsert-rolling-entity }
     *
     * @param rollingEntityBean RollingEntity required field: Id, Status
     * @return ID of RollingEntity TODO: check if Taleo server returns the ID when the user creates a new one, since the XML sample show that the update return message was empty.
     * @throws TaleoException Exception
     */
    @Processor
    public long upsertRollingEntity(
            @Default("#[payload]") FlexRollingEntityBean rollingEntityBean)
            throws TaleoException {
        return connectionManagement.getClient().upsertRollingEntity(rollingEntityBean);
    }


    /**
     * Delete a background check instance.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:delete-background-check }
     *
     * @param backgroundCheckId ID of Background Check
     * @throws TaleoException Exception
     */
    @Processor
    public void deleteBackgroundCheck(
            long backgroundCheckId)
            throws TaleoException {
        connectionManagement.getClient().deleteBackgroundCheck(backgroundCheckId);
    }


    /**
     * Get an account details
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-account-by-id }
     *
     * @param accountId ID of Account
     * @return AccountBean
     * @throws TaleoException Exception
     */
    @Processor
    public AccountBean getAccountById(
            long accountId)
            throws TaleoException {
        return connectionManagement.getClient().getAccountById(accountId);
    }

    /**
     * Upsert, or append a candidate to a requisition
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:upsert-candidate-to-requisitions }
     *
     * @param candidateId    ID of candidate
     * @param requisitionIds Array of requisition IDs
     * @param statusId       ID of status or requisition-specific status ID
     * @param reasonId       ID of reasonRej or requisition rejected status ID
     * @param doRanking      forces the system to recalculate the candidate’s rank for that requisition on upsert
     * @throws TaleoException Exception
     */
    @Processor
    public void upsertCandidateToRequisitions(
            long candidateId,
            LongArr requisitionIds,
            long statusId,
            long reasonId,
            @Default("false") Boolean doRanking)
            throws TaleoException {
        connectionManagement.getClient().upsertCandidateToRequisitions(candidateId, requisitionIds, statusId, reasonId, doRanking);

    }


    /**
     * Get all Departments. This functions similar to getLookUp Request.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-departments }
     *
     * @return Array of departments
     * @throws TaleoException Exception
     */
    @Processor
    public DepartmentArr getDepartments()
            throws TaleoException {
        return connectionManagement.getClient().getDepartments();
    }


    /**
     * Delete a Location.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:delete-location }
     *
     * @param locationId ID of Location
     * @throws TaleoException Exception
     */
    @Processor
    public void deleteLocation(
            long locationId)
            throws TaleoException {
        connectionManagement.getClient().deleteLocation(locationId);
    }


    /**
     * Get an attachment by attachment ID
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-attachment }
     *
     * @param attachmentId ID of the attachment
     * @return Attachment
     * @throws TaleoException Exception
     */
    @Processor
    public AttachmentBean getAttachment(
            long attachmentId)
            throws TaleoException {
        return connectionManagement.getClient().getAttachment(attachmentId);
    }


    /**
     * Receives a message resume binary attachment, and sends back a passed output of resume data (using third-party provider ResumeMirror)
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:parse-resume }
     *
     * @param binaryAttachment Binary Attachment
     * @return Resume data
     * @throws TaleoException Exception
     */
    @Processor
    public String parseResume(
            @Default("#[payload]") ByteArr binaryAttachment)
            throws TaleoException {
        return connectionManagement.getClient().parseResume(binaryAttachment);
    }


    /**
     * Create a background check.
     * Background checks provides background check information that may have occurred for a specific candidate in Taleo Business Edition. There is a one-to-many relationship with candidates to background checks.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-background-check }
     *
     * @param backgroundCheck BackgroundCheck entity to create
     * @return Provided Background Check Id
     * @throws TaleoException Exception
     */
    @Processor
    public long createBackgroundCheck(
            @Default("#[payload]") BackgroundCheckBean backgroundCheck)
            throws TaleoException {
        return connectionManagement.getClient().createBackgroundCheck(backgroundCheck);
    }

    /**
     * Query all accounts based on defined key (field name) and value (data looking for), and sends a correlated message of Account ID and associated search relevance.
     * <p/>
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:search-account }
     *
     * @param searchParams Map of search parameters always contains:key: name of field to search as a string. value: search parameter as string. For more details check <a href="http://tbe.taleo.net/products/TBE_API_Guide.pdf">Taleo Business Guide</a>
     * @return The results that match the criteria
     * @throws TaleoException Exception
     */
    @Processor
    public SearchResultArr searchAccount(
            @Default("#[payload]") java.util.Map<String, Object> searchParams)
            throws TaleoException {
        Map searchMapParams = toTaleoMap(searchParams);
        return connectionManagement.getClient().searchAccount(searchMapParams);
    }


    /**
     * Update an existing user.
     * <p/>
     * Can assign user to department via flexFields where each flexFields array appends an additional department value. The value can be either the department id (set on the bean as valueLong) or department name (as String).
     * <p/>
     * Update requests replace all data fields in the userBean (i.e. data is not appended or upserted). Send all elements back or the field values will be returned NULL.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:update-user }
     *
     * @param user User required fields: status, id, loginName, lastName, role
     * @throws TaleoException Exception
     */
    @Processor
    public void updateUser(
            @Default("#[payload]") UserBean user)
            throws TaleoException {
        connectionManagement.getClient().updateUser(user);
    }


    /**
     * The endpoint receives a message to create an attachment for a specific candidate, and sends a correlated message of attachment ID.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-attachment }
     *
     * @param candidateId           Candidate Id
     * @param attachmentDescription Attachment description
     * @param attachmentName        Attachment name
     * @param contentType           Content Type
     * @param binaryResume          Base64 data
     * @return Provided Attachment Id
     * @throws TaleoException Exception
     */
    @Processor
    public long createAttachment(
            long candidateId,
            String attachmentDescription,
            String attachmentName,
            String contentType,
            @Default("#[payload]") ByteArr binaryResume)
            throws TaleoException {
        return connectionManagement.getClient().createAttachment(candidateId, attachmentDescription, attachmentName, contentType, binaryResume);
    }


    /**
     * Get a Contact transaction history
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-contact-history }
     *
     * @param contactId ID of contact
     * @return History for contact information
     * @throws TaleoException Exception
     */
    @Processor
    public HistoryBeanArr getContactHistory(
            long contactId)
            throws TaleoException {
        return connectionManagement.getClient().getContactHistory(contactId);
    }

    /**
     * Update an existing interview instance.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:update-interview }
     * <p/>
     * Update requests replace all data fields in the interviewBean (i.e. data is not appended or upserted)
     *
     * @param interview Interview required fields: id, status, candidateID, interviewType, requisitionId, startDate.
     * @throws TaleoException Exception
     */
    @Processor
    public void updateInterview(
            @Default("#[payload]") InterviewBean interview)
            throws TaleoException {
        connectionManagement.getClient().updateInterview(interview);
    }

    /**
     * Create a requisition template.
     * Templates are the available jobs preloaded in Taleo for users to post easily with consistency. They are stored in the job library.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-requisition-template }
     *
     * @param requisition Requisition required fields: status, location, title.
     * @return Provided Requisition ID.
     * @throws TaleoException Exception
     */
    @Processor
    public long createRequisitionTemplate(
            @Default("#[payload]") RequisitionBean requisition)
            throws TaleoException {
        return connectionManagement.getClient().createRequisitionTemplate(requisition);
    }


    /**
     * Get a status of a requisition for a particular candidate
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-cand-req-status }
     *
     * @param candidateId   ID of candidate
     * @param requisitionId ID of requisition
     * @return the candidate’s specific requisition status
     * @throws TaleoException Exception
     */
    @Processor
    public String getCandReqStatus(
            long candidateId,
            long requisitionId)
            throws TaleoException {
        return connectionManagement.getClient().getCandReqStatus(candidateId, requisitionId);
    }


    /**
     * Get a Region details.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-region-by-name }
     *
     * @param regionName Name of Region
     * @return Region
     * @throws TaleoException Exception
     */
    @Processor
    public RegionBean getRegionByName(
            String regionName)
            throws TaleoException {
        return connectionManagement.getClient().getRegionByName(regionName);
    }


    /**
     * Get employee general information by ID
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-employee-by-id }
     *
     * @param employeeId ID of Employee
     * @return Employee
     * @throws TaleoException Exception
     */
    @Processor
    public EmployeeBean getEmployeeById(
            long employeeId)
            throws TaleoException {
        return connectionManagement.getClient().getEmployeeById(employeeId);
    }

    /**
     * Query all candidates based on defined key (field name) and value (data looking for).
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:search-candidate }
     *
     * @param searchParams Map of search parameters always contains:key: name of field to search as a string. value: search parameter as string. For more details check <a href="http://tbe.taleo.net/products/TBE_API_Guide.pdf">Taleo Business Guide</a>
     * @return The results that match the criteria
     * @throws TaleoException Exception
     */
    @Processor
    public SearchResultArr searchCandidate(
            @Default("#[payload]") java.util.Map<String, Object> searchParams)
            throws TaleoException {
        Map searchMapParams = toTaleoMap(searchParams);
        return connectionManagement.getClient().searchCandidate(searchMapParams);
    }


    /**
     * Get the offer letter binary attachment associated with a candidate (based on offer ID).
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-binary-offer }
     *
     * @param offerId ID of offer instance
     * @return ByteArr
     * @throws TaleoException Exception
     */
    @Processor
    public ByteArr getBinaryOffer(
            long offerId)
            throws TaleoException {
        return connectionManagement.getClient().getBinaryOffer(offerId);
    }


    /**
     * Query all employees based on defined key (field name) and value (data looking for)
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:search-employee }
     *
     * @param searchParams Map of search parameters always contains:key: name of field to search as a string. value: search parameter as string. For more details check <a href="http://tbe.taleo.net/products/TBE_API_Guide.pdf">Taleo Business Guide</a>
     * @return The results that match the criteria
     * @throws TaleoException Exception
     */
    @Processor
    public SearchResultArr searchEmployee(
            @Default("#[payload]") java.util.Map<String, Object> searchParams) throws TaleoException {
        Map searchMapParams = toTaleoMap(searchParams);
        return connectionManagement.getClient().searchEmployee(searchMapParams);
    }


    /**
     * Get a Region details
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-region-by-id }
     *
     * @param regionId ID of Region
     * @return Region
     * @throws TaleoException Exception
     */
    @Processor
    public RegionBean getRegionById(
            long regionId)
            throws TaleoException {
        return connectionManagement.getClient().getRegionById(regionId);
    }


    /**
     * Delete a user
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:delete-user }
     *
     * @param userId ID of User
     * @throws TaleoException Exception
     */
    @Processor
    public void deleteUser(
            long userId)
            throws TaleoException {
        connectionManagement.getClient().deleteUser(userId);
    }


    /**
     * Delete an event instance
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:delete-event }
     *
     * @param eventId ID of Event
     * @throws TaleoException Exception
     */
    @Processor
    public void deleteEvent(
            long eventId)
            throws TaleoException {
        connectionManagement.getClient().deleteEvent(eventId);
    }


    /**
     * Link an entity (by ID) to another entity (by ID) in Taleo Business Edition.
     * <p/>
     * Links are what tie one entity attribute to another. For example, linking a candidate to a requisition, or an attachment to a candidate.
     * <p/> Full list of entities include:
     * <ul><li>Account = ACCT</li>
     * <li>Candidate = CAND</li>
     * <li>Contact = CTCT</li>
     * <li>Requisition = REQU</li>
     * <li>User = WORK</li></ul>
     * <p/>Where the following linkage is available:
     * <ul><li>ACCT to REQU</li>
     * <li>CTCT to REQU</li>
     * <li>ACCT to CAND</li>
     * <li>CTCT to CAND</li>
     * <li>CAND to WORK</li></ul>
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:create-link }
     *
     * @param entityType1 entityType as provided in above list
     * @param entityId1   ID of entity type 1
     * @param entityType2 entityType as provided in above list
     * @param entityId2   ID of entity type 2
     * @throws TaleoException Exception
     */
    @Processor
    public void createLink(
            String entityType1,
            long entityId1,
            String entityType2,
            long entityId2)
            throws TaleoException {
        connectionManagement.getClient().createLink(entityType1, entityId1, entityType2, entityId2);
    }

    /**
     * Set a user’s association to a requisition
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:set-associated-user }
     *
     * @param requisitionId      Requisition ID
     * @param association        Picklist of Association Values:
     *                           <ul><li>‘O’ = Owner</li>
     *                           <li>‘A’ = approvers (in order)</li>
     *                           <li>‘F’ = offer approvers (in order)</li>
     *                           <li>‘V’ = agencies</li></ul>
     * @param userId             User ID
     * @param userSequenceNumber ￼Sequence number of user to association value
     * @throws TaleoException Exception
     */
    @Processor
    public void setAssociatedUser(
            long requisitionId,
            String association,
            long userId,
            int userSequenceNumber)
            throws TaleoException {
        connectionManagement.getClient().setAssociatedUser(requisitionId, association, userId, userSequenceNumber);
    }

    /**
     * Get URL of a company code, or customer instance.
     * This call is always performed using URL: https://tbe.taleo.net/MANAGER/dispatcher/servlet/rpcrouter
     * <p/>
     * {@sample.xml ../../../doc/mule-module-taleo.xml.sample taleo:get-url }
     *
     * @param companyCode Company Code of Taleo Customer
     * @return URL requested
     * @throws TaleoException Exception
     */
    @Processor
    public String getUrl(String companyCode) throws TaleoException {
        return connectionManagement.getClient().getUrl(companyCode);
    }

    private Map toTaleoMap(java.util.Map<String, Object> mapEntries) {
        Map map = new Map();
        List<MapItem> listItem = new ArrayList<MapItem>();
        MapItem item = null;
        for (java.util.Map.Entry<String, Object> entry : mapEntries.entrySet()) {
            item = new MapItem();
            item.setKey(entry.getKey());
            item.setPropertyValue(entry.getValue());
            listItem.add(item);
        }
        map.setItem(listItem);
        return map;
    }
}
