package com.narvee.usit.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.io.*;

public class STringTest {

	public static void main(String[] args) {
//		List duplicateList=(List) Arrays.asList("Android","Android","Windows","iOS","Beta","Java","Java","iOs","Normal");
//		Set s1=new TreeSet(String.CASE_INSENSITIVE_ORDER);
//		s1.addAll(duplicateList);
//		System.out.println(duplicateList);
//		System.out.println(s1);

		String[] str = new String[] { "saikiran@narveetech.com ", "kiranjava010@gmail.com " };
		str[1]="kk";
		//str[0] = "kk";
		for (String string : str) {
			System.out.println(str.length);

		}
	}
}
