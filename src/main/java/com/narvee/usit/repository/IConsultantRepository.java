package com.narvee.usit.repository;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.narvee.usit.dto.ConsultantDTO;
import com.narvee.usit.dto.ConsultantReportDTO;
import com.narvee.usit.dto.ConsultantTrackerDTO;
import com.narvee.usit.dto.SubmissionDTO;
import com.narvee.usit.entity.ConsultantInfo;
import com.narvee.usit.helper.ListInterview;
import com.narvee.usit.helper.ListRecruitingConsultant;

public interface IConsultantRepository extends JpaRepository<ConsultantInfo, Serializable> {

	public ConsultantInfo findByContactnumber(String number);

	public String findByConsultantid(long number);

	public ConsultantInfo findByContactnumberOrConsultantemail(String number, String email);

	public ConsultantInfo findByContactnumberAndConsultantidNot(String number, Long id);

	public ConsultantInfo findByConsultantemail(String email);

	@Modifying
	@Query(value = "UPDATE consultant_info c SET c.comment=:comment, c.consultantflg= :flg,c.status=:status,c.updatedbyid=:userid   WHERE c.consultantid = :id", nativeQuery = true)
	public int movedtoconsultant(@Param("id") Long id, @Param("flg") String flg, @Param("comment") String rem,
			@Param("status") String status, @Param("userid") long userid);

	@Modifying
	@Query("UPDATE ConsultantInfo c  SET c.status= :status, c.remarks=:remarks, c.consultantflg=:presales  WHERE c.consultantid = :id ")
	public int changeConsultantStatus(@Param("status") String status, @Param("id") Long id,
			@Param("remarks") String rem, @Param("presales") String flg);

	// public List<ConsultantInfo> findByRequirementsRequirementid(long id);

	// public List<ConsultantInfo>
	// findByConsultantemailAndRequirementsRequirementid(String email, Long id);

//	public List<ConsultantInfo> findByConsultantemailAndRequirementsRequirementidAndConsultantidNot(String email,
//			Long id, Long id1);

	public List<ConsultantInfo> findByConsultantemailAndConsultantidNot(String email, Long id);

	public List<ConsultantInfo> findByConsultantemailAndConsultantidNot(String email, long id);

	public List<ConsultantInfo> findByConsultantflg(String flg);

//c.requirementid,
	@Query(value = "select c.comment,u.userid,v.id as visaid,c.techid,c.status,c.addedby,c.consultantid,u.fullname,u.pseudoname, c.availabilityforinterviews, c.currentlocation, c.companyname,c.consultantemail,c.consultantflg,c.consultantname,c.contactnumber\r\n"
			+ ", c.createddate,c.experience,c.linkedin, c.passportnumber, c.priority,c.projectavailabity,\r\n"
			+ "c.ratetype,c.relocatother,c.relocation,c.remarks,c.resume, c.skills,c.summary,c.university,\r\n"
			+ "c.updateddate, c.yop,c.hourlyrate,c.position, t.technologyarea,v.visa_status from consultant_info c, technologies t, visa v,users u \r\n"
			+ "where \r\n" + "t.id = c.techid and \r\n"
			+ "v.id = c.visa_status and  u.userid=c.addedby and c.consultantflg=:flg and u.userid = :userid order by c.updateddate desc", nativeQuery = true)
	public List<ConsultantDTO> normalRoleList(@Param("flg") String flg, @Param("userid") long userid);

// c.empcontact,
	@Query(value = "select  c.comment,u.userid,v.id as visaid,c.techid,c.status,c.addedby,c.consultantid,u.fullname,u.pseudoname, c.availabilityforinterviews, c.currentlocation, c.companyname,c.consultantemail,c.consultantflg,c.consultantname,c.contactnumber\r\n"
			+ ", c.createddate,c.experience,c.linkedin, c.passportnumber, c.priority,c.projectavailabity,\r\n"
			+ "c.ratetype,c.relocatother,c.relocation,c.remarks,c.resume,  c.skills,  c.summary,c.university,\r\n"
			+ "c.updateddate, c.yop,c.hourlyrate,c.position, t.technologyarea,v.visa_status from consultant_info c, technologies t, visa v,users u \r\n"
			+ "where \r\n" + "t.id = c.techid and \r\n"
			+ "v.id = c.visa_status and  u.userid=c.addedby and c.consultantflg=:flg  order by c.updateddate desc", nativeQuery = true)
	public List<ConsultantDTO> adminRoleList(@Param("flg") String flg);

