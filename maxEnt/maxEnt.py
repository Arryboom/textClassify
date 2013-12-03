# -*- coding: cp936 -*-
'''
��������44084
ѵ���ı�����2725
�����ı�����725
'''
import os
import math
import sys

textNum = 2725
wordNum = 44084
ctgyNum = 12
weight = [[0 for x in range(ctgyNum)] for y in range(wordNum)]
ctgyName = ['�ƾ�','����','����','����','����','�Ƽ�','����','�˲�','����','����','����','����']
words = {}
E_P = [[0.0 for x in range(ctgyNum)] for y in range(wordNum)]
texts = [ 0 for x in range(textNum)]            #���е�ѵ���ı� 
category = [ 0 for x in range(textNum)]         #ÿ���ı���Ӧ�����

def get_ctgy(fname):
        index = {'fi':0,'lo':1,'co':2,'ho':3,'ed':4,'te':5,
                 'ca':6,'ta':7,'sp':8,'he':9,'ar':10,'fu':11}
        return index[fname[:2]]
def is_zero(x):
        return (x-1e-17<0)
        
def updateWeight():
        E_P2 = [[0.0 for x in range(ctgyNum)] for y in range(wordNum)]
        prob = [[0.0 for x in range(ctgyNum)] for y in range(textNum)]
        #����p(���|�ı�)        
        for i in range(textNum):
                zw = 0.0  #��һ������
                for j in range(ctgyNum):
                        tmp = 0.0
                        for (k,v) in texts[i].items():
                                tmp+=weight[k][j]*v
                        tmp = math.exp(tmp)
                        zw+=tmp
                        prob[i][j]=tmp
                for j in range(ctgyNum):
                        if is_zero(zw) : continue
                        prob[i][j]/=zw
        #�õ���p(���|�ı�),����Լ������f��ģ������EP2(f)
        for x in range(textNum):
                ctgy = category[x]
                for (k,v) in texts[x].items():
                        E_P2[k][ctgy] += (prob[x][ctgy]*v)        
        #��������������Ȩ��w
        for i in range(wordNum):
                for j in range(ctgyNum):
                        if is_zero(E_P2[i][j]) |  is_zero(E_P[i][j]) :
                                continue                        
                        weight[i][j] += math.log(E_P[i][j]/E_P2[i][j])        

def modelTest():
        testFiles = os.listdir('F:\\IT\\workspace\\maxEntClassify\\data\\test\\')
        errorCnt = 0
        totalCnt = 0
        matrix = [[0 for x in range(ctgyNum)] for y in range(ctgyNum)]
        for fname in testFiles:
                #wf = {}
                lines = open('F:\\IT\\workspace\\maxEntClassify\\data\\test\\'+fname)
                ctgy = get_ctgy(fname)
                probEst = [0.0 for x in range(ctgyNum)]         #�����ĺ������
                for line in lines:
                        arr = line.split('\t')
                        if not words.has_key(arr[0]) : continue        #���Լ��еĵ��������ѵ������û�г�����ֱ�Ӻ���
                        word_id = words[arr[0]]
                        freq = float(arr[1])
                        for index in range(ctgyNum):
                            probEst[index]+=(weight[word_id][index]*freq)
                ctgyEst = 0
                maxProb = -1
                for index in range(ctgyNum):
                        if probEst[index]>maxProb:
                            ctgyEst = index
                            maxProb = probEst[index]
                totalCnt+=1
                if ctgyEst!=ctgy: errorCnt+=1
                matrix[ctgy][ctgyEst]+=1
                lines.close()
        print "%-5s" % ("���"),
        for i in range(ctgyNum):
            print "%-5s" % (ctgyName[i]),  
        print '\n',
        for i in range(ctgyNum):
            print "%-5s" % (ctgyName[i]), 
            for j in range(ctgyNum):
                print "%-5d" % (matrix[i][j]), 
            print '\n',
        print "�������ı�����:"+str(totalCnt)+"  �ܴ������:"+str(errorCnt)+"  �ܴ�����:"+str(errorCnt/float(totalCnt))
                
def init():
        i = 0
        lines = open('F:\\IT\\workspace\\maxEntClassify\\data\\words.txt').readlines()
        for word in lines:
                word = word.strip()
                words[word] = i
                i+=1
        #����Լ������f�ľ�������EP(f)
        files = os.listdir('F:\\IT\\workspace\\maxEntClassify\\data\\train\\')
        index = 0
        for fname in files:
                wf = {}
                lines = open('F:\\IT\\workspace\\maxEntClassify\\data\\train\\'+fname)
                ctgy = get_ctgy(fname)
                category[index] = ctgy
                for line in lines:
                        arr = line.split('\t')
                        word_id = words[arr[0]]
                        freq = float(arr[1])
                        wf[word_id] = freq
                        E_P[word_id][ctgy]+=freq
                texts[index] = wf
                index+=1
                lines.close()
def train():
        for loop in range(40):
            print "����%d�κ��ģ��Ч����" % loop
            updateWeight()
            modelTest()
    
#ע�⣬��Ҫ������ȷ��textNum��wordNum
if __name__ == '__main__' :
    print "��ʼ��:......"
    init()
    print "��ʼ����ϣ�����Ȩ��ѵ��....."
    train()
