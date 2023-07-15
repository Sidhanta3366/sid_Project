package com.narvee.usit.service;

import org.springframework.web.multipart.MultipartFile;

public interface IfileStorageService {

	public String storeFile(MultipartFile file, String mobile, String type,String name);

	public String storemultiplefiles(MultipartFile file, String locname);

}