	/*
	 * c.requirementid,
	 * 
	 * @Query(value =
	 * "select c.comment,u.userid,c.requirementid,v.id as visaid,c.techid,c.status,c.addedby,c.consultantid,u.fullname,u.pseudoname, c.availabilityforinterviews, c.city, c.companyname,c.consultantemail,c.consultantflg,c.consultantname,c.contactnumber\r\n"
	 * +
	 * ", c.createddate, c.empcontact,c.empemail,c.experience,c.linkedin, c.passportnumber, c.priority,c.projectavailabity,c.qualification,\r\n"
	 * +
	 * "c.ratetype,c.relocatother,c.relocation,c.remarks,c.resume, c.salesemp, c.skills, c.specialization, c.state, c.summary,c.university,\r\n"
	 * +
	 * "c.updateddate, c.yop,c.hourlyrate, t.technologyarea,v.visa_status from consultant_info c, technologies t, visa v,users u \r\n"
	 * + "where \r\n" + "t.id = c.techid and \r\n" +
	 * "v.id = c.visa_status and  u.userid=c.addedby and c.consultantflg IN(:flg) and c.status NOT IN ('Initiated', 'Rejected')  order by c.consultantid desc"
	 * , nativeQuery = true) public List<ConsultantDTO>
	 * managerRoleList(@Param("flg") String flg); select
	 * c.comment,u.userid,c.requirementid,v.id as
	 * visaid,c.techid,c.status,c.addedby,c.consultantid,u.fullname,u.pseudoname,\r\
	 * n" +
	 * " c.availabilityforinterviews, c.city, c.companyname,c.consultantemail,c.consultantflg,c.consultantname,c.contactnumber\r\n"
	 * +
	 * ", c.createddate, c.empcontact,c.empemail,c.experience,c.linkedin, c.passportnumber, c.priority,c.projectavailabity,\r\n"
	 * +
	 * "c.qualification,c.ratetype,c.relocatother,c.relocation,c.remarks,c.resume, c.salesemp, c.skills, c.specialization, c.state, \r\n"
	 * +
	 * "c.summary,c.university,c.updateddate, c.yop,c.hourlyrate, t.technologyarea,v.visa_status from consultant_info c, technologies t,\r\n"
	 * + " visa v,users u where t.id = c.techid and \r\n" +
	 * "v.id = c.visa_status and  u.userid=c.addedby and c.consultantflg IN(:flg) and (c.addedby=:userid or u.manager=:userid or u.teamlead=:userid)  and c.status NOT IN ('Initiated','Rejected')\r\n"
	 * + " union \r\n"
	 */
	// c.requirementid,c.empcontact,c.specialization,

	@Query(value = " select c.comment,u.userid,v.id as visaid,c.techid,c.status,c.addedby,c.consultantid,u.fullname,u.pseudoname,\r\n"
			+ " c.availabilityforinterviews, c.currentlocation, c.companyname,c.consultantemail,c.consultantflg,c.consultantname,c.contactnumber\r\n"
			+ ", c.createddate, c.experience,c.linkedin, c.passportnumber, c.priority,c.projectavailabity,\r\n"
			+ "c.ratetype,c.relocatother,c.relocation,c.remarks,c.resume, c.skills, \r\n"
			+ "c.summary,c.university,c.updateddate,c.position, c.yop,c.hourlyrate, t.technologyarea,v.visa_status from consultant_info c, technologies t,\r\n"
			+ " visa v,users u where t.id = c.techid and \r\n"
			+ "v.id = c.visa_status and  u.userid=c.addedby and c.consultantflg IN(:flg) and ((c.addedby=:userid ) or (u.manager=:userid and c.status not in('Initiated','Rejected') ) or (u.teamlead=:userid and c.status not in('Initiated','Rejected'))) order by c.updateddate desc", nativeQuery = true)
	public List<ConsultantDTO> managerRoleList(@Param("flg") String flg, @Param("userid") long userid);

