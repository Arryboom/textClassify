package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

public class computeDF {

	/**
	 * ����DFʱҪ��Ҫ�Ѳ��Լ�����������
	 * �����������Ԥ��ʱ������DF��û�еĴ���ô��
	 */
	
	private static BufferedReader reader;
	private static BufferedWriter writer;
	private static Random rnd = new Random();
	public static void main(String[] args) throws Exception{
		File [] files = new File("./data/train/").listFiles();
		System.out.println("�ĵ������� "+files.length);
		String [] words;
		Map<String,Integer> df = new TreeMap<String,Integer>();
		for(int i=0;i<files.length;i++){
			if(Double.compare(rnd.nextDouble(), 0.9)>0) System.out.println(i+"/"+files.length);  //�������
			File f = files[i];
			reader = new BufferedReader(new FileReader(f));
			words = reader.readLine().split(" ");
			Set<String> exists = new HashSet<String>();	//����DFʱ��ÿ������ÿ���ĵ��������һ��
			for(int j=0;j<words.length;j++){
				String word = words[j];
				if(exists.contains(word)) continue;	//�Ѿ������
				else exists.add(word);
				if(df.containsKey(word)){
					df.put(word,df.get(word)+1);
				}else{
					df.put(word, 1);
				}
			}
		}
		writer = new BufferedWriter(new FileWriter("./data/DF"));
		for(Entry<String, Integer> entry : df.entrySet()){
			writer.write(entry.getKey()+":"+entry.getValue()+"\n");
			writer.flush();	//��flush������
		}

	}

}
