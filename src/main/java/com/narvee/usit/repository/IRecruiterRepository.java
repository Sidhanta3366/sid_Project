package com.narvee.usit.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.usit.dto.VendorAssRecruiterDTO;
import com.narvee.usit.entity.Recruiter;
import com.narvee.usit.entity.VendorDetails;
import com.narvee.usit.helper.ListRecruiter;

public interface IRecruiterRepository extends JpaRepository<Recruiter, Long> {
	public List<Recruiter> findByEmail(String name);
	
	//public Recruiter findByEmail(String email);
	
	public List<Recruiter> findByEmailLike(String email);
	
	@Query("SELECT m FROM Recruiter m WHERE m.email LIKE %:email%")
	List<Recruiter> searchByTitleLike(@Param("email") String email);

	@Query(value = "select r.recruiter, r.usnumber,r.email,  r.status, r.createddate from \r\n"
			+ "recruiter  r, vendor v where r.vendorid = v.id and r.vendorid = :id", nativeQuery = true)
	public List<VendorAssRecruiterDTO> findByVendorVmsid(long id);

	@Modifying
	@Query("UPDATE Recruiter c SET c.rec_stat = 'Rejected',c.status='Rejected', c.remarks=:remarks  WHERE c.recid = :id")
	public int rejectRecruiter(@Param("remarks") String remarks, @Param("id") Long id);

	@Modifying
	@Query("UPDATE Recruiter c SET c.status = :status,c.remarks = :rem, c.updateddate=:now,rec_stat=:stat  WHERE c.recid = :id")
	public int toggleStatus(@Param("status") String status, @Param("id") Long id, @Param("rem") String rem,LocalDateTime now,String stat);

	@Modifying
	@Query("UPDATE Recruiter c SET c.rec_stat = :status, c.updateddate=:now   WHERE c.recid = :id")
	public int approveStatus(@Param("status") String status, @Param("id") Long id,LocalDateTime now);
	

	@Query(value = "select r.updateddate, r.extension,r.addedby,r.recruitertype,r.id,r.recruiter,r.remarks, r.usnumber,u.fullname,u.pseudoname, r.email, r.createddate, r.status, r.rec_stat, v.company from recruiter r , vendor v, users u where u.userid = r.addedby and v.id = r.vendorid and r.rec_stat!='Rejected' order by r.recruiter desc",countQuery = "SELECT count(*) FROM recruiter ", nativeQuery = true)
	public Page<ListRecruiter> getallrecruiter(Pageable pageable);
	
	@Query(value = "select r.updateddate,r.extension, r.addedby,r.recruitertype,r.id,r.recruiter,r.remarks, r.usnumber,u.fullname,u.pseudoname, r.email, r.createddate, r.status, r.rec_stat, v.company from recruiter r , vendor v, users u where u.userid = r.addedby and v.id = r.vendorid and (r.rec_stat!='Rejected' or r.addedby=:userid) order by r.recruiter desc",countQuery = "SELECT count(*) FROM recruiter ", nativeQuery = true)
	public Page<ListRecruiter> getallrecruiterById(long userid,Pageable pageable);
	
	@Query(value = "select r.updateddate,r.extension,r.addedby,r.recruitertype,r.id,r.recruiter,r.remarks, r.usnumber,u.fullname,u.pseudoname, r.email, r.createddate, r.status, r.rec_stat, v.company from recruiter r , vendor v, users u where u.userid = r.addedby and v.id = r.vendorid and r.rec_stat!='Rejected' AND ( r.recruitertype LIKE CONCAT('%',:keyword,'%') OR r.recruiter  LIKE CONCAT('%',:keyword,'%') OR r.usnumber  LIKE CONCAT('%',:keyword,'%') OR u.fullname LIKE CONCAT('%',:keyword,'%') OR u.pseudoname  LIKE CONCAT('%',:keyword,'%') OR r.email  LIKE CONCAT('%',:keyword,'%') OR r.status  LIKE CONCAT('%',:keyword,'%') OR r.rec_stat  LIKE CONCAT('%',:keyword,'%') OR v.company LIKE CONCAT('%',:keyword,'%')  ) order by r.updateddate desc",countQuery = "SELECT count(*) FROM recruiter ", nativeQuery = true)
	public Page<ListRecruiter> getallrecruiterPagination(@Param("keyword")String keyword,Pageable pageable);
	
	@Query(value = "select r.updateddate,r.extension, r.addedby,r.recruitertype,r.id,r.recruiter,r.remarks, r.usnumber,u.fullname,u.pseudoname, r.email, r.createddate, r.status, r.rec_stat, v.company from recruiter r , vendor v, users u where u.userid = r.addedby and v.id = r.vendorid and (r.rec_stat!='Rejected' or r.addedby=:userid) AND ( r.recruitertype LIKE CONCAT('%',:keyword,'%') OR r.recruiter  LIKE CONCAT('%',:keyword,'%') OR r.usnumber  LIKE  CONCAT('%',:keyword,'%') OR u.fullname LIKE CONCAT('%',:keyword,'%') OR u.pseudoname  LIKE CONCAT('%',:keyword,'%') OR r.email LIKE CONCAT('%',:keyword,'%') OR r.status  LIKE CONCAT('%',:keyword,'%') OR r.rec_stat  LIKE CONCAT('%',:keyword,'%') OR v.company LIKE CONCAT('%',:keyword,'%')  ) order by r.updateddate desc",countQuery = "SELECT count(*) FROM recruiter ", nativeQuery = true)
	public Page<ListRecruiter> getallrecruiterByIdPagination(@Param("keyword")String keyword,long userid,Pageable pageable);

}