	@Modifying
	@Query("UPDATE ConsultantInfo c SET c.resume= :resume, c.updateddate=:udate  WHERE c.consultantid = :id")
	public int update(@Param("resume") String resume, @Param("id") Long id, @Param("udate") LocalDateTime now);

	@Query(value = "select u.userid,c.createddate, c.consultantname as name , c.experience, c.consultantemail as email, v.visa_status, u.fullname, c.updateddate, c.isactive as status, c.consultantid as id, t.technologyarea   from ConsultantInfo c, visa v, users u , technologies t where v.id = c.visaid and u.userid = c.addedby and t.id = c. techid ", nativeQuery = true)
	public List<ListRecruitingConsultant> getlistofconsultants();

	@Query(value = "select s.consultantid,s.consultantname, t.technologyarea,s.experience from consultant_info s, technologies t where t.id = s.techid and s.status='Active' and s.consultantflg=:flg order by s.consultantname ", nativeQuery = true)
	public List<Object[]> getconsultInfo(@Param("flg") String flg);

	@Query(value = "SELECT t.createddate,t.remarks,t.status,t.updateddate,a.fullname as addedby,a.pseudoname as addebysudo, u.fullname as updatedby,u.pseudoname as updatedbysudo\r\n"
			+ " FROM consultant_track t, users a, users u where u.userid=t.updatedby and a.userid=t.addedby and t.consultantid=:id\r\n"
			+ " order by  t.id desc", nativeQuery = true)
	public List<ConsultantTrackerDTO> consultantTracker(long id);

	@Query(value = " select c.comment,u.userid,v.id as visaid,c.techid,c.status,c.addedby,c.consultantid,u.fullname,u.pseudoname,\r\n"
			+ " c.availabilityforinterviews, c.currentlocation, c.companyname,c.consultantemail,c.consultantflg,c.consultantname,c.contactnumber\r\n"
			+ ", c.createddate,c.experience,c.linkedin, c.passportnumber, c.priority,c.projectavailabity,\r\n"
			+ "c.ratetype,c.relocatother,c.relocation,c.remarks,c.resume,  c.skills,  \r\n"
			+ "c.summary,c.university,c.updateddate,c.position, c.yop,c.hourlyrate, t.technologyarea,v.visa_status from consultant_info c, technologies t,\r\n"
			+ " visa v,users u where t.id = c.techid and \r\n"
			+ "v.id = c.visa_status and  u.userid=c.addedby and c.createddate between :startDate and :endDate and c.status In (:status) and c.consultantid=:id  order by c.consultantid desc", nativeQuery = true)
	public List<ConsultantDTO> reportDrillDown(@Param("status") List<String>  status,
			@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate,@Param("id") Long id);
	
	@Query(value = " select c.comment,u.userid,v.id as visaid,c.techid,c.status,c.addedby,c.consultantid,u.fullname,u.pseudoname,\r\n"
			+ " c.availabilityforinterviews, c.currentlocation, c.companyname,c.consultantemail,c.consultantflg,c.consultantname,c.contactnumber\r\n"
			+ ", c.createddate,c.experience,c.linkedin, c.passportnumber, c.priority,c.projectavailabity,\r\n"
			+ "c.ratetype,c.relocatother,c.relocation,c.remarks,c.resume,  c.skills,  \r\n"
			+ "c.summary,c.university,c.updateddate,c.position, c.yop,c.hourlyrate, t.technologyarea,v.visa_status from consultant_info c, technologies t,\r\n"
			+ " visa v,users u where t.id = c.techid and \r\n"
			+ "v.id = c.visa_status and  u.userid=c.addedby and c.createddate between :startDate and :endDate and c.status In (:status) and c.addedby=:id  order by c.consultantid desc", nativeQuery = true)
	public List<ConsultantDTO> reportDrillDownForEmployee(@Param("status") List<String>  status,
			@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate,@Param("id") Long id);

