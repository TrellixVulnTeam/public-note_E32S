#include <iostream>
#include <cv.h>
#include <opencv.hpp>
#include <opencv2\core\core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2\ml\ml.hpp>

using namespace cv;
using namespace std;

/*
Mat M( rows, cols, type, s ), ����rows colsֱ��ָ���������������typeָ��������ÿ��Ԫ�ص����ͣ�sΪÿ��Ԫ�صĳ�ʼֵ
Mat M( ndims, size, type, s), ����ndimsָ��ά�ȣ�sizeָ��
Mat A = M;	����ֻ�´������ָ�룬���ҽ������ü�������û�б���ʹ�þ���ʱ�����ͷž���ռ�
Mat A = M.clone(); �����Ż´�������������ݡ�

type: CV_[M][S/U]C( n ), ÿ��Ԫ��Ϊn�����ݣ�ÿ��������Mbit��S/U
M����ÿ�����ݵ�bit��.
S/U�����з��Ż����޷���.
n����ÿ��Ԫ�ص�ͨ����.(ÿ��Ԫ�ؿ������ɶ������)
*/

void test1(void)
{
	Mat M(5, 5, CV_8UC(2), Scalar(125, 1));
	cout << "M = " << endl << " " << M << endl << endl; 
}

void test2(void)
{
	Mat M1 = Mat::eye(4, 4, CV_64F);
	Mat M2 = Mat::ones(4, 4, CV_64F);
	Mat M3 = Mat::zeros(4, 4, CV_64F);
	cout << "M1 = " << endl << " " << M1 << endl << endl;
	cout << "M2 = " << endl << " " << M2 << endl << endl;
	cout << "M3 = " << endl << " " << M3 << endl << endl;
}

/*
�洢��ʽ:
		col0	col1	col2	...	colN-1
row0 c0 c1 c2|c0 c1 c2|c0 c1 c2|...
row1 c0 c1 c2|...
row2
...
rowN-1
M.ptr<type>(row),	���ص�row����Ԫ�ص�ָ��
M.data,				���ص�0�е���Ԫ��ָ��
*/
void test3(void)
{
	Mat M = Mat::eye(3, 3, CV_8UC(2));
	cout << "M = " << endl << " " << M << endl << endl;

	int channels = M.channels();
	int nRows = M.rows; 
    int nCols = M.cols * channels;
	
	cout<<M.isContinuous()<<endl;
	cout<<channels<<endl;
	cout<<nRows<<endl;
	cout<<nCols<<endl;

	for(int row=0; row<nRows; row++)
	{
		uchar *p = M.ptr<uchar>(row);
		for(int col=0; col<nCols; col++)
		{
			cout<<(int)p[col]<<" ";
		}
		cout<<endl;
	}
}

void test4(void)
{
	Mat M = Mat::eye(3, 3, CV_8UC(2));
	cout << "M = " << endl << " " << M << endl << endl;


	cout<<"channel 1"<<endl;
	for(int row=0; row<M.rows; row++)
	{
		for(int col=0; col<M.cols; col++)
		{
			uchar value = M.at<Vec2b>(row, col)[0];
			cout<<int(value)<<" ";
		}
		cout<<endl;
	}
	cout<<"channel 2"<<endl;
	for(int row=0; row<M.rows; row++)
	{
		for(int col=0; col<M.cols; col++)
		{
			uchar value = M.at<Vec2b>(row, col)[1];
			cout<<int(value)<<" ";
		}
		cout<<endl;
	}
}

/***************************
M.rowRange(b, e)
ʹ�õķ�Χ��ʵ��[b, e)
****************************/
void test5(void)
{
	Mat M = Mat::eye(5, 5, CV_8UC(1));
	Mat A = M.row(1);					//A�ľ����ַָ��õ�M��ĳһ�еĵ�ַ
	Mat B = M.row(1).clone();
	Mat C = M.rowRange(0, 1).clone();
	A.ptr<uchar>(0)[0] = 2;				//M��A�������ǹ����
	B.ptr<uchar>(0)[0] = 3;				//M��B�������Ƕ�����
	cout << "M = " << endl << " " << M << endl << endl;
	cout << "A = " << endl << " " << A << endl << endl;
	cout << "B = " << endl << " " << B << endl << endl;
	cout << "C = " << endl << " " << C << endl << endl;
}


