package util;

import java.util.HashMap;
import java.util.Map;

public  class Category {
	private static final Map<String,Integer> category;
	private static String [] name = {"�ƾ�","����","����","����","����",
		"�Ƽ�","����","�˲�","����","����","����","����"};;
	private static int N=12;
	static{
		category = new HashMap<String,Integer>();
		category.put("�ƾ�", 0);
		category.put("����", 1);
		category.put("����", 2);
		category.put("����", 3);
		category.put("����", 4);
		category.put("�Ƽ�", 5);
		category.put("����", 6);
		category.put("�˲�", 7);
		category.put("����", 8);
		category.put("����", 9);
		category.put("����", 10);
		category.put("����", 11);
	}
	
	public static int index(String str){
		return category.get(str);
	}
	public static String getCategory(int i){
		if(i>=name.length){System.out.println("����Խ��");return "";}
		return name[i];
	}
	public static int size(){
		return N;
	}
}
