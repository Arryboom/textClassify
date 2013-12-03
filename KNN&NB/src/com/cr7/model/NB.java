package com.cr7.model;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import util.Category;
import util.Util;
	/**
	 * 
	 * 1��p(wi|cj)�ö�ά����,double���� ��ʾ�����10�����50000������ռ���ڴ棺 
	 * 10*50000*4=10M Ҳ���Ǻܶ�
	 * 2�����̫���˻����Կ��ǰ��պ���˳��д�벻ͬ���ļ��У�Ȼ��ģ���н���һ�����ӣ���Ҫ����p(wi|cj)ʱ
	 * �ӳ�����Ѱ�ң��Ҳ�����ȥ�ļ���load�������Ӵﵽ���ƴ�Сʱɾ����������p(wi|cj)����ģ��ֱ��ʹ��
	 * ��ά���顣
	 *
	 */
public class NB implements Model{
	private static int cNum;	//�������
	private static int DNum;	//�ĵ�����
	private static int [] DCNum;//ÿ������ĵ�����
	private static int [] WCNum;//ÿ������ظ����ʸ���
	//p(wi|cj)��ÿ������wi��Ӧһ��map��map�б����˸õ����ڸ�������г��ֵ��ı�������
	//Ȼ�����������ı��õ��������ʡ�
	private static Map<String,Map<Integer,Double>> conProb;	
	private static double prior [];
	//ÿ������wi���������cj���ı��ĸ���
	public NB(){
		init();
	}
	@Override
	public void init() {
		cNum = Category.size();
		DNum=0;
		DCNum = new int [cNum];
		WCNum = new int [cNum];
		conProb = new HashMap<String,Map<Integer,Double>>();
		prior = new double[cNum];
	}

	@Override
	public void train(File [] files) {
		DNum = files.length;
		for(int i=0;i<files.length;i++){
//if(i%100==0 && (i>100 || i==0)) System.out.println(i+"/"+files.length);//�������
			String cateStr = files[i].getName().substring(0,2);
			int categoryIndex = Category.index(cateStr);
			DCNum[categoryIndex]++;
			String [] words = Util.readWordsFromFile(files[i]);
			Set<String> oldWord = new HashSet<String>();
			for(int j=0;j<words.length;j++){
				if(!oldWord.contains(words[j])){	//ÿƪ�����е�ÿ����ֻ�ܴ���һ��
					oldWord.add(words[j]);
					//�ô���conProb�л�û�г��ֹ���Ϊ���Ľ���һ��map,�����������г��ָôʵĴ���
					if(!conProb.containsKey(words[j])){	
						Map<Integer,Double> cMap = new HashMap<Integer,Double>();
						for(int k=0;k<Category.size();k++){
							if(k==categoryIndex) {
								cMap.put(k, 1.0);
								continue;
							}
							
							cMap.put(k, 0.0);
						}
						conProb.put(words[j], cMap);
					}else{
					//�ô���conProb�г��ֹ��ˣ�Ϊ�ôʶ�Ӧ������1.
						Map<Integer,Double> tmp = conProb.get(words[j]);
						tmp.put(categoryIndex, tmp.get(categoryIndex)+1);
					}
				}else{
					//�Ѿ�������õ�����
				}
				
			}
		}
		//����ÿ�������������
		for(int i=0;i<Category.size();i++){
			prior[i] = ((double)(DCNum[i]+1))/(DNum+cNum);
		}
		//����ÿ�������ֲ�ͬ�ʵĸ���������laplaceƽ����
		for(Entry<String,Map<Integer,Double>> entry : conProb.entrySet()){
			Map<Integer,Double> tmp = conProb.get(entry.getKey());
			for(Entry<Integer,Double> e : tmp.entrySet()){
				if(e.getValue()>0) WCNum[e.getKey()]++;
			}
		}
		//�ѳ��ִ���ת������������
		for(Entry<String,Map<Integer,Double>> entry : conProb.entrySet()){
			Map<Integer,Double> tmp = conProb.get(entry.getKey());
			for(Entry<Integer,Double> e : tmp.entrySet()){
				double condition = ((double)(e.getValue()+1))/(DCNum[e.getKey()]+WCNum[e.getKey()]);
				tmp.put(e.getKey(), condition);
			}
		}
		
		//��ʾ�м���
//		System.out.println("total num of text"+DNum);
//		for(int i=0;i<Category.size();i++){
//			System.out.println(Category.getCategory(i)+" num :"+DCNum[i]);
//		}
//		for(Entry<String,Map<Integer,Double>> entry : conProb.entrySet()){
//			Map<Integer,Double> tmp = conProb.get(entry.getKey());
//			for(Entry<Integer,Double> e : tmp.entrySet()){
//				if(e.getValue()>20) System.out.println("word "+ entry.getKey()+" exists in " + e.getValue()+" texts of category "+Category.getCategory(e.getKey()));
//			}
//		}
//		for(Entry<String,Map<Integer,Double>> entry : conProb.entrySet()){
//			Map<Integer,Double> tmp = conProb.get(entry.getKey());
//			for(Entry<Integer,Double> e : tmp.entrySet()){
//				if(e.getValue()>0.001) System.out.println("p("+entry.getKey()+"|"+Category.getCategory(e.getKey())+")= "+e.getValue());
//			}
//		}
//		
//		for(int i=0;i<Category.size();i++){
//			System.out.println("���"+Category.getCategory(i)+"�������Ϊ�� "+prior[i]);
//		}
		
	}
	
	@Override
	public String predict(File file) {
		String [] words = Util.readWordsFromFile(file);
		Set<String> set = new HashSet<String>();
		for(int i=0;i<words.length;i++){
			set.add(words[i]);
		}
		BigDecimal [] post = new BigDecimal[cNum];
		for(int i=0;i<post.length;i++){
			post[i] = new BigDecimal(prior[i]);
		}
		Iterator<String> itr = set.iterator();
		while(itr.hasNext()){
			String word = itr.next();
			for(int i=0;i<post.length;i++){
				//���������ѵ������û�г��ֹ�,�����������
				if(!conProb.containsKey(word)) continue;
				post[i]=post[i].multiply(new BigDecimal(conProb.get(word).get(i)));
			}
		}
		BigDecimal max = new BigDecimal(0.0);
		int result = 0;
		for(int i=0;i<post.length;i++){
			if(post[i].compareTo(max)>0){
				max = post[i];
				result = i;
			}
		}
		
		return Category.getCategory(result);
	}

}