/*
TermCriteria ��������ֹ����
TermCriteria(type, max_ite, elip);
max_ite�������� elip��������������һ������ֹ��
*/
void test6(void)
{
	int width = 512, height = 512;
	Mat image = Mat::zeros(height, width, CV_8UC3);
	float labels[] = {	 1.0, 1.0, 1.0, 1.0, 1.0,
						-1.0,-1.0,-1.0,-1.0,-1.0};
	float trainingData[][2] = { {10, 300},  {50, 255},  {90, 200},   {130, 289}, {170, 280},
								{500, 10},  {490, 255}, {480, 501},  {470, 10},  {460, 255}};
	int trainSize = sizeof(labels)/sizeof(float);

	Mat labelsMat(trainSize, 1, CV_32FC1, labels);			//ʹ��labels����labelsMat�ĳ�ʼ��
	Mat trainingDataMat(trainSize, 2, CV_32FC1, trainingData);

	CvSVMParams params;
	params.svm_type = SVM::C_SVC;                 //SVM����  
	params.kernel_type = CvSVM::LINEAR;             //�˺���������
	params.term_crit = cvTermCriteria(CV_TERMCRIT_ITER, 100, FLT_EPSILON );

	CvSVM svm;
	svm.train(trainingDataMat, labelsMat, Mat(), Mat(), params);

	Vec3b green(0, 255, 0), blue(255, 0, 0);
	for(int i=0; i<image.rows; i++)
	{
		for(int j=0; j<image.cols; j++)
		{
			Mat sampleMat = (Mat_<float>(1,2)<<i, j);
			float response = svm.predict(sampleMat);

			if(response==1)
				image.at<Vec3b>(j, i) = green;
			else
				image.at<Vec3b>(j, i) = blue;
		}
	}

	 // Show the training data
    int thickness = -1;
    int lineType = 8;
	for(int i=0; i<trainSize; i++)
	{
		if(i<5)		circle( image, Point(trainingData[i][0],  trainingData[i][1]), 5, Scalar(  0,   0,   0), thickness, lineType);
		else		circle( image, Point(trainingData[i][0],  trainingData[i][1]), 5, Scalar(  255,   255,   255), thickness, lineType);
	}

    // Show support vectors
    thickness = 2;
    lineType  = 8;
    int c     = svm.get_support_vector_count();

    for (int i = 0; i < c; ++i)
    {
        const float* v = svm.get_support_vector(i);
        circle( image,  Point( (int) v[0], (int) v[1]),   6,  Scalar(128, 128, 128), thickness, lineType);
    }

    imshow("SVM Simple Example", image); // show it to the user
	waitKey(0);
}

