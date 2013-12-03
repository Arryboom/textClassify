package com.cr7.evaluate;

import java.io.File;

import util.Category;

import com.cr7.model.Model;

public class Evaluator {
	static int [][]  matrix; 
	/*
	 * @param     files ��Ԥ����ı�
     * @param	  m	����Ԥ���ģ��
     * @param 	  progress  �Ƿ��ӡ����
     * @exception IndexOutOfBoundsException if the <code>index</code>
	 */
	public static void evaluate(File [] files,Model m,boolean progress){
		int cNum = Category.size();
		matrix = new int[cNum][cNum];
		for(int i=0;i<files.length;i++){
			if(progress) System.out.println(i+"/"+files.length);
			String categoryT = files[i].getName().substring(0,2);
			String categoryP = m.predict(files[i]);
			matrix[Category.index(categoryT)][Category.index(categoryP)]++;
		}
		toMatrixString();
		getPrecision();
		
	}
	
	public static void getPrecision(){
		int cNum = Category.size();
		double [] rNum = new double[cNum];	//ÿ�������ȷ�������
		double [] fNum = new double[cNum];	//ÿ��������������
		double totalRight = 0;
		double totalWrong = 0;
		for(int i=0;i<matrix.length;i++){
			for(int j=0;j<matrix[0].length;j++){
				if(j==i) {rNum[i]+=matrix[i][j];totalRight+=matrix[i][j];}
				else {fNum[i]+=matrix[i][j];totalWrong+=matrix[i][j];}
			}
		}
		System.out.println("����׼ȷ�ʣ�"+((double)totalRight/(totalRight+totalWrong)));
		for(int i=0;i<matrix.length;i++){
			System.out.println("���"+Category.getCategory(i)+"׼ȷ�ʣ�"+((double)rNum[i]/(rNum[i]+fNum[i])));
		}
	}
	
	public static void toMatrixString(){
		//��ӡ������
		System.out.print("���\t");
		for(int i=0;i<matrix.length;i++){
			System.out.print(Category.getCategory(i)+"\t");
		}
		System.out.println();
		//��ӡ��������
		for(int i=0;i<matrix.length;i++){
			System.out.print(Category.getCategory(i)+"\t");
			for(int j=0;j<matrix[0].length;j++){
				System.out.print(matrix[i][j]+"\t");
			}
			System.out.println("");
		}
	}
}
