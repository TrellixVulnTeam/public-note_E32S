#include "test_ model.h"
#include "gaussian.h"

using namespace cv;
using namespace std;

/*
Mat::at(i��,j��)=value;		�Ǹ�i��j�����ݸ�ֵ�� ���߿���������������ͼ������ϵ��Ϊ(y, x),��(0, 0)��ʼ.
Point(x,y)�����ɵ���ͼ������ϵ�е�λ��(x, y)
*/

int main(void)
{
//	srand(time(NULL));
	srand(0);
	Gaussian randg(0, 50);
	
	// 1).��ʼ��ϵͳ(��������ռ䣬ѵ����������Ӧ��ǩ)
	int width = 512, height = 512;
	Mat space = Mat::zeros(height, width, CV_8UC3);
	float trains[trainSize][2];
	float labels[trainSize];

	for(int i=0; i<trainSize; i++)
	{
		int clabel = (i<trainSize/2) ? 1 : -1;

		trains[i][0] = (clabel==1?classCenter1[0]:classCenter2[0]) + randg.gen_one();
		trains[i][1] = (clabel==1?classCenter1[1]:classCenter2[1]) + randg.gen_one();

		for(int j=0; j<2; j++)
		{
			trains[i][j] = trains[i][j]<0 ? 0 : trains[i][j];
			trains[i][j] = trains[i][j]>(width-1) ? (width-1) : trains[i][j];
		}

		labels[i] = clabel;

		if(clabel == 1)
			circle( space, Point(trains[i][0],  trains[i][1]), 3, Scalar(0,   0,   255), thickness, lineType);
		else
			circle( space, Point(trains[i][0],  trains[i][1]), 3, Scalar(0,   255,   0), thickness, lineType);
	}

	// 2).����ģ�Ͳ�������
	test_bayes(space, trains, labels);
	waitKey(0);

	return 0;
}