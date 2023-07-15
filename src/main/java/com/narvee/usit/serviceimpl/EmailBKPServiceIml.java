package com.narvee.usit.serviceimpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SentDateTerm;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.narvee.usit.dto.ListExtractMailDTO;
import com.narvee.usit.entity.ExtractEmail;
import com.narvee.usit.repository.IEmailExtractRepository;
import com.narvee.usit.entity.EmailAttachment;
import com.narvee.usit.entity.Recruiter;
import com.narvee.usit.entity.Users;
import com.narvee.usit.entity.VendorDetails;
import com.narvee.usit.repository.IEmailAttachmentRepository;
import com.narvee.usit.repository.IRecruiterRepository;
import com.narvee.usit.repository.IVendorRepository;
import com.narvee.usit.service.IEmailBKPService;

@Transactional
@Service
public class EmailBKPServiceIml implements IEmailBKPService {
	private static final Logger logger = LoggerFactory.getLogger(EmailBKPServiceIml.class);
	@Autowired
	private IEmailExtractRepository repo;
	@Autowired
	private IEmailAttachmentRepository repository;

	@Autowired
	private IVendorRepository vendorRepo;

	@Autowired
	private IRecruiterRepository recruiterRepo;

	@Value("${file.upload-dir}")
	private String saveDirectory;

	@Value("${file.upload-dir}")
	private String filesPath;

	@Autowired
	private IEmailAttachmentRepository filerepository;

