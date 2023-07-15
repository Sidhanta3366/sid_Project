package com.narvee.usit.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DropdownVO {
	private Long 				id;
	private String 				name;
	private String 				type;
	
	public DropdownVO(Long id, String name) {
		this.id = id;
		this.name = name;
		
	}
	
}
