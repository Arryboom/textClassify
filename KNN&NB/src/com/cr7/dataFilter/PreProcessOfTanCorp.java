package com.cr7.dataFilter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class PreProcessOfTanCorp {

	private static BufferedReader reader;
	private static BufferedWriter writer;
	private static double ratio = 0.1;	//���Լ�����	

	/**
	 * Ԥ����̷�ɲ����ݼ�
	 */
	public static void main(String[] args) throws IOException {
		String category [] = {"�ƾ�","����","����","����", "����",  "�Ƽ�",  "����",
				"�˲�",  "����", "����", "����" ,"����"};
		for(int i=0;i<category.length;i++){
			try {
				System.out.println(category[i]);
				extract(category[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private static void extract(String category) throws Exception {
		Random rnd = new Random();
		File folder = new File("/home/IT/dataMining/̷�ɲ�/TanCorp-12-Txt/"+category);
		File [] files = folder.listFiles();
		for(int i=0;i<files.length && i<300;i++){		//���Խ׶�Ϊ���Ч��û�����ѡ300������
			File f = files[i];
			//1-ratio�ĸ�����Ϊѵ������������ratio�ĸ�����Ϊ���Լ�������
			File of = null;
			if(Double.compare(rnd.nextDouble(), ratio)>0){
				of = new File("./data/train/"+category+f.getName());	
			}else{
				of = new File("./data/test/"+category+f.getName());
			}
			
			reader = new BufferedReader(new FileReader(f));
			writer = new BufferedWriter(new FileWriter(of));
			String line = "";
			while((line = reader.readLine())!=null){
				writer.write(line);
			}
			writer.flush();
		}
	}

}