	@Override
	public boolean deleteAllByIdInBatch(List<Long> ids) {
		repo.deleteAllById(ids);
		// repo.deletemailById(ids);
		return true;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<ExtractEmail> findAllMailDataWithAttachment() {
		return repo.findAll(Sort.by("receiveddate").descending());
	}

	@Override
	public boolean moveibMailstoDB(List<Long> ids) {
		repo.moveIBtoDB(ids);
		return false;
	}

	@Override
	public List<ListExtractMailDTO> listAll() {
		return repo.listAll();
	}

	@Override
	public List<ListExtractMailDTO> dailyRequirement() {
		return repo.dailyRequirement();
	}

	@Override
	public Optional<ExtractEmail> getmailbyid(long id) {
		return repo.findById(id);
	}

	public static Date dateModfy(Date date, int hour, int subelements) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, subelements);
		cal.set(Calendar.SECOND, subelements);
		cal.set(Calendar.MILLISECOND, subelements);
		return cal.getTime();
	}

	@Override
	public String mailExtraction(String host, String port, String userName, String password, Date fromDate, Date toDate,
			long userid) {
 
		
		Properties properties = new Properties();
		properties.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getDefaultInstance(properties);
	

		// Session session = Session.getDefaultInstance(properties);
		List<ExtractEmail> listOfEntity = new ArrayList<>();
		ExtractEmail entity = null;
		List<ExtractEmail> newemail = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		Date ToDate = dateModfy(toDate, 23, 59);
		Date FromDate = dateModfy(fromDate, 0, 0);
		try { // main try block
			Store store = session.getStore("imaps");
			
			try {
				logger.info("checking credentials");
				store.connect("smtp.gmail.com", userName,password);
				
			} catch (AuthenticationFailedException e) {
				logger.info(" Authentication error mailExtraction in service Impl ");
				return "Authentication Error";
			}
			Folder folderInbox = store.getFolder("INBOX");
			folderInbox.open(Folder.READ_ONLY);

			Calendar cal2 = null;
			cal2 = Calendar.getInstance();
			Date minDate = new Date(cal.getTimeInMillis());
			cal2.add(Calendar.DAY_OF_MONTH, 1); // add 1 day
			Date maxDate = new Date(cal2.getTimeInMillis()); // get tomorrow date
			SearchTerm search = new AndTerm(new SentDateTerm(ComparisonTerm.GE, fromDate),
					new SentDateTerm(ComparisonTerm.LE, toDate));
			Message arrayMessages[] = folderInbox.search(search);
			for (Message messageRef : arrayMessages) {// for loop for looping mails

				if (messageRef.getSentDate().after(FromDate) && messageRef.getSentDate().before(ToDate)) {
					// checking the mails between dates closed
					int count = arrayMessages.length;
					entity = new ExtractEmail();
					Date receivedDate = messageRef.getReceivedDate();
					if (receivedDate.after(ToDate)) {
						logger.info("Completed between dates", receivedDate);
						return "Success";
					}
					if (receivedDate.after(FromDate) && receivedDate.before(ToDate)) {
						// reading mails in between dates
						Address[] fromAdd = messageRef.getFrom();
						String frommail = fromAdd[0].toString();
						// removing extra lines to get the specific mail address (ex <kiran@gmail.com>
						// will remove <,> o/p like kiran@gmail.com
						// reading from mail address
						if (frommail.equalsIgnoreCase("mailman-bounces@narveetech.com")
								|| frommail.equalsIgnoreCase("sep-bounces@narveetech.com")
								|| frommail.equalsIgnoreCase("p2s-owner@narveetech.com")
								|| frommail.equalsIgnoreCase("nuslist-owner@narveetech.com")
								|| frommail.equalsIgnoreCase("gg-owner@narveetech.com")
								|| frommail.equalsIgnoreCase("r2s-owner@narveetech.com")
								|| frommail.equalsIgnoreCase("sep-owner@narveetech.com")
								|| frommail.equalsIgnoreCase("mat12-owner@narveetech.com")
								|| frommail.equalsIgnoreCase("sinlist-owner@narveetech.com")
								|| frommail.equalsIgnoreCase("nuslist-owner@narveetech.com")
								|| frommail.equalsIgnoreCase("klist-owner@narveetech.com")) {

						} else {

							if (frommail.contains("<")) {
								String detectingMail = frommail.substring(frommail.indexOf("<") + 1,
										frommail.indexOf(">", frommail.indexOf(">")));
								entity.setFrommail(detectingMail);
								try {
									String result = detectingMail.substring(detectingMail.indexOf("@") + 1,
											detectingMail.indexOf("."));
									if (result.equalsIgnoreCase("gmail")) {
										entity.setCompany(detectingMail);
									} else {
										entity.setCompany(result);
									}
								} catch (StringIndexOutOfBoundsException e3) {
									logger.info(" mailExtraction in service Impl StringIndexOutOfBoundsException");
								}
							} else {
								entity.setFrommail(frommail);
								try {
									String result = frommail.substring(frommail.indexOf("@") + 1,
											frommail.indexOf("."));
									if (result.contains("gmail")) {
										entity.setCompany(frommail);
									} else {
										entity.setCompany(result);
									}
								} catch (StringIndexOutOfBoundsException e4) {
									logger.info(" mailExtraction in service Impl StringIndexOutOfBoundsException");
								}
							}

							// reading from mail address closed
							// reading to mail address\
							Address[] tomailAdd = messageRef.getRecipients(Message.RecipientType.TO);
							String finalTO = "";
							try {
								for (int j = 0; j < tomailAdd.length; j++) {
									String Tomail = tomailAdd[j].toString();
									if (Tomail.contains("<")) {
										String value = Tomail.substring(Tomail.indexOf("<") + 1,
												Tomail.indexOf(">", Tomail.indexOf(">")));
										value = value.concat(", ");
										finalTO = finalTO.concat(value);
									} else {
										Tomail = Tomail.concat(",");
										finalTO = finalTO.concat(Tomail);
									}
								}
								if (finalTO != "" && finalTO != null && finalTO.length() != 0) {
									finalTO = finalTO.substring(0, finalTO.length() - 1);
								}
								entity.setTomail(finalTO);
							} catch (NullPointerException e1) {
							}

							// reading to mail address closed
							// reading ccmail address
							Address[] ccmail = messageRef.getRecipients(Message.RecipientType.CC);
							String finalCC = "";
							try {
								for (int j = 0; j < ccmail.length; j++) {
									String fromCC = ccmail[j].toString();
									if (fromCC.contains("<")) {
										String value = fromCC.substring(fromCC.indexOf("<") + 1,
												fromCC.indexOf(">", fromCC.indexOf(">")));
										value = value.concat(", ");
										finalCC = finalCC.concat(value);
									} else {
										fromCC = fromCC.concat(",");
										finalCC = finalCC.concat(fromCC);
									}
								}
								if (finalCC != "" && finalCC != null && finalCC.length() != 0) {
									finalCC = finalCC.substring(0, finalCC.length() - 1);
								}
								entity.setCcmail(finalCC);
							} catch (NullPointerException e1) {
							}

							// actual logic to read message content
							String subject = messageRef.getSubject();
							Date receivedDat = messageRef.getReceivedDate();
							Date sentDate = messageRef.getSentDate();
							entity.setReceiveddate(receivedDat);
							entity.setSentdate(sentDate);
							String contentType = messageRef.getContentType();
							String messageContent = "";
							// checking the mail content type
							if (contentType.contains("multipart")) {
								try {// inner try
									Multipart multiPart = (Multipart) messageRef.getContent();
									int numberOfParts = multiPart.getCount();
									// inner for loop
									for (int partCount = 0; partCount < numberOfParts; partCount++) {
										MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
										if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {

											// this part is attachment
											String fileName = part.getFileName();
											part.saveFile(saveDirectory + File.separator + fileName);
											List<EmailAttachment> attachments = new ArrayList<>();
											EmailAttachment email = new EmailAttachment();
											email.setFilename(fileName);
											attachments.add(email);
											for (EmailAttachment attachment : attachments) {
												attachment.setExtractEmail(entity);
												 repository.save(email);
											}
										} else if (part.isMimeType("multipart/*")) {
											MimeMultipart mimeMultipart = (MimeMultipart) messageRef.getContent();
											messageContent = getTextFromMimeMultipart(mimeMultipart);
											entity.setBody(messageContent);
										} else {
											messageContent = part.getContent().toString();
											if (messageContent.contains("BASE64DecoderStream")
													|| messageContent.contains("IMAPNestedMessage")) {
												MimeMultipart mimeMultipart = (MimeMultipart) messageRef.getContent();
												messageContent = getTextFromMimeMultipart(mimeMultipart);
												entity.setBody(Jsoup.parse(messageContent).wholeText());
											} else {
												entity.setBody(messageContent);
											}
										}

									} // inner for loop closed

								} catch (IOException e) {
									e.printStackTrace();
								} // inner try closed

							} else if (contentType.contains("TEXT/HTML")) {
								try {
									messageContent = (String) messageRef.getContent();
									entity.setBody(messageContent);
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								try {
									messageContent = (String) messageRef.getContent();
									entity.setBody(messageContent);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}

							// calling methods
							entity.setSubject(subject);
							entity.setUserid(userid);
							listOfEntity.add(entity);
						} // else for nuslist
							// checking the mail content type
					} // reading mails in between dates closed kiran

				} // checking the mails between dates closed

			} // for loop for looping mails closed
		    saveVendorInfo(listOfEntity);
			logger.info("Calling save method");
			logger.info("End save method");
			logger.info(" mailExtraction in service Impl ");
			folderInbox.close(false);
			store.close();
		} // main try block closed
		catch (NoSuchProviderException ex) {
			// System.out.println("No provider for pop3.");
			ex.printStackTrace();
			return "error1";
		} catch (MessagingException ex) {
			// System.out.println("Could not connect to the message store");
			ex.printStackTrace();
			return "error2";
		}
		return "Success";
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveVendorInfo(List<ExtractEmail> listOfEntity) {
		List<ExtractEmail> email = removeDuplicates(listOfEntity);
		long id = 0;
		for (ListIterator<ExtractEmail> it = email.listIterator(); it.hasNext();) {
			ExtractEmail value = it.next();
			String vendormail = value.getFrommail();
			long userid = value.getUserid();
			Users user = new Users();
			user.setUserid(userid);
			// checking recruiter exists in DB or Not
			List<Recruiter> recruiter = recruiterRepo.findByEmail(vendormail);
			if (recruiter == null) {
				String result = vendormail;
				try {
					result = vendormail.substring(vendormail.lastIndexOf("@") + 1);
				} catch (StringIndexOutOfBoundsException e) {
				}

				String recname = result;
				try {
					recname = vendormail.substring(0, vendormail.indexOf('@'));
				} catch (StringIndexOutOfBoundsException e) {
				}
				if (result.equalsIgnoreCase("gmail.com")) {

				} else {
					List<Recruiter> findByEmailLike = recruiterRepo.searchByTitleLike(result);
					if (findByEmailLike.isEmpty()) {
						VendorDetails vms = new VendorDetails();
						String vendor = vendormail;

						try {
							vendor = vendormail.substring(vendormail.indexOf("@") + 1, vendormail.indexOf("."));
						} catch (StringIndexOutOfBoundsException e) {
						}
						vms.setCompany(vendor);
						vms.setCompanytype("Both");
						vms.setHeadquerter("N/A");
						vms.setUser(user);
						VendorDetails vd = vendorRepo.save(vms);
						// to save recruiter info
						Recruiter recrEntity = new Recruiter();
						recrEntity.setRecruiter(recname);
						recrEntity.setEmail(vendormail);
						recrEntity.setVendor(vd);
						recrEntity.setRecruitertype("Both");
						recrEntity.setUser(user);
						//List<Recruiter> dupcheck = recruiterRepo.findByEmail(vendormail);
						recruiterRepo.save(recrEntity);

					} else {
						for (Recruiter recruiter2 : findByEmailLike) {
							id = recruiter2.getVendor().getVmsid();
						}
						VendorDetails vms = new VendorDetails();
						vms.setVmsid(id);
						Recruiter recrEntity = new Recruiter();
						recrEntity.setEmail(vendormail);
						recrEntity.setVendor(vms);
						recrEntity.setRecruitertype("Both");
						recrEntity.setUser(user);
						recrEntity.setRecruiter(recname);
						recruiterRepo.save(recrEntity);
					}
				}
			}
		}

		int batchSize = 3; // Set batch size no.of mail's to be read & insert
		// for (int i = 0; i < listOfEntity.size(); i += batchSize) {
		// List<ExtractEmail> batchStudents = listOfEntity.subList(i, Math.min(i +
		// batchSize, listOfEntity.size()));
//			try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		// saveEntity(listOfEntity); // kiran

		// }
		List<ExtractEmail> uniqueStudents = removeSubjectDuplicates(listOfEntity);
		saveEntity(uniqueStudents); // kiran
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveEntity(List<ExtractEmail> email) {
		logger.info(" saveEntity in service Impl ");
		List<ExtractEmail> uniqueStudents = new ArrayList();
		email.forEach(entity -> {
			long userid = entity.getUserid();
			Users user = new Users();
			user.setUserid(userid);
			List<ExtractEmail> findBySubject = repo.findBySubject(entity.getSubject());
			if (repo.existsBySubject(entity.getSubject())) {
				uniqueStudents.remove(entity);
			} else {
				uniqueStudents.add(entity);
			}
		});
		repo.saveAllAndFlush(uniqueStudents);
	}

	private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
		String result = "";
		int count = mimeMultipart.getCount();
		for (int i = 0; i < count; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			if (bodyPart.isMimeType("text/plain")) {
				result = result + "\n" + bodyPart.getContent();
				break; // without break same text appears twice in my tests
			} else if (bodyPart.isMimeType("text/html")) {
				String html = (String) bodyPart.getContent();
				result = result + "\n" + html;
				// result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
			} else if (bodyPart.getContent() instanceof MimeMultipart) {
				result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
			}
		}
		return result;
	}

	public static <ExtractEmail> List<ExtractEmail> removeDuplicates(List<ExtractEmail> list) {
		List<ExtractEmail> distinctElements = list.stream()
				.filter(distinctByKey(cust -> ((com.narvee.usit.entity.ExtractEmail) cust).getFrommail()))
				//.filter(distinctByKey(cust -> ((com.narvee.usit.entity.ExtractEmail) cust).getSubject()))
				.collect(Collectors.toList());
		// return the new list
		return distinctElements;
	}

	public static <ExtractEmail> List<ExtractEmail> removeSubjectDuplicates(List<ExtractEmail> list) {
		List<ExtractEmail> distinctElements = list.stream()
				.filter(distinctByKey(cust -> ((com.narvee.usit.entity.ExtractEmail) cust).getSubject()))
				.collect(Collectors.toList());
		// return the new list
		return distinctElements;
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> uniqueMap = new ConcurrentHashMap<>();
		return t -> uniqueMap.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	@Override
	public Resource download(long id) throws FileNotFoundException {
		EmailAttachment model = filerepository.findById(id)
				.orElseThrow(() -> new FileNotFoundException("File does not exist" + id));
		// String filesPath = "D:/stores2/";
		String filename = model.getFilename(); // System.out.println(filename);
		try {
			Path file = Paths.get(filesPath).resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

}
