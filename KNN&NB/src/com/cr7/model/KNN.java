package com.cr7.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import util.Category;
import util.GenarateVSM;
import util.computeDF;

/**
 * ���ʣ�test�ı��г�����ѵ������û�еĴ���ô�죿
 * ���ʣ�treeset��ô����value����
 * @author wcc
 *
 */
public class KNN implements Model{
	private int K=20;		//�ھӸ���
	
	private static double getSimilarity(double sigmaW,Map<String,Double> vsm1,Map<String,Double> vsm2){
		//vsm1(�����ı�)��Ȩֵƽ�����ظ������ˡ�������
		double nominator = 0;
		double sigmaW2 = 0;
		for(Entry<String,Double> entry : vsm2.entrySet()){
			String word = entry.getKey();
			double weight = entry.getValue();
			sigmaW2+=Math.pow(weight, 2);
			if(vsm1.containsKey(word)) nominator+=weight*vsm1.get(word);
		}
		if(Double.compare(nominator, 0.0)==0) return 0.0;
		return nominator/(sigmaW*Math.sqrt(sigmaW2));
	}

	//���㵥���ļ�Ƶ��
	@Override
	public void init() {
		File f = new File("./data/DF");
		if(f.exists())  System.out.println("�������ļ�Ƶ���Ѿ�����");
		else{
			new computeDF();
			System.out.println("�������ļ�Ƶ�ʼ������");
		}
	}


	@Override
	public void train(File [] files) {
		//KNN ����Ҫ����ѵ��
	}


	/**
	 * TreeMap<String name,String age>Ҫ���������Сѡ������ǰK���˵�����Ӧ����ô������
	 * treeMapֻ�ܰ�key���򣬵���ageȴ���ظ���������Ϊkey����
	 * 
	 * ���͵�������������ÿ�γ���һ����ageʱ���伸����������kС��age����ʱ���Ժ�map��ageС�ڸ�ֵ��
	 * ֱ��remove,������ָ�С��age�����滻��ǰ��Сageֵ���������������ƻ�map�ṹ��������Ҫ��ǰ
	 * ����һ��map��
	 */
	@Override
	public String predict(File file) {
		String result  = "";
		try {
			double [] category= new double[Category.size()];
			Map<String,Double> KNeigh = findNeighbour(file);
			//����ÿ�����÷�
			for(Map.Entry<String, Double> entry :KNeigh.entrySet()){
				category[Category.index(entry.getKey().substring(0,2))]+=entry.getValue();
			}
			double max = 0.0;
			//ѡ��÷����������Ϊ���
			for(int i=0;i<category.length;i++){
				if(category[i]>max){
					max = category[i];
					result = Category.getCategory(i);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println("predict result "+result);
		return result;
	}
	
	private Map<String,Double> findNeighbour(File testFile) throws IOException{
		GenarateVSM generator = GenarateVSM.getInstance();
		Map<String,Double> neigh = new HashMap<String,Double>();
		
		Map<String,Double> vsm = generator.getVSM(testFile);
		double sigmaW=0;
		for(double w : vsm.values()){
			sigmaW+=Math.pow(w, 2);
		}
		sigmaW=Math.sqrt(sigmaW);
		File [] files = new File("./data/train/").listFiles();
		for(int i=0;i<files.length;i++){
			File neighFile = files[i];
			double tmp = getSimilarity(sigmaW,vsm,generator.getVSM(neighFile));
			neigh.put(neighFile.getName(),tmp);
		}
		//ֻ����K���ھ�
		List<Map.Entry<String,Double>> al = new ArrayList<Map.Entry<String,Double>>(neigh.entrySet());
		Collections.sort(al,new Comparator<Map.Entry<String,Double>>(){
			public int compare(Map.Entry<String,Double> m1,Map.Entry<String,Double>m2){
				return (m2.getValue()-m1.getValue()>0)?1:-1;
			}
		});
		Map<String,Double> KNeigh = new HashMap<String,Double>();
		for(int i=0;i<al.size() && i<K;i++){
			KNeigh.put(al.get(i).getKey(), al.get(i).getValue());
		}
		return KNeigh;
	}
	
	
	

}