void test7(void)
{
	static int NTRAINING_SAMPLES = 200;
	static float FRAC_LINEAR_SEP = 0.3f;
	const int WIDTH = 512, HEIGHT = 512;
    Mat I = Mat::zeros(HEIGHT, WIDTH, CV_8UC3);

	Mat trainData(2*NTRAINING_SAMPLES, 2, CV_32FC1);
	Mat labels(2*NTRAINING_SAMPLES, 1, CV_32FC1);
	RNG rng(100);		//�����������

	int nLinearSamples = (int) (FRAC_LINEAR_SEP * NTRAINING_SAMPLES);		//���Կɷֵ���������

	//���������Կɷ�����
	Mat trainClass1 = trainData.rowRange(0, nLinearSamples);										//��1����, nLinearSamples��
	Mat trainClass2 = trainData.rowRange(2*NTRAINING_SAMPLES-nLinearSamples, 2*NTRAINING_SAMPLES);	//��2������nLinearSamples��

	Mat c;
	c = trainClass1.colRange(0, 1);rng.fill(c, RNG::UNIFORM, Scalar(1), Scalar(0.4*WIDTH));		//x���귶Χ, [0, 0.4)
	c = trainClass1.colRange(1, 2);rng.fill(c, RNG::UNIFORM, Scalar(1), Scalar(HEIGHT));		//y���귶Χ, [0, 1)

	c = trainClass2.colRange(0, 1);rng.fill(c, RNG::UNIFORM, Scalar(0.6*WIDTH), Scalar(WIDTH));	//x���귶Χ, [0.6, 1)
	c = trainClass2.colRange(1, 2);rng.fill(c, RNG::UNIFORM, Scalar(1), Scalar(HEIGHT));		//y���귶Χ, [0, 1)

	//���������Բ��ɷֵ�����...
	Mat trainClass = trainData.rowRange( nLinearSamples, 2*NTRAINING_SAMPLES-nLinearSamples);
	c = trainClass.colRange(0, 1);rng.fill(c, RNG::UNIFORM, Scalar(0.4*WIDTH), Scalar(0.6*WIDTH));
	c = trainClass.colRange(1, 2);rng.fill(c, RNG::UNIFORM, Scalar(1), Scalar(HEIGHT));

	 //------------------------- Set up the labels for the classes ---------------------------------
    labels.rowRange(                0,   NTRAINING_SAMPLES).setTo(1);  // Class 1
    labels.rowRange(NTRAINING_SAMPLES, 2*NTRAINING_SAMPLES).setTo(2);  // Class 2

    //------------------------ 2. Set up the support vector machines parameters --------------------
    CvSVMParams params;
    params.svm_type    = CvSVM::C_SVC;
    params.C           = 100.0;
	params.kernel_type = SVM::LINEAR;
    params.term_crit   = TermCriteria(CV_TERMCRIT_ITER, (int)1e7, 1e-7);

    //------------------------ 3. Train the svm ----------------------------------------------------
    cout << "Starting training process" << endl;
    CvSVM svm;
    svm.train(trainData, labels, Mat(), Mat(), params);
    cout << "Finished training process" << endl;

	Vec3b green(0,100,0), blue (100,0,0);
    for (int i = 0; i < I.rows; ++i)
	{
        for (int j = 0; j < I.cols; ++j)
        {
            Mat sampleMat = (Mat_<float>(1,2) << i, j);
            float response = svm.predict(sampleMat);

            if      (response == 1)    I.at<Vec3b>(j, i)  = green;
            else if (response == 2)    I.at<Vec3b>(j, i)  = blue;
        }
	}


	int thick = -1;
    int lineType = 8;
    float px, py;
    // Class 1
    for (int i = 0; i < NTRAINING_SAMPLES; ++i)
    {
        px = trainData.at<float>(i,0);
        py = trainData.at<float>(i,1);
        circle(I, Point( (int) px,  (int) py ), 3, Scalar(0, 255, 0), thick, lineType);
    }
    // Class 2
    for (int i = NTRAINING_SAMPLES; i <2*NTRAINING_SAMPLES; ++i)
    {
        px = trainData.at<float>(i,0);
        py = trainData.at<float>(i,1);
        circle(I, Point( (int) px, (int) py ), 3, Scalar(255, 0, 0), thick, lineType);
    }

	//------------------------- 6. Show support vectors --------------------------------------------
    thick = 2;
    lineType  = 8;
    int x     = svm.get_support_vector_count();

    for (int i = 0; i < x; ++i)
    {
        const float* v = svm.get_support_vector(i);
        circle( I,  Point( (int) v[0], (int) v[1]), 6, Scalar(128, 128, 128), thick, lineType);
    }

	imshow("SVM Simple Example", I); // show it to the user
	waitKey(0);
}

void test8(void)
{
	Mat img = imread("D://lena.jpg");
	imshow("image", img);
	cvWaitKey(0);
}

int main()
{
	test8();
	while(1);
}