	// Consultant Report Group By Query c.empcontact,
	@Query(value = "SELECT DATE(c.createddate) DateOnly,COUNT(CASE WHEN c.status='Completed' THEN 1 END)    AS Completed,\r\n"
			+ "COUNT(CASE WHEN c.status='Initiated' THEN 1 END) AS Initiated,\r\n"
			+ "COUNT(CASE WHEN c.status='Verified' THEN 1 END) AS Verified,\r\n"
			+ "COUNT(CASE WHEN c.status='Active' THEN 1 END) AS Active,\r\n"
			+ "COUNT(CASE WHEN c.status='InActive' THEN 1 END) AS InActive,\r\n"
			+ "COUNT(CASE WHEN c.status='Rejected' THEN 1 END) AS Rejected,(select count(*) from submission sub where sub.created_date between :startDate AND :endDate)AS submission,"
			+ "(select count(*) from interview i where i.created_date between :startDate AND :endDate)AS interview FROM consultant_info c where c.createddate between :startDate AND :endDate\r\n"
			+ "GROUP BY\r\n" + "DateOnly\r\n" + "ORDER BY\r\n" + "    DateOnly", nativeQuery = true)
	public List<ConsultantReportDTO> consutantReportGroupbyDate(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	@Query(value = "SELECT u.userid as id,u.pseudoname,COUNT(CASE WHEN c.status='Completed' THEN 1 END)    AS Completed,\r\n"
			+ "COUNT(CASE WHEN c.status='Initiated' THEN 1 END) AS Initiated,\r\n"
			+ "COUNT(CASE WHEN c.status='Verified' THEN 1 END) AS Verified,\r\n"
			+ "COUNT(CASE WHEN c.status='Active' THEN 1 END) AS Active,\r\n"
			+ "COUNT(CASE WHEN c.status='InActive' THEN 1 END) AS InActive,\r\n"
			+ "COUNT(CASE WHEN c.status='Rejected' THEN 1 END) AS Rejected, (select count(*) from submission sub where sub.userid = u.userid and sub.flg!='Recruiting')AS submission,"
			+ "(select count(*) from interview i where i.addedby = u.userid and i.flg!='Recruiting')AS interview FROM consultant_info c, users u where u.userid=c.addedby and c.consultantflg!='Recruiting' and c.createddate between :startDate AND :endDate\r\n"
			+ "GROUP BY\r\n" + "c.addedby\r\n" + "ORDER BY\r\n" + "c.addedby", nativeQuery = true)
	public List<ConsultantReportDTO> consutantReportGroupbyEmployee(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	@Query(value = "SELECT c.createddate,c.consultantid as id,c.consultantname,COUNT(CASE WHEN c.status='Completed' THEN 1 END)    AS Completed,\r\n"
			+ "COUNT(CASE WHEN c.status='Initiated' THEN 1 END) AS Initiated,\r\n"
			+ "COUNT(CASE WHEN c.status='Verified' THEN 1 END) AS Verified,\r\n"
			+ "COUNT(CASE WHEN c.status='Active' THEN 1 END) AS Active,\r\n"
			+ "COUNT(CASE WHEN c.status='InActive' THEN 1 END) AS InActive,\r\n"
			+ "COUNT(CASE WHEN c.status='Rejected' THEN 1 END) AS Rejected,(select count(*) from submission sub where sub.consultantid = c.consultantid and sub.flg!='Recruiting')AS submission,"
			+ "(select count(*) from interview i, submission sub  where sub.consultantid = c.consultantid and c.consultantflg!='Recruiting' and i.submissionid = sub.subid)AS interview FROM consultant_info c where  c.consultantflg!='Recruiting' and c.createddate between :startDate AND :endDate GROUP BY c.consultantid ORDER BY  c.consultantname", nativeQuery = true)
	public List<ConsultantReportDTO> consutantReportGroupbyConsultant(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	@Query(value = "select t.technologyarea,c.status,c.createddate, c.consultantname,c.consultantemail, c.contactnumber, c.currentlocation,c.linkedin,v.visa_status, q.name as qualification, c.university,c.yop , u.fullname from consultant_info c, visa v,qualification q , users u, technologies t where v.id = c.visa_status and  t.id = c.techid and u.userid=c.addedby and q.id = c.qid  and c.status='Completed' and TIMESTAMPDIFF(DAY,c.createddate,NOW()) >= 2 order by c.updateddate desc", nativeQuery = true)
	public List<ConsultantDTO> scheduleForPendingConsultant();
	
	// for report submission
	@Query(value = "select  u.userid,u.fullname,u.pseudoname,c.consultantname,c.consultantid, s.created_date as createddate ,\r\n"
			+ "s.empcontact,s.emplname,s.endclient,s.implpartner, s.position, s.projectlocation,\r\n"
			+ "s.ratetype,s.remarks,s.status,s.submissionrate,s.updateddate,v.company as vendor,s.relocationassistance,s.flg,s.substatus,\r\n"
			+ " s.subid as submissionid from  submission s, consultant_info c, users u, vendor v \r\n"
			+ "where v.id = s.vendorid and s.consultantid = c.consultantid and u.userid = s.userid and s.created_date between :startDate and :endDate and c.consultantid=:id  and c.status='Active' order by s.subid desc", nativeQuery = true)
	public List<SubmissionDTO> getsalessubmissionreport(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate,@Param("id") Long id);
	
	@Query(value = "select  u.userid,u.fullname,u.pseudoname,c.consultantname,c.consultantid, s.created_date as createddate ,\r\n"
			+ "s.empcontact,s.emplname,s.endclient,s.implpartner, s.position, s.projectlocation,\r\n"
			+ "s.ratetype,s.remarks,s.status,s.submissionrate,s.updateddate,v.company as vendor,s.relocationassistance,s.flg,s.substatus,\r\n"
			+ " s.subid as submissionid from  submission s, consultant_info c, users u, vendor v \r\n"
			+ "where v.id = s.vendorid and s.consultantid = c.consultantid and u.userid = s.userid and s.created_date between :startDate and :endDate and s.userid=:id  and c.status='Active' order by s.subid desc", nativeQuery = true)
	public List<SubmissionDTO> getsalessubmissionreportForEmployee(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate,@Param("id") Long id);
	
	// for interview
	@Query(value = " select v.company as vendor,i.flg, s.endclient,c.consultantid as consid, c.consultantname as name, i.interview_date, i.round, i.mode,  s.created_date,\r\n"
			+ " u.userid, u.fullname,u.pseudoname, i.interview_status, i.time_zone, i.id as intrid, s.subid\r\n"
			+ " from interview i, consultant_info c, submission s , users u , vendor v where i.submissionid = s.subid and\r\n"
			+ "  s.consultantid = c.consultantid and s.vendorid=v.id and i.addedby = u.userid and i.created_date between :startDate and :endDate and c.consultantid=:id and c.status='Active' order by i.updated_date desc", nativeQuery = true)
	public List<ListInterview> getInterviewDropdown(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate,@Param("id") Long id);
	
	@Query(value = " select v.company as vendor,i.flg, s.endclient,c.consultantid as consid, c.consultantname as name, i.interview_date, i.round, i.mode,  s.created_date,\r\n"
			+ " u.userid, u.fullname,u.pseudoname, i.interview_status, i.time_zone, i.id as intrid, s.subid\r\n"
			+ " from interview i, consultant_info c, submission s , users u , vendor v where i.submissionid = s.subid and\r\n"
			+ "  s.consultantid = c.consultantid and s.vendorid=v.id and i.addedby = u.userid and i.created_date between :startDate and :endDate and i.addedby =:id and c.status='Active' order by i.updated_date desc", nativeQuery = true)
	public List<ListInterview> getInterviewDropdownForEmployee(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate,@Param("id") Long id);
	

}
