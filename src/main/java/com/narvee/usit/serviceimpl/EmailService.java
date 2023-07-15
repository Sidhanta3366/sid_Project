package com.narvee.usit.serviceimpl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.MailcapCommandMap;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.narvee.usit.dto.ConsultantDTO;
import com.narvee.usit.dto.H1bApplicantDTO;
import com.narvee.usit.entity.ConsultantInfo;
import com.narvee.usit.entity.Interview;
import com.narvee.usit.entity.Requirements;
import com.narvee.usit.entity.Submissions;
import com.narvee.usit.entity.Technologies;
import com.narvee.usit.entity.Users;
import com.narvee.usit.entity.VendorDetails;
import com.narvee.usit.helper.IRecInterviewHelper;
import com.narvee.usit.repository.IConsultantRepository;
import com.narvee.usit.repository.IQualificationRepository;
import com.narvee.usit.repository.IUsersRepository;
import com.narvee.usit.repository.IVendorRepository;
import com.narvee.usit.repository.IVisaRepository;
import com.narvee.usit.repository.InterviewRepository;
import com.narvee.usit.repository.TechnologyRepository;

import javax.mail.util.ByteArrayDataSource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Configuration
public class EmailService {
	// commented for mail error
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private IUsersRepository userRepo;

	@Autowired
	private IConsultantRepository consultantrepo;

	@Autowired
	private InterviewRepository interviewRepo;

	@Autowired
	private IVendorRepository ivendorRepo;

	@Autowired
	private IVisaRepository visarepo;

	@Autowired
	private IQualificationRepository qualirepo;

	@Value("${requirement-ccmail}")
	private String[] requirementccmails;

	private String mailusername = "xxxxx@narveetech.com";

	@Value("${reg-cc-mail}")
	private String cc_mail;

	@Value("${no-reply}")
	private String no_reply;

	public void resetlinkmail(String username, String recipientEmail, String link)
			throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		message.addRecipient(RecipientType.CC, new InternetAddress(mailusername));
		helper.setTo(recipientEmail);
		String subject = "Here's the link to reset your password";
		String content = "<p>Hi " + username + ",</p>  <p> Your request for change password</p>"
				+ "<p>Please click on below link to reset your password "
				+ "<br><a href='http://69.216.19.140/usitats/#/change-password'>Click here to Reset</a> ";
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}

	// sending regarding registration success message
	public void employeeRegistarionMail(String email, String username)
			throws MessagingException, UnsupportedEncodingException {
		logger.info("!!! inside class: EmailService, !! method: employeeRegistarionMail");
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setTo(email);
		helper.setCc(cc_mail);
		String subject = "Registartion Successful with Narvee USIT Portal";
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(
				"<head><title>Registration Successful Message Example</title><style>body {background: #d2d6da;} #card {text-align: center;font-family: 'Source Sans Pro', sans-serif;}")
				.append(" #upper-side {padding: 2em;background-color: #8BC34A;color: #fff;border-top-right-radius: 8px;border-top-left-radius: 8px;}")
				.append("#checkmark {font-weight: lighter;fill: #fff;margin: -3.5em auto auto 20px;} ")
				.append("#status { letter-spacing: 2px;margin-right: 40px;}")
				.append("#lower-side {padding: 2em 2em 5em 2em;background: #555b5e;}")
				.append(" #message {color: #fff;}")
				.append("#contBtn { position: relative;top: 1.5em;text-decoration: none;background: #8bc34a;")
				.append("color: #fff; padding: .8em 3em;border-radius: 25px;}</style></head>")
				.append("<body><h3>" + username + " your account has created successfully</h3>").append("User Name :")
				.append(email).append("  and Password : Narvee123$").append("<br>")
				.append("<section><div id='card' class=\'animated fadeIn\'>").append("<div id='upper-side'>")
				.append("<circle fill='none' stroke='#ffffff' stroke-width='5' stroke-miterlimit='10' cx='109.486' cy='104.353' r='32.53' />")
				.append("</svg><h3 id='status'>SUCCESS</h3></div><div id='lower-side'><p id='message' ><p style='color:#fff'>Congratulations, your account has successfully created.</p>")
				.append("</p><a href='http://69.216.19.140/usitats' id='contBtn'>Click to login</a></div></div></section></body><br><br>")
				.append("<strong ><u> Thanks and regards </u><br><br>Narvee Technologies pvt ltd.<br>1333 Corporate Dr, Suite#102,<br>Irving, Texas, 75038</strong><br>")
				.append(" Email : <strong>sree@narveetech.com</strong>");
		helper.setSubject(subject);
		helper.setText(stringBuilder.toString(), true);
		mailSender.send(message);
	}

	// sending mail for consutant entry
	public void consultantEntry(ConsultantInfo consultant) throws MessagingException, UnsupportedEncodingException {
		logger.info("!!! inside class: EmailService, !! method: consultantEntry");
		String name = consultant.getConsultantname();
		String number = consultant.getContactnumber();
		String email = consultant.getConsultantemail();
		String currentLocation = consultant.getCurrentlocation();
		String linkedin = consultant.getLinkedin();
		long visaid = consultant.getVisa().getVid();
		String visa = visarepo.findById(visaid).get().getVisastatus();
		long qid = consultant.getQualification().getId();
		String education = qualirepo.findById(qid).get().getName();
		String technology = consultant.getJobtitle();
		String university = consultant.getUniversity();									
		String yop = consultant.getYop();
		String status = consultant.getStatus();
		StringBuilder stringBuilder = new StringBuilder();
		Users addebyInfo = userRepo.findById(1L).get();
		String frommail = addebyInfo.getEmail();
		String username = addebyInfo.getPseudoname();
		String employementType = consultant.getRatetype();
		if (consultant.getConsultantflg().equalsIgnoreCase("Recruiting")) {
			username = addebyInfo.getFullname();
		}

		String rolename = addebyInfo.getRole().getRolename();
		String cnumber = addebyInfo.getPersonalcontactnumber().getInternationalNumber();
		String designation = addebyInfo.getDesignation();
		String subject = visa + " (" + name + ") candidate has added in Narvee USIT Portal (" + status + ")";

		String heading = "I have added " + visa + " consultant in  Narvee USIT Portal";
		if (consultant.getUpdatedby() == null) {
			addebyInfo = userRepo.findById(consultant.getAddedby().getUserid()).get();
			frommail = addebyInfo.getEmail();
			username = addebyInfo.getPseudoname();
			subject = visa + " (" + name + ") candidate has added in Narvee USIT Portal (" + status + ")";
			heading = "I have added " + visa + " consultant in  Narvee USIT Portal";
			if (consultant.getConsultantflg().equalsIgnoreCase("Recruiting")) {
				username = addebyInfo.getFullname();
				subject = name + " - " + technology + "(" + consultant.getExperience() + "yrs) has added by "
						+ username;
				heading = "Team i have  added " + name + " for " + employementType + " Requirements";
			}
			rolename = addebyInfo.getRole().getRolename();
			designation = addebyInfo.getDesignation();

			cnumber = addebyInfo.getPersonalcontactnumber().getInternationalNumber();
		} else {
			addebyInfo = userRepo.findById(consultant.getUpdatedby().getUserid()).get();
			frommail = addebyInfo.getEmail();
			username = addebyInfo.getPseudoname();
			subject = visa + " (" + name + ")  candidate has modified by " + username;
			heading = "I have modified the consultant please have a look";
			if (consultant.getConsultantflg().equalsIgnoreCase("Recruiting")) {
				username = addebyInfo.getFullname();
				subject = name + " - " + technology + "(" + consultant.getExperience() + "yrs) has  modified by "
						+ username;
			}
			rolename = addebyInfo.getRole().getRolename();
			designation = addebyInfo.getDesignation();

			cnumber = addebyInfo.getPersonalcontactnumber().getInternationalNumber();
		}
		if (linkedin != null) {
			linkedin = "https://www.linkedin.com/in/" + linkedin;
		}
		stringBuilder.append("<html>").append("<head>").append("<style>").append(
				".styled-table { border-collapse: collapse; font-size: 0.9em; font-family: sans-serif; border: 1px solid #dddddd;}")
				.append(".styled-table thead tr { background-color: #009879; color: #ffffff; }")
				.append(".styled-table th, .styled-table td { padding: 12px 15px; }")
				.append(".styled-table tbody tr { border-bottom: 1px solid #dddddd; }")
				.append(".styled-table tbody tr:nth-of-type(even) { background-color: #f3f3f3; }")
				.append(".styled-table tbody tr:last-of-type { border-bottom: 2px solid #009879; }")
				.append(".styled-table tbody tr td { border: 1px solid black;}")
				.append(".styled-table tbody tr.active-row { font-weight: bold; color: #009879;}").append("</style>")
				.append("</head>").append("<body>").append("<p>Hi Team,<br>").append(heading)
				.append("<table class='styled-table' width='100%'>").append("<thead>").append("<tr>")
				.append("<th colspan='2'>Consultant Details</th>").append("</tr>").append("</thead>").append("<tbody>")
				.append("<tr >").append("<td width='30%'><b>Legal name: <br>(First Middle Last name)</b> </td>")
				.append("<td>" + name + "</td>").append("</tr>").append("<tr><td><b>Contact Number :</b> </td>")
				.append("<td>" + number + "</td>").append("</tr>").append("<tr><td><b>Email :</b> </td>")
				.append("<td>" + email + "</td>").append("</tr>")
				.append("<tr><td><b>Current location (City and State) :</b> </td>")
				.append("<td>" + currentLocation + "</td>").append("</tr>").append("<tr><td><b>LinkedIn ID:</td>")
				.append("<td><a href='" + linkedin + "' >" + linkedin + "</a></td></tr>")
				.append("<tr><td><b>Work Authorization</td>").append("<td>" + visa + "</td></tr>")
				.append("<tr><td><b>Education Details /University / Year:</td>")
				.append("<td>" + education + "/" + university + "/" + yop + "</td></tr>")
				.append("<tr><td><b>Technology :</td>").append("<td>" + technology + "</td></tr>")
				.append("<tr><td><b>Status :</td>").append("<td>" + status + "</td></tr>").append("</tbody>")
				.append("</table>").append("</body>").append("</html>").append("<br>").append("<h3>Regards,</h3>")
				.append("<strong>" + username + ",<br>").append(designation + " | |Narvee Tech Inc<br>")
				.append("1333 Corporate Dr, Suite#102,").append("<br>Irving, Texas, 75038<br>")
				.append("Email : " + frommail + "<br>").append("Conatct Number:" + cnumber)
				.append("<br>www.narveetech.com</strong>");
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true); //
		String[] allusers = {};
		String shortMessage = "Profile added in Pre-sales";
		if (rolename.equalsIgnoreCase("Employee")) {
			long tlid = addebyInfo.getTeamlead();
			long mangerid = addebyInfo.getManager();
			allusers = userRepo.executivemail(tlid, mangerid);
		} else if (rolename.equalsIgnoreCase("Team Lead")) {
			long mangerid = addebyInfo.getManager();
			long userid = addebyInfo.getUserid();
			allusers = userRepo.teamleadmails(userid, mangerid);
		} else if (designation.equalsIgnoreCase("Manager")) {
			long mangerid = addebyInfo.getUserid();
			allusers = userRepo.teamleadmail(mangerid);
		} else {
			allusers = new String[] { "snayak3366@gmail.com" }; // userRepo.findsalesrecruiterMail();
		}
		if (allusers.length != 0) {
			helper.setCc(allusers);
		}
		helper.setBcc("snayak3366@gmail.com");
		message.setSubject(subject);
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(stringBuilder.toString(), "text/html");
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		message.setContent(multipart);
		mailSender.send(message);

		logger.info("!!! inside class: EmailService, !! method: consultant entry mailing send and update");
	}

	ZoneId newYork = ZoneId.of("America/Chicago");
	LocalDateTime now = LocalDateTime.now(newYork);

	final static DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	public boolean resetpasswordmail(String email, String password, String fullname)
			throws MessagingException, UnsupportedEncodingException {
		/*
		 * kiran commented MimeMessage message = mailSender.createMimeMessage();
		 * MimeMessageHelper helper = new MimeMessageHelper(message);
		 * message.addRecipient(RecipientType.BCC, new InternetAddress(mailusername));
		 * helper.setFrom(mailusername, "Password updated for Narvee USIT Protal");
		 * helper.setTo(mailusername); String subject = fullname +
		 * " has  updated his  Narvee USIT Portal password"; String content =
		 * "<p>Dear Admin ,</p>  <p> " + fullname + " updated his password</p>" +
		 * "<p>Here is the new Password  : " + password + "</p>";
		 * helper.setSubject(subject); helper.setText(content, true);
		 * mailSender.send(message); return true;
		 */
		return true;
	}

	@Autowired
	private TechnologyRepository techRepo;

	// requirements entry and update email
	public void sendrequirementMail(String[] to, Requirements requirements) {
		// usermails, requirementraisedby, subject, requirements.getJobdescription()
		logger.info("!!! inside class: EmailService, !! method: requirement mailing send and update");
		Users user = userRepo.findById(requirements.getUsers().getUserid()).get();
		String frommail = user.getEmail();
		String username = user.getFullname();
		String designation = user.getDesignation();
		String cnumber = user.getPersonalcontactnumber().getInternationalNumber();
		String subject = requirements.reqnumber + " - " + requirements.getJobtitle() + " At "
				+ requirements.getLocation();
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom(frommail, "Here is the new Requirement");
			String body = requirements.getJobdescription();
			helper.setCc(to);
			Technologies tech = techRepo.findById(requirements.getTechnology().getId()).get();
			helper.setSubject(subject);
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("<html><head>")
					.append("<style>.styled-table tr {border: 1px solid black;border-collapse: collapse;}</style>")
					.append("</head><body>").append("Dear Team, <br>")
					.append("Below is the new Requirement please go through it<br>")
					.append("<br><table class='styled-table'><tr><td >Requirement Number </td><td>: ")
					.append(requirements.reqnumber).append("</td></tr>").append("<tr><td>Publish Date </td><td>: ")
					.append(requirements.getPostedon()).append("</td></tr>")
					.append("<tr><td>Implementation Partner/Vendor  </td><td>: ").append(requirements.getVendor())
					.append("</td></tr>").append("<tr><td>Job Title  </td><td>: ").append(requirements.getJobtitle())
					.append("</td></tr>").append("<tr><td>Technology </td><td>: ").append(tech.getTechnologyarea())
					.append("</td></tr>").append("<tr><td>Project Location </td><td>: ")
					.append(requirements.getLocation()).append("</td></tr>").append("<tr><td>Experience  </td><td>: ")
					.append(requirements.getJobexperience()).append("</td></tr>")
					.append("<tr><td>Employment Type  </td><td>: ").append(requirements.getEmploymenttype())
					.append("</td></tr>").append("<tr><td>Project Duration  </td><td>: ")
					.append(requirements.getDuration()).append("</td></tr></table>").append("<pre>").append(body)
					.append("</pre>").append("</body>").append("<html>").append("<br>").append("<h3>Regards,</h3>")
					.append("<strong>" + username + ",<br>").append(designation + " | |Narvee Tech Inc<br>")
					.append("1333 Corporate Dr, Suite#102,").append("<br>Irving, Texas, 75038<br>")
					.append("Email : " + frommail + "<br>").append("Conatct Number:" + cnumber)
					.append("<br>www.narveetech.com</strong>");
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(stringBuilder.toString(), "text/html");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			mimeMessage.setContent(multipart);
			mailSender.send(mimeMessage);
			// helper.setText(stringBuilder.toString(), true);
			// mailSender.send(mimeMessage);
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}

		logger.info("!!! inside class: EmailService, !! method: requirements mailing send and update");
	}

	// submisiion entry mail update
	public void sendsubmissionEmail(Submissions submission) throws MessagingException, UnsupportedEncodingException {
		logger.info("!!! inside class: EmailService, !! method: submission mailing send and update");
		String from = "snayak3366@gmail.com";
		String username = "Admin";
		String designation = "Admin";
		String subject = "Here's  the new submission status";
		String cnumber = "";
		long userid = 0;
		String rolename = "Admin";
		Users submittedBy = null;
		if (submission.getSubmissionid() == null) {
			submittedBy = userRepo.findById(submission.getUser().getUserid()).get();
			from = submittedBy.getEmail();
			username = submittedBy.getPseudoname();
			designation = submittedBy.getDesignation();
			userid = submittedBy.getUserid();
			cnumber = submittedBy.getPersonalcontactnumber().getInternationalNumber();
			subject = "Here's the submission status";
			rolename = submittedBy.getRole().getRolename();
		} else {
			submittedBy = userRepo.findById(submission.getUpdatedby()).get();
			from = submittedBy.getEmail();
			username = submittedBy.getPseudoname();
			designation = submittedBy.getDesignation();
			cnumber = submittedBy.getPersonalcontactnumber().getInternationalNumber();
			subject = "Here's the submission follow up status";
			rolename = submittedBy.getRole().getRolename();
		}
		String[] allusers = {};
		if (submission.getFlg().equalsIgnoreCase("sales")) {
			allusers = userRepo.salesTeamEmails();
		} else {
			allusers = userRepo.findrecruiterMail();
		}
		ConsultantInfo consultant = consultantrepo.findById(submission.getConsultant().getConsultantid()).get();
		String name = consultant.getConsultantname();
		String number = consultant.getContactnumber();
		String email = consultant.getConsultantemail();
		String currentLocation = consultant.getCurrentlocation();
		String linkedin = consultant.getLinkedin();
		String visa = consultant.getVisa().getVisastatus();
		String ppnumber = consultant.getPassportnumber();
		String education = consultant.getQualification().getName() + "/" + consultant.getUniversity() + "  ("
				+ consultant.getYop() + ")";
		String projectjoining = consultant.getProjectavailabity();
		String techskill = consultant.getTechnology().getTechnologyarea();
		String client = submission.getEndclient();
		String rate = submission.getSubmissionrate();
		String location = submission.getProjectlocation();

		VendorDetails vms = ivendorRepo.findById(submission.getVendor().getVmsid()).get();
		String company = vms.getCompany();
		String cmptype = vms.getCompanytype();
		String vendortype = vms.getVendortype();
		String ratetype = submission.getRatetype();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<html>").append("<head>").append("<style>").append(
				".styled-table { border-collapse: collapse; font-size: 0.9em; font-family: sans-serif; border: 1px solid #dddddd;}")
				.append(".styled-table thead tr { background-color: #009879; color: #ffffff; }")
				.append(".styled-table th, .styled-table td { padding: 12px 15px; }")
				.append(".styled-table tbody tr { border-bottom: 1px solid #dddddd; }")
				.append(".styled-table tbody tr:nth-of-type(even) { background-color: #f3f3f3; }")
				.append(".styled-table tbody tr:last-of-type { border-bottom: 2px solid #009879; }")
				.append(".styled-table tbody tr td { border: 1px solid black;}")
				.append(".styled-table tbody tr.active-row { font-weight: bold; color: #009879;}").append("</style>")
				.append("</head>").append("<body>").append("<p>Dear Team,<br>").append("I have submitted ")
				.append(name + "(" + techskill + ")").append(" for " + client).append(" at " + location)
				.append(", " + rate + "$/per Hr").append(" On " + ratetype).append(".<br>")
				.append("<table class='styled-table' width='100%'>").append("<thead>").append("<tr>")
				.append("<th colspan='2'>Submission Details</th>").append("</tr>").append("</thead>").append("<tbody>")
				.append("<tr >").append("<td width='30%'><b>Legal name: <br>(First Middle Last name)</b> </td>")
				.append("<td>" + name + "</td>").append("</tr>").append("<tr><td><b>Contact Number :</b> </td>")
				.append("<td>" + number + "</td>").append("</tr>").append("<tr><td><b>Email :</b> </td>")
				.append("<td>" + email + "</td>").append("</tr>")
				.append("<tr><td><b>Current location (City and State) :</b> </td>")
				.append("<td>" + currentLocation + "</td>").append("</tr>").append("<tr><td><b>LinkedIn ID:</td>")
				.append("<td><a href='" + linkedin + "' >" + linkedin + "</a></td></tr>")
				.append("<tr><td><b>Work Authorization with Validity:</td>").append("<td>" + visa + "</td></tr>")
				.append("<tr><td><b>Education Details /University / Year:</td>")
				.append("<td>" + education + "</td></tr>").append("<tr><td><b>Client :</td>")
				.append("<td>" + client + "</td></tr>").append("<tr><td><b>Project Location :</td>")
				.append("<td>" + location + "</td></tr>").append("<tr><td><b>Submission Rate ($ Per Hour):</td>")
				.append("<td>" + rate + "</td></tr>").append("<tr><td><b>Contract Type:</td>")
				.append("<td>" + ratetype + "</td></tr>").append("<tr><td ><b>Vendor</td>")
				.append("<td>" + company + "</td></tr>").append("</tbody>").append("</table>").append("</body>")
				.append("</html>").append("<br>").append("<h3>Regards,</h3>").append("<strong>" + username + ",<br>")
				.append(designation + " | |Narvee Tech Inc<br>").append("1333 Corporate Dr, Suite#102,")
				.append("<br>Irving, Texas, 75038<br>").append("Email : " + from + "<br>")
				.append("Conatct Number:" + cnumber).append("<br>www.narveetech.com</strong>");

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			if (allusers.length != 0) {
				helper.setBcc(allusers);
			}
			//"raghav@narveetech.com", "shekar@narveetech.com", "srikanth@narveetech.com",
			helper.setCc(new String[] { 
					"snayak3366@gmail.com" });
			helper.setFrom(from, "Submission followUp mail");
			helper.setSubject(subject);
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(stringBuilder.toString(), "text/html");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			mailSender.send(message);
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}

		logger.info("!!! inside class: EmailService, !! method: submission mailing send and update");
	}

	// define somewhere the icalendar date format snayak3366@gmail.com
	private static SimpleDateFormat iCalendarDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmm'00'");

	// interview entry mail
	public void interviewFollowupmail(Interview interviews) throws MessagingException, UnsupportedEncodingException {
		logger.info("!!! inside class: EmailService, !! method: interviewFollowupmail mailing send and update");

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
		String from = "snayak3366@gmail.com";
		String username = "Narvee Tech Inc";
		String designation = "Admin";
		String cnumber = "";
		String subject = "Here's the submission status";
		String topic = "";
		IRecInterviewHelper ent = interviewRepo.getinfo(interviews.getIntrid());
		LocalDateTime intrdate = ent.getInterview_date();
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(intrdate.getYear(), intrdate.getMonthValue() - 1, intrdate.getDayOfMonth(), intrdate.getHour(),
				intrdate.getMinute(), intrdate.getSecond());
		Date intdate = calendar.getTime();

		if (interviews.getUpdatedby() == 0) {
			Users user = userRepo.findById(interviews.getUsers().getUserid()).get();
			from = user.getEmail();
			username = user.getPseudoname();
			designation = user.getDesignation();
			cnumber = user.getPersonalcontactnumber().getInternationalNumber();
			subject = "Invitation : " + ent.getJobtitle() + " - " + ent.getName() + " - " + ent.getRound() + " - "
					+ intdate;
			topic = ent.getJobtitle() + " - " + ent.getName() + " - " + ent.getRound() + " round ";
		} else {
			Users user = userRepo.findById(interviews.getUpdatedby()).get();
			from = user.getEmail();
			username = user.getPseudoname();
			designation = user.getDesignation();
			cnumber = user.getPersonalcontactnumber().getInternationalNumber();
			subject = "Invitation : " + ent.getJobtitle() + " - " + ent.getName() + " - " + ent.getRound() + " - "
					+ intdate;
			topic = ent.getJobtitle() + " - " + ent.getName() + " - " + ent.getRound() + " round ";
		} //
		String[] allusers = {};
		if (interviews.getFlg().equalsIgnoreCase("sales")) {
			allusers = userRepo.salesTeamEmails();
		} else {
			allusers = userRepo.findrecruiterMail();
		}

		String name = ent.getName();
		boolean flg = false;
		StringBuilder stringBuilder = new StringBuilder();
		if (interviews.getInterviewstatus().equals("Schedule")) {
			if (interviews.getRound().equals("First")) {
				flg = true;
				subject = "Invitation : for First round of interview - " + ent.getJobtitle() + " - " + ent.getName()
						+ " - " + ent.getRound() + " round - " + intdate;
				topic = ent.getJobtitle() + " - " + ent.getName() + " - " + ent.getRound() + " round ";
				stringBuilder.append("<p>Hi Team ,<br>")
						.append(" Interview has " + ent.getInterview_status() + " for " + ent.getRound() + " round  at "
								+ intdate + " <b>" + ent.getTimezone()+"</b>")
						.append(" for " + ent.getJobtitle() + " position ,")
						.append(" Mode of interview <b>" + ent.getMode() + "</b><br>")
						.append("Vendor / Implementation Partner :" + ent.getVendor()).append("<br>");
			} else if (interviews.getRound().equals("Second")) {
				flg = true;
				subject = "Invitation : for Second round of interview - " + ent.getJobtitle() + " - " + ent.getName()
						+ " - " + ent.getRound() + " - " + intdate;
				topic = ent.getJobtitle() + " - " + ent.getName() + " - " + ent.getRound() + " round ";
				stringBuilder.append("<p>Hi  Team ,<br>")
						.append(name + " Profile has selected for " + ent.getRound()
								+ "> round at and interview Schedule at " + intdate + " <b>" + ent.getTimezone()+"</b>")
						.append(" for " + ent.getJobtitle() + " position ,")
						.append(" Mode of interview <b>" + ent.getMode() + "</b><br>")
						.append("Vendor / Implementation Partner :" + ent.getVendor()).append("<br>");
			}

			else if (interviews.getRound().equals("Third")) {
				flg = true;
				topic = ent.getJobtitle() + " - " + ent.getName() + " - " + ent.getRound() + " round ";
				subject = "Invitation : for third round of interview " + ent.getJobtitle() + " - " + ent.getName()
						+ " - " + ent.getRound() + " - " + intdate;
				stringBuilder.append("<p>Hi Team ,<br>")
						.append(name + " Profile has selected for " + ent.getRound()
								+ " round and interview Schedule at " + intdate + " <b>" + ent.getTimezone()+"</b>")
						.append(" for " + ent.getJobtitle() + " position ,")
						.append(" Mode of interview <b>" + ent.getMode() + "</b><br>")
						.append("Vendor / Implementation Partner :" + ent.getVendor()).append("<br>");
			}

		} else {
			flg = false;
			subject = "interview follow up email : " + ent.getJobtitle() + " - " + ent.getName() + " - "
					+ ent.getReqnumber();
			topic = ent.getJobtitle() + " - " + ent.getName() + " - " + ent.getRound() + " round ";
			stringBuilder.append("<p>Hi Team ,<br>").append(name + " profile has been " + ent.getInterview_status())
					.append(" for " + ent.getJobtitle() + " position for ")
					//.append("Requirement Number :" + ent.getReqnumber()).append(",")
					.append("Vendor / Implementation Partner :" + ent.getVendor()).append("<br>");
		}

		stringBuilder.append("<br>").append("<h3>Regards,</h3>").append("<strong>" + username + ",<br>")
				.append(designation + " | |Narvee Tech Inc<br>").append("1333 Corporate Dr, Suite#102,")
				.append("<br>Irving, Texas, 75038<br>").append("Email : " + from + "<br>")
				.append("Conatct Number:" + cnumber).append("<br>www.narveetech.com</strong>");

		// register the handling of text/calendar mime type
		MailcapCommandMap mailcap = (MailcapCommandMap) MailcapCommandMap.getDefaultCommandMap();
		mailcap.addMailcap("text/calendar;; x-java-content-handler=com.sun.mail.handlers.text_plain");
		Multipart multipart = new MimeMultipart("alternative");
		// part 1, html text // BodyPart messageBodyPart;
		MimeBodyPart descriptionPart = new MimeBodyPart();
		String content = stringBuilder.toString();
		descriptionPart.setContent(content, "text/html; charset=utf-8");
		BodyPart messageBodyPart = descriptionPart;
		multipart.addBodyPart(messageBodyPart);
		if (flg) {
			try {
				BodyPart calendarPart = buildCalendarPart(ent.getInterview_date(), topic, content,from);
				multipart.addBodyPart(calendarPart);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			if (allusers.length != 0) {
				 helper.setBcc(allusers);
			} 
				// consultantmail // helper.setTo(consultantmail); //"raghav@narveetech.com","shekar@narveetech.com",
			//helper.setCc(requirementccmails);
			helper.setFrom(from, "Interview " + ent.getInterview_status() + " mail");
			helper.setSubject(subject); //
			helper.setCc(new String[]{"snayak3366@gmail.com"});
			helper.setText(stringBuilder.toString(), true);
			mimeMessage.setContent(multipart);
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}
		mailSender.send(mimeMessage);
		logger.info(
				"!!! inside class: EmailService, !! method: interviewFollowupmail mailing send and update after sent");
	}

	private BodyPart buildCalendarPart(LocalDateTime dt, String subject, String body,String raisedBy) throws Exception {
		BodyPart calendarPart = new MimeBodyPart();
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(dt.getYear(), dt.getMonthValue() - 1, dt.getDayOfMonth(), dt.getHour(), dt.getMinute(),
				dt.getSecond());
		Date dd = calendar.getTime();
		// check the icalendar spec in order to build a more complicated meeting request
		String calendarContent = "BEGIN:VCALENDAR\n" + "METHOD:REQUEST\n" + "PRODID: BCP - Meeting\n" + "VERSION:2.0\n"
				+ "BEGIN:VEVENT\n" + "DTSTAMP:" + iCalendarDateFormat.format(dd) + "\n" + "DTSTART:"
				+ iCalendarDateFormat.format(dd) + "\n" + "DTEND:" + iCalendarDateFormat.format(dd) + "\n" + "SUMMARY:"
				+ subject + "\n" + "UID:324\n"
				+ "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:"+raisedBy+"\n" +
				// "ORGANIZER:MAILTO:snayak3366@gmail.com\n" +
				// "LOCATION:on the net\n" +
				"DESCRIPTION:" + body + "\n" + "SEQUENCE:0\n" + "PRIORITY:5\n" + "CLASS:PUBLIC\n" + "STATUS:CONFIRMED\n"
				+ "TRANSP:OPAQUE\n" + "BEGIN:VALARM\n" + "ACTION:DISPLAY\n" + "DESCRIPTION:REMINDER\n"
				+ "TRIGGER;RELATED=START:-PT00H15M00S\n" + "END:VALARM\n" + "END:VEVENT\n" + "END:VCALENDAR";

		calendarPart.addHeader("Content-Class", "urn:content-classes:calendarmessage");
		calendarPart.setContent(calendarContent, "text/calendar;method=CANCEL");
		return calendarPart;
	}

	public void unlockUserMail(Users user) throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(mailusername, "User unlocked Successfully");
		message.addRecipient(RecipientType.CC, new InternetAddress(mailusername));
		helper.setTo("snayak3366@gmail.com");
		String subject = "User unlocked Successfully";
		String content = "<p>Dear Sir ,</p><p>" + user.getFullname() + " account has unlocked</p>"
				+  "</p><p> Remarks for unlock " + user.getRemarks(); 									//"<p>Last login time " + user.getLastLogin() +
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}

	public void pendingConsultantInfo(List<ConsultantDTO> entity) {
		String formattedString = now.format(CUSTOM_FORMATTER);

		StringBuilder heading = new StringBuilder();
		heading.append("<html>").append("<head>").append("<style>").append(
				".styled-table { border-collapse: collapse; font-size: 0.9em; font-family: sans-serif; border: 1px solid #dddddd;}")
				.append(".styled-table thead tr { background-color: #009879; color: #ffffff; }")
				.append(".styled-table th, .styled-table td { padding: 12px 15px; }")
				.append(".styled-table tbody tr td { border: 1px solid black;}")
				.append(".styled-table tbody tr { border-bottom: 1px solid #dddddd; }")
				.append(".styled-table tbody tr:nth-of-type(even) { background-color: #f3f3f3; }")
				.append(".styled-table tbody tr:last-of-type { border-bottom: 2px solid #009879; }")
				.append(".styled-table tbody tr.active-row { font-weight: bold; color: #009879;}").append("</style>")
				.append("</head>").append("<body>").append("<p>Hi Team,<br>")
				.append("<p>These are the below consultants newly added into portal please review and approve them</p>")
				.append("<table class='styled-table' width='100%'>").append("<thead>").append("<tr>")
				.append("<th>Created Date</th><th>(First Middle Last name)</th><th>Contact Number</th><th>Email</th><th>Current location (City and State) </th><th>LinkedIn ID</th>"
						+ "<th>Work Authorization</th><th>Education Details /University / Year</th><th>Technology</th><th>Status</th></tr>")
				.append("</thead>");
		entity.forEach(consultant -> {
			String name = consultant.getConsultantname();
			String number = consultant.getContactnumber();
			String email = consultant.getConsultantemail();
			String currentLocation = consultant.getCurrentlocation();
			String linkedin = consultant.getLinkedin();
			String visa = consultant.getVisa_status();
			String education = consultant.getQualification();
			String technology = consultant.getTechnologyarea();
			String university = consultant.getUniversity();
			String yop = consultant.getYop();
			LocalDateTime createddate = consultant.getCreateddate();
			String Staringcreateddate = createddate.format(CUSTOM_FORMATTER);
			String status = consultant.getStatus();
			heading.append("<tbody><tr><td style='width:13%'>" + Staringcreateddate + "</td><td>" + name + "</td>"
					+ "<td>" + number + "</td>" + "<td>" + email + "</td>" + "<td>" + currentLocation + "</td>" + "<td>"
					+ linkedin + "</td>" + "<td>" + visa + "</td>" + "<td>" + education + "/" + university + "/" + yop
					+ "</td>" + "<td>" + technology + "</td>" + "<td>" + status + "</td></tr></tbody>");
		});
		heading.append("</table>");
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setBcc("snayak3366@gmail.com");
			//helper.setCc(new String[] { "srikanth@narveetech.com", "mon@narveetech.com" });
			String subject = "Here's the pending consultant details " + formattedString;
			helper.setSubject(subject);
			helper.setText(heading.toString(), true);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		mailSender.send(message);
	}

	public void gentleReminderTologin(String[] bccmails) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		String subject = "Gentle Reminder to Login into Narvee Portal";
		StringBuilder builder = new StringBuilder();
		builder.append("<p>Hi User,</p>").append("<p>Please login to New Narvee Usit Portal");
		try {
			helper.setTo("snayak3366@gmail.com");
			helper.setSubject(subject);
			helper.setText(builder.toString(), true);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		mailSender.send(message);
	}

	// Pending H1bApplicants list for file uploading's
	public void pendingH1BApplicantsUploadInfo(List<H1bApplicantDTO> entity) {
		String formattedString = now.format(CUSTOM_FORMATTER);

		StringBuilder heading = new StringBuilder();
		heading.append("<html>").append("<head>").append("<style>").append(
				".styled-table { border-collapse: collapse; font-size: 0.9em; font-family: sans-serif; border: 1px solid #dddddd;}")
				.append(".styled-table thead tr { background-color: #009879; color: #ffffff; }")
				.append(".styled-table th, .styled-table td { padding: 12px 15px; }")
				.append(".styled-table tbody tr td { border: 1px solid black;}")
				.append(".styled-table tbody tr { border-bottom: 1px solid #dddddd; }")
				.append(".styled-table tbody tr:nth-of-type(even) { background-color: #f3f3f3; }")
				.append(".styled-table tbody tr:last-of-type { border-bottom: 2px solid #009879; }")
				.append(".styled-table tbody tr.active-row { font-weight: bold; color: #009879;}").append("</style>")
				.append("</head>").append("<body>").append("<p>Hi Team,<br>")
				.append("<p>These are the list of H1BApplicants who are not yet uploaded their Files even after 3 day's of data submmission</p>")
				.append("<table class='styled-table' width='100%'>").append("<thead>").append("<tr>")
				.append("<th>Created date</th>Beneficiary name</th><th>Contact number</th><th>Email</th><th>Receipt number</th><th>Petitioner</th>"
						+ "<th>EVerify</th><th>I9 Doc</th></tr>")
				.append("</thead>");
		entity.forEach(h1bapplicant -> {
			Long applicantId = h1bapplicant.getApplicantid();
			String name = h1bapplicant.getEmployeename();
			String number = h1bapplicant.getContactnumber();
			String email = h1bapplicant.getEmail();
			String receiptNumber = h1bapplicant.getReceiptnumber();
			String petitioner = h1bapplicant.getPetitioner();
			String noticeType = h1bapplicant.getNoticetype();
			LocalDateTime createdDate = h1bapplicant.getCreateddate();
			// LocalDateTime updatedDate = h1bapplicant.getUpdateddate();
			String createdtfrmt = createdDate.format(CUSTOM_FORMATTER);
			String everify = h1bapplicant.getEverifydoc();
			String i9doc = h1bapplicant.getI9doc();
			heading.append("<tbody>" + "<tr>" + "<td>" + createdtfrmt + "</td>" + "<td>" + name + "</td>" + "<td>"
					+ number + "</td>" + "<td>" + email + "</td>" + "<td>" + receiptNumber + "</td>" + "<td>"
					+ petitioner + "</td>" + "<td>" + everify + "</td>" + "<td>" + i9doc + "</td>" + "</tr>"
					+ "</tbody>");
		});

		heading.append("</table>");
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			//"hr@narvee.com",
			helper.setFrom("snayak3366@gmail.com", "Pending h1bapplicants files upload list");
			helper.setCc(new String[] {  "snayak3366@gmail.com" });
			String subject = "Here's the pending H1bApplicant's details " + formattedString;
			helper.setSubject(subject);
			helper.setText(heading.toString(), true);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		mailSender.send(message);
	}

	// requirements entry and update email
	public void VendorInitiationMail(VendorDetails vms) {
		// usermails, requirementraisedby, subject, requirements.getJobdescription()
		Users user = userRepo.findById(vms.getUser().getUserid()).get();
		String frommail = user.getEmail();
		String username = user.getFullname();
		String designation = user.getDesignation();
		String cnumber = user.getPersonalcontactnumber().getInternationalNumber();
		String subject = vms.getCompany() + " added in VMS Please review it";
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom(frommail, "Here is the new Vendor");
			helper.setCc("snayak3366@gmail.com");
			helper.setSubject(subject);
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("<html><head>")
					.append("<style>.styled-table tr {border: 1px solid black;border-collapse: collapse;}</style>")
					.append("</head><body>").append("Hi Sir, <br>")
					.append("I have added new vendor in portal please review it<br>")
					.append("<br><table class='styled-table'><tr><td >Company Name </td><td>: ")
					.append(vms.getCompany()).append("</td></tr>").append("<tr><td>Location  </td><td>: ")
					.append(vms.getHeadquerter()).append("</td></tr>").append("<tr><td>Vendor Type </td><td>: ")
					.append(vms.getVendortype()).append("</td></tr>").append("<tr><td>Tyre Type </td><td>: ")
					.append(vms.getTyretype()).append("</td></tr>").append("<tr><td>Fed Id </td><td>: ")
					.append(vms.getFedid()).append("</td></tr>").append("<tr><td>Company Type </td><td>: ")
					.append(vms.getCompanytype()).append("</td></tr>").append("<tr><td>Clients </td><td>: ")
					.append(vms.getClient()).append("</td></tr>").append("<tr><td>Staff  </td><td>: ")
					.append(vms.getStaff()).append("</td></tr>").append("<tr><td>Web Site  </td><td>: ")
					.append(vms.getWebsite()).append("</td></tr>").append("<tr><td>Industry </td><td>: ")
					.append(vms.getIndustrytype()).append("</td></tr><tr><td>Linked In </td><td>: ")
					.append(vms.getLinkedinid() + "</td></tr>")
					.append("<tr><td><a href='http://69.216.19.140/usitats/#/list-vendor'>Click Here to Go VMS</a>")
					.append("</td></tr></table>").append("<pre>").append("</pre>").append("</body>").append("<html>")
					.append("<br>").append("<h3>Regards,</h3>").append("<strong>" + username + ",<br>")
					.append(designation + " | |Narvee Tech Inc<br>").append("1333 Corporate Dr, Suite#102,")
					.append("<br>Irving, Texas, 75038<br>").append("Email : " + frommail + "<br>")
					.append("Conatct Number:" + cnumber).append("<br>www.narveetech.com</strong>");

			// http://69.216.19.140/usitats/#/list-vendor
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(stringBuilder.toString(), "text/html");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			mimeMessage.setContent(multipart);
			mailSender.send(mimeMessage);
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}

		logger.info("!!! inside class: EmailService, !! method: requirements mailing send and update");
	}
}
